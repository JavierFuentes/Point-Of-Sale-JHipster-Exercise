package asv.exercise.repository;

import asv.exercise.domain.TaxSummary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the TaxSummary entity.
 */
public interface TaxSummaryRepository extends JpaRepository< TaxSummary, Long > {

    Long countBySaleId( Long saleId );

    Long removeBySaleId( Long saleId );

}
