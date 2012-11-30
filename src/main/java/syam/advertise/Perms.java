/**
 * Advertise - Package: syam.advertise
 * Created: 2012/11/30 6:38:22
 */
package syam.advertise;

import org.bukkit.permissions.Permissible;

/**
 * Perms (Perms.java)
 *
 * @author syam(syamn)
 */
public enum Perms {
    /* 権限ノード */
    // User Commands
    ADD ("user.add"),
    LIST_SELF ("user.list.self"),
    LIST_OTHER ("user.list.other"),

    // Free Permissions
    ADD_FREE ("free.add"),

    // Spec Permissions
    HIDE_ADVERTISE("hide"),

    // Admin Commands
    TASK("admin.task"),
    RELOAD("admin.reload"),

    ;

    // ノードヘッダー
    final String HEADER = "advertise.";
    private String node;

    /**
     * コンストラクタ
     *
     * @param node
     *            権限ノード
     */
    Perms(final String node) {
        this.node = HEADER + node;
    }

    /**
     * 指定したプレイヤーが権限を持っているか
     *
     * @param player
     *            Permissible. Player, CommandSender etc
     * @return boolean
     */
    public boolean has(final Permissible perm) {
        if (perm == null)
            return false;
        return perm.hasPermission(node); // only support SuperPerms
    }
}
