package ean.ecom.eanmartadmin.addnewitem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.cityareacode.AreaCodeCityModel;
import ean.ecom.eanmartadmin.cityareacode.SelectAreaCityAdaptor;
import ean.ecom.eanmartadmin.database.DBQuery;
import ean.ecom.eanmartadmin.other.DialogsClass;
import ean.ecom.eanmartadmin.mainpage.homesection.BannerAndCatModel;
import ean.ecom.eanmartadmin.mainpage.homesection.HomeListModel;
import ean.ecom.eanmartadmin.other.StaticMethods;

import static ean.ecom.eanmartadmin.MainActivity.selectAreaCityAdaptor;
import static ean.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanmartadmin.database.DBQuery.homePageList;
import static ean.ecom.eanmartadmin.other.StaticMethods.getRandomShopId;
import static ean.ecom.eanmartadmin.other.StaticValues.CATEGORY_ITEMS_LAYOUT_CONTAINER;
import static ean.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_NAME;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_NON_VEG;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_NO_SHOW;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_VEG;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_VEG_NON;

public class AddNewShopActivity extends AppCompatActivity {

    private TextView currentCity;
    private AutoCompleteTextView shopAreaCode;
    private Spinner categoryIDSpinner;
    private EditText shopCategoryText;
    private TextView shopID;
    private ImageButton generateShopIDBtn;
    private EditText shopName;
    private Spinner shopVegNonTypeSpinner;

    private Button cancelBtn;
    private Button addBtn;
    private TextView backBtn;

    private Dialog dialog;

    List<String > categoryIDList = new ArrayList <>();
    List<String > categoryNameList = new ArrayList <>();
    private int index = 0;
    private String CategoryID;
    private int shopVegNonType = 0;
    private boolean isValidShopID = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new_shop );
        dialog = DialogsClass.getDialog( this );

        currentCity = findViewById( R.id.current_city_text );
        shopAreaCode = findViewById( R.id.shop_area_code_auto_text );
        categoryIDSpinner = findViewById( R.id.shop_category_spinner );
        shopCategoryText = findViewById( R.id.shop_category_edit_text );
        shopID = findViewById( R.id.shop_id_text );
        generateShopIDBtn = findViewById( R.id.shop_id_generate_img_btn );
        shopName = findViewById( R.id.shop_name_edit_text );
        shopVegNonTypeSpinner = findViewById( R.id.shop_veg_non_spinner );
        cancelBtn = findViewById( R.id.shop_cancel_btn );
        addBtn = findViewById( R.id.shop_add_btn );
        backBtn = findViewById( R.id.back_text );

        currentCity.setText( "( "+ CURRENT_CITY_NAME + " )" );
        shopID.setText( getRandomShopId( this ) );

        // Add Area Code...
        addShopAreaCode();

        // Select Category...
        for (HomeListModel homeListModel : homePageList){
           if ( homeListModel.getLayoutType() == CATEGORY_ITEMS_LAYOUT_CONTAINER){
//               index = homePageList.indexOf( homeListModel ); // One Way...
               break;
           }else{
               index = index + 1; // Another Way To reduce taking time...
           }
        }
        selectShopCategory();

        // Select Veg Non
        selectShopVegNoNType();

        // Generate New Shop ID
        generateShopIDBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopID.setText( getRandomShopId( AddNewShopActivity.this ) );
            }
        } );

        // Button Action...
        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
        addBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBtn.setEnabled( false );
                dialog.show();
                if (isValidDetail()){
                    queryToAddShop();
                }else{
                    addBtn.setEnabled( true );
                    dialog.dismiss();
                }
            }
        } );
        backBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

        // Check queryToCheckShopID
        shopID.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryToCheckShopID( s.toString().trim() );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

    }

    private String shopAreaName;
    private String shopAreaPinCode = "00";
