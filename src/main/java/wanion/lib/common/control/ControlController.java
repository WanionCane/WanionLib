package wanion.lib.common.control;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.common.Dependencies;
import wanion.lib.common.ISmartNBTSync;

import javax.annotation.Nonnull;
import java.util.Collection;

public final class ControlController extends Dependencies<IControl<?>> implements ISmartNBTSync
{
	private final IInventory inventory;

	public ControlController(@Nonnull final IInventory inventory)
	{
		this.inventory = inventory;
	}

	public ControlController(@Nonnull final IInventory inventory, final IControl<?>... dependencies)
	{
		super(dependencies);
		this.inventory = inventory;
	}

	public ControlController(@Nonnull final IInventory inventory, @Nonnull final Collection<IControl<?>> dependencies)
	{
		super(dependencies);
		this.inventory = inventory;
	}

	public ControlController(@Nonnull final IInventory inventory, @Nonnull final ControlController controlController)
	{
		super(controlController);
		this.inventory = inventory;
	}

	@Override
	public void smartNBTSync(@Nonnull NBTTagCompound smartNBT)
	{
		final NBTTagCompound controlNBT = smartNBT.getCompoundTag("control");
		if (controlNBT.hasNoTags())
			return;
		getInstances().forEach(control -> control.readFromNBT(controlNBT));
		inventory.markDirty();
	}
}