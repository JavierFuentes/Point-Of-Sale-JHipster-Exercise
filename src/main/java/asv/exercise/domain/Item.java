package asv.exercise.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Item entity
 */
@ApiModel(description = ""
    + "Item entity")
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price", precision=10, scale=3)
    private BigDecimal price;

    @Column(name = "discountpct")
    private Float discountpct;

    @Column(name = "discountamount", precision=10, scale=3)
    private BigDecimal discountamount;

    @Column(name = "baseamount", precision=10, scale=3)
    private BigDecimal baseamount;

    @Column(name = "taxrate")
    private Float taxrate;

    @Column(name = "taxamount", precision=10, scale=3)
    private BigDecimal taxamount;

    @Column(name = "totalamount", precision=10, scale=3)
    private BigDecimal totalamount;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @ManyToOne
    @JoinColumn(name = "tax_id")
    private Tax tax;

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    @JsonIgnore
    private Catalog catalog;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    @JsonBackReference
    private Sale sale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(3, RoundingMode.HALF_UP);
    }

    public Float getDiscountpct() {
        return discountpct;
    }

    public void setDiscountpct(Float discountpct) {
        this.discountpct = discountpct;
    }

    public BigDecimal getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(BigDecimal discountamount) {
        this.discountamount = discountamount.setScale(3, RoundingMode.HALF_UP);
    }

    public BigDecimal getBaseamount() {
        return baseamount;
    }

    public void setBaseamount(BigDecimal baseamount) {
        this.baseamount = baseamount.setScale(3, RoundingMode.HALF_UP);
    }

    public Float getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(Float taxrate) {
        this.taxrate = taxrate;
    }

    public BigDecimal getTaxamount() {
        return taxamount;
    }

    public void setTaxamount(BigDecimal taxamount) {
        this.taxamount = taxamount.setScale(3, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(BigDecimal totalamount) {
        this.totalamount = totalamount.setScale(3, RoundingMode.HALF_UP);
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount( Discount discount) {
        this.discount = discount;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale( Sale sale ) {
        this.sale = sale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Item{" +
            "id=" + id +
            ", barcode='" + barcode + "'" +
            ", description='" + description + "'" +
            ", quantity='" + quantity + "'" +
            ", price='" + price + "'" +
            ", discountpct='" + discountpct + "'" +
            ", discountamount='" + discountamount + "'" +
            ", baseamount='" + baseamount + "'" +
            ", taxrate='" + taxrate + "'" +
            ", taxamount='" + taxamount + "'" +
            ", totalamount='" + totalamount + "'" +
            '}';
    }

}
