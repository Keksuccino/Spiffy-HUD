package de.keksuccino.spiffyhud;

import de.keksuccino.fancymenu.util.AbstractOptions;
import de.keksuccino.konkrete.config.Config;

public class Options extends AbstractOptions {

    protected final Config config = new Config(SpiffyHud.MOD_DIR.getAbsolutePath().replace("\\", "/") + "/options.txt");

    public final Option<Boolean> placeholderOption = new Option<>(config, "placeholder_option", true, "general");

    public Options() {
        this.config.syncConfig();
        this.config.clearUnusedValues();
    }

}
