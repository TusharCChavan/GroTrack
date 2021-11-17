package com.clinicapp.ui.GroScale_Index_Scalp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.GroScaleResponse;
import com.clinicapp.models.GroScalpResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroScalp_ViewPager_ViewModel extends BaseViewModel {
    private MutableLiveData<AsyncResponse<GroScalpResponse,Exception>> apiLiveData;
    private ArrayList<CameraPositions> positions = new ArrayList<>();

    public GroScalp_ViewPager_ViewModel(@NonNull Application application) {
        super(application);
    }





    public void get_groscalp_info(Long id){
        apiLiveData.postValue(AsyncResponse.loading());

        repository.api.get_groscalp_info(id)
                .enqueue(new Callback<GroScalpResponse>() {
                    @Override
                    public void onResponse(Call<GroScalpResponse> call, Response<GroScalpResponse> response) {
                        if(response.isSuccessful()){
                            GroScalpResponse result = response.body();
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
                    public void onFailure(Call<GroScalpResponse> call, Throwable t) {
                        apiLiveData.setValue(AsyncResponse.error(t.getMessage()));
                    }
                });

    }

    public LiveData<AsyncResponse<GroScalpResponse, Exception>> getApiLiveData() {
        if(apiLiveData == null) apiLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
        return apiLiveData;
    }



}
