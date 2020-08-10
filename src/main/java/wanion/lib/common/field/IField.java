package wanion.lib.common.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.common.INBTReceiver;
import wanion.lib.common.ISmartNBT;

import javax.annotation.Nonnull;

public interface IField<F extends IField<?>> extends ISmartNBT, INBTReceiver
{
	@Nonnull
	String getFieldName();

	@Nonnull
	F copy();

	default boolean canInteractWith(@Nonnull EntityPlayer player)
	{
		return true;
	}

	default void startInteraction(@Nonnull EntityPlayer player) {}

	default void endInteraction(@Nonnull EntityPlayer player) {}

	default void update() {}

	@SideOnly(Side.CLIENT)
	default String getHoveringText()
	{
		return null;
	}
}