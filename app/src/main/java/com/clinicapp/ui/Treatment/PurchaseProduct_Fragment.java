package com.clinicapp.ui.Treatment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.clinicapp.R;
import com.clinicapp.databinding.FragmentPurchaseProductBinding;
import com.clinicapp.models.BuyProductResponse;
import com.clinicapp.models.Patient;
import com.clinicapp.models.Products;
import com.clinicapp.ui.Treatment.viewmodel.PurchaseProduct_ViewModel;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseFragment;
import com.clinicapp.utilities.Utils;

public class PurchaseProduct_Fragment extends BaseFragment {
    private FragmentPurchaseProductBinding views;
    private Products products;
    private PurchaseProduct_ViewModel viewModel;
    private MainViewModel mainViewModel;
    private Patient patient;


    public static PurchaseProduct_Fragment getInstance(Products patient){
        final PurchaseProduct_Fragment fragment = new PurchaseProduct_Fragment();
        final Bundle bundle = new Bundle();

        bundle.putParcelable(DATA,patient);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products = getArguments().getParcelable(DATA);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        views = FragmentPurchaseProductBinding.inflate(getLayoutInflater(),container,false);
        viewModel = new ViewModelProvider(this).get(PurchaseProduct_ViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);


        initViews();

        return views.getRoot();
    }

    private void initViews(){
        Glide.with(this)
                .load(products.getImage())
                .into( views.ivProduct);

        views.tvProducttital.setText(products.getName());
      views.tvProductdes.setText(products.getDescription());

        views.btpurchase.setOnClickListener(view -> {
            purchase_product();
        });
        patient = mainViewModel.getSelectedPatient();


        viewModel.getApiLiveData()
                .observe(getViewLifecycleOwner(), result -> {

                    if(result.isError()) {
                        Utils.notify(getActivity(), result.message);
                    } else if(result.isSuccess() && result.isFresh()){
                        //Use pop as we want to act on the result only when it's fresh.
                        final BuyProductResponse groScaleResponse = result.pop();
                        handleSearchResults(groScaleResponse);
                    }
                });


    }

    private void handleSearchResults(BuyProductResponse groScaleResponse) {
        if(groScaleResponse.getStatus()){
            Navigation.findNavController(views.progressCircular)
                    .navigate(R.id.action_returnToHomeScreen_to_homeFragment, getArguments());

            Toast.makeText(getActivity(), "Product purchased successfully.", Toast.LENGTH_SHORT).show();
        }

    }

    private void purchase_product(){
        views.progressCircular.setVisibility(View.VISIBLE);
        views.btpurchase.setVisibility(View.INVISIBLE);
        viewModel.purchase_product(products.getId(),patient.getId());
    }
}