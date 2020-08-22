package ean.ecom.eanmartadmin.cityareacode.servicecity;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.cityareacode.AreaCodeCityModel;
import ean.ecom.eanmartadmin.database.DBQuery;
import ean.ecom.eanmartadmin.other.DialogsClass;

import static ean.ecom.eanmartadmin.database.DBQuery.areaCodeCityModelList;
import static ean.ecom.eanmartadmin.database.DBQuery.cityCodeAndNameList;

/**
 * A simple {@link Fragment} subclass.
 */

public class ServiceAreaFragment extends Fragment {
    public static ServiceAreaFragment serviceAreaFragment;

    public ServiceAreaFragment(String cityCode) {
        // Required empty public constructor
        this.cityCode = cityCode;
        ServiceCityActivity.CurrentFragment = 1;
    }

    private List<AreaCodeCityModel> areaList = new ArrayList <>();

    private FrameLayout parentFrameLayout;
    private ServiceAreaAdaptor areaAdaptor;
    private RecyclerView cityRecycler;
    private TextView addNewArea;

    private String cityCode;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_service_area, container, false );
        serviceAreaFragment = this;

        dialog = DialogsClass.getDialog( getContext() );
        parentFrameLayout = (FrameLayout)view.findViewById( R.id.area_frame_layout );
        // assign..
        cityRecycler = view.findViewById( R.id.service_city_recycler );
        addNewArea = view.findViewById( R.id.add_new_area );
        // Layout Manager...
        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        cityRecycler.setLayoutManager( layoutManager );

        // adaptor...
        areaAdaptor = new ServiceAreaAdaptor( areaList );
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
        addNewArea.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAddNewAreaDialog( getContext() );
            }
        } );

        return view;
    }

    @Override
    public void onDestroyView() {
        parentFrameLayout.removeAllViews();
        super.onDestroyView();
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

    // Fragment Transaction...
    public void setBackFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
        onDestroyView();
        fragmentTransaction.replace( parentFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }

    private int tempCityIndex = -1;
    private void setAddNewAreaDialog(final Context context){
        final Dialog addAreaDialog = new Dialog( context );
        tempCityIndex = -1;
        addAreaDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        addAreaDialog.setContentView( R.layout.dialog_add_new_area_layout );
        addAreaDialog.setCancelable( false );
        addAreaDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );

        Spinner selectCity = addAreaDialog.findViewById( R.id.select_city_spinner );
        final EditText newAreaName = addAreaDialog.findViewById( R.id.new_area_name );
        final EditText newAreaPin = addAreaDialog.findViewById( R.id.new_area_pin );
        Button dOkBtn = addAreaDialog.findViewById( R.id.dialog_ok_btn );
        Button  dCancelBtn = addAreaDialog.findViewById( R.id.dialog_cancel_btn );

        // Set Spinner...
        ArrayList<String> cityList = new ArrayList <>();
        for (AreaCodeCityModel codeCityModel : cityCodeAndNameList){
            cityList.add( codeCityModel.getCityName() );
        }
        ArrayAdapter <String> dataAdapter = new ArrayAdapter <String>(context,
                android.R.layout.simple_spinner_item, cityList );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCity.setAdapter( dataAdapter );
        selectCity.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
//                tempCityCode = cityCodeAndNameList.get( position ).getCityCode();
                tempCityIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//
            }
        } );

        dOkBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempCityIndex != -1 && isNotEmpty(newAreaName) && isNotEmpty( newAreaPin )){
                    dialog.show();
                    // Add Query to update on database...
                    String cityCode = cityCodeAndNameList.get( tempCityIndex ).getCityCode();
                    String cityName = cityCodeAndNameList.get( tempCityIndex ).getCityName();
                    String areaName = newAreaName.getText().toString();
                    String areaCode = newAreaPin.getText().toString();
                    // add In Local...After Add On Database...
//                    areaCodeCityModelList.add( new AreaCodeCityModel( areaCode, areaName, cityName, cityCode ) );
                    // create Map To Add On Database....
                    Map <String, Object> updateMap = new HashMap <>();
                    updateMap.put( "area_code", Integer.parseInt( areaCode ) );
                    updateMap.put( "area_name", areaName );
                    updateMap.put( "area_city_code", cityCode );
                    updateMap.put( "area_city", cityName );
                    updateMap.put( "area_id", areaCode );
                    updateMap.put( "tags", "" );

                    DBQuery.queryToAddNewArea( context, dialog, updateMap );
                    AreaCodeCityModel tempModel = new AreaCodeCityModel( areaCode, areaName, cityName, cityCode );
                    if (!isAlreadyExist(areaCode)){
                        areaList.add( tempModel );
                    }else{
                        areaList.set( getAreaListIndex(areaCode), tempModel );
                    }
                    areaAdaptor.notifyDataSetChanged();
                    // Add This Area in Local List...
                    addAreaDialog.dismiss();
                }else{
                    Toast.makeText( context, "Please fill missing fields!", Toast.LENGTH_SHORT ).show();
                }
            }
        } );

        dCancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAreaDialog.dismiss();
            }
        } );

        addAreaDialog.show();

    }

    private boolean isNotEmpty(EditText editText){
        if ( TextUtils.isEmpty( editText.getText().toString() )){
            editText.setError( "Required!" );
            return false;
        }else{
            return true;
        }
    }

    private boolean isAlreadyExist(String areaCode){
        boolean check = false;
        for (AreaCodeCityModel areaCodeCityModel : areaList ){
            if (areaCodeCityModel.getAreaCode().equals( areaCode )){
                check = true;
                break;
            }
        }
        return check;
    }

    private int getAreaListIndex(String areaCode){
        int index;
        for ( index = 0; index < areaList.size(); index++ ){
            if (areaList.get( index ).getAreaCode().equals( areaCode )){
                break;
            }
        }
        return index;
    }


}
