/**
 * Advertise - Package: syam.advertise.command
 * Created: 2012/11/30 6:37:42
 */
package syam.advertise.command;

import syam.advertise.Perms;
import syam.advertise.util.Actions;

/**
 * ReloadCommand (ReloadCommand.java)
 *
 * @author syam(syamn)
 */
public class ReloadCommand extends BaseCommand {
    public ReloadCommand() {
        bePlayer = false;
        name = "reload";
        argLength = 0;
        usage = "<- reload config.yml";
    }

    @Override
    public void execute() {
        if (plugin.getTaskManager().isRunning()){
            plugin.getTaskManager().setSchedule(false, sender);
        }

        try {
            plugin.getConfigs().loadConfig(false);
        } catch (Exception ex) {
            log.warning(logPrefix
                    + "an error occured while trying to load the config file.");
            ex.printStackTrace();
            return;
        }
        Actions.message(sender, "&aConfiguration reloaded!");
        plugin.getTaskManager().setSchedule(true, sender);
    }

    @Override
    public boolean permission() {
        return Perms.RELOAD.has(sender);
    }
}