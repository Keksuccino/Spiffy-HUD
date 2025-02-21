package de.keksuccino.spiffyhud.customization.placeholders;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.spiffyhud.util.player.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class PlayerItemUseProgressPlaceholder extends Placeholder {

    public PlayerItemUseProgressPlaceholder() {
        super("player_item_use_progress");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        return String.valueOf(PlayerUtils.getItemUseProgress(Minecraft.getInstance().player));
    }

    @Override
    public @Nullable List<String> getValueNames() {
        return null;
    }

    @Override
    public @NotNull String getDisplayName() {
        return I18n.get("spiffyhud.placeholders.player_item_use_progress");
    }

    @Override
    public List<String> getDescription() {
        return List.of(LocalizationUtils.splitLocalizedStringLines("spiffyhud.placeholders.player_item_use_progress.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.placeholders.categories.world");
    }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        return new DeserializedPlaceholderString(this.getIdentifier(), null, "");
    }

}
