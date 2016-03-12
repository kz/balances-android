package in.iamkelv.balances.helpers;

public class Helpers {
    public static Integer parseHoursFromTime(String time) {
        return Integer.parseInt(time.substring(0, 2));
    }

    public static Integer parseMinutesFromTime(String time) {
        return Integer.parseInt(time.substring(3, 5));
    }

    public static String createTimeString(Integer hours, Integer minutes) {
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes);
    }
}
