package de.keksuccino.spiffyhud.customization.elements.chatcustomizer;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderParser;
import de.keksuccino.fancymenu.util.MathUtils;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public class ChatCustomizerElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    @Nullable
    public String customChatBackgroundColor = null;
    @Nullable
    public String customInputBackgroundColor = null;
    @NotNull
    public ChatCustomizerHandler.ChatCorner chatCorner = ChatCustomizerHandler.ChatCorner.BOTTOM_LEFT;

    protected String lastCustomChatBackgroundColor = null;
    protected String lastCustomInputBackgroundColor = null;
    protected DrawableColor cachedCustomChatBackgroundColor = null;
    protected DrawableColor cachedCustomInputBackgroundColor = null;

    public ChatCustomizerElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Nullable
    public DrawableColor getCustomChatBackgroundColor() {
        // Update cached custom chat background color
        if (this.customChatBackgroundColor != null) {
            String parsedCustomChatBackgroundColor = PlaceholderParser.replacePlaceholders(this.customChatBackgroundColor);
            if (!Objects.equals(parsedCustomChatBackgroundColor, this.lastCustomChatBackgroundColor)) {
                this.cachedCustomChatBackgroundColor = DrawableColor.of(parsedCustomChatBackgroundColor);
            }
            this.lastCustomChatBackgroundColor = parsedCustomChatBackgroundColor;
        } else {
            this.lastCustomChatBackgroundColor = null;
            this.cachedCustomChatBackgroundColor = null;
        }
        return this.cachedCustomChatBackgroundColor;
    }

    @Nullable
    public DrawableColor getCustomInputBackgroundColor() {
        // Update cached custom input background color
        if (this.customInputBackgroundColor != null) {
            String parsedCustomInputBackgroundColor = PlaceholderParser.replacePlaceholders(this.customInputBackgroundColor);
            if (!Objects.equals(parsedCustomInputBackgroundColor, this.lastCustomInputBackgroundColor)) {
                this.cachedCustomInputBackgroundColor = DrawableColor.of(parsedCustomInputBackgroundColor);
            }
            this.lastCustomInputBackgroundColor = parsedCustomInputBackgroundColor;
        } else {
            this.lastCustomInputBackgroundColor = null;
            this.cachedCustomInputBackgroundColor = null;
        }
        return this.cachedCustomInputBackgroundColor;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        if (isEditor()) {
            int x = this.getAbsoluteX();
            int y = this.getAbsoluteY();
            int w = this.getAbsoluteWidth();
            int h = this.getAbsoluteHeight();
            RenderSystem.enableBlend();
            graphics.fill(x, y, x + w, y + h, this.inEditorColor.getColorInt());
            graphics.enableScissor(x, y, x + w, y + h);
            graphics.drawCenteredString(Minecraft.getInstance().font, this.getDisplayName(), x + (w / 2), y + (h / 2) - (Minecraft.getInstance().font.lineHeight / 2), -1);
            graphics.disableScissor();
            RenderingUtils.resetShaderColor(graphics);
        }

    }

}
