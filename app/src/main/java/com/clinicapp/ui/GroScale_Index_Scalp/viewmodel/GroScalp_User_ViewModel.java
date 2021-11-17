package com.clinicapp.ui.GroScale_Index_Scalp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.GroScalpMatchResponse;
import com.clinicapp.models.GroScalpResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroScalp_User_ViewModel extends BaseViewModel {
    private MutableLiveData<AsyncResponse<GroScalpMatchResponse,Exception>> apiLiveData;

    public GroScalp_User_ViewModel(@NonNull Application application) {
        super(application);
    }





    public void get_groscalp_match(Long id,Long idd){
        apiLiveData.postValue(AsyncResponse.loading());

        repository.api.get_groscalp_match(id,idd)
                .enqueue(new Callback<GroScalpMatchResponse>() {
                    @Override
                    public void onResponse(Call<GroScalpMatchResponse> call, Response<GroScalpMatchResponse> response) {
                        if(response.isSuccessful()){
                            GroScalpMatchResponse result = response.body();
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
                    public void onFailure(Call<GroScalpMatchResponse> call, Throwable t) {
                        apiLiveData.setValue(AsyncResponse.error(t.getMessage()));
                    }
                });

    }

    public LiveData<AsyncResponse<GroScalpMatchResponse, Exception>> getApiLiveData() {
        if(apiLiveData == null) apiLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
        return apiLiveData;
    }



}
