package de.keksuccino.spiffyhud.customization.elements.vanillalike.mounthealth;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import de.keksuccino.spiffyhud.util.Alignment;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class VanillaLikeMountHealthElementBuilder extends ElementBuilder<VanillaLikeMountHealthElement, VanillaLikeMountHealthEditorElement> {

    public VanillaLikeMountHealthElementBuilder() {
        super("spiffy_vanillalike_mount_health_bar");
    }

    @Override
    public @NotNull VanillaLikeMountHealthElement buildDefaultInstance() {
        return new VanillaLikeMountHealthElement(this);
    }

    @Override
    public VanillaLikeMountHealthElement deserializeElement(@NotNull SerializedElement serialized) {

        VanillaLikeMountHealthElement element = this.buildDefaultInstance();

        String alignment = serialized.getValue("body_alignment");
        if (alignment != null) {
            element.alignment = Objects.requireNonNullElse(Alignment.getByName(alignment), element.alignment);
        }

        return element;

    }

    @Override
    protected SerializedElement serializeElement(@NotNull VanillaLikeMountHealthElement element, @NotNull SerializedElement serializeTo) {

        serializeTo.putProperty("body_alignment", element.alignment.getName());

        return serializeTo;
        
    }

    @Override
    public @NotNull VanillaLikeMountHealthEditorElement wrapIntoEditorElement(@NotNull VanillaLikeMountHealthElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaLikeMountHealthEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("spiffyhud.elements.vanillalike.mount_health");
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
