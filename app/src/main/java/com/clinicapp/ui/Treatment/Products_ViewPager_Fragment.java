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

import com.clinicapp.adapters.Products_ViewPagerAdapter;
import com.clinicapp.databinding.FragmentPurchaseProductViewpagerBinding;
import com.clinicapp.models.Patient;
import com.clinicapp.models.ProductsResponse;
import com.clinicapp.ui.Treatment.viewmodel.Products_ViewPager_ViewModel;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.Utils;

public class Products_ViewPager_Fragment extends Fragment {
    private Products_ViewPager_ViewModel viewModel;
    private FragmentPurchaseProductViewpagerBinding views;
    private MainViewModel mainViewModel;
    private Products_ViewPagerAdapter viewPagerAdapter;
    private Patient patient;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        views = FragmentPurchaseProductViewpagerBinding.inflate(getLayoutInflater(),container,false);

        return views.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        viewModel = new ViewModelProvider(this).get(Products_ViewPager_ViewModel.class);

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
                        final ProductsResponse groScaleResponse = result.pop();
                        handleSearchResults(groScaleResponse);
                    }
                });

        get_products();

    }


    private void handleSearchResults(ProductsResponse groScaleResponse) {
        Log.e( "SearchResults:", groScaleResponse.getStatus()+"");
        views.progressCircular.setVisibility(View.GONE);
        views.pager.setVisibility(View.VISIBLE);
        viewPagerAdapter=new Products_ViewPagerAdapter( this, groScaleResponse.getProducts());
        views.pager.setAdapter(viewPagerAdapter);
    }

    private void get_products(){
        viewModel.Get_Products();
    }
}