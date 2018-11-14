package fr.cantine.cantineserver.service;

import fr.cantine.cantineserver.model.Commande;
import fr.cantine.cantineserver.repository.CommandeRepository;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CommandeServiceImpl implements CommandeService {

    private final ApplicationEventPublisher publisher;
    private final CommandeRepository commandeRepository;

    CommandeServiceImpl(ApplicationEventPublisher publisher
            , CommandeRepository commandeRepository
    ) {
        this.publisher = publisher;
        this.commandeRepository = commandeRepository;
        System.out.println("pub=" + this.publisher + " "
                //+"cmdRep="+this.commandeRepository
        );
    }

    @Override
    public Mono<Commande> createCommande(Commande commande) {
        return this.commandeRepository.insert(commande);
    }

    @Override
    public Flux<Commande> fetchCommande() {
        return this.commandeRepository.findAll();
    }

    @Override
    public Flux<Commande> findByDateCommande(String dateCommande) {
        log.info("CommandeServuceImpl.findByDateCommande" + dateCommande);
        return this.commandeRepository.findByDateCommande(dateCommande);
    }

    @Override
    public Mono<Commande> readCommande(String clientDate) {
        return this.commandeRepository.findByClientDate(clientDate);
    }

    @Override
    public Mono<Commande> updateCommande(Commande commande) {
        return this.commandeRepository.save(commande);
    }

    @Override
    public Mono<Void> deleteCommande(Commande commande) {
        return this.commandeRepository.delete(commande);
    }
}
