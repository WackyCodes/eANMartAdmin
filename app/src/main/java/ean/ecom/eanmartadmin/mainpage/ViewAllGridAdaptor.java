package ean.ecom.eanmartadmin.mainpage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.addnewitem.AddNewImageActivity;
import ean.ecom.eanmartadmin.database.DBQuery;
import ean.ecom.eanmartadmin.mainpage.homesection.categorysection.ShopsViewActivity;
import ean.ecom.eanmartadmin.mainpage.homesection.BannerAndCatModel;
import ean.ecom.eanmartadmin.other.DialogsClass;
import ean.ecom.eanmartadmin.other.StaticMethods;

import static ean.ecom.eanmartadmin.database.DBQuery.homePageList;
import static ean.ecom.eanmartadmin.other.StaticValues.CATEGORY_ITEMS_LAYOUT_CONTAINER;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_ITEMS_LAYOUT_CONTAINER;

public class ViewAllGridAdaptor extends BaseAdapter {

    private Dialog dialog;
    private List <BannerAndCatModel> catModelList;
    private int type;
    private int layoutIndex;
    // CATEGORY_ITEMS_LAYOUT_CONTAINER
    // SHOP_ITEMS_LAYOUT_CONTAINER
    private ViewAllGridAdaptor adaptor;

    public ViewAllGridAdaptor( int type, int layoutIndex, List <BannerAndCatModel> catModelList) {
        this.type = type;
        this.layoutIndex = layoutIndex;
        this.catModelList = catModelList;
        adaptor = this;
    }

