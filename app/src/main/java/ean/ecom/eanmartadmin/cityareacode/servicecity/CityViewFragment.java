package ean.ecom.eanmartadmin.cityareacode.servicecity;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;

import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.database.DBQuery;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityViewFragment extends Fragment {
    public static CityViewFragment cityViewFragment;

    public CityViewFragment() {
        // Required empty public constructor
        ServiceCityActivity.CurrentFragment = 0;
    }

    private FrameLayout parentFrameLayout;

    public static CityViewAdaptor cityViewAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_city_view, container, false );
        cityViewFragment = this;

        //city_grid_view
        parentFrameLayout = (FrameLayout)view.findViewById( R.id.city_frame_layout );
        GridView cityGridView = view.findViewById( R.id.city_grid_view );

        cityViewAdaptor = new CityViewAdaptor( ); // DBQuery.cityCodeAndNameList
        cityGridView.setAdapter( cityViewAdaptor );
        cityViewAdaptor.notifyDataSetChanged();

        if (DBQuery.cityCodeAndNameList.size()==0){
            DBQuery.getCityAndCityCode();
        }

        return view;

    }

    @Override
    public void onDestroyView() {
        parentFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
        onDestroyView();
        fragmentTransaction.replace( parentFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }


}
