package wackycodes.ecom.eanmartadmin.multisection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import wackycodes.ecom.eanmartadmin.R;

public class ShopHomeActivity extends AppCompatActivity {

    private TextView shopIdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shop_home );

        String shopID = getIntent().getStringExtra( "SHOP_ID" );

        shopIdText = findViewById( R.id.shop_id_text );
        shopIdText.setText( shopID );

    }
}
