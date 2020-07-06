package wackycodes.ecom.eanmartadmin.secondpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.addnewitem.AddNewLayoutActivity;
import wackycodes.ecom.eanmartadmin.database.DBQuery;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.getMainListDataQuery;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.homePageList;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.ADD_NEW_STRIP_AD_LAYOUT;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_CONTAINER_ITEM;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;

public class SecondActivity extends AppCompatActivity {

    public static TextView cityName;
    public static TextView userName;

    private Toolbar toolbar;

    private RecyclerView secondRecycler;
    public static HomePageAdaptor homePageAdaptor;

    private LinearLayout activityViewLayout;
    private LinearLayout addNewLayoutBtn;

    private LinearLayout dialogAddLayout;
    private ConstraintLayout newBannerSlidderContainer; // add_banner_slider_layout
    private ConstraintLayout newGridLayoutContainer; // add_grid_layout
    private ConstraintLayout newStripAdLayout; // add_strip_ad_layout
    private LinearLayout addNewBannerSlidderBtn; // add_banner_slider_layout_LinearLay
    private LinearLayout addNewGirdLayoutBtn; // add_grid_layout_LinearLay
    private LinearLayout addNewStripAdLayoutBtn; // add_strip_ad_layout_LinearLay
    private ImageView closeAddLayout; //close_add_layout

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_second );
        dialog = DialogsClass.getDialog( this );
        //...
        cityName = findViewById( R.id.user_city );
        userName = findViewById( R.id.user_name );

        activityViewLayout = findViewById( R.id.activity_view_layout);
        addNewLayoutBtn = findViewById( R.id.add_new_layout);

        // Add Layout...
        dialogAddLayout = findViewById( R.id.dialog_add_layout );
        newBannerSlidderContainer = findViewById( R.id.add_banner_slider_layout );
        newGridLayoutContainer = findViewById( R.id.add_grid_layout );
        newStripAdLayout = findViewById( R.id.add_strip_ad_layout );
        addNewBannerSlidderBtn = findViewById( R.id.add_banner_slider_layout_LinearLay );
        addNewGirdLayoutBtn = findViewById( R.id.add_grid_layout_LinearLay );
        addNewStripAdLayoutBtn = findViewById( R.id.add_strip_ad_layout_LinearLay );
        closeAddLayout = findViewById( R.id.close_add_layout );

        //...
        String toolTitle = "Title";
        toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( toolTitle );
        }catch (NullPointerException ignored){ }

        // -------------
        secondRecycler = findViewById( R.id.second_activity_recycler );
        // Linear Layout...
        LinearLayoutManager homeCatLayoutManager = new LinearLayoutManager( this );
        homeCatLayoutManager.setOrientation( RecyclerView.VERTICAL );
        secondRecycler.setLayoutManager( homeCatLayoutManager );
        // Adaptor...
        homePageAdaptor = new HomePageAdaptor( 1, "Home Page" );
        secondRecycler.setAdapter( homePageAdaptor );
        homePageAdaptor.notifyDataSetChanged();
        if (homePageList.size()==0){
            dialog.show();
            getMainListDataQuery( this, dialog, null, "HOME", true, 0);
        }

        addNewLayoutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogVisibility(true);
                newGridLayoutContainer.setVisibility( View.GONE );
            }
        } );
        addNewBannerSlidderBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO...
                setDialogVisibility(false);
                AddNewLayoutActivity.isHomePage = true;
                Intent intent = new Intent( SecondActivity.this, AddNewLayoutActivity.class );
                intent.putExtra( "CAT_ID", "HOME" );
                intent.putExtra( "LAY_TYPE", BANNER_SLIDER_LAYOUT_CONTAINER );
                intent.putExtra( "LAY_INDEX", homePageList.size() );
                intent.putExtra( "TASK_UPDATE", false );
                intent.putExtra( "CAT_INDEX", 0 );
                startActivity( intent );
            }
        } );
        addNewStripAdLayoutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO...
                setDialogVisibility(false);
                AddNewLayoutActivity.isHomePage = true;
                Intent intent = new Intent( SecondActivity.this, AddNewLayoutActivity.class );
                intent.putExtra( "CAT_ID", "HOME" );
                intent.putExtra( "LAY_TYPE", STRIP_AD_LAYOUT_CONTAINER );
                intent.putExtra( "LAY_INDEX", homePageList.size() );
                intent.putExtra( "TASK_UPDATE", false );
                intent.putExtra( "CAT_INDEX", 0 );
                startActivity( intent );
            }
        } );

        closeAddLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogVisibility(false);
            }
        } );

    }

    private void setDialogVisibility(boolean isVisible){
        if(!isVisible){
            activityViewLayout.setVisibility( View.VISIBLE );
            addNewLayoutBtn.setVisibility( View.VISIBLE );
            dialogAddLayout.setVisibility( View.GONE );
        }else{
            activityViewLayout.setVisibility( View.GONE );
            addNewLayoutBtn.setVisibility( View.GONE );
            dialogAddLayout.setVisibility( View.VISIBLE );
        }
    }

}
