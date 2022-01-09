package de.keksuccino.spiffyhud.mixin.client;

import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

	private static boolean ingameGuiUpdated = false;

	//TODO EXPERIMENTAL HUD INJECTION

	@Inject(at = @At(value = "TAIL"), method = "<init>")
	protected void onConstruct(CallbackInfo info) {

		if (!ingameGuiUpdated) {
			if (!CustomizationHandler.isLightModeEnabled()) {
				setCustomizableGuiInstance((Minecraft)((Object)this), CustomizationHandler.INGAME_GUI);
			}
			ingameGuiUpdated = true;
		}

	}

	private static void setCustomizableGuiInstance(Minecraft mc, Gui gui) {
		try {
			Field f = ObfuscationReflectionHelper.findField(Minecraft.class, "f_91065_");
			f.set(mc, gui);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
