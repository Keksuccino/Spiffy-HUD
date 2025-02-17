package de.keksuccino.spiffyhud.customization.elements.vanillalike.armor;

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

public class VanillaLikePlayerArmorElementBuilder extends ElementBuilder<VanillaLikePlayerArmorElement, VanillaLikePlayerArmorEditorElement> {

    public VanillaLikePlayerArmorElementBuilder() {
        super("spiffy_vanillalike_player_armor_bar");
    }

    @Override
    public @NotNull VanillaLikePlayerArmorElement buildDefaultInstance() {
        return new VanillaLikePlayerArmorElement(this);
    }

    @Override
    public VanillaLikePlayerArmorElement deserializeElement(@NotNull SerializedElement serialized) {

        VanillaLikePlayerArmorElement element = this.buildDefaultInstance();

        String alignment = serialized.getValue("body_alignment");
        if (alignment != null) {
            element.spiffyAlignment = Objects.requireNonNullElse(SpiffyAlignment.getByName(alignment), element.spiffyAlignment);
        }

        return element;

    }

    @Override
    protected SerializedElement serializeElement(@NotNull VanillaLikePlayerArmorElement element, @NotNull SerializedElement serializeTo) {

        serializeTo.putProperty("body_alignment", element.spiffyAlignment.getName());

        return serializeTo;
        
    }

    @Override
    public @NotNull VanillaLikePlayerArmorEditorElement wrapIntoEditorElement(@NotNull VanillaLikePlayerArmorElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaLikePlayerArmorEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("spiffyhud.elements.vanillalike.player_armor");
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
