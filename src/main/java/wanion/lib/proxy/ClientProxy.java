package wanion.lib.proxy;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wanion.lib.WanionLib;
import wanion.lib.common.INBTMessage;
import wanion.lib.network.NBTMessage;

import javax.annotation.Nonnull;

public final class ClientProxy extends CommonProxy
{
	@Override
	public EntityPlayer getEntityPlayerFromContext(@Nonnull final MessageContext messageContext)
	{
		return messageContext.side.isClient() ? Minecraft.getMinecraft().player : super.getEntityPlayerFromContext(messageContext);
	}

	@Override
	public IThreadListener getThreadListener()
	{
		return Minecraft.getMinecraft();
	}

	@Override
	public void receiveNBTMessage(final NBTMessage nbtMessage, final MessageContext ctx)
	{
		final EntityPlayer entityPlayer = WanionLib.proxy.getEntityPlayerFromContext(ctx);
		if (entityPlayer != null && entityPlayer.openContainer.windowId == nbtMessage.getWindowId() && entityPlayer.openContainer instanceof INBTMessage && entityPlayer instanceof EntityPlayerSP && Minecraft.getMinecraft().currentScreen instanceof INBTMessage)
			((INBTMessage) Minecraft.getMinecraft().currentScreen).receiveNBT(nbtMessage.getNbtMessage());
	}
}