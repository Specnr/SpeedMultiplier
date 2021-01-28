package me.specnr.speedmultiplier;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Timer;
import java.util.TimerTask;

public final class SpeedMultiplier extends JavaPlugin {

    public double multiplier = 1;
    public Timer timer = new Timer();

    class updateMultiplier extends TimerTask {
        public void run() {
            if (multiplier * getConfig().getDouble("Multiplier") > getConfig().getDouble("Limit")) {
                multiplier = getConfig().getDouble("Limit");
                timer.cancel();
                timer.purge();
            } else {
                multiplier *= getConfig().getDouble("Multiplier");
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(multiplier / 10);
            }
            sendAll("Current Multiplier: " + multiplier);
        }
    }

    public void sendAll(String msg) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage("[§4§oSpeed§r] " + msg));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("speed")) {
            timer.schedule(new updateMultiplier(), 0, (int) (getConfig().getDouble("Period") * 60000));
        }
        else if (command.getName().equals("speed-reset")) {
            timer.cancel();
            timer.purge();
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);
            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        System.out.println("Speed Multiplier Loaded");
    }

    @Override
    public void onDisable() {
        timer.cancel();
        timer.purge();
    }

}
