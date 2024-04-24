package de.keksuccino.spiffyhud.customization.elements.vanillalike.mounthealth;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.SpiffyUtils;
import de.keksuccino.spiffyhud.util.SizeAndPositionRecorder;
import de.keksuccino.spiffyhud.util.rendering.ElementMobilizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikeMountHealthElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation HEART_VEHICLE_CONTAINER_SPRITE = new ResourceLocation("hud/heart/vehicle_container");
    private static final ResourceLocation HEART_VEHICLE_FULL_SPRITE = new ResourceLocation("hud/heart/vehicle_full");
    private static final ResourceLocation HEART_VEHICLE_HALF_SPRITE = new ResourceLocation("hud/heart/vehicle_half");

    private final Minecraft minecraft = Minecraft.getInstance();
    protected int tickCount;

    private int barWidth = 100;
    private int barHeight = 100;
    private int barOriginalX = 0;
    private int barOriginalY = 0;
    private boolean shouldRenderBar = false;

    @NotNull
    public de.keksuccino.spiffyhud.util.Alignment alignment = de.keksuccino.spiffyhud.util.Alignment.TOP_LEFT;

    public VanillaLikeMountHealthElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        this.tickCount = SpiffyUtils.getGuiAccessor().getTickCount_Spiffy();

        if (this.minecraft.player == null) return;
        if (this.minecraft.level == null) return;

        //Update size and originalPos of bar before render
        this.shouldRenderBar = false;
        this.renderVehicleHealth(graphics);
        this.shouldRenderBar = true;

        int x = this.getAbsoluteX();
        int y = this.getAbsoluteY();
        Integer[] alignedBody = de.keksuccino.spiffyhud.util.Alignment.calculateElementBodyPosition(this.alignment, x, y, this.getAbsoluteWidth(), this.getAbsoluteHeight(), this.barWidth, this.barHeight);
        x = alignedBody[0];
        y = alignedBody[1];

        ElementMobilizer.mobilize(graphics, -this.barOriginalX, -this.barOriginalY, x, y, () -> {

            RenderSystem.enableBlend();
            RenderingUtils.resetShaderColor(graphics);

//            graphics.pose().scale(-1.0F, 1.0F, 1.0F);
//            graphics.pose().translate(-this.barWidth, 0.0F, 0.0F);

            //-------------------------------

            this.renderVehicleHealth(graphics);

            //-------------------------------

            RenderingUtils.resetShaderColor(graphics);

        });

    }

    private void renderVehicleHealth(GuiGraphics graphics) {
        LivingEntity mount = this.getPlayerVehicleWithHealth();
//        if (mount == null) {
//            return;
//        }
        int mountMaxHealth = (mount != null) ? this.getVehicleMaxHearts(mount) : 10; //added NULL check
        int mountHealth = (int)Math.ceil((mount != null) ? mount.getHealth() : 0); //added NULL check
        //Tweak to Vanilla logic
        if (isEditor()) {
            mountMaxHealth = 20;
            mountHealth = 10;
        }
        //---------------------
        if (mountMaxHealth == 0) {
            return;
        }
        int k = 0; //changed to 0
        int l = 0; //changed to 0
        int barY = k;
        int n = 0;
        //Tweak to Vanilla logic
        SizeAndPositionRecorder recorder = new SizeAndPositionRecorder();
        recorder.setWidthOffset(9);
        recorder.setHeightOffset(9);
        //--------------------------
        while (mountMaxHealth > 0) {
            int o = Math.min(mountMaxHealth, 10);
            mountMaxHealth -= o;
            for (int p = 0; p < o; ++p) {
                int barX = l - p * 8 - 9;
                //Tweak to Vanilla logic
                recorder.updateX(barX);
                recorder.updateY(barY);
                //----------------------
                if (this.shouldRenderBar) { //wrapped in IF
                    graphics.blitSprite(HEART_VEHICLE_CONTAINER_SPRITE, barX, barY, 9, 9);
                    if (p * 2 + 1 + n < mountHealth) {
                        graphics.blitSprite(HEART_VEHICLE_FULL_SPRITE, barX, barY, 9, 9);
                    }
                    if (p * 2 + 1 + n != mountHealth) continue;
                    graphics.blitSprite(HEART_VEHICLE_HALF_SPRITE, barX, barY, 9, 9);
                }
            }
            barY -= 10;
            n += 20;
        }
        //Tweak to Vanilla logic
        this.barOriginalX = recorder.getX();
        this.barOriginalY = recorder.getY();
        this.barWidth = recorder.getWidth();
        this.barHeight = recorder.getHeight();
        //--------------------
    }

    private int getVehicleMaxHearts(@Nullable LivingEntity vehicle) {
        if (vehicle == null || !vehicle.showVehicleHealth()) {
            return 0;
        }
        float f = vehicle.getMaxHealth();
        int i = (int)(f + 0.5f) / 2;
        if (i > 30) {
            i = 30;
        }
        return i;
    }

    @Nullable
    private LivingEntity getPlayerVehicleWithHealth() {
        Player player = this.getCameraPlayer();
        if (player != null) {
            Entity entity = player.getVehicle();
            if (entity == null) {
                return null;
            }
            if (entity instanceof LivingEntity) {
                return (LivingEntity)entity;
            }
        }
        return null;
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
        return 100;
    }

    @Override
    public int getAbsoluteHeight() {
        return 30;
    }

}
