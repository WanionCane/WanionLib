package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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

	void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound);
}