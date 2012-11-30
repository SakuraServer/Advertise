/**
 * Advertise - Package: syam.advertise.announce
 * Created: 2012/11/30 7:53:34
 */
package syam.advertise.announce;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import syam.advertise.Advertise;
import syam.advertise.Perms;
import syam.advertise.util.Actions;

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

    public AnnounceTask(final Advertise plugin){
        this.plugin = plugin;
    }

    public void run(){
        String announce = plugin.getManager().getNextMessage();
        if (announce == null || announce.length() <= 0){
            return;
        }
        announce = plugin.getConfigs().getPrefix() + announce;

        for (Player player : Bukkit.getOnlinePlayers()){
            if (!plugin.getConfigs().getUseHidePerm() || !Perms.HIDE_ADVERTISE.has(player)){
                Actions.message(player, announce.replace("%player%", player.getName()));
            }
        }
        log.info(announce.replace("%player%", "CONSOLE"));
    }
}
