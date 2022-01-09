package de.keksuccino.spiffyhud.customization.helper.ui.content;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.spiffyhud.api.DynamicValueRegistry;
import de.keksuccino.spiffyhud.api.DynamicValueRegistry.DynamicValue;
import de.keksuccino.spiffyhud.customization.helper.ui.UIBase;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.AdvancedImageButton;
import de.keksuccino.konkrete.gui.content.AdvancedTextField;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class DynamicValueTextfield extends AdvancedTextField {

	private AdvancedImageButton variableButton;
	private FHContextMenu variableMenu;
	
	private static final ResourceLocation VARIABLES_BUTTON_RESOURCE = new ResourceLocation("spiffyhud", "add_btn.png");
	
	public DynamicValueTextfield(FontRenderer fontrenderer, int x, int y, int width, int height, boolean handleTextField, CharacterFilter filter) {
		super(fontrenderer, x, y, width, height, handleTextField, filter);
		
		variableMenu = new FHContextMenu();
		variableMenu.setAutoclose(true);
		
		/** PLAYER CATEGORY **/
		FHContextMenu playerMenu = new FHContextMenu();
		playerMenu.setAutoclose(true);
		variableMenu.addChild(playerMenu);
		
		AdvancedButton playerName = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.playername"), true, (press) -> {
			this.writeText("%playername%");
		});
		playerName.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.playername.desc"), "%n%"));
		UIBase.colorizeButton(playerName);
		playerMenu.addContent(playerName);
		
		AdvancedButton playerUUID = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.playeruuid"), true, (press) -> {
			this.writeText("%playeruuid%");
		});
		playerUUID.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.playeruuid.desc"), "%n%"));
		UIBase.colorizeButton(playerUUID);
		playerMenu.addContent(playerUUID);
		
		playerMenu.addSeparator();
		
		AdvancedButton currentHealth = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.currenthealth"), true, (press) -> {
			this.writeText("%currenthealth%");
		});
		UIBase.colorizeButton(currentHealth);
		playerMenu.addContent(currentHealth);
		
		AdvancedButton maxHealth = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.maxhealth"), true, (press) -> {
			this.writeText("%maxhealth%");
		});
		UIBase.colorizeButton(maxHealth);
		playerMenu.addContent(maxHealth);
		
		AdvancedButton currentFood = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.currentfood"), true, (press) -> {
			this.writeText("%currentfood%");
		});
		UIBase.colorizeButton(currentFood);
		playerMenu.addContent(currentFood);
		
		AdvancedButton currentAir = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.currentair"), true, (press) -> {
			this.writeText("%currentair%");
		});
		UIBase.colorizeButton(currentAir);
		playerMenu.addContent(currentAir);
		
		AdvancedButton maxAir = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.maxair"), true, (press) -> {
			this.writeText("%maxair%");
		});
		UIBase.colorizeButton(maxAir);
		playerMenu.addContent(maxAir);
		
		AdvancedButton currentArmor = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.currentarmor"), true, (press) -> {
			this.writeText("%currentarmor%");
		});
		UIBase.colorizeButton(currentArmor);
		playerMenu.addContent(currentArmor);
		
		AdvancedButton currentScore = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.currentscore"), true, (press) -> {
			this.writeText("%currentscore%");
		});
		UIBase.colorizeButton(currentScore);
		playerMenu.addContent(currentScore);
		
		playerMenu.addSeparator();
		
		AdvancedButton posX = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.posx"), true, (press) -> {
			this.writeText("%posx%");
		});
		UIBase.colorizeButton(posX);
		playerMenu.addContent(posX);
		
		AdvancedButton posY = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.posy"), true, (press) -> {
			this.writeText("%posy%");
		});
		UIBase.colorizeButton(posY);
		playerMenu.addContent(posY);
		
		AdvancedButton posZ = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.posz"), true, (press) -> {
			this.writeText("%posz%");
		});
		UIBase.colorizeButton(posZ);
		playerMenu.addContent(posZ);
		
		AdvancedButton direction = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.direction"), true, (press) -> {
			this.writeText("%direction%");
		});
		UIBase.colorizeButton(direction);
		playerMenu.addContent(direction);
		
		AdvancedButton biome = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.biome"), true, (press) -> {
			this.writeText("%biome%");
		});
		UIBase.colorizeButton(biome);
		playerMenu.addContent(biome);
		
		playerMenu.addSeparator();
		
		AdvancedButton experience = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.experience"), true, (press) -> {
			this.writeText("%experience%");
		});
		UIBase.colorizeButton(experience);
		playerMenu.addContent(experience);
		
		AdvancedButton level = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.level"), true, (press) -> {
			this.writeText("%level%");
		});
		UIBase.colorizeButton(level);
		playerMenu.addContent(level);
		
		playerMenu.addSeparator();
		
		AdvancedButton mainhandName = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.mhitemname"), true, (press) -> {
			this.writeText("%mhitemname%");
		});
		UIBase.colorizeButton(mainhandName);
		playerMenu.addContent(mainhandName);
		
		AdvancedButton mainhandCurDurab = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.mhitemcurdurab"), true, (press) -> {
			this.writeText("%mhitemcurdurab%");
		});
		UIBase.colorizeButton(mainhandCurDurab);
		playerMenu.addContent(mainhandCurDurab);
		
		AdvancedButton mainhandMaxDurab = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.mhitemmaxdurab"), true, (press) -> {
			this.writeText("%mhitemmaxdurab%");
		});
		UIBase.colorizeButton(mainhandMaxDurab);
		playerMenu.addContent(mainhandMaxDurab);
		
		playerMenu.addSeparator();
		
		AdvancedButton offhandName = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.ohitemname"), true, (press) -> {
			this.writeText("%ohitemname%");
		});
		UIBase.colorizeButton(offhandName);
		playerMenu.addContent(offhandName);
		
		AdvancedButton offhandCurDurab = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.ohitemcurdurab"), true, (press) -> {
			this.writeText("%ohitemcurdurab%");
		});
		UIBase.colorizeButton(offhandCurDurab);
		playerMenu.addContent(offhandCurDurab);
		
		AdvancedButton offhandMaxDurab = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.ohitemmaxdurab"), true, (press) -> {
			this.writeText("%ohitemmaxdurab%");
		});
		UIBase.colorizeButton(offhandMaxDurab);
		playerMenu.addContent(offhandMaxDurab);
		
		playerMenu.addSeparator();
		
		AdvancedButton targettedBlock = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.targetblock"), true, (press) -> {
			this.writeText("%targetblock%");
		});
		UIBase.colorizeButton(targettedBlock);
		playerMenu.addContent(targettedBlock);

		AdvancedButton playerCategoryButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.categories.player"), true, (press) -> {
			playerMenu.setParentButton((AdvancedButton) press);
			playerMenu.openMenuAt(0, press.y);
		});
		UIBase.colorizeButton(playerCategoryButton);
		variableMenu.addContent(playerCategoryButton);
		
		/** SERVER CATEGORY **/
		FHContextMenu serverMenu = new FHContextMenu();
		serverMenu.setAutoclose(true);
		variableMenu.addChild(serverMenu);
		
		AdvancedButton serverIp = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.serverip"), true, (press) -> {
			this.writeText("%serverip%");
		});
		UIBase.colorizeButton(serverIp);
		serverMenu.addContent(serverIp);
		
		AdvancedButton serverMotd = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.servermotd"), true, (press) -> {
			this.writeText("%servermotd%");
		});
		UIBase.colorizeButton(serverMotd);
		serverMenu.addContent(serverMotd);
		
		AdvancedButton serverPing = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.serverping"), true, (press) -> {
			this.writeText("%serverping%");
		});
		UIBase.colorizeButton(serverPing);
		serverMenu.addContent(serverPing);
		
		AdvancedButton serverPlayers = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.serverplayers"), true, (press) -> {
			this.writeText("%serverplayers%");
		});
		UIBase.colorizeButton(serverPlayers);
		serverMenu.addContent(serverPlayers);
		
		AdvancedButton serverMods = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.servermods"), true, (press) -> {
			this.writeText("%servermods%");
		});
		UIBase.colorizeButton(serverMods);
		serverMenu.addContent(serverMods);
		
		AdvancedButton serverCategoryButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.categories.server"), true, (press) -> {
			serverMenu.setParentButton((AdvancedButton) press);
			serverMenu.openMenuAt(0, press.y);
		});
		UIBase.colorizeButton(serverCategoryButton);
		variableMenu.addContent(serverCategoryButton);
		
		/** WORLD CATEGORY **/
		FHContextMenu worldMenu = new FHContextMenu();
		worldMenu.setAutoclose(true);
		variableMenu.addChild(worldMenu);
		
		AdvancedButton gameDaytimeHours = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.daytimehours"), true, (press) -> {
			this.writeText("%daytimehours%");
		});
		UIBase.colorizeButton(gameDaytimeHours);
		worldMenu.addContent(gameDaytimeHours);
		
		AdvancedButton gameDaytimeMinutes = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.daytimeminutes"), true, (press) -> {
			this.writeText("%daytimeminutes%");
		});
		UIBase.colorizeButton(gameDaytimeMinutes);
		worldMenu.addContent(gameDaytimeMinutes);
		
		AdvancedButton worldCategoryButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.categories.world"), true, (press) -> {
			worldMenu.setParentButton((AdvancedButton) press);
			worldMenu.openMenuAt(0, press.y);
		});
		UIBase.colorizeButton(worldCategoryButton);
		variableMenu.addContent(worldCategoryButton);
		
		/** REAL TIME CATEGORY **/
		FHContextMenu realtimeMenu = new FHContextMenu();
		realtimeMenu.setAutoclose(true);
		variableMenu.addChild(realtimeMenu);
		
		AdvancedButton realtimeYear = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.realtimeyear"), true, (press) -> {
			this.writeText("%realtimeyear%");
		});
		UIBase.colorizeButton(realtimeYear);
		realtimeMenu.addContent(realtimeYear);
		
		AdvancedButton realtimeMonth = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.realtimemonth"), true, (press) -> {
			this.writeText("%realtimemonth%");
		});
		UIBase.colorizeButton(realtimeMonth);
		realtimeMenu.addContent(realtimeMonth);
		
		AdvancedButton realtimeDay = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.realtimeday"), true, (press) -> {
			this.writeText("%realtimeday%");
		});
		UIBase.colorizeButton(realtimeDay);
		realtimeMenu.addContent(realtimeDay);
		
		AdvancedButton realtimeHour = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.realtimehour"), true, (press) -> {
			this.writeText("%realtimehour%");
		});
		UIBase.colorizeButton(realtimeHour);
		realtimeMenu.addContent(realtimeHour);
		
		AdvancedButton realtimeMinute = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.realtimeminute"), true, (press) -> {
			this.writeText("%realtimeminute%");
		});
		UIBase.colorizeButton(realtimeMinute);
		realtimeMenu.addContent(realtimeMinute);
		
		AdvancedButton realtimeSecond = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.realtimesecond"), true, (press) -> {
			this.writeText("%realtimesecond%");
		});
		UIBase.colorizeButton(realtimeSecond);
		realtimeMenu.addContent(realtimeSecond);
		
		AdvancedButton realtimeCategoryButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.categories.realtime"), true, (press) -> {
			realtimeMenu.setParentButton((AdvancedButton) press);
			realtimeMenu.openMenuAt(0, press.y);
		});
		UIBase.colorizeButton(realtimeCategoryButton);
		variableMenu.addContent(realtimeCategoryButton);
		
		/** OTHER CATEGORY **/
		FHContextMenu otherMenu = new FHContextMenu();
		otherMenu.setAutoclose(true);
		variableMenu.addChild(otherMenu);
		
		AdvancedButton mcVersion = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.mcversion"), true, (press) -> {
			this.writeText("%mcversion%");
		});
		mcVersion.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.mcversion.desc"), "%n%"));
		UIBase.colorizeButton(mcVersion);
		otherMenu.addContent(mcVersion);
		
		AdvancedButton forgeVersion = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.forgeversion"), true, (press) -> {
			this.writeText("%version:forge%");
		});
		forgeVersion.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.forgeversion.desc"), "%n%"));
		UIBase.colorizeButton(forgeVersion);
		otherMenu.addContent(forgeVersion);
		
		AdvancedButton modVersion = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.modversion"), true, (press) -> {
			this.writeText("%version:<modid>%");
		});
		modVersion.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.modversion.desc"), "%n%"));
		UIBase.colorizeButton(modVersion);
		otherMenu.addContent(modVersion);
		
		AdvancedButton totalMods = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.totalmods"), true, (press) -> {
			this.writeText("%totalmods%");
		});
		totalMods.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.totalmods.desc"), "%n%"));
		UIBase.colorizeButton(totalMods);
		otherMenu.addContent(totalMods);
		
		AdvancedButton loadedMods = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.loadedmods"), true, (press) -> {
			this.writeText("%loadedmods%");
		});
		loadedMods.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.loadedmods.desc"), "%n%"));
		UIBase.colorizeButton(loadedMods);
		otherMenu.addContent(loadedMods);
		
		AdvancedButton fps = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.fps"), true, (press) -> {
			this.writeText("%fps%");
		});
		UIBase.colorizeButton(fps);
		otherMenu.addContent(fps);
		
		otherMenu.addSeparator();
		
		AdvancedButton cps = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.cps"), true, (press) -> {
			this.writeText("%cps%");
		});
		UIBase.colorizeButton(cps);
		otherMenu.addContent(cps);
		
		AdvancedButton bps = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.bps"), true, (press) -> {
			this.writeText("%bps%");
		});
		UIBase.colorizeButton(bps);
		otherMenu.addContent(bps);
		
		AdvancedButton tps = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.tps"), true, (press) -> {
			this.writeText("%tps%");
		});
		UIBase.colorizeButton(tps);
		otherMenu.addContent(tps);
		
		otherMenu.addSeparator();
		
		AdvancedButton percentRam = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.percentram"), true, (press) -> {
			this.writeText("%percentram%");
		});
		UIBase.colorizeButton(percentRam);
		otherMenu.addContent(percentRam);
		
		AdvancedButton usedRam = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.usedram"), true, (press) -> {
			this.writeText("%usedram%");
		});
		UIBase.colorizeButton(usedRam);
		otherMenu.addContent(usedRam);
		
		AdvancedButton maxRam = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.maxram"), true, (press) -> {
			this.writeText("%maxram%");
		});
		UIBase.colorizeButton(maxRam);
		otherMenu.addContent(maxRam);
		
		otherMenu.addSeparator();
		
		//Custom values without category
		for (DynamicValue v : DynamicValueRegistry.getInstance().getValuesAsList()) {
			if (v.valueCategory == null) {
				AdvancedButton customValue = new AdvancedButton(0, 0, 0, 0, v.valueDisplayName, true, (press) -> {
					this.writeText(v.getPlaceholder());
				});
				UIBase.colorizeButton(customValue);
				otherMenu.addContent(customValue);
			}
		}
		
		AdvancedButton otherCategoryButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.categories.other"), true, (press) -> {
			otherMenu.setParentButton((AdvancedButton) press);
			otherMenu.openMenuAt(0, press.y);
		});
		UIBase.colorizeButton(otherCategoryButton);
		variableMenu.addContent(otherCategoryButton);
		
		variableMenu.addSeparator();
		
		/** CUSTOM CATEGORIES **/
		for (String c : DynamicValueRegistry.getInstance().getCategories()) {
			
			List<DynamicValue> values = DynamicValueRegistry.getInstance().getValuesForCategory(c);
			
			if (!values.isEmpty()) {
				
				FHContextMenu customCategoryMenu = new FHContextMenu();
				customCategoryMenu.setAutoclose(true);
				variableMenu.addChild(customCategoryMenu);
				
				for (DynamicValue v : values) {
					AdvancedButton customValue = new AdvancedButton(0, 0, 0, 0, v.valueDisplayName, true, (press) -> {
						this.writeText(v.getPlaceholder());
					});
					UIBase.colorizeButton(customValue);
					customCategoryMenu.addContent(customValue);
				}
				
				AdvancedButton customCategoryButton = new AdvancedButton(0, 0, 0, 0, c, true, (press) -> {
					customCategoryMenu.setParentButton((AdvancedButton) press);
					customCategoryMenu.openMenuAt(0, press.y);
				});
				UIBase.colorizeButton(customCategoryButton);
				variableMenu.addContent(customCategoryButton);
				
			}
			
		}

		/** VARIABLE BUTTON **/
		variableButton = new AdvancedImageButton(0, 0, height, height, VARIABLES_BUTTON_RESOURCE, true, (press) -> {
			UIBase.openScaledContextMenuAtMouse(variableMenu);
		});
		variableButton.ignoreBlockedInput = true;
		variableButton.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.ui.dynamicvariabletextfield.variables.desc"), "%n%"));
		UIBase.colorizeButton(variableButton);
		
		variableMenu.setParentButton(variableButton);
		
	}
	
	@Override
	public void renderWidget(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		if (this.variableButton != null) {
			
			this.variableButton.setWidth(this.height);
			this.variableButton.setHeight(this.height);
			
			super.renderWidget(matrix, mouseX, mouseY, partialTicks);
			
			this.variableButton.setX(this.x + this.width + 5);
			this.variableButton.setY(this.y);
			this.variableButton.render(matrix, mouseX, mouseY, partialTicks);
			
			float scale = UIBase.getUIScale();
			
			MouseInput.setRenderScale(scale);
			
			matrix.push();
			
			matrix.scale(scale, scale, scale);
			
			if (this.variableMenu != null) {
				this.variableMenu.render(matrix, MouseInput.getMouseX(), MouseInput.getMouseY());
			}
			
			matrix.pop();
			
			MouseInput.resetRenderScale();
			
		}
	}

}
