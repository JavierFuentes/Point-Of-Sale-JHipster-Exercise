package asv.exercise.repository;

import asv.exercise.domain.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Catalog entity.
 */
public interface CatalogRepository extends JpaRepository<Catalog,Long> {

    Optional<Catalog> findOneByShopIdAndBarcode( Long shopId, String barcode );

}
