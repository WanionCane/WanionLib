package wanion.lib.common.control;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.network.NetworkHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.stream.Collectors;

public class ControlContainer extends Container implements IControlContainer
{
	private final ControlController controlController;
	private final IControlInventory controlInventory;

	public ControlContainer(@Nonnull final IControlInventory controlInventory)
	{
		this.controlController = new ControlController(controlInventory, controlInventory.getControlController().getInstances().stream().<IControl<?>>map(IControl::copy).collect(Collectors.toList()));
		this.controlInventory = controlInventory;
	}

	@Override
	public void addListener(@Nonnull final IContainerListener listener)
	{
		super.addListener(listener);
		if (!(listener instanceof EntityPlayerMP))
			return;
		NetworkHelper.addControlListener(windowId, this, (EntityPlayerMP) listener);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		NetworkHelper.detectAndSendControlChanges(windowId, this);
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return controlInventory.isUsableByPlayer(playerIn);
	}

	@Nonnull
	@Override
	public ControlController getControlController()
	{
		return controlInventory.getControlController();
	}

	@Nonnull
	@Override
	public ControlController getContainerControlController()
	{
		return controlController;
	}

	@Nonnull
	@Override
	public Collection<IContainerListener> getListeners()
	{
		return listeners;
	}

	@Override
	public void readNBT(@Nonnull NBTTagCompound smartNBT)
	{
		controlController.readNBT(smartNBT);
		controlInventory.markDirty();
	}
}