package org.andreis.mc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventListener implements Listener {
    @EventHandler
    public void s(PlayerMoveEvent e) { //u cant fight in lobby
        try {
            if (SpeedRun.countdown > 0) {
                if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Hunter").hasPlayer(e.getPlayer())) {
                    e.setCancelled(true);
                }
            }
            e.getPlayer().setCompassTarget(SpeedRun.runner.getLocation());
        }

        catch (Exception ex){
            for(Player pl:Bukkit.getOnlinePlayers()){
                pl.sendTitle("НЕ ВЫБРАН СПИДРАНЕР", "use command /setmerunner, and start the game with /start");
            }
        }
    }
}
