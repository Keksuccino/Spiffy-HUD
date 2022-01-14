package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.util.Random;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

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

		this.renderHealth(matrix);

	}
	
	protected void renderHealth(MatrixStack matrix) {
		
		if (this.visible) {
			this.handler.bind(GUI_ICONS_TEXTURE);
		}
		
		if (this.visible) {
			
			mc.getProfiler().push("health");
			RenderSystem.enableBlend();

			PlayerEntity player = (PlayerEntity)this.mc.getCameraEntity();
			int health = 10;
			if (!this.handler.isEditor()) {
				health = MathHelper.ceil(player.getHealth());
				
				if ((player.getHealth() >= player.getMaxHealth()) && this.hideWhenFull) {
					return;
				}
			}

			//If white border should be rendered around hearts
			boolean highlight = healthUpdateCounter > (long)this.handler.getTicks() && (healthUpdateCounter - (long)this.handler.getTicks()) / 3L %2L == 1L;

			if (health < this.playerHealth && player.timeUntilRegen > 0) {
				this.lastSystemTime = Util.getMeasuringTimeMs();
				this.healthUpdateCounter = (this.handler.getTicks() + 20);
			} else if (health > this.playerHealth && player.timeUntilRegen > 0) {
				this.lastSystemTime = Util.getMeasuringTimeMs();
				this.healthUpdateCounter = (this.handler.getTicks() + 10);
			}

			if (Util.getMeasuringTimeMs() - this.lastSystemTime > 1000L) {
				this.playerHealth = health;
				this.lastPlayerHealth = health;
				this.lastSystemTime = Util.getMeasuringTimeMs();
			}

			this.playerHealth = health;
			int healthLast = this.lastPlayerHealth;

			EntityAttributeInstance attrMaxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
			float healthMax = (float)attrMaxHealth.getValue();
			float absorb = MathHelper.ceil(player.getAbsorptionAmount());

			int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
			int rowHeight = Math.max(10 - (healthRows - 2), 3);
			
			this.currentHealthHeight = (healthRows * rowHeight);
	        if (rowHeight != 10) this.currentHealthHeight += 10 - rowHeight;
	        
	        this.currentHealthHeight = (int) (this.currentHealthHeight * this.scale);

			this.rand.setSeed((this.handler.getTicks() * 312871));

			int left = (int) (this.x / this.scale);
			if (this.alignment == BarAlignment.RIGHT) {
				left = left + 90 - 18;
			}
			int top = (int) (this.y / this.scale);

			int regen = -1;
			if (player.hasStatusEffect(StatusEffects.REGENERATION)) {
				regen = this.handler.getTicks() % 25;
			}

			final int marginTop =  9 * (mc.world.getLevelProperties().isHardcore() ? 5 : 0);
			final int background = (highlight ? 25 : 16);
			int marginLeft = 16;
			int marginLeftAbsorption = 160;
			if (player.hasStatusEffect(StatusEffects.POISON) && !this.handler.isEditor()) {
				marginLeft += 36;
			} else if (player.hasStatusEffect(StatusEffects.WITHER) && !this.handler.isEditor()) {
				marginLeft += 72;
				//absorption hearts are rendered as wither hearts if effect is active
				marginLeftAbsorption -= 36;
			} else if (player.isFreezing() && !this.handler.isEditor()) {
				marginLeft += 126;
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

				if (health <= 4) y += rand.nextInt(2);
				if (i == regen) y -= 2;

				//Render heart background
				drawTexture(matrix, x, y, background, marginTop, 9, 9);

				//Render white border around heart
				if (highlight) {
					//Render half or full heart (????)
					if (i * 2 + 1 < healthLast) {
						drawTexture(matrix, x, y, marginLeft + 54, marginTop, 9, 9);
					} else if (i * 2 + 1 == healthLast) {
						drawTexture(matrix, x, y, marginLeft + 63, marginTop, 9, 9);
					}
				}

				//Render normal or absorption heart
				if (absorbRemaining > 0.0F) {
					if ((absorbRemaining == absorb) && (absorb % 2.0F == 1.0F)) {
						drawTexture(matrix, x, y, marginLeftAbsorption + 9, marginTop, 9, 9);
						absorbRemaining -= 1.0F;
					} else {
						drawTexture(matrix, x, y, marginLeftAbsorption, marginTop, 9, 9);
						absorbRemaining -= 2.0F;
					}
				} else {
					//Render half or full heart
					if (i * 2 + 1 < health) {
						drawTexture(matrix, x, y, marginLeft + 36, marginTop, 9, 9);
					} else if (i * 2 + 1 == health) {
						drawTexture(matrix, x, y, marginLeft + 45, marginTop, 9, 9);
					}
				}
			}

			matrix.pop();

			RenderSystem.disableBlend();
			mc.getProfiler().pop();
			
		}

	}

}
