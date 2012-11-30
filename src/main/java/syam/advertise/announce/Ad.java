/**
 * Advertise - Package: syam.advertise.announce
 * Created: 2012/11/30 10:33:02
 */
package syam.advertise.announce;

import syam.advertise.Advertise;
import syam.advertise.database.Database;
import syam.advertise.util.Util;

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

    }

    public Ad(final String playerName, final int days, final String text){

    }

    public void save(){
        if (dataID <= 0 || playerID <= 0){
            throw new IllegalStateException("Null dataID or playerID, can't save data!");
        }

        db.write("REPLACE INTO " + db.dataTable + " VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?)"
                , this.dataID, playerID, this.status, this.registered.intValue(), this.expired.intValue(), this.text, this.view_count, this.view_players);
    }


}
