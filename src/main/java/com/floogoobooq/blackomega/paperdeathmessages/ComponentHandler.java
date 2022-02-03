package com.floogoobooq.blackomega.paperdeathmessages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ComponentHandler {

    private static final Locale defaultLocale = Locale.ENGLISH;

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

            GlobalTranslator globalTranslator = GlobalTranslator.get();
            MessageFormat translatedFormat = globalTranslator.translate(((TranslatableComponent) component).key(), defaultLocale);
            List<Component> args = ((TranslatableComponent) component).args();
            if (translatedFormat == null) {
                return "";
            } else {
                return translatedFormat.format(args);
            }

        } else { //Handle as plain text
            return PlainTextComponentSerializer.plainText().serialize(component);
        }
    }
}
