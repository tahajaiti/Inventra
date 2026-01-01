package com.hnaya.inventra.service.impl;

import com.hnaya.inventra.dto.response.PrevisionResponseDTO;
import com.hnaya.inventra.entity.HistoriqueVente;
import com.hnaya.inventra.entity.Prevision;
import com.hnaya.inventra.entity.Product;
import com.hnaya.inventra.entity.Stock;
import com.hnaya.inventra.entity.Warehouse;
import com.hnaya.inventra.entity.enums.Role;
import com.hnaya.inventra.mapper.PrevisionMapper;
import com.hnaya.inventra.repository.*;
import com.hnaya.inventra.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrevisionServiceImplTest {

    @Mock
    private PrevisionRepository previsionRepository;
    @Mock
    private HistoriqueVenteRepository historiqueVenteRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private WarehouseRepository warehouseRepository;
    @Mock
    private PrevisionMapper previsionMapper;
    @Mock
    private GeminiService geminiService;

    @InjectMocks
    private PrevisionServiceImpl previsionService;

    private UserPrincipal adminPrincipal;

    @BeforeEach
    void setUp() {
        adminPrincipal = new UserPrincipal(
                1L, "admin", "password", "Admin", "User", "admin@test.com",
                Role.ADMIN, true, null
        );
    }

    @Test
    void genererPrevision_AvecDonneesValides_RetournePrevisionComplete() {
        Long productId = 1L;
        Long warehouseId = 1L;

        Product product = Product.builder().id(productId).nom("Ordinateur HP").build();
        Warehouse warehouse = Warehouse.builder().id(warehouseId).name("Paris").build();
        Stock stock = Stock.builder().quantiteDisponible(100).seuilAlerte(50).build();

        List<HistoriqueVente> ventes = creerHistoriqueVentes(30, 100);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));
        when(stockRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.of(stock));
        when(historiqueVenteRepository.findHistoriqueForPrevision(anyLong(), anyLong(), any()))
                .thenReturn(ventes);
        when(geminiService.genererMessageRecommandation(anyString(), anyInt(), anyDouble(), anyInt(), anyDouble()))
                .thenReturn("Commander 150 unités");

        Prevision savedPrevision = Prevision.builder()
                .id(1L)
                .product(product)
                .warehouse(warehouse)
                .datePrevision(LocalDate.now())
                .quantitePrevue30Jours(3000.0)
                .niveauConfiance(85.0)
                .recommandation("Commander 150 unités")
                .build();

        when(previsionRepository.save(any(Prevision.class))).thenReturn(savedPrevision);

        PrevisionResponseDTO expectedDTO = PrevisionResponseDTO.builder()
                .id(1L)
                .productId(productId)
                .productName("Ordinateur HP")
                .warehouseId(warehouseId)
                .warehouseName("Paris")
                .build();

        when(previsionMapper.toResponseDTO(any(Prevision.class))).thenReturn(expectedDTO);

        PrevisionResponseDTO result = previsionService.genererPrevision(productId, warehouseId, adminPrincipal);

        assertNotNull(result);
        verify(previsionRepository).save(any(Prevision.class));
        verify(geminiService).genererMessageRecommandation(anyString(), anyInt(), anyDouble(), anyInt(), anyDouble());
    }

    @Test
    void genererPrevision_ProduitNonTrouve_LanceException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> previsionService.genererPrevision(1L, 1L, adminPrincipal));
    }

    @Test
    void genererPrevision_StockNonTrouve_LanceException() {
        Product product = Product.builder().id(1L).nom("Test").build();
        Warehouse warehouse = Warehouse.builder().id(1L).name("Test").build();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(warehouse));
        when(stockRepository.findByProductIdAndWarehouseId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> previsionService.genererPrevision(1L, 1L, adminPrincipal));
    }

    @Test
    void getPrevisionsByWarehouse_RetourneListePrevisionsDTO() {
        Long warehouseId = 1L;
        List<Prevision> previsions = List.of(
                Prevision.builder().id(1L).build(),
                Prevision.builder().id(2L).build());

        when(previsionRepository.findByWarehouseId(warehouseId)).thenReturn(previsions);
        when(previsionMapper.toResponseDTO(any(Prevision.class)))
                .thenReturn(PrevisionResponseDTO.builder().build());

        List<PrevisionResponseDTO> result = previsionService.getPrevisionsByWarehouse(warehouseId, adminPrincipal);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(previsionRepository).findByWarehouseId(warehouseId);
    }

    @Test
    void getPrevisionById_AvecIdValide_RetournePrevisionDTO() {
        Long id = 1L;
        Warehouse warehouse = Warehouse.builder().id(1L).name("Test").build();
        Prevision prevision = Prevision.builder().id(id).warehouse(warehouse).build();
        PrevisionResponseDTO expectedDTO = PrevisionResponseDTO.builder().id(id).build();

        when(previsionRepository.findById(id)).thenReturn(Optional.of(prevision));
        when(previsionMapper.toResponseDTO(prevision)).thenReturn(expectedDTO);

        PrevisionResponseDTO result = previsionService.getPrevisionById(id, adminPrincipal);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getPrevisionById_AvecIdInvalide_LanceException() {
        when(previsionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> previsionService.getPrevisionById(999L, adminPrincipal));
    }

    private List<HistoriqueVente> creerHistoriqueVentes(int nombre, int quantite) {
        List<HistoriqueVente> ventes = new ArrayList<>();
        for (int i = 0; i < nombre; i++) {
            HistoriqueVente vente = HistoriqueVente.builder()
                    .quantiteVendue(quantite)
                    .dateVente(LocalDate.now().minusDays(i))
                    .build();
            ventes.add(vente);
        }
        return ventes;
    }
}