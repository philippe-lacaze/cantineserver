package fr.cantine.cantineserver.model;

import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entité commande.
 */
@Data
@Document(collection = "commande")
public class Commande extends AbstractEntity {
    @TextIndexed
    private String clientDate;

    private String client;
    private String dateCommande;


    private String menu;
    private String plat;
    private String pain;
    private String ingredient;
    private String[] accompagnements;
    private String dessert;
    private String complement;
    private String boisson;
    private boolean traitee;

}
