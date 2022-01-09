package de.keksuccino.spiffyhud.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeIngameGui.class)
public class MixinForgeIngameGui {
	
	@Inject(at = @At(value = "HEAD"), method = "render")
	protected void onRenderIngameGuiPre(PoseStack matrix, float partialTicks, CallbackInfo info) {

		if (CustomizationHandler.isLightModeEnabled() && !(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) {

			CustomizationHandler.INGAME_GUI.renderBackgroundItems(matrix);
			
		}
		
	}
	
	@Inject(at = @At(value = "TAIL"), method = "render")
	protected void onRenderIngameGuiPost(PoseStack matrix, float partialTicks, CallbackInfo info) {
		
		if (CustomizationHandler.isLightModeEnabled() && !(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) {

			CustomizationHandler.INGAME_GUI.renderCustomVanillaElements(matrix, partialTicks);
			
			CustomizationHandler.INGAME_GUI.renderForegroundItems(matrix);
			
		}
		
	}

	@Inject(at = @At("RETURN"), method = "shouldDrawSurvivalElements", remap = false, cancellable = true)
	protected void onShouldDrawSurvivalElements(CallbackInfoReturnable<Boolean> info) {
		if ((Minecraft.getInstance() != null) && (Minecraft.getInstance().screen instanceof LayoutEditorScreen)) {
			info.setReturnValue(true);
		}
	}
	
}
