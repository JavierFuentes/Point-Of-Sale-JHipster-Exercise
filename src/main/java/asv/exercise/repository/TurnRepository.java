package asv.exercise.repository;

import asv.exercise.domain.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Turn entity.
 */
public interface TurnRepository extends JpaRepository<Turn,Long> {

    @Query("select turn from Turn turn where turn.cashier.login = ?#{principal.username}")
    List<Turn> findByCashierIsCurrentUser();

    Optional<Turn> findOneByCashierIdAndActivatedIsTrue( Long cashierId);

}
