package de.keksuccino.spiffyhud.mixin.client;

import de.keksuccino.konkrete.reflection.ReflectionHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import java.lang.reflect.Field;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraft {

	private static boolean ingameGuiUpdated = false;

	@Inject(at = @At(value = "TAIL"), method = "<init>")
	protected void onConstruct(CallbackInfo info) {

		if (!ingameGuiUpdated) {
			if (!CustomizationHandler.isLightModeEnabled()) {
				setCustomizableGuiInstance((MinecraftClient)((Object)this), CustomizationHandler.INGAME_GUI);
			}
			ingameGuiUpdated = true;
		}

	}

	private static void setCustomizableGuiInstance(MinecraftClient mc, InGameHud gui) {
		try {
			Field f = ReflectionHelper.findField(MinecraftClient.class, "inGameHud", "field_1705");
			f.set(mc, gui);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
