package wackycodes.ecom.eanmartadmin.cityareacode.servicecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.cityareacode.AreaCodeCityModel;
import wackycodes.ecom.eanmartadmin.other.StaticValues;

import static wackycodes.ecom.eanmartadmin.other.StaticValues.ADMIN_SHOP_FOUNDER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.ADMIN_SHOP_MANAGER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CLOSE_SERVICE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.OPEN_SERVICE;

public class ServiceCityActivity extends AppCompatActivity {

    private List<AreaCodeCityModel> areaCodeCityList = new ArrayList <>();

    private Toolbar toolbar;

    private FrameLayout cityFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_service_city );

        // Srt ToolBar...
        toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }

        // Assign...
        cityFrameLayout = findViewById( R.id.frameLayout );

        setCityFrameLayout(new CityViewFragment());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu( menu );
//        getMenuInflater().inflate( R.menu.menu_service_city_option, menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home){
//            onBackPressed();
//            return true;
//        }
//        else if ( id == R.id.menu_add_shop_member ){
//            // Add ClickListener...
//            return true;
//        }

        return super.onOptionsItemSelected( item );
    }

    // Change City Dialog...
    private void updateService( ){
        /// Single Ok Button ...
        final Dialog serviceDialog = new Dialog( this );
        serviceDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        serviceDialog.setContentView( R.layout.dialog_a_spinner );
        serviceDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        serviceDialog.setCancelable( false );
        final RadioGroup radioGroup = serviceDialog.findViewById( R.id.dialog_radio_group );
        final RadioButton openServiceBtn = serviceDialog.findViewById( R.id.open_radio_btn );
        final RadioButton closeServiceBtn = serviceDialog.findViewById( R.id.close_radio_btn );
        final EditText closeMessageEt = serviceDialog.findViewById( R.id.close_message );
        TextView cancelBtn = serviceDialog.findViewById( R.id.dialog_cancel_btn );
        TextView okBtn = serviceDialog.findViewById( R.id.dialog_ok_btn );

        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == closeServiceBtn.getId()){
                    closeMessageEt.setVisibility( View.VISIBLE );
                }else{
                    closeMessageEt.setVisibility( View.GONE );
                }
            }
        } );

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int memberType = -1;
                if (radioGroup.getCheckedRadioButtonId() == openServiceBtn.getId()){
                    memberType = OPEN_SERVICE;
                }
                else if(radioGroup.getCheckedRadioButtonId() == closeServiceBtn.getId()){
                    memberType = CLOSE_SERVICE;
                }

                if (memberType != -1 ){
                    Map <String, Object> updateMap = new HashMap <>();
                    updateMap.clear();
                    if ( memberType == OPEN_SERVICE ){
                        updateMap.put( "available_service", true );
                    }else if ( !TextUtils.isEmpty( closeMessageEt.getText().toString() )){
                        updateMap.put( "available_service", false );
                        updateMap.put( "service_alert_type", "1" );
                        updateMap.put( "service_alert_text", closeMessageEt.getText().toString() );
                    }else{
                        closeMessageEt.setError( "Required!" );
                    }
                    //  Request
                    serviceDialog.dismiss();
                }else{
                    Toast.makeText( ServiceCityActivity.this, "Please Choose Option!", Toast.LENGTH_SHORT ).show();
                }

            }
        } );

        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceDialog.dismiss();
            }
        } );
        serviceDialog.show();
    }

    private void setCityFrameLayout(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add( cityFrameLayout.getId(), fragment );
        fragmentTransaction.commit();

    }


}
