package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class PlayerHealthHudElement extends IngameHudElement {

	protected Random rand = new Random();
	protected long healthUpdateCounter;
	protected int playerHealth;
	protected long lastSystemTime;
	protected int lastPlayerHealth;
	public int currentHealthHeight = 10;
	public boolean isDefaultPos = false;
	public boolean hideWhenFull = false;
	
	public BarAlignment alignment = BarAlignment.LEFT;
	
	public PlayerHealthHudElement(CustomizableIngameGui handler) {
		super(handler);

		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);
		
		this.currentHealthHeight = (int) (10 * this.scale);

		this.renderHealth(scaledWidth, scaledHeight, matrix);

	}
	
	protected void renderHealth(int width, int height, MatrixStack matrix) {
		
		if (this.visible) {
			this.handler.bind(GUI_ICONS_LOCATION);
		}
		
		if (this.fireEvents) {
			if (this.handler.pre(ElementType.HEALTH, matrix)) return;
		}
		
		if (this.visible) {
			
			mc.getProfiler().startSection("health");
			RenderSystem.enableBlend();

			PlayerEntity player = (PlayerEntity)this.mc.getRenderViewEntity();
			int health = 10;
			if (!this.handler.isEditor()) {
				health = MathHelper.ceil(player.getHealth());
				
				if ((player.getHealth() >= player.getMaxHealth()) && this.hideWhenFull) {
					return;
				}
			}
			boolean highlight = healthUpdateCounter > (long)this.handler.getTicks() && (healthUpdateCounter - (long)this.handler.getTicks()) / 3L %2L == 1L;

			if (health < this.playerHealth && player.hurtResistantTime > 0) {
				this.lastSystemTime = Util.milliTime();
				this.healthUpdateCounter = (long)(this.handler.getTicks() + 20);
			} else if (health > this.playerHealth && player.hurtResistantTime > 0) {
				this.lastSystemTime = Util.milliTime();
				this.healthUpdateCounter = (long)(this.handler.getTicks() + 10);
			}

			if (Util.milliTime() - this.lastSystemTime > 1000L) {
				this.playerHealth = health;
				this.lastPlayerHealth = health;
				this.lastSystemTime = Util.milliTime();
			}

			this.playerHealth = health;
			int healthLast = this.lastPlayerHealth;

			ModifiableAttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
			float healthMax = (float)attrMaxHealth.getValue();
			float absorb = MathHelper.ceil(player.getAbsorptionAmount());

			int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
			int rowHeight = Math.max(10 - (healthRows - 2), 3);
			
			this.currentHealthHeight = (healthRows * rowHeight);
	        if (rowHeight != 10) this.currentHealthHeight += 10 - rowHeight;
	        
	        this.currentHealthHeight = (int) (this.currentHealthHeight * this.scale);

			this.rand.setSeed((long)(this.handler.getTicks() * 312871));

			int left = (int) (this.x / this.scale);
			if (this.alignment == BarAlignment.RIGHT) {
				left = left + 90 - 18;
			}
			int top = (int) (this.y / this.scale);

			int regen = -1;
			if (player.isPotionActive(Effects.REGENERATION)) {
				regen = this.handler.getTicks() % 25;
			}

			final int TOP =  9 * (mc.world.getWorldInfo().isHardcore() ? 5 : 0);
			final int BACKGROUND = (highlight ? 25 : 16);
			int MARGIN = 16;
			if (player.isPotionActive(Effects.POISON) && !this.handler.isEditor()) {
				MARGIN += 36;
			} else if (player.isPotionActive(Effects.WITHER) && !this.handler.isEditor()) {
				MARGIN += 72;
			}
			float absorbRemaining = absorb;

			matrix.push();

			matrix.scale(this.scale, this.scale, this.scale);
			
			for (int i = MathHelper.ceil((healthMax + absorb) / 2.0F) - 1; i >= 0; --i) {
				int row = MathHelper.ceil((float)(i + 1) / 10.0F) - 1;
				int x = left + i % 10 * 8;
				if (this.alignment == BarAlignment.RIGHT) {
					x = left - i % 10 * 8;
				}
				int y = top - row * rowHeight;
				
				//TODO höhe immer mit rows abstimmen und dann in armor bar nutzen, um pos anzupassen (row werte ausgeben und checken, welcher der richtige ist)
				// ggf hier noch ein "isDefaultPos" boolean, der in armor abgefragt werden kann (wird in cus action gehandelt)

				if (health <= 4) y += rand.nextInt(2);
				if (i == regen) y -= 2;

				blit(matrix, x, y, BACKGROUND, TOP, 9, 9);

				if (highlight) {
					if (i * 2 + 1 < healthLast) {
						blit(matrix, x, y, MARGIN + 54, TOP, 9, 9); //6
					} else if (i * 2 + 1 == healthLast) {
						blit(matrix, x, y, MARGIN + 63, TOP, 9, 9); //7
					}
				}

				if (absorbRemaining > 0.0F) {
					if (absorbRemaining == absorb && absorb % 2.0F == 1.0F) {
						blit(matrix, x, y, MARGIN + 153, TOP, 9, 9); //17
						absorbRemaining -= 1.0F;
					} else {
						blit(matrix, x, y, MARGIN + 144, TOP, 9, 9); //16
						absorbRemaining -= 2.0F;
					}
				} else {
					if (i * 2 + 1 < health) {
						blit(matrix, x, y, MARGIN + 36, TOP, 9, 9); //4
					} else if (i * 2 + 1 == health) {
						blit(matrix, x, y, MARGIN + 45, TOP, 9, 9); //5
					}
				}
			}

			matrix.pop();

			RenderSystem.disableBlend();
			mc.getProfiler().endSection();
			
		}
		
		if (this.fireEvents) {
			this.handler.post(ElementType.HEALTH, matrix);
		}

	}

}
