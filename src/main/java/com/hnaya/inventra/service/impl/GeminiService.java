package com.hnaya.inventra.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GeminiService {


    private final ChatModel chatModel;


    public String genererMessageRecommandation(String productName, Integer stockActuel, Double prevision30Jours,
            Integer quantiteACommander, Double niveauConfiance) {
        try {
            String prompt = construirePrompt(productName, stockActuel, prevision30Jours, quantiteACommander,
                    niveauConfiance);
            return chatModel.call(prompt);


        } catch (Exception e) {
            return genererMessageParDefaut(quantiteACommander, productName);
        }
    }


    private String construirePrompt(String productName, Integer stockActuel, Double prevision30Jours,
            Integer quantiteACommander, Double niveauConfiance) {
        return String.format(
                """
                        Tu es un assistant de gestion de stocks. Génère un message court et professionnel (maximum 2 phrases).


                        Contexte :
                        - Produit : %s
                        - Stock actuel : %d unités
                        - Prévision ventes (30 jours) : %.0f unités
                        - Quantité recommandée à commander : %d unités
                        - Niveau de confiance : %.0f%%


                        Instructions :
                        - Si quantité recommandée = 0, dis que le stock est suffisant
                        - Si quantité > 0, recommande de commander
                        - Si confiance < 50%%, ajoute une note de prudence
                        - Sois concis et professionnel


                        Génère UNIQUEMENT le message, sans introduction ni conclusion.
                        """,
                productName,
                stockActuel,
                prevision30Jours,
                quantiteACommander,
                niveauConfiance);
    }


    private String genererMessageParDefaut(Integer quantiteACommander, String productName) {
        if (quantiteACommander > 0) {
            return String.format("Commander %d unités de %s", quantiteACommander, productName);
        } else {
            return "Stock suffisant pour les 30 prochains jours.";
        }
    }
}
