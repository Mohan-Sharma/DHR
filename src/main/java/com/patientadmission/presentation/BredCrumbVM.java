package com.patientadmission.presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.impl.LabelElement;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class BredCrumbVM  {

	@Wire("#breadCrunmId")
	private Div breadCrunmId;
	private Component previousComponent = null;

	@SuppressWarnings("unchecked")
	private List<Component> crumbList = (List<Component>) Sessions.getCurrent().getAttribute("crumbList");

	private boolean validate = true;
	private Map<String, List<Component>> map = new HashMap<String, List<Component>>();

	@Init
	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, true);

	}

	@SuppressWarnings("unchecked")
	@Command("go")
	public void go(@BindingParam("component") Component component) {
		breadCrunmId.getChildren().clear();

		String linkLabel = "";
		String target = "";

		crumbList = (List<Component>) Sessions.getCurrent().getAttribute("crumbList");
		if (crumbList == null)
			crumbList = new ArrayList<Component>();


		if (component instanceof A) {
			component = ((A) component);
			linkLabel = ((A) component).getLabel().replaceAll(">>","");
			target = ((A) component).getTarget();
		}
		if (crumbList.isEmpty()) {
			A homeLink = new A("Home");
			homeLink.setTarget("viewDemography.zul");
			crumbList.add(homeLink);
			crumbList.add(component);
			Sessions.getCurrent().setAttribute("crumbList", crumbList);
		}else
		{
			crumbList.add(component);
			

		}

		for (int i = 0; i < crumbList.size(); i++) {
			if ((linkLabel.equals(((A) crumbList.get(i)).getLabel()))) 
			{
				i = i + 1;
				while (i < crumbList.size()) 
				{
					for (int j = i; j < crumbList.size(); i++) 
					{
						crumbList.remove(j);
						Sessions.getCurrent().setAttribute("crumbList", crumbList);
					}
				}
				break;
			}

		}

		validate = connectSession(target, component);
		Executions.sendRedirect(target);
		if (validate) {
			map.put("model", crumbList);
			Executions.createComponents("breadTest.zul", breadCrunmId, map);

		}
	}

	@Command("initializeBredCrumb")
	public void initializeBredCrumb(){
		//breadCrunmId.getChildren().clear();
		Map<String, Object> arg = new HashMap<String, Object>() ;
		arg.put("model", Sessions.getCurrent().getAttribute("crumbList"));
		Executions.createComponents("breadTest.zul", breadCrunmId, arg);
	}

	private boolean connectSession(String link, Component str) {
		if (Sessions.getCurrent().getAttribute("strComp") != null) {
			Sessions.getCurrent().removeAttribute("strComp");
			return true;
		}
		Sessions.getCurrent().setAttribute("strComp", str);
		Sessions.getCurrent().setAttribute("crumbList", crumbList);
		Executions.sendRedirect(link);
		return false;
	}

	@Command("connectChild")
	public void connectChild(@BindingParam("component")Component component) {
		breadCrunmId.getChildren().clear();
		String linkLabel = "";
		String target = "";

		if (component instanceof A) {
			component = ((A) component);
			linkLabel = ((A) component).getLabel().replaceAll(">>","");
			target = ((A) component).getTarget();
		}
		for (int i = 0; i < crumbList.size(); i++) {
			if ((linkLabel.equals(((A) crumbList.get(i)).getLabel()))) 
			{
				i = i + 1;
				while (i < crumbList.size()) 
				{
					for (int j = i; j < crumbList.size(); i++) 
					{
						crumbList.remove(j);
						Sessions.getCurrent().setAttribute("crumbList", crumbList);
					}
				}
				break;
			}

		}

		validate = connectSession(target, component);
		Executions.sendRedirect(target);
		if (validate) {
			map.put("model", crumbList);
			//Executions.createComponents("breadTest.zul", breadCrunmId, map);

		}
	}

}