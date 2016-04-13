package asv.exercise.repository;

import asv.exercise.domain.Shop;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Shop entity.
 */
public interface ShopRepository extends JpaRepository<Shop,Long> {

}
