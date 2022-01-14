package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.util.Random;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;

public class FoodMountHealthHudElement extends IngameHudElement {

	protected Random rand = new Random();
	protected long healthUpdateCounter;
	protected int playerHealth;
	protected long lastSystemTime;
	protected int lastPlayerHealth;
	public int currentFoodHeight = 10;
	public boolean isDefaultPos = false;
	public boolean hideWhenFull = false;
	
	public BarAlignment alignment = BarAlignment.RIGHT;

	public FoodMountHealthHudElement(CustomizableIngameGui handler) {
		super(handler);

		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {
		
		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);

		this.currentFoodHeight = (int) (10 * this.scale);

		boolean isMounted = (this.mc.player.getVehicle() instanceof LivingEntity);
		boolean shouldDraw = (handler.shouldDrawSurvivalElements() || handler.isEditor());

		if (shouldDraw && !isMounted) {
			this.renderFood(matrix);
		}
		if (shouldDraw && isMounted) {
			this.renderHealthMount(matrix);
		}
		
	}

	protected void renderFood(MatrixStack matrix) {

		PlayerEntity player = (PlayerEntity)this.mc.getCameraEntity();
		Entity tmpMountEntity = player.getVehicle();

		if ((tmpMountEntity instanceof LivingEntity) && !this.handler.isEditor()) return;

		if (this.visible) {

			mc.getProfiler().push("food");
			
			RenderSystem.enableBlend();
			int left = ((int)(this.x / this.scale)) + 90;
			if (this.alignment == BarAlignment.LEFT) {
				left = ((int)(this.x / this.scale)) + 18;
			}
			int top = (int) (this.y / this.scale);

			HungerManager stats = mc.player.getHungerManager();
			int level = 10;
			
			if (!this.handler.isEditor()) {
				
				level = stats.getFoodLevel();
				
				if (this.hideWhenFull && !stats.isNotFull()) {
					return;
				}
				
			}

            matrix.push();

			matrix.scale(this.scale, this.scale, this.scale);

			for (int i = 0; i < 10; ++i) {
				int idx = i * 2 + 1;
				int x = left - i * 8 - 18;
				if (this.alignment == BarAlignment.LEFT) {
					x = left + i * 8 - 18;
				}
				int y = top;
				int icon = 16;
				byte background = 0;

				if (mc.player.hasStatusEffect(StatusEffects.HUNGER) && !this.handler.isEditor()) {
					icon += 36;
					background = 13;
				}

				if (!this.handler.isEditor()) {
					if (player.getHungerManager().getSaturationLevel() <= 0.0F && this.handler.getTicks() % (level * 3 + 1) == 0) {
						y = top + (rand.nextInt(3) - 1);
					}
				}

				drawTexture(matrix, x, y, 16 + background * 9, 27, 9, 9);

				if (idx < level)
					drawTexture(matrix, x, y, icon + 36, 27, 9, 9);
				else if (idx == level)
					drawTexture(matrix, x, y, icon + 45, 27, 9, 9);
			}
			
			matrix.pop();

			RenderSystem.disableBlend();
			mc.getProfiler().pop();
			
			this.currentFoodHeight = (int) (10 * this.scale);

		}

	}

	protected void renderHealthMount(MatrixStack matrix) {

		PlayerEntity player = (PlayerEntity)mc.getCameraEntity();
		Entity tmpMountEntity = player.getVehicle();

		if (!(tmpMountEntity instanceof LivingEntity)) return;
		
		if (this.handler.isEditor()) {
			return;
		}

		if (this.visible) {
			this.handler.bind(GUI_ICONS_TEXTURE);
		}

		if (this.visible) {

			int left = ((int)(this.x / this.scale)) + 90;
			if (this.alignment == BarAlignment.LEFT) {
				left = ((int)(this.x / this.scale)) + 18;
			}

			mc.getProfiler().swap("mountHealth");
			RenderSystem.enableBlend();
			LivingEntity mount = (LivingEntity)tmpMountEntity;
			int health = (int)Math.ceil(mount.getHealth());
			float healthMax = mount.getMaxHealth();
			int hearts = (int)(healthMax + 0.5F) / 2;

			if (hearts > 30) {
				hearts = 30;
			}
			
			if ((mount.getHealth() >= mount.getMaxHealth()) && this.hideWhenFull) {
				return;
			}

			final int MARGIN = 52;
			final int BACKGROUND = MARGIN;
			final int HALF = MARGIN + 45;
			final int FULL = MARGIN + 36;

            matrix.push();

			matrix.scale(this.scale, this.scale, this.scale);
			
			int heightRow = 0;
			for (int heart = 0; hearts > 0; heart += 20) {
				int top = ((int)(this.y / this.scale)) - heightRow;

				int rowCount = Math.min(hearts, 10);
				hearts -= rowCount;

				for (int i = 0; i < rowCount; ++i) {
					int x = left - i * 8 - 18;
					if (this.alignment == BarAlignment.LEFT) {
						x = left + i * 8 - 18;
					}
					drawTexture(matrix, x, top, BACKGROUND, 9, 9, 9);

					int idx = i * 2 + 1 + heart;

					if (idx < health) {
						drawTexture(matrix, x, top, FULL, 9, 9, 9);
					} else if (idx == health) {
						drawTexture(matrix, x, top, HALF, 9, 9, 9);
					}
				}

				heightRow += 10;
			}
			
			matrix.pop();
			
			this.currentFoodHeight = (int) (heightRow * this.scale);

			RenderSystem.disableBlend();

		}

	}

}
