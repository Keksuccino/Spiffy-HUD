package de.keksuccino.spiffyhud.util;

import de.keksuccino.fancymenu.util.enums.LocalizedCycleEnum;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Alignment implements LocalizedCycleEnum<Alignment> {

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

    Alignment(String name) {
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
    public @NotNull Alignment[] getValues() {
        return Alignment.values();
    }

    @Override
    @Nullable
    public Alignment getByNameInternal(@NotNull String name) {
        return Alignment.getByName(name);
    }

    @Nullable
    public static Alignment getByName(@NotNull String name) {
        for (Alignment a : Alignment.values()) {
            if (a.name.equals(name)) return a;
        }
        return null;
    }

    /**
     * Aligns the body/content of an element within the element.<br>
     * Returns an array with the new BODY coordinates (not element coordinates!). The first number is X and the second is Y.
     */
    public static Integer[] calculateElementBodyPosition(@NotNull Alignment alignment, int elementX, int elementY, int elementWidth, int elementHeight, int bodyWidth, int bodyHeight) {
        int fixedX = elementX;
        int fixedY = elementY;
        if (alignment == Alignment.TOP_CENTERED) {
            fixedX = fixedX + (elementWidth / 2) - (bodyWidth / 2);
        } else if (alignment == Alignment.TOP_RIGHT) {
            fixedX = fixedX + elementWidth - bodyWidth;
        } else if (alignment == Alignment.MID_LEFT) {
            fixedY = fixedY + (elementHeight / 2) - (bodyHeight / 2);
        } else if (alignment == Alignment.MID_CENTERED) {
            fixedX = fixedX + (elementWidth / 2) - (bodyWidth / 2);
            fixedY = fixedY + (elementHeight / 2) - (bodyHeight / 2);
        } else if (alignment == Alignment.MID_RIGHT) {
            fixedX = fixedX + elementWidth - bodyWidth;
            fixedY = fixedY + (elementHeight / 2) - (bodyHeight / 2);
        } else if (alignment == Alignment.BOTTOM_LEFT) {
            fixedY = fixedY + elementHeight - bodyHeight;
        } else if (alignment == Alignment.BOTTOM_CENTERED) {
            fixedX = fixedX + (elementWidth / 2) - (bodyWidth / 2);
            fixedY = fixedY + elementHeight - bodyHeight;
        } else if (alignment == Alignment.BOTTOM_RIGHT) {
            fixedX = fixedX + elementWidth - bodyWidth;
            fixedY = fixedY + elementHeight - bodyHeight;
        }
        return new Integer[] { fixedX, fixedY };
    }

}
