package de.keksuccino.spiffyhud.utils;

import java.util.stream.Stream;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import com.google.common.collect.Streams;

public class TextComponentUtils {

	public static String getFormattedStringFromComponent(Text component) {
		StringBuilder stringbuilder = new StringBuilder();
		String s = "";

		String s1 = component.getString();
		if (!s1.isEmpty()) {
			String s2 = getFormattingCodesFromStyle(component.getStyle());
			if (!s2.equals(s)) {
				if (!s.isEmpty()) {
					stringbuilder.append(Formatting.RESET);
				}

				stringbuilder.append(s2);
				s = s2;
			}

			stringbuilder.append(s1);
		}

		for (Text c : component.getSiblings()) {
			String s3 = c.getString();
			if (!s3.isEmpty()) {
				String s2 = getFormattingCodesFromStyle(c.getStyle());
				if (!s2.equals(s)) {
					if (!s.isEmpty()) {
						stringbuilder.append(Formatting.RESET);
					}

					stringbuilder.append(s2);
					s = s2;
				}

				stringbuilder.append(s3);
			}
		}

		if (!s.isEmpty()) {
			stringbuilder.append(Formatting.RESET);
		}

		return stringbuilder.toString();
	}

	public static Stream<Text> getStreamOfComponent(Text component) {
		return Streams.concat(Stream.of(component));
	}

	public static String getFormattingCodesFromStyle(Style style) {
		String s = "";

		TextColor c = style.getColor();
		if (c != null) {
			if (c.getRgb() == Formatting.AQUA.getColorValue()) {
				s += "§b";
			}
			if (c.getRgb() == Formatting.BLACK.getColorValue()) {
				s += "§0";
			}
			if (c.getRgb() == Formatting.BLUE.getColorValue()) {
				s += "§9";
			}
			if (c.getRgb() == Formatting.DARK_AQUA.getColorValue()) {
				s += "§3";
			}
			if (c.getRgb() == Formatting.DARK_BLUE.getColorValue()) {
				s += "§1";
			}
			if (c.getRgb() == Formatting.DARK_GRAY.getColorValue()) {
				s += "§8";
			}
			if (c.getRgb() == Formatting.DARK_GREEN.getColorValue()) {
				s += "§2";
			}
			if (c.getRgb() == Formatting.DARK_PURPLE.getColorValue()) {
				s += "§5";
			}
			if (c.getRgb() == Formatting.DARK_RED.getColorValue()) {
				s += "§4";
			}
			if (c.getRgb() == Formatting.GOLD.getColorValue()) {
				s += "§6";
			}
			if (c.getRgb() == Formatting.GRAY.getColorValue()) {
				s += "§7";
			}
			if (c.getRgb() == Formatting.GREEN.getColorValue()) {
				s += "§a";
			}
			if (c.getRgb() == Formatting.LIGHT_PURPLE.getColorValue()) {
				s += "§d";
			}
			if (c.getRgb() == Formatting.RED.getColorValue()) {
				s += "§c";
			}
			if (c.getRgb() == Formatting.WHITE.getColorValue()) {
				s += "§f";
			}
			if (c.getRgb() == Formatting.YELLOW.getColorValue()) {
				s += "§e";
			}
		}

		if (style.isBold()) {
			s += "§l";
		}
		if (style.isItalic()) {
			s += "§o";
		}
		if (style.isObfuscated()) {
			s += "§k";
		}
		if (style.isStrikethrough()) {
			s += "§m";
		}
		if (style.isUnderlined()) {
			s += "§n";
		}

		return s;
	}

	public static String toHexString(int red, int green, int blue) {
		String hex = String. format("#%02X%02X%02X", red, green, blue);
		return hex;
	}
	
	public static LiteralText getWithGradient(java.awt.Color start, String text, java.awt.Color end) {

		LiteralText sc = new LiteralText("");

		int length = text.length();
		int i = 0;
		for (char c : text.toCharArray()) {

			int red = (int)(start.getRed() + ((float)(end.getRed() - start.getRed())) / (length - 1) * i);
			int green = (int)(start.getGreen() + ((float)(end.getGreen() - start.getGreen())) / (length - 1) * i);
			int blue = (int)(start.getBlue() + ((float)(end.getBlue() - start.getBlue())) / (length - 1) * i);

			LiteralText charComp = new LiteralText("" + c);

			Style s = charComp.getStyle();
			TextColor textColor = TextColor.parse(toHexString(red, green, blue));
			Style colored = s.withColor(textColor);

			charComp.setStyle(colored);

			sc.append(charComp);
			
			i++;

		}

		return sc;

	}

}
