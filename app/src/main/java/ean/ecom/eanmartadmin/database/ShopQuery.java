package ean.ecom.eanmartadmin.database;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ean.ecom.eanmartadmin.multisection.aboutshop.ShopHomeActivity;
import ean.ecom.eanmartadmin.multisection.aboutshop.UpdateShopListener;
import ean.ecom.eanmartadmin.multisection.aboutshop.VersionModel;

import static ean.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;

/**
 * Created by Shailendra (WackyCodes) on 06/12/2020 01:06
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class ShopQuery {


    // Update Shop Activate / Deactivate ...

    public static List<VersionModel> versionModelList = new ArrayList <>();

    public static void queryToActivateShop(final Context context, final Dialog dialog, final String shopId, final Boolean activateVal){
        firebaseFirestore.collection( "SHOPS" ).document( shopId )
                .update( "available_service", activateVal )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            DBQuery.getCollectionRef( "SHOPS" ).document( shopId )
                                    .update( "available_service", activateVal )
                                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task <Void> task1) {
                                            if (task1.isSuccessful()){
                                                Toast.makeText( context, "Update Successfully!", Toast.LENGTH_SHORT ).show();
                                                ShopHomeActivity.shopHomeActivityModel.setServiceAvailable( activateVal );
                                            }else{
                                                Toast.makeText( context, "Failed!", Toast.LENGTH_SHORT ).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    } );
                        }else{
                            Toast.makeText( context, "Failed!", Toast.LENGTH_SHORT ).show();
                            dialog.dismiss();
                        }
                    }
                } );
    }

    public static void queryAddShopMember(final UpdateShopListener.UpdateListener listener, Map <String, Object> updateMap){

        String shopId = updateMap.get( "shop_id" ).toString();
        String adminMobile = updateMap.get( "admin_mobile" ).toString();

        firebaseFirestore.collection( "SHOPS" ).document( shopId )
                .collection( "ADMINS" )
                .document( adminMobile ).set( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            listener.onUpdateResponseAddMember( true );
                        }else{
                            listener.onUpdateResponseAddMember( false );
                        }
                    }
                } );

    }

    // get variant list...
    public static void getProductVariant(final UpdateShopListener.LoadListListener listListener){
        firebaseFirestore.collection( "PERMISSION" )
                .document( "SHOP_PRODUCT_VARIANT" )
                .get()
                .addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            versionModelList.clear();
                            List<String> list = (List <String>) task.getResult().get( "variant_list" );

                            for (String s : list){
                                versionModelList.add( new VersionModel( s, false ) );
                            }
                            listListener.onResponse( true );
                        }else{
                            listListener.onResponse( false );
                        }
                    }
                } );
    }


}
