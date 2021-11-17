package com.clinicapp.ui.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.clinicapp.databinding.ActivityMainBinding;
import com.clinicapp.ui.common.WifiDialogFragment;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseActivity;
import com.clinicapp.utilities.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class MainActivity extends BaseActivity {
    private ActivityMainBinding views;
    private MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = ActivityMainBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(views.getRoot());

        init();
    }


    private boolean beforeClickPermissionRat;
    private boolean afterClickPermissionRat;
    private void init() {
        Log.e( "initViews: ",Utils.checkCameraPermission(this)+"" );
        beforeClickPermissionRat = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
        Log.e( "initViews: ",beforeClickPermissionRat+" -" );

        if (!Utils.checkCameraPermission(this)){
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);

        }


        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        /* ... */
                        Log.e( "onPermissionGranted: ","grant" );
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Log.e( "onPermissionDenied: ","denied" );
                        Toast.makeText(MainActivity.this, "Go to Settings and Grant the permission to use this feature.", Toast.LENGTH_SHORT).show();
                        showdialog();
                        /* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        /* ... */
                        Log.e( "onPermissionRationaleShouldBeShown: ","show" );
                        Toast.makeText(MainActivity.this, "Go to Settings and Grant the permission to use this feature.", Toast.LENGTH_SHORT).show();
                        showdialog();

                        token.continuePermissionRequest();

                    }
                }).check();

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        afterClickPermissionRat  = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
        Log.e( "initViews: ",afterClickPermissionRat+" -" );

    }

    @Override
    public void updateBatteryPercent(int battery) {
    }

    private void showdialog(){
        new MaterialAlertDialogBuilder(this)
                .setTitle("Permission")
                .setMessage("Go to Settings and Grant the permission to use this feature.")
                .setPositiveButton("Setting", /* listener = */ new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {
                        Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Cancel", /* listener = */ new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            Log.e( "onBackPressed: ","test1" );

        } else {
            super.onBackPressed();

            Log.e( "onBackPressed: ","test2" );

        }

    }

}