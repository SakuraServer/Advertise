/**
 * Advertise - Package: syam.advertise.command
 * Created: 2012/11/30 17:29:20
 */
package syam.advertise.command;

import org.bukkit.entity.Player;

import syam.advertise.Perms;
import syam.advertise.announce.Ad;
import syam.advertise.exception.CommandException;
import syam.advertise.util.Actions;
import syam.advertise.util.Util;

/**
 * InfoCommand (InfoCommand.java)
 * @author syam(syamn)
 */
public class InfoCommand extends BaseCommand {
    public InfoCommand() {
        bePlayer = false;
        name = "info";
        argLength = 1;
        usage = "<Ad ID> <- show your ad info";
    }

    @Override
    public void execute() throws CommandException {
        // check id
        if (!Util.isInteger(args.get(0))){
            throw new CommandException("&cNot a number: " + args.get(0));
        }
        final int data_id = Integer.parseInt(args.get(0));
        if (data_id <= 0){
            throw new CommandException("&cInvalid number: " + data_id);
        }

        Ad ad;
        try{
            ad= new Ad(data_id);
        }catch (IllegalArgumentException ex){
            throw new CommandException("&c広告ID " + data_id + " が見つかりません！");
        }

        // check owner
        if (!Perms.INFO_OTHER.has(sender) && sender instanceof Player){
            if (!ad.getPlayerName().equals(player.getName())){
                throw new CommandException("&c指定したIDはあなたの広告ではありません！");
            }
        }

        // build status
        String status = null;
        switch (ad.getStatus()){
            // case 0: 正常、未削除状態
            case 1:
                status = "&c登録者によって削除(キャンセル)済み";
                break;
            case 2:
                status = "&cスタッフによって削除(キャンセル)済み";
                break;
        }

        // send info
        Actions.message(sender, "&a広告#&6" + data_id + "&f (&6" + ad.getPlayerName() + "&f)");
        Actions.message(sender, "&7->&f " + ad.getText());
        Actions.message(sender, "&a登録日:&6 " + Util.getDispTimeByUnixTime(ad.getRegistered()));
        Actions.message(sender, "&a終了日:&6 " + Util.getDispTimeByUnixTime(ad.getExpired()));
        if (status != null) Actions.message(sender, "&aステータス:&6 " + status);
        Actions.message(sender, "&a総選択回数:&6 " + ad.getViewCount() + " &a総表示回数:&6" + ad.getViewPlayers());
    }

    @Override
    public boolean permission() {
        return (Perms.INFO_SELF.has(sender) || Perms.INFO_OTHER.has(sender));
    }
}