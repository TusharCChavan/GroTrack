package com.clinicapp.ui.Treatment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.MDResponse;
import com.clinicapp.models.ProductsResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Refer_MD_ViewPager_ViewModel extends BaseViewModel {
    private MutableLiveData<AsyncResponse<MDResponse,Exception>> apiLiveData;
    private ArrayList<CameraPositions> positions = new ArrayList<>();

    public Refer_MD_ViewPager_ViewModel(@NonNull Application application) {
        super(application);
    }


    public ArrayList<CameraPositions> getSelectedPositions() {
        return positions;
    }




    public void get_md(){
        apiLiveData.postValue(AsyncResponse.loading());

        repository.api.get_md()
                .enqueue(new Callback<MDResponse>() {
                    @Override
                    public void onResponse(Call<MDResponse> call, Response<MDResponse> response) {
                        if(response.isSuccessful()){
                            MDResponse result = response.body();
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
                    public void onFailure(Call<MDResponse> call, Throwable t) {
                        apiLiveData.setValue(AsyncResponse.error(t.getMessage()));
                    }
                });

    }

    public LiveData<AsyncResponse<MDResponse, Exception>> getApiLiveData() {
        if(apiLiveData == null) apiLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
        return apiLiveData;
    }



}
