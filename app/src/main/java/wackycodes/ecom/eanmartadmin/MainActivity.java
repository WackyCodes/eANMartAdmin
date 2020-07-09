package wackycodes.ecom.eanmartadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wackycodes.ecom.eanmartadmin.cityareacode.AreaCodeCityModel;
import wackycodes.ecom.eanmartadmin.cityareacode.SelectAreaCityAdaptor;
import wackycodes.ecom.eanmartadmin.database.DBQuery;
import wackycodes.ecom.eanmartadmin.mainpage.MainActivityAdaptor;
import wackycodes.ecom.eanmartadmin.mainpage.MainActivityGridModel;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;

import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_NAME;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.REQUEST_TO_ADD_SHOP;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.REQUEST_TO_VIEW_HOME;

public class MainActivity extends AppCompatActivity {
    public static AppCompatActivity mainActivity;

    public static FrameLayout mainHomeContentFrame;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    public static TextView badgeNotifyCount;
    // Drawer...User info
    public static CircleImageView drawerImage;
    public static TextView drawerName;
    public static TextView drawerEmail;
    public static LinearLayout drawerCityLayout;
    public static TextView drawerCityTitle;
    public static TextView drawerCityName;


    public static TextView toolCityName;
    public static TextView toolUserName;

    public static SelectAreaCityAdaptor selectAreaCityAdaptor;

    // Main Page...
    private GridView homeGridView;
    public static List <MainActivityGridModel> mainPageList = new ArrayList <>();

    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mainActivity = this;
        dialog = DialogsClass.getDialog( this );
        // assign..
        mainHomeContentFrame = findViewById( R.id.main_content_frame_layout );

        toolbar = findViewById( R.id.appToolbar );
        drawer = findViewById( R.id.drawer_layout );
        navigationView = findViewById( R.id.nav_view );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
        }catch (NullPointerException ignored){ }

        // Nav Header...
        drawerImage = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_Image );
        drawerName = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_UserName );
        drawerEmail = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_userEmail );
        drawerCityLayout = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_user_city_layout );
        drawerCityTitle = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_title_text );
        drawerCityName = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_user_city );

        toolCityName = findViewById( R.id.tool_user_city );
        toolUserName = findViewById( R.id.tool_user_name );

        // Home Main Page....
        homeGridView = findViewById( R.id.home_grid_view );

        mainPageList.add( new MainActivityGridModel( R.drawable.ic_home_black_24dp, "View Home", REQUEST_TO_VIEW_HOME ) );
        mainPageList.add( new MainActivityGridModel( R.drawable.ic_store_mall_directory_black_24dp, "Add New Shop", REQUEST_TO_ADD_SHOP ) );
        mainPageList.add( new MainActivityGridModel( R.drawable.ic_person_pin_circle_black_24dp, "View Profile", 4 ) );
        mainPageList.add( new MainActivityGridModel( R.drawable.ic_color_lens_black_24dp, "View Sample", 4 ) );


        MainActivityAdaptor mainActivityAdaptor = new MainActivityAdaptor();
        homeGridView.setAdapter( mainActivityAdaptor );
        mainActivityAdaptor.notifyDataSetChanged();


        dialog.show();
        // Get Shop List Of Current City...
        DBQuery.getShopListOfCurrentCity();
        // Load Home Page...
        DBQuery.getMainListDataQuery( this, dialog, null, null, "HOME", true, 0);

        toolCityName.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCityDialog();
            }
        } );
        drawerCityLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCityDialog();
            }
        } );

    }

    // Select City Dialog...
    private AreaCodeCityModel tempAreaCodeCityModel = null;
    private void selectCityDialog(){
        // TODO :
        tempAreaCodeCityModel=null;
        /// Sample Button click...
        final Dialog cityDialog = new Dialog( this );
        cityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        cityDialog.setContentView( R.layout.dialog_select_city_dialog );
        cityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        cityDialog.setCancelable( false );
        Button cancelBtn = cityDialog.findViewById( R.id.dialog_cancel_btn );
        Button selectBtn = cityDialog.findViewById( R.id.dialog_select_btn );
        final AutoCompleteTextView cityText = cityDialog.findViewById( R.id.dialogEditText );
        ArrayList<AreaCodeCityModel> areaCodeCityModelArrayList = new ArrayList <>();
        areaCodeCityModelArrayList.addAll( DBQuery.areaCodeCityModelList );
//        showToast( "Size : "+ areaCodeCityModelArrayList.size() );
        selectAreaCityAdaptor =
                new SelectAreaCityAdaptor(MainActivity.this, R.layout.select_area_list_item, areaCodeCityModelArrayList);

        cityText.setThreshold(1);
        cityText.setAdapter(selectAreaCityAdaptor);

        // handle click event and set desc on textView
        cityText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AreaCodeCityModel areaCodeCityModel = (AreaCodeCityModel) adapterView.getItemAtPosition(i);
                cityText.setText( areaCodeCityModel.getAreaCode() );
                tempAreaCodeCityModel = areaCodeCityModel;
            }
        });

        selectBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check
                if (tempAreaCodeCityModel!=null){
                    cityDialog.dismiss();
                    drawerCityTitle.setText( "Your City" );
                    drawerCityName.setText( tempAreaCodeCityModel.getAreaCode() + ", " + tempAreaCodeCityModel.getCityName() );
                    toolCityName.setText( tempAreaCodeCityModel.getCityName() );

//                    showToast( "CityName : " +tempAreaCodeCityModel.getCityName() + " Code : " + tempAreaCodeCityModel.getAreaCode() );
                    //  : Reload Product...
                    if (tempAreaCodeCityModel.getCityCode()!=CURRENT_CITY_CODE){
                     // TODO   loadMainHomePageAgain(tempAreaCodeCityModel.getCityCode());
                        CURRENT_CITY_CODE = tempAreaCodeCityModel.getCityCode();
                        CURRENT_CITY_NAME = tempAreaCodeCityModel.getCityName();
                    }

                }else{
                    cityText.setError( "pin code / City not found!" );
                }

            }
        } );
        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityDialog.dismiss();
            }
        } );

        cityDialog.show();

    }



}
