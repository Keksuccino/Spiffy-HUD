package de.keksuccino.spiffyhud.customization.elements.vanillalike.playerhealth;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.SpiffyUtils;
import de.keksuccino.spiffyhud.util.rendering.ElementMobilizer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikePlayerHealthElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Minecraft minecraft = Minecraft.getInstance();
    protected final RandomSource random = RandomSource.create();
    protected int lastHealth;
    protected int displayHealth;
    protected long lastHealthTime;
    protected long healthBlinkTime;
    protected int tickCount;

    private int barWidth = 100;
    private int barHeight = 100;
    private int barOriginalX = 0;
    private int barOriginalY = 0;
    private boolean shouldRenderBar = false;

    @NotNull
    public de.keksuccino.spiffyhud.util.Alignment alignment = de.keksuccino.spiffyhud.util.Alignment.TOP_LEFT;

    public VanillaLikePlayerHealthElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        this.tickCount = SpiffyUtils.getGuiAccessor().getTickCount_Spiffy();

        if (this.minecraft.player == null) return;
        if (this.minecraft.level == null) return;

        //Update size and originalPos of bar before render
        this.shouldRenderBar = false;
        this.renderPlayerHealth(graphics);
        this.shouldRenderBar = true;

        int x = this.getAbsoluteX();
        int y = this.getAbsoluteY();
        Integer[] alignedBody = de.keksuccino.spiffyhud.util.Alignment.calculateElementBodyPosition(this.alignment, x, y, this.getAbsoluteWidth(), this.getAbsoluteHeight(), this.barWidth, this.barHeight);
        x = alignedBody[0];
        y = alignedBody[1];

//        graphics.pose().pushPose();
//        graphics.pose().scale(-1.0F, 1.0F, 1.0F);
//        graphics.pose().translate(-this.barWidth, 0.0F, 0.0F);

        ElementMobilizer.mobilize(graphics, -this.barOriginalX, -this.barOriginalY, x, y, () -> {

            RenderSystem.enableBlend();
            RenderingUtils.resetShaderColor(graphics);

            //-------------------------------

            this.renderPlayerHealth(graphics);

            //-------------------------------

            RenderingUtils.resetShaderColor(graphics);

        });

//        graphics.pose().popPose();

    }

    private void renderPlayerHealth(GuiGraphics graphics) {
        int aa;
        int z;
        int y;
        int x;
        Player player = this.getCameraPlayer();
        if (player == null) {
            return;
        }
        int i = Mth.ceil(player.getHealth());
        boolean bl = this.healthBlinkTime > (long)this.tickCount && (this.healthBlinkTime - (long)this.tickCount) / 3L % 2L == 1L;
        long l = Util.getMillis();
        if (i < this.lastHealth && player.invulnerableTime > 0) {
            this.lastHealthTime = l;
            this.healthBlinkTime = this.tickCount + 20;
        } else if (i > this.lastHealth && player.invulnerableTime > 0) {
            this.lastHealthTime = l;
            this.healthBlinkTime = this.tickCount + 10;
        }
        if (l - this.lastHealthTime > 1000L) {
            this.lastHealth = i;
            this.displayHealth = i;
            this.lastHealthTime = l;
        }
        this.lastHealth = i;
        int j = this.displayHealth;
        this.random.setSeed(this.tickCount * 312871L);
        FoodData foodData = player.getFoodData();
        int k = foodData.getFoodLevel();
        int m = getScreenWidth() / 2 - 91;
        int n = getScreenWidth() / 2 + 91;
        int o = getScreenHeight() - 39;
        float f = Math.max((float)player.getAttributeValue(Attributes.MAX_HEALTH), (float)Math.max(j, i));
        int p = Mth.ceil(player.getAbsorptionAmount());
        int q = Mth.ceil((f + (float)p) / 2.0f / 10.0f);
        int r = Math.max(10 - (q - 2), 3);
        int s = o - (q - 1) * r - 10;
        int t = o - 10;
        int u = player.getArmorValue();
        int v = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            v = this.tickCount % Mth.ceil(f + 5.0f);
        }
        //Tweak to Vanilla logic
//        this.minecraft.getProfiler().push("armor");
//        for (int w = 0; w < 10; ++w) {
//            if (u <= 0) continue;
//            x = m + w * 8;
//            if (w * 2 + 1 < u) {
//                graphics.blitSprite(ARMOR_FULL_SPRITE, x, s, 9, 9);
//            }
//            if (w * 2 + 1 == u) {
//                graphics.blitSprite(ARMOR_HALF_SPRITE, x, s, 9, 9);
//            }
//            if (w * 2 + 1 <= u) continue;
//            graphics.blitSprite(ARMOR_EMPTY_SPRITE, x, s, 9, 9);
//        }
//        this.minecraft.getProfiler().popPush("health");
        this.renderHearts(graphics, player, m, o, r, v, f, i, j, p, bl);
        //Tweak to Vanilla logic
