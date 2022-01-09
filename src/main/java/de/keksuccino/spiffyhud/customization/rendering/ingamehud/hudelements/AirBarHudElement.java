package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class AirBarHudElement extends IngameHudElement {
	
	public BarAlignment alignment = BarAlignment.RIGHT;
	
	public AirBarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);
	}

	@Override
	public void render(PoseStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {
		
		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);

		if (this.fireEvents) {
			if (this.handler.pre(ForgeIngameGui.AIR_LEVEL_ELEMENT, matrix)) return;
		}
		
		if (this.visible) {
			this.renderAirRaw(matrix);
		}
		
		if (this.fireEvents) {
			this.handler.post(ForgeIngameGui.AIR_LEVEL_ELEMENT, matrix);
		}
		
	}
	
	protected void renderAirRaw(PoseStack matrix) {
		
        mc.getProfiler().push("air");
        Player player = (Player)this.mc.getCameraEntity();
        RenderSystem.enableBlend();
        int left = ((int)(this.x / this.scale)) + 90;
        if (this.alignment == BarAlignment.LEFT) {
			left = ((int)(this.x / this.scale)) + 18;
		}
        int top = (int) (this.y / this.scale);

        int air = 150;
        if (!this.handler.isEditor()) {
        	air = player.getAirSupply();
        }
        if (player.isEyeInFluid(FluidTags.WATER) || air < 300) {
            int full = Mth.ceil((double)(air - 2) * 10.0D / 300.0D);
            int partial = Mth.ceil((double)air * 10.0D / 300.0D) - full;
            
            matrix.pushPose();

			matrix.scale(this.scale, this.scale, this.scale);
			
            for (int i = 0; i < full + partial; ++i) {
            	int x = left - i * 8 - 18;
            	if (this.alignment == BarAlignment.LEFT) {
					x = left + i * 8 - 18;
				}
                blit(matrix, x, top, (i < full ? 16 : 25), 18, 9, 9);
            }
            
            matrix.popPose();
        }

        RenderSystem.disableBlend();
        mc.getProfiler().pop();
        
    }
	
	public int getCurrentFoodBarHeight() {
		return this.handler.foodMountHealthElement.currentFoodHeight;
	}
	
	public boolean isFoodBarAtDefaultPos() {
		return this.handler.foodMountHealthElement.isDefaultPos;
	}

}
