package de.keksuccino.spiffyhud.customization.elements.singlelinetext;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.spiffyhud.util.ComponentUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SingleLineTextElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    @Nullable
    public String text = null;

    public SingleLineTextElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        if (this.shouldRender() && (this.text != null)) {

            Component c = ComponentUtils.fromJsonOrPlainText(this.text);
            this.baseWidth = Minecraft.getInstance().font.width(c);

            graphics.drawString(Minecraft.getInstance().font, c, this.getAbsoluteX(), this.getAbsoluteY(), DrawableColor.WHITE.getColorIntWithAlpha(this.opacity));

        } else {
            this.baseWidth = 100;
        }

        this.baseHeight = Minecraft.getInstance().font.lineHeight;

    }

}
