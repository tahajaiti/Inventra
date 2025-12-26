package com.hnaya.inventra.repository;

import com.hnaya.inventra.entity.Prevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrevisionRepository extends JpaRepository<Prevision, Long> {

    List<Prevision> findByWarehouseId(Long warehouseId);

    Optional<Prevision> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
}
