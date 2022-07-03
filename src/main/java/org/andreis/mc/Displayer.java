package org.andreis.mc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Displayer {
    ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    Scoreboard scoreboard=scoreboardManager.getMainScoreboard();

    private Objective hunt;
    private Objective run;

    public Displayer() {
    scoreboardManager = SpeedRun.instance.getServer().getScoreboardManager();

    scoreboard = scoreboardManager.getMainScoreboard();


    }
    public void display(Player player, int time) {
        hunt = scoreboard.getObjective("hunt");
        hunt.unregister();
        hunt=scoreboard.registerNewObjective("hunt", "null");
        hunt.setDisplaySlot(DisplaySlot.SIDEBAR);
        run = scoreboard.getObjective("run");
        run.unregister();
        run=scoreboard.registerNewObjective("run", "null");
        run.setDisplaySlot(DisplaySlot.SIDEBAR);
        int sec=time*-1;
        int min=0;
        int hours=0;

        min=(sec/60)%60;
        hours=sec/3600;
        sec=sec%60;
        String tosay="Время игры: ";
        if(hours>0){
            tosay=tosay+hours+":";
        }
        if(min>0){
            tosay=tosay+min+":";
        }
        tosay=tosay+sec;

        if(SpeedRun.runner.getName().equals(player.getName())) {
            run.setDisplayName("Ваша роль: SpeedRunner");
            run.getScore(tosay).setScore(0);

        }
        else{
            hunt.setDisplayName("Ваша роль: Hunter");
            hunt.getScore(tosay).setScore(0);
        }


        player.setScoreboard(scoreboard);
    }


}