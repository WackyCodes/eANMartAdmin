package wackycodes.ecom.eanmartadmin.cityareacode.servicecity;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.database.DBQuery;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityViewFragment extends Fragment {


    public CityViewFragment() {
        // Required empty public constructor
    }

    public static CityViewAdaptor cityViewAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_city_view, container, false );

        //city_grid_view
        GridView cityGridView = view.findViewById( R.id.city_grid_view );

        cityViewAdaptor = new CityViewAdaptor( ); // DBQuery.cityCodeAndNameList
        cityGridView.setAdapter( cityViewAdaptor );
        cityViewAdaptor.notifyDataSetChanged();

        if (DBQuery.cityCodeAndNameList.size()==0){
            DBQuery.getCityAndCityCode();
        }

        return view;

    }



}
