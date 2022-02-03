package com.floogoobooq.blackomega.paperdeathmessages;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ComponentHandler {

    private static Multimap<String, String> deathMessageTranslations;
    private static Multimap<String, String> otherTranslations;
    private static final Locale defaultLocale = Locale.ENGLISH;

    static {
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("death.fell.accident.ladder", "%1$s fell off a ladder");
        multimap.put("death.fell.accident.vines", "%1$s fell off some vines");
        multimap.put("death.fell.accident.weeping_vines", "%1$s fell off some weeping vines");
        multimap.put("death.fell.accident.twisting_vines", "%1$s fell off some twisting vines");
        multimap.put("death.fell.accident.scaffolding", "%1$s fell off scaffolding");
        multimap.put("death.fell.accident.other_climbable", "%1$s fell while climbing");
        multimap.put("death.fell.accident.generic", "%1$s fell from a high place");
        multimap.put("death.fell.killer", "%1$s was doomed to fall");
        multimap.put("death.fell.assist", "%1$s was doomed to fall by %2$s");
        multimap.put("death.fell.assist.item", "%1$s was doomed to fall by %2$s using %3$s");
        multimap.put("death.fell.finish", "%1$s fell too far and was finished by %2$s");
        multimap.put("death.fell.finish.item", "%1$s fell too far and was finished by %2$s using %3$s");
        multimap.put("death.attack.lightningBolt", "%1$s was struck by lightning");
        multimap.put("death.attack.lightningBolt.player", "%1$s was struck by lightning whilst fighting %2$s");
        multimap.put("death.attack.inFire", "%1$s went up in flames");
        multimap.put("death.attack.inFire.player", "%1$s walked into fire whilst fighting %2$s");
        multimap.put("death.attack.onFire", "%1$s burned to death");
        multimap.put("death.attack.onFire.player", "%1$s was burnt to a crisp whilst fighting %2$s");
        multimap.put("death.attack.lava", "%1$s tried to swim in lava");
        multimap.put("death.attack.lava.player", "%1$s tried to swim in lava to escape %2$s");
        multimap.put("death.attack.hotFloor", "%1$s discovered the floor was lava");
        multimap.put("death.attack.hotFloor.player", "%1$s walked into danger zone due to %2$s");
        multimap.put("death.attack.inWall", "%1$s suffocated in a wall");
        multimap.put("death.attack.inWall.player", "%1$s suffocated in a wall whilst fighting %2$s");
        multimap.put("death.attack.cramming", "%1$s was squished too much");
        multimap.put("death.attack.cramming.player", "%1$s was squashed by %2$s");
        multimap.put("death.attack.drown", "%1$s drowned");
        multimap.put("death.attack.drown.player", "%1$s drowned whilst trying to escape %2$s");
        multimap.put("death.attack.dryout", "%1$s died from dehydration");
        multimap.put("death.attack.dryout.player", "%1$s died from dehydration whilst trying to escape %2$s");
        multimap.put("death.attack.starve", "%1$s starved to death");
        multimap.put("death.attack.starve.player", "%1$s starved to death whilst fighting %2$s");
        multimap.put("death.attack.cactus", "%1$s was pricked to death");
        multimap.put("death.attack.cactus.player", "%1$s walked into a cactus whilst trying to escape %2$s");
        multimap.put("death.attack.generic", "%1$s died");
        multimap.put("death.attack.generic.player", "%1$s died because of %2$s");
        multimap.put("death.attack.explosion", "%1$s blew up");
        multimap.put("death.attack.explosion.player", "%1$s was blown up by %2$s");
        multimap.put("death.attack.explosion.player.item", "%1$s was blown up by %2$s using %3$s");
        multimap.put("death.attack.magic", "%1$s was killed by magic");
        multimap.put("death.attack.magic.player", "%1$s was killed by magic whilst trying to escape %2$s");
        multimap.put("death.attack.even_more_magic", "%1$s was killed by even more magic");
        multimap.put("death.attack.wither", "%1$s withered away");
        multimap.put("death.attack.wither.player", "%1$s withered away whilst fighting %2$s");
        multimap.put("death.attack.witherSkull", "%1$s was shot by a skull from %2$s");
        multimap.put("death.attack.anvil", "%1$s was squashed by a falling anvil");
        multimap.put("death.attack.anvil.player", "%1$s was squashed by a falling anvil whilst fighting %2$s");
        multimap.put("death.attack.fallingBlock", "%1$s was squashed by a falling block");
        multimap.put("death.attack.fallingBlock.player", "%1$s was squashed by a falling block whilst fighting %2$s");
        multimap.put("death.attack.stalagmite", "%1$s was impaled on a stalagmite");
        multimap.put("death.attack.stalagmite.player", "%1$s was impaled on a stalagmite whilst fighting %2$s");
        multimap.put("death.attack.fallingStalactite", "%1$s was skewered by a falling stalactite");
        multimap.put("death.attack.fallingStalactite.player", "%1$s was skewered by a falling stalactite whilst fighting %2$s");
        multimap.put("death.attack.mob", "%1$s was slain by %2$s");
        multimap.put("death.attack.mob.item", "%1$s was slain by %2$s using %3$s");
        multimap.put("death.attack.player", "%1$s was slain by %2$s");
        multimap.put("death.attack.player.item", "%1$s was slain by %2$s using %3$s");
        multimap.put("death.attack.arrow", "%1$s was shot by %2$s");
        multimap.put("death.attack.arrow.item", "%1$s was shot by %2$s using %3$s");
        multimap.put("death.attack.fireball", "%1$s was fireballed by %2$s");
        multimap.put("death.attack.fireball.item", "%1$s was fireballed by %2$s using %3$s");
        multimap.put("death.attack.thrown", "%1$s was pummeled by %2$s");
        multimap.put("death.attack.thrown.item", "%1$s was pummeled by %2$s using %3$s");
        multimap.put("death.attack.indirectMagic", "%1$s was killed by %2$s using magic");
        multimap.put("death.attack.indirectMagic.item", "%1$s was killed by %2$s using %3$s");
        multimap.put("death.attack.thorns", "%1$s was killed trying to hurt %2$s");
        multimap.put("death.attack.thorns.item", "%1$s was killed by %3$s trying to hurt %2$s");
        multimap.put("death.attack.trident", "%1$s was impaled by %2$s");
        multimap.put("death.attack.trident.item", "%1$s was impaled by %2$s with %3$s");
        multimap.put("death.attack.fall", "%1$s hit the ground too hard");
        multimap.put("death.attack.fall.player", "%1$s hit the ground too hard whilst trying to escape %2$s");
        multimap.put("death.attack.outOfWorld", "%1$s fell out of the world");
        multimap.put("death.attack.outOfWorld.player", "%1$s didn't want to live in the same world as %2$s");
        multimap.put("death.attack.dragonBreath", "%1$s was roasted in dragon breath");
        multimap.put("death.attack.dragonBreath.player", "%1$s was roasted in dragon breath by %2$s");
        multimap.put("death.attack.flyIntoWall", "%1$s experienced kinetic energy");
        multimap.put("death.attack.flyIntoWall.player", "%1$s experienced kinetic energy whilst trying to escape %2$s");
        multimap.put("death.attack.fireworks", "%1$s went off with a bang");
        multimap.put("death.attack.fireworks.player", "%1$s went off with a bang whilst fighting %2$s");
        multimap.put("death.attack.fireworks.item", "%1$s went off with a bang due to a firework fired from %3$s by %2$s");
        multimap.put("death.attack.badRespawnPoint.message", "%1$s was killed by %2$s");
        multimap.put("death.attack.badRespawnPoint.link", "Intentional Game Design");
        multimap.put("death.attack.sweetBerryBush", "%1$s was poked to death by a sweet berry bush");
        multimap.put("death.attack.sweetBerryBush.player", "%1$s was poked to death by a sweet berry bush whilst trying to escape %2$s");
        multimap.put("death.attack.sting", "%1$s was stung to death");
        multimap.put("death.attack.sting.player", "%1$s was stung to death by %2$s");
        multimap.put("death.attack.freeze", "%1$s froze to death");
        multimap.put("death.attack.freeze.player", "%1$s was frozen to death by %2$s");

        deathMessageTranslations = multimap;
        
        Multimap<String, String> otherMap = ArrayListMultimap.create();
        otherMap.put("entity.minecraft.area_effect_cloud", "Area Effect Cloud");
        otherMap.put("entity.minecraft.armor_stand", "Armor Stand");
        otherMap.put("entity.minecraft.arrow", "Arrow");
        otherMap.put("entity.minecraft.axolotl", "Axolotl");
        otherMap.put("entity.minecraft.bat", "Bat");
        otherMap.put("entity.minecraft.bee", "Bee");
        otherMap.put("entity.minecraft.blaze", "Blaze");
        otherMap.put("entity.minecraft.boat", "Boat");
        otherMap.put("entity.minecraft.cat", "Cat");
        otherMap.put("entity.minecraft.cave_spider", "Cave Spider");
        otherMap.put("entity.minecraft.chest_minecart", "Minecart with Chest");
        otherMap.put("entity.minecraft.chicken", "Chicken");
        otherMap.put("entity.minecraft.command_block_minecart", "Minecart with Command Block");
        otherMap.put("entity.minecraft.cod", "Cod");
        otherMap.put("entity.minecraft.cow", "Cow");
        otherMap.put("entity.minecraft.creeper", "Creeper");
        otherMap.put("entity.minecraft.dolphin", "Dolphin");
        otherMap.put("entity.minecraft.donkey", "Donkey");
        otherMap.put("entity.minecraft.drowned", "Drowned");
        otherMap.put("entity.minecraft.dragon_fireball", "Dragon Fireball");
        otherMap.put("entity.minecraft.egg", "Thrown Egg");
        otherMap.put("entity.minecraft.elder_guardian", "Elder Guardian");
        otherMap.put("entity.minecraft.end_crystal", "End Crystal");
        otherMap.put("entity.minecraft.ender_dragon", "Ender Dragon");
        otherMap.put("entity.minecraft.ender_pearl", "Thrown Ender Pearl");
        otherMap.put("entity.minecraft.enderman", "Enderman");
        otherMap.put("entity.minecraft.endermite", "Endermite");
        otherMap.put("entity.minecraft.evoker_fangs", "Evoker Fangs");
        otherMap.put("entity.minecraft.evoker", "Evoker");
        otherMap.put("entity.minecraft.eye_of_ender", "Eye of Ender");
        otherMap.put("entity.minecraft.falling_block", "Falling Block");
        otherMap.put("entity.minecraft.fireball", "Fireball");
        otherMap.put("entity.minecraft.firework_rocket", "Firework Rocket");
        otherMap.put("entity.minecraft.fishing_bobber", "Fishing Bobber");
        otherMap.put("entity.minecraft.fox", "Fox");
        otherMap.put("entity.minecraft.furnace_minecart", "Minecart with Furnace");
        otherMap.put("entity.minecraft.ghast", "Ghast");
        otherMap.put("entity.minecraft.giant", "Giant");
        otherMap.put("entity.minecraft.glow_item_frame", "Glow Item Frame");
        otherMap.put("entity.minecraft.glow_squid", "Glow Squid");
        otherMap.put("entity.minecraft.goat", "Goat");
        otherMap.put("entity.minecraft.guardian", "Guardian");
        otherMap.put("entity.minecraft.hoglin", "Hoglin");
        otherMap.put("entity.minecraft.hopper_minecart", "Minecart with Hopper");
        otherMap.put("entity.minecraft.horse", "Horse");
        otherMap.put("entity.minecraft.husk", "Husk");
        otherMap.put("entity.minecraft.ravager", "Ravager");
        otherMap.put("entity.minecraft.illusioner", "Illusioner");
        otherMap.put("entity.minecraft.item", "Item");
        otherMap.put("entity.minecraft.item_frame", "Item Frame");
        otherMap.put("entity.minecraft.killer_bunny", "The Killer Bunny");
        otherMap.put("entity.minecraft.leash_knot", "Leash Knot");
        otherMap.put("entity.minecraft.lightning_bolt", "Lightning Bolt");
        otherMap.put("entity.minecraft.llama", "Llama");
        otherMap.put("entity.minecraft.llama_spit", "Llama Spit");
        otherMap.put("entity.minecraft.magma_cube", "Magma Cube");
        otherMap.put("entity.minecraft.marker", "Marker");
        otherMap.put("entity.minecraft.minecart", "Minecart");
        otherMap.put("entity.minecraft.mooshroom", "Mooshroom");
        otherMap.put("entity.minecraft.mule", "Mule");
        otherMap.put("entity.minecraft.ocelot", "Ocelot");
        otherMap.put("entity.minecraft.painting", "Painting");
        otherMap.put("entity.minecraft.panda", "Panda");
        otherMap.put("entity.minecraft.parrot", "Parrot");
        otherMap.put("entity.minecraft.phantom", "Phantom");
        otherMap.put("entity.minecraft.pig", "Pig");
        otherMap.put("entity.minecraft.piglin", "Piglin");
        otherMap.put("entity.minecraft.piglin_brute", "Piglin Brute");
        otherMap.put("entity.minecraft.pillager", "Pillager");
        otherMap.put("entity.minecraft.player", "Player");
        otherMap.put("entity.minecraft.polar_bear", "Polar Bear");
        otherMap.put("entity.minecraft.potion", "Potion");
        otherMap.put("entity.minecraft.pufferfish", "Pufferfish");
        otherMap.put("entity.minecraft.rabbit", "Rabbit");
        otherMap.put("entity.minecraft.salmon", "Salmon");
        otherMap.put("entity.minecraft.sheep", "Sheep");
        otherMap.put("entity.minecraft.shulker", "Shulker");
        otherMap.put("entity.minecraft.shulker_bullet", "Shulker Bullet");
        otherMap.put("entity.minecraft.silverfish", "Silverfish");
        otherMap.put("entity.minecraft.skeleton", "Skeleton");
        otherMap.put("entity.minecraft.skeleton_horse", "Skeleton Horse");
        otherMap.put("entity.minecraft.slime", "Slime");
        otherMap.put("entity.minecraft.small_fireball", "Small Fireball");
        otherMap.put("entity.minecraft.snowball", "Snowball");
        otherMap.put("entity.minecraft.snow_golem", "Snow Golem");
        otherMap.put("entity.minecraft.spawner_minecart", "Minecart with Spawner");
        otherMap.put("entity.minecraft.spectral_arrow", "Spectral Arrow");
        otherMap.put("entity.minecraft.spider", "Spider");
        otherMap.put("entity.minecraft.squid", "Squid");
        otherMap.put("entity.minecraft.stray", "Stray");
        otherMap.put("entity.minecraft.strider", "Strider");
        otherMap.put("entity.minecraft.tnt", "Primed TNT");
        otherMap.put("entity.minecraft.tnt_minecart", "Minecart with TNT");
        otherMap.put("entity.minecraft.trader_llama", "Trader Llama");
        otherMap.put("entity.minecraft.trident", "Trident");
        otherMap.put("entity.minecraft.tropical_fish", "Tropical Fish");
        otherMap.put("entity.minecraft.tropical_fish.predefined.0", "Anemone");
        otherMap.put("entity.minecraft.tropical_fish.predefined.1", "Black Tang");
        otherMap.put("entity.minecraft.tropical_fish.predefined.2", "Blue Tang");
        otherMap.put("entity.minecraft.tropical_fish.predefined.3", "Butterflyfish");
        otherMap.put("entity.minecraft.tropical_fish.predefined.4", "Cichlid");
        otherMap.put("entity.minecraft.tropical_fish.predefined.5", "Clownfish");
        otherMap.put("entity.minecraft.tropical_fish.predefined.6", "Cotton Candy Betta");
        otherMap.put("entity.minecraft.tropical_fish.predefined.7", "Dottyback");
        otherMap.put("entity.minecraft.tropical_fish.predefined.8", "Emperor Red Snapper");
        otherMap.put("entity.minecraft.tropical_fish.predefined.9", "Goatfish");
        otherMap.put("entity.minecraft.tropical_fish.predefined.10", "Moorish Idol");
        otherMap.put("entity.minecraft.tropical_fish.predefined.11", "Ornate Butterflyfish");
        otherMap.put("entity.minecraft.tropical_fish.predefined.12", "Parrotfish");
        otherMap.put("entity.minecraft.tropical_fish.predefined.13", "Queen Angelfish");
        otherMap.put("entity.minecraft.tropical_fish.predefined.14", "Red Cichlid");
        otherMap.put("entity.minecraft.tropical_fish.predefined.15", "Red Lipped Blenny");
        otherMap.put("entity.minecraft.tropical_fish.predefined.16", "Red Snapper");
        otherMap.put("entity.minecraft.tropical_fish.predefined.17", "Threadfin");
        otherMap.put("entity.minecraft.tropical_fish.predefined.18", "Tomato Clownfish");
        otherMap.put("entity.minecraft.tropical_fish.predefined.19", "Triggerfish");
        otherMap.put("entity.minecraft.tropical_fish.predefined.20", "Yellowtail Parrotfish");
        otherMap.put("entity.minecraft.tropical_fish.predefined.21", "Yellow Tang");
        otherMap.put("entity.minecraft.tropical_fish.type.flopper", "Flopper");
        otherMap.put("entity.minecraft.tropical_fish.type.stripey", "Stripey");
        otherMap.put("entity.minecraft.tropical_fish.type.glitter", "Glitter");
        otherMap.put("entity.minecraft.tropical_fish.type.blockfish", "Blockfish");
        otherMap.put("entity.minecraft.tropical_fish.type.betty", "Betty");
        otherMap.put("entity.minecraft.tropical_fish.type.clayfish", "Clayfish");
        otherMap.put("entity.minecraft.tropical_fish.type.kob", "Kob");
        otherMap.put("entity.minecraft.tropical_fish.type.sunstreak", "Sunstreak");
        otherMap.put("entity.minecraft.tropical_fish.type.snooper", "Snooper");
        otherMap.put("entity.minecraft.tropical_fish.type.dasher", "Dasher");
        otherMap.put("entity.minecraft.tropical_fish.type.brinely", "Brinely");
        otherMap.put("entity.minecraft.tropical_fish.type.spotty", "Spotty");
        otherMap.put("entity.minecraft.turtle", "Turtle");
        otherMap.put("entity.minecraft.vex", "Vex");
        otherMap.put("entity.minecraft.villager.armorer", "Armorer");
        otherMap.put("entity.minecraft.villager.butcher", "Butcher");
        otherMap.put("entity.minecraft.villager.cartographer", "Cartographer");
        otherMap.put("entity.minecraft.villager.cleric", "Cleric");
        otherMap.put("entity.minecraft.villager.farmer", "Farmer");
        otherMap.put("entity.minecraft.villager.fisherman", "Fisherman");
        otherMap.put("entity.minecraft.villager.fletcher", "Fletcher");
        otherMap.put("entity.minecraft.villager.leatherworker", "Leatherworker");
        otherMap.put("entity.minecraft.villager.librarian", "Librarian");
        otherMap.put("entity.minecraft.villager.mason", "Mason");
        otherMap.put("entity.minecraft.villager.none", "Villager");
        otherMap.put("entity.minecraft.villager.nitwit", "Nitwit");
        otherMap.put("entity.minecraft.villager.shepherd", "Shepherd");
        otherMap.put("entity.minecraft.villager.toolsmith", "Toolsmith");
        otherMap.put("entity.minecraft.villager.weaponsmith", "Weaponsmith");
        otherMap.put("entity.minecraft.villager", "Villager");
        otherMap.put("entity.minecraft.wandering_trader", "Wandering Trader");
        otherMap.put("entity.minecraft.iron_golem", "Iron Golem");
        otherMap.put("entity.minecraft.vindicator", "Vindicator");
        otherMap.put("entity.minecraft.witch", "Witch");
        otherMap.put("entity.minecraft.wither", "Wither");
        otherMap.put("entity.minecraft.wither_skeleton", "Wither Skeleton");
        otherMap.put("entity.minecraft.wither_skull", "Wither Skull");
        otherMap.put("entity.minecraft.wolf", "Wolf");
        otherMap.put("entity.minecraft.experience_bottle", "Thrown Bottle o' Enchanting");
        otherMap.put("entity.minecraft.experience_orb", "Experience Orb");
        otherMap.put("entity.minecraft.zoglin", "Zoglin");
        otherMap.put("entity.minecraft.zombie", "Zombie");
        otherMap.put("entity.minecraft.zombie_horse", "Zombie Horse");
        otherMap.put("entity.minecraft.zombified_piglin", "Zombified Piglin");
        otherMap.put("entity.minecraft.zombie_villager", "Zombie Villager");

        otherTranslations = otherMap;
        
    }


    public static Component checkNullComponent(Component component) {
        return Objects.requireNonNullElseGet(component, () -> Component.text(""));
    }

    public static String serializePlainComponent(Component component) {
        if (component == null) {
            return "";
        } else {
            return PlainTextComponentSerializer.plainText().serialize(component);
        }
    }

    public static String serializeComplexComponent(Component component) {
        if (component == null) {
            return "";
        } else if (component.children().isEmpty()) { //Component has only one item
            return processComplexComponent(component);
        } else { //Component has multiple children to process
            StringBuilder bob = new StringBuilder();
            for (Component child : component.children()) {
                bob.append(processComplexComponent(child));
            }
            return bob.toString();
        }
    }

    private static String processComplexComponent(Component component) {
        if (component instanceof TranslatableComponent) { //Component is a translatable
            List<Component> args = ((TranslatableComponent) component).args();
            String translated = translateComponent(((TranslatableComponent) component).key(), args);
            return Objects.requireNonNullElse(translated, "");
        } else { //Handle as plain text
            return PlainTextComponentSerializer.plainText().serialize(component);
        }
    }

    private static String translateComponent(String key, List<Component> args) {
        if (deathMessageTranslations.containsKey(key)) { //Given key is a translatable death message
            String formatString = deathMessageTranslations.get(key).iterator().next();
            int numArgs = 0; //Assume no args
            if (formatString.matches("%3\\$s")) { //There are 3 arguments
                numArgs = 3;
            } else if (formatString.matches("%2\\$s")) { // 2 args
                numArgs = 2;
            } else if (formatString.matches("%1\\$s")) { // 1 arg
                numArgs = 1;
            }
            if (args.size() < numArgs) {
                return key;
            }
            for (int i = 1; i <= numArgs; i++) { //Loop through args and parse
                String regex = "%" + i + "\\$s";
                formatString = formatString.replaceAll(regex, processComplexComponent(args.get(i)));
            }
            return formatString;
        } else if (otherTranslations.containsKey(key)) { //Given key is a translatable entity or whatever else is included in the list
            return otherTranslations.get(key).iterator().next();

        } else {
            return key; //Key isn't a death message
        }
    }
}
