package com.clinicapp.ui.GroScale_Index_Scalp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clinicapp.R;
import com.clinicapp.databinding.FragmentGrowScalpStanderedImgBinding;
import com.clinicapp.databinding.FragmentGrowScalpUserImgBinding;
import com.clinicapp.models.GroScalpMatchResponse;
import com.clinicapp.models.GroScalpResponse;
import com.clinicapp.models.GroScalp_UserDataImg;
import com.clinicapp.models.Patient;
import com.clinicapp.models.ScalpStandard;
import com.clinicapp.ui.GroScale_Index_Scalp.viewmodel.GroScaleViewModel;
import com.clinicapp.ui.GroScale_Index_Scalp.viewmodel.GroScalp_User_ViewModel;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseFragment;
import com.clinicapp.utilities.Utils;

public class Gro_Scalp_User_img_Fragment extends BaseFragment implements RequestListener {
    private MainViewModel mainViewModel;
    private FragmentGrowScalpUserImgBinding views;
    private GroScalp_UserDataImg clientAnalysis;
    private GroScalp_User_ViewModel viewModel;



    public static Gro_Scalp_User_img_Fragment getInstance(GroScalp_UserDataImg patient){
        final Gro_Scalp_User_img_Fragment fragment = new Gro_Scalp_User_img_Fragment();
        final Bundle bundle = new Bundle();

        bundle.putParcelable(DATA,patient);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientAnalysis = getArguments().getParcelable(DATA);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        views = FragmentGrowScalpUserImgBinding.inflate(getLayoutInflater(),container,false);

        return views.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        viewModel = new ViewModelProvider(getActivity()).get(GroScalp_User_ViewModel.class);

        initViews();
        initListeners();
    }

    private void initViews() {
         Patient patient = mainViewModel.getSelectedPatient();


    }




    private void initListeners() {

        views.textView3.setText(clientAnalysis.getSubType()+" ("+mainViewModel.getDate(clientAnalysis.getCreated_at())+")");
        onClickShoot();



    }


    private void onClickShoot() {
        views.pbImage.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(clientAnalysis.getImg())
                    .listener(this)
                    .into(views.ivHairimg);

    }


    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        views.pbImage.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        views.pbImage.setVisibility(View.GONE);

        return false;
    }


}