package wackycodes.ecom.eanmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wackycodes.ecom.eanmartadmin.cityareacode.AreaCodeCityModel;
import wackycodes.ecom.eanmartadmin.cityareacode.SelectAreaCityAdaptor;
import wackycodes.ecom.eanmartadmin.database.DBQuery;
import wackycodes.ecom.eanmartadmin.mainpage.MainActivityAdaptor;
import wackycodes.ecom.eanmartadmin.mainpage.MainActivityGridModel;
import wackycodes.ecom.eanmartadmin.multisection.AboutShopModel;
import wackycodes.ecom.eanmartadmin.multisection.SearchShopAdaptor;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.currentUser;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseAuth;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_NAME;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.REQUEST_TO_ADD_SHOP;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.REQUEST_TO_VIEW_HOME;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
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

    // Search Variables...
    private SearchView homeMainSearchView;
    private RecyclerView homeSearchItemRecycler;
    private static List <AboutShopModel> searchShopItemList = new ArrayList <>();
    private List<String> searchShopTags = new ArrayList <>();
    private SearchShopAdaptor searchAdaptor;

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


        // setNavigationItemSelectedListener()...
        navigationView.setNavigationItemSelectedListener( MainActivity.this );
        navigationView.getMenu().getItem( 0 ).setChecked( true );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this,drawer,toolbar,
                R.string.navigation_Drawer_Open,R.string.navigation_Drawer_close);
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        toolCityName = findViewById( R.id.tool_user_city );
        toolUserName = findViewById( R.id.tool_user_name );

        // Home Main Page....
        homeGridView = findViewById( R.id.home_grid_view );

        if (mainPageList.size() == 0){
            mainPageList.add( new MainActivityGridModel( R.drawable.ic_home_black_24dp, "View Home", REQUEST_TO_VIEW_HOME ) );
            mainPageList.add( new MainActivityGridModel( R.drawable.ic_store_mall_directory_black_24dp, "Add New Shop", REQUEST_TO_ADD_SHOP ) );
            mainPageList.add( new MainActivityGridModel( R.drawable.ic_person_pin_circle_black_24dp, "View Profile", 4 ) );
            mainPageList.add( new MainActivityGridModel( R.drawable.ic_color_lens_black_24dp, "View Sample", 4 ) );

        }

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

        // Set Drawer City Click listener...
        drawerCityLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen( GravityCompat.START )){
                    drawer.closeDrawer( GravityCompat.START );
                }
                // select city Dialog...
                selectCityDialog();
            }
        } );

        // Search...
        homeMainSearchView = findViewById( R.id.home_shop_search_view );
        homeSearchItemRecycler = findViewById( R.id.home_search_recycler_view );
