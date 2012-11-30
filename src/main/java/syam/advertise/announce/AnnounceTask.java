/**
 * Advertise - Package: syam.advertise.announce
 * Created: 2012/11/30 7:53:34
 */
package syam.advertise.announce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.avaje.ebeaninternal.server.autofetch.TunedQueryInfo;

import syam.advertise.Advertise;
import syam.advertise.Perms;
import syam.advertise.database.Database;
import syam.advertise.util.Actions;
import syam.advertise.util.Util;

/**
 * AnnounceTask (AnnounceTask.java)
 * @author syam(syamn)
 */
public class AnnounceTask implements Runnable{
    // Logger
    private static final Logger log = Advertise.log;
    private static final String logPrefix = Advertise.logPrefix;
    private static final String msgPrefix = Advertise.msgPrefix;
    private final Advertise plugin;

    private static Object lock = new Object();
    private static boolean running = false;

    private int forceID = -1;

    public AnnounceTask(final Advertise plugin){
        this.plugin = plugin;
    }
    public AnnounceTask(final Advertise plugin, final int data_ID){
        this.plugin = plugin;
        this.forceID = data_ID;
    }

    public void run(){
        synchronized (lock){
            if (running) return;
            running = true;
        }
        try{
            int data_ID = (forceID <= 0) ? plugin.getManager().getNextID() : forceID;
            if (data_ID <= 0) return;
            announce(data_ID);
        }finally{
            running = false;
        }
    }

    private void announce(int data_ID){
        Ad ad = new Ad(data_ID);
        if (ad.getText() == null || ad.getText().length() <= 0){
            return;
        }

        // default ad color: &3
        String text = plugin.getConfigs().getPrefix() + "&6" + ad.getPlayerName() + "&7: &3" + ad.getText();
        int i = 0;
        for (Player player : Bukkit.getOnlinePlayers()){
            if (!plugin.getConfigs().getUseHidePerm() || !Perms.HIDE_ADVERTISE.has(player)){
                Actions.message(player, text.replace("%player%", player.getName()));
                i++;
            }
        }
        Actions.message(Bukkit.getConsoleSender(), text.replace("%player%", "CONSOLE"));

        // update stats
        if (i > 0){
            ad.addViewCount(1);
            ad.addViewPlayers(i);
            ad.save();
        }
    }
}
