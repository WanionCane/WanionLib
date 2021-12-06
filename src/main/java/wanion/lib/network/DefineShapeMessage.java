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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.WanionLib;
import wanion.lib.common.IResourceShapedContainer;

import javax.annotation.Nonnull;

public class DefineShapeMessage implements IMessage
{
	private int windowId;
	private ResourceLocation resourceLocation;

	public DefineShapeMessage(final int windowId, final ResourceLocation resourceLocation)
	{
		this.windowId = windowId;
		this.resourceLocation = resourceLocation;
	}

	public DefineShapeMessage() {}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.windowId = ByteBufUtils.readVarInt(buf, 5);
		resourceLocation = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, windowId, 5);
		ByteBufUtils.writeUTF8String(buf, resourceLocation.toString());
	}

	@SideOnly(Side.CLIENT)
	public static void sendToServer(@Nonnull final Container container, @Nonnull final ResourceLocation resourceLocation)
	{
		WanionLib.networkWrapper.sendToServer(new DefineShapeMessage(container.windowId, resourceLocation));
	}

	public static class Handler implements IMessageHandler<DefineShapeMessage, IMessage>
	{
		@Override
		public IMessage onMessage(final DefineShapeMessage message, final MessageContext ctx)
		{
			WanionLib.proxy.getThreadListener().addScheduledTask(() -> {
				final EntityPlayer entityPlayer = WanionLib.proxy.getEntityPlayerFromContext(ctx);
				if (entityPlayer != null && entityPlayer.openContainer instanceof IResourceShapedContainer && entityPlayer.openContainer.windowId == message.windowId)
					((IResourceShapedContainer) entityPlayer.openContainer).defineShape(message.resourceLocation);
			});
			return null;
		}
	}
}