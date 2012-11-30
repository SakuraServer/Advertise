/**
 * Advertise - Package: syam.advertise.command
 * Created: 2012/11/30 13:24:59
 */
package syam.advertise.command;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import syam.advertise.Advertise;
import syam.advertise.Perms;
import syam.advertise.database.Database;
import syam.advertise.exception.CommandException;
import syam.advertise.util.Actions;
import syam.advertise.util.Util;

/**
 * RemoveCommand (RemoveCommand.java)
 * @author syam(syamn)
 */
public class RemoveCommand extends BaseCommand {
    public RemoveCommand() {
        bePlayer = false;
        name = "remove";
        argLength = 1;
        usage = "<Ads ID> <- remove your advertise";
    }

    @Override
    public void execute() throws CommandException {
        boolean console = (!(sender instanceof Player));

        // check id
        if (!Util.isInteger(args.get(0))){
            throw new CommandException("&cNot a number: " + args.get(0));
        }
        final int data_id = Integer.parseInt(args.get(0));
        if (data_id <= 0){
            throw new CommandException("&cInvalid number: " + data_id);
        }

        Database db = Advertise.getDatabases();
        HashMap<Integer, ArrayList<String>> records = db.read("SELECT `player_id`, `status`, `expired`, `text` FROM " + db.dataTable + " WHERE data_id = ?", data_id);
        if (records == null || records.size() <= 0){
            throw new CommandException("&c広告ID " + data_id + " が見つかりません！");
        }

        ArrayList<String> data = records.get(1);
        int ad_userID = Integer.parseInt(data.get(0));
        int ad_status = Integer.parseInt(data.get(1));
        Long ad_expired = Long.parseLong(data.get(2));
        String ad_text = data.get(3);

        // consoleなら所有者チェックをバイパス
        if (!console){
            int userID = plugin.getManager().getUserID(player.getName(), false);
            if (ad_userID != userID){
                throw new CommandException("&c指定したIDはあなたの広告ではありません！");
            }
        }
        if (ad_status != 0 || ad_expired <= Util.getCurrentUnixSec()){
            throw new CommandException("&c指定したIDはアクティブ広告ではありません！");
        }

        plugin.getManager().removeAdvertise(data_id);

        Actions.message(sender, "&a次の広告(#" + data_id + ")を削除しました！");
        Actions.message(sender, "&7->&f " + ad_text);
    }

    @Override
    public boolean permission() {
        return Perms.ADD.has(sender);
    }
}