package de.keksuccino.spiffyhud.customization.elements.chatcustomizer;

import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayerHandler;
import de.keksuccino.fancymenu.util.enums.LocalizedCycleEnum;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChatCustomizerHandler {

    public static ChatCorner chatCorner = ChatCorner.BOTTOM_LEFT;
    public static DrawableColor chatBackgroundColor = null;
    public static DrawableColor inputBackgroundColor = null;

    public static void tick() {

        // Reset all before updating
        chatCorner = ChatCorner.BOTTOM_LEFT;
        chatBackgroundColor = null;
        inputBackgroundColor = null;

        ScreenCustomizationLayer layer = ScreenCustomizationLayerHandler.getLayerOfScreen(SpiffyOverlayScreen.class);
        if (layer != null) {
            layer.allElements.forEach(abstractElement -> {
                if (abstractElement instanceof ChatCustomizerElement e) {
                    if (e.shouldRender()) {

                        if (chatCorner == ChatCorner.BOTTOM_LEFT) {
                            chatCorner = e.chatCorner;
                        }
                        if (chatBackgroundColor == null) {
                            chatBackgroundColor = e.getCustomChatBackgroundColor();
                        }
                        if (inputBackgroundColor == null) {
                            inputBackgroundColor = e.getCustomInputBackgroundColor();
                        }

                    }
                }
            });
        }

    }

    /**
     * Checks if chat is positioned on the right side
     */
    public static boolean isChatRightAligned() {
        return chatCorner == ChatCorner.BOTTOM_RIGHT;
    }

    public enum ChatCorner implements LocalizedCycleEnum<ChatCorner> {

        BOTTOM_LEFT("bottom_left"),  // Default Minecraft position
        BOTTOM_RIGHT("bottom_right");

        private final String name;

        ChatCorner(@NotNull String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getLocalizationKeyBase() {
            return "spiffyhud.elements.chat_customizer.corner";
        }

        @Override
        public @NotNull String getName() {
            return this.name;
        }

        @Override
        public @NotNull Style getValueComponentStyle() {
            return WARNING_TEXT_STYLE.get();
        }

        @Override
        public @NotNull ChatCorner[] getValues() {
            return ChatCorner.values();
        }

        @Override
        public @Nullable ChatCorner getByNameInternal(@NotNull String s) {
            return getByName(s);
        }

        @Nullable
        public static ChatCorner getByName(@NotNull String name) {
            for (ChatCorner corner : ChatCorner.values()) {
                if (corner.name.equals(name)) return corner;
            }
            return null;
        }

    }

}