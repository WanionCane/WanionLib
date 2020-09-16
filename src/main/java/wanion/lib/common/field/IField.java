package wanion.lib.common.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.common.ICopyable;
import wanion.lib.common.INBTMessage;
import wanion.lib.common.ISmartNBT;

import javax.annotation.Nonnull;

public interface IField<F extends IField<F>> extends ISmartNBT, ICopyable<F>, INBTMessage
{
	@Nonnull
	String getFieldName();

	default boolean canInteractWith(@Nonnull EntityPlayerMP player)
	{
		return true;
	}

	default void startInteraction(@Nonnull EntityPlayerMP player) {}

	default void endInteraction(@Nonnull EntityPlayerMP player) {}

	@SideOnly(Side.CLIENT)
	default String getHoveringText(@Nonnull EntityPlayerMP player)
	{
		return null;
	}
}