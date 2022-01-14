package de.keksuccino.spiffyhud.events;

import de.keksuccino.konkrete.events.EventBase;

public class MouseInputEvent extends EventBase {

    public int button;
    public int action;
    public int mods;

    public MouseInputEvent(int button, int action, int mods) {
        this.button = button;
        this.action = action;
        this.mods = mods;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

}
