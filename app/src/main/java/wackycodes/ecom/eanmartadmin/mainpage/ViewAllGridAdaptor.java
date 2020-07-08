package wackycodes.ecom.eanmartadmin.mainpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import wackycodes.ecom.eanmartadmin.MainActivity;
import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.category.ShopsViewActivity;
import wackycodes.ecom.eanmartadmin.secondpage.BannerAndCatModel;

import static wackycodes.ecom.eanmartadmin.other.StaticValues.CATEGORY_ITEMS_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_ITEMS_LAYOUT_CONTAINER;

public class ViewAllGridAdaptor extends BaseAdapter {

    private List <BannerAndCatModel> catModelList;
    private int type;
    // CATEGORY_ITEMS_LAYOUT_CONTAINER
    // SHOP_ITEMS_LAYOUT_CONTAINER

    public ViewAllGridAdaptor( int type, List <BannerAndCatModel> catModelList) {
        this.type = type;
        this.catModelList = catModelList;
    }

    @Override
    public int getCount() {
        return catModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.category_layout_item, null );

        LinearLayout catItem = view.findViewById( R.id.cat_item );
        LinearLayout addNewItem = view.findViewById( R.id.add_new_cat_item );
        addNewItem.setVisibility( View.GONE );
        catItem.setVisibility( View.VISIBLE );

        ImageView itemImage = view.findViewById( R.id.cat_image );
        TextView itemName =  view.findViewById( R.id.cat_name );
        Glide.with( parent.getContext() ).load( catModelList.get( position ).getImageLink() )
                .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( itemImage );
        itemName.setText( catModelList.get( position ).getName() );

        catItem.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent( parent.getContext(),catModelList.get( position ).getClickID(), catModelList.get( position ).getName());
            }
        } );

        return view;
    }

    private void onClickEvent(Context context, String clickID, String name){
        // TODO;
        switch (type){
            case CATEGORY_ITEMS_LAYOUT_CONTAINER:
                Intent intent = new Intent( context, ShopsViewActivity.class );
                intent.putExtra( "CAT_ID", clickID );
                intent.putExtra( "CAT_NAME", name );
                context.startActivity( intent );
                break;
            case SHOP_ITEMS_LAYOUT_CONTAINER:
                break;
        }

    }

}