package de.keksuccino.spiffyhud;

import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import de.keksuccino.fancymenu.customization.screen.identifier.ScreenIdentifierHandler;
import org.jetbrains.annotations.Nullable;

public class SpiffyUtils {

    public static final SpiffyOverlayScreen DUMMY_SPIFFY_OVERLAY_SCREEN = new SpiffyOverlayScreen(false);

    public static boolean isSpiffyIdentifier(@Nullable String identifier) {
        if (identifier == null) return false;
        return ScreenIdentifierHandler.isIdentifierOfScreen(identifier, DUMMY_SPIFFY_OVERLAY_SCREEN);
    }

}
