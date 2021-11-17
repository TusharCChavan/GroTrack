package com.clinicapp.ui.GroScale_Index_Scalp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.clinicapp.databinding.FragmentGrowScalpDetailBinding;
import com.clinicapp.databinding.FragmentGrowScalpStanderedImgBinding;
import com.clinicapp.models.GroScalpMatchResponse;
import com.clinicapp.models.ScalpStandard;
import com.clinicapp.ui.GroScale_Index_Scalp.viewmodel.GroScaleViewModel;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseFragment;

public class Gro_Scalp_Detail_Fragment extends BaseFragment implements
        RequestListener {
    private GroScaleViewModel viewModel;
    private MainViewModel mainViewModel;
    private FragmentGrowScalpDetailBinding views;
    private GroScalpMatchResponse clientAnalysis;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientAnalysis = getArguments().getParcelable(DATA);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        views = FragmentGrowScalpDetailBinding.inflate(getLayoutInflater(),container,false);

        return views.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GroScaleViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        load_data();
    }



    private void load_data() {
        views.pbImage.setVisibility(View.VISIBLE);
        views.tvProductdes.setText(clientAnalysis.getScalp().getDescription());

            Glide.with(this)
                    .load(clientAnalysis.getScalp().getImage_path())
                    .listener(this)
                    .into(views.ivHairimg);


            views.btSolution.setOnClickListener(view ->
                    Navigation.findNavController(view)
                            .navigate(R.id.action_show_treatment)
                    );
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