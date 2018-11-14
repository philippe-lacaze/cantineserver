package fr.cantine.cantineserver.service;

import fr.cantine.cantineserver.model.Commande;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommandeService {
    Mono<Commande> createCommande(Commande commande);

    Flux<Commande> fetchCommande();

    Flux<Commande> findByDateCommande(String dateCommande);

    Mono<Commande> readCommande(String clientDate);

    Mono<Commande> updateCommande(Commande commande);

    Mono<Void> deleteCommande(Commande commande);
}
