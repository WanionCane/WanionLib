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
import wanion.lib.common.IResourceShapedContainer;

import javax.annotation.Nonnull;

public class ClearShapeMessage implements IMessage
{
	private int windowId;

	public ClearShapeMessage(final int windowId)
	{
		this.windowId = windowId;
	}

	public ClearShapeMessage() {}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.windowId = ByteBufUtils.readVarInt(buf, 5);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, windowId, 5);
	}

	@SideOnly(Side.CLIENT)
	public static void sendToServer(@Nonnull final Container container)
	{
		WanionLib.networkWrapper.sendToServer(new ClearShapeMessage(container.windowId));
	}

	public static class Handler implements IMessageHandler<ClearShapeMessage, IMessage>
	{
		@Override
		public IMessage onMessage(final ClearShapeMessage message, final MessageContext ctx)
		{
			WanionLib.proxy.getThreadListener().addScheduledTask(() -> {
				final EntityPlayer entityPlayer = WanionLib.proxy.getEntityPlayerFromContext(ctx);
				if (entityPlayer != null && entityPlayer.openContainer instanceof IResourceShapedContainer && entityPlayer.openContainer.windowId == message.windowId)
					((IResourceShapedContainer) entityPlayer.openContainer).clearShape();
			});
			return null;
		}
	}
}