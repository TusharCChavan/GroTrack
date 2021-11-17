package com.clinicapp.adapters;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.clinicapp.models.MDClinics;
import com.clinicapp.models.Products;
import com.clinicapp.ui.Treatment.MD_Refer_Fragment;
import com.clinicapp.ui.Treatment.PurchaseProduct_Fragment;
import com.clinicapp.ui.Treatment.Refer_MD_ViewPager_Fragment;

import java.util.List;


public class MD_Refer_ViewPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter{

    private List<MDClinics> patients;

    public MD_Refer_ViewPagerAdapter(@NonNull Fragment fragment, List<MDClinics> patients) {
        super(fragment);
        this.patients = patients;
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (patients.get(position).getAdmin_profile()==null) {

            return MD_Refer_Fragment.getInstance(null,patients.get(position).getClinic_id());
        }
            return MD_Refer_Fragment.getInstance(patients.get(position).getAdmin_profile(),patients.get(position).getClinic_id());

    }

    @Override
    public int getItemCount() {
        return patients.size();
    }
}
