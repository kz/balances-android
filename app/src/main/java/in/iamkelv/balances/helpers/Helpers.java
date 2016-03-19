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

    public static String getErrorTextFromStatusCode(Integer statusCode) {
        if (statusCode == 401) {
            return "Your credentials are invalid. Please check your username/password and try again.";
        } else if (statusCode == 400) {
            return "There is an issue with the app. Please email the app developer.";
        } else if (statusCode == 500) {
            return "An error has occurred with the server. Please try again later.";
        } else if (statusCode == 501) {
            return "WisePay services are currently unavailable. Please try again later.";
        } else {
            return "An unknown error has occurred. Please try again later.";
        }
    }
}
