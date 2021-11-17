package com.clinicapp.ui.GroScale_Index_Scalp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.GroScaleResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroIndex_ViewPager_ViewModel extends BaseViewModel {
    private MutableLiveData<AsyncResponse<GroScaleResponse,Exception>> apiLiveData;
    private ArrayList<CameraPositions> positions = new ArrayList<>();

    public GroIndex_ViewPager_ViewModel(@NonNull Application application) {
        super(application);
    }


    public ArrayList<CameraPositions> getSelectedPositions() {
        return positions;
    }




    public void Get_Gro_Scale(Long id){
        apiLiveData.postValue(AsyncResponse.loading());

        repository.api.getGroScale(id)
                .enqueue(new Callback<GroScaleResponse>() {
                    @Override
                    public void onResponse(Call<GroScaleResponse> call, Response<GroScaleResponse> response) {
                        if(response.isSuccessful()){
                            GroScaleResponse result = response.body();
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
                    public void onFailure(Call<GroScaleResponse> call, Throwable t) {
                        apiLiveData.setValue(AsyncResponse.error(t.getMessage()));
                    }
                });

    }

    public LiveData<AsyncResponse<GroScaleResponse, Exception>> getApiLiveData() {
        if(apiLiveData == null) apiLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
        return apiLiveData;
    }


    public boolean setZoomPositions(boolean hasCrownTop, boolean hasCrownMid, boolean hasCrownBottom,
                                    boolean hasHairRight, boolean hasHairBack, boolean hasHairLeft) {
        ArrayList<Integer> selectedPositions = new ArrayList<>();

        if(hasHairRight) {
            selectedPositions.add(CameraPositions.Positions.HAIR_RIGHT);
            selectedPositions.add(CameraPositions.Positions.HAIR_RIGHT_ZOOM);
        }
        if(hasHairLeft) {
            selectedPositions.add(CameraPositions.Positions.HAIR_LEFT);
            selectedPositions.add(CameraPositions.Positions.HAIR_LEFT_ZOOM);
        }
        if(hasHairBack) {
            selectedPositions.add(CameraPositions.Positions.HAIR_OCCIPITAL);
            selectedPositions.add(CameraPositions.Positions.HAIR_OCCIPITAL_ZOOM);
        }
        if(hasCrownTop) {
            selectedPositions.add(CameraPositions.Positions.HAIR_FRONTAL);
            selectedPositions.add(CameraPositions.Positions.HAIR_FRONTAL_ZOOM);
        }
        if(hasCrownMid) {
            selectedPositions.add(CameraPositions.Positions.HAIR_CROWN);
            selectedPositions.add(CameraPositions.Positions.HAIR_CROWN_ZOOM);
        }
        if(hasCrownBottom) {
            selectedPositions.add(CameraPositions.Positions.HAIR_VERTEX);
            selectedPositions.add(CameraPositions.Positions.HAIR_VERTEX_ZOOM);
        }
        Collections.sort(selectedPositions);
        positions = CameraPositions.getCameraPositionArray(selectedPositions);
        return !selectedPositions.isEmpty();
    }


}
