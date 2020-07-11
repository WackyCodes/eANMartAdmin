package wackycodes.ecom.eanmartadmin.multisection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import wackycodes.ecom.eanmartadmin.R;

import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_NON_VEG;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_NO_SHOW;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_VEG;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_VEG_NON;

public class ShopListAdaptor  extends RecyclerView.Adapter <ShopListAdaptor.ViewHolder> {

    private List <AboutShopModel> shopItemModelList;

    public ShopListAdaptor(List <AboutShopModel> shopItemModelList) {
        this.shopItemModelList = shopItemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View shopView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.shop_rectangle_layout_item, parent, false );
        return new ViewHolder( shopView );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AboutShopModel shopItemModel = shopItemModelList.get( position );
        String sID = shopItemModel.getShopID();
        String sName = shopItemModel.getShopName();
        String sLogo = shopItemModel.getShopLogo();
        String sCategory = shopItemModel.getShopCategory();
        String sRating = shopItemModel.getShopRatingStars();
        int sVegType = shopItemModel.getShopVegNonCode();
        holder.setData( sID, sLogo, sName, sCategory, sRating, sVegType );
    }

    @Override
    public int getItemCount() {
        return shopItemModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView shopLogo;
        private TextView shopName;
        private TextView shopCategory;
        private TextView shopRating;
        private ImageView shopVegType;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            shopLogo = itemView.findViewById( R.id.shop_logo );
            shopName = itemView.findViewById( R.id.shop_name );
            shopCategory = itemView.findViewById( R.id.shop_category );
            shopRating = itemView.findViewById( R.id.shop_rating );
            shopVegType = itemView.findViewById( R.id.shop_veg_type_image );
        }

        @SuppressLint("NewApi")
        private void setData(final String shopID, String sLogoLink, String sName, String sCategory, String sRating, int sVegType ){

            Glide.with( itemView.getContext() ).load( sLogoLink ).apply( new RequestOptions()
                    .placeholder( R.drawable.ic_photo_black_24dp ) ).into( shopLogo );

            shopName.setText( sName );
            shopCategory.setText( sCategory );
            shopRating.setText( sRating );
            if (sVegType == SHOP_TYPE_VEG){
                shopVegType.setImageTintList( itemView.getContext().getColorStateList( R.color.colorGreen )  );
            }else if(sVegType == SHOP_TYPE_NON_VEG){
                shopVegType.setImageTintList( itemView.getContext().getColorStateList( R.color.colorRed )  );
            }else if(sVegType == SHOP_TYPE_VEG_NON){
                shopVegType.setVisibility( View.GONE );
            }else if(sVegType == SHOP_TYPE_NO_SHOW){
                shopVegType.setVisibility( View.GONE );
            }

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  : Use Shop ID and Go TO...
//                    StaticMethods.showToast( itemView.getContext(), "Code Not Found..!" );
                    Intent intent = new Intent( itemView.getContext(), ShopHomeActivity.class );
                    intent.putExtra( "SHOP_ID", shopID );
                    itemView.getContext().startActivity( intent );

                }
            } );

        }

    }

}
