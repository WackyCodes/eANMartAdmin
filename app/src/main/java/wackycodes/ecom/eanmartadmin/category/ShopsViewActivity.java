package wackycodes.ecom.eanmartadmin.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.addnewitem.AddNewLayoutActivity;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;
import wackycodes.ecom.eanmartadmin.secondpage.HomeListModel;
import wackycodes.ecom.eanmartadmin.secondpage.HomePageAdaptor;
import wackycodes.ecom.eanmartadmin.secondpage.SecondActivity;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.categoryIDList;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.categoryList;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.getMainListDataQuery;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;

public class ShopsViewActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView shopsViewRecycler;
    public static ShopsViewAdaptor shopsViewAdaptor;

    private String categoryID = "ID";
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
        String toolTitle = "Shops View";
        toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( toolTitle );
        }catch (NullPointerException ignored){ }

        // -------------
        shopsViewRecycler = findViewById( R.id.shops_view_activity_recycler );
        // Linear Layout...
        LinearLayoutManager homeCatLayoutManager = new LinearLayoutManager( this );
        homeCatLayoutManager.setOrientation( RecyclerView.VERTICAL );
        shopsViewRecycler.setLayoutManager( homeCatLayoutManager );

        catIndex = categoryIDList.indexOf( categoryID ); // Get Category Index...
//        categoryList.add( new ArrayList <HomeListModel>() ); // Add Items (Done At homeList time...
        shopsViewAdaptor = new ShopsViewAdaptor( categoryList.get( catIndex ), catIndex, "Name", categoryID );
        shopsViewRecycler.setAdapter( shopsViewAdaptor );

        if (categoryList.get( catIndex ).size() == 0){
            //  Load List and Set...
            dialog.show();
            getMainListDataQuery( this, dialog, null, categoryID, false, catIndex);
        }else{
            // notifyDataSetChanged...
            shopsViewAdaptor.notifyDataSetChanged();
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
