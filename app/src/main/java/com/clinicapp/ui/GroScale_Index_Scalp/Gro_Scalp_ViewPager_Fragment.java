package com.clinicapp.ui.GroScale_Index_Scalp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.clinicapp.adapters.GroScale_ViewPagerAdapter;
import com.clinicapp.adapters.Scalp_Standard_ViewPagerAdapter;
import com.clinicapp.adapters.Scalp_User_ViewPagerAdapter;
import com.clinicapp.databinding.FragmentGrowScaleViewpagerBinding;
import com.clinicapp.databinding.FragmentScalpInformationViewpagerBinding;
import com.clinicapp.models.GroScaleImagesDetail;
import com.clinicapp.models.GroScaleResponse;
import com.clinicapp.models.GroScalpResponse;
import com.clinicapp.models.GroScalp_UserData;
import com.clinicapp.models.GroScalp_UserDataImg;
import com.clinicapp.models.Patient;
import com.clinicapp.ui.GroScale_Index_Scalp.viewmodel.GroScale_ViewPager_ViewModel;
import com.clinicapp.ui.GroScale_Index_Scalp.viewmodel.GroScalp_ViewPager_ViewModel;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class Gro_Scalp_ViewPager_Fragment extends Fragment {
    private GroScalp_ViewPager_ViewModel viewModel;
    private FragmentScalpInformationViewpagerBinding views;
    private MainViewModel mainViewModel;
    private Scalp_Standard_ViewPagerAdapter viewPagerAdapter;
    private Scalp_User_ViewPagerAdapter scalp_user_viewPagerAdapter;
    private Patient patient;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        views = FragmentScalpInformationViewpagerBinding.inflate(getLayoutInflater(),container,false);

        return views.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        viewModel = new ViewModelProvider(this).get(GroScalp_ViewPager_ViewModel.class);

        initViews();
    }

    private void initViews() {
        patient = mainViewModel.getSelectedPatient();

        views.patientName.setText(patient.getName());

        viewModel.getApiLiveData()
                .observe(getViewLifecycleOwner(), result -> {

                    if(result.isError()) {
                        Utils.notify(getActivity(), result.message);
                    } else if(result.isSuccess() && result.isFresh()){
                        //Use pop as we want to act on the result only when it's fresh.
                        final GroScalpResponse groScaleResponse = result.pop();
                        handleSearchResults(groScaleResponse);
                    }
                });

        get_groscalp_info();



    }


    private void handleSearchResults(GroScalpResponse groScaleResponse) {
        Log.e( "SearchResults:", groScaleResponse.getStatus()+"");
        views.progressCircular.setVisibility(View.GONE);
        views.pager.setVisibility(View.VISIBLE);
        views.pager1.setVisibility(View.VISIBLE);

        viewPagerAdapter=new Scalp_Standard_ViewPagerAdapter( this, groScaleResponse.getStandard());
        views.pager.setAdapter(viewPagerAdapter);
        List<GroScalp_UserDataImg> groScalp_userDataImgs=new ArrayList<>();
            for (int i=0;i<groScaleResponse.getUser_data().size();i++){
                List<GroScalp_UserData> list=groScaleResponse.getUser_data();

                for (int k=0;k<list.get(i).getThreex_closeup_images().size();k++){
                    List<GroScaleImagesDetail> groScaleImagesDetails=list.get(i).getThreex_closeup_images();
                    groScalp_userDataImgs.add(new GroScalp_UserDataImg(list.get(i).getId(),
                            list.get(i).getCreated_at(),groScaleImagesDetails.get(k).getImage_path()
                            ,groScaleImagesDetails.get(k).getSubType()));
                }
            }

        scalp_user_viewPagerAdapter=new Scalp_User_ViewPagerAdapter( this, groScalp_userDataImgs);
        views.pager1.setAdapter(scalp_user_viewPagerAdapter);

    }

    private void get_groscalp_info(){
        viewModel.get_groscalp_info(mainViewModel.getSelectedPatient().getId());
    }
}