package com.clinicapp.adapters;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.clinicapp.models.GroScalp_UserDataImg;
import com.clinicapp.models.ScalpStandard;
import com.clinicapp.ui.GroScale_Index_Scalp.Gro_Scalp_User_img_Fragment;
import com.clinicapp.ui.GroScale_Index_Scalp.Gro_Scalp_standerd_img_Fragment;

import java.util.List;


public class Scalp_User_ViewPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter{

    private List<GroScalp_UserDataImg> patients;

    public Scalp_User_ViewPagerAdapter(@NonNull Fragment fragment, List<GroScalp_UserDataImg> patients) {
        super(fragment);
        this.patients = patients;
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return Gro_Scalp_User_img_Fragment.getInstance(patients.get(position));
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }
}
