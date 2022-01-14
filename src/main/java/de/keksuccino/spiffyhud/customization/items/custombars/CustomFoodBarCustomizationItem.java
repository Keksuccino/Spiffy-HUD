package de.keksuccino.spiffyhud.customization.items.custombars;

import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.HungerManager;

public class CustomFoodBarCustomizationItem extends CustomBarCustomizationItemBase {
	
	protected int currentPercentWidthHeight = 0;
	
	public int maxFood = 20;
	public boolean hideWhenFull = false;

	public CustomFoodBarCustomizationItem(PropertiesSection item) {
		super(item);
		
		this.value = "Custom Food Bar";
		
		String maxFoodString = item.getEntryValue("maxfood");
		if (maxFoodString != null) {
			if (MathUtils.isInteger(maxFoodString)) {
				this.maxFood = Integer.parseInt(maxFoodString);
			}
		}
		
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
			this.barColorHex = "#f0cc00";
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
			
			HungerManager f = p.getHungerManager();
			
			float foodPercent = (float)(((float)f.getFoodLevel() / (float)this.maxFood)) * 100.0F;
			if (this.isEditor()) {
				foodPercent = 50.0F;
			}
			if (foodPercent >= 100) {
				if (this.hideWhenFull) {
					return;
				}
			}
			if ((this.direction == BarDirection.LEFT) || (this.direction == BarDirection.RIGHT)) {
				this.currentPercentWidthHeight = (int)((((float)this.width) / 100.0F) * foodPercent);
			}
			if ((this.direction == BarDirection.UP) || (this.direction == BarDirection.DOWN)) {
				this.currentPercentWidthHeight = (int)((((float)this.height) / 100.0F) * foodPercent);
			}
			
			this.renderBarBackground(matrix);
			
			this.renderBar(matrix);
			
		}
		
	}

	@Override
	protected void renderBar(MatrixStack matrix) {
		
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
				drawTexture(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.currentPercentWidthHeight, this.height, this.width, this.height);
			}
			if (this.direction == BarDirection.LEFT) {
				int i = (this.width - this.currentPercentWidthHeight);
				drawTexture(matrix, this.getPosX() + i, this.getPosY(), i, 0.0F, this.currentPercentWidthHeight, this.height, this.width, this.height);
			}
			if (this.direction == BarDirection.UP) {
				int i = (this.height - this.currentPercentWidthHeight);
				drawTexture(matrix, this.getPosX(), this.getPosY() + i, 0.0F, i, this.width, this.currentPercentWidthHeight, this.width, this.height);
			}
			if (this.direction == BarDirection.DOWN) {
				drawTexture(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.width, this.currentPercentWidthHeight, this.width, this.height);
			}
			
			RenderSystem.disableBlend();
			
		}
		
	}

	@Override
	protected void renderBarBackground(MatrixStack matrix) {
		
		if (this.backgroundTexture == null) {
			
			RenderUtils.fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, this.backgroundColor.getRGB(), 1.0F);
		
		} else {
			
			RenderUtils.bindTexture(this.backgroundTexture);
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexture(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.width, this.height, this.width, this.height);
			RenderSystem.disableBlend();
			
		}
		
	}

}
