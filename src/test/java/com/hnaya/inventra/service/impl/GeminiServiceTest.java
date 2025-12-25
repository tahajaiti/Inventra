package com.hnaya.inventra.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.model.ChatModel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeminiServiceTest {

    @Mock
    private ChatModel chatModel;

    @InjectMocks
    private GeminiService geminiService;

    @Test
    void genererMessageRecommandation_AvecQuantitePositive_RetourneMessageGemini() {
        String messageAttendu = "Commander 150 unités d'Ordinateur HP rapidement.";
        when(chatModel.call(anyString())).thenReturn(messageAttendu);

        String result = geminiService.genererMessageRecommandation("Ordinateur HP", 50, 200.0, 150, 85.0);

        assertNotNull(result);
        assertEquals(messageAttendu, result);
    }

    @Test
    void genererMessageRecommandation_AvecQuantiteZero_RetourneMessageGemini() {
        String messageAttendu = "Stock suffisant pour les 30 prochains jours.";
        when(chatModel.call(anyString())).thenReturn(messageAttendu);

        String result = geminiService.genererMessageRecommandation("Ordinateur HP", 300, 200.0, 0, 95.0);

        assertNotNull(result);
        assertEquals(messageAttendu, result);
    }

    @Test
    void genererMessageRecommandation_GeminiEchoue_RetourneMessageParDefaut() {
        when(chatModel.call(anyString())).thenThrow(new RuntimeException("Gemini error"));

        String result = geminiService.genererMessageRecommandation("Ordinateur HP", 50, 200.0, 150, 85.0);

        assertNotNull(result);
        assertEquals("Commander 150 unités de Ordinateur HP", result);
    }

    @Test
    void genererMessageRecommandation_GeminiEchoueAvecQuantiteZero_RetourneMessageStockSuffisant() {
        when(chatModel.call(anyString())).thenThrow(new RuntimeException("Gemini error"));

        String result = geminiService.genererMessageRecommandation("Ordinateur HP", 300, 200.0, 0, 95.0);

        assertNotNull(result);
        assertEquals("Stock suffisant pour les 30 prochains jours.", result);
    }
}
