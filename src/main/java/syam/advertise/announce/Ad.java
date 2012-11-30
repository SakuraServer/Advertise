/**
 * Advertise - Package: syam.advertise.announce
 * Created: 2012/11/30 10:33:02
 */
package syam.advertise.announce;

import java.util.ArrayList;
import java.util.HashMap;

import syam.advertise.Advertise;
import syam.advertise.database.Database;

/**
 * Ad (Ad.java)
 * @author syam(syamn)
 */
public class Ad {
    Advertise plugin = Advertise.getInstance();
    Database db = Advertise.getDatabases();

    private int dataID = 0;
    private int playerID = 0;
    private String playerName;
    private int status = 0;
    private Long registered;
    private Long expired;
    private String text;
    private int view_count = 0;
    private int view_players = 0;

    public Ad(final int dataID){
        HashMap<Integer, ArrayList<String>> records = db.read("SELECT " + db.dataTable + ".player_id, `player_name`, " + db.dataTable + ".`status`, `registered`, `expired`, `text`, `view_count`, `view_players`" +
                " FROM " + db.dataTable + " JOIN " + db.userTable + " ON " + db.dataTable + ".player_id = " + db.userTable + ".player_id WHERE data_id = ?", dataID);
        if(records == null || records.size() <= 0){
            throw new IllegalArgumentException("Record not found by advertise id " + dataID);
        }

        ArrayList<String> record = records.get(1);

        this.dataID = dataID;
        this.playerID = Integer.parseInt(record.get(0));
        this.playerName = record.get(1);
        this.status = Integer.parseInt(record.get(2));
        this.registered = Long.parseLong(record.get(3));
        this.expired = Long.parseLong(record.get(4));
        this.text = record.get(5);
        this.view_count = Integer.parseInt(record.get(6));
        this.view_players = Integer.parseInt(record.get(7));
    }

    public void save(){
        if (dataID <= 0 || playerID <= 0){
            throw new IllegalStateException("Null dataID or playerID, can't save data!");
        }

        db.write("REPLACE INTO " + db.dataTable + " VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?)"
                , this.dataID, playerID, this.status, this.registered.intValue(), this.expired.intValue(), this.text, this.view_count, this.view_players);
    }

    public int getDataID(){
        return this.dataID;
    }
    public int getPlayerID(){
        return this.playerID;
    }
    public String getPlayerName(){
        return this.playerName;
    }
    public int getStatus(){
        return this.status;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public Long getRegistered(){
        return this.registered;
    }
    public Long getExpired(){
        return this.expired;
    }
    public void setExpired(Long expired){
        this.expired = expired;
    }
    public String getText(){
        return this.text;
    }
    public void setText(String text){
        this.text = text;
    }
    public int getViewCount(){
        return this.view_count;
    }
    public void addViewCount(int count){
        this.view_count += count;
    }
    public int getViewPlayers(){
        return this.view_players;
    }
    public void addViewPlayers(int count){
        this.view_players += count;
    }
}
