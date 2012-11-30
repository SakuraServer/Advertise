/**
 * Advertise - Package: syam.advertise.command
 * Created: 2012/11/30 8:43:14
 */
package syam.advertise.command;

import syam.advertise.Perms;
import syam.advertise.util.Actions;

/**
 * TaskCommand (TaskCommand.java)
 * @author syam(syamn)
 */
public class TaskCommand extends BaseCommand {
    public TaskCommand() {
        bePlayer = false;
        name = "task";
        argLength = 0;
        usage = "<enable|disable> <- set task status";
    }

    @Override
    public void execute() {
        if (args.size() <= 0){
            if (plugin.getTaskManager().isRunning()){
                Actions.message(sender, "&aAnnounce task running!");
            }else{
                Actions.message(sender, "&aAnnounce task not running!");
            }
            return;
        }else{
            String arg = args.get(0);
            if ("enable".equalsIgnoreCase(arg)){
                plugin.getTaskManager().setSchedule(true, sender);
            }else{
                plugin.getTaskManager().setSchedule(false, sender);
            }
        }
    }

    @Override
    public boolean permission() {
        return Perms.TASK.has(sender);
    }
}