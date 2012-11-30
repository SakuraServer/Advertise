/**
 * Advertise - Package: syam.advertise.command
 * Created: 2012/11/30 15:04:57
 */
package syam.advertise.command;

import syam.advertise.Perms;
import syam.advertise.announce.AnnounceTask;
import syam.advertise.util.Actions;

/**
 * ForceCommand (ForceCommand.java)
 * @author syam(syamn)
 */
public class ForceCommand extends BaseCommand {
    public ForceCommand() {
        bePlayer = false;
        name = "force";
        argLength = 0;
        usage = "<- force broadcast advertisement";
    }

    @Override
    public void execute() {
        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new AnnounceTask(plugin), 0L);
        Actions.message(sender, "&a広告をスケジューリングしました");
    }

    @Override
    public boolean permission() {
        return Perms.FORCE.has(sender);
    }
}