package wanion.lib.client;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ClientHelper
{
	private ClientHelper() {}

	public static String getProblemFound(final boolean plural)
	{
		final String s = !plural ? "singular" : "plural";
		return TextFormatting.RED + I18n.format("wanionlib.problem." + s) + " " + I18n.format("wanionlib.found." + s) + ":";
	}

	public static String getSuccess()
	{
		return TextFormatting.GREEN + I18n.format("wanionlib.success");
	}
}