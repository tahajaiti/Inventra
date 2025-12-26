package com.hnaya.inventra.service.impl;

import com.hnaya.inventra.dto.response.PrevisionResponseDTO;
import com.hnaya.inventra.entity.HistoriqueVente;
import com.hnaya.inventra.entity.Prevision;
import com.hnaya.inventra.entity.Product;
import com.hnaya.inventra.entity.Stock;
import com.hnaya.inventra.entity.Warehouse;
import com.hnaya.inventra.exception.ResourceNotFoundException;
import com.hnaya.inventra.mapper.PrevisionMapper;
import com.hnaya.inventra.repository.*;
import com.hnaya.inventra.service.PrevisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PrevisionServiceImpl implements PrevisionService {

    private final PrevisionRepository previsionRepository;
    private final HistoriqueVenteRepository historiqueVenteRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final PrevisionMapper previsionMapper;
    private final GeminiService geminiService;

    @Override
    public PrevisionResponseDTO genererPrevision(Long productId, Long warehouseId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, productId));

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(Warehouse.class, warehouseId));

        Stock stock = stockRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(Stock.class,
                        "productId: " + productId + ", warehouseId: " + warehouseId));

        double prevision = calculerPrevision30Jours(productId, warehouseId);
        double confiance = calculerNiveauConfiance(productId, warehouseId);
        int quantiteCommande = calculerQuantiteACommander(prevision, stock);

        String message = geminiService.genererMessageRecommandation(product.getNom(), stock.getQuantiteDisponible(),
                prevision, quantiteCommande, confiance);

        Prevision previsionEntity = Prevision.builder()
                .product(product)
                .warehouse(warehouse)
                .datePrevision(LocalDate.now())
                .quantitePrevue30Jours(prevision)
                .niveauConfiance(confiance)
                .recommandation(message)
                .build();

        Prevision saved = previsionRepository.save(previsionEntity);

        PrevisionResponseDTO dto = previsionMapper.toResponseDTO(saved);
        dto.setQuantiteACommander(quantiteCommande);
        dto.setStockActuel(stock.getQuantiteDisponible());
        dto.setSeuilAlerte(stock.getSeuilAlerte());

        return dto;
    }

    private double calculerPrevision30Jours(Long productId, Long warehouseId) {
        LocalDateTime date60JoursAvant = LocalDateTime.now().minusDays(60);
        List<HistoriqueVente> ventes = historiqueVenteRepository.findHistoriqueForPrevision(productId, warehouseId,
                date60JoursAvant);

        if (ventes.isEmpty()) {
            return 0.0;
        }

        double moyenne = ventes.stream()
                .limit(30)
                .mapToInt(HistoriqueVente::getQuantiteVendue)
                .average()
                .orElse(0.0);

        return moyenne * 30;
    }

    private double calculerNiveauConfiance(Long productId, Long warehouseId) {
        LocalDateTime date30JoursAvant = LocalDateTime.now().minusDays(30);
        List<HistoriqueVente> ventes = historiqueVenteRepository.findHistoriqueForPrevision(productId, warehouseId,
                date30JoursAvant);

        if (ventes.size() < 7) {
            return 30.0;
        }

        double moyenne = ventes.stream()
                .mapToInt(HistoriqueVente::getQuantiteVendue)
                .average()
                .orElse(0.0);

        if (moyenne == 0) {
            return 30.0;
        }

        double variance = ventes.stream()
                .mapToDouble(v -> Math.pow(v.getQuantiteVendue() - moyenne, 2))
                .average()
                .orElse(0.0);

        double ecartType = Math.sqrt(variance);
        double confiance = 100 - ((ecartType / moyenne) * 100);

        return Math.clamp(confiance, 20, 99);
    }

    private int calculerQuantiteACommander(double prevision, Stock stock) {
        int besoin = (int) Math.ceil(prevision - stock.getQuantiteDisponible());

        if (besoin <= 0) {
            return 0;
        }

        int marge = (int) Math.ceil(besoin * 0.10);
        int total = besoin + marge;

        if (stock.getQuantiteDisponible() < stock.getSeuilAlerte()) {
            int min = stock.getSeuilAlerte() - stock.getQuantiteDisponible();
            return Math.max(total, min);
        }

        return total;
    }

    @Override
    public List<PrevisionResponseDTO> getPrevisionsByWarehouse(Long warehouseId) {
        return previsionRepository.findByWarehouseId(warehouseId).stream()
                .map(previsionMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PrevisionResponseDTO getPrevisionById(Long id) {
        Prevision prevision = previsionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Prevision.class, id));
        return previsionMapper.toResponseDTO(prevision);
    }
}
