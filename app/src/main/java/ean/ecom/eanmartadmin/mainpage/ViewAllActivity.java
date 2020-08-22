package ean.ecom.eanmartadmin.mainpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import java.util.List;

import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.mainpage.homesection.BannerAndCatModel;
import ean.ecom.eanmartadmin.mainpage.homesection.BannerItemAdaptor;

import static ean.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static ean.ecom.eanmartadmin.other.StaticValues.CATEGORY_ITEMS_LAYOUT_CONTAINER;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_ITEMS_LAYOUT_CONTAINER;

public class ViewAllActivity extends AppCompatActivity {

    private GridView viewAllGridView;
    private RecyclerView viewAllRecycler;
    private ViewAllGridAdaptor viewAllGridAdaptor;
    private BannerItemAdaptor viewAllRecyclerAdaptor;

    private int type;
    public static List <BannerAndCatModel> viewAllList;

    private String catOrCollectionID;
    private int catIndex;
    private int layoutIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_all );

        type = getIntent().getIntExtra( "TYPE", -1 );

        // assign..
        viewAllGridView =  findViewById( R.id.view_all_grid_view );
        viewAllRecycler =  findViewById( R.id.view_all_recycler );

        // Linear Layout...
        LinearLayoutManager homeCatLayoutManager = new LinearLayoutManager( this );
        homeCatLayoutManager.setOrientation( RecyclerView.VERTICAL );
        viewAllRecycler.setLayoutManager( homeCatLayoutManager );

        switch (type){
            case BANNER_SLIDER_LAYOUT_CONTAINER:
                // Case 1
                catOrCollectionID = getIntent().getStringExtra( "CAT_COLL_ID" );
                catIndex = getIntent().getIntExtra( "CAT_INDEX", -1);
                layoutIndex = getIntent().getIntExtra( "LAY_INDEX", -1);

                viewAllGridView.setVisibility( View.GONE );
                viewAllRecycler.setVisibility( View.VISIBLE );

                viewAllRecyclerAdaptor = new BannerItemAdaptor( catOrCollectionID, catIndex, viewAllList, true, layoutIndex  );
                viewAllRecycler.setAdapter( viewAllRecyclerAdaptor );
                viewAllRecyclerAdaptor.notifyDataSetChanged();
                break;
            case CATEGORY_ITEMS_LAYOUT_CONTAINER:
            case SHOP_ITEMS_LAYOUT_CONTAINER:
                // Case 2.
                viewAllRecycler.setVisibility( View.GONE );
                viewAllGridView.setVisibility( View.VISIBLE );
                layoutIndex = getIntent().getIntExtra( "LAY_INDEX", -1);

                viewAllGridAdaptor = new ViewAllGridAdaptor( type, layoutIndex, viewAllList );
                viewAllGridView.setAdapter( viewAllGridAdaptor );
                viewAllGridAdaptor.notifyDataSetChanged();
                break;
            default:
                break;
        }


    }


}