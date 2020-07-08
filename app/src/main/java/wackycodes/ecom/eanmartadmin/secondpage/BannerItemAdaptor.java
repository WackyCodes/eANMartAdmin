package wackycodes.ecom.eanmartadmin.secondpage;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.addnewitem.AddNewLayout;
import wackycodes.ecom.eanmartadmin.addnewitem.AddNewLayoutActivity;
import wackycodes.ecom.eanmartadmin.other.MyImageView;

import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_CONTAINER_ITEM;

public class BannerItemAdaptor extends RecyclerView.Adapter<BannerItemAdaptor.ViewHolder> {

    List <BannerAndCatModel> bannerAndCatModelList;
    boolean isViewAll;
    int layoutIndex;
    String catOrCollectionID;
    int catIndex;

    public BannerItemAdaptor( String catOrCollectionID, int catIndex, List <BannerAndCatModel> bannerAndCatModelList, boolean isViewAll, int layoutIndex ) {
        this.catOrCollectionID = catOrCollectionID;
        this.catIndex = catIndex;
        this.bannerAndCatModelList = bannerAndCatModelList;
        this.isViewAll = isViewAll;
        this.layoutIndex = layoutIndex;
    }

    @NonNull
    @Override
    public BannerItemAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bannerView =  LayoutInflater.from( parent.getContext() ).inflate( R.layout.banner_slider_item_layout, parent, false );
        return new ViewHolder( bannerView );
    }

    @Override
    public void onBindViewHolder(@NonNull BannerItemAdaptor.ViewHolder holder, int position) {
        if (position < bannerAndCatModelList.size() ){
            String imgLink = bannerAndCatModelList.get( position ).getImageLink();
//            String bgColor = bannerAndCatModelList.get( position ).getTitleOrBgColor();
            String clickID = bannerAndCatModelList.get( position ).getClickID();
            int clickType = bannerAndCatModelList.get( position ).getClickType();
            holder.setData( imgLink, clickID, clickType);
        }
        if ( !isViewAll && position == bannerAndCatModelList.size() ){
            holder.setAddNew();
        }
    }

    @Override
    public int getItemCount() {
        if (isViewAll){
            return bannerAndCatModelList.size();
        }else {
            return bannerAndCatModelList.size()+1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageView bannerImage;
        private LinearLayout addNewItemLayout;
        private ImageView editBannerLayBtn;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerImage = itemView.findViewById( R.id.banner_slider_Image );
            addNewItemLayout = itemView.findViewById( R.id.add_new_item_Linearlayout );
            editBannerLayBtn = itemView.findViewById( R.id.update_item_lay_imgBtn );
        }

        private void setData(String imgLink, final String clickID, int clickType ){
            if (isViewAll){
                editBannerLayBtn.setVisibility( View.VISIBLE );
            }
            bannerImage.setVisibility( View.VISIBLE );
            addNewItemLayout.setVisibility( View.GONE );
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_panorama_black_24dp ) ).into( bannerImage );
            // Set Background color...

            bannerImage.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : Click..
                    showToast( itemView.getContext(), "Code Not Found! "+ clickID );
                }
            } );
        }
        private void setAddNew(){
            bannerImage.setVisibility( View.GONE );
            addNewItemLayout.setVisibility( View.VISIBLE );
            addNewItemLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start Activity...
                    if (catOrCollectionID.equals( "HOME" )){
                        AddNewLayoutActivity.isHomePage = true;
                    }else{
                        AddNewLayoutActivity.isHomePage = false;
                    }
                    Intent intent = new Intent( itemView.getContext(), AddNewLayoutActivity.class );
                    intent.putExtra( "CAT_ID", catOrCollectionID );
                    intent.putExtra( "LAY_TYPE", BANNER_SLIDER_CONTAINER_ITEM );
                    intent.putExtra( "LAY_INDEX", layoutIndex );
                    intent.putExtra( "TASK_UPDATE", false );
                    intent.putExtra( "CAT_INDEX", catIndex );
                    itemView.getContext().startActivity( intent );
//                    Toast.makeText( itemView.getContext(), "Method is not written", Toast.LENGTH_SHORT ).show();
                }
            } );
        }

    }

    private void showToast(Context context, String msg){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }



}
