package wackycodes.ecom.eanmartadmin.launching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import wackycodes.ecom.eanmartadmin.MainActivity;
import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.database.DBQuery;
import wackycodes.ecom.eanmartadmin.other.CheckInternetConnection;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;
import wackycodes.ecom.eanmartadmin.other.StaticValues;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.currentUser;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseAuth;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.DEFAULT_CITY_NAME;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.STORAGE_PERMISSION;

public class WelcomeActivity extends AppCompatActivity {
    public static AppCompatActivity welcomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_welcome );
        welcomeActivity = this;
        if( CheckInternetConnection.isInternetConnected( this ) ){ // CheckInternetConnection.isInternetConnected( this )
            firebaseFirestore.collection( "PERMISSION" ).document( "APP_USE_PERMISSION" )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        boolean isAllowed = task.getResult().getBoolean( StaticValues.APP_VERSION );
                        if ( isAllowed ){
                            askStoragePermission();
//                            checkCurrentUser();
                        }else{
                            DialogsClass.getMessageDialog( WelcomeActivity.this
                                    , "Sorry, Permission denied..!"
                                    , "You Don't have permission to use this App. If you have any query, Please contact App founder..!\\n Thank You!" );
                            finish();
                        }
                    }else {
                        showToast( "Failed..!" );
                        finish();
                    }
                }
            } );
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void checkCurrentUser(){
//        DBQuery.getCityListQuery();
        if (currentUser != null){
            checkAdminPermission();
        }else{
            startActivity( new Intent( WelcomeActivity.this, AuthActivity.class ) );
            finish();
        }

    }

    private void checkAdminPermission(  ){
        firebaseFirestore.collection( "ROOT_ADMIN" ).document( currentUser.getUid() )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Boolean is_root_admin = task.getResult().getBoolean( "is_root_admin" );
                    int admin_code = Integer.parseInt( task.getResult().get( "admin_code" ).toString() );
                    if (is_root_admin){
                        // Forward to home page...
                        ADMIN_DATA_MODEL.setAdminCode( admin_code );
                        Intent intent = new Intent( WelcomeActivity.this, MainActivity.class );
                        startActivity( intent );
                        finish();
                    }else{
                        // SignOut...
                        deniedDialog();
                    }
                }else{
                    deniedDialog();
                }
            }
        } );

    }

    private void deniedDialog(){
        firebaseAuth.signOut();
        DialogsClass.alertDialog( WelcomeActivity.this, "Permission denied!", "You have not permission to use this app." );
    }

    private boolean isInternetConnect() {
        return CheckInternetConnection.isInternetConnected( this );
    }
    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }
    /// Storage Permission...
    public void askStoragePermission(){
        if(ContextCompat.checkSelfPermission( WelcomeActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission( WelcomeActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
            checkCurrentUser();
        }else {
            requestStoragePermission();
        }
    }
    private void requestStoragePermission(){

        if(ActivityCompat.shouldShowRequestPermissionRationale( this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )
        || ActivityCompat.shouldShowRequestPermissionRationale( this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE )){

            new AlertDialog.Builder( this )
                    .setTitle( "Storage Permission" )
                    .setMessage( "Storage permission is needed, because of File Storage will be required!" )
                    .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions( WelcomeActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    }, STORAGE_PERMISSION );
                        }
                    } ).setNegativeButton( "Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
//                    requestStoragePermission();
                    finish();
                }
            } ).create().show();
        }else{
            ActivityCompat.requestPermissions( WelcomeActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, STORAGE_PERMISSION );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== STORAGE_PERMISSION){
            if(grantResults.length>0
                    && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED
            ){
                showToast( "Permission is GRANTED..." );
                checkCurrentUser();
            }
            else{
                showToast( "Permission DENIED!" );
                requestStoragePermission();
            }
        }
    }





}
