package de.keksuccino.spiffyhud.customization.elements.vanillalike.experience;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.util.rendering.ElementMobilizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikeExperienceElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    private final Minecraft minecraft = Minecraft.getInstance();

    public VanillaLikeExperienceElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        if (this.minecraft.player == null) return;
        if (this.minecraft.level == null) return;

        int x = this.getAbsoluteX();
        int y = this.getAbsoluteY();

        ElementMobilizer.mobilize(graphics, -(getScreenWidth() / 2 - 91), -(getScreenHeight() - 32 + 3), x, y, () -> {

            RenderSystem.enableBlend();
            RenderingUtils.resetShaderColor(graphics);

            //-------------------------------

            this.renderExperienceBar(graphics);

            //-------------------------------

            RenderingUtils.resetShaderColor(graphics);

        });

    }

    public void renderExperienceBar(GuiGraphics graphics) {
        int barX = getScreenWidth() / 2 - 91;
        int barY = getScreenHeight() - 32 + 3;
        int k;
        int i = this.minecraft.player.getXpNeededForNextLevel();
        if (i > 0) {
            k = (int)(this.minecraft.player.experienceProgress * 183.0f);
            graphics.blit(GUI_ICONS_LOCATION, barX, barY, 0, 64, 182, 5);
            if (k > 0) {
                graphics.blit(GUI_ICONS_LOCATION, barX, barY, 0, 69, k, 5);
            }
        }
        if (this.minecraft.player.experienceLevel > 0) {
            String string = "" + this.minecraft.player.experienceLevel;
            k = (getScreenWidth() - this.getFont().width(string)) / 2;
            barY = getScreenHeight() - 31 - 4;
            graphics.drawString(this.getFont(), string, k + 1, barY, 0, false);
            graphics.drawString(this.getFont(), string, k - 1, barY, 0, false);
            graphics.drawString(this.getFont(), string, k, barY + 1, 0, false);
            graphics.drawString(this.getFont(), string, k, barY - 1, 0, false);
            graphics.drawString(this.getFont(), string, k, barY, 8453920, false);
        }
    }

    private Font getFont() {
        return Minecraft.getInstance().font;
    }

    @Nullable
    private Player getCameraPlayer() {
        return (Minecraft.getInstance().getCameraEntity() instanceof Player p) ? p : null;
    }

    @Override
    public int getAbsoluteWidth() {
        return 182;
    }

    @Override
    public int getAbsoluteHeight() {
        return 5;
    }

}
