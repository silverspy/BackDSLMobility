import spark.Filter;

import java.util.ArrayList;

import static spark.Spark.*;

public class Main {


    public static void main(String[] args) {

        ArrayList<Meeting> meetings = new ArrayList<>();
        Planning planning = new Planning();

        // Start embedded server at this port
        port(8080);


        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
        });

        /*
        /createRDV + data {

            name : nom_user
            adress: adress_user
            dateRDV:
            adressRDV:
            api : google / tisseo

        } => creer le rdv et renvoie les instructions
         */
        post("/createRDV", (request, response) -> {
            String name = request.queryParams("name").replace(" ", "+");
            String adress = request.queryParams("adress").replace(" ", "+");
            String adressRDV = request.queryParams("adressRDV").replace(" ", "+");
            String dateRDV = request.queryParams("dateRDV").replace(" ", "+");
            String API = request.queryParams("api").replace(" ", "+");
            meetings.add(new Meeting(adressRDV, dateRDV,"" + meetings.size() + 1 , name, adress));
            response.status(200);
            response.type("application/json");
            String APIresponse = RequestAPI.getItiniraire(adress, adressRDV, dateRDV, API);
            String Response = "{\"APIresponse\":" + APIresponse + ",\"APItype\":\"" + API + "\"" + "}";
            return Response;
        });

        /*
        /listRDV  => donne la liste des rdv
         */
        post("/listRDV", (request, response) -> {
            response.status(200);
            response.type("application/json");

            String Jsonlist = "{\"meetings\": [";
            for (int i = 0; i < meetings.size(); i++){
                if(i < meetings.size() - 1) {
                    Jsonlist += meetings.get(i).toJson() + ",";
                } else {
                    Jsonlist += meetings.get(i).toJson();
                }
            }
            Jsonlist += "]}";
            return Jsonlist;
        });

        /*
        /joinRDV  + data{
        name:
        adress:
        idRDV:
        api: google / tisseo
        } => renvoie le rendez vous et ajoute a la liste des participants
         */
        post("/joinRDV", (request, response) -> {
            String name = request.queryParams("name").replace(" ", "+");
            String adress = request.queryParams("adress").replace(" ", "+");
            String idRDV = request.queryParams("idRDV").replace(" ", "+");
            String API = request.queryParams("api").replace(" ", "+");
            //String moyen = request.queryParams("moyen").replace(" ", "+");
            response.status(200);
            response.type("application/json");
            Meeting m = meetings.get(Integer.parseInt(idRDV) - 1);
            m.addUser(name,adress);
            String APIresponse = RequestAPI.getItiniraire(adress, m.adress, m.date, API);
            String Response = "{\"APIresponse\":" + APIresponse + ",\"APItype\":\"" + API + "\"" + "}";
            return Response;
        });

        /*
            /joinRDVinPlanning  + data{
            name:
            adress:
            idRDV:
            api: google / tisseo
            } => renvoie le rendez vous et ajoute a la liste des participants
         */
        post("/joinRDVinPlanning", (request, response) -> {
            String name = request.queryParams("name").replace(" ", "+");
            String adress = request.queryParams("adress").replace(" ", "+");
            String idRDV = request.queryParams("idRDV").replace(" ", "+");
            String API = request.queryParams("api").replace(" ", "+");
            //String moyen = request.queryParams("moyen").replace(" ", "+");
            if (planning.joinMeeting(idRDV,name,adress)){
                System.out.println("je rentre");
                response.status(200);
                response.type("application/json");
                Meeting m = planning.getMeeting(idRDV);
                String APIresponse = RequestAPI.getItiniraire(adress, m.adress, m.date, API);
                String Response = "{\"APIresponse\":" + APIresponse + ",\"APItype\":\"" + API + "\"" + "}";
                return Response;
            } else {
                response.status(420);
                response.type("application/json");
                return "Meeting full or inexistant";
            }

        });

        /*
        /createRDVinPlanning + data {

            name : nom_user
            adress: adress_user
            dateRDV:
            adressRDV:
            api : google / tisseo

        } => creer le rdv dans le planning et renvoie les instructions
        */
        post("/createRDVinPlanning", (request, response) -> {
            String name = request.queryParams("name").replace(" ", "+");
            String adress = request.queryParams("adress").replace(" ", "+");
            String adressRDV = request.queryParams("adressRDV").replace(" ", "+");
            String dateRDV = request.queryParams("dateRDV").replace(" ", "+");
            String API = request.queryParams("api").replace(" ", "+");
            if(planning.addMeeting(adressRDV, dateRDV, name, adress)) {
                response.status(200);
                response.type("application/json");
                String APIresponse = RequestAPI.getItiniraire(adress, adressRDV, dateRDV, API);
                String Response = "{\"APIresponse\":" + APIresponse + ",\"APItype\":\"" + API + "\"" + "}";
                return Response;
            } else {
                response.status(420);
                response.type("application/json");
                return "Already exist or not in the time slot";
            }

        });

        /*
        /listRDVinPlanning => liste les rendez vous du planning
        */
        post("/listRDVinPlanning", (request, response) -> {
            response.status(200);
            response.type("application/json");
            return planning.JsonListRDV();
        });

        /*
        /listDispo => liste les horaires disponible
        */
        post("/listDispo", (request, response) -> {
            response.status(200);
            response.type("application/json");
            return planning.getDispo();
        });

        /*
        /getHoraire => Liste des horaire
        */
        post("/getHoraire", (request, response) -> {
            response.status(200);
            response.type("application/json");
            return planning.getJsonHoraire();
        });

        /*
            /getHoraireRDV => Liste les horarire des RDV disponible
        */
        post("/getHoraireRDV", (request, response) -> {
            response.status(200);
            response.type("application/json");
            return planning.JsonListHoraireRDV();
        });


        /*
        /test => return success
         */
        get("/test", (request, response) -> {
            response.status(200);
            return "ok";
        });

        post("/test", (request, response) -> {
            response.status(200);
            return "ok";
        });
    }

}