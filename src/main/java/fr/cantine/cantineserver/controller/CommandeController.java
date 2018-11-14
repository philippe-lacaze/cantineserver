package fr.cantine.cantineserver.controller;

import fr.cantine.cantineserver.flux.FluxPublisher;
import fr.cantine.cantineserver.model.Commande;
import fr.cantine.cantineserver.service.CommandeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Controller REST principal de l'application.
 */
@Slf4j
@RestController
@RequestMapping("api/commande")
@CrossOrigin({"*"})
public class CommandeController {

    private Flux<Long> messageFlux;

    /**
     * Flux d'action CRUD sur les commandes, recues par ce controller.
     */
    private Flux<EntityCrudAction<Commande>> commandeFlux;

    /**
     * Holder Optional du Publisher sur le flux d'action CRUD de commande.
     * Cette instance d'Optional ne sera remplacée par une instance non vide que lorsqu'un observer
     * aura souscrit au flux de commande.
     * C'est par le biais de ce publisher qu'on publie une action CRUD sur le flux.
     */
    private Optional<FluxPublisher<EntityCrudAction<Commande>>> commandeFluxPublisher = Optional.empty();

    /**
     * Service de gestion des commandes.
     */
    private final CommandeService commandeService;

    CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    /**
     * Initialisation du bean.
     * <p>Création du Flux de notification SSE.</p>
     */
    @PostConstruct
    private void init() {
        log.info("PHILOU log init()");
        messageFlux = Flux.interval(Duration.ofSeconds(30)).share();

        // Création du flux de commande
        commandeFlux = Flux.<EntityCrudAction<Commande>>create(sink -> {
            // Création du publisher sur le flux et affectation a l'instance d'Optional.
            commandeFluxPublisher = Optional.of(new FluxPublisher<EntityCrudAction<Commande>>() {

                /**
                 * Emet la commande en paramètre sur le flux.
                 * @param object
                 */
                public void next(EntityCrudAction<Commande> entityCrudAction) {
                    log.info("commandeFluxPublisher sink.next ", entityCrudAction);
                    sink.next(entityCrudAction);
                }

                public void complete() {
                    sink.complete();
                }
            });
        }).share(); // le flux est partagé a tous les subscribers
    }

    /**
     * Requête renvoyant les commandes en base et celles qui arrivent, sous forme de SSE.
     * <p>On combine le flux resultant du fetch sur la base et le flux de notification</p>
     *
     * @return Flux des commandes sous forme de SSE.
     */
    @GetMapping("/message")
    public Flux<ServerSentEvent<EntityCrudAction<Commande>>> message() {
        return Flux.concat(
                // Commandes en base
                this.commandeService
                        .findByDateCommande(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .map(commande ->
                new EntityCrudAction<Commande>(commande, CrudActionEnum.READ)
        ),

                // Action CRUD de commandes reçues et publiées sur le publisher
                this.commandeFlux)
                .map(entityCrudAction ->
                ServerSentEvent.<EntityCrudAction<Commande>>builder()
                        .id(entityCrudAction.getEntity().getClient())
                        .event("message")
                        .data(entityCrudAction)
                        .build()
        );
    }

    /**
     * Retourne le flux des commandes en base.
     *
     * @return le flux des commandes en base.
     */
    @GetMapping()
    public Flux<Commande> fetch() {
        log.info("fetch");
        return this.commandeService.fetchCommande();
    }

    /**
     * Retourne un flux mono de la commande dont l'id est donné.
     *
     * @param clientDate donné.
     * @return un flux mono de la commande dont l'id est donné.
     */
    @GetMapping("/{clientDate}")
    public Mono<Commande> read(@PathVariable String clientDate) {
        log.info("read with clientDate : {}", clientDate);
        return this.commandeService.readCommande(clientDate);
    }

    /**
     * Crée une commande et publie une notification de CRUD via le publisher.
     *
     * @param commande
     * @return commande crée.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Commande> create(@RequestBody Commande commande) {
        log.info("PHILOU create " + commande);

        Mono<Commande> result;
        try {
            result = this.commandeService
                    .createCommande(commande)
                    .doOnSuccess(
                            cmd -> commandeFluxPublisher.ifPresent(
                                    publisher -> publisher.next(
                                            new EntityCrudAction<Commande>(cmd, CrudActionEnum.CREATE)
                                    )
                            )
                    ).doOnError(
                            throwable -> log.error("Erreur à l'insertion de la commande", throwable)
                    );
        } catch (Throwable t) {
            log.error("Erreur a l'appel de commandeService ", t);
            result = Mono.just(new Commande());
        }

        return result;

    }

    /**
     * Modifie la commande donné en base.
     * <p>Publie une notification via le publisher de CRUD</p>
     *
     * @param commande donnés.
     * @return la commande modifié.
     */
    @PutMapping
    public Mono<Commande> update(@RequestBody Commande commande) {
        log.info("PHILOU update " + commande);

        Mono<Commande> result;
        try {
            result = this.commandeService
                    .updateCommande(commande)
                    .doOnSuccess(
                            (Commande cmd) -> {
                                commandeFluxPublisher.ifPresent(
                                        publisher -> publisher.next(new EntityCrudAction<Commande>(cmd, CrudActionEnum.UPDATE)));
                            }
                    ).doOnError(
                            throwable -> log.error("Erreur à l'insertion de la commande", throwable)
                    );
        } catch (Throwable t) {
            log.error("Erreur a l'apper de commandeService ", t);
            result = Mono.just(new Commande());
        }

        return result;

    }


    @DeleteMapping
    public Mono<Void> delete(@RequestBody Commande commande) {
        return this.commandeService.deleteCommande(commande);
    }
}
