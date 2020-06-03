public class RequestAPI {

    public static String getItiniraire(String departure, String arrival, String date, String api) {
        System.out.println(api);
        if (api.equals("google")) {
            return RequestGoogleMapsAPI.getItiniraire(departure,arrival,date);
        } else if (api.equals("tisseo")) {
            return RequestTisseoAPI.getItiniraire(departure,arrival,date);
        } else {
            return "error wrong API";
        }
    }

}

