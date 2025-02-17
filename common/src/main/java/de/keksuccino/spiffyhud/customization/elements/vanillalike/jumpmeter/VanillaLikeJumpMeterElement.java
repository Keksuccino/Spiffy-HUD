package de.keksuccino.spiffyhud.customization.elements.vanillalike.jumpmeter;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.util.rendering.ElementMobilizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class VanillaLikeJumpMeterElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    private final Minecraft minecraft = Minecraft.getInstance();

    public VanillaLikeJumpMeterElement(@NotNull ElementBuilder<?, ?> builder) {
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

            this.renderJumpMeter(graphics);

            //-------------------------------

            RenderingUtils.resetShaderColor(graphics);

        });

    }

    private void renderJumpMeter(GuiGraphics graphics) {
        PlayerRideableJumping rideable = Objects.requireNonNull(this.minecraft.player).jumpableVehicle();
        int x = getScreenWidth() / 2 - 91;
        float f = this.minecraft.player.getJumpRidingScale();
        int j = (int)(f * 183.0f);
        int k = getScreenHeight() - 32 + 3;
        graphics.blit(GUI_ICONS_LOCATION, x, k, 0, 84, 182, 5);
        if ((rideable != null) && (rideable.getJumpCooldown() > 0)) {
            graphics.blit(GUI_ICONS_LOCATION, x, k, 0, 74, 182, 5);
        } else if (j > 0) {
            graphics.blit(GUI_ICONS_LOCATION, x, k, 0, 89, j, 5);
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
