package de.keksuccino.fancyhud.customization.rendering.ingamehud;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.fancyhud.FancyHud;
import de.keksuccino.fancyhud.api.hud.CustomVanillaCustomizationItem;
import de.keksuccino.fancyhud.api.hud.HudElementContainer;
import de.keksuccino.fancyhud.api.hud.HudElementRegistry;
import de.keksuccino.fancyhud.api.item.CustomizationItem;
import de.keksuccino.fancyhud.api.item.CustomizationItemContainer;
import de.keksuccino.fancyhud.api.item.CustomizationItemRegistry;
import de.keksuccino.fancyhud.customization.CustomizationPropertiesHandler;
import de.keksuccino.fancyhud.customization.items.CustomizationItemBase;
import de.keksuccino.fancyhud.customization.items.ShapeCustomizationItem;
import de.keksuccino.fancyhud.customization.items.SlideshowCustomizationItem;
import de.keksuccino.fancyhud.customization.items.SplashTextCustomizationItem;
import de.keksuccino.fancyhud.customization.items.StringCustomizationItem;
import de.keksuccino.fancyhud.customization.items.TextureCustomizationItem;
import de.keksuccino.fancyhud.customization.items.WebStringCustomizationItem;
import de.keksuccino.fancyhud.customization.items.WebTextureCustomizationItem;
import de.keksuccino.fancyhud.customization.items.vanilla.AirBarCustomizationItem;
import de.keksuccino.fancyhud.customization.items.vanilla.ArmorBarCustomizationItem;
import de.keksuccino.fancyhud.customization.items.vanilla.BossBarCustomizationItem;
import de.keksuccino.fancyhud.customization.items.vanilla.CrosshairCustomizationItem;
import de.keksuccino.fancyhud.customization.items.vanilla.ExperienceJumpBarCustomizationItem;
import de.keksuccino.fancyhud.customization.items.vanilla.FoodMountHealthCustomizationItem;
import de.keksuccino.fancyhud.customization.items.vanilla.HotbarCustomizationItem;
import de.keksuccino.fancyhud.customization.items.vanilla.OverlayMessageCustomizationItem;
import de.keksuccino.fancyhud.customization.items.vanilla.PlayerHealthBarCustomizationItem;
import de.keksuccino.fancyhud.customization.items.vanilla.SelectedItemNameCustomizationItem;
import de.keksuccino.fancyhud.customization.items.vanilla.TitleCustomizationItem;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.AirBarHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.ArmorBarHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.BossBarHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.CrosshairHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.ExperienceJumpBarHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.HotbarHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.OverlayMessageHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.FoodMountHealthHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.PlayerHealthHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.SelectedItemNameHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.TitleHudElement;
import de.keksuccino.fancyhud.events.CustomizationSystemReloadedEvent;
import de.keksuccino.fancyhud.events.WindowResizedEvent;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.properties.PropertiesSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.overlay.BossOverlayGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.PotionSpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CustomizableIngameGui extends ForgeIngameGui {
	
	private boolean isEditor;
	
	public CrosshairHudElement crosshairElement = new CrosshairHudElement(this);
	public BossBarHudElement bossBarElement = new BossBarHudElement(this);
	public HotbarHudElement hotbarElement = new HotbarHudElement(this);
	public PlayerHealthHudElement healthElement = new PlayerHealthHudElement(this);
	public FoodMountHealthHudElement foodMountHealthElement = new FoodMountHealthHudElement(this);
	public ExperienceJumpBarHudElement experienceJumpBarElement = new ExperienceJumpBarHudElement(this);
	//TODO implementieren
//	public PotionIconsHudElement potionIconsElement = new PotionIconsHudElement(this);
	public ArmorBarHudElement armorBarElement = new ArmorBarHudElement(this);
	public AirBarHudElement airBarElement = new AirBarHudElement(this);
	public TitleHudElement titleElement = new TitleHudElement(this, false);
	public TitleHudElement subtitleElement = new TitleHudElement(this, true);
	public SelectedItemNameHudElement selectedItemNameElement = new SelectedItemNameHudElement(this);
	public OverlayMessageHudElement overlayMessageElement = new OverlayMessageHudElement(this);
	
	public Map<String, HudElementContainer> customElements;
	
	protected CustomizableBossOverlayGui bossGui;
	
	protected List<CustomizationItemBase> backgroundElements = new ArrayList<CustomizationItemBase>();
	protected List<CustomizationItemBase> foregroundElements = new ArrayList<CustomizationItemBase>();
	
	protected boolean prevTickIsSingleplayer = true;
	protected boolean prevTickWorldNull = true;
	
	public boolean showVignette = true;
	
	public CustomizableIngameGui(Minecraft mc, boolean isEditor) {
		super(mc);
		
		this.isEditor = isEditor;
		
		this.bossGui = new CustomizableBossOverlayGui(mc, this);
		
		this.customElements = HudElementRegistry.getInstance().getElements();
		
		if (!this.isEditor()) {
			MinecraftForge.EVENT_BUS.register(this);
			this.reloadHud(Minecraft.getInstance().getMainWindow().getScaledWidth(), Minecraft.getInstance().getMainWindow().getScaledHeight());
		}
	}
	
	@SubscribeEvent
	public void onWindowResized(WindowResizedEvent e) {
		this.reloadHud(e.getScaledWidth(), e.getScaledHeight());
	}
	
	@SubscribeEvent
	public void onSystemReloaded(CustomizationSystemReloadedEvent e) {
		this.reloadHud(Minecraft.getInstance().getMainWindow().getScaledWidth(), Minecraft.getInstance().getMainWindow().getScaledHeight());
	}
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) {
		if ((Minecraft.getInstance() != null)) {
			
			if ((Minecraft.getInstance().world == null) != this.prevTickWorldNull) {
				this.reloadHud(Minecraft.getInstance().getMainWindow().getScaledWidth(), Minecraft.getInstance().getMainWindow().getScaledHeight());
			}
			this.prevTickWorldNull = (Minecraft.getInstance().world == null);
			
			if (Minecraft.getInstance().isSingleplayer() != this.prevTickIsSingleplayer) {
				this.reloadHud(Minecraft.getInstance().getMainWindow().getScaledWidth(), Minecraft.getInstance().getMainWindow().getScaledHeight());
			}
			this.prevTickIsSingleplayer = Minecraft.getInstance().isSingleplayer();
			
		}
	}
	
	public void reloadHud(int scaledWidth, int scaledHeight) {

		try {
			
			if (this.isEditor()) {
				return;
			}
			
			this.foregroundElements.clear();
			this.backgroundElements.clear();
			
			this.customElements = HudElementRegistry.getInstance().getElements();
			
			for (HudElementContainer c : this.customElements.values()) {
				c.onResetElement();
				c.element.setHandler(this);
			}
			
			List<PropertiesSet> props = CustomizationPropertiesHandler.getProperties();
			
			boolean crossSet = false;
			boolean bossSet = false;
			boolean hotbarSet = false;
			boolean playerHealthSet = false;
			boolean foodSet = false;
			boolean experienceSet = false;
			boolean titleSet = false;
			boolean subtitleSet = false;
			boolean armorSet = false;
			boolean airSet = false;
			boolean selectedItemNameSet = false;
			boolean overlayMessageSet = false;
			
			List<String> customElementsSet = new ArrayList<String>();
			
			for (PropertiesSet s : props) {
				
				boolean renderInBackground = false;
				
				List<PropertiesSection> metas = s.getPropertiesOfType("customization-meta");
				
				if (metas.isEmpty()) {
					continue;
				}
				
				String roString = metas.get(0).getEntryValue("renderorder");
				if ((roString != null) && roString.equalsIgnoreCase("background")) {
					renderInBackground = true;
				}
				
				String vignetteString = metas.get(0).getEntryValue("showvignette");
				if ((vignetteString != null) && vignetteString.equalsIgnoreCase("false")) {
					this.showVignette = false;
				}
				
				String showIn = metas.get(0).getEntryValue("showin");
				if (showIn != null) {
					if (showIn.equalsIgnoreCase("singleplayer")) {
						if (!Minecraft.getInstance().isSingleplayer()) {
							continue;
						}
					}
					if (showIn.equalsIgnoreCase("multiplayer")) {
						if (Minecraft.getInstance().isSingleplayer()) {
							continue;
						}
					}
				}
				
				//TODO reimplement biggerthan, smallerthan, etc ----> AT THIS LINE <----
				
				for (PropertiesSection sec : s.getPropertiesOfType("customization")) {
					String action = sec.getEntryValue("action");
					
					if (action != null) {
						
						/** ################## VANILLA ELEMENTS / CUSTOMIZATIONS ################## **/
						
						/** CROSSHAIR **/
						if (action.equalsIgnoreCase("editcrosshair")) {
							this.backgroundElements.add(new CrosshairCustomizationItem(this.crosshairElement, sec, crossSet));
							crossSet = true;
						}
						
						/** BOSS HEALTH **/
						if (action.equalsIgnoreCase("editbosshealth")) {
							this.backgroundElements.add(new BossBarCustomizationItem(this.bossBarElement, sec, bossSet));
							bossSet = true;
						}
						
						/** HOTBAR **/
						if (action.equalsIgnoreCase("edithotbar")) {
							this.backgroundElements.add(new HotbarCustomizationItem(this.hotbarElement, sec, hotbarSet));
							hotbarSet = true;
						}
						
						/** PLAYER HEALTH BAR **/
						if (action.equalsIgnoreCase("editplayerhealthbar")) {
							this.backgroundElements.add(new PlayerHealthBarCustomizationItem(this.healthElement, sec, playerHealthSet));
							playerHealthSet = true;
						}
						
						/** PLAYER FOOD BAR | MOUNT HEALTH BAR **/
						if (action.equalsIgnoreCase("editplayerfoodbar")) {
							this.backgroundElements.add(new FoodMountHealthCustomizationItem(this.foodMountHealthElement, sec, foodSet));
							foodSet = true;
						}
						
						/** EXPERIENCE BAR | JUMP BAR **/
						if (action.equalsIgnoreCase("editexperiencebar")) {
							this.backgroundElements.add(new ExperienceJumpBarCustomizationItem(this.experienceJumpBarElement, sec, experienceSet));
							experienceSet = true;
						}
						
						/** TITLE **/
						if (action.equalsIgnoreCase("edittitle")) {
							this.backgroundElements.add(new TitleCustomizationItem(this.titleElement, sec, titleSet));
							titleSet = true;
						}
						
						/** SUBTITLE **/
						if (action.equalsIgnoreCase("editsubtitle")) {
							this.backgroundElements.add(new TitleCustomizationItem(this.subtitleElement, sec, subtitleSet));
							subtitleSet = true;
						}

						/** ARMOR BAR **/
						if (action.equalsIgnoreCase("editarmorbar")) {
							this.backgroundElements.add(new ArmorBarCustomizationItem(this.armorBarElement, sec, armorSet));
							armorSet = true;
						}

						/** AIR BAR **/
						if (action.equalsIgnoreCase("editairbar")) {
							this.backgroundElements.add(new AirBarCustomizationItem(this.airBarElement, sec, airSet));
							airSet = true;
						}
						
						/** SELECTED ITEM NAME **/
						if (action.equalsIgnoreCase("editselecteditemname")) {
							this.backgroundElements.add(new SelectedItemNameCustomizationItem(this.selectedItemNameElement, sec, selectedItemNameSet));
							selectedItemNameSet = true;
						}
						
						/** OVERLAY MESSAGE **/
						if (action.equalsIgnoreCase("editoverlaymessage")) {
							this.backgroundElements.add(new OverlayMessageCustomizationItem(this.overlayMessageElement, sec, overlayMessageSet));
							overlayMessageSet = true;
						}
						
						/** ################## CUSTOM VANILLA ELEMENTS ################## **/
						
						if (action.startsWith("edit_")) {
							
							String id = action.split("[_]", 2)[1];
							HudElementContainer c = this.customElements.get(id);
							boolean isSecond = false;
							if (customElementsSet.contains(id)) {
								isSecond = true;
							}
							
							if (c != null) {
								
								this.backgroundElements.add(new CustomVanillaCustomizationItem(c, sec, isSecond));
								if (!customElementsSet.contains(id)) {
									customElementsSet.add(id);
								}
								
							}
							
						}
						
						/** ################## ITEMS ################## **/
						
						/** TEXT ELEMENT **/
						if (action.equalsIgnoreCase("addtext")) {
							if (renderInBackground) {
								backgroundElements.add(new StringCustomizationItem(sec));
							} else {
								foregroundElements.add(new StringCustomizationItem(sec));
							}
						}

						/** WEB TEXT ELEMENT **/
						if (action.equalsIgnoreCase("addwebtext")) {
							if (renderInBackground) {
								backgroundElements.add(new WebStringCustomizationItem(sec));
							} else {
								foregroundElements.add(new WebStringCustomizationItem(sec));
							}
						}
						
						/** TEXTURE ELEMENT **/
						if (action.equalsIgnoreCase("addtexture")) {
							if (renderInBackground) {
								backgroundElements.add(new TextureCustomizationItem(sec));
							} else {
								foregroundElements.add(new TextureCustomizationItem(sec));
							}
						}

						/** WEB TEXTURE ELEMENT **/
						if (action.equalsIgnoreCase("addwebtexture")) {
							if (renderInBackground) {
								backgroundElements.add(new WebTextureCustomizationItem(sec));
							} else {
								foregroundElements.add(new WebTextureCustomizationItem(sec));
							}
						}

						/** SHAPE ELEMENT **/
						if (action.equalsIgnoreCase("addshape")) {
							if (renderInBackground) {
								backgroundElements.add(new ShapeCustomizationItem(sec));
							} else {
								foregroundElements.add(new ShapeCustomizationItem(sec));
							}
						}
						
						/** SLIDESHOW ELEMENT **/
						if (action.equalsIgnoreCase("addslideshow")) {
							if (renderInBackground) {
								backgroundElements.add(new SlideshowCustomizationItem(sec));
							} else {
								foregroundElements.add(new SlideshowCustomizationItem(sec));
							}
						}

//						if (action.equalsIgnoreCase("addentity")) {
//							if (renderInBackground) {
//								backgroundElements.add(new PlayerEntityCustomizationItem(sec));
//							} else {
//								foregroundElements.add(new PlayerEntityCustomizationItem(sec));
//							}
//						}
						
						/** SPLASH TEXT ELEMENT **/
						if (action.equalsIgnoreCase("addsplash")) {
							String file = sec.getEntryValue("splashfilepath");
							String text = sec.getEntryValue("text");
							if ((file != null) || (text != null)) {
								
								SplashTextCustomizationItem i = new SplashTextCustomizationItem(sec);
								
								if (renderInBackground) {
									backgroundElements.add(i);
								} else {
									foregroundElements.add(i);
								}
								
							}
						}
						
						/** ################## CUSTOM ITEMS ################## **/
						
						if (action.startsWith("add_")) {
							String id = action.split("[_]", 2)[1];
							CustomizationItemContainer c = CustomizationItemRegistry.getInstance().getElement(id);
							if (c != null) {
								
								CustomizationItem i = c.constructWithProperties(sec);
								if (renderInBackground) {
									backgroundElements.add(i);
								} else {
									foregroundElements.add(i);
								}
								
							}
						}
						
					}
					
				}
				
			}
			
			//Add dummy customization items to handle positions of all non-customized elements
			PropertiesSection dummySec = new PropertiesSection("customization");
			if (!crossSet) {
				this.backgroundElements.add(new CrosshairCustomizationItem(this.crosshairElement, dummySec, false));
			}
//			if (!potionsSet) {
//				this.backgroundElements.add(new PotionIconsCustomizationItem(this.potionIconsElement, dummySec));
//			}
			if (!bossSet) {
				this.backgroundElements.add(new BossBarCustomizationItem(this.bossBarElement, dummySec, false));
			}
			if (!hotbarSet) {
				this.backgroundElements.add(new HotbarCustomizationItem(this.hotbarElement, dummySec, false));
			}
			if (!playerHealthSet) {
				this.backgroundElements.add(new PlayerHealthBarCustomizationItem(this.healthElement, dummySec, false));
			}
			if (!foodSet) {
				this.backgroundElements.add(new FoodMountHealthCustomizationItem(this.foodMountHealthElement, dummySec, false));
			}
			if (!experienceSet) {
				this.backgroundElements.add(new ExperienceJumpBarCustomizationItem(this.experienceJumpBarElement, dummySec, false));
			}
			if (!titleSet) {
				this.backgroundElements.add(new TitleCustomizationItem(this.titleElement, dummySec, false));
			}
			if (!subtitleSet) {
				this.backgroundElements.add(new TitleCustomizationItem(this.subtitleElement, dummySec, false));
			}
			if (!armorSet) {
				this.backgroundElements.add(new ArmorBarCustomizationItem(this.armorBarElement, dummySec, false));
			}
			if (!airSet) {
				this.backgroundElements.add(new AirBarCustomizationItem(this.airBarElement, dummySec, false));
			}
			if (!selectedItemNameSet) {
				this.backgroundElements.add(new SelectedItemNameCustomizationItem(this.selectedItemNameElement, dummySec, false));
			}
			if (!overlayMessageSet) {
				this.backgroundElements.add(new OverlayMessageCustomizationItem(this.overlayMessageElement, dummySec, false));
			}
			
			for (Map.Entry<String, HudElementContainer> m : this.customElements.entrySet()) {
				HudElementContainer c = m.getValue();
				if (!customElementsSet.contains(c.elementIdentifier)) {
					this.backgroundElements.add(new CustomVanillaCustomizationItem(c, dummySec, false));
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Override
	public void renderIngameGui(MatrixStack matrix, float partialTicks) {
				
		try {
			
			if (!this.isEditor()) {
				for (CustomizationItemBase c : this.backgroundElements) {
					c.render(matrix);
				}
			}
			
			super.renderIngameGui(matrix, partialTicks);
			
			//Render custom vanilla elements
			if (!this.isEditor()) {
				for (Map.Entry<String, HudElementContainer> m : this.customElements.entrySet()) {
					m.getValue().element.render(matrix, scaledWidth, scaledHeight, partialTicks);
				}
			}
			
			if (!this.isEditor()) {
				for (CustomizationItemBase c : this.foregroundElements) {
					c.render(matrix);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	protected void renderVignette(Entity entity) {
		if (this.showVignette) {
			super.renderVignette(entity);
		}
	}
	
	//Render Crosshair
	@Override
	protected void func_238456_d_(MatrixStack matrix) {
		
		this.crosshairElement.render(matrix, scaledWidth, scaledHeight, Minecraft.getInstance().getRenderPartialTicks());
		
	}

	//TODO editierbar machen
//	@Override
//    protected void renderPotionIcons(MatrixStack matrix) {
//
//		if (this.potionIconsElement.fireEvents) {
//			if (pre(ElementType.POTION_ICONS, matrix, false)) return;
//		}
//
//		if (this.potionIconsElement.visible) {
//
//			this.renderPotionIconsRaw(matrix);
//
//		}
//
//		if (this.potionIconsElement.fireEvents) {
//			post(ElementType.POTION_ICONS, matrix, false);
//		}
//
////		this.potionIconsElement.render(matrix, scaledWidth, scaledHeight, Minecraft.getInstance().getRenderPartialTicks());
//        
//    }

	protected void renderPotionIconsRaw(MatrixStack matrix) {
		
		Collection<EffectInstance> collection = this.mc.player.getActivePotionEffects();
		if (!collection.isEmpty()) {
			RenderSystem.enableBlend();
			int i = 0;
			int j = 0;
			PotionSpriteUploader potionspriteuploader = this.mc.getPotionSpriteUploader();
			List<Runnable> list = Lists.newArrayListWithExpectedSize(collection.size());
			this.mc.getTextureManager().bindTexture(ContainerScreen.INVENTORY_BACKGROUND);

			for(EffectInstance effectinstance : Ordering.natural().reverse().sortedCopy(collection)) {
				Effect effect = effectinstance.getPotion();
				if (!effectinstance.shouldRenderHUD()) continue;
				this.mc.getTextureManager().bindTexture(ContainerScreen.INVENTORY_BACKGROUND);
				if (effectinstance.isShowIcon()) {
					int k = this.scaledWidth;
					int l = 1;
					if (this.mc.isDemo()) {
						l += 15;
					}

					if (effect.isBeneficial()) {
						++i;
						k = k - 25 * i;
					} else {
						++j;
						k = k - 25 * j;
						l += 26;
					}

					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					float f = 1.0F;
					if (effectinstance.isAmbient()) {
						this.blit(matrix, k, l, 165, 166, 24, 24);
					} else {
						this.blit(matrix, k, l, 141, 166, 24, 24);
						if (effectinstance.getDuration() <= 200) {
							int i1 = 10 - effectinstance.getDuration() / 20;
							f = MathHelper.clamp((float)effectinstance.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((float)effectinstance.getDuration() * (float)Math.PI / 5.0F) * MathHelper.clamp((float)i1 / 10.0F * 0.25F, 0.0F, 0.25F);
						}
					}

					TextureAtlasSprite textureatlassprite = potionspriteuploader.getSprite(effect);
					int j1 = k;
					int k1 = l;
					float f1 = f;
					list.add(() -> {
						this.mc.getTextureManager().bindTexture(textureatlassprite.getAtlasTexture().getTextureLocation());
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);
						blit(matrix, j1 + 3, k1 + 3, this.getBlitOffset(), 18, 18, textureatlassprite);
					});
					effectinstance.renderHUDEffect(this, matrix, k, l, this.getBlitOffset(), f);
				}
			}

			list.forEach(Runnable::run);
		}
		
	}

	@Override
	protected void renderBossHealth(MatrixStack matrix) {
		
		this.bossBarElement.render(matrix, scaledWidth, scaledHeight, Minecraft.getInstance().getRenderPartialTicks());
        
    }
	
	@Override
	protected void renderHotbar(float partialTicks, MatrixStack matrix) {

		this.hotbarElement.render(matrix, scaledWidth, scaledHeight, partialTicks);
		
	}
	
	@Override
	public void renderHealth(int width, int height, MatrixStack matrix) {
		
		this.healthElement.render(matrix, width, height, Minecraft.getInstance().getRenderPartialTicks());
		
		this.armorBarElement.render(matrix, width, height, Minecraft.getInstance().getRenderPartialTicks());

		this.foodMountHealthElement.render(matrix, width, height, Minecraft.getInstance().getRenderPartialTicks());

		this.airBarElement.render(matrix, width, height, Minecraft.getInstance().getRenderPartialTicks());
		
		this.experienceJumpBarElement.render(matrix, scaledWidth, scaledHeight, Minecraft.getInstance().getRenderPartialTicks());

	}
	
	//renderSelectedItemName
	@Override
	public void func_238453_b_(MatrixStack matrix) {
		
		this.selectedItemNameElement.render(matrix, scaledWidth, scaledHeight, Minecraft.getInstance().getRenderPartialTicks());
		
	}
	
	@Override
	protected void renderRecordOverlay(int width, int height, float partialTicks, MatrixStack matrix) {
		
		this.overlayMessageElement.render(matrix, width, height, partialTicks);
		
	}
	
	@Override
	public void renderFood(int width, int height, MatrixStack matrix) {
		//empty bc food + mount health are rendered in renderHealth
    }
	
	@Override
	protected void renderHealthMount(int width, int height, MatrixStack matrix) {
		//empty bc food + mount health are rendered in renderHealth
    }
	
	@Override
	public void renderHorseJumpBar(MatrixStack matrix, int posX) {
		//empty bc exp + jump bar are rendered in renderHealth
	}
	
	@Override
	protected void renderExperience(int posX, MatrixStack matrix) {
		//empty bc exp + jump bar are rendered in renderHealth
	}
	
	@Override
	protected void renderArmor(MatrixStack matrix, int width, int height) {
		//empty bc armor bar is rendered in renderHealth
	}
	
	@Override
	protected void renderAir(int width, int height, MatrixStack matrix) {
		//empty bc air bar is rendered in renderHealth
	}
	
	@Override
	protected void renderTitle(int width, int height, float partialTicks, MatrixStack matrix) {
		
		this.titleElement.render(matrix, width, height, partialTicks);
		
		this.subtitleElement.render(matrix, width, height, partialTicks);
        
    }
	
	@Override
	public BossOverlayGui getBossOverlay() {
		return this.bossGui;
	}

	public boolean pre(ElementType type, MatrixStack matrix, boolean isInsideCustomization) {
		if (isInsideCustomization && !FancyHud.config.getOrDefault("customizeforgehooks", true)) {
    		return false;
    	}
    	if (!isInsideCustomization && FancyHud.config.getOrDefault("customizeforgehooks", true)) {
    		return false;
    	}
		try {
			Method m = ForgeIngameGui.class.getDeclaredMethod("pre", ElementType.class, MatrixStack.class);
			m.setAccessible(true);
			return (boolean) m.invoke(this, type, matrix);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }
	
    public void post(ElementType type, MatrixStack matrix, boolean isInsideCustomization) {
    	if (isInsideCustomization && !FancyHud.config.getOrDefault("customizeforgehooks", true)) {
    		return;
    	}
    	if (!isInsideCustomization && FancyHud.config.getOrDefault("customizeforgehooks", true)) {
    		return;
    	}
    	try {
			Method m = ForgeIngameGui.class.getDeclaredMethod("post", ElementType.class, MatrixStack.class);
			m.setAccessible(true);
			m.invoke(this, type, matrix);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void bind(ResourceLocation res) {
        mc.getTextureManager().bindTexture(res);
    }
    
    public boolean isEditor() {
    	return this.isEditor;
    }
    
    public ITextComponent getCurrentTitle() {
		return this.displayedTitle;
	}
	
	public ITextComponent getCurrentSubTitle() {
		return this.displayedSubTitle;
	}
	
	public int getTitlesTimer() {
		return this.titlesTimer;
	}
	
	public int getTitleFadeOut() {
		return this.titleFadeOut;
	}
	
	public int getTitleFadeIn() {
		return this.titleFadeIn;
	}
	
	public int getTitleDisplayTime() {
		return this.titleDisplayTime;
	}
	
	public int getRemainingHighlightingTicks() {
		return this.remainingHighlightTicks;
	}
	
	public ItemStack getHighlightingItemStack() {
		return this.highlightingItemStack;
	}
	
	public int getOverlayMessageTime() {
		return this.overlayMessageTime;
	}
	
	public ITextComponent getOverlayMessage() {
		return this.overlayMessage;
	}
	
	public boolean getAnimateOverlayMessageColor() {
		return this.animateOverlayMessageColor;
	}
	
	@Override
	public void func_238448_a_(MatrixStack matrix, FontRenderer p_238448_2_, int p_238448_3_, int p_238448_4_, int p_238448_5_) {
		super.func_238448_a_(matrix, p_238448_2_, p_238448_3_, p_238448_4_, p_238448_5_);
	}

}
