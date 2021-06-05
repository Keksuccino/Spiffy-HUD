package de.keksuccino.fancyhud.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.fancyhud.customization.CustomizationHandler;
import de.keksuccino.fancyhud.customization.helper.editor.LayoutEditorScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.ForgeIngameGui;

@Mixin(ForgeIngameGui.class)
public class MixinForgeIngameGui {
	
	@Inject(at = @At(value = "HEAD"), method = "renderIngameGui")
	protected void onRenderIngameGuiPre(MatrixStack matrix, float partialTicks, CallbackInfo info) {

		if (CustomizationHandler.isLightModeEnabled() && !(Minecraft.getInstance().currentScreen instanceof LayoutEditorScreen)) {

			CustomizationHandler.INGAME_GUI.renderBackgroundItems(matrix);
			
		}
		
	}
	
	@Inject(at = @At(value = "TAIL"), method = "renderIngameGui")
	protected void onRenderIngameGuiPost(MatrixStack matrix, float partialTicks, CallbackInfo info) {
		
		if (CustomizationHandler.isLightModeEnabled() && !(Minecraft.getInstance().currentScreen instanceof LayoutEditorScreen)) {

			CustomizationHandler.INGAME_GUI.renderCustomVanillaElements(matrix, partialTicks);
			
			CustomizationHandler.INGAME_GUI.renderForegroundItems(matrix);
			
		}
		
	}
	
}
