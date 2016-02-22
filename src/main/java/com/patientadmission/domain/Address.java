package com.patientadmission.domain;

import org.hibernate.annotations.Immutable;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;
import org.nthdimenzion.ddd.domain.annotations.ValueObject;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 19/1/13
 * Time: 9:59 PM
 */
@ValueObject
@Immutable
@Embeddable
public class Address {

    private String addr1;
    private String addr2;
    private String addr3;
    private Country country;
    private State state;
    private String city;

    Address() {
        // For ORM
    }

    public Address(String addr1, String addr2, String addr3,Country country, State state, String city) {
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.addr3 = addr3;
        this.country = country;
        this.state = state;
        this.city = city;
    }
    
    public  Address updateAddress(Address address,String addr1, String addr2, String addr3,Country country, State state, String city){
    	address.addr1 = addr1;
    	address.addr2 = addr2;
    	address.addr3 = addr3;
    	address.country = country;
    	address.state = state;
    	address.city = city;
        return address;
    }


    String getAddr1() {
        return addr1;
    }

    void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    String getAddr2() {
        return addr2;
    }

    void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    String getAddr3() {
        return addr3;
    }

    void setAddr3(String addr3) {
        this.addr3 = addr3;
    }

    @ManyToOne
    Country getCountry() {
        return country;
    }

    void setCountry(Country country) {
        this.country = country;
    }

    @ManyToOne
    State getState() {
        return state;
    }

    void setState(State state) {
        this.state = state;
    }

    String getCity() {
        return city;
    }

    void setCity(String city) {
        this.city = city;
    }
}
