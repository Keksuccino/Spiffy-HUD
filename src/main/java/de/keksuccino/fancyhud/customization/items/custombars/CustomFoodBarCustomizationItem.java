package de.keksuccino.fancyhud.customization.items.custombars;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.FoodStats;

import de.keksuccino.fancyhud.customization.items.custombars.CustomBarCustomizationItemBase.BarDirection;

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
		
		ClientPlayerEntity p = Minecraft.getInstance().player;
		
		if (p != null) {
			
			FoodStats f = p.getFoodStats();
			
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
			
			Minecraft.getInstance().textureManager.bindTexture(this.barTexture);
			RenderSystem.enableBlend();
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			
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
	protected void renderBarBackground(MatrixStack matrix) {
		
		if (this.backgroundTexture == null) {
			
			RenderUtils.fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, this.backgroundColor.getRGB(), 1.0F);
		
		} else {
			
			Minecraft.getInstance().textureManager.bindTexture(this.backgroundTexture);
			RenderSystem.enableBlend();
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			blit(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.width, this.height, this.width, this.height);
			RenderSystem.disableBlend();
			
		}
		
	}

}
