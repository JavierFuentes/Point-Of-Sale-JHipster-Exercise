package asv.exercise.domain;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Turn entity
 */
@ApiModel(description = ""
    + "Turn entity")
@Entity
@Table(name = "turn")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Turn implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "started")
    private ZonedDateTime started;

    @Column(name = "ended")
    private ZonedDateTime ended;

    @ManyToOne
    @JoinColumn(name = "pointofsale_id")
    private Pointofsale pointofsale;

    @OneToMany(mappedBy = "turn")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sale> sales = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "cashier_id")
    private User cashier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public ZonedDateTime getStarted() {
        return started;
    }

    public void setStarted(ZonedDateTime started) {
        this.started = started;
    }

    public ZonedDateTime getEnded() {
        return ended;
    }

    public void setEnded(ZonedDateTime ended) {
        this.ended = ended;
    }

    public Pointofsale getPointofsale() {
        return pointofsale;
    }

    public void setPointofsale(Pointofsale pointofsale) {
        this.pointofsale = pointofsale;
    }

    public Set<Sale> getSales() {
        return sales;
    }

    public void setSales(Set<Sale> sales) {
        this.sales = sales;
    }

    public User getCashier() {
        return cashier;
    }

    public void setCashier(User user) {
        this.cashier = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Turn turn = (Turn) o;
        return Objects.equals(id, turn.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Turn{" +
            "id=" + id +
            ", activated='" + activated + "'" +
            ", started='" + started + "'" +
            ", ended='" + ended + "'" +
            '}';
    }
}
