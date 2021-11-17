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
import com.clinicapp.databinding.FragmentReferMdBinding;
import com.clinicapp.databinding.FragmentReferMdViewpagerBinding;
import com.clinicapp.models.Admin_Profile;
import com.clinicapp.models.BuyProductResponse;
import com.clinicapp.models.MDClinics;
import com.clinicapp.models.MD_ReferResponse;
import com.clinicapp.models.Patient;
import com.clinicapp.models.Products;
import com.clinicapp.ui.Treatment.viewmodel.MDRefer_ViewModel;
import com.clinicapp.ui.Treatment.viewmodel.PurchaseProduct_ViewModel;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseFragment;
import com.clinicapp.utilities.Utils;

public class MD_Refer_Fragment extends BaseFragment {
    private FragmentReferMdBinding views;
    private Admin_Profile products;
    private MDRefer_ViewModel viewModel;
    private MainViewModel mainViewModel;
    private Patient patient;
 private Long clinic_id;


    public static MD_Refer_Fragment getInstance(Admin_Profile patient,Long id){
        final MD_Refer_Fragment fragment = new MD_Refer_Fragment();
        final Bundle bundle = new Bundle();

        bundle.putParcelable(DATA,patient);
        bundle.putLong("id",id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products = getArguments().getParcelable(DATA);
        clinic_id = getArguments().getLong("id");

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        views = FragmentReferMdBinding.inflate(getLayoutInflater(),container,false);
        viewModel = new ViewModelProvider(this).get(MDRefer_ViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);


        initViews();

        return views.getRoot();
    }

    private void initViews(){
//        Glide.with(this)
//                .load(products.getImage())
//                .into( views.ivProduct);

        if (products!=null) {
            views.tvProducttital.setText(products.getPracticeName());
            views.tvProductdes.setText(products.getPhysicianName());
        }
        views.btRefer.setOnClickListener(view -> {
            refer_to_md();
        });
        patient = mainViewModel.getSelectedPatient();

        viewModel.getApiLiveData()
                .observe(getViewLifecycleOwner(), result -> {

                    if(result.isError()) {
                        Utils.notify(getActivity(), result.message);
                    } else if(result.isSuccess() && result.isFresh()){
                        //Use pop as we want to act on the result only when it's fresh.
                        final MD_ReferResponse groScaleResponse = result.pop();
                        handleSearchResults(groScaleResponse);
                    }
                });



    }


    private void handleSearchResults(MD_ReferResponse groScaleResponse) {
        if(groScaleResponse.getStatus()){
            Navigation.findNavController(views.progressCircular)
                    .navigate(R.id.action_returnToHomeScreen_to_homeFragment, getArguments());

            Toast.makeText(getActivity(), "Successfully referred.", Toast.LENGTH_SHORT).show();
        }

    }

    private void refer_to_md(){
        views.progressCircular.setVisibility(View.VISIBLE);
        views.btRefer.setVisibility(View.INVISIBLE);

            viewModel.refer_to_md(patient.getId(), clinic_id);

    }
}