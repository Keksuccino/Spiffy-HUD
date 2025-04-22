package de.keksuccino.spiffyhud.customization.elements.vanillalike.scoreboard;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import de.keksuccino.spiffyhud.customization.elements.vanillalike.air.VanillaLikePlayerAirElement;
import de.keksuccino.spiffyhud.util.SpiffyAlignment;
import de.keksuccino.fancymenu.util.rendering.text.Components;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public class VanillaLikeScoreboardElementBuilder extends ElementBuilder<VanillaLikeScoreboardElement, VanillaLikeScoreboardEditorElement> {

    public VanillaLikeScoreboardElementBuilder() {
        super("spiffy_vanillalike_scoreboard_sidebar");
    }

    @Override
    public @NotNull VanillaLikeScoreboardElement buildDefaultInstance() {
        VanillaLikeScoreboardElement e = new VanillaLikeScoreboardElement(this);
        e.stickyAnchor = true;
        e.stayOnScreen = false;
        return e;
    }

    @Override
    public VanillaLikeScoreboardElement deserializeElement(@NotNull SerializedElement serialized) {

        VanillaLikeScoreboardElement element = this.buildDefaultInstance();

        String alignment = serialized.getValue("body_alignment");
        if (alignment != null) {
            element.spiffyAlignment = Objects.requireNonNullElse(SpiffyAlignment.getByName(alignment), element.spiffyAlignment);
        }

        String titleBackColor = serialized.getValue("title_background_color");
        if (titleBackColor != null) element.customTitleBackgroundColor = DrawableColor.of(titleBackColor);

        String linesBackColor = serialized.getValue("lines_background_color");
        if (linesBackColor != null) element.customLineBackgroundColor = DrawableColor.of(linesBackColor);

        return element;

    }

    @Override
    public @Nullable VanillaLikeScoreboardElement deserializeElementInternal(@NotNull SerializedElement serialized) {
        VanillaLikeScoreboardElement e = super.deserializeElementInternal(serialized);
        if (e != null) {
            // Fix "Stay on Screen" resetting itself for element types that have it disabled by default
            e.stayOnScreen = this.deserializeBoolean(e.stayOnScreen, serialized.getValue("stay_on_screen"));
        }
        return e;
    }

    @Override
    protected SerializedElement serializeElement(@NotNull VanillaLikeScoreboardElement element, @NotNull SerializedElement serializeTo) {

        serializeTo.putProperty("body_alignment", element.spiffyAlignment.getName());
        if (element.customTitleBackgroundColor != null) serializeTo.putProperty("title_background_color", element.customTitleBackgroundColor.getHex());
        if (element.customLineBackgroundColor != null) serializeTo.putProperty("lines_background_color", element.customLineBackgroundColor.getHex());

        return serializeTo;
        
    }

    @Override
    public @NotNull VanillaLikeScoreboardEditorElement wrapIntoEditorElement(@NotNull VanillaLikeScoreboardElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaLikeScoreboardEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Components.translatable("spiffyhud.elements.vanillalike.scoreboard");
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
