package ean.ecom.eanmartadmin.multisection.aboutshop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.database.DBQuery;
import ean.ecom.eanmartadmin.database.ShopQuery;

import static ean.ecom.eanmartadmin.database.ShopQuery.versionModelList;
import static ean.ecom.eanmartadmin.other.StaticMethods.isValid;
import static ean.ecom.eanmartadmin.other.StaticMethods.isValidEmail;
import static ean.ecom.eanmartadmin.other.StaticValues.ADMIN_SHOP_FOUNDER;
import static ean.ecom.eanmartadmin.other.StaticValues.ADMIN_SHOP_MANAGER;

/**
 * Created by Shailendra (WackyCodes) on 05/12/2020 12:39
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */

public class UpdateShopFragment extends DialogFragment implements UpdateShopListener.UpdateListener,
        UpdateShopListener.LoadListListener {
    public static final int DIALOG_ADD_NEW_MEMBER = 1;
    public static final int DIALOG_PRODUCT_OPTIONS = 2;

    private int DIALOG_CODE;
    private UpdateShopListener listener;

    private String shopID;
    private String shopName;

    //Add New Member...
    public UpdateShopFragment(int DIALOG_CODE, UpdateShopListener listener, String shopID, String shopName) {
        this.DIALOG_CODE = DIALOG_CODE;
        this.listener = listener;
        this.shopID = shopID;
        this.shopName = shopName;
    }

    //Add New Member...
    public UpdateShopFragment(int DIALOG_CODE, UpdateShopListener listener, String shopID) {
        this.DIALOG_CODE = DIALOG_CODE;
        this.listener = listener;
        this.shopID = shopID;
    }

    private ImageView imageViewCloseBtn;
    private TextView btnAdd;
    private RecyclerView recyclerViewVersionType;
    private RelativeLayout relativeLayoutAddNewMember;
    private RelativeLayout relativeLayoutProductOptions;

    ///
    private TextView shopNameText;
    private TextView shopIdText;
    private EditText memberName;
    private EditText memberEmail;
    private EditText memberMobile;
    private RadioGroup typeRadioGroup;
    private RadioButton shopOwner;
    private RadioButton shopManager;
    ///
    private Switch showVegNonOptions;

    private Dialog updateDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_update_shop_fragment, container );
        updateDialog = getDialog();
        updateDialog.setCancelable( false );
        updateDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        updateDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );

        imageViewCloseBtn = view.findViewById( R.id.iv_close_dialog_btn );
        btnAdd = view.findViewById( R.id.tv_add_btn );
        recyclerViewVersionType = view.findViewById( R.id.recycler_view_version_weight );
        relativeLayoutAddNewMember = view.findViewById( R.id.relative_layout_add_new_member );
        relativeLayoutProductOptions = view.findViewById( R.id.relative_layout_product_options );
        setLayoutVisibility( view );

        imageViewCloseBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        } );

        btnAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClick();
            }
        } );
        return view;
    }

    private void setLayoutVisibility(View view){
        switch (DIALOG_CODE){
            case DIALOG_ADD_NEW_MEMBER:
                relativeLayoutAddNewMember.setVisibility( View.VISIBLE );
                relativeLayoutProductOptions.setVisibility( View.GONE );
                /// Add New Member...
                shopNameText = view.findViewById( R.id.dialog_shop_name );
                shopIdText = view.findViewById( R.id.dialog_shop_id_text );
                memberName = view.findViewById( R.id.dialog_shop_member_name );
                memberEmail = view.findViewById( R.id.dialog_shop_member_email );
                memberMobile = view.findViewById( R.id.dialog_member_mobile );
                typeRadioGroup = view.findViewById( R.id.dialog_radio_group );
                shopOwner = view.findViewById( R.id.shop_owner );
                shopManager = view.findViewById( R.id.shop_manager );

                shopNameText.setText( shopName );
                shopIdText.setText( "("+shopID+")" );
                btnAdd.setText( "Add Member" );
                break;
            case DIALOG_PRODUCT_OPTIONS:
                relativeLayoutAddNewMember.setVisibility( View.GONE );
                relativeLayoutProductOptions.setVisibility( View.VISIBLE );
                btnAdd.setText( "Update Options" );
                /// Veg Non...
                showVegNonOptions = view.findViewById( R.id.switch_show_veg_non_option );

                /// Recycler...
                LinearLayoutManager layoutManager = new LinearLayoutManager( view.getContext() );
                layoutManager.setOrientation( RecyclerView.VERTICAL );
                recyclerViewVersionType.setLayoutManager( layoutManager );

                setVersionModelList();

                btnAdd.setText( "Update" );
                break;
            default:
                break;
        }
    }

    private void onAddButtonClick(){
        switch (DIALOG_CODE){
            case DIALOG_ADD_NEW_MEMBER:
                listener.showDialog();
                int memberType = -1;
                if (typeRadioGroup.getCheckedRadioButtonId() == shopOwner.getId()){
                    memberType = ADMIN_SHOP_FOUNDER;
                }
                else if(typeRadioGroup.getCheckedRadioButtonId() == shopManager.getId()){
                    memberType = ADMIN_SHOP_MANAGER;
                }

                if (isValid( memberName ) && isValidEmail( memberEmail ) && isValidMobile( memberMobile )
                        && memberType != -1 ){
                    //  Request
                    Map <String, Object> updateMap = new HashMap <>();
                    updateMap.put( "admin_email", memberEmail.getText().toString() );
                    updateMap.put( "admin_mobile", memberMobile.getText().toString() );
                    updateMap.put( "admin_name", memberName.getText().toString() );
                    updateMap.put( "admin_code", String.valueOf( memberType ) );
                    updateMap.put( "admin_photo", "" );
                    updateMap.put( "is_allowed", true );
                    updateMap.put( "admin_address", "" );
                    updateMap.put( "shop_id", shopID );

                    ShopQuery.queryAddShopMember( this, updateMap);
                }
                else{
                    listener.showToast(  "Something went Wrong!" );
                    listener.dismissDialog();
                }
                break;
            case DIALOG_PRODUCT_OPTIONS:
                // Check: How many is checked from list..
                // Then Check if any item selected or not. VARIANT_LIST
                listener.showDialog();
                List<String> variant_list = new ArrayList <>();
                for (int i = 0; i < versionModelList.size(); i++){
                    if (versionModelList.get( i ).isChecked()){
                        variant_list.add( versionModelList.get( i ).getVersionName() );
                    }
                }

                if (variant_list.size()>0){
                    Map<String, Object> updateMap = new HashMap <>();
                    updateMap.put( "variant_list",  variant_list);
                    updateMap.put( "is_show_veg_non_label",  showVegNonOptions.isChecked() );
                    ShopQuery.queryToAssignProductOptions( this, shopID, updateMap );
                }else{
                    listener.showToast( "Please select any option" );
                    listener.dismissDialog();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpdateResponseAddMember(boolean isSuccess) {
        updateResponse( isSuccess );
    }

    @Override
    public void onUpdateProductOptions(boolean isSuccess) {
        if (isSuccess){
            for (int i = 0; i < versionModelList.size(); i++){
                // Set UnChecked!
                versionModelList.get( i ).setChecked( false );
            }
        }
        updateResponse( isSuccess );
    }


    private void updateResponse(boolean isSuccess){
        if (isSuccess){
            listener.showToast(  "Successful" );
            updateDialog.dismiss();
        }else{
            listener.showToast(  "Failed! Something going wrong!" );
        }
        listener.dismissDialog();
    }

    @Override
    public void onResponse(boolean isSuccess) {
        listener.dismissDialog();
        setVersionModelList();
    }

    private void setVersionModelList(){
        if (versionModelList.size() != 0){
            VersionAdaptor adaptor = new VersionAdaptor();
            recyclerViewVersionType.setAdapter( adaptor );
            adaptor.notifyDataSetChanged();
        }
        else{
            listener.showDialog();
            ShopQuery.getProductVariant( this );
        }
    }
    private boolean isValidMobile( EditText ref){
        if (!isValid( ref )){
            return false;
        }else if (ref.getText().toString().length() != 10){
            ref.setError( "Wrong Mobile!" );
            return false;
        }else{
            return true;
        }
    }

    // Version Adaptor...
    private class VersionAdaptor extends RecyclerView.Adapter<VersionAdaptor.ViewHolder>{
        public VersionAdaptor() {
        }

        @NonNull
        @Override
        public VersionAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.layout_text_item, parent, false );
            return new ViewHolder( view );
        }

        @Override
        public void onBindViewHolder(@NonNull VersionAdaptor.ViewHolder holder, int position) {
            holder.setData( position );
        }

        @Override
        public int getItemCount() {
            return versionModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;
            private ImageView imageViewCheck;
            public ViewHolder(@NonNull View itemView) {
                super( itemView );
                textView = itemView.findViewById( R.id.text_view_tag );
                imageViewCheck = itemView.findViewById( R.id.iv_remove_tag_btn );
            }

            private void setData(final int position){
                textView.setText( versionModelList.get( position ).getVersionName() );
                setImageViewCheck( versionModelList.get( position ).isChecked() );

                itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isChecked = versionModelList.get( position ).isChecked();
                        versionModelList.get( position ).setChecked( !isChecked );
                        setImageViewCheck( !isChecked );
                        VersionAdaptor.this.notifyItemChanged( position );
                    }
                } );
            }

            private void setImageViewCheck( boolean isChecked){
                if ( isChecked ){
                    imageViewCheck.setVisibility( View.VISIBLE );
                }else{
                    imageViewCheck.setVisibility( View.GONE );
                }
            }
        }
    }


}
