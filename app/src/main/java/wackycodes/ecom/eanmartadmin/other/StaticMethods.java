package wackycodes.ecom.eanmartadmin.other;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class StaticMethods {

    public static String getRandomNumAccordingToDate(){
        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());

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
