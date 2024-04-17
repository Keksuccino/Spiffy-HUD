package de.keksuccino.spiffyhud;

import java.io.File;
import de.keksuccino.fancymenu.customization.layout.LayoutHandler;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class SpiffyHud {

	//TODO HIDE FM MENU BAR WHEN NOT IN "EDIT" MODE
	//TODO HIDE FM MENU BAR WHEN NOT IN "EDIT" MODE
	//TODO HIDE FM MENU BAR WHEN NOT IN "EDIT" MODE
	//TODO HIDE FM MENU BAR WHEN NOT IN "EDIT" MODE
	//TODO HIDE FM MENU BAR WHEN NOT IN "EDIT" MODE

	//TODO render custom elements ALWAYS behind all default Vanilla elements ( to not cover something important )

	//TODO add option to disable powder snow overlay

	//TODO add option to disable pumpkin head overlay

	//TODO add option to disable vignette








	private static final Logger LOGGER = LogManager.getLogger();

	public static final String VERSION = "2.0.0";
	public static final String MOD_LOADER = Services.PLATFORM.getPlatformName();
	public static final String MOD_ID = "spiffyhud";
	public static final File MOD_DIR = createDirectory(new File(GameDirectoryUtils.getGameDirectory(), "/config/spiffyhud"));

	private static Options options;
	private static boolean initialized = false;

	public static void init() {

		if (initialized) return;
		initialized = true;

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
