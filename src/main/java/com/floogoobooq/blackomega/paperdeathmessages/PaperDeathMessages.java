package com.floogoobooq.blackomega.paperdeathmessages;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperDeathMessages extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void entityDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        EntityDamageEvent damageEvent = player.getLastDamageCause();
        if (damageEvent == null) {
            //No death actually happened, probably cancelled by something
            return;
        }
        DamageCause cause = player.getLastDamageCause().getCause();

        StringBuilder stringBuilder = new StringBuilder();

        Random random = new Random();
        if(cause == DamageCause.DROWNING) { //Death by drowning
            int drownRand = random.nextInt(2);
            if(drownRand == 0) {
                stringBuilder.append(player.getDisplayName());
                stringBuilder.append(" forgot how to swim");
            } else if(drownRand == 1) {
                stringBuilder.append(player.getDisplayName());
                stringBuilder.append(" discovered they don't have gills");
            }

        } else if(cause == DamageCause.LIGHTNING) { //Death by lightning (not fire afterwards)
            stringBuilder.append(player.getDisplayName());
            stringBuilder.append(" built up too much charge");

        } else if(cause == DamageCause.FLY_INTO_WALL) { //Death by hitting a wall with elytra (not when hitting ground)
            int flyRand = random.nextInt(3);
            if(flyRand == 0) {
                stringBuilder.append(player.getDisplayName());
                stringBuilder.append(" forgot their flying lessons");
            } else if(flyRand == 1) {
                stringBuilder.append("Oof ouch owie ");
                stringBuilder.append(player.getDisplayName());
                stringBuilder.append("'s bones");
            } else if(flyRand == 2) {
                stringBuilder.append(player.getDisplayName());
                stringBuilder.append(" performed a controlled flight into terrain");
            }

        } else if(cause == DamageCause.ENTITY_ATTACK || cause == DamageCause.ENTITY_SWEEP_ATTACK) { //Death by whack
            if(player.getKiller() != null) {
                Player killer = player.getKiller();
                String bitchwhipper = "The Bitchwhipper";
                String name = killer.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                if (name.equals(bitchwhipper)) {
                    stringBuilder.append(killer.getDisplayName());
                    stringBuilder.append(" bitchwhipped ");
                    stringBuilder.append(player.getDisplayName());
                    player.getWorld().playSound(player.getLocation(), "custom.whip", 1F, 1F);
                } else {
                    stringBuilder.append(event.getDeathMessage());
                }
            } else {
                stringBuilder.append(event.getDeathMessage());
            }

        } else {
            stringBuilder.append(event.getDeathMessage());
        }

        stringBuilder.append(" at [");
        stringBuilder.append(player.getLocation().getBlockX());
        stringBuilder.append(", ");
        stringBuilder.append(player.getLocation().getBlockY());
        stringBuilder.append(", ");
        stringBuilder.append(player.getLocation().getBlockZ());
        stringBuilder.append("]");

        event.setDeathMessage(stringBuilder.toString());

        player.getWorld().playSound(player.getLocation(), "custom.oof", 1F, 1F);
    }
}
