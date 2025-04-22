package de.keksuccino.spiffyhud.customization.elements.vanillalike.attackindicator;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import de.keksuccino.fancymenu.util.rendering.text.Components;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikeAttackIndicatorElementBuilder extends ElementBuilder<VanillaLikeAttackIndicatorElement, VanillaLikeAttackIndicatorEditorElement> {

    public VanillaLikeAttackIndicatorElementBuilder() {
        super("spiffy_vanillalike_attack_indicator");
    }

    @Override
    public @NotNull VanillaLikeAttackIndicatorElement buildDefaultInstance() {
        VanillaLikeAttackIndicatorElement e = new VanillaLikeAttackIndicatorElement(this);
        e.baseHeight = 4 * 4;
        e.baseWidth = 16 * 4;
        e.stickyAnchor = true;
        e.stayOnScreen = false;
        return e;
    }

    @Override
    public VanillaLikeAttackIndicatorElement deserializeElement(@NotNull SerializedElement serialized) {

        VanillaLikeAttackIndicatorElement element = this.buildDefaultInstance();

        element.isHotbar = this.deserializeBoolean(element.isHotbar, serialized.getValue("is_hotbar"));

        return element;

    }

    @Override
    public @Nullable VanillaLikeAttackIndicatorElement deserializeElementInternal(@NotNull SerializedElement serialized) {
        VanillaLikeAttackIndicatorElement e = super.deserializeElementInternal(serialized);
        if (e != null) {
            // Fix "Stay on Screen" resetting itself for element types that have it disabled by default
            e.stayOnScreen = this.deserializeBoolean(e.stayOnScreen, serialized.getValue("stay_on_screen"));
        }
        return e;
    }

    @Override
    protected SerializedElement serializeElement(@NotNull VanillaLikeAttackIndicatorElement element, @NotNull SerializedElement serializeTo) {

        serializeTo.putProperty("is_hotbar", "" + element.isHotbar);

        return serializeTo;
        
    }

    @Override
    public @NotNull VanillaLikeAttackIndicatorEditorElement wrapIntoEditorElement(@NotNull VanillaLikeAttackIndicatorElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaLikeAttackIndicatorEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Components.translatable("spiffyhud.elements.vanillalike.attack_indicator");
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
