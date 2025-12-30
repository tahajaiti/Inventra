package com.hnaya.inventra.service.impl;

import com.hnaya.inventra.dto.request.HistoriqueVenteRequestDTO;
import com.hnaya.inventra.dto.response.HistoriqueVenteResponseDTO;
import com.hnaya.inventra.entity.HistoriqueVente;
import com.hnaya.inventra.entity.Stock;
import com.hnaya.inventra.exception.ResourceNotFoundException;
import com.hnaya.inventra.exception.StockNotFoundException;
import com.hnaya.inventra.mapper.HistoriqueVenteMapper;
import com.hnaya.inventra.repository.HistoriqueVenteRepository;
import com.hnaya.inventra.repository.StockRepository;
import com.hnaya.inventra.service.HistoriqueVenteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoriqueVenteServiceImpl implements HistoriqueVenteService {

    private final HistoriqueVenteRepository historiqueRepository;
    private final StockRepository stockRepository;
    private final HistoriqueVenteMapper historiqueMapper;

    @Override
    @Transactional
    public HistoriqueVenteResponseDTO enregistrerVente(HistoriqueVenteRequestDTO dto) {
        Stock stock = stockRepository.findByProductIdAndWarehouseId(dto.getProductId(), dto.getWarehouseId())
                .orElseThrow(() -> new StockNotFoundException("Stock non trouvé pour ce produit dans cet entrepôt"));

        if (stock.getQuantiteDisponible() < dto.getQuantiteVendue()) {
            throw new StockNotFoundException("Stock insuffisant pour réaliser la vente");
        }

        stock.setQuantiteDisponible(stock.getQuantiteDisponible() - dto.getQuantiteVendue());
        stockRepository.save(stock);

        HistoriqueVente vente = historiqueMapper.toEntity(dto);
        vente.setJourSemaine(dto.getDateVente().getDayOfWeek().name());
        vente.setMois(dto.getDateVente().getMonthValue());
        vente.setAnnee(dto.getDateVente().getYear());


        HistoriqueVente savedVente = historiqueRepository.save(vente);

        return historiqueMapper.toResponseDto(savedVente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueVenteResponseDTO> findByWarehouse(Long warehouseId) {
        return historiqueRepository.findByWarehouseId(warehouseId).stream()
                .map(historiqueMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueVenteResponseDTO> findAll() {
        return historiqueRepository.findAll().stream()
                .map(historiqueMapper::toResponseDto)
                .toList();
    }
}