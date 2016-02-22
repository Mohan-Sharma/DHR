package com.master.presentation.viewmodel;

import com.google.common.collect.Lists;
import com.master.domain.Enumeration;
import com.master.domain.Enumeration.EnumerationType;
import com.master.services.IMasterService;
import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.domain.Bed;
import com.patientadmission.domain.Ward;
import org.hibernate.exception.ConstraintViolationException;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Combobox;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 30/1/13
 * Time: 7:27 PM
 */
@Composer
public class MasterViewModel extends AbstractZKModel {

    private Enumeration enumeration = new Enumeration();
    private EnumerationType selectedEnumType;
    private List masterList = Lists.newArrayList();
    private Component rootComponent;

    @WireVariable
    private IMasterService masterService;



    public AdmittingDepartment getAdmittingDepartment() {
        return admittingDepartment;
    }

    public void setAdmittingDepartment(AdmittingDepartment admittingDepartment) {
        this.admittingDepartment = admittingDepartment;
    }

    private AdmittingDepartment admittingDepartment;
    private Ward ward;
    private Bed bed;

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Bed getBed() {
        return bed;
    }

    public void setBed(Bed bed) {
        this.bed = bed;
    }

    @WireVariable
    private ICrud crudDao;

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        admittingDepartment = new AdmittingDepartment();
        ward = new Ward();
        bed = new Bed();

        this.rootComponent = view;
        masterList = (List) Executions.getCurrent().getArg().get("masterList");
        Selectors.wireComponents(view, this, false);
    }

    public Enumeration getEnumeration() {
        return enumeration;
    }

    @Command
    public void createEnumeration(@BindingParam("targetComp") Component component) {
    	if(UtilValidator.isEmpty(enumeration.getDescription()) || UtilValidator.isEmpty(enumeration.getEnumCode()))
    		return;
    	List<Enumeration> enumerations = masterService.getEnumerationByEnumType(selectedEnumType.toString());
    	for(Enumeration enumeration : enumerations){
    		if(enumeration.getEnumCode().equalsIgnoreCase(this.enumeration.getEnumCode()) || enumeration.getDescription().equalsIgnoreCase(this.enumeration.getDescription()))
    			throw new WrongValueException(" Enumeration ' " + this.enumeration.getDescription() +" ' already exists.");
    	}
        enumeration.setEnumType(selectedEnumType);
        enumeration = crudDao.save(enumeration);
        if (component instanceof Combobox) {
            masterList.add(enumeration);
            ((Combobox) component).setModel(new BindingListModelList<Enumeration>(masterList, false));
            rootComponent.detach();
            Events.postEvent("onReload", component, enumeration);
        }
    }

    @Command
    public void createAdmittingDept(@BindingParam("targetComp") Component component) {
    	if(UtilValidator.isEmpty(admittingDepartment.getDepartmentName()))
    		return;
    	/*for(AdmittingDepartment admittingDepartment : admittingDepartments)
    		if(admittingDepartment.getDepartmentName().equals(admittingDepartment.getDepartmentName()))
    			throw new WrongValueException("Admitting Department with name ' " + admittingDepartment.getDepartmentName() + " ' already exists.");*/
        admittingDepartment = crudDao.save(admittingDepartment);
        Events.postEvent("onReload", component, admittingDepartment);
        rootComponent.detach();
    }


    @Command
    public void createWard(@BindingParam("targetComp") Component component) {
    	if(UtilValidator.isEmpty(ward.getWardName()))
    		return;
    	List<Ward> wards = crudDao.getAll(Ward.class);
    	for(Ward existingWard : wards)
    		if(existingWard.getWardName().equals(ward.getWardName()))
    			throw new WrongValueException("Ward with name ' " + ward.getWardName() + " ' already exists.");
        ward = crudDao.save(ward);
        Events.postEvent("onReload", component, ward);
        rootComponent.detach();
    }

    @Command
    public void createBed(@BindingParam("targetComp") Component component) {
    	if(UtilValidator.isEmpty(bed.getName()))
    		return;
    	/*List<Bed> beds = crudDao.getAll(Bed.class);*/
    	/*for(Bed existingBed : beds)
    		if(existingBed.getName().equals(bed.getName()))
    			throw new WrongValueException("Bed with name ' " + bed.getName() + " ' already exists.");*/
        try{
            bed = crudDao.save(bed);
        }catch (ConstraintViolationException e){
            displayMessages.displayError("Bed with name " + bed.getName() + " already exists ");
        }
        Events.postEvent("onReload", component, bed);
        rootComponent.detach();
    }

    public EnumerationType getSelectedEnumType() {
        return selectedEnumType;
    }

    public void setSelectedEnumType(EnumerationType selectedEnumType) {
        this.selectedEnumType = selectedEnumType;
    }
}