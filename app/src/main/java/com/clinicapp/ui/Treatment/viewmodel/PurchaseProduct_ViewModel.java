package com.clinicapp.ui.Treatment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.BuyProductResponse;
import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.ProductsResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseProduct_ViewModel extends BaseViewModel {
    private MutableLiveData<AsyncResponse<BuyProductResponse,Exception>> apiLiveData;

    public PurchaseProduct_ViewModel(@NonNull Application application) {
        super(application);
    }






    public void purchase_product(long productid,long clientid){
        apiLiveData.postValue(AsyncResponse.loading());

        repository.api.buy_product(productid,clientid)
                .enqueue(new Callback<BuyProductResponse>() {
                    @Override
                    public void onResponse(Call<BuyProductResponse> call, Response<BuyProductResponse> response) {
                        if(response.isSuccessful()){
                            BuyProductResponse result = response.body();
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
                    public void onFailure(Call<BuyProductResponse> call, Throwable t) {
                        apiLiveData.setValue(AsyncResponse.error(t.getMessage()));
                    }
                });

    }

    public LiveData<AsyncResponse<BuyProductResponse, Exception>> getApiLiveData() {
        if(apiLiveData == null) apiLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
        return apiLiveData;
    }



}
