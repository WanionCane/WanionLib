package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.WanionLib;
import wanion.lib.network.NBTMessage;

import javax.annotation.Nonnull;

public interface INBTMessage
{
	static void sendNBT(final int windowId, @Nonnull final NBTTagCompound nbtTagCompound)
	{
		WanionLib.networkWrapper.sendToServer(new NBTMessage(windowId, nbtTagCompound));
	}

	static void sendNBT(final int windowId, @Nonnull final NBTTagCompound nbtTagCompound, @Nonnull final EntityPlayerMP entityPlayerMP)
	{
		WanionLib.networkWrapper.sendTo(new NBTMessage(windowId, nbtTagCompound), entityPlayerMP);
	}

	void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound);
}