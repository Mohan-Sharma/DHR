package com.patientadmission.presentation;

import com.google.common.collect.Lists;
import com.patientadmission.domain.Procedure;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 30/1/13
 * Time: 7:27 PM
 */
@Composer
public class CptViewModel extends AbstractZKModel{

    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;
    
    @WireVariable
    private ICrud crudDao;

    List<Map<String,? >> cptProcedures = Lists.newArrayList();

    private String cptCode;

    private String description;
    
    @Wire
    private Window cptWin;
    
    private List<Procedure> selectedProcedures;
    
    private String multiSelect;
    
    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        selectedProcedures = (List<Procedure>) Executions.getCurrent().getArg().get("procedures");
        multiSelect = (String) Executions.getCurrent().getArg().get("multiSelect");
    }

    public String getCptCode() {
        return cptCode;
    }

    public void setCptCode(String cptCode) {
        this.cptCode = cptCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Map<String, ?>> getCptProcedures() {
        return cptProcedures;
    }

    public void setCptProcedures(List<Map<String, ?>> cptProcedures) {
        this.cptProcedures = cptProcedures;
    }

    @Command
    @NotifyChange("cptProcedures")
    public void searchCpt(){
    	if(UtilValidator.isEmpty(cptCode) && UtilValidator.isEmpty(description)){
    		throw new WrongValueException("Please enter either code or description to search");
    	}
        cptProcedures = patientAdmissionFinder.getCptCodeOrDescription(cptCode,description);
    }

    @Command
    public void addSelectedCpts(@BindingParam("listbox")Listbox listbox){
    	if(UtilValidator.isEmpty(listbox.getSelectedItems()))
    		throw new WrongValueException("Please Select items to add");
    	for(Listitem listitem : listbox.getSelectedItems()){
    	Map<String,?> map=listitem.getValue();
    	selectedProcedures.add((Procedure)crudDao.find(Procedure.class,(Long) map.get("id")));
    	}
    	Events.postEvent("onDetach", cptWin, selectedProcedures);
    	cptWin.detach();
    }

	public String getMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(String multiSelect) {
		this.multiSelect = multiSelect;
	}
	
	@Command
	public void selectCpt(@BindingParam("id")Long id){
		if(multiSelect.equals("false")){
    	Procedure procedure = (Procedure)crudDao.find(Procedure.class,id);
    	Events.postEvent("onDetach", cptWin, procedure);
    	cptWin.detach();
		}
	}
}