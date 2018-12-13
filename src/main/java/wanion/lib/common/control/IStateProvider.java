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

public interface IStateProvider<C extends IControl, S extends IState<S>> extends IControl<C>
{
	S getState();

	void setState(@Nonnull S state);

	void writeToNBT(@Nonnull NBTTagCompound nbtTagCompound, @Nonnull S state);
}