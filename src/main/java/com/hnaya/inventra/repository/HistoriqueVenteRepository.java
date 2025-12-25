package com.hnaya.inventra.repository;


import com.hnaya.inventra.entity.HistoriqueVente;
import com.hnaya.inventra.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface HistoriqueVenteRepository extends JpaRepository<HistoriqueVente, Long> {

    List<HistoriqueVente> findByProductIdAndWarehouseIdAndDateVenteAfter(Long productId, Long warehouseId, LocalDateTime dateDebut);
    List<HistoriqueVente> findByWarehouseId(Long warehouseId);

    @Query("SELECT h FROM HistoriqueVente h WHERE h.product.id = :productId " + "AND h.warehouse.id = :warehouseId " + "AND h.dateVente >= :dateDebut " + "ORDER BY h.dateVente DESC")
    List<HistoriqueVente> findHistoriqueForPrevision(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId, @Param("dateDebut") LocalDateTime dateDebut);
}