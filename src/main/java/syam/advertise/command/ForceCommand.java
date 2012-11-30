/**
 * Advertise - Package: syam.advertise.command
 * Created: 2012/11/30 15:04:57
 */
package syam.advertise.command;

import syam.advertise.Perms;
import syam.advertise.announce.Ad;
import syam.advertise.announce.AnnounceTask;
import syam.advertise.exception.CommandException;
import syam.advertise.util.Actions;
import syam.advertise.util.Util;

/**
 * ForceCommand (ForceCommand.java)
 * @author syam(syamn)
 */
public class ForceCommand extends BaseCommand {
    public ForceCommand() {
        bePlayer = false;
        name = "force";
        argLength = 0;
        usage = "[AdsID] <- force broadcast advertisement";
    }

    @Override
    public void execute() throws CommandException {
        if (args.size() <= 0){
            plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new AnnounceTask(plugin), 0L);
        }else{
            if (!Util.isInteger(args.get(0))){
                throw new CommandException("&cNot a number: " + args.get(0));
            }
            int id = Integer.parseInt(args.get(0));
            try{
                new Ad(id);
            }catch (IllegalArgumentException ex){
                throw new CommandException("&c広告ID " + id + " が見つかりません！");
            }
            plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new AnnounceTask(plugin, id), 0L);
        }
        Actions.message(sender, "&a広告をスケジューリングしました");
    }

    @Override
    public boolean permission() {
        return Perms.FORCE.has(sender);
    }
}