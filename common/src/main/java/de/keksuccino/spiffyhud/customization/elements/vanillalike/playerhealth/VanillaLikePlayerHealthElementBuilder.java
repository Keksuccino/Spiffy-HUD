package de.keksuccino.spiffyhud.customization.elements.vanillalike.playerhealth;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import de.keksuccino.spiffyhud.util.SpiffyAlignment;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class VanillaLikePlayerHealthElementBuilder extends ElementBuilder<VanillaLikePlayerHealthElement, VanillaLikePlayerHealthEditorElement> {

    public VanillaLikePlayerHealthElementBuilder() {
        super("spiffy_vanillalike_player_health_bar");
    }

    @Override
    public @NotNull VanillaLikePlayerHealthElement buildDefaultInstance() {
        return new VanillaLikePlayerHealthElement(this);
    }

    @Override
    public VanillaLikePlayerHealthElement deserializeElement(@NotNull SerializedElement serialized) {

        VanillaLikePlayerHealthElement element = this.buildDefaultInstance();

        String alignment = serialized.getValue("body_alignment");
        if (alignment != null) {
            element.spiffyAlignment = Objects.requireNonNullElse(SpiffyAlignment.getByName(alignment), element.spiffyAlignment);
        }

        return element;

    }

    @Override
    protected SerializedElement serializeElement(@NotNull VanillaLikePlayerHealthElement element, @NotNull SerializedElement serializeTo) {

        serializeTo.putProperty("body_alignment", element.spiffyAlignment.getName());

        return serializeTo;
        
    }

    @Override
    public @NotNull VanillaLikePlayerHealthEditorElement wrapIntoEditorElement(@NotNull VanillaLikePlayerHealthElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaLikePlayerHealthEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("spiffyhud.elements.vanillalike.player_health");
    }

    @Override
    public @Nullable Component[] getDescription(@Nullable AbstractElement element) {
        return null;
    }

    @Override
    public boolean shouldShowUpInEditorElementMenu(@NotNull LayoutEditorScreen editor) {
        return (editor.layoutTargetScreen instanceof SpiffyOverlayScreen);
    }

}
