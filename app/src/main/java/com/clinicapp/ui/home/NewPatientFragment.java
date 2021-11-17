package com.clinicapp.ui.home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.clinicapp.R;
import com.clinicapp.databinding.FragmentNewPatientBinding;
import com.clinicapp.models.AddPatientResponse;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.ui.home.viewmodels.NewPatientViewModel;
import com.clinicapp.utilities.BaseFragment;
import com.clinicapp.utilities.PostTextWatcher;
import com.clinicapp.utilities.Utils;

import java.util.Calendar;

import kotlin.Pair;

public class NewPatientFragment extends BaseFragment {
    private FragmentNewPatientBinding views;
    private NewPatientViewModel viewModel;
    private MainViewModel mainViewModel;
    private static final String TAG = "NewPatientFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = FragmentNewPatientBinding.inflate(getLayoutInflater(),container,false);
        views.form.txtTitle.setText(R.string.create_new_patient);
        return views.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NewPatientViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        init();
    }


    private void init() {
        new PostTextWatcher(views.form.firstname, this::handleFirstNameChange);
        new PostTextWatcher(views.form.lastname, this::handleLastNameChange);
        new PostTextWatcher(views.form.phone, this::handlePhoneChange);

        views.form.dob.setOnClickListener(this::showDatePicker);
        views.form.gender.setOnClickListener(view -> selectgender(getChildFragmentManager()));
        views.addPatient.setOnClickListener(this::addPatient);

        viewModel.getApiLiveData()
                .observe(getViewLifecycleOwner(), result -> {
                    swapButtonAndLoader(result.isLoading());
                    setViewState(!result.isLoading(), views.form.firstname, views.form.lastname,
                            views.form.gender, views.form.dob, views.form.phone);
                    if(result.isError()) {
                        handleApiError(result.value, result.message);

                    } else if( result.isSuccess() && result.value.getStatus()) {

                        handleSuccessApiResponse();

                    }
                });
    }

    private void handleSuccessApiResponse(){
        mainViewModel.onPatientAdded(viewModel.getPatient());
        Navigation.findNavController(views.getRoot())
                .navigate(R.id.action_newPatientFragment_to_selectShootPosition);
    }


    private void handleApiError(AddPatientResponse response, String message) {
        if(!response.showFieldError()){
            Utils.notify(getActivity(), message); } else {
//            Toast.makeText(getActivity(), response.getDOBError(), Toast.LENGTH_SHORT).show();
            showError(views.form.firstnameErr, response.getFirstNameError());
            showError(views.form.lastnameErr, response.getLastNameError());
            showError(views.form.phoneErr, response.getPhoneError());
        }
    }


    private void swapButtonAndLoader(boolean isLoading){
        views.progressBar.setVisibility(isLoading ? View.VISIBLE:View.INVISIBLE);
        views.addPatient.setVisibility(isLoading ? View.INVISIBLE:View.VISIBLE);
    }


    private boolean handleFirstNameChange(String value){
        final Pair<Boolean,String> valResult = viewModel.validateFirstName(value);
        return handleValidation(views.form.firstnameErr, valResult);
    }

    private boolean handleLastNameChange(String s) {
        final Pair<Boolean,String> valResult = viewModel.validateLastName(s);
        return handleValidation(views.form.lastnameErr, valResult);
    }

    private boolean handlePhoneChange(String s) {
        final Pair<Boolean,String> valResult = viewModel.validatePhone(s);
        return handleValidation(views.form.phoneErr, valResult);
    }

    private boolean handleGender(String s) {
       if (s.length()>0){
           return true;
       }
            Utils.toste(getActivity(),"Please select gender");
           return false;
    }


    private void showDatePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog=     new DatePickerDialog(getActivity(), (datePicker, year1, month1, day) -> {
            month1++;
            String monthOfYear = month1 < 10 ? "0" + month1 : month1 + "";
            String date_str = day < 10 ? "0" + day : day + "";
//            views.form.dob.setText(monthOfYear + "/" + date_str + "/" + year1);
            views.form.dob.setText(year1+"-"+monthOfYear+"-"+date_str);
        }, year, month, date);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    String gender_str="";
    public void selectgender(FragmentManager fragmentManager){

        final String[] fonts = {"Male", "Female"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Gender");
        builder.setItems(fonts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Male".equals(fonts[which])) {
                    Toast.makeText(getActivity(),"Male", Toast.LENGTH_SHORT).show();
                    gender_str="M";
                    views.form.gender.setText("Male");
                }
                else if ("Female".equals(fonts[which])) {
                    Toast.makeText(getActivity(),"Female", Toast.LENGTH_SHORT).show();
                    gender_str="F";
                    views.form.gender.setText("Female");
                }

            }
        });
        builder.show();
    }

    private void addPatient(View view) {

        final String firstName = views.form.firstname.getText().toString();
        final String lastName = views.form.lastname.getText().toString();
        final String dateOfBirth = views.form.dob.getText().toString();
        final String phone = views.form.phone.getText().toString();
        final String ethnicity = views.form.Ethnicity.getText().toString();
      final String email = views.form.email.getText().toString();

        final boolean isValidFirstName = handleFirstNameChange(firstName);
        final boolean isValidLastName = handleLastNameChange(lastName);
        final boolean isValidPhone = handlePhoneChange(phone);
        final boolean isValidGender = handleGender(gender_str);

        if(isValidFirstName && isValidLastName && isValidPhone && isValidGender)
//        handleSuccessApiResponse();
            viewModel.addPatient(firstName,lastName, dateOfBirth,gender_str, phone,ethnicity,email);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Navigation.findNavController(views.form.firstname).navigate(R.id.action_returnToHomeScreen_to_homeFragment);
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

}