package de.keksuccino.spiffyhud.events.hud;

import de.keksuccino.konkrete.events.EventBase;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

public class RenderSidebarEvent extends EventBase {

    public MatrixStack matrixStack;
    public InGameHud hud;

    public RenderSidebarEvent(MatrixStack matrixStack, InGameHud hud) {
        this.matrixStack = matrixStack;
        this.hud = hud;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    public static class Pre extends RenderSidebarEvent {

        public Pre(MatrixStack matrixStack, InGameHud hud) {
            super(matrixStack, hud);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }

    }

    public static class Post extends RenderSidebarEvent {

        public Post(MatrixStack matrixStack, InGameHud hud) {
            super(matrixStack, hud);
        }

    }

}
