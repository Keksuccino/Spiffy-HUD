package de.keksuccino.spiffyhud.util;

import de.keksuccino.fancymenu.customization.placeholder.PlaceholderParser;
import de.keksuccino.fancymenu.util.rendering.text.Components;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class ComponentUtils {

    @NotNull
    public static Component fromJsonOrPlainText(@NotNull String serializedComponentOrPlainText) {
        serializedComponentOrPlainText = PlaceholderParser.replacePlaceholders(serializedComponentOrPlainText);
        if (!serializedComponentOrPlainText.startsWith("{") && !serializedComponentOrPlainText.startsWith("[")) {
            return Components.literal(serializedComponentOrPlainText);
        } else {
            try {
                Component c = Component.Serializer.fromJson(serializedComponentOrPlainText);
                if (c != null) {
                    return c;
                }
            } catch (Exception ignore) {}
        }
        return Components.literal(serializedComponentOrPlainText);
    }

    @NotNull
    public static String toJson(@NotNull Component component) {
        return Component.Serializer.toJson(Objects.requireNonNull(component));
    }

}
