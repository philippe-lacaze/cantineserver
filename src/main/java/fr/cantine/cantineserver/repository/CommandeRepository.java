package fr.cantine.cantineserver.repository;

import fr.cantine.cantineserver.model.Commande;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository Spring de gestion d'une persistance de commande.
 */
@Repository
public interface CommandeRepository extends ReactiveMongoRepository<Commande, String> {
    Mono<Commande> findByClientDate(String clientDate);

    Flux<Commande> findByDateCommande(String dateCommande);
}
