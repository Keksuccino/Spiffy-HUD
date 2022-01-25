package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.MathHelper;

public class AirBarHudElement extends IngameHudElement {
	
	public BarAlignment alignment = BarAlignment.RIGHT;
	
	public AirBarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("air");
		this.elementActive = InGameHudOverlay.isElementActive("air");
		
		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);

		if (this.renderElement) {
			if (this.visible) {
				this.renderAirRaw(matrix);
			}
		}
		
	}
	
	protected void renderAirRaw(MatrixStack matrix) {
		
        mc.getProfiler().push("air");
        PlayerEntity player = (PlayerEntity)this.mc.getCameraEntity();
        RenderSystem.enableBlend();
        int left = ((int)(this.x / this.scale)) + 90;
        if (this.alignment == BarAlignment.LEFT) {
			left = ((int)(this.x / this.scale)) + 18;
		}
        int top = (int) (this.y / this.scale);

        int air = 150;
        if (!this.handler.isEditor()) {
        	air = player.getAir();
        }
        if (player.isSubmergedIn(FluidTags.WATER) || air < 300) {
            int full = MathHelper.ceil((double)(air - 2) * 10.0D / 300.0D);
            int partial = MathHelper.ceil((double)air * 10.0D / 300.0D) - full;
            
            matrix.push();

			matrix.scale(this.scale, this.scale, this.scale);
			
            for (int i = 0; i < full + partial; ++i) {
            	int x = left - i * 8 - 18;
            	if (this.alignment == BarAlignment.LEFT) {
					x = left + i * 8 - 18;
				}
                drawTexture(matrix, x, top, (i < full ? 16 : 25), 18, 9, 9);
            }
            
            matrix.pop();
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
