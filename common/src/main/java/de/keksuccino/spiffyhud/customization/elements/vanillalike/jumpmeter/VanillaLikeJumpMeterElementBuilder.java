package de.keksuccino.spiffyhud.customization.elements.vanillalike.jumpmeter;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import de.keksuccino.spiffyhud.customization.elements.vanillalike.air.VanillaLikePlayerAirElement;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikeJumpMeterElementBuilder extends ElementBuilder<VanillaLikeJumpMeterElement, VanillaLikeJumpMeterEditorElement> {

    public VanillaLikeJumpMeterElementBuilder() {
        super("spiffy_vanillalike_jump_meter");
    }

    @Override
    public @NotNull VanillaLikeJumpMeterElement buildDefaultInstance() {
        VanillaLikeJumpMeterElement e = new VanillaLikeJumpMeterElement(this);
        e.stickyAnchor = true;
        e.stayOnScreen = false;
        return e;
    }

    @Override
    public VanillaLikeJumpMeterElement deserializeElement(@NotNull SerializedElement serialized) {

        VanillaLikeJumpMeterElement element = this.buildDefaultInstance();

        return element;

    }

    @Override
    public @Nullable VanillaLikeJumpMeterElement deserializeElementInternal(@NotNull SerializedElement serialized) {
        VanillaLikeJumpMeterElement e = super.deserializeElementInternal(serialized);
        if (e != null) {
            // Fix "Stay on Screen" resetting itself for element types that have it disabled by default
            e.stayOnScreen = this.deserializeBoolean(e.stayOnScreen, serialized.getValue("stay_on_screen"));
        }
        return e;
    }

    @Override
    protected SerializedElement serializeElement(@NotNull VanillaLikeJumpMeterElement element, @NotNull SerializedElement serializeTo) {

        return serializeTo;
        
    }

    @Override
    public @NotNull VanillaLikeJumpMeterEditorElement wrapIntoEditorElement(@NotNull VanillaLikeJumpMeterElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaLikeJumpMeterEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("spiffyhud.elements.vanillalike.jump_meter");
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
