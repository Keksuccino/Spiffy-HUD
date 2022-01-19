package de.keksuccino.spiffyhud.customization.dynamicvalues;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.spiffyhud.api.DynamicValueRegistry;
import de.keksuccino.spiffyhud.api.DynamicValueRegistry.DynamicValue;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.spiffyhud.api.placeholder.PlaceholderTextContainer;
import de.keksuccino.spiffyhud.api.placeholder.PlaceholderTextRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.game.minecraft.launchwrapper.FabricServerTweaker;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class DynamicValueHelper {

	private static final File MOD_DIRECTORY = new File("mods");

	private static int cachedTotalMods = -10;
	
	public static void init() {
		
		CpsHandler.init();
		
		BpsHandler.init();
		
		TpsHandler.init();
		
	}
	
	public static String convertFromRaw(String in) {
		try {

			MinecraftClient mc = MinecraftClient.getInstance();
			Entity entity = mc.getCameraEntity();

			String playername = mc.getSession().getUsername();
			String playeruuid = mc.getSession().getUuid();
			String mcversion = SharedConstants.getGameVersion().getReleaseTarget();

			//Convert &-formatcodes to real ones
			in = StringUtils.convertFormatCodes(in, "&", "§");

			//Only for internal use
			in = in.replace("%guiwidth%", "" + MinecraftClient.getInstance().getWindow().getScaledWidth());
			in = in.replace("%guiheight%", "" + MinecraftClient.getInstance().getWindow().getScaledHeight());
			//-------------

			//Replace player name and uuid placeholders
			in = in.replace("%playername%", playername);
			in = in.replace("%playeruuid%", playeruuid);

			//Replace mc version placeholder
			in = in.replace("%mcversion%", mcversion);

			//Replace mod version placeholder
			in = replaceModVersionPlaceolder(in);

			//Replace loaded mods placeholder
			in = in.replace("%loadedmods%", "" + getLoadedMods());

			//Replace total mods placeholder
			in = in.replace("%totalmods%", "" + getTotalMods());

			//Current player health
			in = in.replace("%currenthealth%", "" + getCurrentHP());

			//Max player health
			in = in.replace("%maxhealth%", "" + getMaxHP());

			//Current player food
			in = in.replace("%currentfood%", "" + getCurrentFood());

			//Current game day time hours
			in = in.replace("%daytimehours%", getDayTimeHours());

			//Current game day time minutes
			in = in.replace("%daytimeminutes%", getDayTimeMinutes());

			ClientPlayerEntity p = mc.player;

			if (p != null) {

				BlockPos pp = p.getBlockPos();

				if (pp != null) {

					in = in.replace("%posx%", "" + pp.getX());

					in = in.replace("%posy%", "" + pp.getY());

					in = in.replace("%posz%", "" + pp.getZ());

				}

				in = in.replace("%currentair%", "" + (int)(p.getAir() / 27.27));

				in = in.replace("%maxair%", "" + p.getMaxAir());

				in = in.replace("%currentscore%", "" + p.getScore());

				in = in.replace("%experience%", "" + p.totalExperience);

				in = in.replace("%level%", "" + p.experienceLevel);

				in = in.replace("%currentarmor%", "" + p.getArmor());

				ItemStack mh = p.getMainHandStack();
				ItemStack oh = p.getOffHandStack();

				if (mh != null) {

					in = in.replace("%mhitemname%", "" + mh.getName().getString());

					in = in.replace("%mhitemcurdurab%", "" + (mh.getMaxDamage() - mh.getDamage()));

					in = in.replace("%mhitemmaxdurab%", "" + mh.getMaxDamage());

				}

				if (oh != null) {

					in = in.replace("%ohitemname%", "" + oh.getName().getString());

					in = in.replace("%ohitemcurdurab%", "" + (oh.getMaxDamage() - oh.getDamage()));

					in = in.replace("%ohitemmaxdurab%", "" + oh.getMaxDamage());

				}

			}

			ServerInfo sd = mc.getCurrentServerEntry();

			//Current IP
			if ((sd != null) && (sd.address != null)) {
				in = in.replace("%serverip%", "" + sd.address);
			} else {
				in = in.replace("%serverip%", "none");
			}

			//Current MOTD
			if ((sd != null) && (sd.label != null)) {
				in = in.replace("%servermotd%", "" + sd.label.getString());
			} else {
				in = in.replace("%servermotd%", "none");
			}

			//Current Ping
			if (sd != null) {
				in = in.replace("%serverping%", "" + sd.ping);
			} else {
				in = in.replace("%serverping%", "0");
			}

			//Current Player Count
			if (sd != null) {
				if (sd.playerListSummary != null) {
					in = in.replace("%serverplayers%", "" + sd.playerListSummary.size());
				} else {
					in = in.replace("%serverplayers%", "1");
				}
			} else {
				in = in.replace("%serverplayers%", "1");
			}

			//Current Server Mods Count
			if (sd != null) {

				//TODO get mods list from server if possible
//				ExtendedServerListData fd = sd.forgeData;
//				if (fd != null) {
//					in = in.replace("%servermods%", "" + fd.numberOfMods);
//				} else {
//					in = in.replace("%servermods%", "0");
//				}

				//remove this when fixing the placeholder ---->
				in = in.replace("%servermods%", "0");

			} else {
				in = in.replace("%servermods%", "0");
			}

			if (in.contains("%realtime")) {

				Calendar c = Calendar.getInstance();

				in = in.replace("%realtimeyear%", "" + c.get(Calendar.YEAR));

				in = in.replace("%realtimemonth%", formatToFancyDateTime(c.get(Calendar.MONTH) + 1));

				in = in.replace("%realtimeday%", formatToFancyDateTime(c.get(Calendar.DAY_OF_MONTH)));

				in = in.replace("%realtimehour%", formatToFancyDateTime(c.get(Calendar.HOUR_OF_DAY)));

				in = in.replace("%realtimeminute%", formatToFancyDateTime(c.get(Calendar.MINUTE)));

				in = in.replace("%realtimesecond%", formatToFancyDateTime(c.get(Calendar.SECOND)));

			}

			if (in.contains("%biome%")) {
				BlockPos blockpos = null;
				if (entity != null) {
					blockpos = entity.getBlockPos();
				}
				if (blockpos != null) {
					in = in.replace("%biome%", "" + mc.world.getRegistryManager().get(Registry.BIOME_KEY).getId(mc.world.getBiome(blockpos)));
				}
			}

			if (entity != null) {
				in = in.replace("%direction%", "" + entity.getHorizontalFacing());
			}

			if (in.contains("%fps%")) {
				in = in.replace("%fps%", mc.fpsDebugString.split("[ ]", 2)[0]);
			}

			if (in.contains("ram%")) {
				long i = Runtime.getRuntime().maxMemory();
				long j = Runtime.getRuntime().totalMemory();
				long k = Runtime.getRuntime().freeMemory();
				long l = j - k;

				in = in.replace("%percentram%", (l * 100L / i) + "%");

				in = in.replace("%usedram%", "" + bytesToMb(l));

				in = in.replace("%maxram%", "" + bytesToMb(i));
			}

			if (in.contains("%targetblock%")) {
				if (entity != null) {
					HitResult lookingAt = entity.raycast(20.0D, 0.0F, false);
					if ((lookingAt != null) && (lookingAt.getType() == HitResult.Type.BLOCK)) {
						BlockPos blockpos2 = ((BlockHitResult)lookingAt).getBlockPos();
						BlockState blockstate = mc.world.getBlockState(blockpos2);

						in = in.replace("%targetblock%", String.valueOf(Registry.BLOCK.getId(blockstate.getBlock())));
					} else {
						in = in.replace("%targetblock%", "none");
					}
				}
			}

			in = in.replace("%cps%", "" + CpsHandler.getCps());

			in = in.replace("%bps%", "" + BpsHandler.getBps());

			in = in.replace("%tps%", "" + TpsHandler.getTps());

			//Deprecated old custom placeholder handling (old API)
			for (DynamicValue v : DynamicValueRegistry.getInstance().getValuesAsList()) {
				in = in.replace(v.getPlaceholder(), v.get());
			}

			//Handle all custom placeholders added via the API (new API)
			for (PlaceholderTextContainer pc : PlaceholderTextRegistry.getPlaceholders()) {
				in = pc.replacePlaceholders(in);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return in;
	}
	
	public static boolean containsDynamicValues(String in) {
		String s = convertFromRaw(in);
		return !s.equals(in);
	}
	
	private static String replaceModVersionPlaceolder(String in) {
		try {
			if (in.contains("%version:")) {
				List<String> l = new ArrayList<String>();
				int index = -1;
				int i = 0;
				while (i < in.length()) {
					String s = "" + in.charAt(i);
					if (s.equals("%")) {
						if (index == -1) {
							index = i;
						} else {
							String sub = in.substring(index, i+1);
							if (sub.startsWith("%version:") && sub.endsWith("%")) {
								l.add(sub);
							}
							index = -1;
						}
					}
					i++;
				}
				for (String s : l) {
					if (s.contains(":")) {
						String blank = s.substring(1, s.length()-1);
						String mod = blank.split(":", 2)[1];
						if (FabricLoader.getInstance().isModLoaded(mod)) {
							Optional<ModContainer> o = FabricLoader.getInstance().getModContainer(mod);
							if (o.isPresent()) {
								ModContainer c = o.get();
								String version = c.getMetadata().getVersion().getFriendlyString();
								in = in.replace(s, version);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

	private static int getTotalMods() {
		if (cachedTotalMods == -10) {
			if (MOD_DIRECTORY.exists()) {
				int i = 0;
				for (File f : MOD_DIRECTORY.listFiles()) {
					if (f.isFile() && f.getName().toLowerCase().endsWith(".jar")) {
						i++;
					}
				}
				cachedTotalMods = i+2;
			} else {
				cachedTotalMods = -1;
			}
		}
		return cachedTotalMods;
	}

	private static int getLoadedMods() {
		try {
			int i = 0;
			if (Konkrete.isOptifineLoaded) {
				i++;
			}
			return FabricLoader.getInstance().getAllMods().size() + i;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private static int getCurrentHP() {
		ClientPlayerEntity p = MinecraftClient.getInstance().player;
		if (p != null) {
			float raw = p.getHealth();
			String rawString = raw + "";
			String preDecimalString = rawString.split("[.]", 2)[0];
			String postDecimalString = rawString.split("[.]", 2)[1];
			if (postDecimalString.length() > 3) {
				postDecimalString = postDecimalString.substring(0, 3);
			}
			if (MathUtils.isInteger(preDecimalString) && MathUtils.isInteger(postDecimalString)) {
				int pre = Integer.parseInt(preDecimalString);
				int post = Integer.parseInt(postDecimalString);
				if (post > 0) {
					pre++;
				}
				return pre;
			}
		}
		return -1;
	}
	
	private static int getMaxHP() {
		ClientPlayerEntity p = MinecraftClient.getInstance().player;
		if (p != null) {
			return (int) p.getMaxHealth();
		}
		return -1;
	}
	
	private static int getCurrentFood() {
		ClientPlayerEntity p = MinecraftClient.getInstance().player;
		if (p != null) {
			return p.getHungerManager().getFoodLevel();
		}
		return -1;
	}
	
	private static long getDayTime() {
		ClientWorld w = MinecraftClient.getInstance().world;
		if (w != null) {
			return w.getTimeOfDay();
		}
		return 1L;
	}
	
	private static String getDayTimeHours() {
		String hString = "00";
		long dt = getDayTime();
		while (dt >= 24000) {
			dt -= 24000;
		}
		long h = 0;
		if (dt < 18000) {
			h = (dt / 1000) + 6;
		} else {
			h = (dt / 1000) - 18;
		}
		hString = "" + h;
		if (hString.length() < 2) {
			hString = "0" + hString;
		}
		return hString;
	}
	
	private static String getDayTimeMinutes() {
		String minString = "00";
		long i = getDayTime() / 1000;
		long i2 = getDayTime() - (i * 1000);
		if (i2 <= 0) {
			return minString;
		}
		long min = (long)((float)i2 / 16.6F);
		if (min > 59) {
			min = 0;
		}
		minString = "" + min;
		if (minString.length() < 2) {
			minString = "0" + minString;
		}
		return minString;
	}
	
	private static String formatToFancyDateTime(int in) {
		String s = "" + in;
		if (s.length() < 2) {
			s = "0" + s;
		}
		return s;
	}
	
	private static long bytesToMb(long bytes) {
		return bytes / 1024L / 1024L;
	}

}
