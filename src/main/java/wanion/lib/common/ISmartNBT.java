package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public interface ISmartNBT
{
	@Nonnull
	default NBTTagCompound writeNBT()
	{
		return new NBTTagCompound();
	}

	// This method should only be called in Tile Saving!!
	default void afterWriteNBT(@Nonnull final NBTTagCompound smartNBT) {}

	void readNBT(@Nonnull NBTTagCompound smartNBT);



}