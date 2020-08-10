package wanion.lib.network;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class EmptyMessage implements IMessage
{
	public int windowId;

	public EmptyMessage() {}

	public EmptyMessage(final int windowId)
	{
		this.windowId = windowId;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.windowId = ByteBufUtils.readVarInt(buf, 5);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, windowId, 5);
	}
}