/**
 * Advertise - Package: syam.advertise.database
 * Created: 2012/11/30 9:07:56
 */
package syam.advertise.database;

import syam.advertise.Advertise;

/**
 * MySQLReconnect (MySQLReconnect.java)
 * @author syam(syamn)
 */
public class MySQLReconnect implements Runnable{
    private final Advertise plugin;

    public MySQLReconnect(final Advertise plugin){
        this.plugin = plugin;
    }

    @Override
    public void run(){
        if (!Database.isConnected()){
            Database.connect();
            if (Database.isConnected()){
                // TODO: do stuff..
            }
        }
    }
}
