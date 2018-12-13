package wanion.lib.proxy;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import wanion.lib.WanionLib;
import wanion.lib.network.ControlsSync;

import javax.annotation.Nonnull;

public class CommonProxy
{
	public final void preInit()
	{
		int d = 0;
		WanionLib.networkWrapper.registerMessage(ControlsSync.Handler.class, ControlsSync.class, d++, Side.SERVER);
		WanionLib.networkWrapper.registerMessage(ControlsSync.Handler.class, ControlsSync.class, d, Side.CLIENT);
	}

	public EntityPlayer getEntityPlayerFromContext(@Nonnull final MessageContext messageContext)
	{
		return messageContext.getServerHandler().player;
	}

	public final boolean isClient()
	{
		return FMLCommonHandler.instance().getEffectiveSide().isClient();
	}

	public final boolean isServer()
	{
		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}

	public IThreadListener getThreadListener()
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}
}