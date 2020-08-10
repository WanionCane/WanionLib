package wanion.lib.network;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.WanionLib;
import wanion.lib.common.INameAcceptorContainer;

public class NameTransferMessage implements IMessage
{
	private int windowId;
	private String name;

	public NameTransferMessage() {}

	public NameTransferMessage(final int windowId, final String name)
	{
		this.windowId = windowId;
		this.name = name;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.windowId = ByteBufUtils.readVarInt(buf, 5);
		name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, windowId, 5);
		ByteBufUtils.writeUTF8String(buf, name);
	}

	@SideOnly(Side.CLIENT)
	public static void sendToServer(final Container container, final String name)
	{
		WanionLib.networkWrapper.sendToServer(new NameTransferMessage(container.windowId, name));
	}

	public static class Handler implements IMessageHandler<NameTransferMessage, IMessage>
	{
		@Override
		public IMessage onMessage(final NameTransferMessage message, final MessageContext ctx)
		{
			WanionLib.proxy.getThreadListener().addScheduledTask(() -> {
				final EntityPlayer entityPlayer = WanionLib.proxy.getEntityPlayerFromContext(ctx);
				if (entityPlayer != null && entityPlayer.openContainer instanceof INameAcceptorContainer && entityPlayer.openContainer.windowId == message.windowId)
					((INameAcceptorContainer) entityPlayer.openContainer).acceptName(message.name);
			});
			return null;
		}
	}
}