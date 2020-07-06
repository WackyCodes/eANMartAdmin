package wackycodes.ecom.eanmartadmin.cityareacode;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.admin.AreaCodeCityModel;

public class SelectAreaCityAdaptor  extends ArrayAdapter <AreaCodeCityModel> {

    private Context context;
    private int resourceId;
    private List <AreaCodeCityModel> items, tempItems, suggestions;
    public SelectAreaCityAdaptor(@NonNull Context context, int resourceId, ArrayList<AreaCodeCityModel> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList <>(items);
        suggestions = new ArrayList<>();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            AreaCodeCityModel areaCodeCityModel = getItem(position);
            TextView cityName = (TextView) view.findViewById( R.id.area_city_name);
            TextView areaPinCode = (TextView) view.findViewById( R.id.area_pin_code);
            TextView areaName = (TextView) view.findViewById( R.id.area_name);
            areaName.setText( areaCodeCityModel.getAreaName() );
            areaPinCode.setText( areaCodeCityModel.getAreaCode() );
            cityName.setText( areaCodeCityModel.getCityName() );

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    @Nullable
    @Override
    public AreaCodeCityModel getItem(int position) {
        return items.get(position);
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return areaNameFilter;
    }
    private Filter areaNameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            AreaCodeCityModel areaCodeCityModel = (AreaCodeCityModel) resultValue;
            return areaCodeCityModel.getAreaCode();
//            return (CharSequence) resultValue;
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (AreaCodeCityModel areaCodeCityModel: tempItems) {
                    if (areaCodeCityModel.getCityName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(areaCodeCityModel);
                    }else
                    if (areaCodeCityModel.getAreaCode().startsWith(charSequence.toString())){
                        suggestions.add(areaCodeCityModel);
                    }else
                    if (areaCodeCityModel.getAreaName().toLowerCase().startsWith(charSequence.toString().toLowerCase())){
                        suggestions.add(areaCodeCityModel);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<AreaCodeCityModel> tempValues = (ArrayList<AreaCodeCityModel>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (AreaCodeCityModel areaCodeCityModel : tempValues) {
                    add(areaCodeCityModel);
                }
                notifyDataSetChanged();
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };




}
