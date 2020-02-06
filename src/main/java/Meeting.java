import java.util.ArrayList;

public class Meeting {
    String adress;
    String date;
    ArrayList<Guest> GuestList = new ArrayList<>();

    public Meeting(String rdvAdress, String date, String userName, String userAdress){
        this.adress = rdvAdress;
        this.date = date;
        addUser(userName, userAdress);
    }

    public void addUser(String name, String adress) {
        GuestList.add(new Guest(name,adress));
    }

    public String getAllGuest() {
        String Jsonlist = "\"meetings\": [";
        for (int i = 0; i < GuestList.size(); i++){
            Jsonlist += GuestList.get(i);
        }
        Jsonlist += "]";
        return Jsonlist;
    }

}