    @Override
    public int getCount() {
        if (type == CATEGORY_ITEMS_LAYOUT_CONTAINER){
            // To add New Category...
            return catModelList.size() + 1;
        }else{
            return catModelList.size();
        }
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

        ImageView itemImage = view.findViewById( R.id.cat_image );
        TextView itemName =  view.findViewById( R.id.cat_name );

        // Data Set...
        if (position < catModelList.size()){
            addNewItem.setVisibility( View.GONE );
            catItem.setVisibility( View.VISIBLE );
            // Set Main Data...
            Glide.with( parent.getContext() ).load( catModelList.get( position ).getImageLink() )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( itemImage );
            itemName.setText( catModelList.get( position ).getName() );

        }else{
            addNewItem.setVisibility( View.VISIBLE );
            catItem.setVisibility( View.GONE );
        }

        // Item Click Listener...
        catItem.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent( parent.getContext(),catModelList.get( position ).getClickID(), catModelList.get( position ).getName());
            }
        } );

        // Only For New Category Add ... Use
        addNewItem.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogToChangeName( parent.getContext(), catModelList.size()+1, null, true, null );
            }
        } );

        // For type = CATEGORY_ITEMS_LAYOUT_CONTAINER ----------------------------------------------
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
        if ( position < catModelList.size() ){
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
            else{

            }
        }
        // Set Click Listener for Update...
        updateImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // : Update Image....
                Intent intent = new Intent( parent.getContext(), AddNewImageActivity.class );
                intent.putExtra( "LAYOUT_INDEX", layoutIndex );
                intent.putExtra( "CAT_INDEX", position );
                parent.getContext().startActivity( intent );
            }
        } );
        updateName.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogToChangeName( parent.getContext(), position+1, catModelList.get( layoutIndex ).getName(), false, catModelList.get( layoutIndex ).getLayoutId() );
            }
        } );
        updateVisibilityLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText( parent.getContext(), "Code Not Found!", Toast.LENGTH_SHORT ).show();
                if (catModelList.get( position ).getExtraText().equals( "0" )){
                    updateCatVisibility(  parent.getContext(), true, position );
                }else{
                    updateCatVisibility(  parent.getContext(), false, position );
                }
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

    private void dialogToChangeName(final Context context, final int catNo, @Nullable String updateCatName, final boolean isNew,@Nullable final String layoutID ){
        dialog = DialogsClass.getDialog( context );

        final Dialog progressDialog = new Dialog( context );
        progressDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        progressDialog.setContentView( R.layout.dialog_single_edit_text );
        progressDialog.setCancelable( false );
        progressDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );

        TextView  dTitle = progressDialog.findViewById( R.id.dialog_title );
        final EditText dEditText = progressDialog.findViewById( R.id.dialog_editText );
        Button dOkBtn = progressDialog.findViewById( R.id.dialog_ok_btn );
        Button  dCancelBtn = progressDialog.findViewById( R.id.dialog_cancel_btn );

        dTitle.setText( "Enter Category Name" );
        if (isNew){
            dEditText.setHint( "Category Name" );
            dOkBtn.setText( "Add" );
        }else{
            dEditText.setText( updateCatName );
            dOkBtn.setText( "Update" );
        }

        dOkBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                String catID = getNewCatID(dEditText.getText().toString());

                if (TextUtils.isEmpty( dEditText.getText().toString() )){
                    dEditText.setError( "Required!" );
                    dialog.dismiss();
                }else if (isValidName(dEditText, catID)){
                    Map <String, Object> uploadMap = new HashMap <>();
                    if (isNew){
                        // new
                        uploadMap.put( "no_of_cat", catNo );
                        uploadMap.put( "layout_id", "cat_layout" );
                        uploadMap.put( "cat_id_"+catNo, catID );
                        uploadMap.put( "cat_image_"+catNo, "" );
                        uploadMap.put( "cat_name_"+catNo, dEditText.getText().toString() );
                        uploadMap.put( "cat_visibility_"+catNo, false );
                        uploadMap.put( "cat_delete_id_"+catNo, catID );

                        // TODO : Check from local list...
                        // Update on Database...
                        DBQuery.setNewCategoryOnDataBase( context, dialog, catID, dEditText.getText().toString(), layoutIndex, uploadMap );

                        homePageList.get( layoutIndex ).getBannerAndCatModelList().add( new BannerAndCatModel(
                                catID, "", catID, CATEGORY_ITEMS_LAYOUT_CONTAINER,  dEditText.getText().toString(), "0"
                        ) );
                    }else{
                        // Update...
//                        categoryTypeModelList.get( catNo).setNameOrExtraText( dEditText.getText().toString() );
                        uploadMap.put( "cat_name_" + (catNo) , dEditText.getText().toString() );
                        DBQuery.updateCategoryOnDatabase(context, dialog, layoutID, uploadMap );
                        homePageList.get( layoutIndex ).getBannerAndCatModelList().get( catNo-1 ).setName( dEditText.getText().toString() );
                        catModelList.get( catNo-1 ).setExtraText( dEditText.getText().toString()  );
                    }
                    // notify to local changes..
                    adaptor.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }
        } );
        dCancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
            }
        } );
        progressDialog.show();


    }

    private String getNewCatID(String catName){
        if (catName.length() <= 5 ){
            catName = catName.toUpperCase().replace( " ", "" )+ "_" + StaticMethods.getTwoDigitRandom();
            return catName;
        }else{
            catName = catName.toUpperCase().replace( " ", "" );
            return catName.trim();
        }
    }

    // TODO:
    private boolean isValidName( EditText editText, String catID){
        String catName = editText.getText().toString();
        if (catName.toUpperCase().equals( "HOME" ) || catName.toUpperCase().equals( "ADMINS" ) || catName.toUpperCase().equals( "ORDERS" ) ||
                catName.toUpperCase().equals( "PRODUCTS" ) || catName.toUpperCase().equals( "NOTIFICATIONS" )){
            editText.setError( "This Name reserved!" );
            return false;
        }else{
            // Check in local...

            return true;
        }
    }

    private void updateCatVisibility(Context context, final boolean visible, final int catIndex ){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (visible){
            builder.setTitle("Do you want to show this category to user?");
            builder.setMessage("It will show all the shops inside this category!");
        }else{
            builder.setTitle("Do you want to hide this category to user?");
            builder.setMessage("It will hide all the shops inside this category!");
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // update in local..
//                homePageList.get( layoutIndex ).getBannerAndCatModelList().get( catIndex ).setVisibility( visible );
                if (visible){
                    homePageList.get( layoutIndex ).getBannerAndCatModelList().get( catIndex ).setExtraText( "1" );
                    catModelList.get( catIndex ).setExtraText( "1" );
                }else{
                    homePageList.get( layoutIndex ).getBannerAndCatModelList().get( catIndex ).setExtraText( "0" );
                    catModelList.get( catIndex ).setExtraText( "0" );
                }
                // notify to local changes..
                adaptor.notifyDataSetChanged();
                // Update on Database...
                Map<String, Object> updateMap = new HashMap <>();
                updateMap.put( "cat_visibility_"+ catIndex, visible );

                DBQuery.updateCategoryOnDatabase(null, null, homePageList.get( catIndex ).getLayoutID(), updateMap );
                dialog.dismiss();
            }
        });

        builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                dialog.dismiss();
            }
        } );

        builder.show();
    }


}