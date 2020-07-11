package wackycodes.ecom.eanmartadmin.multisection;

import android.widget.Filter;
import android.widget.Filterable;

import java.util.List;

public class SearchShopAdaptor extends ShopListAdaptor implements Filterable {

    public SearchShopAdaptor(List <AboutShopModel> shopItemModelList) {
        super( shopItemModelList );
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }
}