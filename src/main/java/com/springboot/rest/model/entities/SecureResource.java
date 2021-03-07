package com.springboot.rest.model.entities;

import javax.persistence.*;

@Entity
public class SecureResource {

    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_resource_owner_id"))
    User owner;

    public SecureResource(Long id, User owner) {
        this.id = id;
        this.owner = owner;
    }

    public SecureResource() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
