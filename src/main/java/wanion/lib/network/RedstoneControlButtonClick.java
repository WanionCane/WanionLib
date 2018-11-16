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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wanion.lib.WanionLib;
import wanion.lib.common.control.redstone.IRedstoneControlProvider;
import wanion.lib.common.control.redstone.RedstoneControl;

public class RedstoneControlButtonClick implements IMessage
{
	private boolean leftClick;

	public RedstoneControlButtonClick() {}

	public RedstoneControlButtonClick(final boolean leftClick)
	{
		this.leftClick = leftClick;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		leftClick = buf.getBoolean(0);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeBoolean(leftClick);
	}

	public static class Handler implements IMessageHandler<RedstoneControlButtonClick, IMessage>
	{
		@Override
		public IMessage onMessage(final RedstoneControlButtonClick redstoneControlButtonClick, final MessageContext ctx)
		{
			final EntityPlayer entityPlayer = WanionLib.proxy.getEntityPlayerFromContext(ctx);
			if (entityPlayer != null && entityPlayer.openContainer instanceof IRedstoneControlProvider) {
				final RedstoneControl redstoneControllable = ((IRedstoneControlProvider) entityPlayer.openContainer).getRedstoneControl();
				if (redstoneControlButtonClick.leftClick)
					redstoneControllable.setRedstoneControlState(RedstoneControl.State.getNextRedstoneControlState(redstoneControllable.getRedstoneControlState()));
				else
					redstoneControllable.setRedstoneControlState(RedstoneControl.State.getPreviousRedstoneControlState(redstoneControllable.getRedstoneControlState()));
			}
			return null;
		}
	}
}