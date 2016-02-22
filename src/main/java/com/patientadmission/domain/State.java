package com.patientadmission.domain;

import com.google.common.collect.Lists;
import org.nthdimenzion.crud.ICrudEntity;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 19/1/13
 * Time: 11:13 PM
 */
@Entity
public class State extends IdGeneratingArcheType implements ICrudEntity{

    private String label;
    private List<String> cities = Lists.newArrayList();

    State() {
        //ORM
    }

    public State(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @ElementCollection
    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public State addCities(List<String> cities){
        this.cities.addAll(cities);
        return this;
    }
}
