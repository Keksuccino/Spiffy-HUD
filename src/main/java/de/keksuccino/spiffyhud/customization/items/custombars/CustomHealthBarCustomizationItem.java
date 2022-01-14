package de.keksuccino.spiffyhud.customization.items.custombars;

import de.keksuccino.konkrete.properties.PropertiesSection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;

public class CustomHealthBarCustomizationItem extends CustomBarCustomizationItemBase {
	
	public boolean hideWhenFull = false;

	public CustomHealthBarCustomizationItem(PropertiesSection item) {
		super(item);
		
		this.value = "Custom Health Bar";
		
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
	public void render(MatrixStack matrix) {

		if (!this.shouldRender()) {
			return;
		}
		
		ClientPlayerEntity p = MinecraftClient.getInstance().player;
		
		if (p != null) {
			
			float healthPercent = ((p.getHealth() / p.getMaxHealth()) * 100.0F);
			if (this.isEditor()) {
				healthPercent = 50.0F;
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

//	@Override
//	protected void renderBar(PoseStack matrix) {
//
//		if (this.barTexture == null) {
//
//			if (this.direction == BarDirection.RIGHT) {
//				RenderUtils.fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.currentPercentWidthHeight, this.getPosY() + this.height, this.barColor.getRGB(), 1.0F);
//			}
//			if (this.direction == BarDirection.LEFT) {
//				RenderUtils.fill(matrix, this.getPosX() + this.width - this.currentPercentWidthHeight, this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, this.barColor.getRGB(), 1.0F);
//			}
//			if (this.direction == BarDirection.UP) {
//				RenderUtils.fill(matrix, this.getPosX(), this.getPosY() + this.height - this.currentPercentWidthHeight, this.getPosX() + this.width, this.getPosY() + this.height, this.barColor.getRGB(), 1.0F);
//			}
//			if (this.direction == BarDirection.DOWN) {
//				RenderUtils.fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.currentPercentWidthHeight, this.barColor.getRGB(), 1.0F);
//			}
//
//		} else {
//
//			int mainTextureWidth = this.width;
//			if (this.barEndTexture != null) {
//				mainTextureWidth -= this.barEndTextureWidth;
//			}
//
//			Minecraft.getInstance().textureManager.bindTexture(this.barTexture);
//			RenderSystem.enableBlend();
//			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//
//			if (this.direction == BarDirection.RIGHT) {
//				blit(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.currentPercentWidthHeight, this.height, mainTextureWidth, this.height);
//			}
//			if (this.direction == BarDirection.LEFT) {
//				int i = (mainTextureWidth - this.currentPercentWidthHeight);
//				blit(matrix, this.getPosX() + i, this.getPosY(), i, 0.0F, this.currentPercentWidthHeight, this.height, mainTextureWidth, this.height);
//			}
//			if (this.direction == BarDirection.UP) {
//				int i = (this.height - this.currentPercentWidthHeight);
//				blit(matrix, this.getPosX(), this.getPosY() + i, 0.0F, i, mainTextureWidth, this.currentPercentWidthHeight, mainTextureWidth, this.height);
//			}
//			if (this.direction == BarDirection.DOWN) {
//				blit(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, mainTextureWidth, this.currentPercentWidthHeight, mainTextureWidth, this.height);
//			}
//
//			RenderSystem.disableBlend();
//
//		}
//
//	}
//
//	@Override
//	protected void renderBarBackground(PoseStack matrix) {
//
//		if (this.backgroundTexture == null) {
//
//			RenderUtils.fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, this.backgroundColor.getRGB(), 1.0F);
//
//		} else {
//
//			Minecraft.getInstance().textureManager.bindTexture(this.backgroundTexture);
//			RenderSystem.enableBlend();
//			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//			blit(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.width, this.height, this.width, this.height);
//			RenderSystem.disableBlend();
//
//		}
//
//	}

}
