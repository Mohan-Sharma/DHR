package com.patientadmission.presentation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilDateTime;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.domain.IUserLoginRepository;
import org.nthdimenzion.security.domain.IsADoctor;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.master.domain.Enumeration;
import com.master.services.IMasterService;
import com.patientadmission.command.RegisterPatientCommand;
import com.patientadmission.domain.Country;
import com.patientadmission.domain.Preference;
import com.patientadmission.domain.State;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class PatientViewModel extends AbstractZKModel {

    private RegisterPatientCommand registerPatientCommand = new RegisterPatientCommand();

    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;

    @WireVariable
    private IMasterService masterService;

    @WireVariable
    private ICrud crudDao;

    private List<Enumeration> titles = Lists.newArrayList();

    private List<Enumeration> guardianRelationShips = Lists.newArrayList();

    private List<Country> countries = Lists.newArrayList();

    private List<State> states = Lists.newArrayList();

    private List<String> cities = Lists.newArrayList();

    private Enumeration selectedTitle;

    private Enumeration selectedGuardianRelationShip;

    private Country selectedCountry;

    private State selectedState;

    private String selectedCity;

    private String gender;

    private boolean patientExists;

    private Map<String, ?> patientDetailsMap;

    private String calculatedAge;

    private boolean addAdmissionVisible;

    Map<String, String> map = new HashMap<String, String>();

    private Enumeration newMaster = null;
    
    private Integer enteredAge;
    
    private String ageSelection;
    
    String user;
    
    @Wire("#saveDiv")
    private Div saveDiv;
    
    private IsADoctor isADoctor;
    @WireVariable
	private IUserLoginRepository userLoginRepository;
    
    @Wire("#viewPatientDetalsBtn")
    private A viewPatientDetalsBtn;
    
    private Preference preference = null;
    
    private List<Preference> preferences;
    
    private String dataOp = "dataop";


    public PatientViewModel() {
        newMaster = new Enumeration();
        newMaster.setDescription("New");
        newMaster.setEnumCode(null);
    }

	@AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        isADoctor = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername());
         user  = loggedInUser.getName().toString();
         
         if(user.equals(dataOp)||isADoctor.isADoctor()){
        	 saveDiv.setVisible(true);
         }
        titles = masterService
                .getEnumerationByEnumType(Enumeration.EnumerationType.TITLE
                        .toString());
        guardianRelationShips = masterService
                .getEnumerationByEnumType(Enumeration.EnumerationType.GUARDIAN_RELATIONSHIP
                        .toString());
        guardianRelationShips.add(0, newMaster);
        List<Map<String, ?>> countryMapList = patientAdmissionFinder
                .findAllCountries();
        for (Map<String, ?> m : countryMapList) {
            for (Map.Entry<String, ?> mEntry : m.entrySet()) {
                if ("id".equals(mEntry.getKey())) {
                    countries.add((Country) crudDao.find(Country.class,
                            (Long) mEntry.getValue()));
                }
            }
        }
        preferences = crudDao.getAll(Preference.class);
        if(UtilValidator.isNotEmpty(preferences))
        preference = preferences.get(0);
        populateCountriesAndStates();
        String mrnNumber = Executions.getCurrent().getParameter("mrnNumber");
        if(UtilValidator.isNotEmpty(mrnNumber)){
        	registerPatientCommand.medicalRecordNumber = mrnNumber;
        	findPatient();
        }
    }
	
	private void populateCountriesAndStates(){
    if(preference != null){
        this.selectedCountry = preference.getCountry();
        populateStates();
        this.selectedState = preference.getState();
        populateCities();
        this.selectedCity = preference.getCity();
        registerPatientCommand.countryId = this.selectedCountry.getId();
        registerPatientCommand.stateId = this.selectedState.getId();
        registerPatientCommand.city = this.selectedCity;
        }
	}

    public RegisterPatientCommand getRegisterPatientCommand() {
        return registerPatientCommand;
    }

    @Command
    @NotifyChange("addAdmissionVisible")
    public void registerPatient() {
        registerPatientCommand.medicalRecordNumber = registerPatientCommand.medicalRecordNumber.trim();
        if (UtilValidator.isEmpty(registerPatientCommand.medicalRecordNumber)) {
            displayMessages.displayError("MRN Number Required");
            return;
        }
        if (isSuccess(sendCommand(registerPatientCommand))) {
            displayMessages.displaySuccess();
            Map<String, String> map = Maps.newHashMap();
            map.put("mrnNumber", registerPatientCommand.medicalRecordNumber);
            navigation.redirect("patientBanner", map);
            addAdmissionVisible = true;
        }
    }

    public PatientAdmissionFinder getPatientAdmissionFinder() {
        return patientAdmissionFinder;
    }

    public List<Enumeration> getTitles() {
        return titles;
    }

    public List<Enumeration> getGuardianRelationShips() {
        return guardianRelationShips;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public List<State> getStates() {
        return states;
    }

    public List<String> getCities() {
        return cities;
    }

    public Enumeration getSelectedTitle() {
        return selectedTitle;
    }

    @Command
    @NotifyChange("gender")
    public void selectTitle() {
        if (selectedTitle == null) return;
        registerPatientCommand.titleCode = selectedTitle.getEnumCode();
        if ("MR".equals(selectedTitle.getEnumCode().toString()) || "MASTER".equals(selectedTitle.getEnumCode().toString()))
            gender = registerPatientCommand.gender = "MALE";
        else
            gender = registerPatientCommand.gender = "FEMALE";
    }

    public void setSelectedTitle(Enumeration selectedTitle) {
        this.selectedTitle = selectedTitle;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }


    public State getSelectedState() {
        return selectedState;
    }

    public String getSelectedCity() {
        return selectedCity;
    }

    public Enumeration getSelectedGuardianRelationShip() {
        return selectedGuardianRelationShip;
    }

    @Command
    public void selectGuardianRelationShip(BindContext ctx) {
        Combobox combobox = (Combobox) ctx.getComponent();
        combobox.addEventListener("onReload", new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                selectedGuardianRelationShip = ((Enumeration) event.getData());
                if(selectedGuardianRelationShip != null)
                registerPatientCommand.guardianRelationShipCode = selectedGuardianRelationShip.getEnumCode();
            }
        });
        if (combobox.getSelectedItem() == null) return;
        String code = ((Combobox) ctx.getComponent()).getSelectedItem().getValue();
        if (code == null) {
            Component targetComp = ctx.getComponent();
            Map viewArgs = new HashMap();
            viewArgs.put("selectedEnumType", com.master.domain.Enumeration.EnumerationType.GUARDIAN_RELATIONSHIP);
            viewArgs.put("masterName", "Guardian Relationship");
            viewArgs.put("targetComp", targetComp);
            viewArgs.put("masterList", guardianRelationShips);
            Window window = navigation.openModalWindow("createMasterData", viewArgs);
            window.addEventListener("onDetach",new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    selectedGuardianRelationShip = null;
                }
            });
        } else
            registerPatientCommand.guardianRelationShipCode = code;
    }

    @Command
    @NotifyChange("states")
    public void selectCountry() {
        if (selectedCountry == null) return;
        registerPatientCommand.countryId = selectedCountry.getId();
        populateStates();
    }

    private void populateStates() {
        states.clear();
        selectedState=null;
        if(selectedCountry != null){
        List<Map<String, ?>> stateMapList = patientAdmissionFinder.findAllStatesInCountry(selectedCountry.getId());
        for (Map<String, ?> m : stateMapList) {
            for (Map.Entry<String, ?> mEntry : m.entrySet()) {
                if ("id".equals(mEntry.getKey())) {
                    states.add((State) crudDao.find(State.class,
                            (Long) mEntry.getValue()));
                }
            }
        }
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public void setRegisterPatientCommand(
            RegisterPatientCommand registerPatientCommand) {
        this.registerPatientCommand = registerPatientCommand;
    }

    public void setTitles(List<Enumeration> titles) {
        this.titles = titles;
    }

    public void setGuardianRelationShips(List<Enumeration> guardianRelationShips) {
        this.guardianRelationShips = guardianRelationShips;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public void setSelectedGuardianRelationShip(
            Enumeration selectedGuardianRelationShip) {
        this.selectedGuardianRelationShip = selectedGuardianRelationShip;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public void setSelectedState(State selectedState) {
        this.selectedState = selectedState;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity = selectedCity;
    }

    @Command
    @NotifyChange("cities")
    public void selectState() {
        if (selectedState == null) return;
        registerPatientCommand.stateId = selectedState.getId();
        populateCities();
    }

    private void populateCities() {
        cities.clear();
        selectedCity=null;
        if(selectedState != null){
        List<Map<String, ?>> cityMapList = patientAdmissionFinder.findAllCitiesInState(selectedState.getId());
        for (Map<String, ?> m : cityMapList) {
            for (Map.Entry<String, ?> mEntry : m.entrySet()) {
                if ("cities".equals(mEntry.getKey())) {
                    cities.add((String) mEntry.getValue());
                }
            }
        }
        }
    }

    @Command
    public void selectCity() {
        if (selectedCity == null) return;
        registerPatientCommand.city = selectedCity;
    }

    @Command
    @NotifyChange({"patientDetailsMap", "patientExists", "map", "registerPatientCommand", "gender",
            "selectedTitle", "selectedGuardianRelationShip", "selectedState", "selectedCountry", "states", "cities", "selectedCity", "calculatedAge", "addAdmissionVisible",
            "enteredAge","ageSelection"})
    public void findPatient() {
        patientDetailsMap = patientAdmissionFinder.getPatientByMRN(registerPatientCommand.medicalRecordNumber);
        if (UtilValidator.isEmpty(patientDetailsMap)){
        	String mrnNumber = registerPatientCommand.medicalRecordNumber;
        	registerPatientCommand = new RegisterPatientCommand();
        	registerPatientCommand.medicalRecordNumber = mrnNumber;
        	selectedTitle = null;
        	selectedGuardianRelationShip = null;
        	enteredAge = null;
        	ageSelection = null;
        	gender = null;
            viewPatientDetalsBtn.setVisible(false);
            populateCountriesAndStates();
        	return;
        }
        this.gender = patientDetailsMap.get("gender") != null ? (String) patientDetailsMap.get("gender") : "";
        registerPatientCommand = new RegisterPatientCommand
                ((String) patientDetailsMap.get("first_name"), (String) patientDetailsMap.get("middle_name"), (String) patientDetailsMap.get("last_name"),
                        (Date) patientDetailsMap.get("dob"), (String) patientDetailsMap.get("guardianName"),
                        (String) patientDetailsMap.get("addr1"), (String) patientDetailsMap.get("addr2"), (String) patientDetailsMap.get("addr3"),
                        (String) patientDetailsMap.get("cityName"), (String) patientDetailsMap.get("contact_number"), (String) patientDetailsMap.get("medicalRecordNumber"));
        if (patientDetailsMap.get("countryId") != null)
            registerPatientCommand.countryId = (Long) patientDetailsMap.get("countryId");
        if (patientDetailsMap.get("stateId") != null)
            registerPatientCommand.stateId = (Long) patientDetailsMap.get("stateId");
        registerPatientCommand.gender = this.gender;
        if (patientDetailsMap.get("patientId") != null)
            registerPatientCommand.patientId = (Long) patientDetailsMap.get("patientId");
        if (patientDetailsMap.get("title") != null) {
            Enumeration title = crudDao.find(Enumeration.class, (Long) patientDetailsMap.get("title"));
            this.selectedTitle = title;
            registerPatientCommand.titleCode = title.getEnumCode();
        }
        if (patientDetailsMap.get("guardianrelationship") != null) {
            Enumeration guardianRelationShip = crudDao.find(Enumeration.class, (Long) patientDetailsMap.get("guardianrelationship"));
            this.selectedGuardianRelationShip = guardianRelationShip;
            registerPatientCommand.guardianRelationShipCode = guardianRelationShip.getEnumCode();
        }
        Country country = crudDao.find(Country.class, (Long) patientDetailsMap.get("countryId"));       
        this.selectedCountry = country;
        populateStates();
        State state = crudDao.find(State.class, (Long) patientDetailsMap.get("stateId"));
        this.selectedState = state;
        populateCities();
        registerPatientCommand.city = (String) patientDetailsMap.get("cityName");
        for (String city : cities) {
            if (((String) patientDetailsMap.get("cityName")).equals(city))
                this.selectedCity = city;
        }
        calculateAge();
        if(viewPatientDetalsBtn != null){
        viewPatientDetalsBtn.setVisible(true);
        addAdmissionVisible = true;
        }
    }

    @Command
    @NotifyChange({"map", "patientExists"})
    public void viewPaientDetails() {
        patientExists = true;
        Enumeration title = crudDao.find(Enumeration.class, (Long) patientDetailsMap.get("title"));
        map.put("titleName", title.getDescription());
        Enumeration guardianRelationShip = crudDao.find(Enumeration.class, (Long) patientDetailsMap.get("guardianrelationship"));
        map.put("guardianRelName", guardianRelationShip.getDescription());
    }

    public Map<String, ?> getPatientDetailsMap() {
        return patientDetailsMap;
    }

    public boolean isPatientExists() {
    		return patientExists;
    }

    public Map<String, String> getMap() {
        return map;
    }

    @Command
    public void navigateToPatientDetails() {
        Map<String, String> map = Maps.newHashMap();
        map.put("mrnNumber", patientDetailsMap.get("medicalRecordNumber") == null ? registerPatientCommand.medicalRecordNumber : (String) patientDetailsMap.get("medicalRecordNumber"));
        map.put("patientId", ((Long) patientDetailsMap.get("patientId")).toString());
        navigation.redirect("patientBanner", map);
    }

    @Command
    @NotifyChange({"calculatedAge","registerPatientCommand","enteredAge","ageSelection"})
    public void calculateAge() {
        if (registerPatientCommand.dateOfBirth != null){
            this.calculatedAge = UtilDateTime.calculateAge(registerPatientCommand.dateOfBirth, UtilDateTime.nowDate());
            registerPatientCommand.calculatedAge = false;
            enteredAge = Integer.valueOf(calculatedAge.split(" ")[0]);
            ageSelection = calculatedAge.split(" ")[1];
        }
    }

    public String getCalculatedAge() {
        return calculatedAge;
    }

    public void setCalculatedAge(String calculatedAge) {
        this.calculatedAge = calculatedAge;
    }

    @Command
    public void addAdmission() {
        Map<String, String> map = Maps.newHashMap();
        map.put("mrnNumber", registerPatientCommand.medicalRecordNumber);
        if (UtilValidator.isNotEmpty((String) Executions.getCurrent().getParameter("patientId")))
            map.put("patientId", patientDetailsMap == null ? "" : ((Long) patientDetailsMap.get("patientId")).toString());
        navigation.redirect("addPatientAdmission", map);
    }

    public boolean isAddAdmissionVisible() {
        return addAdmissionVisible;
    }

    public void setAddAdmissionVisible(boolean addAdmissionVisible) {
        this.addAdmissionVisible = addAdmissionVisible;
    }

	public Integer getEnteredAge() {
		return enteredAge;
	}

	public void setEnteredAge(Integer enteredAge) {
		this.enteredAge = enteredAge;
	}
	
	@Command
	@NotifyChange({"registerPatientCommand","calculatedAge"})
	public void calculateDOB(){
		if(enteredAge == null || UtilValidator.isEmpty(ageSelection))
			return;
		Date nowDate = UtilDateTime.nowDate();
		Date calculatedDOB = null;
		if("Day(s)".equals(ageSelection))
			calculatedDOB = UtilDateTime.addDaysToDate(nowDate, -enteredAge);
		else if("Week(s)".equals(ageSelection)){
			calculatedDOB = UtilDateTime.addWeeksToDate(nowDate, -enteredAge);
		}else if("Month(s)".equals(ageSelection))
			calculatedDOB = UtilDateTime.addMonthsToDate(nowDate, -enteredAge);
		else if("Year(s)".equals(ageSelection))
			calculatedDOB = UtilDateTime.addYearsToDate(nowDate, -enteredAge);
		registerPatientCommand.dateOfBirth = calculatedDOB;
		registerPatientCommand.calculatedAge = true;
		calculatedAge="";
	}

	public String getAgeSelection() {
		return ageSelection;
	}

	public void setAgeSelection(String ageSelection) {
		this.ageSelection = ageSelection;
	}
}