//    private String shopAreaCityCode;
    private String shopAreaCityName;

    private void addShopAreaCode(){
        ArrayList<AreaCodeCityModel> areaCodeCityModelArrayList = new ArrayList <>();
        areaCodeCityModelArrayList.addAll( DBQuery.areaCodeCityModelList );
        selectAreaCityAdaptor =
                new SelectAreaCityAdaptor( AddNewShopActivity.this, R.layout.select_area_list_item, areaCodeCityModelArrayList);

        shopAreaCode.setThreshold(1);
        shopAreaCode.setAdapter(selectAreaCityAdaptor);

        // handle click event and set desc on textView
        shopAreaCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AreaCodeCityModel areaCodeCityModel = (AreaCodeCityModel) adapterView.getItemAtPosition(i);
                shopAreaCode.setText( areaCodeCityModel.getAreaCode() );
                shopAreaPinCode = areaCodeCityModel.getAreaCode();
                shopAreaName = areaCodeCityModel.getAreaName();
//                shopAreaCityCode = areaCodeCityModel.getCityCode();
                shopAreaCityName = areaCodeCityModel.getCityName();
            }
        });
    }

    private void selectShopCategory(){

        for (BannerAndCatModel bannerAndCatModel : homePageList.get( index ).getBannerAndCatModelList()){
            categoryIDList.add( bannerAndCatModel.getClickID() );
            categoryNameList.add( bannerAndCatModel.getName() );
        }

        ArrayAdapter <String> dataAdapter = new ArrayAdapter <String>(this,
                android.R.layout.simple_spinner_item, categoryNameList );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryIDSpinner.setAdapter(dataAdapter);
        categoryIDSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                CategoryID = categoryIDList.get( position );
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//
            }
        } );

    }

    private void selectShopVegNoNType(){
        // Select Banner Type...
        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray( R.array.shop_veg_non_type ));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopVegNonTypeSpinner.setAdapter(dataAdapter);
        shopVegNonTypeSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {

                if (position != 0){ // Shop Type
                    switch (position){
                        case 1: // Pure Veg
                            shopVegNonType = SHOP_TYPE_VEG;
                            break;
                        case 2: // Non Veg
                            shopVegNonType = SHOP_TYPE_NON_VEG;
                            break;
                        case 3: // Veg + Non Veg
                            shopVegNonType = SHOP_TYPE_VEG_NON;
                            break;
                        case 4: // Others
                            shopVegNonType = SHOP_TYPE_NO_SHOW;
                            break;
                        default:
                            break;
                    }

                }else{
                    shopVegNonType = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//
            }
        } );
    }

    private boolean isValidDetail(){
        if (TextUtils.isEmpty(shopAreaCode.getText().toString() )){
            shopAreaCode.setError( "Required!" );
            return false;
        }else if (shopAreaCode.getText().toString().trim().length() != 6 ){
            shopAreaCode.setError( "Wrong pin Code!" );
            return false;
        }else if ( !shopAreaPinCode.equals( shopAreaCode.getText().toString() )){
            shopAreaCode.setError( "Not Exist On Database!" );
            return false;
        }else if(CategoryID == null){
            DialogsClass.alertDialog( this, null, "Please Select Category!" ).show();
            return false;
        }else if (TextUtils.isEmpty( shopCategoryText.getText().toString() )){
            shopCategoryText.setError( "Please Enter Shop Category!" );
            return false;
        }else if (TextUtils.isEmpty( shopName.getText().toString() )){
            shopName.setError( "Enter Shop Name!" );
            return false;
        }else if (shopVegNonType == 0){
            DialogsClass.alertDialog( this, null, "Please Select Shop Type!" ).show();
            return false;
        }else if(!isValidShopID){
            Toast.makeText( this, "Please Refresh shop ID!", Toast.LENGTH_SHORT ).show();
            return false;
        }else{
            return true;
        }
    }

    private void queryToAddShop(){
        String shopId =  shopID.getText().toString().trim();
        String sAreaCode = shopAreaCode.getText().toString().trim();
        String sName = shopName.getText().toString();
        String sCatName = shopCategoryText.getText().toString();

        String tagString = CategoryID + " " + sCatName + " " + shopAreaName + " " + shopId + " " + sName + " " + shopVegNonType ;
        String[] categories = {CategoryID};
        String[] tags = tagString.toLowerCase().split( " " );

        // get Current Time..
        String addShopDateTime = StaticMethods.getCurrentDateAndTime();

//        ArrayList<String> tagList = new ArrayList <>();
//        tagList.addAll( Arrays.asList( tags ) );

        Map<String, Object> shopMap = new HashMap <>();

        shopMap.put( "available_service", false );
        shopMap.put( "is_open", false );
        shopMap.put( "shop_address", " " );
        shopMap.put( "shop_area_code", sAreaCode );
        shopMap.put( "shop_area_name", shopAreaName );
        shopMap.put( "shop_cat_main", CategoryID );
        shopMap.put( "shop_categories",  Arrays.asList( categories ) );
        shopMap.put( "shop_category_name", sCatName );
        shopMap.put( "shop_city_name", shopAreaCityName );
        shopMap.put( "shop_close_msg", "Shop is temporary close!" );
        shopMap.put( "shop_id",shopId );
        shopMap.put( "shop_landmark", "" );
        shopMap.put( "shop_logo", "" );
        shopMap.put( "shop_map_latitude", "" );
        shopMap.put( "shop_map_longitude", "" );
        shopMap.put( "shop_name",sName );
        shopMap.put( "shop_owner_address", "" );
        shopMap.put( "shop_owner_name", "" );
        shopMap.put( "shop_owner_mobile", "" );
        shopMap.put( "shop_owner_email", "" );
        shopMap.put( "shop_type", String.valueOf( shopVegNonType ) );
        shopMap.put( "shop_veg_non_type", String.valueOf( shopVegNonType ) );
        shopMap.put( "shop_image", "" );
        shopMap.put( "shop_rating", "" );
        shopMap.put( "shop_add_on_date", addShopDateTime );
        shopMap.put( "tags", Arrays.asList( tags ) );

        Map<String, Object> shopHomeMap = new HashMap <>();

        shopHomeMap.put( "available_service", false );
        shopHomeMap.put( "is_open", false );
        shopHomeMap.put( "tags", Arrays.asList( tags ) );
        shopHomeMap.put( "shop_id", shopId );
        shopHomeMap.put( "shop_image", "" );
        shopHomeMap.put( "shop_name", sName );
        shopHomeMap.put( "shop_logo", "" );
        shopHomeMap.put( "shop_rating", "" );
        shopHomeMap.put( "shop_add_on_date", addShopDateTime );
        shopHomeMap.put( "shop_veg_non_type", String.valueOf( shopVegNonType ) );
        shopHomeMap.put( "shop_categories", Arrays.asList( categories ));
        shopHomeMap.put( "shop_category_name", sCatName );

        addOnDataBase( dialog, shopId, shopMap, shopHomeMap );

    }

    private void queryToCheckShopID(String shopId){

        firebaseFirestore.collection( "SHOPS" ).document( shopId ).addSnapshotListener( new EventListener <DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if( documentSnapshot != null){
                    if (documentSnapshot.exists()){
                        shopID.setError( "Shop ID is already Registered!" );
                        isValidShopID = false;
                    }else {
                        isValidShopID = true;
                    }
                }else{
                    isValidShopID = true;
                }
            }
        } );

    }

    private void addOnDataBase(@NonNull final Dialog dialog, @NonNull final String shopId
            , @NonNull Map<String, Object> shopMap, @NonNull final Map<String, Object> shopHomeMap ){
        firebaseFirestore.collection( "SHOPS" ).document( shopId )
                .set( shopMap ).addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()){
                    DBQuery.getCollectionRef( "SHOPS" ).document( shopId )
                            .set( shopHomeMap ).addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task1) {
                            if (task1.isSuccessful()){
                                Toast.makeText( AddNewShopActivity.this, "Successfully added!", Toast.LENGTH_SHORT ).show();
                                dialog.dismiss();
                                finish();
                            }else{
                                Toast.makeText( AddNewShopActivity.this, "Failed!", Toast.LENGTH_SHORT ).show();
                                dialog.dismiss();
                            }
                        }
                    } );
                }else{
                    Toast.makeText( AddNewShopActivity.this, "Failed! Something Went Wrong", Toast.LENGTH_SHORT ).show();
                    dialog.dismiss();
                }
            }
        } );

    }

    /**
     * Main : SHOPS>ShopId>
     *
     * 1. available_service : true
     * 2. is_open : true
     * 3. shop_address : "Shop - full - address "
  >  * 4. shop_area_code : "462021" (String)
     * 5. shop_area_name : "Ratnagiri"
     * 6. shop_cat_main : "ELECTRONICS"
     * 7. shop_categories : array ( "ELECTRONICS", "MOBILES")
  >  * 8. shop_category_name : "Name of Main Category"
     * 9. shop_city_name : "Bhopal"
     * 10. shop_close_msg : "Whatever message shop admin want to show;"
  >  * 11. shop_id : "462099009"
     * 12. shop_landmark : " Anything"
     * 13. shop_logo : "Logo Link"
     * 14. shop_map_latitude :
     * 15. shop_map_longitude :
  >  * 16. shop_name : " AN Electronics :"
     * 17. shop_owner_address : "jadsb :"
     * 18. shop_owner_name : "Name:"
     * 19. shop_owner_mobile : '20329092390"
     * 20. shop_owner_email : "alksnd!"
     * 21. shop_type : "1" (Not required)
  >  * 22. shop_veg_non_type : "2"
     *
     * 23. shop_image : " Shop image Link "
     * 24. shop_rating : "4.3"
     * 25. tags : Array (l, aos,_)
     *
     *
     * ---------------------------
     * BHOPAL > SHOPS > shop_Id:
     *
     * 1. shop_id : "2939320"
     * 2. shop_image : "Link:"
     * 2. shop_name : 'name"
     * 4. shop_logo : "link :"
     * 5. shop_rating : "4.2"
     * 6. shop_veg_non_type :"2"
     * 7. tags : Array (l, aos,_)
     *
     * 8. shop_categories : array (ls. Lsa0)
     * 9. shop_category_name : "kugs "
     *
     *
     */

}
