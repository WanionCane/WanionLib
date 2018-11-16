package wanion.lib.common.control;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public interface IControl<I extends IControl>
{
	default boolean canOperate()
	{
		return true;
	}

	default void operate() {}

	void writeToNBT(@Nonnull NBTTagCompound nbtTagCompound);

	void readFromNBT(@Nonnull NBTTagCompound nbtTagCompound);

	@Nonnull
	I copy();
}