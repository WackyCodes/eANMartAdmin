package wackycodes.ecom.eanmartadmin.cityareacode.servicecity;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.DialogCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.cityareacode.AreaCodeCityModel;
import wackycodes.ecom.eanmartadmin.database.DBQuery;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;
import wackycodes.ecom.eanmartadmin.other.StaticValues;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.areaCodeCityModelList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceAreaFragment extends Fragment {

    public ServiceAreaFragment(String cityCode) {
        // Required empty public constructor
        this.cityCode = cityCode;
    }

    private List<AreaCodeCityModel> areaList = new ArrayList <>();

    public static ServiceAreaAdaptor areaAdaptor;
    private RecyclerView cityRecycler;

    private String cityCode;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_service_area, container, false );

        dialog = DialogsClass.getDialog( getContext() );
        // assign..
        cityRecycler = view.findViewById( R.id.service_city_recycler );
        // Layout Manager...
        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        cityRecycler.setLayoutManager( layoutManager );

        // adaptor...
        ServiceAreaAdaptor areaAdaptor = new ServiceAreaAdaptor( areaList );
        cityRecycler.setAdapter( areaAdaptor );
        areaAdaptor.notifyDataSetChanged();

        // Reloading List...
        dialog.show();
        for (AreaCodeCityModel areaCodeCityModel : areaCodeCityModelList){
            if (areaCodeCityModel.getCityCode().equals( cityCode )){
                areaList.add( areaCodeCityModel );
                areaAdaptor.notifyDataSetChanged();
            }
        }

        if (areaList.size()>0){

        }else{

        }
        dialog.dismiss();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate( R.menu.menu_service_city_option, menu);
        super.onCreateOptionsMenu( menu, inflater );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Toast.makeText( getContext(), "Back Pressed!", Toast.LENGTH_SHORT ).show();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
