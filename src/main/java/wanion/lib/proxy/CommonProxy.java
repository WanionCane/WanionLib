package wanion.lib.proxy;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import wanion.lib.WanionLib;
import wanion.lib.network.ControlsContainerSync;
import wanion.lib.network.RedstoneControlButtonClick;

import javax.annotation.Nonnull;

public class CommonProxy
{
	public final void preInit()
	{
		WanionLib.networkWrapper.registerMessage(RedstoneControlButtonClick.Handler.class, RedstoneControlButtonClick.class, 0, Side.SERVER);
		WanionLib.networkWrapper.registerMessage(RedstoneControlButtonClick.Handler.class, RedstoneControlButtonClick.class, 1, Side.CLIENT);
		WanionLib.networkWrapper.registerMessage(ControlsContainerSync.Handler.class, ControlsContainerSync.class, 2, Side.SERVER);
		WanionLib.networkWrapper.registerMessage(ControlsContainerSync.Handler.class, ControlsContainerSync.class, 3, Side.CLIENT);
	}

	public EntityPlayer getEntityPlayerFromContext(@Nonnull final MessageContext messageContext)
	{
		return messageContext.getServerHandler().player;
	}
}