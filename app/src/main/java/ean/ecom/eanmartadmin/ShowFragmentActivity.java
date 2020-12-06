package ean.ecom.eanmartadmin;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ean.ecom.eanmartadmin.multisection.shoplist.ShopListFragment;

import static ean.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static ean.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_NAME;
import static ean.ecom.eanmartadmin.other.StaticValues.REQUEST_TO_SHOP_LIST;

/**
 * Created by Shailendra (WackyCodes) on 06/12/2020 22:10
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class ShowFragmentActivity extends AppCompatActivity {

    private FrameLayout frameLayoutShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_show_fragment );

        int fragmentCode = getIntent().getIntExtra( "FRAGMENT_CODE", -1 );

        frameLayoutShow = findViewById( R.id.frameLayout_show_fragment );

        Toolbar toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }

        setFragment( fragmentCode );

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private void setFragment( int fragmentCode ){
        switch (fragmentCode){
            case REQUEST_TO_SHOP_LIST:
                getSupportActionBar().setTitle( "Shop List ("+ CURRENT_CITY_NAME +")" );
                setFrameLayoutShow( new ShopListFragment( ) );
                break;
            case 2:
            default:
                break;
        }
    }

    private void setFrameLayoutShow(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add( frameLayoutShow.getId(), fragment );
        fragmentTransaction.commit();
    }

}
