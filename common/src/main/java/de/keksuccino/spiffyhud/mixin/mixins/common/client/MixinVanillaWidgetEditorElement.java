package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import de.keksuccino.fancymenu.customization.element.HideableElement;
import de.keksuccino.fancymenu.customization.element.elements.button.custombutton.ButtonEditorElement;
import de.keksuccino.fancymenu.customization.element.elements.button.vanillawidget.VanillaWidgetEditorElement;
import de.keksuccino.fancymenu.customization.element.elements.button.vanillawidget.VanillaWidgetElement;
import de.keksuccino.fancymenu.customization.widget.WidgetMeta;
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

        if (this.isSpiffyDummyElement_Spiffy()) {

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

    /**
     * @reason Remove the "Copy Widget Locator" option from Spiffy Dummy elements
     */
    @Inject(method = "init", at = @At("RETURN"), remap = false)
    private void after_init_Spiffy(CallbackInfo info) {

        this.rightClickMenu.removeEntry("copy_vanilla_widget_locator");

    }

    @Unique
    private boolean isSpiffyDummyElement_Spiffy() {
        WidgetMeta meta = ((VanillaWidgetElement)this.element).widgetMeta;
        if (meta == null) return false;
        String compId = meta.getUniversalIdentifier();
        return ((compId != null) && compId.startsWith("spiffy_") && compId.endsWith("_dummy"));
    }

}