//        homeMainSearchView.setFocusableInTouchMode( false );
        homeMainSearchView.setFocusable( false );
        LinearLayoutManager searchLinearLManager = new LinearLayoutManager( this );
        searchLinearLManager.setOrientation( RecyclerView.VERTICAL );
        homeSearchItemRecycler.setLayoutManager( searchLinearLManager );
        searchAdaptor = new SearchShopAdaptor( searchShopItemList );
        homeSearchItemRecycler.setAdapter( searchAdaptor );
        getShopSearchItems( );

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen( GravityCompat.START )){
            drawer.closeDrawer( GravityCompat.START );
        }
        else if (homeSearchItemRecycler.getVisibility() == View.VISIBLE){
            setFrameVisibility(true);
        }
        else {
            super.onBackPressed();
        }

    }

    int mainNavItemId;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawer( GravityCompat.START );

        mainNavItemId = menuItem.getItemId();

        // ------- On Item Click...
        // Home Nav Option...
        if ( mainNavItemId == R.id.nav_home ){
            // index - 0
            getSupportActionBar().setTitle( R.string.app_name );
            return true;
        }else
            // Bottom Options...
            if ( mainNavItemId == R.id.menu_log_out ){
                // index - 5
                if (currentUser != null){
                    // TODO : Show Dialog to logOut..!
                    // Sign Out Dialog...
                    final Dialog signOut = new Dialog( MainActivity.this );
                    signOut.requestWindowFeature( Window.FEATURE_NO_TITLE );
                    signOut.setContentView( R.layout.dialog_sign_out );
                    signOut.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                    signOut.setCancelable( false );
                    ImageView imageView = signOut.findViewById( R.id.sign_out_image );
                    Glide.with( this ).load( "sample" ).apply( new RequestOptions().placeholder( R.drawable.ic_account_circle_black_24dp ) ).into( imageView );
                    final Button signOutOkBtn = signOut.findViewById( R.id.sign_out_ok_btn );
                    Button signOutCancelBtn = signOut.findViewById( R.id.sign_out_cancel_btn );
                    signOut.show();

                    signOutOkBtn.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signOutOkBtn.setEnabled( false );
                            firebaseAuth.signOut();
                            currentUser = null;
                            navigationView.getMenu().getItem( 0 ).setChecked( true );
                            navigationView.getMenu().getItem( 5 ).setEnabled( false );
                            signOut.dismiss();
                            finish();
                        }
                    } );
                    signOutCancelBtn.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signOut.dismiss();
                            // TODO : Sign Out
                        }
                    } );

                    return false;
                }
            }
        return false;
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


    private void setFrameVisibility(boolean isVisible){
        if (isVisible){
            homeGridView.setVisibility( View.VISIBLE );
            homeSearchItemRecycler.setVisibility( View.GONE );
        }else{
            homeGridView.setVisibility( View.GONE );
            homeSearchItemRecycler.setVisibility( View.VISIBLE );
        }
    }

    private void getShopSearchItems(){

        // Search Methods...
        homeMainSearchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                dialog.show();
                searchShopItemList.clear();
                searchShopTags.clear();
                final String [] tags = query.toLowerCase().split( " " );
                for ( final String tag : tags ){
                    firebaseFirestore
                            .collection( "HOME_PAGE" )
                            .document( CURRENT_CITY_CODE.toUpperCase() )
                            .collection( "SHOPS" )
                            .whereArrayContainsAny( "tags", Arrays.asList( tags ) )
//                            .whereArrayContains( "tags", tag.trim() )
                            .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task <QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    AboutShopModel model = new AboutShopModel(
                                            documentSnapshot.getId(),
                                            documentSnapshot.get( "shop_name" ).toString(),
                                            documentSnapshot.get( "shop_category_name" ).toString(),
                                            documentSnapshot.get( "shop_logo" ).toString(),
                                            documentSnapshot.get( "shop_rating" ).toString(),
                                            Integer.parseInt( documentSnapshot.get( "shop_veg_non_type" ).toString() )
                                    );
                                    if ( !searchShopTags.contains( model.getShopID() )){
                                        searchShopItemList.add( model );
                                        searchShopTags.add( model.getShopID() );
                                    }

                                }
                                if (searchShopItemList.size()>0){
                                    setFrameVisibility(false);
                                }else{
                                    setFrameVisibility(true);
                                }
                                if (tag.equals(tags[tags.length - 1])){
                                    if (searchShopTags.isEmpty()){
                                        DialogsClass.alertDialog( MainActivity.this, null, "No Shop found.!" ).show();
                                        setFrameVisibility(true);
                                    }else{
                                        searchAdaptor.getFilter().filter( query );
                                    }
                                    dialog.dismiss();
                                }
                                dialog.dismiss();
                            }else{
                                // error...
                                dialog.dismiss();
                                Toast.makeText( MainActivity.this, "Failed ! Product Not found.!", Toast.LENGTH_SHORT ).show();
                                setFrameVisibility(true);
                            }
                        }
                    } );
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        } );
        homeMainSearchView.setOnCloseListener( new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setFrameVisibility(true);
                return false;
            }
        } );

    }




}
