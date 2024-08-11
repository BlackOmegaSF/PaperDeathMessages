package com.kleinercode.plugins.paperdeathmessages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.ComponentDecoder;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

import static com.kleinercode.plugins.paperdeathmessages.ComponentHandler.checkNullComponent;

public final class PaperDeathMessages extends JavaPlugin implements Listener {

    private final List<String> playersToTrack = new ArrayList<>();
    File serverFolder;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig(); // Saves default config if none exists. Will not overwrite existing
        // Get and load config
        List<String> loadedTrackedPlayers = getConfig().getStringList("players");
        playersToTrack.addAll(loadedTrackedPlayers);
        serverFolder = getServer().getWorldContainer();

        // Register player logging command
        Objects.requireNonNull(this.getCommand("logdeaths")).setExecutor(new CommandLogDeaths());
        Objects.requireNonNull(this.getCommand("logdeaths")).setTabCompleter(new LogDeathsTabCompleter());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        playersToTrack.clear();
    }

    @EventHandler
    public void entityDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        EntityDamageEvent damageEvent = player.getLastDamageCause();
        if (damageEvent == null) {
            // No death actually happened, probably cancelled by something
            return;
        }
        EntityDamageEvent.DamageCause cause = player.getLastDamageCause().getCause();
        Component displayName = player.displayName();
        Location playerLocation = player.getLocation();

        TextComponent.Builder componentBuilder = Component.empty().toBuilder();

        Random random = new Random();
        switch (cause) {

            case EntityDamageEvent.DamageCause.DROWNING -> {
                int drownRand = random.nextInt(2);
                componentBuilder.append(displayName);
                if(drownRand == 0) {
                    componentBuilder.append(Component.text(" forgot how to swim"));
                } else {
                    componentBuilder.append(Component.text(" discovered they don't have gills"));
                }
            }

            case EntityDamageEvent.DamageCause.LIGHTNING -> {
                componentBuilder.append(displayName);
                componentBuilder.append(Component.text(" built up too much charge"));
            }

            case EntityDamageEvent.DamageCause.FLY_INTO_WALL -> {
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
            }

            case EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK -> {
                if(player.getKiller() != null) {
                    Player killer = player.getKiller();
                    String bitchwhipper = "The Bitchwhipper";
                    ItemMeta itemMeta = killer.getInventory().getItemInMainHand().getItemMeta();
                    String name = ComponentHandler.serializePlainComponent(itemMeta.displayName());
                    if (name.equals(bitchwhipper)) {
                        componentBuilder.append(killer.displayName());
                        componentBuilder.append(Component.text(" bitchwhipped "));
                        componentBuilder.append(displayName);
                        player.getWorld().playSound(playerLocation, "custom.whip", 1F, 1F);
                    } else {
                        componentBuilder.append(checkNullComponent(event.deathMessage()));
                    }
                } else {
                    componentBuilder.append(checkNullComponent(event.deathMessage()));
                }
            }

            default -> componentBuilder.append(checkNullComponent(event.deathMessage()));
        }

        componentBuilder.append(Component.text(" at "));

        TextComponent.Builder deathLocationBuilder = Component.empty().toBuilder();
        int playerX = playerLocation.getBlockX();
        int playerY = playerLocation.getBlockY();
        int playerZ = playerLocation.getBlockZ();

        deathLocationBuilder.append(Component.text("["));
        deathLocationBuilder.append(Component.text(playerX));
        deathLocationBuilder.append(Component.text(", "));
        deathLocationBuilder.append(Component.text(playerY));
        deathLocationBuilder.append(Component.text(", "));
        deathLocationBuilder.append(Component.text(playerZ));
        deathLocationBuilder.append(Component.text("]"));

        HoverEvent<Component> teleportHoverEvent = HoverEvent.showText(Component.text("Click to teleport"));
        ClickEvent teleportClickEvent = ClickEvent.suggestCommand("/tp @s " + playerX + " " + playerY + " " + playerZ);

        componentBuilder.append(deathLocationBuilder.hoverEvent(teleportHoverEvent).clickEvent(teleportClickEvent).color(NamedTextColor.GREEN));

        event.deathMessage(componentBuilder.asComponent());

        player.getWorld().playSound(player.getLocation(), "custom.oof", 1F, 1F);

        // Log the death message to output file
        try {
            if (playersToTrack.contains(player.getName())) {
                // Create empty player log file if it doesn't exist
                File playerDeathLog = new File(serverFolder, player.getName() + "Deaths.log");
                playerDeathLog.createNewFile();
                String serializedMessage = ComponentHandler.serializeComplexComponent(event.deathMessage(), player.locale());
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                Files.write(playerDeathLog.toPath(), ("[" + format.format(now) + "] " + serializedMessage + "\r\n").getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Can't log death of " + player.getName() + ", IOError");
        }

    }

    public class CommandLogDeaths implements CommandExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            final String _START = "start";
            final String _STOP = "stop";
            final String _LIST = "list";
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                return false;
            } else if (args.length < 2 && !args[0].equalsIgnoreCase(_LIST)) {
                sender.sendMessage("Missing arguments!");
                return false;
            } else if (args.length > 2) {
                sender.sendMessage("Too many arguments!");
                return false;
            }

            if (args[0].equalsIgnoreCase(_LIST)) {
                StringJoiner joiner = new StringJoiner(", ");
                playersToTrack.forEach(joiner::add);
                sender.sendMessage("Tracking deaths for: " + joiner);
                return true;
            }

            Player targetPlayer = sender.getServer().getPlayer(args[1]);
            if (targetPlayer == null) {
                sender.sendMessage("Player not found");
                return true;
            }

            final String playerName = targetPlayer.getName();
            switch (args[0].toLowerCase()) {

                case _START -> {
                    if (playersToTrack.contains(playerName)) {
                        sender.sendMessage("Already logging deaths for " + playerName);
                        return true;
                    }
                    playersToTrack.add(playerName);
                    getConfig().set("players", playersToTrack);
                    saveConfig();
                    sender.sendMessage("Now logging deaths for " + playerName);
                    return true;
                }

                case _STOP -> {
                    try {
                        if (!playersToTrack.contains(playerName)) {
                            throw new NullPointerException();
                        }
                        playersToTrack.remove(playerName);
                        getConfig().set("players", playersToTrack);
                        saveConfig();
                        sender.sendMessage("Stopped logging deaths for " + playerName);
                    } catch (NullPointerException e) {
                        sender.sendMessage("Deaths aren't being logged for " + playerName);
                    }
                    return true;
                }

                default -> {
                    sender.sendMessage("Invalid parameter \"" + args[0].toLowerCase() + "\"");
                    return false;
                }
            }
        }
    }

    public class LogDeathsTabCompleter implements TabCompleter {

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            List<String> tabCompleteValues = new ArrayList<>();
            final String _START = "start";
            final String _STOP = "stop";
            final String _LIST = "list";
            if (args.length == 1) {
                if (args[0].isEmpty()) {
                    tabCompleteValues.add(_START);
                    tabCompleteValues.add(_STOP);
                    tabCompleteValues.add(_LIST);
                } else {
                    Pattern pattern = Pattern.compile(args[0].toLowerCase());
                    if (pattern.matcher(_START).lookingAt()) {
                        tabCompleteValues.add(_START);
                    }
                    if (pattern.matcher(_STOP).lookingAt()) {
                        tabCompleteValues.add(_STOP);
                    }
                    if (pattern.matcher(_LIST).lookingAt()) {
                        tabCompleteValues.add(_LIST);
                    }
                }
            } else if (args.length == 2) {
                switch (args[0].toLowerCase()) {
                    case _START -> {
                        if (args[1].isEmpty()) {
                            for (Player player : sender.getServer().getOnlinePlayers()) {
                                if (!playersToTrack.contains(player.getName())) {
                                    tabCompleteValues.add(player.getName());
                                }
                            }
                        } else {
                            for (Player player : sender.getServer().getOnlinePlayers()) {
                                Pattern pattern = Pattern.compile(args[1].toLowerCase());
                                if (pattern.matcher(player.getName().toLowerCase()).lookingAt()) {
                                    if (!playersToTrack.contains(player.getName())) {
                                        tabCompleteValues.add(player.getName());
                                    }
                                }
                            }
                        }
                    }

                    case _STOP  -> {
                        if (args[1].isEmpty()) {
                            tabCompleteValues.addAll(playersToTrack);
                        } else {
                            for(String name : playersToTrack) {
                                Pattern pattern = Pattern.compile(args[1].toLowerCase());
                                if (pattern.matcher(name.toLowerCase()).lookingAt()) {
                                    tabCompleteValues.add(name);
                                }
                            }
                        }
                    }

                }

            }
            return tabCompleteValues;
        }

    }
}
