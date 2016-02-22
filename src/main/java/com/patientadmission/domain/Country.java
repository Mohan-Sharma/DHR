package com.patientadmission.domain;

import org.nthdimenzion.crud.ICrudEntity;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 19/1/13
 * Time: 11:14 PM
 */
@Entity
public class Country extends IdGeneratingArcheType implements ICrudEntity {

    private String label;
    private Set<State> states;

    Country() {
        // ORM
    }

    public Country(String label, Set<State> states) {
        this.label = label;
        this.states = states;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @OneToMany
    @JoinColumn(name = "COUNTRY_ID")
    public Set<State> getStates() {
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }
}


