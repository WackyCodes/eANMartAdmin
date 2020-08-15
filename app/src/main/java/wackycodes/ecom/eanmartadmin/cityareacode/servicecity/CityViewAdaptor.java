package wackycodes.ecom.eanmartadmin.cityareacode.servicecity;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.cityareacode.AreaCodeCityModel;
import wackycodes.ecom.eanmartadmin.mainpage.MainActivityGridModel;

import static wackycodes.ecom.eanmartadmin.other.StaticValues.CLOSE_SERVICE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.OPEN_SERVICE;

/**
 * Created by Shailendra (WackyCodes) on 15/08/2020 23:27
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class CityViewAdaptor extends BaseAdapter {

    private List <AreaCodeCityModel> cityModelList;

    public CityViewAdaptor(List <AreaCodeCityModel> cityModelList) {
        this.cityModelList = cityModelList;
    }

    @Override
    public int getCount() {
        return cityModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.city_view_item, null );
        LinearLayout contentLayout = view.findViewById( R.id.city_item_content_layout );
        LinearLayout addNewLayout = view.findViewById( R.id.new_city_layout );
        if (position < cityModelList.size()){
            contentLayout.setVisibility( View.VISIBLE );
            addNewLayout.setVisibility( View.GONE );
            ImageView cityImage = view.findViewById( R.id.city_image );
            TextView cityName =  view.findViewById( R.id.city_name );
            TextView cityID =  view.findViewById( R.id.city_id );
            TextView serviceAvailable =  view.findViewById( R.id.service_open_close_text );
            TextView publicMessage =  view.findViewById( R.id.public_message_activate_text );

            cityName.setText( cityModelList.get( position ).getCityName() );
            cityID.setText( cityModelList.get( position ).getCityCode() );

            if (cityModelList.get( position ).isServiceAvailable()){
                serviceAvailable.setText( "OPEN" );
                serviceAvailable.setBackgroundColor( parent.getResources().getColor( R.color.colorGreen ) );
            }else{
                serviceAvailable.setText( "CLOSE" );
                serviceAvailable.setBackgroundColor( parent.getResources().getColor( R.color.colorRed ) );
            }

            if (cityModelList.get( position ).isPublicMessage()){
                publicMessage.setText( "Shown" );
                publicMessage.setBackgroundColor( parent.getResources().getColor( R.color.colorDarkViolet ) );
            }else{
                publicMessage.setText( "NONE" );
                publicMessage.setBackgroundColor( parent.getResources().getColor( R.color.colorGreen ) );
            }
        }else{
            contentLayout.setVisibility( View.GONE );
            addNewLayout.setVisibility( View.VISIBLE );

        }

        view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );

        return view;
    }

    // Change City Service Status...
    private void updateService(final Context context){
        /// Single Ok Button ...
        final Dialog serviceDialog = new Dialog( context );
        serviceDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        serviceDialog.setContentView( R.layout.dialog_a_spinner );
        serviceDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        serviceDialog.setCancelable( false );
        final RadioGroup radioGroup = serviceDialog.findViewById( R.id.dialog_radio_group );
        final RadioButton openServiceBtn = serviceDialog.findViewById( R.id.open_radio_btn );
        final RadioButton closeServiceBtn = serviceDialog.findViewById( R.id.close_radio_btn );
        final EditText closeMessageEt = serviceDialog.findViewById( R.id.close_message );
        TextView cancelBtn = serviceDialog.findViewById( R.id.dialog_cancel_btn );
        TextView okBtn = serviceDialog.findViewById( R.id.dialog_ok_btn );

        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == closeServiceBtn.getId()){
                    closeMessageEt.setVisibility( View.VISIBLE );
                }else{
                    closeMessageEt.setVisibility( View.GONE );
                }
            }
        } );

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int memberType = -1;
                if (radioGroup.getCheckedRadioButtonId() == openServiceBtn.getId()){
                    memberType = OPEN_SERVICE;
                }
                else if(radioGroup.getCheckedRadioButtonId() == closeServiceBtn.getId()){
                    memberType = CLOSE_SERVICE;
                }

                if (memberType != -1 ){
                    Map <String, Object> updateMap = new HashMap <>();
                    updateMap.clear();
                    if ( memberType == OPEN_SERVICE ){
                        updateMap.put( "available_service", true );
                    }else if ( !TextUtils.isEmpty( closeMessageEt.getText().toString() )){
                        updateMap.put( "available_service", false );
                        updateMap.put( "service_alert_type", "1" );
                        updateMap.put( "service_alert_text", closeMessageEt.getText().toString() );
                    }else{
                        closeMessageEt.setError( "Required!" );
                    }
                    //  Request
                    serviceDialog.dismiss();
                }else{
                    Toast.makeText( context, "Please Choose Option!", Toast.LENGTH_SHORT ).show();
                }

            }
        } );

        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceDialog.dismiss();
            }
        } );

        serviceDialog.show();

    }




}
