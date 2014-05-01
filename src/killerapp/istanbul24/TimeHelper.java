package killerapp.istanbul24;

import android.text.format.Time;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by jack on 01/05/14.
 */
public class TimeHelper {

    public static String getGreeting() {

        int hour = getHour();

        if(hour > 0 &&  hour < 12) {
             return "Goodmorning";
        } else if(hour > 12 && hour < 18) {
             return "Good afternoon";
        } else {
             return "Good evening";
        }

    }

    private static int getHour() {
        Time now = new Time(Time.getCurrentTimezone());
        now.setToNow();
        return now.hour;
    }

}
