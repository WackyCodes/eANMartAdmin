package ean.ecom.eanmartadmin.other;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import static ean.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;

public class StaticMethods {

    public static void gotoURL(Context context, String urlLink){
        Uri uri = Uri.parse( urlLink );
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity( intent );
    }

    public static String getRandomNumAccordingToDate(){
        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());

        String crrDateDay = simpleDateFormat.format(new Date());

        return crrDateDay;
    }

    public static String getCurrentDateAndTime(){
//        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        String crrDateDay = simpleDateFormat.format(new Date());
        return crrDateDay;
    }

    public static String getTwoDigitRandom(){
        Random random = new Random();
        // Generate random integers in range 0 to 9999
        int rand_int1 = 0;
        String randNum = "";
        do {
            rand_int1 = random.nextInt(1000);
        }while ( rand_int1 <= 0 );

        if (rand_int1<99){
            rand_int1 = rand_int1*100;
        }

        randNum = String.valueOf( rand_int1 ).substring( 0, 2 );

        return randNum;
    }

    public static String getFiveDigitRandom(){
        Random random = new Random();
        // Generate random integers in range 0 to 9999
        int rand_int1 = 0;
        String randNum = "";
        do {
            rand_int1 = random.nextInt(100000);
        }while ( rand_int1 <= 0 );

        if (rand_int1<999){
            rand_int1 = rand_int1*1000;
        }else if (rand_int1<9999){
            rand_int1 = rand_int1*100;
        }else if (rand_int1<=99999){
            rand_int1 = rand_int1*10;
        }
            randNum = String.valueOf( rand_int1 ).substring( 0, 5 );

        return randNum;
    }

    public static String getRandomShopId(Context context){
        String shopId = "";
        if ( CURRENT_CITY_CODE.equals( "BHOPAL" ) ){
            shopId = "4620" + getFiveDigitRandom();
            return shopId;
        }else if ( CURRENT_CITY_CODE.equals( "INDORE" ) ){
            shopId = "4520" + getFiveDigitRandom();
            return shopId;
        }else{
            Toast.makeText( context, "City not registered yet!", Toast.LENGTH_SHORT ).show();
            return shopId;
        }
    }

    public static void showToast( Context context, String msg){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }

    public static boolean isValidEmail( EditText wReference ){
        String wEmail = wReference.getText().toString().trim();
        String emailRegex =
                "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        boolean bool = pat.matcher(wEmail).matches();

        if (TextUtils.isEmpty( wEmail )) {
            wReference.setError( "Please Enter Email! " );
            return false;
        } else if (!bool){
            wReference.setError( "Please Enter Valid Email! " );
            return false;
        }

        return true;
    }

    public static boolean isValid(EditText ref){
        if (TextUtils.isEmpty( ref.getText().toString() )){
            ref.setError( "Required..!" );
            return false;
        }else{
            return true;
        }
    }

    /*
        // TODO : List...
        1. Add Shop View Page
            - Add Shop Properties
            - permission denied
            - Admin restrict
            - change owner admin

        2. Add New Admin
            - Type...
            - Permission..

        3. Total Shop List with city name
        4. Search Any shop
        5. Add New City
           - Add Home Page with
           - add category layout...
        6. Add New Shop
            - Using new Activity
            - add shop details

        7. Add New Category
            - Add Shop container with..

        8. Add Accept Request of Shop...


     */

}
