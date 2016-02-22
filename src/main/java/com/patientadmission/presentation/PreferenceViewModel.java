package com.patientadmission.presentation;

import com.google.common.collect.Lists;
import com.patientadmission.command.UpdatePreferenceCommand;
import com.patientadmission.domain.Country;
import com.patientadmission.domain.Preference;
import com.patientadmission.domain.State;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;
import java.util.Map;

@Composer
public class PreferenceViewModel extends AbstractZKModel {

    private UpdatePreferenceCommand updatePreferenceCommand = new UpdatePreferenceCommand();

    private List<Country> countries;

    private List<State> states = Lists.newArrayList();

    private List<String> cities = Lists.newArrayList();

    @WireVariable
    private ICrud crudDao;

    private Long preferenceId;

    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;

    private Country selectedCountry;

    private State selectedState;

    private String selectedCity;

    @Init
    public void init() {
        countries = crudDao.getAll(Country.class);
        List<Preference> preferences = crudDao.getAll(Preference.class);
        if (preferences.size() == 1) {
            Preference selectedPreference = preferences.get(0);
            preferenceId = selectedPreference.getId();
            selectedCountry = selectedPreference.getCountry();
            selectedState = selectedPreference.getState();
            selectedCity = selectedPreference.getCity();
        }
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

    @Command
    @NotifyChange({"states","selectedState"})
    public void selectCountry() {
        if (selectedCountry == null) return;
        selectedState = null;
        states.clear();
        updatePreferenceCommand.countryId = selectedCountry.getId();
        populateStates();
    }

    private void populateStates() {
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

    @Command
    @NotifyChange({"cities","selectedCity"})
    public void selectState() {
        if (selectedState == null) return;
        selectedCity = null;
        updatePreferenceCommand.stateId = selectedState.getId();
        populateCities();
    }

    private void populateCities() {
        cities.clear();
        List<Map<String, ?>> cityMapList = patientAdmissionFinder.findAllCitiesInState(selectedState.getId());
        for (Map<String, ?> m : cityMapList) {
            for (Map.Entry<String, ?> mEntry : m.entrySet()) {
                if ("cities".equals(mEntry.getKey())) {
                    cities.add((String) mEntry.getValue());
                }
            }
        }
    }

    @Command
    public void selectCity() {
        if (selectedCity == null)
            return;
        updatePreferenceCommand.city = selectedCity;
    }

    @Command
    public void updatePreference() {
        updatePreferenceCommand.preferenceId = preferenceId;
        updatePreferenceCommand.countryId = selectedCountry.getId();
        updatePreferenceCommand.stateId = selectedState.getId();
        updatePreferenceCommand.city = selectedCity;
        if (isSuccess(sendCommand(updatePreferenceCommand)))
            displayMessages.showSuccess();
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public State getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(State selectedState) {
        this.selectedState = selectedState;
    }

    public String getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity = selectedCity;
    }
}
