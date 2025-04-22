package de.keksuccino.spiffyhud.customization.elements.chatcustomizer;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.editor.AbstractEditorElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.text.Components;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import org.jetbrains.annotations.NotNull;

public class ChatCustomizerEditorElement extends AbstractEditorElement {

    public ChatCustomizerEditorElement(@NotNull AbstractElement element, @NotNull LayoutEditorScreen editor) {

        super(element, editor);

        this.settings.setInEditorColorSupported(true);
        this.settings.setParallaxAllowed(false);

    }

    @Override
    public void init() {

        super.init();

        this.addStringInputContextMenuEntryTo(this.rightClickMenu, "chat_background_color", ChatCustomizerEditorElement.class,
                chatCustomizerEditorElement -> chatCustomizerEditorElement.getElement().customChatBackgroundColor,
                (chatCustomizerEditorElement, s) -> chatCustomizerEditorElement.getElement().customChatBackgroundColor = s,
                null, false, true, Components.translatable("spiffyhud.elements.chat_customizer.chat_background_color"), true, null, null, null);

        this.addStringInputContextMenuEntryTo(this.rightClickMenu, "input_background_color", ChatCustomizerEditorElement.class,
                chatCustomizerEditorElement -> chatCustomizerEditorElement.getElement().customInputBackgroundColor,
                (chatCustomizerEditorElement, s) -> chatCustomizerEditorElement.getElement().customInputBackgroundColor = s,
                null, false, true, Components.translatable("spiffyhud.elements.chat_customizer.input_background_color"), true, null, null, null);

        this.rightClickMenu.addValueCycleEntry("screen_corner", ChatCustomizerHandler.ChatCorner.BOTTOM_LEFT.cycle(this.getElement().chatCorner)
                        .addCycleListener(type -> {
                            editor.history.saveSnapshot();
                            this.getElement().chatCorner = type;
                        }))
                .setStackable(false)
                .setTooltipSupplier((contextMenu, contextMenuEntry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("spiffyhud.elements.chat_customizer.corner.desc")));

    }

    public ChatCustomizerElement getElement() {
        return (ChatCustomizerElement) this.element;
    }

}
