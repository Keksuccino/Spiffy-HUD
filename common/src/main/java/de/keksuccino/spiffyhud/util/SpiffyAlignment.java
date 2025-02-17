package de.keksuccino.spiffyhud.util;

import de.keksuccino.fancymenu.util.enums.LocalizedCycleEnum;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum SpiffyAlignment implements LocalizedCycleEnum<SpiffyAlignment> {

    TOP_LEFT("top_left"),
    TOP_CENTERED("top_centered"),
    TOP_RIGHT("top_right"),
    BOTTOM_LEFT("bottom_left"),
    BOTTOM_CENTERED("bottom_centered"),
    BOTTOM_RIGHT("bottom_right"),
    MID_LEFT("mid_left"),
    MID_CENTERED("mid_centered"),
    MID_RIGHT("mid_right");

    private final String name;

    SpiffyAlignment(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getLocalizationKeyBase() {
        return "spiffyhud.alignment";
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull Style getValueComponentStyle() {
        return WARNING_TEXT_STYLE.get();
    }

    @Override
    public @NotNull SpiffyAlignment[] getValues() {
        return SpiffyAlignment.values();
    }

    @Override
    @Nullable
    public SpiffyAlignment getByNameInternal(@NotNull String name) {
        return SpiffyAlignment.getByName(name);
    }

    @Nullable
    public static SpiffyAlignment getByName(@NotNull String name) {
        for (SpiffyAlignment a : SpiffyAlignment.values()) {
            if (a.name.equals(name)) return a;
        }
        return null;
    }

    /**
     * Aligns the body/content of an element within the element.<br>
     * Returns an array with the new BODY coordinates (not element coordinates!). The first number is X and the second is Y.
     */
    public static Integer[] calculateElementBodyPosition(@NotNull SpiffyAlignment spiffyAlignment, int elementX, int elementY, int elementWidth, int elementHeight, int bodyWidth, int bodyHeight) {
        int fixedX = elementX;
        int fixedY = elementY;
        if (spiffyAlignment == SpiffyAlignment.TOP_CENTERED) {
            fixedX = fixedX + (elementWidth / 2) - (bodyWidth / 2);
        } else if (spiffyAlignment == SpiffyAlignment.TOP_RIGHT) {
            fixedX = fixedX + elementWidth - bodyWidth;
        } else if (spiffyAlignment == SpiffyAlignment.MID_LEFT) {
            fixedY = fixedY + (elementHeight / 2) - (bodyHeight / 2);
        } else if (spiffyAlignment == SpiffyAlignment.MID_CENTERED) {
            fixedX = fixedX + (elementWidth / 2) - (bodyWidth / 2);
            fixedY = fixedY + (elementHeight / 2) - (bodyHeight / 2);
        } else if (spiffyAlignment == SpiffyAlignment.MID_RIGHT) {
            fixedX = fixedX + elementWidth - bodyWidth;
            fixedY = fixedY + (elementHeight / 2) - (bodyHeight / 2);
        } else if (spiffyAlignment == SpiffyAlignment.BOTTOM_LEFT) {
            fixedY = fixedY + elementHeight - bodyHeight;
        } else if (spiffyAlignment == SpiffyAlignment.BOTTOM_CENTERED) {
            fixedX = fixedX + (elementWidth / 2) - (bodyWidth / 2);
            fixedY = fixedY + elementHeight - bodyHeight;
        } else if (spiffyAlignment == SpiffyAlignment.BOTTOM_RIGHT) {
            fixedX = fixedX + elementWidth - bodyWidth;
            fixedY = fixedY + elementHeight - bodyHeight;
        }
        return new Integer[] { fixedX, fixedY };
    }

}
