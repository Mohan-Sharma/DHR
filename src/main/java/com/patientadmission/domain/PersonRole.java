package com.patientadmission.domain;

import org.joda.time.DateTime;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;
import org.nthdimenzion.ddd.domain.annotations.Role;

import javax.persistence.*;

@Role
@MappedSuperclass
abstract class PersonRole extends IdGeneratingArcheType {

    protected Person person;
    protected RoleTitle roleTitle;

    public static enum RoleTitle {
        PATIENT,DOCTOR;
    }

    protected PersonRole(){
        person = new Person();
    }

    protected PersonRole(Person person) {
        this.person = person;
    }

    protected PersonRole(String firstName, String lastName, DateTime dateOfBirth) {
        this.person.setFirstName(firstName);
        this.person.setLastName(lastName);
        this.person.setDob(dateOfBirth);
    }

    protected PersonRole(String firstName, String middleName, String lastName, DateTime dateOfBirth) {
        this(firstName,lastName,dateOfBirth);
        this.person.setMiddleName(middleName);
    }

    @ManyToOne(cascade = CascadeType.ALL)
    protected Person getPerson(){
        return person;
    }

    protected void setPerson(Person person){
        this.person = person;
    }

    @Enumerated(EnumType.STRING)
    public RoleTitle getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(RoleTitle roleTitle) {
        this.roleTitle = roleTitle;
    }
}

