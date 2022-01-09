package de.keksuccino.spiffyhud.customization.items.custombars;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CustomMountHealthBarCustomizationItem extends CustomBarCustomizationItemBase {

    protected int currentPercentWidthHeight = 0;

    public boolean hideWhenFull = false;

    public CustomMountHealthBarCustomizationItem(PropertiesSection item) {
        super(item);

        this.value = "Custom Mount Health Bar";

        String hideFullString = item.getEntryValue("hidewhenfull");
        if (hideFullString != null) {
            if (hideFullString.equalsIgnoreCase("true")) {
                this.hideWhenFull = true;
            }
        }

    }

    @Override
    public void init(PropertiesSection item) {

        super.init(item);

        boolean b = false;
        if (this.barColorHex == null) {
            this.barColorHex = "#fc5142";
            b = true;
        }
        if (this.backgroundColorHex == null) {
            this.backgroundColorHex = "#5c5c5c80";
            b = true;
        }
        if (b) {
            this.updateItem();
        }

    }

    @Override
    public void render(PoseStack matrix) {

        if (!this.shouldRender()) {
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if ((player != null) || this.isEditor()) {
            Entity eTemp = player.getVehicle();
            if (((eTemp != null) && (eTemp instanceof LivingEntity)) || this.isEditor()) {
                LivingEntity e = null;
                if (!this.isEditor()) {
                    e = (LivingEntity) eTemp;
                }
                float healthPercent = 50.0F;
                if (!this.isEditor()) {
                    healthPercent = ((e.getHealth() / e.getMaxHealth()) * 100.0F);
                }
                if (healthPercent >= 100) {
                    if (this.hideWhenFull) {
                        return;
                    }
                }
                if ((this.direction == BarDirection.LEFT) || (this.direction == BarDirection.RIGHT)) {
                    this.currentPercentWidthHeight = (int)((((float)this.width) / 100.0F) * healthPercent);
                }
                if ((this.direction == BarDirection.UP) || (this.direction == BarDirection.DOWN)) {
                    this.currentPercentWidthHeight = (int)((((float)this.height) / 100.0F) * healthPercent);
                }

                this.renderBarBackground(matrix);

                this.renderBar(matrix);
            }
        }

    }

    @Override
    protected void renderBar(PoseStack matrix) {

        if (this.barTexture == null) {

            if (this.direction == BarDirection.RIGHT) {
                RenderUtils.fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.currentPercentWidthHeight, this.getPosY() + this.height, this.barColor.getRGB(), 1.0F);
            }
            if (this.direction == BarDirection.LEFT) {
                RenderUtils.fill(matrix, this.getPosX() + this.width - this.currentPercentWidthHeight, this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, this.barColor.getRGB(), 1.0F);
            }
            if (this.direction == BarDirection.UP) {
                RenderUtils.fill(matrix, this.getPosX(), this.getPosY() + this.height - this.currentPercentWidthHeight, this.getPosX() + this.width, this.getPosY() + this.height, this.barColor.getRGB(), 1.0F);
            }
            if (this.direction == BarDirection.DOWN) {
                RenderUtils.fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.currentPercentWidthHeight, this.barColor.getRGB(), 1.0F);
            }

        } else {

            RenderUtils.bindTexture(this.barTexture);
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.direction == BarDirection.RIGHT) {
                blit(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.currentPercentWidthHeight, this.height, this.width, this.height);
            }
            if (this.direction == BarDirection.LEFT) {
                int i = (this.width - this.currentPercentWidthHeight);
                blit(matrix, this.getPosX() + i, this.getPosY(), i, 0.0F, this.currentPercentWidthHeight, this.height, this.width, this.height);
            }
            if (this.direction == BarDirection.UP) {
                int i = (this.height - this.currentPercentWidthHeight);
                blit(matrix, this.getPosX(), this.getPosY() + i, 0.0F, i, this.width, this.currentPercentWidthHeight, this.width, this.height);
            }
            if (this.direction == BarDirection.DOWN) {
                blit(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.width, this.currentPercentWidthHeight, this.width, this.height);
            }

            RenderSystem.disableBlend();

        }

    }

    @Override
    protected void renderBarBackground(PoseStack matrix) {

        if (this.backgroundTexture == null) {

            RenderUtils.fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, this.backgroundColor.getRGB(), 1.0F);

        } else {

            RenderUtils.bindTexture(this.backgroundTexture);
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            blit(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.width, this.height, this.width, this.height);
            RenderSystem.disableBlend();

        }

    }

}
