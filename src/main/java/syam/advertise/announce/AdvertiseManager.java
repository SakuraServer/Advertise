/**
 * Advertise - Package: syam.advertise.announce
 * Created: 2012/11/30 8:54:55
 */
package syam.advertise.announce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import syam.advertise.Advertise;
import syam.advertise.database.Database;
import syam.advertise.util.Util;

/**
 * AdvertiseManager (AdvertiseManager.java)
 * @author syam(syamn)
 */
public class AdvertiseManager {
    // Logger
    private static final Logger log = Advertise.log;
    private static final String logPrefix = Advertise.logPrefix;
    private static final String msgPrefix = Advertise.msgPrefix;
    private final Advertise plugin;

    // taskID
    private int taskID = -1;
    Database db = Advertise.getDatabases();

    // lastMessageID
    private int lastID = 0;

    public AdvertiseManager (final Advertise plugin){
        this.plugin = plugin;
    }

    public int getNextID(){
        HashMap<Integer, ArrayList<String>> records = db.read("SELECT `data_id` FROM " + db.dataTable + " WHERE `expired` > ? AND `status` = 0", Util.getCurrentUnixSec().intValue());
        if (records == null || records.size() <= 0) {
            return -1;
        }

        boolean found = false;
        int data_id = -1;
        for(ArrayList<String> record : records.values()){
            data_id = Integer.parseInt(record.get(0));
            if (data_id > lastID){
                lastID = data_id;
                found = true;
                break;
            }
        }
        if (!found){
            // not 0, first index: 1
            data_id = Integer.parseInt(records.get(1).get(0));
            lastID = 0;
        }

        return data_id;
    }

    /**
     * 広告を追加
     * @param playerName
     * @param days
     * @param text
     */
    public void addAdvertise(final String playerName, final int days, final String text){
        addAdvertise(getUserID(playerName, true), days, text);
    }
    public void addAdvertise(final int playerID, final int days, final String text){
        Long registered = Util.getCurrentUnixSec();
        Long expired = registered + (days * 86400);

        db.write("INSERT INTO " + db.dataTable + " (player_id, registered, expired, `text`) VALUES (?, ?, ?, ?)",
                playerID, registered.intValue(), expired.intValue(), text);
    }

    /**
     * 広告を削除
     * @param adv_id
     */
    public void removeAdvertise(final int adv_id, final boolean byStaff){
        final int status = (!byStaff) ? 1 : 2;
        db.write("UPDATE " + db.dataTable + " SET `status` = ? WHERE `data_id` = ?", status, adv_id);
    }

    /**
     * Get player_id from Database
     * @param playerName
     * @param addNew
     * @return
     */
    public int getUserID(final String playerName, final boolean addNew){
        // プレイヤーID(DB割り当て)を読み出す
        int playerID = db.getInt("SELECT player_id FROM " + db.userTable + " WHERE player_name = ?", playerName);

        // 存在確認
        if (playerID <= 0){
            // not found
            if (addNew) {
                // adding
                db.write("INSERT INTO " + db.userTable + " (player_name) VALUES (?)", playerName); // usersテーブル
                playerID = db.getInt("SELECT player_id FROM " + db.userTable + " WHERE player_name = ?", playerName);
            }else{
                return 0;
            }
        }

        return playerID;
    }
}
