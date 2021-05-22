package de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class AirBarHudElement extends IngameHudElement {
	
	public BarAlignment alignment = BarAlignment.RIGHT;
	
	public AirBarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = 80;
		this.height = 9;
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		if (this.fireEvents) {
			if (this.handler.pre(ElementType.AIR, matrix, false)) return;
		}
		
		if (this.visible) {
			this.renderAirRaw(matrix);
		}
		
		if (this.fireEvents) {
			this.handler.post(ElementType.AIR, matrix, false);
		}
		
	}
	
	protected void renderAirRaw(MatrixStack matrix) {
		
        mc.getProfiler().startSection("air");
        PlayerEntity player = (PlayerEntity)this.mc.getRenderViewEntity();
        RenderSystem.enableBlend();
        int left = this.x + 90;
        if (this.alignment == BarAlignment.LEFT) {
			left = this.x + 18;
		}
        int top = this.y;

        int air = 150;
        if (!this.handler.isEditor()) {
        	air = player.getAir();
        }
        if (player.areEyesInFluid(FluidTags.WATER) || air < 300) {
            int full = MathHelper.ceil((double)(air - 2) * 10.0D / 300.0D);
            int partial = MathHelper.ceil((double)air * 10.0D / 300.0D) - full;
            
            for (int i = 0; i < full + partial; ++i) {
            	int x = left - i * 8 - 18;
            	if (this.alignment == BarAlignment.LEFT) {
					x = left + i * 8 - 18;
				}
                blit(matrix, x, top, (i < full ? 16 : 25), 18, 9, 9);
            }
        }

        RenderSystem.disableBlend();
        mc.getProfiler().endSection();
        
    }
	
	public int getCurrentFoodBarHeight() {
		return this.handler.foodMountHealthElement.currentFoodHeight;
	}
	
	public boolean isFoodBarAtDefaultPos() {
		return this.handler.foodMountHealthElement.isDefaultPos;
	}

}
