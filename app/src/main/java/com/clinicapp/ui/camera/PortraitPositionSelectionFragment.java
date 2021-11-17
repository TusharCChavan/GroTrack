package com.clinicapp.ui.camera;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.clinicapp.R;
import com.clinicapp.databinding.FragmentPortraitImageSelectionBinding;
import com.clinicapp.models.HairAnalysisResponse;
import com.clinicapp.models.Patient;
import com.clinicapp.ui.camera.viewmodel.PositionViewModel;
import com.clinicapp.ui.home.HomeFragment;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseFragment;
import com.clinicapp.utilities.Utils;

public class PortraitPositionSelectionFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private FragmentPortraitImageSelectionBinding views;
    private Patient patient;
    private MainViewModel mainViewModel;
    private PositionViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        views = FragmentPortraitImageSelectionBinding.inflate(getLayoutInflater(),container,false);
        return views.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PositionViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        patient=mainViewModel.getSelectedPatient();
        initViews();
        initListeners();
    }

    private void initViews(){
        patient = mainViewModel.getSelectedPatient();
//        patient = new Patient(1,"Test","LastTest","ab","20"
//                ,"01-01-2000","Male","123456",29  );
        views.patientName.setText(patient.getName());
//     views.patientName.setText("Name");

        if(patient.getGender()!=null&&patient.getGender().contains("F")){
            views.maleRightSideImg.setVisibility(View.GONE);
            views.maleLeftSideImg.setVisibility(View.GONE);
            views.maleTopSideImg.setVisibility(View.GONE);
            views.maleFrontSideImg.setVisibility(View.GONE);
            views.maleBackSideImg.setVisibility(View.GONE);


            views.femaleRightSideImg.setVisibility(View.VISIBLE);
            views.femaleLeftSideImg.setVisibility(View.VISIBLE);
            views.femaleTopSideImg.setVisibility(View.VISIBLE);
            views.femaleFrontSideImg.setVisibility(View.VISIBLE);
            views.femaleBackSideImg.setVisibility(View.VISIBLE);
        }
    }


    private void initListeners(){
        views.selectAllPositions.setOnCheckedChangeListener(this::onCheckAllChanged);
        views.skipAllPositions.setOnCheckedChangeListener(this::onCheckskilChanged);
        views.topSide.setOnCheckedChangeListener(this);
        views.backSide.setOnCheckedChangeListener(this);
        views.leftSide.setOnCheckedChangeListener(this);
        views.rightSide.setOnCheckedChangeListener(this);
        views.frontSide.setOnCheckedChangeListener(this);
        views.shoot.setOnClickListener(this::onClickShoot);

        viewModel.getApiLiveData().observe(getViewLifecycleOwner(),result->{
            if(result.isError()) {
                Utils.notify(getActivity(), result.message);
            } else if(result.isSuccess() && result.isFresh()){
                //Use pop as we want to act on the result only when it's fresh.
                final HairAnalysisResponse analysisResponse = result.pop();

                handleApiResponse(analysisResponse);
            }
        });
    }




    private void onCheckAllChanged(CompoundButton compoundButton, boolean b) {
        Log.e( "onCheckedChanged:2", b+" "+views.selectAllPositions.isPressed());

        if (views.selectAllPositions.isPressed()){
        boolean allPositions = true;
        views.topSide.setChecked(allPositions);
        views.backSide.setChecked(allPositions);
        views.leftSide.setChecked(allPositions);
        views.rightSide.setChecked(allPositions);
        views.frontSide.setChecked(allPositions);
        views.selectAllPositions.setChecked(allPositions);
    }

    }

      private void onCheckskilChanged(CompoundButton compoundButton, boolean b) {
          Log.e( "onCheckedChanged:1", b+" "+views.skipAllPositions.isPressed());
        if (views.skipAllPositions.isPressed()) {
            boolean allPositions = false;
            views.topSide.setChecked(allPositions);
            views.backSide.setChecked(allPositions);
            views.leftSide.setChecked(allPositions);
            views.rightSide.setChecked(allPositions);
            views.frontSide.setChecked(allPositions);
            views.selectAllPositions.setChecked(allPositions);
        }
        }

    private void onClickShoot(View view) {
        final boolean canProceed = viewModel.setPositions(views.rightSide.isChecked(),
                views.leftSide.isChecked(),views.frontSide.isChecked(),
                views.backSide.isChecked(),views.topSide.isChecked())
                ||views.skipAllPositions.isChecked();

        Log.e( "onClickShoot: ",canProceed+"" );

        if(canProceed) {
            viewModel.createAnalysisID(patient.getId());
//            handleApiResponse(new HairAnalysisResponse(true,1));
        } else{
            Utils.notify(getContext(),"Please select at least one position.");
        }
    }

    private void startCameraFragment(){
        Log.e( "startCameraFragment: ","in" );
        if (Utils.checkCameraPermission(getContext())) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("SHOW_ZOOM", false);
            mainViewModel.setCameraPositions(viewModel.getSelectedPositions());
            Navigation.findNavController(views.getRoot())
                    .navigate(R.id.action_selectShootPosition_to_cameraFragment, bundle);
        } else {
            //Request Permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        if(requestCode==0){
            if(Utils.isInternetAvailable(getContext())){
                //Start Camera after allowing the permission
                startCameraFragment();
            }else{
                Utils.notify(getContext(),"Unable to access Camera");
                getFragmentManager().beginTransaction().replace(R.id.main_layout,new HomeFragment()).commit();
            }

        }
    }
    private void handleApiResponse(HairAnalysisResponse result){
        Long analysisId=result.getHairAnalysisID();
        mainViewModel.setHairAnalysisID(analysisId);

        final boolean canSkip=views.skipAllPositions.isChecked();

        if(canSkip){
            Navigation.findNavController(views.getRoot()).navigate(R.id.action_selectShootPosition_to_selectHairPosition);
        }else{
            startCameraFragment();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.e( "onCheckedChanged:0 ", b+"");
        if(!b){
            views.selectAllPositions.setChecked(false);
        }
        if(b){
            views.skipAllPositions.setChecked(false);
        }

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Navigation.findNavController(views.skipAllPositions).navigate(R.id.action_homeFragment_to_newPatientFragment);
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

}