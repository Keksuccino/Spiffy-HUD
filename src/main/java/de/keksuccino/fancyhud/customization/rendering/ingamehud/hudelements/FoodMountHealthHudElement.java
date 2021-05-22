package de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class FoodMountHealthHudElement extends IngameHudElement {

	protected Random rand = new Random();
	protected long healthUpdateCounter;
	protected int playerHealth;
	protected long lastSystemTime;
	protected int lastPlayerHealth;
	
	public int currentFoodHeight = 10;
	public boolean isDefaultPos = false;
	
	public BarAlignment alignment = BarAlignment.RIGHT;

	public FoodMountHealthHudElement(CustomizableIngameGui handler) {
		super(handler);

		this.width = 80; //90
		this.height = 9;
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		this.currentFoodHeight = 10;
		
		if (CustomizableIngameGui.renderFood && !CustomizableIngameGui.renderHealthMount) {
			
			this.renderFood(matrix);
			
		} else if (CustomizableIngameGui.renderHealthMount) {
			
			this.renderHealthMount(matrix);
			
		}
		
	}

	protected void renderFood(MatrixStack matrix) {

		PlayerEntity player = (PlayerEntity)this.mc.getRenderViewEntity();
		Entity tmpMountEntity = player.getRidingEntity();

		if ((tmpMountEntity instanceof LivingEntity) && !this.handler.isEditor()) return;
		
		if (this.fireEvents) {
			if (this.handler.pre(ElementType.FOOD, matrix, false)) return;
		}

		if (this.visible) {

			mc.getProfiler().startSection("food");
			
			RenderSystem.enableBlend();
			int left = this.x + 90;
			if (this.alignment == BarAlignment.LEFT) {
				left = this.x + 18;
			}
			int top = this.y;

			FoodStats stats = mc.player.getFoodStats();
			int level = 10;
			
			if (!this.handler.isEditor()) {
				level = stats.getFoodLevel();
			}

			for (int i = 0; i < 10; ++i) {
				int idx = i * 2 + 1;
				int x = left - i * 8 - 18;
				if (this.alignment == BarAlignment.LEFT) {
					x = left + i * 8 - 18;
				}
				int y = top;
				int icon = 16;
				byte background = 0;

				if (mc.player.isPotionActive(Effects.HUNGER) && !this.handler.isEditor()) {
					icon += 36;
					background = 13;
				}

				if (!this.handler.isEditor()) {
					if (player.getFoodStats().getSaturationLevel() <= 0.0F && this.handler.getTicks() % (level * 3 + 1) == 0) {
						y = top + (rand.nextInt(3) - 1);
					}
				}

				blit(matrix, x, y, 16 + background * 9, 27, 9, 9);

				if (idx < level)
					blit(matrix, x, y, icon + 36, 27, 9, 9);
				else if (idx == level)
					blit(matrix, x, y, icon + 45, 27, 9, 9);
			}

			RenderSystem.disableBlend();
			mc.getProfiler().endSection();
			
			this.currentFoodHeight = 10;

		}

		if (this.fireEvents) {
			this.handler.post(ElementType.FOOD, matrix, false);
		}

	}

	protected void renderHealthMount(MatrixStack matrix) {

		PlayerEntity player = (PlayerEntity)mc.getRenderViewEntity();
		Entity tmpMountEntity = player.getRidingEntity();

		if (!(tmpMountEntity instanceof LivingEntity)) return;
		
		if (this.handler.isEditor()) {
			return;
		}

		if (this.visible) {
			this.handler.bind(GUI_ICONS_LOCATION);
		}

		if (this.fireEvents) {
			if (this.handler.pre(ElementType.HEALTHMOUNT, matrix, false)) return;
		}

		if (this.visible) {

			int left = this.x + 90;
			if (this.alignment == BarAlignment.LEFT) {
				left = this.x + 18;
			}

			mc.getProfiler().endStartSection("mountHealth");
			RenderSystem.enableBlend();
			LivingEntity mount = (LivingEntity)tmpMountEntity;
			int health = (int)Math.ceil((double)mount.getHealth());
			float healthMax = mount.getMaxHealth();
			int hearts = (int)(healthMax + 0.5F) / 2;

			if (hearts > 30) {
				hearts = 30;
			}

			final int MARGIN = 52;
			final int BACKGROUND = MARGIN;
			final int HALF = MARGIN + 45;
			final int FULL = MARGIN + 36;

			int heightRow = 0;
			for (int heart = 0; hearts > 0; heart += 20) {
				int top = this.y - heightRow;

				int rowCount = Math.min(hearts, 10);
				hearts -= rowCount;

				for (int i = 0; i < rowCount; ++i) {
					int x = left - i * 8 - 18;
					if (this.alignment == BarAlignment.LEFT) {
						x = left + i * 8 - 18;
					}
					blit(matrix, x, top, BACKGROUND, 9, 9, 9);

					int idx = i * 2 + 1 + heart;

					if (idx < health) {
						blit(matrix, x, top, FULL, 9, 9, 9);
					} else if (idx == health) {
						blit(matrix, x, top, HALF, 9, 9, 9);
					}
				}

				heightRow += 10;
			}
			
			this.currentFoodHeight = heightRow;

			RenderSystem.disableBlend();

		}

		if (this.fireEvents) {
			this.handler.post(ElementType.HEALTHMOUNT, matrix, false);
		}

	}

}
