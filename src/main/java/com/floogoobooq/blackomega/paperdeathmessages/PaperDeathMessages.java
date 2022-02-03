package com.floogoobooq.blackomega.paperdeathmessages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.regex.Pattern;

import static com.floogoobooq.blackomega.paperdeathmessages.ComponentHandler.checkNullComponent;

public class PaperDeathMessages extends JavaPlugin implements Listener {

    private final List<String> playersToTrack = new ArrayList<>();
    File serverFolder;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig(); //Saves default config if none exists. Will not overwrite existing
        //Get and load config
        List<String> loadedTrackedPlayers = getConfig().getStringList("players");
        playersToTrack.addAll(loadedTrackedPlayers);
        serverFolder = getServer().getWorldContainer();

        //Register player logging command
        this.getCommand("logdeaths").setExecutor(new CommandLogDeaths());
        this.getCommand("logdeaths").setTabCompleter(new LogDeathsTabCompleter());


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
                String name = ComponentHandler.serializePlainComponent(itemMeta.displayName());
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

        //Log the death message to output file
        try {
            if (playersToTrack.contains(player.getName())) { //Player's death should be tracked
                //Create empty player log file if it doesn't exist
                File playerDeathLog = new File(serverFolder, player.getName() + "Deaths.log");
                playerDeathLog.createNewFile();
                Files.write(playerDeathLog.toPath(), (ComponentHandler.serializeComplexComponent(event.deathMessage()) + "\r\n").getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Can't log death of " + player.getName() + ", IOError");
        }

    }

    public class CommandLogDeaths implements CommandExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                return false;
            } else if (args.length < 2) {
                sender.sendMessage("Missing arguments");
            }

            Player targetPlayer = sender.getServer().getPlayer(args[1]);
            if (targetPlayer == null) {
                sender.sendMessage("Player not found");
                return false;
            }

            final String _START = "START";
            final String _STOP = "STOP";
            switch (args[0].toUpperCase()) {
                case _START -> {
                    playersToTrack.add(targetPlayer.getName());
                    getConfig().set("players", playersToTrack);
                    saveConfig();
                    return true;
                }
                case _STOP -> {
                    try {
                        playersToTrack.remove(targetPlayer.getName());
                        getConfig().set("players", playersToTrack);
                        saveConfig();
                    } catch (NullPointerException e) {
                        sender.sendMessage("Player was not being logged");
                    }
                    return true;
                }
                default -> {
                    sender.sendMessage("Invalid parameter \"" + args[0].toUpperCase() + "\"");
                    return false;
                }
            }

        }
    }

    public class LogDeathsTabCompleter implements TabCompleter {

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            List<String> tabCompleteValues = new ArrayList<>();
            if (args.length == 1) {
                if (args[0].isEmpty()) {
                    tabCompleteValues.add("START");
                    tabCompleteValues.add("STOP");
                } else {
                    Pattern pattern = Pattern.compile(args[0].toUpperCase());
                    if (pattern.matcher("START").lookingAt()) {
                        tabCompleteValues.add("START");
                    }
                    if (pattern.matcher("STOP").lookingAt()) {
                        tabCompleteValues.add("STOP");
                    }
                }
            } else if (args.length == 2) {
                switch (args[0].toUpperCase()) {
                    case "START":
                        if (args[1].isEmpty()) {
                            for (Player player : sender.getServer().getOnlinePlayers()) {
                                if (!playersToTrack.contains(player.getName())) {
                                    tabCompleteValues.add(player.getName());
                                }
                            }
                        } else {
                            for (Player player : sender.getServer().matchPlayer(args[1])) {
                                if (!playersToTrack.contains(player.getName())) {
                                    tabCompleteValues.add(player.getName());
                                }
                            }
                        }
                    case "STOP":
                        if (args[1].isEmpty()) {
                            tabCompleteValues.addAll(playersToTrack);
                        } else {
                            for(String name : playersToTrack) {
                                Pattern pattern = Pattern.compile(args[1].toUpperCase());
                                if (pattern.matcher(name.toUpperCase()).lookingAt()) {
                                    tabCompleteValues.add(name);
                                }
                            }
                        }

                }

            } else if (args.length > 2) {
                tabCompleteValues.add("Too many arguments!");
            }
            return tabCompleteValues;
        }
    }

}