//        LivingEntity livingEntity = this.getPlayerVehicleWithHealth();
//        x = this.getVehicleMaxHearts(livingEntity);
//        if (x == 0) {
//            this.minecraft.getProfiler().popPush("food");
//            for (y = 0; y < 10; ++y) {
//                ResourceLocation resourceLocation3;
//                ResourceLocation resourceLocation2;
//                ResourceLocation resourceLocation;
//                z = o;
//                if (player.hasEffect(MobEffects.HUNGER)) {
//                    resourceLocation = FOOD_EMPTY_HUNGER_SPRITE;
//                    resourceLocation2 = FOOD_HALF_HUNGER_SPRITE;
//                    resourceLocation3 = FOOD_FULL_HUNGER_SPRITE;
//                } else {
//                    resourceLocation = FOOD_EMPTY_SPRITE;
//                    resourceLocation2 = FOOD_HALF_SPRITE;
//                    resourceLocation3 = FOOD_FULL_SPRITE;
//                }
//                if (player.getFoodData().getSaturationLevel() <= 0.0f && this.tickCount % (k * 3 + 1) == 0) {
//                    z += this.random.nextInt(3) - 1;
//                }
//                aa = n - y * 8 - 9;
//                graphics.blitSprite(resourceLocation, aa, z, 9, 9);
//                if (y * 2 + 1 < k) {
//                    graphics.blitSprite(resourceLocation3, aa, z, 9, 9);
//                }
//                if (y * 2 + 1 != k) continue;
//                graphics.blitSprite(resourceLocation2, aa, z, 9, 9);
//            }
//            t -= 10;
//        }
//        this.minecraft.getProfiler().popPush("air");
//        y = player.getMaxAirSupply();
//        z = Math.min(player.getAirSupply(), y);
//        if (player.isEyeInFluid(FluidTags.WATER) || z < y) {
//            int ab = this.getVisibleVehicleHeartRows(x) - 1;
//            t -= ab * 10;
//            int ac = Mth.ceil((double)(z - 2) * 10.0 / (double)y);
//            int ad = Mth.ceil((double)z * 10.0 / (double)y) - ac;
//            for (aa = 0; aa < ac + ad; ++aa) {
//                if (aa < ac) {
//                    graphics.blitSprite(AIR_SPRITE, n - aa * 8 - 9, t, 9, 9);
//                    continue;
//                }
//                graphics.blitSprite(AIR_BURSTING_SPRITE, n - aa * 8 - 9, t, 9, 9);
//            }
//        }
//        this.minecraft.getProfiler().pop();
    }

    private void renderHearts(GuiGraphics guiGraphics, Player player, int x, int y, int height, int offsetHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorptionAmount, boolean renderHighlight) {
        Gui.HeartType heartType = Gui.HeartType.forPlayer(player);
        boolean hardcore = player.level().getLevelData().isHardcore();
        int i = Mth.ceil((double)maxHealth / 2.0);
        int j = Mth.ceil((double)absorptionAmount / 2.0);
        int k = i * 2;
        //Tweak to Vanilla logic
        int startHeartX = -10000000;
        int startHeartY = -10000000;
        int endHeartX = -10000000;
        int endHeartY = -10000000;
        //-------------------------
        for (int l = i + j - 1; l >= 0; --l) {
            boolean bl4;
            int r;
            boolean bl2;
            int m = l / 10;
            int n = l % 10;
            int heartX = x + n * 8;
            int heartY = y - m * height;
            if (currentHealth + absorptionAmount <= 4) {
                heartY += this.random.nextInt(2);
            }
            if (l < i && l == offsetHeartIndex) {
                heartY -= 2;
            }
            //Tweak to Vanilla logic
            if (startHeartX == -10000000) {
                startHeartX = heartX;
            } else if (heartX < startHeartX) {
                startHeartX = heartX;
            }
            if (startHeartY == -10000000) {
                startHeartY = heartY;
            } else if (heartY < startHeartY) {
                startHeartY = heartY;
            }
            if (endHeartX == -10000000) {
                endHeartX = heartX;
            } else if (heartX > endHeartX) {
                endHeartX = heartX;
            }
            if (endHeartY == -10000000) {
                endHeartY = heartY;
            } else if (heartY > endHeartY) {
                endHeartY = heartY;
            }
            //--------------------
            //Tweak to Vanilla logic (if wrap)
            if (this.shouldRenderBar) {
                this.renderHeart(guiGraphics, Gui.HeartType.CONTAINER, heartX, heartY, hardcore, renderHighlight, false);
                int q = l * 2;
                boolean bl3 = bl2 = l >= i;
                if (bl2 && (r = q - k) < absorptionAmount) {
                    boolean bl32 = r + 1 == absorptionAmount;
                    this.renderHeart(guiGraphics, heartType == Gui.HeartType.WITHERED ? heartType : Gui.HeartType.ABSORBING, heartX, heartY, hardcore, false, bl32);
                }
                if (renderHighlight && q < displayHealth) {
                    bl4 = q + 1 == displayHealth;
                    this.renderHeart(guiGraphics, heartType, heartX, heartY, hardcore, true, bl4);
                }
                if (q >= currentHealth) continue;
                bl4 = q + 1 == currentHealth;
                this.renderHeart(guiGraphics, heartType, heartX, heartY, hardcore, false, bl4);
            }
        }
        //Tweak to Vanilla logic
        if (startHeartX != -10000000) {
            this.barOriginalX = Math.min(startHeartX, endHeartX);
            this.barOriginalY = Math.min(startHeartY, endHeartY);
            this.barWidth = Math.max(startHeartX, endHeartX) - Math.min(startHeartX, endHeartX);
            this.barHeight = Math.max(startHeartY, endHeartY) - Math.min(startHeartY, endHeartY);
            this.barWidth += 9;
            this.barHeight += 9;
        } else {
            this.barWidth = 0;
            this.barHeight = 9;
        }
        //---------------------
    }

    private void renderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, boolean hardcore, boolean halfHeart, boolean blinking) {
        guiGraphics.blitSprite(heartType.getSprite(hardcore, blinking, halfHeart), x, y, 9, 9);
    }

    private Font getFont() {
        return Minecraft.getInstance().font;
    }

    @Nullable
    private Player getCameraPlayer() {
        return (Minecraft.getInstance().getCameraEntity() instanceof Player p) ? p : null;
    }

    @Override
    public int getAbsoluteWidth() {
        return 100;
    }

    @Override
    public int getAbsoluteHeight() {
        return 100;
    }

}
