package de.keksuccino.spiffyhud.customization.elements.vanillalike.experience;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikeExperienceElementBuilder extends ElementBuilder<VanillaLikeExperienceElement, VanillaLikeExperienceEditorElement> {

    public VanillaLikeExperienceElementBuilder() {
        super("spiffy_vanillalike_player_exp_bar");
    }

    @Override
    public @NotNull VanillaLikeExperienceElement buildDefaultInstance() {
        return new VanillaLikeExperienceElement(this);
    }

    @Override
    public VanillaLikeExperienceElement deserializeElement(@NotNull SerializedElement serialized) {

        VanillaLikeExperienceElement element = this.buildDefaultInstance();

        return element;

    }

    @Override
    protected SerializedElement serializeElement(@NotNull VanillaLikeExperienceElement element, @NotNull SerializedElement serializeTo) {

        return serializeTo;
        
    }

    @Override
    public @NotNull VanillaLikeExperienceEditorElement wrapIntoEditorElement(@NotNull VanillaLikeExperienceElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaLikeExperienceEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("spiffyhud.elements.vanillalike.player_experience");
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
