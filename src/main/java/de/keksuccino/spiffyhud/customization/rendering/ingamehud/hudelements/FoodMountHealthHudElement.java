package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;

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
	public void render(PoseStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {
		
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

	protected void renderFood(PoseStack matrix) {

		Player player = (Player)this.mc.getCameraEntity();
		Entity tmpMountEntity = player.getVehicle();

		if ((tmpMountEntity instanceof LivingEntity) && !this.handler.isEditor()) return;
		
		if (this.fireEvents) {
			if (this.handler.pre(ForgeIngameGui.FOOD_LEVEL_ELEMENT, matrix)) return;
		}

		if (this.visible) {

			mc.getProfiler().push("food");
			
			RenderSystem.enableBlend();
			int left = ((int)(this.x / this.scale)) + 90;
			if (this.alignment == BarAlignment.LEFT) {
				left = ((int)(this.x / this.scale)) + 18;
			}
			int top = (int) (this.y / this.scale);

			FoodData stats = mc.player.getFoodData();
			int level = 10;
			
			if (!this.handler.isEditor()) {
				
				level = stats.getFoodLevel();
				
				if (this.hideWhenFull && !stats.needsFood()) {
					return;
				}
				
			}

            matrix.pushPose();

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

				if (mc.player.hasEffect(MobEffects.HUNGER) && !this.handler.isEditor()) {
					icon += 36;
					background = 13;
				}

				if (!this.handler.isEditor()) {
					if (player.getFoodData().getSaturationLevel() <= 0.0F && this.handler.getGuiTicks() % (level * 3 + 1) == 0) {
						y = top + (rand.nextInt(3) - 1);
					}
				}

				blit(matrix, x, y, 16 + background * 9, 27, 9, 9);

				if (idx < level)
					blit(matrix, x, y, icon + 36, 27, 9, 9);
				else if (idx == level)
					blit(matrix, x, y, icon + 45, 27, 9, 9);
			}
			
			matrix.popPose();

			RenderSystem.disableBlend();
			mc.getProfiler().pop();
			
			this.currentFoodHeight = (int) (10 * this.scale);

		}

		if (this.fireEvents) {
			this.handler.post(ForgeIngameGui.FOOD_LEVEL_ELEMENT, matrix);
		}

	}

	protected void renderHealthMount(PoseStack matrix) {

		Player player = (Player)mc.getCameraEntity();
		Entity tmpMountEntity = player.getVehicle();

		if (!(tmpMountEntity instanceof LivingEntity)) return;
		
		if (this.handler.isEditor()) {
			return;
		}

		if (this.visible) {
			this.handler.bind(GUI_ICONS_LOCATION);
		}

		if (this.fireEvents) {
			if (this.handler.pre(ForgeIngameGui.MOUNT_HEALTH_ELEMENT, matrix)) return;
		}

		if (this.visible) {

			int left = ((int)(this.x / this.scale)) + 90;
			if (this.alignment == BarAlignment.LEFT) {
				left = ((int)(this.x / this.scale)) + 18;
			}

			mc.getProfiler().popPush("mountHealth");
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

            matrix.pushPose();

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
			
			matrix.popPose();
			
			this.currentFoodHeight = (int) (heightRow * this.scale);

			RenderSystem.disableBlend();

		}

		if (this.fireEvents) {
			this.handler.post(ForgeIngameGui.MOUNT_HEALTH_ELEMENT, matrix);
		}

	}

}
