package asv.exercise.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Sale entity
 */
@ApiModel( description = ""
        + "Sale entity" )
@Entity
@Table( name = "sale" )
@Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public class Sale implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @Column( name = "completed" )
    private Boolean completed;

    @Column( name = "time" )
    private ZonedDateTime time;

    @Column( name = "totalamount", precision = 10, scale = 2 )
    private BigDecimal totalamount;

    @Column( name = "payedamount", precision = 10, scale = 2 )
    private BigDecimal payedamount;

    @ManyToOne
    @JoinColumn( name = "turn_id" )
    private Turn turn;

    @ManyToOne
    @JoinColumn( name = "paymentmethod_id" )
    private PaymentMethod paymentmethod;

    @OneToMany( mappedBy = "sale", fetch = FetchType.EAGER )
//    @JsonIgnore
    @Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
//    @JsonManagedReference
    private Set< TaxSummary > taxSummarys = new HashSet<>();

    @OneToMany( mappedBy = "sale", fetch = FetchType.EAGER )
//    @JsonIgnore
    @Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
//    @JsonManagedReference
    private Set< Item > Items = new HashSet<>();

    @Column( name = "paymentauth")
    private String paymentauth;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted( Boolean completed ) {
        this.completed = completed;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime( ZonedDateTime time ) {
        this.time = time;
    }

    public BigDecimal getTotalamount() {
        return totalamount;
    }

    public void setTotalamount( BigDecimal totalamount ) {
        this.totalamount = totalamount.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPayedamount() {
        return payedamount;
    }

    public void setPayedamount( BigDecimal payedamount ) {
        this.payedamount = payedamount.setScale(2, RoundingMode.HALF_UP);
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn( Turn turn ) {
        this.turn = turn;
    }

    public PaymentMethod getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod( PaymentMethod paymentMethod ) {
        this.paymentmethod = paymentMethod;
    }

    public Set< TaxSummary > getTaxSummarys() {
        return taxSummarys;
    }

    public void setTaxSummarys( Set< TaxSummary > taxSummarys ) {
        this.taxSummarys = taxSummarys;
    }

    public Set< Item > getItems() {
        return Items;
    }

    public void setItems( Set< Item > items ) {
        Items = items;
    }

    public String getPaymentauth() {
        return paymentauth;
    }

    public void setPaymentauth( String paymentauth ) {
        this.paymentauth = paymentauth;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Sale sale = (Sale) o;
        return Objects.equals( id, sale.id );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( id );
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", completed='" + completed + "'" +
                ", time='" + time + "'" +
                ", totalamount='" + totalamount + "'" +
                ", payedamount='" + payedamount + "'" +
                ", paymentauth='" + paymentauth + "'" +
                '}';
    }

    @JsonIgnore
    public Shop getShop() {
        try {
            return this.getTurn().getPointofsale().getShop();
        } catch ( Exception e ) {
            return null;
        }
    }

    @JsonIgnore
    public Long getShopId() {
        try {
            return this.getTurn().getPointofsale().getShop().getId();
        } catch ( Exception e ) {
            return null;
        }
    }

}
