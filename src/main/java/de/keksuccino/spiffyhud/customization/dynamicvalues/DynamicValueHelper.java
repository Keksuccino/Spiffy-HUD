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
import de.keksuccino.spiffyhud.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.ExtendedServerListData;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.versions.mcp.MCPVersion;

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

			Minecraft mc = Minecraft.getInstance();
			Entity entity = mc.getCameraEntity();

			String playername = mc.getUser().getName();
			String playeruuid = mc.getUser().getUuid();
			String mcversion = MCPVersion.getMCVersion();

			//Convert &-formatcodes to real ones
			in = StringUtils.convertFormatCodes(in, "&", "§");

			//Only for internal use
			in = in.replace("%guiwidth%", "" + Minecraft.getInstance().getWindow().getGuiScaledWidth());
			in = in.replace("%guiheight%", "" + Minecraft.getInstance().getWindow().getGuiScaledHeight());
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

			LocalPlayer p = mc.player;

			if (p != null) {

				BlockPos pp = p.blockPosition();

				if (pp != null) {

					in = in.replace("%posx%", "" + pp.getX());

					in = in.replace("%posy%", "" + pp.getY());

					in = in.replace("%posz%", "" + pp.getZ());

				}

				in = in.replace("%currentair%", "" + (int)(p.getAirSupply() / 27.27));

				in = in.replace("%maxair%", "" + p.getMaxAirSupply());

				in = in.replace("%currentscore%", "" + p.getScore());

				in = in.replace("%experience%", "" + p.totalExperience);

				in = in.replace("%level%", "" + p.experienceLevel);

				in = in.replace("%currentarmor%", "" + p.getArmorValue());

				ItemStack mh = p.getMainHandItem();
				ItemStack oh = p.getOffhandItem();

				if (mh != null) {

					in = in.replace("%mhitemname%", "" + mh.getHoverName().getString());

					in = in.replace("%mhitemcurdurab%", "" + (mh.getMaxDamage() - mh.getDamageValue()));

					in = in.replace("%mhitemmaxdurab%", "" + mh.getMaxDamage());

				}

				if (oh != null) {

					in = in.replace("%ohitemname%", "" + oh.getHoverName().getString());

					in = in.replace("%ohitemcurdurab%", "" + (oh.getMaxDamage() - oh.getDamageValue()));

					in = in.replace("%ohitemmaxdurab%", "" + oh.getMaxDamage());

				}

			}

			ServerData sd = mc.getCurrentServer();

			//Current IP
			if ((sd != null) && (sd.ip != null)) {
				in = in.replace("%serverip%", "" + sd.ip);
			} else {
				in = in.replace("%serverip%", "none");
			}

			//Current MOTD
			if ((sd != null) && (sd.motd != null)) {
				in = in.replace("%servermotd%", "" + sd.motd.getString());
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
				if (sd.playerList != null) {
					in = in.replace("%serverplayers%", "" + sd.playerList.size());
				} else {
					in = in.replace("%serverplayers%", "1");
				}
			} else {
				in = in.replace("%serverplayers%", "1");
			}

			//Current Server Mods Count
			if (sd != null) {
				ExtendedServerListData fd = sd.forgeData;
				if (fd != null) {
					in = in.replace("%servermods%", "" + fd.numberOfMods);
				} else {
					in = in.replace("%servermods%", "0");
				}
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
					blockpos = entity.blockPosition();
				}
				if (blockpos != null) {
					in = in.replace("%biome%", WorldUtils.getCurrentBiomeName());
				}
			}

			if (entity != null) {
				in = in.replace("%direction%", "" + entity.getDirection());
			}

			if (in.contains("%fps%")) {
				in = in.replace("%fps%", mc.fpsString.split("[ ]", 2)[0]);
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
					HitResult lookingAt = entity.pick(20.0D, 0.0F, false);
					if ((lookingAt != null) && (lookingAt.getType() == HitResult.Type.BLOCK)) {
						BlockPos blockpos2 = ((BlockHitResult)lookingAt).getBlockPos();
						BlockState blockstate = mc.level.getBlockState(blockpos2);

						in = in.replace("%targetblock%", String.valueOf(Registry.BLOCK.getKey(blockstate.getBlock())));
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
						if (ModList.get().isLoaded(mod)) {
							Optional<? extends ModContainer> o = ModList.get().getModContainerById(mod);
							if (o.isPresent()) {
								ModContainer c = o.get();
								String version = c.getModInfo().getVersion().toString();
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
			return ModList.get().getMods().size() + i;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private static int getCurrentHP() {
		LocalPlayer p = Minecraft.getInstance().player;
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
		LocalPlayer p = Minecraft.getInstance().player;
		if (p != null) {
			return (int) p.getMaxHealth();
		}
		return -1;
	}
	
	private static int getCurrentFood() {
		LocalPlayer p = Minecraft.getInstance().player;
		if (p != null) {
			return p.getFoodData().getFoodLevel();
		}
		return -1;
	}
	
	private static long getDayTime() {
		ClientLevel w = Minecraft.getInstance().level;
		if (w != null) {
			return w.getDayTime();
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
