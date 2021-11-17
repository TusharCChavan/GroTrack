package com.clinicapp.adapters;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.clinicapp.models.GroScaleResponse;
import com.clinicapp.ui.GroScale_Index_Scalp.Gro_Scale_Fragment;


public class GroScale_ViewPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter{

    private GroScaleResponse patients;

    public GroScale_ViewPagerAdapter(@NonNull Fragment fragment, GroScaleResponse patients) {
        super(fragment);
        this.patients = patients;
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
            return Gro_Scale_Fragment.getInstance(patients.getAll_analysis().get(position));
    }

    @Override
    public int getItemCount() {
        return patients.getAll_analysis().size();
    }
}
