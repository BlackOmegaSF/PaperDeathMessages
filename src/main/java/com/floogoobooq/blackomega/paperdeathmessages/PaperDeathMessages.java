package com.floogoobooq.blackomega.paperdeathmessages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

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
        Component displayName = player.displayName();

        TextComponent.Builder componentBuilder = Component.empty().toBuilder();

        Random random = new Random();
        if(cause == DamageCause.DROWNING) { //Death by drowning
            int drownRand = random.nextInt(2);
            componentBuilder.append(displayName);
            if(drownRand == 0) {
                componentBuilder.append(Component.text(" forgot how to swim"));
            } else {
                componentBuilder.append(Component.text(" discovered they don't have gills"));
            }

        } else if(cause == DamageCause.LIGHTNING) { //Death by lightning (not fire afterwards)
            componentBuilder.append(displayName);
            componentBuilder.append(Component.text(" built up too much charge"));

        } else if(cause == DamageCause.FLY_INTO_WALL) { //Death by hitting a wall with elytra (not when hitting ground)
            int flyRand = random.nextInt(3);
            if(flyRand == 0) {
                componentBuilder.append(displayName);
                componentBuilder.append(Component.text(" forgot their flying lessons"));
            } else if(flyRand == 1) {
                componentBuilder.append(Component.text("Oof ouch owie "));
                componentBuilder.append(displayName);
                componentBuilder.append(Component.text("'s bones"));
            } else {
                componentBuilder.append(displayName);
                componentBuilder.append(Component.text(" performed a controlled flight into terrain"));
            }

        } else if(cause == DamageCause.ENTITY_ATTACK || cause == DamageCause.ENTITY_SWEEP_ATTACK) { //Death by whack
            if(player.getKiller() != null) {
                Player killer = player.getKiller();
                String bitchwhipper = "The Bitchwhipper";
                ItemMeta itemMeta = killer.getInventory().getItemInMainHand().getItemMeta();
                String name = serializeComponent(itemMeta.displayName());
                if (name.equals(bitchwhipper)) {
                    componentBuilder.append(killer.displayName());
                    componentBuilder.append(Component.text(" bitchwhipped "));
                    componentBuilder.append(displayName);
                    player.getWorld().playSound(player.getLocation(), "custom.whip", 1F, 1F);
                } else {
                    componentBuilder.append(checkNullComponent(event.deathMessage()));
                }
            } else {
                componentBuilder.append(checkNullComponent(event.deathMessage()));
            }

        } else {
            componentBuilder.append(checkNullComponent(event.deathMessage()));
        }

        componentBuilder.append(Component.text(" at ["));
        componentBuilder.append(Component.text(player.getLocation().getBlockX()));
        componentBuilder.append(Component.text(", "));
        componentBuilder.append(Component.text(player.getLocation().getBlockY()));
        componentBuilder.append(Component.text(", "));
        componentBuilder.append(Component.text(player.getLocation().getBlockZ()));
        componentBuilder.append(Component.text("]"));

        event.deathMessage(componentBuilder.asComponent());

        player.getWorld().playSound(player.getLocation(), "custom.oof", 1F, 1F);
    }

    private String serializeComponent(Component component) {
        if (component == null) {
            return "";
        } else {
            return PlainTextComponentSerializer.plainText().serialize(component);
        }
    }

    private Component checkNullComponent(Component component) {
        if (component == null) {
            return Component.text("");
        } else {
            return component;
        }
    }
}
