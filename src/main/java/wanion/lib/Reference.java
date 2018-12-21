package wanion.lib;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.util.ResourceLocation;

public final class Reference
{
	public static final String MOD_ID = "wanionlib";
	public static final String MOD_NAME = "WanionLib";
	public static final String MOD_VERSION = "1.12.2-2.2";
	public static final String ACCEPTED_MINECRAFT = "[1.12,]";
	public static final String CLIENT_PROXY = "wanion.lib.proxy.ClientProxy";
	public static final String SERVER_PROXY = "wanion.lib.proxy.CommonProxy";
	public static final ResourceLocation GUI_TEXTURES = new ResourceLocation(MOD_ID, "textures/gui/gui_textures.png");

	private Reference() {}
}