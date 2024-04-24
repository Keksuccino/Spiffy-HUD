package de.keksuccino.spiffyhud.customization.elements.vanillalike.effects;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.SpiffyUtils;
import de.keksuccino.spiffyhud.util.SizeAndPositionRecorder;
import de.keksuccino.spiffyhud.util.rendering.ElementMobilizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VanillaLikeEffectsElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation EFFECT_BACKGROUND_AMBIENT_SPRITE = new ResourceLocation("hud/effect_background_ambient");
    private static final ResourceLocation EFFECT_BACKGROUND_SPRITE = new ResourceLocation("hud/effect_background");

    private final Minecraft minecraft = Minecraft.getInstance();
    protected int tickCount;

    private int barWidth = 100;
    private int barHeight = 100;
    private int barOriginalX = 0;
    private int barOriginalY = 0;
    private boolean shouldRenderBar = false;

    @NotNull
    public de.keksuccino.spiffyhud.util.Alignment alignment = de.keksuccino.spiffyhud.util.Alignment.TOP_LEFT;

    public VanillaLikeEffectsElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        this.tickCount = SpiffyUtils.getGuiAccessor().getTickCount_Spiffy();

        if (this.minecraft.player == null) return;
        if (this.minecraft.level == null) return;

        //Update size and originalPos of bar before render
        this.shouldRenderBar = false;
        this.renderEffects(graphics);
        this.shouldRenderBar = true;

        int x = this.getAbsoluteX();
        int y = this.getAbsoluteY();
        Integer[] alignedBody = de.keksuccino.spiffyhud.util.Alignment.calculateElementBodyPosition(this.alignment, x, y, this.getAbsoluteWidth(), this.getAbsoluteHeight(), this.barWidth, this.barHeight);
        x = alignedBody[0];
        y = alignedBody[1];

        ElementMobilizer.mobilize(graphics, -this.barOriginalX, -this.barOriginalY, x, y, () -> {

            RenderSystem.enableBlend();
            RenderingUtils.resetShaderColor(graphics);

//            graphics.pose().scale(-1.0F, 1.0F, 1.0F);
//            graphics.pose().translate(-this.barWidth, 0.0F, 0.0F);

            //-------------------------------

            this.renderEffects(graphics);

            //-------------------------------

            RenderingUtils.resetShaderColor(graphics);

        });

    }

    protected void renderEffects(GuiGraphics graphics) {
        EffectRenderingInventoryScreen effectRenderingInventoryScreen;
        Screen screen;
        Collection<MobEffectInstance> collection = this.minecraft.player.getActiveEffects();
        //Tweak to Vanilla logic
        if (isEditor()) {
            collection = List.of(new MobEffectInstance(MobEffects.LUCK, 300), new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300), new MobEffectInstance(MobEffects.BAD_OMEN, 300));
        }
        //----------------------
        if (collection.isEmpty() || (screen = this.minecraft.screen) instanceof EffectRenderingInventoryScreen && (effectRenderingInventoryScreen = (EffectRenderingInventoryScreen)screen).canSeeEffects()) {
            return;
        }
        RenderSystem.enableBlend();
        int i = 0;
        int j = 0;
        MobEffectTextureManager mobEffectTextureManager = this.minecraft.getMobEffectTextures();
        ArrayList<Runnable> list = Lists.newArrayListWithExpectedSize(collection.size());
        //Tweak to Vanilla logic
        SizeAndPositionRecorder recorder = new SizeAndPositionRecorder();
        recorder.setWidthOffset(24);
        recorder.setHeightOffset(24);
        //---------------------
        for (MobEffectInstance mobEffectInstance : Ordering.natural().reverse().sortedCopy(collection)) {
            int n;
            MobEffect mobEffect = mobEffectInstance.getEffect();
            if (!mobEffectInstance.showIcon()) continue;
            int effectX = getScreenWidth();
            int effectY = 1;
            if (this.minecraft.isDemo()) {
                effectY += 15;
            }
            if (mobEffect.isBeneficial()) {
                effectX -= 25 * ++i;
            } else {
                effectX -= 25 * ++j;
                effectY += 26;
            }
            float f = 1.0f;
            //Tweak to Vanilla logic
            recorder.updateX(effectX);
            recorder.updateY(effectY);
            //-----------------------
            if (this.shouldRenderBar) { //wrapped in IF
                if (mobEffectInstance.isAmbient()) {
                    graphics.blitSprite(EFFECT_BACKGROUND_AMBIENT_SPRITE, effectX, effectY, 24, 24);
                } else {
                    graphics.blitSprite(EFFECT_BACKGROUND_SPRITE, effectX, effectY, 24, 24);
                    if (mobEffectInstance.endsWithin(200)) {
                        int m = mobEffectInstance.getDuration();
                        n = 10 - m / 20;
                        f = Mth.clamp((float)m / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + Mth.cos((float)m * (float)Math.PI / 5.0f) * Mth.clamp((float)n / 10.0f * 0.25f, 0.0f, 0.25f);
                    }
                }
            }
            TextureAtlasSprite textureAtlasSprite = mobEffectTextureManager.get(mobEffect);
            n = effectX;
            int o = effectY;
            float g = f;
            int finalN = n;
            if (this.shouldRenderBar) { //wrapped in IF
                list.add(() -> {
                    graphics.setColor(1.0f, 1.0f, 1.0f, g);
                    graphics.blit(finalN + 3, o + 3, 0, 18, 18, textureAtlasSprite);
                    graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                });
            }
        }
        //Tweak to Vanilla logic
        this.barOriginalX = recorder.getX();
        this.barOriginalY = recorder.getY();
        this.barWidth = recorder.getWidth();
        this.barHeight = recorder.getHeight();
        //-----------------------
        list.forEach(Runnable::run);
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
        return 60;
    }

}
