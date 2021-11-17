package com.clinicapp.ui.Treatment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.clinicapp.adapters.MD_Refer_ViewPagerAdapter;
import com.clinicapp.adapters.Products_ViewPagerAdapter;
import com.clinicapp.databinding.FragmentPurchaseProductViewpagerBinding;
import com.clinicapp.databinding.FragmentReferMdViewpagerBinding;
import com.clinicapp.models.MDClinics;
import com.clinicapp.models.MDResponse;
import com.clinicapp.models.Patient;
import com.clinicapp.models.Products;
import com.clinicapp.models.ProductsResponse;
import com.clinicapp.ui.Treatment.viewmodel.Products_ViewPager_ViewModel;
import com.clinicapp.ui.Treatment.viewmodel.Refer_MD_ViewPager_ViewModel;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseFragment;
import com.clinicapp.utilities.Utils;

import java.util.List;

public class Refer_MD_ViewPager_Fragment extends BaseFragment {
    private Refer_MD_ViewPager_ViewModel viewModel;
    private FragmentReferMdViewpagerBinding views;
    private MainViewModel mainViewModel;
    private MD_Refer_ViewPagerAdapter viewPagerAdapter;
    private Patient patient;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        views = FragmentReferMdViewpagerBinding.inflate(getLayoutInflater(),container,false);

        return views.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        viewModel = new ViewModelProvider(this).get(Refer_MD_ViewPager_ViewModel.class);

        initViews();
    }

    private void initViews() {
        patient = mainViewModel.getSelectedPatient();


        viewModel.getApiLiveData()
                .observe(getViewLifecycleOwner(), result -> {

                    if(result.isError()) {
                        Utils.notify(getActivity(), result.message);
                    } else if(result.isSuccess() && result.isFresh()){
                        //Use pop as we want to act on the result only when it's fresh.
                        final MDResponse groScaleResponse = result.pop();
                        handleSearchResults(groScaleResponse.getClinics());
                    }
                });

        get_md();

    }


    private void handleSearchResults(List<MDClinics> groScaleResponse) {
        views.progressCircular.setVisibility(View.GONE);
        views.pager.setVisibility(View.VISIBLE);
        viewPagerAdapter=new MD_Refer_ViewPagerAdapter( this, groScaleResponse);
        views.pager.setAdapter(viewPagerAdapter);
    }

    private void get_md(){
        viewModel.get_md();
    }
}