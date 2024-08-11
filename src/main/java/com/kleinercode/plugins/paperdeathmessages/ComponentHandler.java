package com.kleinercode.plugins.paperdeathmessages;

import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ComponentHandler {

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

    public static String serializeComplexComponent(Component component, Locale locale) {
        if (component == null) {
            return "";
        } else if (component.children().isEmpty()) { //Component has only one item
            return processComplexComponent(component, locale);
        } else { //Component has multiple children to process
            StringBuilder bob = new StringBuilder();
            for (Component child : component.children()) {
                bob.append(processComplexComponent(child, locale));
            }
            return bob.toString();
        }
    }

    private static String processComplexComponent(Component component, Locale locale) {
        if (component instanceof TranslatableComponent) { // Component is a translatable
            return serializePlainComponent(GlobalTranslator.render(component, locale));
        } else { //Handle as plain text
            return PlainTextComponentSerializer.plainText().serialize(component);
        }
    }

}
