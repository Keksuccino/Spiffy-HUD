package de.keksuccino.spiffyhud.customization.elements.vanillalike.hotbar;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import de.keksuccino.spiffyhud.customization.elements.vanillalike.air.VanillaLikePlayerAirElement;
import de.keksuccino.fancymenu.util.rendering.text.Components;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikeHotbarElementBuilder extends ElementBuilder<VanillaLikeHotbarElement, VanillaLikeHotbarEditorElement> {

    public VanillaLikeHotbarElementBuilder() {
        super("spiffy_vanillalike_hotbar");
    }

    @Override
    public @NotNull VanillaLikeHotbarElement buildDefaultInstance() {
        VanillaLikeHotbarElement e = new VanillaLikeHotbarElement(this);
        e.stickyAnchor = true;
        e.stayOnScreen = false;
        return e;
    }

    @Override
    public VanillaLikeHotbarElement deserializeElement(@NotNull SerializedElement serialized) {

        VanillaLikeHotbarElement element = this.buildDefaultInstance();

//        String hex = serialized.getValue("color");
//        if (hex != null) element.color = DrawableColor.of(hex);

        return element;

    }

    @Override
    public @Nullable VanillaLikeHotbarElement deserializeElementInternal(@NotNull SerializedElement serialized) {
        VanillaLikeHotbarElement e = super.deserializeElementInternal(serialized);
        if (e != null) {
            // Fix "Stay on Screen" resetting itself for element types that have it disabled by default
            e.stayOnScreen = this.deserializeBoolean(e.stayOnScreen, serialized.getValue("stay_on_screen"));
        }
        return e;
    }

    @Override
    protected SerializedElement serializeElement(@NotNull VanillaLikeHotbarElement element, @NotNull SerializedElement serializeTo) {

//        serializeTo.putProperty("color", element.color.getHex());

        return serializeTo;
        
    }

    @Override
    public @NotNull VanillaLikeHotbarEditorElement wrapIntoEditorElement(@NotNull VanillaLikeHotbarElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaLikeHotbarEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Components.translatable("spiffyhud.elements.vanillalike.hotbar");
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
