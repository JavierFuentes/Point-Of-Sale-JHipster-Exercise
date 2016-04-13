package asv.exercise.repository;

import asv.exercise.domain.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the PaymentMethod entity.
 */
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Long> {

    Optional<PaymentMethod> findByDescriptionIgnoreCaseContaining( String description );

}
