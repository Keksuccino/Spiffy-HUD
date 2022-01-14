package de.keksuccino.spiffyhud.mixin.client;

import net.minecraft.client.gui.hud.*;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface IMixinInGameHud {

    @Accessor public void setTicks(int i);

    @Accessor public Text getOverlayMessage();
    @Accessor public void setOverlayMessage(Text t);

    @Accessor public int getOverlayRemaining();
    @Accessor public void setOverlayRemaining(int i);

    @Accessor public boolean getOverlayTinted();
    @Accessor public void setOverlayTinted(boolean b);

    @Accessor public int getHeldItemTooltipFade();
    @Accessor public void setHeldItemTooltipFade(int i);

    @Accessor public ItemStack getCurrentStack();
    @Accessor public void setCurrentStack(ItemStack i);

    @Accessor public int getTitleTotalTicks();
    @Accessor public void setTitleTotalTicks(int i);

    @Accessor public Text getTitle();

    @Accessor public Text getSubtitle();

    @Accessor public int getTitleFadeInTicks();
    @Accessor public void setTitleFadeInTicks(int i);

    @Accessor public int getTitleRemainTicks();
    @Accessor public void setTitleRemainTicks(int i);

    @Accessor public int getTitleFadeOutTicks();
    @Accessor public void setTitleFadeOutTicks(int i);

    @Accessor public int getLastHealthValue();
    @Accessor public void setLastHealthValue(int i);

    @Accessor public int getRenderHealthValue();
    @Accessor public void setRenderHealthValue(int i);

    @Accessor public long getLastHealthCheckTime();
    @Accessor public void setLastHealthCheckTime(long l);

    @Accessor public long getHeartJumpEndTick();
    @Accessor public void setHeartJumpEndTick(long l);

    @Accessor public int getScaledWidth();
    @Accessor public void setScaledWidth(int i);

    @Accessor public int getScaledHeight();
    @Accessor public void setScaledHeight(int i);

    @Accessor public float getAutosaveIndicatorAlpha();
    @Accessor public void setAutosaveIndicatorAlpha(float f);

    @Accessor public float getLastAutosaveIndicatorAlpha();
    @Accessor public void setLastAutosaveIndicatorAlpha(float f);

    @Accessor public float getSpyglassScale();
    @Accessor public void setSpyglassScale(float f);

}
