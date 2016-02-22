package com.patientadmission.presentation;

import java.util.List;

import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
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

import com.google.common.collect.Lists;
import com.master.domain.Enumeration;
import com.master.services.IMasterService;

@Composer
public class IndicationsForAdmissionViewModel extends AbstractZKModel {
	
	private List<Enumeration> indicationsForAdmissionList =  Lists.newArrayList();
	
	@WireVariable
	private IMasterService masterService;
	
	private List<Enumeration> selectedIndications;
	
	
	@Wire
	private Window addIndicationsForAdmissionWin;
	
	@Init
	public void init(@ContextParam(ContextType.VIEW) Component view){
	Selectors.wireComponents(view, this, false);
	selectedIndications = (List<Enumeration>) Executions.getCurrent().getArg().get("indications");
	indicationsForAdmissionList = masterService.getEnumerationByEnumType(Enumeration.EnumerationType.INDICATION_FOR_ADMISSION.toString());
	}

	public List<Enumeration> getIndicationsForAdmissionList() {
		return indicationsForAdmissionList;
	}
	
	@Command
	public void addSelectedIndications(@BindingParam("listbox")Listbox listbox){
	if(UtilValidator.isEmpty(listbox.getSelectedItems()))
		throw new WrongValueException("Please Select items to add");
	for(Listitem listitem : listbox.getSelectedItems())
		selectedIndications.add((Enumeration) listitem.getValue());
	Events.postEvent("onDetach", addIndicationsForAdmissionWin, selectedIndications);
	addIndicationsForAdmissionWin.detach();
	}
}
