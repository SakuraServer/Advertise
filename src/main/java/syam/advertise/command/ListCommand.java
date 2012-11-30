/**
 * Advertise - Package: syam.advertise.command
 * Created: 2012/11/30 11:37:33
 */
package syam.advertise.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import syam.advertise.Advertise;
import syam.advertise.Perms;
import syam.advertise.database.Database;
import syam.advertise.exception.CommandException;
import syam.advertise.util.Actions;
import syam.advertise.util.Util;

/**
 * ListCommand (ListCommand.java)
 * @author syam(syamn)
 */
public class ListCommand extends BaseCommand {
    public ListCommand() {
        bePlayer = false;
        name = "list";
        argLength = 0;
        usage = "<- show advertisement list";
    }

    @Override
    public void execute() throws CommandException {
        boolean self = false;
        boolean console = false;
        if (!(sender instanceof Player)){
            console = true;
        }

        String target;

        // 自分の情報表示
        if (args.size() <= 0){
            target = (!console) ? player.getName() : null;
            self = (!console) ? true : false;
        }else{
            // check perm
            if (!Perms.LIST_OTHER.has(sender)){
                throw new CommandException("&cあなたは他人の広告リストを見る権限がありません");
            }

            target = args.get(0);
            if (target.equalsIgnoreCase("-all")){
                target = null;
            }
        }

        String header = msgPrefix;
        if (target == null){
            header += "&a全アクティブ広告リスト";
        }else{
            if (self) header += "&aあなたのアクティブ広告リスト";
            else header += "&a" + target + " のアクティブ広告リスト";
        }

        List<String> lines = buildStrings(target);
        if (lines == null || lines.size() <= 0){
            Actions.message(sender, header + "&7(0)");
            Actions.message(sender, "&7 (なし)");
        }else{
            Actions.message(sender, header + "&7(" + lines.size() + ")");
            for (String line : lines){
                Actions.message(sender, line);
            }
        }
    }

    private List<String> buildStrings(final String target){
        List<String> ret = new ArrayList<String>();
        Database db = Advertise.getDatabases();

        HashMap<Integer, ArrayList<String>> datas;
        if (target != null){
            int playerID = plugin.getManager().getUserID(target, false);
            if (playerID <= 0){
                return null;
            }
            datas = db.read("SELECT `data_id`, `text` FROM " + db.dataTable + " WHERE player_id = ? AND `expired` > ? AND `status` = 0", playerID, Util.getCurrentUnixSec().intValue());
            for (ArrayList<String> record : datas.values()){
                ret.add("&2#&a" + record.get(0) + "&7: &f" + record.get(1));
            }
        }
        else{
            datas = db.read("SELECT `data_id`, `player_name`, `text` FROM " + db.dataTable + " NATURAL JOIN " + db.userTable +
                    " WHERE `expired` > ? AND `status` = 0", Util.getCurrentUnixSec().intValue());
            for (ArrayList<String> record : datas.values()){
                ret.add("&2#&a" + record.get(0) + "&7: &6" + record.get(1) + "&7: &f" + record.get(2));
            }
        }

        return ret;
    }

    @Override
    public boolean permission() {
        return (Perms.LIST_SELF.has(sender) || Perms.LIST_OTHER.has(sender));
    }
}