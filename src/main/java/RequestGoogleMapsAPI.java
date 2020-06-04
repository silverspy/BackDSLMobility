import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestGoogleMapsAPI {

    private static String requestMaps(String request) {
        String key = "AIzaSyCZ_dx0VgOs940ze9-SUUltgSfH_sk6kzY";
        String targetURL = "https://maps.googleapis.com/maps/api/directions/json?";
        String urlParameters = targetURL +
            "language=fr&" +
            request +
            "&key=" + key;

        try {
            String readLine = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(((HttpURLConnection) (new URL(urlParameters)).openConnection()).getInputStream(), Charset.forName("UTF-8")));
            StringBuffer response = new StringBuffer();
            while ((readLine = reader.readLine()) != null) {

                response.append(readLine);

            }

            reader.close();
            return response.toString();

        } catch (Exception e) {
            System.out.println(e);
        }

        return "error";
    }

    public static String getItiniraire(String departure, String arrival, String date){
        date = date + ":00GMT+02:00";
        Date dNow = new Date( );
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'+'HH:mm:ssz");
            dNow = formatter.parse(date);
        } catch (Exception e){
            System.out.println(e);
        }
        return requestMaps("origin=" + departure + "&destination=" + arrival + "&departure_time=" + dNow.getTime());

    }
}

