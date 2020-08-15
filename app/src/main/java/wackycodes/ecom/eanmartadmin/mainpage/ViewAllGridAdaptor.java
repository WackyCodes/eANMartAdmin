package wackycodes.ecom.eanmartadmin.mainpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.categorysection.ShopsViewActivity;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.BannerAndCatModel;

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

        // For type = CATEGORY_ITEMS_LAYOUT_CONTAINER
        LinearLayout updateCategoryLayout = view.findViewById( R.id.update_category_layout );
        LinearLayout updateVisibilityLayout = view.findViewById( R.id.visibility_layout );
        if (type == CATEGORY_ITEMS_LAYOUT_CONTAINER){
            updateCategoryLayout.setVisibility( View.VISIBLE );
            updateVisibilityLayout.setVisibility( View.VISIBLE );
        }else {
            updateCategoryLayout.setVisibility( View.GONE );
            updateVisibilityLayout.setVisibility( View.GONE );
        }
        // Add For Update Category...
        TextView updateImage = view.findViewById( R.id.update_image );
        TextView updateName = view.findViewById( R.id.update_name );
        TextView updateVisibility = view.findViewById( R.id.update_visibility_text );
        ImageView visibilityIcon = view.findViewById( R.id.update_visibility_image );
        // Set Visibility Layout
        if (catModelList.get( position ).getExtraText()!=null){
            if ( catModelList.get( position ).getExtraText().equals( "0" ) ){ // Invisible
                visibilityIcon.setImageResource( R.drawable.ic_visibility_black_24dp );
                updateVisibility.setText( "Show Category" );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    updateVisibilityLayout.setBackgroundTintList( parent.getResources().getColorStateList( R.color.colorRecyclerBack ) );
                }
            }else{
                visibilityIcon.setImageResource( R.drawable.ic_visibility_off_black_24dp );
                updateVisibility.setText( "Hide Category" );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    updateVisibilityLayout.setBackgroundTintList( parent.getResources().getColorStateList( R.color.colorYellow ) );
                }
            }
        }
        // Set Click Listener for Update...
        updateImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( parent.getContext(), "Code Not Found!", Toast.LENGTH_SHORT ).show();
            }
        } );
        updateName.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( parent.getContext(), "Code Not Found!", Toast.LENGTH_SHORT ).show();
            }
        } );
        updateVisibilityLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( parent.getContext(), "Code Not Found!", Toast.LENGTH_SHORT ).show();
            }
        } );


        return view;
    }

    private void onClickEvent(Context context, String clickID, String name){
        // TODO;
        switch (type){ // CATEGORY_ITEMS_LAYOUT_CONTAINER
            case CATEGORY_ITEMS_LAYOUT_CONTAINER:
                Intent intent = new Intent( context, ShopsViewActivity.class );
                intent.putExtra( "CAT_ID", clickID );
                intent.putExtra( "CAT_NAME", name );
                context.startActivity( intent );

//                Intent intent = new Intent( itemView.getContext(), ShopsViewActivity.class );
//                intent.putExtra( "CAT_ID", catID );
//                intent.putExtra( "CAT_NAME", catName );
//                itemView.getContext().startActivity( intent );
                break;
            case SHOP_ITEMS_LAYOUT_CONTAINER:

                break;
        }

    }

}