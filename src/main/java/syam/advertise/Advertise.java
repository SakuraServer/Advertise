/**
 * Advertise - Package: syam.advertise
 * Created: 2012/11/30 6:09:41
 */
package syam.advertise;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import syam.advertise.announce.AdvertiseManager;
import syam.advertise.announce.TaskManager;
import syam.advertise.command.AddCommand;
import syam.advertise.command.BaseCommand;
import syam.advertise.command.ForceCommand;
import syam.advertise.command.HelpCommand;
import syam.advertise.command.ListCommand;
import syam.advertise.command.ReloadCommand;
import syam.advertise.command.RemoveCommand;
import syam.advertise.command.TaskCommand;
import syam.advertise.database.Database;
import syam.advertise.util.Metrics;

/**
 * Advertise (Advertise.java)
 *
 * @author syam(syamn)
 */
public class Advertise extends JavaPlugin {
    // ** Logger **
    public final static Logger log = Logger.getLogger("Minecraft");
    public final static String logPrefix = "[Advertise] ";
    public final static String msgPrefix = "&6[Advertise] &f";

    // ** Listener **
    // ServerListener serverListener = new ServerListener(this);

    // ** Commands **
    private List<BaseCommand> commands = new ArrayList<BaseCommand>();

    // ** Private Classes **
    private ConfigurationManager config;
    private TaskManager taskManager;
    private AdvertiseManager manager;

    // ** Static **
    private static Database database;

    // ** Instance **
    private static Advertise instance;

    // ** Hookup Plugins **
    private static Vault vault = null;
    private static Economy economy = null;

    /**
     * プラグイン起動処理
     */
    @Override
    public void onEnable() {
        instance = this;

        PluginManager pm = getServer().getPluginManager();
        config = new ConfigurationManager(this);

        // loadconfig
        try {
            config.loadConfig(true);
        } catch (Exception ex) {
            log.warning(logPrefix
                    + "an error occured while trying to load the config file.");
            ex.printStackTrace();
        }

        if (config.getUseVault()) {
            config.setUseVault(setupVault());
        }

        // プラグインを無効にした場合進まないようにする
        if (!pm.isPluginEnabled(this)) {
            return;
        }

        // Regist Listeners
        // pm.registerEvents(serverListener, this);

        // コマンド登録
        registerCommands();

        // database
        database = new Database(this);
        database.createStructure();

        // manager
        taskManager = new TaskManager(this);
        manager = new AdvertiseManager(this);

        // task start
        taskManager.setSchedule(true, null);

        // メッセージ表示
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[" + pdfFile.getName() + "] version " + pdfFile.getVersion()
                + " is enabled!");

        setupMetrics(); // mcstats
    }

    /**
     * プラグイン停止処理
     */
    @Override
    public void onDisable() {
        taskManager.setSchedule(false, null);
        getServer().getScheduler().cancelTasks(this);

        // メッセージ表示
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[" + pdfFile.getName() + "] version " + pdfFile.getVersion()
                + " is disabled!");
    }

    /**
     * コマンドを登録
     */
    private void registerCommands() {
        // Intro Commands
        commands.add(new HelpCommand());

        // Main Commands
        commands.add(new AddCommand());
        commands.add(new ListCommand());
        commands.add(new RemoveCommand());

        // Admin Commands
        commands.add(new TaskCommand());
        commands.add(new ForceCommand());
        commands.add(new ReloadCommand());
    }

    /**
     * Vaultプラグインにフック
     */
    public boolean setupVault() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("Vault");
        if (plugin != null & plugin instanceof Vault) {
            RegisteredServiceProvider<Economy> economyProvider = getServer()
                    .getServicesManager().getRegistration(
                            net.milkbowl.vault.economy.Economy.class);
            // 経済概念のプラグインがロードされているかチェック
            if (economyProvider == null) {
                log.warning(logPrefix
                        + "Economy plugin NOT found. Disabled Vault plugin integration.");
                return false;
            }

            try {
                vault = (Vault) plugin;
                economy = economyProvider.getProvider();

                if (vault == null || economy == null) {
                    throw new NullPointerException();
                }
            } // 例外チェック
            catch (Exception e) {
                log.warning(logPrefix
                        + "Could NOT be hook to Vault plugin. Disabled Vault plugin integration.");
                return false;
            }

            // Success
            log.info(logPrefix + "Hooked to Vault plugin!");
            return true;
        } else {
            // Vaultが見つからなかった
            log.warning(logPrefix
                    + "Vault plugin was NOT found! Disabled Vault integration.");
            return false;
        }
    }

    /**
     * AnnounceManagerを返す
     * @return AnnounceManager
     */
    public TaskManager getTaskManager(){
        return this.taskManager;
    }

    /**
     * AnnounceManagerを返す
     * @return AnnounceManager
     */
    public AdvertiseManager getManager(){
        return this.manager;
    }

    /**
     * Vaultを返す
     *
     * @return Vault
     */
    public Vault getVault() {
        return vault;
    }

    /**
     * Economyを返す
     *
     * @return Economy
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * データベースを返す
     * @return Database
     */
    public static Database getDatabases(){
        return database;
    }

    /**
     * Metricsセットアップ
     */
    private void setupMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ex) {
            log.warning(logPrefix + "cant send metrics data!");
            ex.printStackTrace();
        }
    }

    /**
     * コマンドが呼ばれた
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String args[]) {
        if (cmd.getName().equalsIgnoreCase("advertise")) {
            if (args.length == 0) {
                // 引数ゼロはヘルプ表示
                args = new String[] { "help" };
            }
            /*
             * else if (args[0].equalsIgnoreCase("gen")){ args[0] = "generate";
             * }
             */

            outer: for (BaseCommand command : commands
                    .toArray(new BaseCommand[0])) {
                String[] cmds = command.getName().split(" ");
                for (int i = 0; i < cmds.length; i++) {
                    if (i >= args.length || !cmds[i].equalsIgnoreCase(args[i])) {
                        continue outer;
                    }
                    // 実行
                    return command.run(this, sender, args, commandLabel);
                }
            }
            // 有効コマンドなし デフォルトコマンド実行
            new HelpCommand().run(this, sender, args, commandLabel);
            return true;
        }
        return false;
    }

    /**
     * デバッグログ
     *
     * @param msg
     */
    public void debug(final String msg) {
        if (config.isDebug()) {
            log.info(logPrefix + "[DEBUG]" + msg);
        }
    }

    /* getter */
    /**
     * コマンドを返す
     *
     * @return List<BaseCommand>
     */
    public List<BaseCommand> getCommands() {
        return commands;
    }

    /**
     * 設定マネージャを返す
     *
     * @return ConfigurationManager
     */
    public ConfigurationManager getConfigs() {
        return config;
    }

    /**
     * インスタンスを返す
     *
     * @return MotdManagerインスタンス
     */
    public static Advertise getInstance() {
        return instance;
    }
}