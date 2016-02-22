package com.patientadmission.presentation;

import com.google.common.collect.Lists;
import com.patientadmission.domain.IcdElement;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import org.apache.commons.lang.StringUtils;
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
public class IcdElementViewModel extends AbstractZKModel{

    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;
    
    @WireVariable
    private ICrud crudDao;

    List<Map<String,? >> icdElements = Lists.newArrayList();

    private String icdCode= StringUtils.EMPTY;

    private String description= StringUtils.EMPTY;
    
    @Wire
    private Window icdElementWin;
    
    private List<IcdElement> selectedIcdElements;
    
    private String multiSelect;
    
    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        selectedIcdElements = (List<IcdElement>) Executions.getCurrent().getArg().get("icdElements");
        multiSelect = (String) Executions.getCurrent().getArg().get("multiSelect");
    }

    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Map<String, ?>> getIcdElements() {
        return icdElements;
    }

    public void setIcdElements(List<Map<String, ?>> icdElements) {
        this.icdElements = icdElements;
    }

    @Command
    @NotifyChange("icdElements")
    public void searchIcdElements(){
    	if(UtilValidator.isEmpty(description) && UtilValidator.isEmpty(icdCode))
    		throw new WrongValueException("Please enter either code or description to search");
        if(UtilValidator.isEmpty(description))
            description=null;
        if(UtilValidator.isEmpty(icdCode))icdCode=null;
        System.out.println("searchIcdElements icdCode " + icdCode + " description " + description);
        icdElements = patientAdmissionFinder.getIcdElementByCodeOrCategory(icdCode, description);
    }

    @Command
    public void addSelectedIcds(@BindingParam("listbox")Listbox listbox){
    	if(UtilValidator.isEmpty(listbox.getSelectedItems()))
    		throw new WrongValueException("Please Select items to add");
    	for(Listitem listitem : listbox.getSelectedItems()){
    	Map<String,?> map=listitem.getValue();
    	selectedIcdElements.add((IcdElement)crudDao.find(IcdElement.class,(String) map.get("id")));
    	}
    	Events.postEvent("onDetach", icdElementWin, selectedIcdElements);
    	icdElementWin.detach();
    }
    
    @Command
    public void selectIcd(@BindingParam("id")String id){
    	if(multiSelect.equals("false")){
    	IcdElement element = (IcdElement)crudDao.find(IcdElement.class,id);
    	Events.postEvent("onDetach", icdElementWin, element);
    	icdElementWin.detach();
    	}
    }

	public String getMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(String multiSelect) {
		this.multiSelect = multiSelect;
	}
}