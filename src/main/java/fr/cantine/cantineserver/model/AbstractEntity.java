package fr.cantine.cantineserver.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Super classe abstraite d'une entitée persistée.
 */
@Getter
@Setter
public abstract class AbstractEntity implements Serializable {
    @Id
    private String id;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Date createdDate;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private Date updatedDate;

    @Version
    private Long version;
}
