package asv.exercise.repository;

import asv.exercise.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Sale entity.
 */
public interface SaleRepository extends JpaRepository<Sale,Long> {

    Optional<Sale> findOneByTurnIdAndCompletedIsFalse(Long turnId);

}
