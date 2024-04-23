package de.keksuccino.spiffyhud;

import java.io.File;
import de.keksuccino.fancymenu.customization.layout.LayoutHandler;
import de.keksuccino.fancymenu.customization.overlay.CustomizationOverlay;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import de.keksuccino.spiffyhud.customization.backgrounds.Backgrounds;
import de.keksuccino.spiffyhud.customization.elements.Elements;
import de.keksuccino.spiffyhud.customization.placeholders.Placeholders;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.screen.identifier.UniversalScreenIdentifierRegistry;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.spiffyhud.platform.Services;
import de.keksuccino.fancymenu.util.file.FileUtils;
import de.keksuccino.fancymenu.util.file.GameDirectoryUtils;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class SpiffyHud {

	//TODO bei schließen von Screen -> HUD layout resize update triggern (falls window in screen resized wurde)

	//TODO make anchor overlay not work for when hovering Spiffy dummy elements

	//TODO disable Audio and MusicController elements in Spiffy layouts

	//TODO disable option to set if Vanilla elements should render behind or in front of custom elements in Spiffy layouts

	//TODO change mod icon to Spiffy icon

	//TODO change icon of customization button (is still Drippy icon)

	//TODO add option to disable powder snow overlay

	//TODO add option to disable pumpkin head overlay

	//TODO add option to disable vignette

	//TODO "Inventory Slot" element (shows the item in slot X)
	// - Bei "Set Slot" in Tooltip die Slots für Offhand, Armor, etc. packen
	// - Element ist scalable
	// - Element kann rotiert werden (falls möglich)

	//TODO "Current Player" element
	// - Like normal Player Entity element, but mimics the current player in-game player
	// - Has pose features of normal Player Entity element
	// - Has "Show Name" toggle
	// - Is scalable

	//TODO Add Vanilla-Like elements for all Vanilla HUD elements (crosshair, hotbar, scoreboard sidebar, effects overlay, Title (line 1 + 2), etc.)







	private static final Logger LOGGER = LogManager.getLogger();

	public static final String VERSION = "2.0.0";
	public static final String MOD_LOADER = Services.PLATFORM.getPlatformName();
	public static final String MOD_ID = "spiffyhud";
	public static final File MOD_DIR = createDirectory(new File(GameDirectoryUtils.getGameDirectory(), "/config/spiffyhud"));

	private static Options options;

	public static void earlyInit() {

		if (Services.PLATFORM.isOnClient()) {
			LOGGER.info("[SPIFFY HUD] Loading v" + VERSION + " in client-side mode on " + MOD_LOADER.toUpperCase() + "!");
		} else {
			LOGGER.info("[SPIFFY HUD] Loading v" + VERSION + " in server-side mode on " + MOD_LOADER.toUpperCase() + "!");
		}

		if (Services.PLATFORM.isOnClient()) {

			EventHandler.INSTANCE.registerListenersOf(new SpiffyEvents());

			//Register universal identifier for Drippy screen
			UniversalScreenIdentifierRegistry.register("spiffy_overlay", SpiffyOverlayScreen.class.getName());

			//Disable customization for all background config screens
			ScreenCustomization.addScreenBlacklistRule(s -> s.startsWith("de.keksuccino.drippyloadingscreen.customization.backgrounds."));

			//Register custom backgrounds
			Backgrounds.registerAll();

			//Register custom placeholders
			Placeholders.registerAll();

			//Register custom element types
			Elements.registerAll();

			//Disable universal layouts for the HUD / Spiffy Overlay
			LayoutHandler.registerUniversalLayoutInclusionRule(screenIdentifier -> !SpiffyUtils.isSpiffyIdentifier(screenIdentifier));

			//Don't show FancyMenu's customization overlay in the HUD
			CustomizationOverlay.registerOverlayVisibilityController(screen -> {
				if (Minecraft.getInstance().screen instanceof SpiffyOverlayScreen s) return s.showFancyMenuOverlay;
				return true;
			});

		}

	}

	public static Options getOptions() {
		if (options == null) {
			reloadOptions();
		}
		return options;
	}

	public static void reloadOptions() {
		options = new Options();
	}

	private static File createDirectory(@NotNull File directory) {
		return FileUtils.createDirectory(directory);
	}

}
