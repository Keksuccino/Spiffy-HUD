package de.keksuccino.spiffyhud.customization.elements.vanillalike.scoreboard;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikeScoreboardElementBuilder extends ElementBuilder<VanillaLikeScoreboardElement, VanillaLikeScoreboardEditorElement> {

    public VanillaLikeScoreboardElementBuilder() {
        super("spiffy_vanillalike_scoreboard_sidebar");
    }

    @Override
    public @NotNull VanillaLikeScoreboardElement buildDefaultInstance() {
        return new VanillaLikeScoreboardElement(this);
    }

    @Override
    public VanillaLikeScoreboardElement deserializeElement(@NotNull SerializedElement serialized) {

        VanillaLikeScoreboardElement element = this.buildDefaultInstance();

//        String hex = serialized.getValue("color");
//        if (hex != null) element.color = DrawableColor.of(hex);

        return element;

    }

    @Override
    protected SerializedElement serializeElement(@NotNull VanillaLikeScoreboardElement element, @NotNull SerializedElement serializeTo) {

//        serializeTo.putProperty("color", element.color.getHex());

        return serializeTo;
        
    }

    @Override
    public @NotNull VanillaLikeScoreboardEditorElement wrapIntoEditorElement(@NotNull VanillaLikeScoreboardElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaLikeScoreboardEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("spiffyhud.elements.vanillalike.scoreboard");
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
