import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;

import java.util.ArrayList;

import static spark.Spark.*;

public class Main {


    public static void main(String[] args) {

        ArrayList<Meeting> meetings = new ArrayList<>();

        // Start embedded server at this port
        port(8080);

        meetings.add(new Meeting("adres1","date1","username","adress"));
        System.out.println(meetings.toString());

        get("/createRDV", (request, response) -> {
            String name = request.queryParams("name");
            String adress = request.queryParams("adress");
            String adressRDV = request.queryParams("adressRDV");
            String dateRDV = request.queryParams("dateRDV");
            meetings.add(new Meeting(adressRDV, dateRDV, name, adress));
            response.status(200);
            response.type("application/json");
            String APIresponse = RequestTisseoAPI.getItiniraire(adress, adressRDV, dateRDV);
            return APIresponse;
        });

        get("/listRDV", (request, response) -> {
            response.status(200);
            response.type("application/json");
            System.out.println(meetings);
            return meetings.toString();
        });

        get("/joinRDV", (request, response) -> {
            String name = request.queryParams("name");
            String adress = request.queryParams("adress");
            String idRDV = request.queryParams("idRDV");
            //String moyen = request.queryParams("moyen");
            response.status(200);
            response.type("application/json");
            Meeting m = meetings.get(Integer.parseInt(idRDV));
            m.addUser(name,adress);
            String APIresponse = RequestTisseoAPI.getItiniraire(adress,m.adress, m.adress);
            return APIresponse;
        });

    }

}