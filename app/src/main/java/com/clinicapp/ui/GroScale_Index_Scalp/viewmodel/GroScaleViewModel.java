package com.clinicapp.ui.GroScale_Index_Scalp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.ClientAnalysis;
import com.clinicapp.models.GroScaleResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroScaleViewModel extends BaseViewModel {
    private MutableLiveData<AsyncResponse<GroScaleResponse,Exception>> apiLiveData;
    private ArrayList<CameraPositions> positions = new ArrayList<>();

    public GroScaleViewModel(@NonNull Application application) {
        super(application);
    }


    public ArrayList<CameraPositions> getSelectedPositions() {
        return positions;
    }




    public void Get_Gro_Scale(Long id){
        apiLiveData.postValue(AsyncResponse.loading());

        repository.api.getGroScale(35)
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


    public String image_path(String key, ClientAnalysis clientAnalysis, boolean closeup_3xcloseup, Context context){
        String path="";
        if (closeup_3xcloseup) {
            for (int i = 0; i < clientAnalysis.getImages().getCloseup().size(); i++) {
                if (clientAnalysis.getImages().getCloseup().get(i).getSubType().equals(key)) {
                    path = clientAnalysis.getImages().getCloseup().get(i).getImage_path();
                    return path;
                }
            }
        }else {
            for (int i = 0; i < clientAnalysis.getImages().getThreex_closeup().size(); i++) {
                if (clientAnalysis.getImages().getThreex_closeup().get(i).getSubType().equals(key)) {
                    path = clientAnalysis.getImages().getThreex_closeup().get(i).getImage_path();
                    return path;
                }
            }
        }
//        if (path.length()==0){
//            Toast.makeText(context, "Image is not availabe for this part", Toast.LENGTH_SHORT).show();
//        }
        Log.e("pathimg",path);
        return path;

    }
    public String set_classvale(Long value){
        Log.e( "11value: ", value+"");
        String class_str="";
        String json_str="[{\n" +
                "\t\"class\": \"Class I\",\n" +
                "\t\"about1\": 1,\n" +
                "  \"about2\": 20,\n" +
                "\t\"value\": \"Normal\"\n" +
                "}, {\n" +
                "\t\"class\": \"Class II\",\n" +
                "\t\"about1\": 21,\n" +
                "  \"about2\": 40,\n" +
                "\t\"value\": \"Early Thinning\"\n" +
                "}, {\n" +
                "\t\"class\": \"Class III\",\n" +
                "\t\"about1\": 41,\n" +
                "  \"about2\": 60,\n" +
                "\t\"value\": \"Visible Thinning\"\n" +
                "}, {\n" +
                "\t\"class\": \"Class IV\",\n" +
                "\t\"about1\": 61,\n" +
                "  \"about2\": 80,\n" +
                "\t\"value\": \"Severe Thinning\"\n" +
                "}, {\n" +
                "\t\"class\": \"Class V\",\n" +
                "\t\"about1\": 81,\n" +
                "  \"about2\": 100,\n" +
                "\t\"value\": \"Balding\"\n" +
                "}]";
        try {
            JSONArray jsonArray=new JSONArray(json_str);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                if ((value>=jsonObject.getLong("about1")) && (value<=jsonObject.getLong("about2"))){
                    Log.e( "12value: ", value+"");
                    class_str=jsonObject.getString("class")+" "+jsonObject.getString("value");
                    return class_str;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", "set_classvale: ",e );
        }
        return class_str;
    }
}
