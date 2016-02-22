package org.nthdimenzion.zk.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.util.UtilReflection;
import org.springframework.util.StringUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import com.google.common.collect.Lists;
import com.patientadmission.domain.IcdElement;
import com.patientadmission.domain.Procedure;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

public class AutoSuggestionBox<T> extends Combobox {

	private static final long serialVersionUID = 1L;

	private Collection<T> entities;

	private String fieldName;

	private String renderer;
	
	private T entity;
	
	private String buttonVisibility= "false";
	
	private PatientAdmissionFinder patientAdmissionFinder; 
	
	private ICrud crudDao;
	
	private String name;
	
	public AutoSuggestionBox() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		super();
		this.addEventListener("onChanging", filteredEntities());
		this.addEventListener("onCreate", setCustomItemRenderer());
		this.setAutodrop(true);
		setFocus(true);
	//	this.addEventListener("onClick", displayAllEntities());
		this.addEventListener(Events.ON_CHANGE, new OnChangeEventListener());
	}
	
	private class OnChangeEventListener implements EventListener<InputEvent>{

		@Override
		public void onEvent(InputEvent event) throws Exception {
			if(UtilValidator.isEmpty(entities))
				return;
		String value = event.getValue();
		for(T entity:entities){
			String fieldValue = (String) UtilReflection.getFieldValue(entity, fieldName);
			if(StringUtils.startsWithIgnoreCase(fieldValue, org.apache.commons.lang.StringUtils.trim(value))){
				Comboitem comboitem = new Comboitem(value);
				comboitem.setParent(AutoSuggestionBox.this);
				comboitem.setValue(entity);
				setSelectedItem(comboitem);
				break;
			}
		}		
		}
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
	
	private EventListener<? extends Event> setCustomItemRenderer() {
		EventListener<Event> createListener = new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				setButtonVisible(Boolean.valueOf(buttonVisibility));
				if(UtilValidator.isNotEmpty(renderer)){
					Class<?> comboItemClass = Class.forName(renderer);
					setItemRenderer((ComboitemRenderer<?>)comboItemClass.newInstance());
					}
			}
		};
		return createListener;
	}

	public EventListener<Event> displayAllEntities() {
		EventListener<Event> displayAllEntitiesListener = new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				getItems().clear();
				List<T> model = new ArrayList<T>(entities);
				List<T> list = Lists.newArrayList();
				if(UtilValidator.isEmpty(entities))
					return;
				for (T entity : model){
					if (UtilValidator.isEmpty(renderer)){
						String label = ((String) UtilReflection.getFieldValue(entity,fieldName));
						appendItem(label).setValue(entity);
					}
					else
						list.add(entity);
				}
				if (UtilValidator.isNotEmpty(renderer))
					setModel(new BindingListModelList<T>(model, true));				
			}
		};
		return displayAllEntitiesListener;
	}

	public EventListener<InputEvent> filteredEntities() {
		EventListener<InputEvent> onChangingListener = new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				String value = (String) event.getValue();
				getItems().clear();
				if(UtilValidator.isEmpty(entities))
					entities = new ArrayList<T>();
				if(UtilValidator.isNotEmpty(entities))
					entities.clear();
				if (UtilValidator.isEmpty(value))
					return;
				if(value.length() < 3)
					return;
				List<Map<String, ?>> maps = null;
				if(name.equals("com.patientadmission.domain.IcdElement"))
					entity = (T) new IcdElement();
				else
					if(name.equals("com.patientadmission.domain.Procedure"))
						entity = (T)new Procedure();
				if(entity instanceof IcdElement){
				maps =	patientAdmissionFinder.getIcdElementByDescription(value);
				for(Map<String,?> map : maps)
					entities.add((T) crudDao.find(entity.getClass(), (Long)map.get("id")));
				}
				else if(entity instanceof Procedure){
					maps =	patientAdmissionFinder.getProceduresByDescription(value);
					for(Map<String,?> map : maps)
						entities.add((T) crudDao.find(entity.getClass(), (String)map.get("id")));
				}
				List<T> list = Lists.newArrayList();
				if(UtilValidator.isNotEmpty(entities))
				for (T entity : entities) {
					String fieldValue = (String) UtilReflection.getFieldValue(entity, fieldName);
						if (UtilValidator.isEmpty(renderer)){
							appendItem(fieldValue).setValue(entity);
						}
						else
							list.add(entity);
					}
				if (UtilValidator.isNotEmpty(renderer))
					setModel(new BindingListModelList<T>(list, true));
			}
		};
		return onChangingListener;
	}

	public Collection<T> getEntities() {
		return entities;
	}

	public void setEntities(Collection<T> entities) {
		this.entities = entities;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getRenderer() {
		return renderer;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public String getButtonVisibility() {
		return buttonVisibility;
	}

	public void setButtonVisibility(String buttonVisibility) {
		this.buttonVisibility = buttonVisibility;
	}

	public PatientAdmissionFinder getPatientAdmissionFinder() {
		return patientAdmissionFinder;
	}

	public void setPatientAdmissionFinder(
			PatientAdmissionFinder patientAdmissionFinder) {
		this.patientAdmissionFinder = patientAdmissionFinder;
	}

	public ICrud getCrudDao() {
		return crudDao;
	}

	public void setCrudDao(ICrud crudDao) {
		this.crudDao = crudDao;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
