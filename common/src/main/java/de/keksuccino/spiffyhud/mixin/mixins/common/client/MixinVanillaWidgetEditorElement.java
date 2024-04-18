package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.keksuccino.fancymenu.customization.element.HideableElement;
import de.keksuccino.fancymenu.customization.element.elements.button.custombutton.ButtonEditorElement;
import de.keksuccino.fancymenu.customization.element.elements.button.vanillawidget.VanillaWidgetEditorElement;
import de.keksuccino.fancymenu.customization.element.elements.button.vanillawidget.VanillaWidgetElement;
import de.keksuccino.fancymenu.customization.widget.WidgetMeta;
import de.keksuccino.fancymenu.util.rendering.ui.contextmenu.v2.ContextMenu;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VanillaWidgetEditorElement.class)
public abstract class MixinVanillaWidgetEditorElement extends ButtonEditorElement implements HideableElement {

    //unused dummy constructor
    @SuppressWarnings("all")
    private MixinVanillaWidgetEditorElement() {
        super(null, null);
    }

    //Disable most customization stuff for Spiffy's dummy Vanilla elements
    @Inject(method = "init", at = @At("HEAD"), remap = false)
    private void beforeInit_Spiffy(CallbackInfo info) {

        if (this.isSpiffyHudElement()) {

            this.settings.setSkipReInitAfterSettingsChanged(true);

            this.settings.setMovable(false);
            this.settings.setResizeable(false);
            this.settings.setAnchorPointChangeable(false);
            this.settings.setAdvancedPositioningSupported(false);
            this.settings.setAdvancedSizingSupported(false);
            this.settings.setDelayable(false);
            this.settings.setLoadingRequirementsEnabled(false);
            this.settings.setFadeable(false);
            this.settings.setIdentifierCopyable(false);
            this.settings.setStretchable(false);

            this.settings.setSkipReInitAfterSettingsChanged(false);

        }

    }

    //Don't add the "Copy Widget Locator" button to Spiffy's dummy Vanilla elements
    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lde/keksuccino/fancymenu/util/rendering/ui/contextmenu/v2/ContextMenu;addClickableEntryAfter(Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/network/chat/Component;Lde/keksuccino/fancymenu/util/rendering/ui/contextmenu/v2/ContextMenu$ClickableContextMenuEntry$ClickAction;)Lde/keksuccino/fancymenu/util/rendering/ui/contextmenu/v2/ContextMenu$ClickableContextMenuEntry;"), remap = false)
    private @NotNull ContextMenu.ClickableContextMenuEntry<?> wrapAddClickableEntryAfter_Spiffy(ContextMenu instance, @NotNull String addAfterIdentifier, @NotNull String identifier, @NotNull Component label, @NotNull ContextMenu.ClickableContextMenuEntry.ClickAction clickAction, Operation<ContextMenu.ClickableContextMenuEntry<?>> original) {
        if ("copy_vanilla_widget_locator".equals(identifier) && this.isSpiffyHudElement()) {
            ContextMenu.ClickableContextMenuEntry<?> dummyEntry = instance.addClickableEntry(identifier, label, clickAction);
            instance.removeEntry(identifier);
            return dummyEntry;
        }
        return instance.addClickableEntryAfter(addAfterIdentifier, identifier, label, clickAction);
    }

    @Unique
    private boolean isSpiffyHudElement() {
        WidgetMeta meta = ((VanillaWidgetElement)this.element).widgetMeta;
        if (meta == null) return false;
        String compId = meta.getUniversalIdentifier();
        return ((compId != null) && compId.startsWith("spiffy_"));
    }

}
