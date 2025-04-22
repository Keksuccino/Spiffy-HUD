package de.keksuccino.spiffyhud.customization.elements.singlelinetext;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.editor.AbstractEditorElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.spiffyhud.customization.elements.chatcustomizer.ChatCustomizerHandler;
import de.keksuccino.fancymenu.util.rendering.text.Components;
import org.jetbrains.annotations.NotNull;

public class SingleLineTextEditorElement extends AbstractEditorElement {

    public SingleLineTextEditorElement(@NotNull AbstractElement element, @NotNull LayoutEditorScreen editor) {

        super(element, editor);

        this.settings.setParallaxAllowed(false);
        this.settings.setResizeable(false);
        this.settings.setAdvancedSizingSupported(false);
        this.settings.setStretchable(false);
        this.settings.setAutoSizingAllowed(false);

    }

    @Override
    public void init() {

        super.init();

        this.addStringInputContextMenuEntryTo(this.rightClickMenu, "text", SingleLineTextEditorElement.class,
                chatCustomizerEditorElement -> chatCustomizerEditorElement.getElement().text,
                (chatCustomizerEditorElement, s) -> chatCustomizerEditorElement.getElement().text = s,
                null, false, true, Components.translatable("spiffyhud.elements.single_line_text.text"), true, null, null, null);

    }

    public SingleLineTextElement getElement() {
        return (SingleLineTextElement) this.element;
    }

}
