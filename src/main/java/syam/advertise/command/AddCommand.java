/**
 * Advertise - Package: syam.advertise.command
 * Created: 2012/11/30 11:11:14
 */
package syam.advertise.command;

import syam.advertise.Advertise;
import syam.advertise.Perms;
import syam.advertise.database.Database;
import syam.advertise.exception.CommandException;
import syam.advertise.util.Actions;
import syam.advertise.util.Util;

/**
 * AddCommand (AddCommand.java)
 * @author syam(syamn)
 */
public class AddCommand extends BaseCommand {
    public AddCommand() {
        bePlayer = true;
        name = "add";
        argLength = 2;
        usage = "<days> <text> <- add your advertise";
    }

    @Override
    public void execute() throws CommandException {
        Database db = Advertise.getDatabases();

        // check days
        if (!Util.isInteger(args.get(0))){
            throw new CommandException("&cNot a number: " + args.get(0));
        }
        final int days = Integer.parseInt(args.remove(0));
        if (days <= 0){
            throw new CommandException("&cInvalid number: " + days);
        }
        if (days > config.getMaxDays()){
            throw new CommandException("&c設定可能な最大日数は" + config.getMaxDays() + "日です: " + days);
        }

        // check length
        String text = Util.join(args, " ");
        if (text.length() > 100){
            throw new CommandException("&c広告文が100文字を超えています！ (" + text.length() + "文字)");
        }

        // check registerd count
        int userID = plugin.getManager().getUserID(player.getName(), true);
        int already = db.getInt("SELECT COUNT(*) FROM " + db.dataTable + " WHERE `player_id` = ? AND `expired` > ? AND `status` = 0", userID, Util.getCurrentUnixSec().intValue());
        if (already >= config.getMaxPerPlayer()){
            throw new CommandException("&c既に上限数(" + config.getMaxPerPlayer() + "個)の広告を登録しています！");
        }

        // pay cost
        boolean paid = false;
        double cost = config.getCostPerDay() * days;
        if (config.getUseVault() && cost > 0 && !Perms.ADD_FREE.has(sender)){
            paid = Actions.takeMoney(player.getName(), cost);
            if (!paid){
                throw new CommandException("&cお金が足りません！ " + Actions.getCurrencyString(cost) + "必要です！");
            }
        }

        plugin.getManager().addAdvertise(player.getName(), days, text);

        String msg = "&a次の広告を " + days + "日間 登録しました！";
        if (paid) msg = msg + " &c(-" + Actions.getCurrencyString(cost) + ")";
        Actions.message(sender, msg);
        Actions.message(sender, "&7->&f " + text);
    }

    @Override
    public boolean permission() {
        return Perms.ADD.has(sender);
    }
}