package wackycodes.ecom.eanmartadmin.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.secondpage.HomePageAdaptor;

public class ShopsViewActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView shopsViewRecycler;
    public static HomePageAdaptor homePageAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shops_view );

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


    }




}
