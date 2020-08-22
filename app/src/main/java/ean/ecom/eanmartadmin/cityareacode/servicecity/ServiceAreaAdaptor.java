package ean.ecom.eanmartadmin.cityareacode.servicecity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.cityareacode.AreaCodeCityModel;

/**
 * Created by Shailendra (WackyCodes) on 13/08/2020 23:41
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class ServiceAreaAdaptor extends RecyclerView.Adapter<ServiceAreaAdaptor.ViewHolder> {

    private List<AreaCodeCityModel> cityModelList;

    public ServiceAreaAdaptor(List <AreaCodeCityModel> cityModelList) {
        this.cityModelList = cityModelList;
    }

    @NonNull
    @Override
    public ServiceAreaAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.select_area_list_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAreaAdaptor.ViewHolder holder, int position) {
        holder.setData( position );
    }

    @Override
    public int getItemCount() {
        return cityModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cityName;
        private TextView areaCode;
        private TextView areaName;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            cityName = itemView.findViewById( R.id.area_city_name );
            areaCode = itemView.findViewById( R.id.area_pin_code );
            areaName = itemView.findViewById( R.id.area_name );

        }

        private void setData(int index){
            // Set Data...
            cityName.setText( cityModelList.get( index ).getCityName() );
            areaCode.setText( cityModelList.get( index ).getAreaCode() );
            areaName.setText( cityModelList.get( index ).getAreaName() );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText( v.getContext(), "Code Not Found!", Toast.LENGTH_SHORT ).show();
                }
            } );

        }



    }






}
