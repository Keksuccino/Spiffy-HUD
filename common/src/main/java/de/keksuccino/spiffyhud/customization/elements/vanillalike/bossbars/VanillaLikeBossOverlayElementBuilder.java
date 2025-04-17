package de.keksuccino.spiffyhud.customization.elements.vanillalike.bossbars;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import de.keksuccino.spiffyhud.customization.elements.vanillalike.air.VanillaLikePlayerAirElement;
import de.keksuccino.spiffyhud.util.SpiffyAlignment;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public class VanillaLikeBossOverlayElementBuilder extends ElementBuilder<VanillaLikeBossOverlayElement, VanillaLikeBossOverlayEditorElement> {

    public VanillaLikeBossOverlayElementBuilder() {
        super("spiffy_vanillalike_boss_overlay");
    }

    @Override
    public @NotNull VanillaLikeBossOverlayElement buildDefaultInstance() {
        VanillaLikeBossOverlayElement e = new VanillaLikeBossOverlayElement(this);
        e.stickyAnchor = true;
        e.stayOnScreen = false;
        return e;
    }

    @Override
    public VanillaLikeBossOverlayElement deserializeElement(@NotNull SerializedElement serialized) {

        VanillaLikeBossOverlayElement element = this.buildDefaultInstance();

        String alignment = serialized.getValue("body_alignment");
        if (alignment != null) {
            element.spiffyAlignment = Objects.requireNonNullElse(SpiffyAlignment.getByName(alignment), element.spiffyAlignment);
        }

        return element;

    }

    @Override
    public @Nullable VanillaLikeBossOverlayElement deserializeElementInternal(@NotNull SerializedElement serialized) {
        VanillaLikeBossOverlayElement e = super.deserializeElementInternal(serialized);
        if (e != null) {
            // Fix "Stay on Screen" resetting itself for element types that have it disabled by default
            e.stayOnScreen = this.deserializeBoolean(e.stayOnScreen, serialized.getValue("stay_on_screen"));
        }
        return e;
    }

    @Override
    protected SerializedElement serializeElement(@NotNull VanillaLikeBossOverlayElement element, @NotNull SerializedElement serializeTo) {

        serializeTo.putProperty("body_alignment", element.spiffyAlignment.getName());

        return serializeTo;
        
    }

    @Override
    public @NotNull VanillaLikeBossOverlayEditorElement wrapIntoEditorElement(@NotNull VanillaLikeBossOverlayElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaLikeBossOverlayEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("spiffyhud.elements.vanillalike.boss_bars");
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
