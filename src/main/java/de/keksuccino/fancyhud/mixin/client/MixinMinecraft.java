package de.keksuccino.fancyhud.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;

@Mixin(value = Minecraft.class)
public class MixinMinecraft {

	@Shadow protected IngameGui ingameGUI;
	
	private static boolean ingameGuiUpdated = false;
	
	@Inject(at = @At(value = "HEAD"), method = "updateWindowSize")
	protected void onUpdateWindowSize(CallbackInfo info) {
		if (!ingameGuiUpdated) {
			this.ingameGUI = new CustomizableIngameGui((Minecraft) ((Object)this), false);
			ingameGuiUpdated = true;
		}
	}
	
}
