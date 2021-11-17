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
import com.clinicapp.models.ClientAnalysis;
import com.clinicapp.models.GroScalpMatchResponse;
import com.clinicapp.models.Patient;
import com.clinicapp.models.ScalpStandard;
import com.clinicapp.ui.GroScale_Index_Scalp.viewmodel.GroScaleViewModel;
import com.clinicapp.ui.GroScale_Index_Scalp.viewmodel.GroScalp_User_ViewModel;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseFragment;
import com.clinicapp.utilities.Utils;

public class Gro_Scalp_standerd_img_Fragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,
        RequestListener {
    private GroScalp_User_ViewModel viewModel;
    private MainViewModel mainViewModel;
    private FragmentGrowScalpStanderedImgBinding views;
    private ScalpStandard clientAnalysis;



    public static Gro_Scalp_standerd_img_Fragment getInstance(ScalpStandard patient){
        final Gro_Scalp_standerd_img_Fragment fragment = new Gro_Scalp_standerd_img_Fragment();
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
        views = FragmentGrowScalpStanderedImgBinding.inflate(getLayoutInflater(),container,false);

        return views.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GroScalp_User_ViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        load_data();
    }



    private void load_data() {
        views.crownMiddle.setOnCheckedChangeListener(this);

        views.pbImage.setVisibility(View.VISIBLE);


            Glide.with(this)
                    .load(clientAnalysis.getImage_path())
                    .listener(this)
                    .into(views.ivHairimg);


        viewModel.getApiLiveData()
                .observe(getViewLifecycleOwner(), result -> {

                    if(result.isError()) {
                        Utils.notify(getActivity(), result.message);
                    } else if(result.isSuccess() && result.isFresh()){
                        //Use pop as we want to act on the result only when it's fresh.
                        final GroScalpMatchResponse groScaleResponse = result.pop();
                        handleSearchResults(groScaleResponse);
                    }
                });
    }

    private void handleSearchResults(GroScalpMatchResponse groScaleResponse) {
        if (groScaleResponse.getStatus()) {
            views.pbApi.setVisibility(View.GONE);
            final Bundle bundle = new Bundle();
            bundle.putParcelable(DATA, groScaleResponse);
            Navigation.findNavController(views.getRoot())
                    .navigate(R.id.show_groscaledetail, bundle);
        }
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (b){
            views.pbApi.setVisibility(View.VISIBLE);
            views.crownMiddle.setVisibility(View.GONE);
            call_match_api();
        }
    }

    private void call_match_api(){
        viewModel.get_groscalp_match(mainViewModel.getSelectedPatient().getId(),clientAnalysis.getId());
    }
}