package com.master.domain;

import org.hibernate.annotations.Immutable;
import org.nthdimenzion.crud.ICrudEntity;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames ={"enumCode"}))
@Immutable
public class Enumeration extends IdGeneratingArcheType implements ICrudEntity {

    public static enum EnumerationType {

        TITLE,MASTER,GUARDIAN_RELATIONSHIP,CATEGORY,
        INDICATION_FOR_ADMISSION,INTERNAL_HOSPITAL_UNIT,
        INTERNAL_CLINIC,CONDITION_AT_DISCHARGE,DISCHARGE_DESTINATION,BED_CATEGORY,REFERRED_FROM;
    }

    @XmlElement
	public String description;
    @XmlElement
	private String enumCode;
    @XmlElement
	private EnumerationType enumType;

    public Enumeration() {
    }

    public Enumeration(String enumCode, EnumerationType enumType) {
        this.enumCode = enumCode;
        this.enumType = enumType;
    }

    public String getDescription() {
	return description;
	}

    public void setDescription(String description) {
	this.description = description;
	}

	public String getEnumCode() {
	return enumCode;
	}

    public void setEnumCode(String enumCode) {
	this.enumCode = enumCode;
	}

    @Enumerated(EnumType.STRING)
	public EnumerationType getEnumType() {
	return enumType;
	}

    public void setEnumType(EnumerationType enumType) {
	this.enumType = enumType;
	}



    public String toString(){
        return description;
    }
}
