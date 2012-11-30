/**
 * Advertise - Package: syam.advertise.announce
 * Created: 2012/11/30 7:55:30
 */
package syam.advertise.announce;

import java.util.logging.Logger;

import org.bukkit.command.CommandSender;

import syam.advertise.Advertise;
import syam.advertise.util.Actions;

/**
 * TaskManager (TaskManager.java)
 * @author syam(syamn)
 */
public class TaskManager {
    // Logger
    private static final Logger log = Advertise.log;
    private static final String logPrefix = Advertise.logPrefix;
    private static final String msgPrefix = Advertise.msgPrefix;
    private final Advertise plugin;

    // taskID
    private int taskID = -1;

    public TaskManager (final Advertise plugin){
        this.plugin = plugin;
    }

    public boolean setSchedule(final boolean enable, final CommandSender sender){
        if (enable){
            return enableTask(sender);
        }else{
            return disableTask(sender);
        }
    }

    private boolean enableTask(CommandSender sender){
        if (taskID != -1){
            if (sender != null) Actions.message(sender, "&cScheduler already running.");
            log.warning(logPrefix + "Scheduler already running");
            return true;
        }else{
            final int ticks = 20 * 60 * plugin.getConfigs().getInterval();
            // try to use async thread
            //taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new AnnounceTask(plugin), ticks, ticks);
            taskID = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new AnnounceTask(plugin), ticks, ticks);
            if (taskID == -1){
                if (sender == null) Actions.message(sender, "&cScheduling failed!");
                log.warning(logPrefix + "Scheduling failed");
                return false;
            }else{
                if (sender == null) Actions.message(sender, "&aScheduled every " + plugin.getConfigs().getInterval() + " minutes!");
                log.info(logPrefix + "Scheduled every " + plugin.getConfigs().getInterval() + " minutes!");
                return true;
            }
        }
    }

    private boolean disableTask(CommandSender sender){
        if (taskID == -1){
            if (sender != null) Actions.message(sender, "&cNo schedule running.");
            log.warning(logPrefix + "No schedule running");
        }else{
            plugin.getServer().getScheduler().cancelTask(taskID);
            if (sender != null) Actions.message(sender, "&cScheduling finished!");
            log.info(logPrefix + "Scheduling finished");

        }
        taskID = -1;
        return true;
    }

    public boolean isRunning(){
        return (taskID != -1);
    }
}
