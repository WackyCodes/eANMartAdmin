package wackycodes.ecom.eanmartadmin.launching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.DialogCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.regex.Pattern;

import wackycodes.ecom.eanmartadmin.MainActivity;
import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.database.DBQuery;
import wackycodes.ecom.eanmartadmin.other.CheckInternetConnection;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.currentUser;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseAuth;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_NAME;

public class AuthActivity extends AppCompatActivity {
    private Dialog dialog;

    //---------
    private TextView signInForgetPassword;
    private EditText signInEmail;
    private EditText signInPassword;
    private Button signInBtn;
    //---------
    private LinearLayout signInLayout;
    private LinearLayout forgetPassLayout;
    private EditText forgetPassEmail;
    private Button forgetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_auth );

        dialog = DialogsClass.getDialog( this );

        signInLayout = findViewById( R.id.login_layout );
        forgetPassLayout = findViewById( R.id.forget_pass_layout );

        signInForgetPassword = findViewById( R.id.sign_in_forget_password );
        signInEmail = findViewById( R.id.sign_in_email );
        signInPassword = findViewById( R.id.sign_in_password );
        signInBtn = findViewById( R.id.sign_in_btn );

        forgetPassEmail = findViewById( R.id.forget_pass_email );
        forgetBtn = findViewById( R.id.get_email_btn );

        signInBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogIn();
            }
        } );

        signInForgetPassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInLayout.setVisibility( View.GONE );
                forgetPassLayout.setVisibility( View.VISIBLE );
            }
        } );

        forgetBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if (isValidEmail(forgetPassEmail)){
                       dialog.show();
                       getMailToResetPassword(forgetPassEmail.getText().toString().trim());
                   }
                }
            } );


    }

    private void userLogIn(){
        if ( isValidEmail( signInEmail ) && isValidPass( signInPassword ) && w_isInternetConnect()){
            dialog.show();
            final String email_Mobile = signInEmail.getText().toString().trim();
            final String password = signInPassword.getText().toString().trim();
            firebaseAuth.signInWithEmailAndPassword( email_Mobile, password )
                    .addOnCompleteListener( new OnCompleteListener <AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task <AuthResult> task) {
                            if (task.isSuccessful()){
                                // write file in local memory..!
                                currentUser = firebaseAuth.getCurrentUser();
                                // Check Permission...
                                checkAdminPermission();
                            }else{
                                showToast( "Something going wrong..!!" );
                                dialog.dismiss();
                            }
                        }
                    } );
            //-----------
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
                    String current_city_code = task.getResult().get( "current_city_code" ).toString();
                    String current_city_name = task.getResult().get( "current_city_name" ).toString();

                    if (is_root_admin){
                        // Forward to home page...
                        ADMIN_DATA_MODEL.setAdminCode( admin_code );
                        CURRENT_CITY_CODE = current_city_code;
                        CURRENT_CITY_NAME = current_city_name;
                        Intent intent = new Intent( AuthActivity.this, MainActivity.class );
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
        dialog.dismiss();
        DialogsClass.alertDialog( AuthActivity.this, "Permission denied!", "You have not permission to use this app." );
    }

    // Forget Password Method...
    private void getMailToResetPassword(String email) {
        DBQuery.firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()) {
                            showToast( "For Reset your password, Link has been send successfully..!! Please Check Your Email." );
                        }
                        else if (task.isCanceled()){
                            showToast( "Can't Send Email..! Error Occurred.!" );
                        }else {
                            showToast( "Can't Send Email..! Try Again..1" );
                        }
//                        forgetPassGetMailBtn.setEnabled( true );
                        dialog.dismiss();
                    }
                });
    }

    private boolean isValidPass( EditText password){
        String wPass = password.getText().toString().trim();
        if(TextUtils.isEmpty( wPass )){
            password.setError( "Please Enter Password" );
            return false;
        }
        return true;
    }

    private boolean isValidEmail( EditText wReference ){
        String wEmail = wReference.getText().toString().trim();
        String emailRegex =
                "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        boolean bool = pat.matcher(wEmail).matches();

        if (TextUtils.isEmpty( wEmail )) {
            wReference.setError( "Please Enter Email! " );
            return false;
        } else if (!bool){
            wReference.setError( "Please Enter Valid Email! " );
            return false;
        }

        return true;
    }

    private boolean w_isInternetConnect() {
        return CheckInternetConnection.isInternetConnected( this );
    }

    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }



}
