package TimeJava;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);
        System.out.println(date.getTime());

        TimeZone gmt = TimeZone.getTimeZone("GMT");

        Calendar epoch1900 = Calendar.getInstance(gmt);
        epoch1900.set(1900,Calendar.JANUARY,1,0,0,0);
        long epoch1900ms = epoch1900.getTimeInMillis();

        Calendar epoch1970 = Calendar.getInstance(gmt);
        epoch1970.set(1970,Calendar.JANUARY,1,0,0,0);
        long epoch1970ms = epoch1970.getTimeInMillis();

        System.out.println(epoch1900ms);
        System.out.println(epoch1970ms);

        long diffms = epoch1970ms - epoch1900ms;
        long diff = diffms/1000;
        System.out.println(diff);
        
    }
}
