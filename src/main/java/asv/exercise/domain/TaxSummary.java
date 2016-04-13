package asv.exercise.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Tax Summary entity
 */
@ApiModel(description = ""
    + "Tax Summary entity")
@Entity
@Table(name = "tax_summary")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaxSummary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "taxbase", precision=10, scale=2)
    private BigDecimal taxbase;

    @Column(name = "taxrate")
    private Float taxrate;

    @Column(name = "taxamount", precision=10, scale=2)
    private BigDecimal taxamount;

    @Column(name = "totalamount", precision=10, scale=2)
    private BigDecimal totalamount;

    @ManyToOne
    @JoinColumn(name = "tax_id")
    private Tax tax;

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

    public BigDecimal getTaxbase() {
        return taxbase;
    }

    public void setTaxbase(BigDecimal taxbase) {
        this.taxbase = taxbase.setScale(2, RoundingMode.HALF_UP);
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
        this.taxamount = taxamount.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(BigDecimal totalamount) {
        this.totalamount = totalamount.setScale(2, RoundingMode.HALF_UP);
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
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
        TaxSummary taxSummary = (TaxSummary) o;
        return Objects.equals(id, taxSummary.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TaxSummary{" +
            "id=" + id +
            ", taxbase='" + taxbase + "'" +
            ", taxrate='" + taxrate + "'" +
            ", taxamount='" + taxamount + "'" +
            ", totalamount='" + totalamount + "'" +
            '}';
    }
}
