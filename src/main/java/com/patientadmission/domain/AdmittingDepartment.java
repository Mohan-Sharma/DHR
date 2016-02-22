package com.patientadmission.domain;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.nthdimenzion.crud.ICrudEntity;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames ={"departmentName"}))
public class AdmittingDepartment extends IdGeneratingArcheType implements
        ICrudEntity {

    private String departmentName;
    private String type;
    Set<Doctor> doctors = Sets.newHashSet();
    public AdmittingDepartment(){

    }

    public AdmittingDepartment(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }
}
