package de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.util.math.MathHelper;

public class TitleHudElement extends IngameHudElement {
	
	public final boolean isSubtitle;

	public TitleHudElement(CustomizableIngameGui handler, boolean isSubtitle) {
		super(handler);
		
		this.isSubtitle = isSubtitle;
		
//		this.width = 182;
//		this.height = 22;
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		if (this.visible) {
			
			this.renderTitleRaw(scaledWidth, scaledHeight, partialTicks, matrix);
			
		}

	}

	protected void renderTitleRaw(int width, int height, float partialTicks, MatrixStack matrix) {

		if ((this.handler.getCurrentTitle() != null) && (this.handler.getTitlesTimer() > 0)) {
			
			//Update width + height
			if (!this.isSubtitle) {
				this.width = this.mc.fontRenderer.getStringPropertyWidth(this.handler.getCurrentTitle()) * 4;
				this.height = this.mc.fontRenderer.FONT_HEIGHT * 4;
			} else {
				if (this.handler.getCurrentSubTitle() != null) {
					this.width = this.mc.fontRenderer.getStringPropertyWidth(this.handler.getCurrentSubTitle()) * 2;
					this.height = this.mc.fontRenderer.FONT_HEIGHT * 2;
				}
			}
			
			mc.getProfiler().startSection("titleAndSubtitle");
			float age = (float)this.handler.getTitlesTimer() - partialTicks;
			int opacity = 255;

			if (!this.handler.isEditor()) {
				if (this.handler.getTitlesTimer() > this.handler.getTitleFadeOut() + this.handler.getTitleDisplayTime()) {
					float f3 = (float)(this.handler.getTitleFadeIn() + this.handler.getTitleDisplayTime() + this.handler.getTitleFadeOut()) - age;
					opacity = (int)(f3 * 255.0F / (float)this.handler.getTitleFadeIn());
				}
				if (this.handler.getTitlesTimer() <= this.handler.getTitleFadeOut()) {
					opacity = (int)(age * 255.0F / (float)this.handler.getTitleFadeOut());
				}
				
				opacity = MathHelper.clamp(opacity, 0, 255);
			}

			if (opacity > 8) {
				RenderSystem.pushMatrix();
//				RenderSystem.translatef((float)(width / 2), (float)(height / 2), 0.0F);
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.pushMatrix();
				RenderSystem.scalef(4.0F, 4.0F, 4.0F);
				int l = opacity << 24 & -16777216;
				if (!this.isSubtitle) {
					//(float)(-this.mc.fontRenderer.getStringPropertyWidth(this.handler.getCurrentTitle()) / 2)
					this.mc.fontRenderer.drawTextWithShadow(matrix, this.handler.getCurrentTitle().func_241878_f(), this.x / 4, this.y / 4, 16777215 | l); //y = -10.0F
				}
				RenderSystem.popMatrix();
				if (this.isSubtitle) {
					if (this.handler.getCurrentSubTitle() != null) {
						RenderSystem.pushMatrix();
						RenderSystem.scalef(2.0F, 2.0F, 2.0F);
						//(float)(-this.mc.fontRenderer.getStringPropertyWidth(this.handler.getCurrentSubTitle()) / 2)
						this.mc.fontRenderer.drawTextWithShadow(matrix, this.handler.getCurrentSubTitle().func_241878_f(), this.x / 2, this.y / 2, 16777215 | l); //y = 5.0F
						RenderSystem.popMatrix();
					}
				}
				RenderSystem.disableBlend();
				RenderSystem.popMatrix();
			}

			this.mc.getProfiler().endSection();
			
		}

	}

}
