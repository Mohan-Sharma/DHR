package com.patientadmission.domain;

import com.master.domain.Enumeration;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 19/1/13
 * Time: 11:08 PM
 */
@Entity
public class Person extends IdGeneratingArcheType{

    private Enumeration title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private DateTime dob;
    private String contactNumber;

    public static Person createPersonWithName(Enumeration title,String firstName,String middleName,String lastName){
        Person person = new Person();
        person.title = title;
        person.firstName = firstName;
        person.middleName = middleName;
        person.lastName = lastName;
        return person;
    }

    public Person withGender(String gender){
        this.gender = gender;
        return this;
    }

    public Person bornOn(DateTime dob){
        this.dob = dob;
        return this;
    }

    public Person withContactNumber(String contactNumber){
        this.contactNumber = contactNumber;
        return this;
    }


    @ManyToOne
    Enumeration getTitle() {
        return title;
    }

    void setTitle(Enumeration title) {
        this.title = title;
    }

    String getFirstName() {
        return firstName;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    String getMiddleName() {
        return middleName;
    }

    void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    String getLastName() {
        return lastName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    String getGender() {
        return gender;
    }

    void setGender(String gender) {
        this.gender = gender;
    }

    @Column
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    DateTime getDob() {
        return dob;
    }

    void setDob(DateTime dob) {
        this.dob = dob;
    }

    String getContactNumber() {
        return contactNumber;
    }

    void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public static Person updatePersonWithName(Person person, Enumeration title,String firstName,String middleName,String lastName){
        person.title = title;
        person.firstName = firstName;
        person.middleName = middleName;
        person.lastName = lastName;
        return person;
    }

    public Person updateWithGender(Person person,String gender){
        person.gender = gender;
        return person;
    }

    public Person updateBornOn(Person person,DateTime dob){
        person.dob = dob;
        return person;
    }

    public Person updateWithContactNumber(Person person,String contactNumber){
        person.contactNumber = contactNumber;
        return person;
    }
}

