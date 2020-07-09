package wackycodes.ecom.eanmartadmin.addnewitem;

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
import wackycodes.ecom.eanmartadmin.shopsgrid.ShopListModel;

public class SelectShopAdaptor extends ArrayAdapter <ShopListModel> {

    private Context context;
    private int resourceId;
    private List <ShopListModel> items, tempItems, suggestions;

    public SelectShopAdaptor(@NonNull Context context, int resourceId, ArrayList <ShopListModel> items) {
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
            ShopListModel areaCodeCityModel = getItem(position);
            TextView shopID = (TextView) view.findViewById( R.id.shop_id);
            TextView shopName = (TextView) view.findViewById( R.id.shop_name);
            shopID.setText( areaCodeCityModel.getShopID() );
            shopName.setText( areaCodeCityModel.getShopName() );

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public ShopListModel getItem(int position) {
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
            ShopListModel areaCodeCityModel = (ShopListModel) resultValue;
            return areaCodeCityModel.getShopName();
//            return (CharSequence) resultValue;
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (ShopListModel shopListModel: tempItems) {
                    if (shopListModel.getShopName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(shopListModel);
                    }else
                    if (shopListModel.getShopID().startsWith(charSequence.toString())){
                        suggestions.add(shopListModel);
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
            ArrayList<ShopListModel> tempValues = (ArrayList<ShopListModel>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (ShopListModel areaCodeCityModel : tempValues) {
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
