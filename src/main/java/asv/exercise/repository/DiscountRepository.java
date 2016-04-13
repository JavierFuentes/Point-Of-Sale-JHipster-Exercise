package asv.exercise.repository;

import asv.exercise.domain.Discount;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Discount entity.
 */
public interface DiscountRepository extends JpaRepository<Discount,Long> {

}
