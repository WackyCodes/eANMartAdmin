package ean.ecom.eanmartadmin.multisection.shoplist;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.database.ShopQuery;
import ean.ecom.eanmartadmin.multisection.ShopListAdaptor;
import ean.ecom.eanmartadmin.multisection.aboutshop.AboutShopModel;
import ean.ecom.eanmartadmin.other.DialogsClass;
import ean.ecom.eanmartadmin.other.StaticMethods;

import static ean.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;

public class ShopListFragment extends Fragment implements ShopListListener{

    public ShopListFragment( ) {
        // Required empty public constructor
        if (currentCityNameOfShops == null){
            currentCityNameOfShops = CURRENT_CITY_CODE.toUpperCase();
        }
    }

    public static List <AboutShopModel> shopModelList = new ArrayList <>();
    public static String currentCityNameOfShops;

    private ShopListAdaptor shopListAdaptor;
    private RecyclerView recyclerView;

    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_shop_list, container, false );

        dialog = DialogsClass.getDialog( getContext() );

        recyclerView = view.findViewById( R.id.recycler_view_shop_list );
        // Layout...
        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerView.setLayoutManager( layoutManager );

        shopListAdaptor = new ShopListAdaptor( shopModelList );

        // Adaptor...
        if (CURRENT_CITY_CODE.toUpperCase().equals( currentCityNameOfShops ) && shopModelList.size() != 0){
            // Set Adaptor...
            setAdaptor();
        }else{
            // Request To Load...
            dialog.show();
            ShopQuery.getShopList(this );
        }

        return view;
    }

    private void setAdaptor(){
        recyclerView.setAdapter( shopListAdaptor );
        shopListAdaptor.notifyDataSetChanged();
    }

    @Override
    public void onLoadResponse(boolean isSuccess, @Nullable String responseMsg) {
        if (isSuccess){
            setAdaptor();
        }else{
            // Failed...
            StaticMethods.showToast( getContext(), "Failed, "+responseMsg );
        }
        dialog.dismiss();
    }


}