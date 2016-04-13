package asv.exercise.repository;

import asv.exercise.domain.Pointofsale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Pointofsale entity.
 */
public interface PointofsaleRepository extends JpaRepository< Pointofsale, Long > {

    Page<Pointofsale> findAllByActivatedIsTrue( Pageable pageable);

}
