package com.patientadmission.domain;

import org.nthdimenzion.crud.ICrudEntity;
import org.nthdimenzion.ddd.domain.INamed;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;

import com.master.domain.Enumeration;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 20/1/13
 * Time: 9:15 PM
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames ={"name"}))
public class Bed extends IdGeneratingArcheType implements ICrudEntity,INamed {

    private String name;
    
    private Enumeration bedCategory;

    public Bed() {
        //ORM
    }

    public Bed(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
	public Enumeration getBedCategory() {
		return bedCategory;
	}

	public void setBedCategory(Enumeration bedCategory) {
		this.bedCategory = bedCategory;
	}
}
