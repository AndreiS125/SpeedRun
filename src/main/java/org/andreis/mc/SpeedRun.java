package org.andreis.mc;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public final class SpeedRun extends JavaPlugin {
    static int countdown=30;
    static boolean started = false;
    static int timer;
    static Player runner;
    static SpeedRun instance;
    public Displayer play;
    @Override
    public void onEnable() {

            instance = this;
            Scoreboard main = Bukkit.getScoreboardManager().getMainScoreboard();


            Team hunters = main.registerNewTeam("Hunter");

            hunters.setPrefix(ChatColor.RED + "HUNTER ");


        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        play=new Displayer();
        getCommand("start").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


                    for (Player pl : Bukkit.getOnlinePlayers()){
                        if(Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Hunter").hasPlayer(pl)){
                            pl.sendTitle("До старта "+countdown+" секунд.","Ваша роль - охотник. ");
                        }
                        else{
                            pl.sendTitle("До старта "+countdown+" секунд.","Вы обязаны выжить.");
                        }

                }
                    started=true;
                    BossBar bar = Bukkit.createBossBar("ДО СТАРТА "+countdown+" секунд.", BarColor.BLUE, BarStyle.SEGMENTED_10);
                    bar.setVisible(true);
                    timer=Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, ()->{
                        countdown=countdown-1;
                        for (Player pl : Bukkit.getOnlinePlayers()){
                            if(Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Hunter").hasPlayer(pl)){
                                if(countdown>0) {
                                    pl.sendTitle("До старта " + countdown + " секунд.", "Ваша роль - охотник. ");
                                    pl.setDisplayName(ChatColor.RED+"HUNTER "+ChatColor.WHITE+pl.getName());
                                    pl.setPlayerListName(ChatColor.RED+"HUNTER "+ChatColor.WHITE+pl.getName());
                                    pl.setCustomName(ChatColor.RED+"HUNTER "+ChatColor.WHITE+pl.getName());
                                    pl.setCustomNameVisible(true);
                                    bar.setTitle("ДО СТАРТА "+countdown+" секунд.");
                                    bar.setProgress(countdown/32);
                                    
                                }


                            }

                            play=new Displayer();
                            play.display(pl, countdown);



                            hunters.setPrefix(ChatColor.RED + "HUNTER ");

                        }
                    }, 20,20);



                return false;
            }
        });
        getCommand("stop").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

                Player pl = Bukkit.getPlayer(sender.getName());
                Bukkit.getScheduler().cancelTask(timer);

                hunters.unregister();
                pl.getWorld().setGameRuleValue("keepInventory", "true");
                for(Player p:Bukkit.getOnlinePlayers()){
                    p.setHealth(0);
                }
                pl.getWorld().setGameRuleValue("keepInventory", "false");
                started=false;
                countdown=30;


                return false;
            }
        });
        getCommand("c").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

                Player pl = Bukkit.getPlayer(sender.getName());
                if(pl.getInventory().getItemInMainHand().getType()==Material.AIR) {
                    pl.getInventory().setItemInHand(new ItemStack(Material.COMPASS));
                }
                else{
                    pl.getWorld().dropItem(pl.getLocation(), pl.getInventory().getItemInMainHand());
                    pl.getInventory().setItemInHand(new ItemStack(Material.COMPASS));
                }
                pl.setCompassTarget(runner.getLocation());
                return false;
            }
        });


        getCommand("setmerunner").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
                try {
                    runner = Bukkit.getPlayer(sender.getName());
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        if (!pl.getName().equals(runner.getName())) {
                            Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Hunter").addPlayer(pl);

                         }
                    }

                }
                catch (Exception ex){
                    sender.sendMessage(ex.toString());
                }
                return false;
            }
        });

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
