package com.patientadmission.domain;

import org.nthdimenzion.crud.ICrudEntity;
import org.nthdimenzion.ddd.domain.INamed;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 20/1/13
 * Time: 9:13 PM
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames ={"wardName"}))
public class Ward extends IdGeneratingArcheType implements ICrudEntity,INamed {

    private String wardName;

    public Ward() {
    }

    public Ward(String wardName) {
        this.wardName = wardName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    @Override
    @Transient
    public String getName() {
        return getWardName();
    }
}
