package fr.cantine.cantineserver.controller;

import lombok.Data;

/**
 * Objet représentant une action de CRUD sur une entité.
 * @param <T> type de l'entité.
 */
@Data
public class EntityCrudAction<T> {
    private T entity;
    private CrudActionEnum action;

    public EntityCrudAction(T entity, CrudActionEnum action) {
        this.entity = entity;
        this.action = action;
    }
}
