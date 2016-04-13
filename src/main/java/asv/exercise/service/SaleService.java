package asv.exercise.service;

import asv.exercise.domain.*;
import asv.exercise.domain.builders.CatalogConfig;
import asv.exercise.domain.builders.ItemConfig;
import asv.exercise.repository.*;
import asv.exercise.security.SecurityUtils;
import asv.exercise.service.util.UpcDatabaseConnectorService;
import asv.exercise.service.util.strategies.payment.PaymentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by javier on 22/01/16.
 */
@Service
@Transactional
public class SaleService {

    private final Logger log = LoggerFactory.getLogger( SaleService.class );

    @Inject
    private UpcDatabaseConnectorService upcDatabaseConnectorService;

    @Inject
    private CatalogRepository catalogRepository;

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private SaleRepository saleRepository;

    @Inject
    private TaxSummaryRepository taxSummaryRepository;

    @Inject
    private PaymentMethodRepository paymentmethodRepository;

    @Inject
    private TurnRepository turnRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * Adds a new {@link Item} to {@link Sale} with {@code <qty>} units of an existent {@link Product}
     * in {@link Shop} {@link Catalog} finding by {@code barcode}.
     * <br/>
     * Uses {@link #makeTaxSummaryCalculations(Sale)} function to make temporary {@link TaxSummary} calculations
     * in-memory without persistence.
     *
     * @param saleId  The id of {@link Sale} to attach the {@link Item}
     * @param barcode The barcode to find in {@link Shop} {@link Catalog}
     * @param qty     The quantity sold. Can be positve or negative
     * @return {@link Optional} with updated Sale if success or empty {@link Optional} if barcode does not
     * exists in {@link Catalog}
     */
    public Optional< Sale > addBarcodeToSale( Long saleId, String barcode, Long qty ) throws ExecutionException, InterruptedException, TimeoutException {

        Optional< Sale > updatedSale = Optional.empty();

        Sale sale = saleRepository.findOne( saleId );
        if ( sale == null )
            return updatedSale;

        Long shopId = sale.getShopId();

        Optional< Catalog > catalog = catalogRepository.findOneByShopIdAndBarcode( shopId, barcode );

        if ( !catalog.isPresent() ) {

            // Search barcode in external API and automatic creation in owr DB
            Optional< Product > newProduct = upcDatabaseConnectorService.searchAPIAndCreateNewProduct( barcode );

            if ( newProduct.isPresent() ) {
                // Random Prince for new Catalog's record
                BigDecimal newPrice;
                newPrice = new BigDecimal( Math.random() * 5D );
                newPrice = newPrice.setScale( 2, RoundingMode.CEILING );

                CatalogConfig catalogConfig = new CatalogConfig();
                Catalog newCatalog = catalogConfig
                        .setBarcode( barcode )
                        .setProduct( newProduct.get() )
                        .setShop( sale.getShop() )
                        .setInventory( 1000L )
                        .setPrice( newPrice )
                        .getCatalog();

                catalog = Optional.ofNullable( catalogRepository.save( newCatalog ) );
            }

        }

        Optional< Item > newItem =
                catalog.map( existingCatalog -> {

                    log.debug( "Barcode {} is for catalog: {}", barcode, existingCatalog );

                    // Use ItemConfig to configure new Item
                    ItemConfig itemConfig = new ItemConfig();

                    return itemConfig
                            .setSale( sale )
                            .setCatalogAndQuantity( existingCatalog, qty )
                            .getItem();

                } );

        if ( newItem.isPresent() ) {
            // Add Item to sale's list
            sale.getItems().add( newItem.get() );

            // Generate TaxSummary in-memory for calculations (not persisted here!)
            List< TaxSummary > summaryList = makeTaxSummaryCalculations( sale );

            // Calc Total Amounts from TaxSummary and update Sale
            BigDecimal sum = new BigDecimal( 0 );
            for ( TaxSummary taxSummary : summaryList ) {
                sum = sum.add( taxSummary.getTotalamount() );
            }
            sale.setTotalamount( sum );

            itemRepository.save( newItem.get() );
            Sale saleSaved = saleRepository.save( sale );
            updatedSale = Optional.ofNullable( saleSaved );
        }

        return updatedSale;
    }

    /**
     * Does Tax calculations and apply to Sale without persistence
     *
     * @param saleId
     * @return
     */
    public Optional< Sale > calculateTaxSummary( Long saleId ) {

        // Return empty if Sale does not exists
        Sale sale = saleRepository.findOne( saleId );
        if ( sale == null )
            return Optional.empty();

        // Return empty if TaxSummaryList is empty
        List< TaxSummary > taxSummaryList = makeTaxSummaryCalculations( sale );
        if ( taxSummaryList.size() == 0 )
            return Optional.empty();

        // Apply in-memory Tax calculations to Sale
        HashSet< TaxSummary > taxSummaryHashSet = new HashSet<>();
        taxSummaryHashSet.addAll( taxSummaryList );
        sale.setTaxSummarys( taxSummaryHashSet );

        return Optional.of( sale );
    }

    /**
     * Makes temporary {@link TaxSummary} in-memory calculations attached to a {@link Sale}
     * to avoid lost of precision.
     * <br/>
     * Data is grouped by {@link Tax}.
     * <br/>
     * Records does not persist in database because is used every time an {@link Item} is
     * attached to the {@link Sale}.
     *
     * @param sale The Sale with {@link Item} list to iterate
     * @return List of {@link TaxSummary} records with {@code base}, {@code tax} and {@code total}
     * calculated for every different {@link Tax} used in {@link Sale}
     */
    private List< TaxSummary > makeTaxSummaryCalculations( Sale sale ) {

        // TODO: Llevarmelo a una nueva Clase TaxSumaryServices

        // Accumulate data in a map
        Map< Tax, BigDecimal > map = new HashMap<>();
        Tax tax;
        BigDecimal totalBase;

        for ( Item item : sale.getItems() ) {

            tax = item.getTax();

            totalBase = item.getBaseamount();
            if ( map.get( tax ) != null ) {
                totalBase = map.get( tax ).add( item.getBaseamount() );
            }

            map.put( tax, totalBase );

        }

        // Generate TaxSummary calculations in-memory
        List< TaxSummary > summaryList = new ArrayList<>();

        map.forEach( ( _tax, _totalBase ) -> {
            TaxSummary taxSummary = new TaxSummary();

            taxSummary.setId( summaryList.size() + 1L );

            taxSummary.setSale( sale );
            taxSummary.setTax( _tax );

            taxSummary.setTaxrate( _tax.getRate() );
            taxSummary.setTaxbase( _totalBase );

            taxSummary.setTaxamount(
                    taxSummary.getTaxbase()
                            .multiply( new BigDecimal( _tax.getRate() ) )
                            .divide( new BigDecimal( "100" ), 2, BigDecimal.ROUND_HALF_UP )
            );

            taxSummary.setTotalamount( taxSummary.getTaxbase().add( taxSummary.getTaxamount() ) );

            summaryList.add( taxSummary );
        } );

        return summaryList;

    }

    public List< TaxSummary > persistTaxSummaryCalculations( Set< TaxSummary > taxSummarySet ) {

        List< TaxSummary > taxSummaryList = new ArrayList<>( taxSummarySet );

        // Clean Ids assigned by makeTaxSummaryCalculations()
        for ( TaxSummary taxSummary : taxSummaryList ) {
            taxSummary.setId( null );
        }

        // Save in db to assign real Ids
        return ( taxSummaryRepository.save( taxSummaryList ) );

    }

    /**
     * Updates a {@link Sale} with {@code payedamount} and {@code paymentmethod}.
     * <br/>
     * Returns an empty Optional if can't find these entities before update.
     *
     * @param saleId        the id of Sale
     * @param payedamount   amount payed by customer
     * @param paymentmethod payment method used by customer
     * @return Optional of {@link Sale} or empty
     */
    public Optional< Sale > paySale( Long saleId, BigDecimal payedamount, String paymentmethod ) {

        // Return empty if Sale does not exists
        Sale sale = saleRepository.findOne( saleId );
        if ( sale == null )
            return Optional.empty();

        // Return empty if can't find Payment Method
        Optional< PaymentMethod > paymentMethod = paymentmethodRepository.findByDescriptionIgnoreCaseContaining( paymentmethod );
        if ( !paymentMethod.isPresent() )
            return Optional.empty();

        String registerResult;

        try {

            // Strategy Pattern
            PaymentContext paymentContext = new PaymentContext();
            paymentContext.setPaymentStrategy( paymentMethod.get().getId() );
            registerResult = paymentContext.register();

            log.debug( registerResult );

        } catch ( Exception e ) {
            return Optional.empty();
        }

        if ( registerResult.contains( "ERROR" ) ) {
            return Optional.empty();
        }

        // Removes previous Tax Summary records if exist someone
        if ( taxSummaryRepository.countBySaleId( saleId ) > 0 ) {
            if ( taxSummaryRepository.removeBySaleId( saleId ) == 0 ) {

                log.error( "Existent TaxSummary registers attached to Sale {} has not been deleted before new calculations", saleId );
                return Optional.empty();

            }
        }

        Optional< Sale > optSale = calculateTaxSummary( saleId );

        if ( optSale.isPresent() ) {
            // Save TaxSummaryList
            List< TaxSummary > taxSummaryList = persistTaxSummaryCalculations( optSale.get().getTaxSummarys() );

            if ( taxSummaryList != null ) {
                // Update and Save the Sale
                sale = optSale.get();
                sale.setTaxSummarys( new HashSet<>( taxSummaryList ) );
                sale.setPayedamount( payedamount );
                sale.setPaymentmethod( paymentMethod.get() );
                sale.setPaymentauth( registerResult );
                sale.setCompleted( true );
                sale.setTime( ZonedDateTime.now() );
                sale = saleRepository.save( sale );
            }
        }

        return Optional.ofNullable( sale );
    }

    /**
     * Find the uncompleted Sale associated to an active Turn (User + Point Of Sale).
     * <br/>
     * If exists, this one is returned. If not, creates a new one that is returned.
     * <br/>
     * If can't identify active User by Login or active Turn of this User, then returns empty.
     * <br/>
     *
     * @return Optional of {@link Sale} or empty
     */
    public Optional< Sale > findLastOrCreateNewUncompletedSale() {

        String currentUserLogin = SecurityUtils.getCurrentUserLogin();

        Optional< User > currentUser = userRepository.findOneByLogin( currentUserLogin );
        if ( !currentUser.isPresent() ) {
            return Optional.empty();
        }

        Optional< Turn > currentTurn = turnRepository.findOneByCashierIdAndActivatedIsTrue( currentUser.get().getId() );
        if ( !currentTurn.isPresent() ) {
            return Optional.empty();
        }

        // Update Turn info
        if ( currentTurn.get().getStarted() == null ) {
            currentTurn.get().setStarted( ZonedDateTime.now() );
        }
        currentTurn.get().setEnded( ZonedDateTime.now() );
        turnRepository.save( currentTurn.get() );

        // Search for a previous uncompleted Sale or Create a new one
        Optional< Sale > sale = saleRepository.findOneByTurnIdAndCompletedIsFalse( currentTurn.get().getId() );
        if ( !sale.isPresent() ) {

            // Initialize and Creates a new Sale
            Sale newSale = new Sale();
            newSale.setTurn( currentTurn.get() );
            newSale.setCompleted( false );

            sale = Optional.ofNullable( saleRepository.save( newSale ) );

        }

        return sale;

    }
}
