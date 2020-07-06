package wackycodes.ecom.eanmartadmin.other;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StaticMethods {

    public static String getRandomNumAccordingToDate(){
        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());

        String crrDateDay = simpleDateFormat.format(new Date());

        return crrDateDay;
    }

}
