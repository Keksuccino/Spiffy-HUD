package de.keksuccino.spiffyhud.events.hud;

import de.keksuccino.konkrete.events.EventBase;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

public class RenderHudEvent extends EventBase {

    public MatrixStack matrixStack;
    public InGameHud hud;

    public RenderHudEvent(MatrixStack matrix, InGameHud hud) {
        this.matrixStack = matrix;
        this.hud = hud;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    public static class Pre extends RenderHudEvent {

        public Pre(MatrixStack matrix, InGameHud hud) {
            super(matrix, hud);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }

    }

    public static class Post extends RenderHudEvent {

        public Post(MatrixStack matrix, InGameHud hud) {
            super(matrix, hud);
        }

    }

}
