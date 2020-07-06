package wackycodes.ecom.eanmartadmin.secondpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmartadmin.R;

public class SecondActivity extends AppCompatActivity {

    public static TextView cityName;
    public static TextView userName;

    private Toolbar toolbar;

    private RecyclerView secondRecycler;
    public static HomePageAdaptor homePageAdaptor;

//    public static List<List <HomeListModel>> commonCatList = new ArrayList <>();
    public static List<HomeListModel> homePageList = new ArrayList <>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_second );

        //...
        cityName = findViewById( R.id.user_city );
        userName = findViewById( R.id.user_name );

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

    }


}
