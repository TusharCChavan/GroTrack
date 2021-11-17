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
import androidx.viewpager2.widget.ViewPager2;

import com.clinicapp.adapters.GroScale_ViewPagerAdapter;
import com.clinicapp.databinding.FragmentGrowScaleViewpagerBinding;
import com.clinicapp.models.GroScaleResponse;
import com.clinicapp.models.Patient;
import com.clinicapp.ui.GroScale_Index_Scalp.viewmodel.GroScale_ViewPager_ViewModel;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.Utils;

public class Gro_Scale_ViewPager_Fragment extends Fragment {
    private GroScale_ViewPager_ViewModel viewModel;
    private FragmentGrowScaleViewpagerBinding views;
    private MainViewModel mainViewModel;
    private GroScale_ViewPagerAdapter viewPagerAdapter;
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
        viewModel = new ViewModelProvider(this).get(GroScale_ViewPager_ViewModel.class);

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
        views.rightArrow.setVisibility(View.GONE);
        views.leftArrow.setVisibility(View.GONE);
        views.progressCircular.setVisibility(View.GONE);
        views.pager.setVisibility(View.VISIBLE);
        viewPagerAdapter=new GroScale_ViewPagerAdapter( this, groScaleResponse);
        views.pager.setAdapter(viewPagerAdapter);

        if (groScaleResponse.getAll_analysis().size()>1){
            views.rightArrow.setVisibility(View.VISIBLE);

        }
        views.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.e( "onPageScrolled: ", position+"");
                if (position==0){
              views.leftArrow.setVisibility(View.GONE);
                }
            }
        });

    }

    private void get_groscale(){
        viewModel.Get_Gro_Scale(mainViewModel.getSelectedPatient().getId());
    }
}