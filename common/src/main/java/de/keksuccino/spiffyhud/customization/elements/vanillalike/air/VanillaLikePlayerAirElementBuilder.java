package de.keksuccino.spiffyhud.customization.elements.vanillalike.air;

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

public class VanillaLikePlayerAirElementBuilder extends ElementBuilder<VanillaLikePlayerAirElement, VanillaLikePlayerAirEditorElement> {

    public VanillaLikePlayerAirElementBuilder() {
        super("spiffy_vanillalike_player_air_bar");
    }

    @Override
    public @NotNull VanillaLikePlayerAirElement buildDefaultInstance() {
        VanillaLikePlayerAirElement e = new VanillaLikePlayerAirElement(this);
        e.stickyAnchor = true;
        e.stayOnScreen = false;
        return e;
    }

    @Override
    public VanillaLikePlayerAirElement deserializeElement(@NotNull SerializedElement serialized) {

        VanillaLikePlayerAirElement element = this.buildDefaultInstance();

        String alignment = serialized.getValue("body_alignment");
        if (alignment != null) {
            element.spiffyAlignment = Objects.requireNonNullElse(SpiffyAlignment.getByName(alignment), element.spiffyAlignment);
        }

        return element;

    }

    @Override
    protected SerializedElement serializeElement(@NotNull VanillaLikePlayerAirElement element, @NotNull SerializedElement serializeTo) {

        serializeTo.putProperty("body_alignment", element.spiffyAlignment.getName());

        return serializeTo;
        
    }

    @Override
    public @NotNull VanillaLikePlayerAirEditorElement wrapIntoEditorElement(@NotNull VanillaLikePlayerAirElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaLikePlayerAirEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("spiffyhud.elements.vanillalike.player_air");
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
