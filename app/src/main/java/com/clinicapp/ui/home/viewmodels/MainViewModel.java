package com.clinicapp.ui.home.viewmodels;

import android.app.Application;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;

import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.Patient;
import com.clinicapp.utilities.BaseViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainViewModel extends BaseViewModel {

    private List<Patient> searchResults = new ArrayList<>();
    private Patient selectedPatient;
    private long hairAnalysisID;
    private ArrayList<CameraPositions> selectedPositions = new ArrayList<>();

    public long getHairAnalysisID() {
        return hairAnalysisID;
    }

    public void setHairAnalysisID(long hairAnalysisID) {
        this.hairAnalysisID = hairAnalysisID;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void onPatientAdded(Patient patient){
        this.selectedPatient = patient;
    }

    public void setSearchResult(List<Patient> patients){
        this.searchResults = patients;
    }

    public List<Patient> getSearchResults() {
        return searchResults;
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    public void setCameraPositions(ArrayList<CameraPositions> selectedPositions) {
        this.selectedPositions = selectedPositions;
    }

    public ArrayList<CameraPositions> getSelectedPositions() {
        return selectedPositions;
    }

    public String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
}
