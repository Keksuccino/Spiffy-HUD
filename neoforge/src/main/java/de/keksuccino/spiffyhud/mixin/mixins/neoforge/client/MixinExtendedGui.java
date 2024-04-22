package de.keksuccino.spiffyhud.mixin.mixins.neoforge.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.keksuccino.spiffyhud.customization.SpiffyGui;
import de.keksuccino.spiffyhud.customization.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import net.neoforged.neoforge.client.gui.overlay.NamedGuiOverlay;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExtendedGui.class)
public class MixinExtendedGui extends Gui {

    //unused dummy constructor
    @SuppressWarnings("all")
    private MixinExtendedGui() {
        super(null, null);
    }

    @Unique
    private SpiffyGui spiffyGui = null;

    /**
     * @reason Renders Spiffy's overlay to the HUD.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;setSeed(J)V"))
    private void before_setSeed_in_render_Spiffy(GuiGraphics graphics, float partial, CallbackInfo info) {

        if (this.spiffyGui == null) this.spiffyGui = new SpiffyGui();

        if (!Minecraft.getInstance().options.hideGui) {
            spiffyGui.render(graphics, -10000000, -10000000, partial);
        }

    }

    /**
     * @reason Hides the title and subtitle if they are hidden in Spiffy HUD.
     */
    @WrapOperation(method = "renderTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)I"))
    private int wrap_drawString_in_renderTitle_Spiffy(GuiGraphics instance, Font font, FormattedCharSequence sequence, int x, int y, int color, boolean shadow, Operation<Integer> original) {
        if (this.title != null) {
            if ((sequence == this.title.getVisualOrderText()) && VanillaHudElements.isHidden(VanillaHudElements.TITLE_IDENTIFIER)) return 0;
        }
        if (this.subtitle != null) {
            if ((sequence == this.subtitle.getVisualOrderText()) && VanillaHudElements.isHidden(VanillaHudElements.SUBTITLE_IDENTIFIER)) return 0;
        }
        return original.call(instance, font, sequence, x, y, color, shadow);
    }

    /**
     * @reason Sets the return value to { true } to cancel rendering of overlay elements if they are hidden in Spiffy HUD.
     */
    @Inject(method = "pre", at = @At("HEAD"), cancellable = true, remap = false) //use HEAD to stop mods from rendering custom stuff to overlay elements if the element is hidden
    private void head_Pre_Spiffy(NamedGuiOverlay overlay, GuiGraphics graphics, CallbackInfoReturnable<Boolean> info) {

        if ((overlay == VanillaGuiOverlay.HOTBAR.type()) && VanillaHudElements.isHidden(VanillaHudElements.HOTBAR_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.JUMP_BAR.type()) && VanillaHudElements.isHidden(VanillaHudElements.JUMP_METER_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.EXPERIENCE_BAR.type()) && VanillaHudElements.isHidden(VanillaHudElements.EXPERIENCE_BAR_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.ITEM_NAME.type()) && VanillaHudElements.isHidden(VanillaHudElements.SELECTED_ITEM_NAME_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.SCOREBOARD.type()) && VanillaHudElements.isHidden(VanillaHudElements.SCOREBOARD_SIDEBAR_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.FOOD_LEVEL.type()) && VanillaHudElements.isHidden(VanillaHudElements.FOOD_BAR_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.ARMOR_LEVEL.type()) && VanillaHudElements.isHidden(VanillaHudElements.ARMOR_BAR_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.AIR_LEVEL.type()) && VanillaHudElements.isHidden(VanillaHudElements.AIR_BAR_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.MOUNT_HEALTH.type()) && VanillaHudElements.isHidden(VanillaHudElements.MOUNT_HEALTH_BAR_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.PLAYER_HEALTH.type()) && VanillaHudElements.isHidden(VanillaHudElements.PLAYER_HEALTH_BAR_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.RECORD_OVERLAY.type()) && VanillaHudElements.isHidden(VanillaHudElements.OVERLAY_MESSAGE_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.CROSSHAIR.type()) && VanillaHudElements.isHidden(VanillaHudElements.CROSSHAIR_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.BOSS_EVENT_PROGRESS.type()) && VanillaHudElements.isHidden(VanillaHudElements.BOSS_BARS_IDENTIFIER)) {
            info.setReturnValue(true);
            return;
        }
        if ((overlay == VanillaGuiOverlay.POTION_ICONS.type()) && VanillaHudElements.isHidden(VanillaHudElements.EFFECTS_IDENTIFIER)) {
            info.setReturnValue(true);
        }

    }

}
