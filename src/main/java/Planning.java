import javax.naming.Name;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;

public class Planning {
    int hstart;
    int hend;
    int nbPers;
    HashMap<String, Meeting> Listerdv = new HashMap<>();


    public Planning(){
        hstart = 9;
        hend = 18;
        nbPers = 2;
    }

    public boolean addMeeting(String rdvAdress, String date, String userName, String userAdress){
        String day = date.split("\\+")[0];
        int heurerdv  = Integer.parseInt(date.split("\\+")[1].split(":")[0]);
        if(heurerdv > hstart && heurerdv < hend) {
            if (Listerdv.containsKey(day + "+" + heurerdv)){
                return false;
            } else {
                Listerdv.put(date , new Meeting(rdvAdress, date, date, userName, userAdress));
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean joinMeeting(String id, String name, String adress){
        System.out.println(Listerdv.containsKey(id) + " : " + id);
        if (Listerdv.containsKey(id)){
            if(Listerdv.get(id).nbUser() < nbPers){
                Listerdv.get(id).addUser(name,adress);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Meeting getMeeting(String id){
        return Listerdv.get(id);
    }

    public String JsonListRDV(){
        String ListJson = "{\"meetings\": [";
        for (String name: Listerdv.keySet()){
            ListJson += (Listerdv.get(name).toJson() +",");
        }
        return new StringBuilder(ListJson).deleteCharAt(ListJson.length()-1).toString() + "]}";
    }

    public String getDispo(){

        Calendar calendar = Calendar.getInstance();

        //définir le format de la date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");
        String ListJson = "{\"dispo\": [";

        for (int i = 0; i < 7; i++) {
            String stringdate = sdf.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
            for (int j = hstart; j < hend; j++){
                String idRDV = stringdate + "+" + j + ":00";
                if(!Listerdv.containsKey(idRDV)) {
                    ListJson += "\"" + idRDV +  "\",";
                }
            }
        }
        return new StringBuilder(ListJson).deleteCharAt(ListJson.length()-1).toString() + "]}";
    }


}
