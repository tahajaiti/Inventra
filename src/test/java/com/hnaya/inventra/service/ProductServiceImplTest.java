package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.request.ProductRequestDTO;
import com.hnaya.inventra.dto.response.ProductResponseDTO;
import com.hnaya.inventra.entity.Product;
import com.hnaya.inventra.entity.enums.UniteMesure;
import com.hnaya.inventra.mapper.ProductMapper;
import com.hnaya.inventra.repository.ProductRepository;
import com.hnaya.inventra.service.impl.ProductServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequestDTO requestDTO;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = ProductRequestDTO.builder()
                .nom("Ordinateur")
                .description("PC portable")
                .categorie("Informatique")
                .prixVente(1000.0)      // ✓ Double
                .prixAchat(800.0)       // ✓ Double
                .poids(2.5)
                .unite(UniteMesure.KG)
                .build();

        product = Product.builder()
                .id(1L)                 // ✓ Long
                .nom("Ordinateur")
                .description("PC portable")
                .prixVente(1000.0)      // ✓ Double
                .prixAchat(800.0)       // ✓ Double
                .poids(2.5)
                .unite(UniteMesure.KG)
                .marge(200.0)           // ✓ Double
                .build();

        responseDTO = new ProductResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNom("Ordinateur");
        responseDTO.setDescription("PC portable");
        responseDTO.setCategorie("Informatique");
        responseDTO.setPrixVente(1000.0);
        responseDTO.setPoids(2.5);
        responseDTO.setUnite(UniteMesure.KG);
    }

    @Test
    @DisplayName("Devrait créer un produit avec le calcul de la marge correct")
    void createProduct_ShouldCalculateMargeAndSave() {
        // Given
        when(productMapper.toEntity(any(ProductRequestDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponseDto(any(Product.class))).thenReturn(responseDTO);

        // When
        ProductResponseDTO result = productService.create(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(200.0, product.getMarge()); // Vérifie la logique métier (1000 - 800)
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Devrait retourner la liste de tous les produits")
    void findAll_ShouldReturnList() {
        // Given
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toResponseDto(any(Product.class))).thenReturn(responseDTO);

        // When
        List<ProductResponseDTO> result = productService.findAll();

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Devrait supprimer un produit s'il existe")
    void delete_WhenProductExists_ShouldDelete() {
        // Given
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(true);
        doNothing().when(productRepository).deleteById(id);  // ✓ Pour méthode void

        // When
        productService.delete(id);

        // Then
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Devrait lancer une exception si le produit à supprimer n'existe pas")
    void delete_WhenProductDoesNotExist_ShouldThrowException() {
        // Given
        Long id = 99L;
        when(productRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> productService.delete(id));
        verify(productRepository, never()).deleteById(id);
    }
}