package org.nthdimenzion.zk.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;

public class BreadCrumb extends Div {
	
	private List<Component> crumbList = (List<Component>) Sessions.getCurrent().getAttribute("crumbList");
	
	private boolean validate = true;
	
	private Map<String, List<Component>> map = new HashMap<String, List<Component>>();

	public BreadCrumb() {
		super();
		refreshBreadCrumb();
		this.addEventListener("onPush", new PushEventListener());
		this.addEventListener("onPop", new PopEventListener());
	}

	private class PushEventListener implements EventListener<Event> {

		@Override
		public void onEvent(Event event) throws Exception {
			Component component = (Component) event.getData();
			BreadCrumb.this.getChildren().clear();
			String linkLabel = "";
			String target = "";
			crumbList = (List<Component>) Sessions.getCurrent().getAttribute("crumbList");
			if (crumbList == null)
				crumbList = new ArrayList<Component>();
			if (component instanceof A) {
				component = ((A) component);
				linkLabel = ((A) component).getLabel().replaceAll(">>","");
				target = ((A) component).getHref();
			}
			if (crumbList.isEmpty()) {
				A homeLink = new A("Home");
				homeLink.setHref("viewDemography.zul");
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
				Executions.createComponents("/component/viewBreadCrumb.zul", BreadCrumb.this, map);
			}
		}

	}

	private class PopEventListener implements EventListener<Event> {

		@Override
		public void onEvent(Event event) throws Exception {
			
			Component component = (Component) event.getData();
			String linkLabel = "";
			String target = "";
  
			if (component instanceof A) {
				component = ((A) component);
				linkLabel = ((A) component).getLabel().replaceAll(">>","");
				target = ((A) component).getHref();
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
			BreadCrumb.this.getChildren().clear();
			if (validate) {
				map.put("model", crumbList);
				Executions.createComponents("/component/viewBreadCrumb.zul", BreadCrumb.this, map);
			}
		}

	}
	
	private void refreshBreadCrumb(){
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("model", Sessions.getCurrent().getAttribute("crumbList"));
		Executions.createComponents("/component/viewBreadCrumb.zul", BreadCrumb.this, arg);
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

	private static final long serialVersionUID = 1L;

}
