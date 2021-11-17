package com.clinicapp.adapters;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.clinicapp.models.Products;
import com.clinicapp.models.ScalpStandard;
import com.clinicapp.ui.GroScale_Index_Scalp.Gro_Scalp_standerd_img_Fragment;
import com.clinicapp.ui.Treatment.PurchaseProduct_Fragment;

import java.util.List;


public class Scalp_Standard_ViewPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter{

    private List<ScalpStandard> patients;

    public Scalp_Standard_ViewPagerAdapter(@NonNull Fragment fragment, List<ScalpStandard> patients) {
        super(fragment);
        this.patients = patients;
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return Gro_Scalp_standerd_img_Fragment.getInstance(patients.get(position));
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }
}
