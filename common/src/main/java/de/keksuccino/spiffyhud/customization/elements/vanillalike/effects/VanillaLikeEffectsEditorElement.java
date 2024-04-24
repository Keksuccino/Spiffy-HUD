package de.keksuccino.spiffyhud.customization.elements.vanillalike.effects;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.editor.AbstractEditorElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.util.Alignment;
import org.jetbrains.annotations.NotNull;

public class VanillaLikeEffectsEditorElement extends AbstractEditorElement {

    public VanillaLikeEffectsEditorElement(@NotNull AbstractElement element, @NotNull LayoutEditorScreen editor) {
        super(element, editor);
        this.settings.setStretchable(false);
        this.settings.setAdvancedSizingSupported(false);
        this.settings.setResizeable(false);
    }

    @Override
    public void init() {

        super.init();

        this.rightClickMenu.addValueCycleEntry("body_alignment", Alignment.TOP_LEFT.cycle(this.getElement().alignment)
                        .addCycleListener(alignment -> {
                            editor.history.saveSnapshot();
                            this.getElement().alignment = alignment;
                        }))
                .setStackable(false);

    }

    public VanillaLikeEffectsElement getElement() {
        return (VanillaLikeEffectsElement) this.element;
    }

}
