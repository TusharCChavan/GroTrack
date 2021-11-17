package com.clinicapp.ui.Treatment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.clinicapp.R;
import com.clinicapp.databinding.FragmentReturnOrShootHairBinding;
import com.clinicapp.databinding.FragmentTreatmentSelectionBinding;
import com.clinicapp.utilities.BaseFragment;

public class Treatment_Fragment extends BaseFragment {
    private FragmentTreatmentSelectionBinding views;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        views = FragmentTreatmentSelectionBinding.inflate(getLayoutInflater(),container,false);


        initViews();

        return views.getRoot();
    }

    private void initViews(){

        views.rlRefermd.setOnClickListener(view ->
                {

                        Navigation.findNavController(view)
                          .navigate(R.id.action_md_refer);


            });

    views.rlProduct.setOnClickListener(view ->
                {

                        Navigation.findNavController(view)
                          .navigate(R.id.action_product_viewpager);


            });

   views.rlScalp.setOnClickListener(view ->
                {

                        Navigation.findNavController(view)
                          .navigate(R.id.action_scalp_treatment);



            });


    }
}