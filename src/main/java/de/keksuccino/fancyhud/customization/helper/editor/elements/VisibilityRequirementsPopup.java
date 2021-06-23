package de.keksuccino.fancyhud.customization.helper.editor.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.keksuccino.fancyhud.customization.helper.ui.popup.FHPopup;
import de.keksuccino.fancyhud.customization.items.CustomizationItemBase;
import de.keksuccino.fancyhud.customization.items.visibilityrequirements.VisibilityRequirementContainer;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.AdvancedImageButton;
import de.keksuccino.konkrete.gui.content.AdvancedTextField;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class VisibilityRequirementsPopup extends FHPopup {

    protected CustomizationItemBase parent;
    protected List<Requirement> requirements = new ArrayList<Requirement>();
    protected int currentRequirement = 0;

    protected AdvancedButton doneButton;
    protected AdvancedButton leftButton;
    protected AdvancedButton rightButton;

    public VisibilityRequirementsPopup(CustomizationItemBase parent) {
        super(240);
        this.parent = parent;

        this.doneButton = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.done"), true, (press) -> {
            this.setDisplayed(false);
        });
        this.addButton(this.doneButton);

        this.leftButton = new AdvancedImageButton(0, 0, 20, 20, new ResourceLocation("keksuccino", "arrow_left.png"), true, (press) -> {
            int i = this.currentRequirement - 1;
            if (i >= 0) {
                this.currentRequirement = i;
            }
        });
        this.addButton(this.leftButton);

        this.rightButton = new AdvancedImageButton(0, 0, 20, 20, new ResourceLocation("keksuccino", "arrow_right.png"), true, (press) -> {
            int i = this.currentRequirement + 1;
            if (i <= this.requirements.size() - 1) {
                this.currentRequirement = i;
            }
        });
        this.addButton(this.rightButton);

        this.initRequirements();
    }

    protected void initRequirements() {

        VisibilityRequirementContainer c = this.parent.visibilityRequirementContainer;

        /** Active Slot **/
        String activeSlotName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.activeslot");
        String activeSlotDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.activeslot.desc");
        String activeSlotValueName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.activeslot.valuename");
        Requirement activeSlot = new Requirement(this, activeSlotName, activeSlotDesc, activeSlotValueName, c.vrCheckForActiveSlot, c.vrShowIfActiveSlot,
                (enabledCallback) -> {
            c.vrCheckForActiveSlot = enabledCallback;
        }, (showIfCallback) -> {
            c.vrShowIfActiveSlot = showIfCallback;
        }, (valueCallback) -> {
            if ((valueCallback != null) && MathUtils.isInteger(valueCallback)) {
                c.vrActiveSlot = Integer.parseInt(valueCallback);
            } else {
                c.vrActiveSlot = 0;
            }
        }, CharacterFilter.getIntegerCharacterFiler(), "" + c.vrActiveSlot);
        this.requirements.add(activeSlot);

        /** Item In Main Hand **/
        String itemInMainHandName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.iteminmainhand");
        String itemInMainHandDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.iteminmainhand.desc");
        Requirement itemInMainHand = new Requirement(this, itemInMainHandName, itemInMainHandDesc, null, c.vrCheckForItemInMainHand, c.vrShowIfItemInMainHand,
                (enabledCallback) -> {
                    c.vrCheckForItemInMainHand = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfItemInMainHand = showIfCallback;
        }, null, null, null);
        this.requirements.add(itemInMainHand);

        /** Item In Off Hand **/
        String itemInOffHandName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.iteminoffhand");
        String itemInOffHandDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.iteminoffhand.desc");
        Requirement itemInOffHand = new Requirement(this, itemInOffHandName, itemInOffHandDesc, null, c.vrCheckForItemInOffHand, c.vrShowIfItemInOffHand,
                (enabledCallback) -> {
                    c.vrCheckForItemInOffHand = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfItemInOffHand = showIfCallback;
        }, null, null, null);
        this.requirements.add(itemInOffHand);

        //TODO active item type HERE

        /** Active Item Name **/
        String activeItemNameName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.activeitemname");
        String activeItemNameDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.activeitemname.desc");
        String activeItemNameValueName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.activeitemname.valuename");
        Requirement activeItemName = new Requirement(this, activeItemNameName, activeItemNameDesc, activeItemNameValueName, c.vrCheckForActiveItemName, c.vrShowIfActiveItemName,
                (enabledCallback) -> {
                    c.vrCheckForActiveItemName = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfActiveItemName = showIfCallback;
        }, (valueCallback) -> {
            c.vrActiveItemName = valueCallback;
        }, null, c.vrActiveItemName);
        this.requirements.add(activeItemName);

        /** Slot Item Name **/
        String slotItemNameName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.slotitemname");
        String slotItemNameDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.slotitemname.desc");
        String slotItemNameValueName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.slotitemname.valuename");
        Requirement slotItemName = new Requirement(this, slotItemNameName, slotItemNameDesc, slotItemNameValueName, c.vrCheckForSlotItemName, c.vrShowIfSlotItemName,
                (enabledCallback) -> {
                    c.vrCheckForSlotItemName = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfSlotItemName = showIfCallback;
        }, (valueCallback) -> {
            if (valueCallback.contains(":")) {
                String slotString = valueCallback.split("[:]", 2)[0];
                String itemNameString = valueCallback.split("[:]", 2)[1];
                if (MathUtils.isInteger(slotString)) {
                    c.vrSlotItemName = itemNameString;
                    c.vrSlotItemNameSlot = Integer.parseInt(slotString);
                }
            }
        }, null, c.vrSlotItemNameSlot + ":" + c.vrSlotItemName);
        this.requirements.add(slotItemName);

        /** Singleplayer **/
        String singleplayerName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.singleplayer");
        String singleplayerDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.singleplayer.desc");
        Requirement singleplayer = new Requirement(this, singleplayerName, singleplayerDesc, slotItemNameValueName, c.vrCheckForSingleplayer, c.vrShowIfSingleplayer,
                (enabledCallback) -> {
                    c.vrCheckForSingleplayer = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfSingleplayer = showIfCallback;
        }, null, null, null);
        this.requirements.add(singleplayer);

        /** Multiplayer **/
        String multiplayerName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.multiplayer");
        String multiplayerDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.multiplayer.desc");
        Requirement multiplayer = new Requirement(this, multiplayerName, multiplayerDesc, slotItemNameValueName, c.vrCheckForMultiplayer, c.vrShowIfMultiplayer,
                (enabledCallback) -> {
                    c.vrCheckForMultiplayer = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfMultiplayer = showIfCallback;
        }, null, null, null);
        this.requirements.add(multiplayer);

        /** Player On Ground **/
        String playerOnGroundName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playeronground");
        String playerOnGroundDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playeronground.desc");
        Requirement playerOnGround = new Requirement(this, playerOnGroundName, playerOnGroundDesc, slotItemNameValueName, c.vrCheckForPlayerOnGround, c.vrShowIfPlayerOnGround,
                (enabledCallback) -> {
                    c.vrCheckForPlayerOnGround = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfPlayerOnGround = showIfCallback;
        }, null, null, null);
        this.requirements.add(playerOnGround);

        /** Player Is In Water **/
        String playerIsInWaterName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playerisinwater");
        String playerIsInWaterDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playerisinwater.desc");
        Requirement playerIsInWater = new Requirement(this, playerIsInWaterName, playerIsInWaterDesc, slotItemNameValueName, c.vrCheckForPlayerIsInWater, c.vrShowIfPlayerIsInWater,
                (enabledCallback) -> {
                    c.vrCheckForPlayerIsInWater = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfPlayerIsInWater = showIfCallback;
        }, null, null, null);
        this.requirements.add(playerIsInWater);

        /** Player Underwater **/
        String playerUnderwaterName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playerunderwater");
        String playerUnderwaterDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playerunderwater.desc");
        Requirement playerUnderwater = new Requirement(this, playerUnderwaterName, playerUnderwaterDesc, slotItemNameValueName, c.vrCheckForPlayerUnderwater, c.vrShowIfPlayerUnderwater,
                (enabledCallback) -> {
                    c.vrCheckForPlayerUnderwater = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfPlayerUnderwater = showIfCallback;
        }, null, null, null);
        this.requirements.add(playerUnderwater);

        /** Player Is Riding Horse **/
        String playerIsRidingHorseName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playerisridinghorse");
        String playerIsRidingHorseDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playerisridinghorse.desc");
        Requirement playerIsRidingHorse = new Requirement(this, playerIsRidingHorseName, playerIsRidingHorseDesc, slotItemNameValueName, c.vrCheckForPlayerIsRidingHorse, c.vrShowIfPlayerIsRidingHorse,
                (enabledCallback) -> {
                    c.vrCheckForPlayerIsRidingHorse = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfPlayerIsRidingHorse = showIfCallback;
        }, null, null, null);
        this.requirements.add(playerIsRidingHorse);

        /** Player Is Riding Entity **/
        String playerIsRidingEntityName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playerisridingentity");
        String playerIsRidingEntityDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playerisridingentity.desc");
        Requirement playerIsRidingEntitiy = new Requirement(this, playerIsRidingEntityName, playerIsRidingEntityDesc, slotItemNameValueName, c.vrCheckForPlayerIsRidingEntity, c.vrShowIfPlayerIsRidingEntity,
                (enabledCallback) -> {
                    c.vrCheckForPlayerIsRidingEntity = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfPlayerIsRidingEntity = showIfCallback;
        }, null, null, null);
        this.requirements.add(playerIsRidingEntitiy);

        /** Player Is Running **/
        String playerIsRunningName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playerisrunning");
        String playerIsRunningDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.playerisrunning.desc");
        Requirement playerIsRunning = new Requirement(this, playerIsRunningName, playerIsRunningDesc, slotItemNameValueName, c.vrCheckForPlayerIsRunning, c.vrShowIfPlayerIsRunning,
                (enabledCallback) -> {
                    c.vrCheckForPlayerIsRunning = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfPlayerIsRunning = showIfCallback;
        }, null, null, null);
        this.requirements.add(playerIsRunning);

        /** Is Debug Open **/
        String debugOpenName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.debugopen");
        String debugOpenDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.debugopen.desc");
        Requirement debugOpen = new Requirement(this, debugOpenName, debugOpenDesc, slotItemNameValueName, c.vrCheckForDebugOpen, c.vrShowIfDebugOpen,
                (enabledCallback) -> {
                    c.vrCheckForDebugOpen = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfDebugOpen = showIfCallback;
        }, null, null, null);
        this.requirements.add(debugOpen);

        /** Is Game Paused **/
        String gamePausedName = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.gamepaused");
        String gamePausedDesc = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.gamepaused.desc");
        Requirement gamePaused = new Requirement(this, gamePausedName, gamePausedDesc, slotItemNameValueName, c.vrCheckForGamePaused, c.vrShowIfGamePaused,
                (enabledCallback) -> {
                    c.vrCheckForGamePaused = enabledCallback;
                }, (showIfCallback) -> {
            c.vrShowIfGamePaused = showIfCallback;
        }, null, null, null);
        this.requirements.add(gamePaused);

    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, Screen renderIn) {
        super.render(matrix, mouseX, mouseY, renderIn);

        int centerX = renderIn.width / 2;
        int centerY = renderIn.height / 2;

        this.doneButton.x = centerX - (this.doneButton.getWidth() / 2);
        this.doneButton.y = centerY + 50;

        this.leftButton.x = centerX - this.leftButton.getWidth() - 135;
        this.leftButton.y = centerY - (this.leftButton.getHeight() / 2);

        this.rightButton.x = centerX + 135;
        this.rightButton.y = centerY - (this.leftButton.getHeight() / 2);

        Requirement r = this.requirements.get(this.currentRequirement);
        if (r != null) {
            r.render(matrix, mouseX, mouseY, renderIn);
        }

        this.renderButtons(matrix, mouseX, mouseY);
    }

    public static class Requirement extends AbstractGui {

        protected VisibilityRequirementsPopup parent;
        protected String name;
        protected String desc;
        protected String valueName;
        protected Consumer<Boolean> enabledCallback;
        protected Consumer<Boolean> showIfCallback;
        protected Consumer<String> valueCallback;
        protected CharacterFilter valueFilter;
        protected boolean enabled;
        protected boolean showIf;
        protected String valueString;

        protected List<Runnable> preRenderTasks = new ArrayList<Runnable>();
        protected List<AdvancedButton> buttonList = new ArrayList<AdvancedButton>();

        protected AdvancedButton enableRequirementButton;
        protected AdvancedButton showIfButton;
        protected AdvancedButton showIfNotButton;
        protected AdvancedTextField valueTextField;

        public Requirement(VisibilityRequirementsPopup parent, String name, String desc, @Nullable String valueName, boolean enabled, boolean showIf, Consumer<Boolean> enabledCallback, Consumer<Boolean> showIfCallback, @Nullable Consumer<String> valueCallback, CharacterFilter valueFilter, String valueString) {
            this.parent = parent;
            this.name = name;
            this.desc = desc;
            this.valueName = valueName;
            this.enabledCallback = enabledCallback;
            this.showIfCallback = showIfCallback;
            this.valueCallback = valueCallback;
            this.valueFilter = valueFilter;
            this.enabled = enabled;
            this.showIf = showIf;
            this.valueString = valueString;
            this.init();
        }

        protected void init() {

            /** Toggle Requirement Button **/
            String enabledString = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.enabled", this.name);
            if (!this.enabled) {
                enabledString = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.disabled", this.name);
            }
            this.enableRequirementButton = new AdvancedButton(0, 0, 150, 20, enabledString, true, (press) -> {
                if (this.enabled) {
                    this.enabled = false;
                    this.enabledCallback.accept(false);
                    ((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.disabled", this.name));
                } else {
                    this.enabled = true;
                    this.enabledCallback.accept(true);
                    ((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.enabled", this.name));
                }
            });
            List<String> descLines = new ArrayList<String>();
            descLines.addAll(Arrays.asList(StringUtils.splitLines(this.desc, "%n%")));
            descLines.add("");
            descLines.add(Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.toggle.btn.desc"));
            this.enableRequirementButton.setDescription(descLines.toArray(new String[0]));
            this.preRenderTasks.add(() -> enableRequirementButton.setWidth(Minecraft.getInstance().fontRenderer.getStringPropertyWidth(enableRequirementButton.getMessage()) + 10));
            this.addButton(this.enableRequirementButton);

            /** Show If Button **/
            String showIfString = "§a" + Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.showif");
            if (!this.showIf) {
                showIfString = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.showif");
            }
            this.showIfButton = new AdvancedButton(0, 0, 100, 20, showIfString, true, (press) -> {
                this.showIf = true;
                this.showIfCallback.accept(true);
                ((AdvancedButton)press).setMessage("§a" + Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.showif"));
                this.showIfNotButton.setMessage(Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.showifnot"));
            });
            this.showIfButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.showif.btn.desc"), "%n%"));
            this.addButton(this.showIfButton);

            /** Show If Not Button **/
            String showIfNotString = "§a" + Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.showifnot");
            if (this.showIf) {
                showIfNotString = Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.showifnot");
            }
            this.showIfNotButton = new AdvancedButton(0, 0, 100, 20, showIfNotString, true, (press) -> {
                this.showIf = false;
                this.showIfCallback.accept(false);
                ((AdvancedButton)press).setMessage("§a" + Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.showifnot"));
                this.showIfButton.setMessage(Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.showif"));
            });
            this.showIfNotButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.showifnot.btn.desc"), "%n%"));
            this.addButton(this.showIfNotButton);

            if ((this.valueCallback != null) && (this.valueName != null)) {
                this.valueTextField = new AdvancedTextField(Minecraft.getInstance().fontRenderer, 0, 0, 150, 20, true, this.valueFilter);
                this.valueTextField.setCanLoseFocus(true);
                this.valueTextField.setFocused2(false);
                this.valueTextField.setMaxStringLength(1000);
                if (this.valueString != null) {
                    this.valueTextField.setText(this.valueString);
                }
            }

        }

        public void render(MatrixStack matrix, int mouseX, int mouseY, Screen renderIn) {

            for (Runnable r : this.preRenderTasks) {
                r.run();
            }

            float partial = Minecraft.getInstance().getRenderPartialTicks();
            int centerX = renderIn.width / 2;
            int centerY = renderIn.height / 2;

            drawCenteredString(matrix, Minecraft.getInstance().fontRenderer, Locals.localize("fancyhud.helper.creator.items.visibilityrequirements.requirement") + ":", centerX, centerY - 83, -1);
            this.enableRequirementButton.x = centerX - (this.enableRequirementButton.getWidth() / 2);
            this.enableRequirementButton.y = centerY - 70;

            this.showIfButton.x = centerX - this.showIfButton.getWidth() - 5;
            this.showIfButton.y = centerY - 40;
            this.showIfNotButton.active = this.enabled;

            this.showIfNotButton.x = centerX + 5;
            this.showIfNotButton.y = centerY - 40;
            this.showIfButton.active = this.enabled;

            if (this.valueTextField != null) {
                drawCenteredString(matrix, Minecraft.getInstance().fontRenderer, this.valueName + ":", centerX, centerY - 10, -1);

                this.valueTextField.x = centerX - (this.valueTextField.getWidth() / 2);
                this.valueTextField.y = centerY + 3;
                this.valueTextField.render(matrix, mouseX, mouseY, partial);
                this.valueTextField.active = this.enabled;
                this.valueTextField.setEnabled(this.enabled);
                this.valueCallback.accept(this.valueTextField.getText());
                this.valueString = this.valueTextField.getText();
            }

            this.renderButtons(matrix, mouseX, mouseY, partial);

        }

        protected void renderButtons(MatrixStack matrix, int mouseX, int mouseY, float partial) {
            for (AdvancedButton b : this.buttonList) {
                b.render(matrix, mouseX, mouseY, partial);
            }
        }

        protected void addButton(AdvancedButton b) {
            if (!this.buttonList.contains(b)) {
                this.buttonList.add(b);
                b.ignoreBlockedInput = true;
                this.parent.colorizePopupButton(b);
            }
        }

    }

}
