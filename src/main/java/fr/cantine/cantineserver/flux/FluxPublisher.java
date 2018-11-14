package fr.cantine.cantineserver.flux;

/**
 * Interfaçe d'un Publisher utilisée pour envoyer programmatiquement des évènements sur
 * le flux d'action CRUD sur une entité.
 * @param <T> type de l'entité.
 */
public interface FluxPublisher<T> {
    /**
     * Publie l'objet de type T donné sur le flux.
     * @param object donné.
     */
    void next(T object);

    /**
     * Termine le flux.
     */
    void complete();
}
