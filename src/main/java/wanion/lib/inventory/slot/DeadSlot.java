package wanion.lib.inventory.slot;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public final class DeadSlot extends Slot
{
	public DeadSlot(final IInventory inventory, final int id, final int x, final int y)
	{
		super(inventory, id, x, y);
	}

	@Override
	public boolean isItemValid(@Nonnull final ItemStack itemStack)
	{
		return false;
	}

	@Override
	public boolean canTakeStack(@Nonnull final EntityPlayer entityPlayer)
	{
		return false;
	}
}