package com.clinicapp.ui.Treatment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.GroScaleResponse;
import com.clinicapp.models.ProductsResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Products_ViewPager_ViewModel extends BaseViewModel {
    private MutableLiveData<AsyncResponse<ProductsResponse,Exception>> apiLiveData;
    private ArrayList<CameraPositions> positions = new ArrayList<>();

    public Products_ViewPager_ViewModel(@NonNull Application application) {
        super(application);
    }


    public ArrayList<CameraPositions> getSelectedPositions() {
        return positions;
    }




    public void Get_Products(){
        apiLiveData.postValue(AsyncResponse.loading());

        repository.api.getProducts()
                .enqueue(new Callback<ProductsResponse>() {
                    @Override
                    public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                        if(response.isSuccessful()){
                            ProductsResponse result = response.body();
                            if(result.getStatus()){
                                apiLiveData.setValue(AsyncResponse.success(result));
                            } else {
                                apiLiveData.setValue(AsyncResponse.error(result.getMessage()));
                            }
                        } else {
                            onFailure(call, new Exception(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductsResponse> call, Throwable t) {
                        apiLiveData.setValue(AsyncResponse.error(t.getMessage()));
                    }
                });

    }

    public LiveData<AsyncResponse<ProductsResponse, Exception>> getApiLiveData() {
        if(apiLiveData == null) apiLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
        return apiLiveData;
    }



}
