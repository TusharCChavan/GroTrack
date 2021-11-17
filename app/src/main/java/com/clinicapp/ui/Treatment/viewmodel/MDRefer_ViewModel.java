package com.clinicapp.ui.Treatment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.BuyProductResponse;
import com.clinicapp.models.MDReferRequest;
import com.clinicapp.models.MD_ReferResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MDRefer_ViewModel extends BaseViewModel {
    private MutableLiveData<AsyncResponse<MD_ReferResponse,Exception>> apiLiveData;

    public MDRefer_ViewModel(@NonNull Application application) {
        super(application);
    }



    public void refer_to_md(long clientid,long clinic_id){
        apiLiveData.postValue(AsyncResponse.loading());
        MDReferRequest mdReferRequest=new MDReferRequest(clinic_id);
        repository.api.refer_to_md(clientid,mdReferRequest)
                .enqueue(new Callback<MD_ReferResponse>() {
                    @Override
                    public void onResponse(Call<MD_ReferResponse> call, Response<MD_ReferResponse> response) {
                        if(response.isSuccessful()){
                            MD_ReferResponse result = response.body();
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
                    public void onFailure(Call<MD_ReferResponse> call, Throwable t) {
                        apiLiveData.setValue(AsyncResponse.error(t.getMessage()));
                    }
                });

    }

    public LiveData<AsyncResponse<MD_ReferResponse, Exception>> getApiLiveData() {
        if(apiLiveData == null) apiLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
        return apiLiveData;
    }



}
