package ean.ecom.eanmartadmin.mainpage.homesection.categorysection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.addnewitem.AddNewLayoutActivity;
import ean.ecom.eanmartadmin.other.CheckInternetConnection;
import ean.ecom.eanmartadmin.other.DialogsClass;

import static ean.ecom.eanmartadmin.database.DBQuery.categoryIDList;
import static ean.ecom.eanmartadmin.database.DBQuery.categoryList;
import static ean.ecom.eanmartadmin.database.DBQuery.getMainListDataQuery;
import static ean.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static ean.ecom.eanmartadmin.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;

public class ShopsViewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView shopsViewRecycler;
    public static ShopsViewAdaptor shopsViewAdaptor;

    private String categoryID;
    private int catIndex = 0;

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
        setContentView( R.layout.activity_shops_view );
        dialog = DialogsClass.getDialog( this );

        activityViewLayout = findViewById( R.id.activity_view_layout);
        addNewLayoutBtn = findViewById( R.id.add_new_layout);
        // Get Intent Value...
        categoryID = getIntent().getStringExtra( "CAT_ID" );
        String catName = getIntent().getStringExtra( "CAT_NAME" );
        catIndex = categoryIDList.indexOf( categoryID ); // Get Category Index...

        // Add Layout...
        swipeRefreshLayout = findViewById( R.id.home_swipe_refresh_layout );
        dialogAddLayout = findViewById( R.id.dialog_add_layout );
        newBannerSlidderContainer = findViewById( R.id.add_banner_slider_layout );
        newGridLayoutContainer = findViewById( R.id.add_grid_layout );
        newStripAdLayout = findViewById( R.id.add_strip_ad_layout );
        addNewBannerSlidderBtn = findViewById( R.id.add_banner_slider_layout_LinearLay );
        addNewGirdLayoutBtn = findViewById( R.id.add_grid_layout_LinearLay );
        addNewStripAdLayoutBtn = findViewById( R.id.add_strip_ad_layout_LinearLay );
        closeAddLayout = findViewById( R.id.close_add_layout );

        //...
        toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( "Category : "+ catName );
        }catch (NullPointerException ignored){ }

        // -------------
        shopsViewRecycler = findViewById( R.id.shops_view_activity_recycler_view );

        activityViewLayout.setVisibility( View.VISIBLE );
        addNewLayoutBtn.setVisibility( View.VISIBLE );
        // Linear Layout...
        LinearLayoutManager homeCatLayoutManager = new LinearLayoutManager( this );
        homeCatLayoutManager.setOrientation( RecyclerView.VERTICAL );
        shopsViewRecycler.setLayoutManager( homeCatLayoutManager );

//        categoryList.add( new ArrayList <HomeListModel>() ); // Add Items (Done At homeList time...
        shopsViewAdaptor = new ShopsViewAdaptor( categoryList.get( catIndex ), catIndex, "Name", categoryID );
        shopsViewRecycler.setAdapter( shopsViewAdaptor );
        shopsViewAdaptor.notifyDataSetChanged();

        if (categoryList.get( catIndex ).size() == 0){
            //  Load List and Set...
            dialog.show();
            getMainListDataQuery( this, dialog, shopsViewRecycler, null, categoryID, false, catIndex);
        }else{
            // notifyDataSetChanged...
//            shopsViewAdaptor.notifyDataSetChanged();
        }

        // ----------------------------------------------------------------------------
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
                AddNewLayoutActivity.isHomePage = false;
                Intent intent = new Intent( ShopsViewActivity.this, AddNewLayoutActivity.class );
                intent.putExtra( "CAT_ID", categoryID );
                intent.putExtra( "LAY_TYPE", BANNER_SLIDER_LAYOUT_CONTAINER );
                intent.putExtra( "LAY_INDEX", categoryList.get(catIndex).size() );
                intent.putExtra( "TASK_UPDATE", false );
                intent.putExtra( "CAT_INDEX", catIndex );
                startActivity( intent );
            }
        } );
        addNewStripAdLayoutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO...
                setDialogVisibility(false);
                AddNewLayoutActivity.isHomePage = false;
                Intent intent = new Intent( ShopsViewActivity.this, AddNewLayoutActivity.class );
                intent.putExtra( "CAT_ID", categoryID );
                intent.putExtra( "LAY_TYPE", STRIP_AD_LAYOUT_CONTAINER );
                intent.putExtra( "LAY_INDEX", categoryList.get(catIndex).size() );
                intent.putExtra( "TASK_UPDATE", false );
                intent.putExtra( "CAT_INDEX", catIndex );
                startActivity( intent );
            }
        } );

        closeAddLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogVisibility(false);
            }
        } );

        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing( true );
                if (CheckInternetConnection.isInternetConnected( ShopsViewActivity.this )){
                    getMainListDataQuery( ShopsViewActivity.this, null, shopsViewRecycler, swipeRefreshLayout, categoryID, false, catIndex);
                }else{
                    swipeRefreshLayout.setRefreshing( false );
                }
            }
        } );

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
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
