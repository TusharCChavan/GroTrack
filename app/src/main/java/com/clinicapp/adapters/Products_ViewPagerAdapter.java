package com.clinicapp.adapters;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.clinicapp.models.Products;
import com.clinicapp.ui.Treatment.PurchaseProduct_Fragment;

import java.util.List;


public class Products_ViewPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter{

    private List<Products> patients;

    public Products_ViewPagerAdapter(@NonNull Fragment fragment,List<Products> patients) {
        super(fragment);
        this.patients = patients;
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return PurchaseProduct_Fragment.getInstance(patients.get(position));
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }
}
