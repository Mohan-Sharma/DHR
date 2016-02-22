package com.patientadmission.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.nthdimenzion.crud.ICrudEntity;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;

@Entity
public class Preference extends IdGeneratingArcheType implements ICrudEntity{
	
	private Country country;
	
	private State state;
	
	private String city;

	@ManyToOne
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@ManyToOne
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
