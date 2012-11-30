/**
 * Advertise - Package: syam.advertise.announce
 * Created: 2012/11/30 8:54:55
 */
package syam.advertise.announce;

import java.util.logging.Logger;

import syam.advertise.Advertise;

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

    public AdvertiseManager (final Advertise plugin){
        this.plugin = plugin;
    }

    public String getNextMessage(){
        return "&cSAKURA ADVERTISEMENT! Hello, &b%player%&c! (testing!)";
    }
}
