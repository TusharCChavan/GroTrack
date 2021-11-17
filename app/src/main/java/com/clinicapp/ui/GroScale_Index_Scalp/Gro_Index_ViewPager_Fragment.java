package com.clinicapp.ui.GroScale_Index_Scalp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.clinicapp.adapters.GroIndex_ViewPagerAdapter;
import com.clinicapp.databinding.FragmentGrowScaleViewpagerBinding;
import com.clinicapp.models.GroScaleResponse;
import com.clinicapp.models.Patient;
import com.clinicapp.ui.GroScale_Index_Scalp.viewmodel.GroIndex_ViewPager_ViewModel;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.Utils;

public class Gro_Index_ViewPager_Fragment extends Fragment {
    private GroIndex_ViewPager_ViewModel viewModel;
    private FragmentGrowScaleViewpagerBinding views;
    private MainViewModel mainViewModel;
    private GroIndex_ViewPagerAdapter viewPagerAdapter;
    private Patient patient;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        views = FragmentGrowScaleViewpagerBinding.inflate(getLayoutInflater(),container,false);

        return views.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        viewModel = new ViewModelProvider(this).get(GroIndex_ViewPager_ViewModel.class);

        initViews();
    }

    private void initViews() {
        patient = mainViewModel.getSelectedPatient();

        views.patientName.setText(patient.getName());

        viewModel.getApiLiveData()
                .observe(getViewLifecycleOwner(), result -> {

                    if(result.isError()) {
                        Utils.notify(getActivity(), result.message);
                    } else if(result.isSuccess() && result.isFresh()){
                        //Use pop as we want to act on the result only when it's fresh.
                        final GroScaleResponse groScaleResponse = result.pop();
                        handleSearchResults(groScaleResponse);
                    }
                });

        get_groscale();



    }


    private void handleSearchResults(GroScaleResponse groScaleResponse) {
        Log.e( "SearchResults:", groScaleResponse.getStatus()+"");
        views.progressCircular.setVisibility(View.GONE);
        views.pager.setVisibility(View.VISIBLE);
        viewPagerAdapter=new GroIndex_ViewPagerAdapter( this, groScaleResponse);
        views.pager.setAdapter(viewPagerAdapter);
    }

    private void get_groscale(){
        viewModel.Get_Gro_Scale(mainViewModel.getSelectedPatient().getId());
    }
}