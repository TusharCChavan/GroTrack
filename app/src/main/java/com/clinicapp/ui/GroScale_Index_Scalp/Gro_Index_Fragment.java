package com.clinicapp.ui.GroScale_Index_Scalp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.clinicapp.R;
import com.clinicapp.databinding.FragmentGrowScaleHairPositionNewBinding;
import com.clinicapp.models.ClientAnalysis;
import com.clinicapp.models.Patient;
import com.clinicapp.ui.GroScale_Index_Scalp.viewmodel.GroScaleViewModel;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseFragment;
import com.google.android.material.checkbox.MaterialCheckBox;

public class Gro_Index_Fragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,
        RequestListener {
    private GroScaleViewModel viewModel;
    private MainViewModel mainViewModel;
    private FragmentGrowScaleHairPositionNewBinding views;
    private ClientAnalysis clientAnalysis;



    public static Gro_Index_Fragment getInstance(ClientAnalysis patient){
        final Gro_Index_Fragment fragment = new Gro_Index_Fragment();
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
        views = FragmentGrowScaleHairPositionNewBinding.inflate(getLayoutInflater(),container,false);

        return views.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GroScaleViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        initViews();
        initListeners();
    }

    private void initViews() {
         Patient patient = mainViewModel.getSelectedPatient();


        int maleVisibility = patient.isFemale() ? View.GONE:View.VISIBLE;
        int femaleVisibility = patient.isFemale() ? View.VISIBLE:View.GONE;

        views.maleCrown.setVisibility(maleVisibility);
        views.maleRightImg.setVisibility(maleVisibility);
        views.maleLeftImg.setVisibility(maleVisibility);

        views.femaleCrown.setVisibility(femaleVisibility);
        views.femaleRightImg.setVisibility(femaleVisibility);
        views.femaleBackImg.setVisibility(femaleVisibility);
        views.femaleLeftImg.setVisibility(femaleVisibility);

        views.btSolution.setVisibility(View.GONE);

        views.crownMiddle.setChecked(true);
        onClickShoot(views.crownMiddle);

    }




    private void initListeners() {

        views.femaleCrownTop.setOnCheckedChangeListener(this);
        views.femaleCrownMiddle.setOnCheckedChangeListener(this);
        views.femaleCrownBottom.setOnCheckedChangeListener(this);
        views.femaleHairRight.setOnCheckedChangeListener(this);
        views.femaleHairLeft.setOnCheckedChangeListener(this);

       views.crownTop.setOnCheckedChangeListener(this);
        views.crownMiddle.setOnCheckedChangeListener(this);
        views.crownBottom.setOnCheckedChangeListener(this);
        views.maleHairRight.setOnCheckedChangeListener(this);
        views.maleHairLeft.setOnCheckedChangeListener(this);

        views.ivHairimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (closeup_3xcloseup){
                closeup_3xcloseup=false;
            }else {closeup_3xcloseup=true;}
                onClickShoot(view);
            }
        });
        views.tvClass.setText("("+mainViewModel.getDate(clientAnalysis.getCreated_at())+")");

        check_image_exist();
    }

    boolean closeup_3xcloseup=true;
    private void onClickShoot(View view) {
        views.pbImage.setVisibility(View.VISIBLE);
        boolean crownTop = views.femaleCrownTop.isChecked() || views.crownTop.isChecked();
        boolean crownMiddle = views.femaleCrownMiddle.isChecked() || views.crownMiddle.isChecked();
        boolean crownBottom = views.femaleCrownBottom.isChecked() || views.crownBottom.isChecked();
        boolean hairRight = views.femaleHairRight.isChecked() || views.maleHairRight.isChecked();
        boolean hairLeft = views.femaleHairLeft.isChecked() || views.maleHairLeft.isChecked();
        String crownTop_key="vertex";
        String crownMiddle_key="crown";
        String crownBottom_key="front";
        String hairLeft_key="left";
     String hairRight_key="right";
     String percentage_str = "0.0";
        RequestOptions requestOptions=new RequestOptions().dontTransform();
        String image_str="";

        if (crownMiddle){
//            mark_checkbox_false(0);
            image_str=viewModel.image_path(crownMiddle_key,clientAnalysis,closeup_3xcloseup,getActivity());

            percentage_str =clientAnalysis.getData().getGroIndex().getCrown();
        }else if (crownTop){
//            mark_checkbox_false(1);
            image_str=viewModel.image_path(crownTop_key,clientAnalysis,closeup_3xcloseup,getActivity());


            percentage_str =clientAnalysis.getData().getGroIndex().getVertex();
        }else if (crownBottom){
//            mark_checkbox_false(2);
            image_str=viewModel.image_path(crownBottom_key,clientAnalysis,closeup_3xcloseup,getActivity());

            percentage_str =clientAnalysis.getData().getGroIndex().getFront();
        }else if (hairRight){
//            mark_checkbox_false(3);
            image_str=viewModel.image_path(hairRight_key,clientAnalysis,closeup_3xcloseup,getActivity());

            percentage_str =clientAnalysis.getData().getGroIndex().getRight();
        }else if (hairLeft){
//            mark_checkbox_false(4);
            image_str=viewModel.image_path(hairLeft_key,clientAnalysis,closeup_3xcloseup,getActivity());


            percentage_str =clientAnalysis.getData().getGroIndex().getLeft();
        }
        views.tvClass.setText(percentage_str+"% "+"("+mainViewModel.getDate(clientAnalysis.getCreated_at())+")");

        if (image_str.length()==0){
            views.tvError.setVisibility(View.VISIBLE);
            views.pbImage.setVisibility(View.GONE);
            views.ivHairimg.setVisibility(View.INVISIBLE);
        }else {
            views.tvError.setVisibility(View.GONE);
            views.pbImage.setVisibility(View.VISIBLE);
            views.ivHairimg.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(image_str)
                    .listener(this).apply(requestOptions)
                    .into(views.ivHairimg);

        }

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.e( "onCheckedChanged: ",b+"" );

        if (compoundButton.getId()== R.id.male_hair_left || compoundButton.getId()==R.id.female_hair_left){
            if (views.maleHairLeft.isPressed() || views.femaleHairLeft.isPressed()) {
                mark_checkbox_false(4);
            }
        }else if (compoundButton.getId()==R.id.male_hair_right || compoundButton.getId()==R.id.female_hair_right){
            if (views.maleHairRight.isPressed() || views.femaleHairRight.isPressed()) {
                mark_checkbox_false(3);
            }
        }else if (compoundButton.getId()==R.id.female_crown_bottom || compoundButton.getId()==R.id.crown_bottom){
            if (views.femaleCrownBottom.isPressed() || views.crownBottom.isPressed()) {
                mark_checkbox_false(2);
            }
        }else if (compoundButton.getId()==R.id.female_crown_top || compoundButton.getId()==R.id.crown_top){
            if (views.femaleCrownTop.isPressed() || views.crownTop.isPressed()) {
                mark_checkbox_false(1);
            }
        }else if (compoundButton.getId()==R.id.female_crown_middle || compoundButton.getId()==R.id.crown_middle){
            if (views.femaleCrownMiddle.isPressed() || views.crownMiddle.isPressed()) {
                mark_checkbox_false(0);
            }
        }
        onClickShoot(compoundButton);
    }


    private void mark_checkbox_false(int pos){
        if (pos==0){
            views.femaleCrownTop.setChecked(false);
            views.femaleCrownBottom.setChecked(false);
            views.femaleHairRight.setChecked(false);
            views.femaleHairLeft.setChecked(false);

            views.crownTop.setChecked(false);
            views.crownBottom.setChecked(false);
            views.maleHairRight.setChecked(false);
            views.maleHairLeft.setChecked(false);
        }else if (pos==1){
            views.femaleCrownMiddle.setChecked(false);
            views.femaleCrownBottom.setChecked(false);
            views.femaleHairRight.setChecked(false);
            views.femaleHairLeft.setChecked(false);

            views.crownMiddle.setChecked(false);
            views.crownBottom.setChecked(false);
            views.maleHairRight.setChecked(false);
            views.maleHairLeft.setChecked(false);
        }else if (pos==2){
            views.femaleCrownTop.setChecked(false);
            views.femaleCrownMiddle.setChecked(false);
            views.femaleHairRight.setChecked(false);
            views.femaleHairLeft.setChecked(false);

            views.crownTop.setChecked(false);
            views.crownMiddle.setChecked(false);
            views.maleHairRight.setChecked(false);
            views.maleHairLeft.setChecked(false);
        }else if (pos==3){
            views.femaleCrownTop.setChecked(false);
            views.femaleCrownMiddle.setChecked(false);
            views.femaleCrownBottom.setChecked(false);
            views.femaleHairLeft.setChecked(false);

            views.crownTop.setChecked(false);
            views.crownMiddle.setChecked(false);
            views.crownBottom.setChecked(false);
            views.maleHairLeft.setChecked(false);
        }else if (pos==4){
            views.femaleCrownTop.setChecked(false);
            views.femaleCrownMiddle.setChecked(false);
            views.femaleCrownBottom.setChecked(false);
            views.femaleHairRight.setChecked(false);

            views.crownTop.setChecked(false);
            views.crownMiddle.setChecked(false);
            views.crownBottom.setChecked(false);
            views.maleHairRight.setChecked(false);
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


    public void check_image_exist(){
        String crownTop_key="vertex";
        String crownMiddle_key="crown";
        String crownBottom_key="front";
        String hairLeft_key="left";
        String hairRight_key="right";
        RequestOptions requestOptions=new RequestOptions().dontTransform();
        String image_str="";


        image_str=viewModel.image_path(crownMiddle_key,clientAnalysis,closeup_3xcloseup,getActivity());
        if (image_str.length()==0) {
            set_no_image(views.crownMiddle);
            set_no_image(views.femaleCrownMiddle);
        }

        image_str=viewModel.image_path(crownTop_key,clientAnalysis,closeup_3xcloseup,getActivity());

        if (image_str.length()==0) {
            set_no_image(views.crownTop);
            set_no_image(views.femaleCrownTop);
        }
        image_str=viewModel.image_path(crownBottom_key,clientAnalysis,closeup_3xcloseup,getActivity());
        if (image_str.length()==0) {
            set_no_image(views.crownBottom);
            set_no_image(views.femaleCrownBottom);
        }

        image_str=viewModel.image_path(hairRight_key,clientAnalysis,closeup_3xcloseup,getActivity());
        if (image_str.length()==0) {
            set_no_image(views.maleHairRight);
            set_no_image(views.femaleHairRight);
        }

        image_str=viewModel.image_path(hairLeft_key,clientAnalysis,closeup_3xcloseup,getActivity());
        if (image_str.length()==0) {
            set_no_image(views.maleHairLeft);
            set_no_image(views.femaleHairLeft);
        }



    }

    public void set_no_image(MaterialCheckBox materialCheckBox){
        materialCheckBox.setClickable(false);
        materialCheckBox.setBackground(getActivity().getDrawable(R.drawable.ic_circle_red));

    }
}