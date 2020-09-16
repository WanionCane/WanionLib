package wanion.lib.common;

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
import wanion.lib.common.control.ControlController;
import wanion.lib.common.control.IControlContainer;
import wanion.lib.common.field.FieldController;
import wanion.lib.common.field.IFieldContainer;
import wanion.lib.common.matching.IMatchingContainer;
import wanion.lib.common.matching.MatchingController;
import wanion.lib.network.NetworkHelper;

import javax.annotation.Nonnull;
import java.util.Collection;

public class ControlFieldMatchingContainer extends Container implements IControlContainer, IFieldContainer, IMatchingContainer
{
	private final ControlController controlController;
	private final FieldController fieldController;
	private final MatchingController matchingController;
	private final IControlFieldMatchingInventory controlFieldMatchingInventory;

	public ControlFieldMatchingContainer(@Nonnull final IControlFieldMatchingInventory controlFieldMatchingInventory)
	{
		this.controlController = controlFieldMatchingInventory.getControlController().copy();
		this.fieldController = controlFieldMatchingInventory.getFieldController().copy();
		this.matchingController = controlFieldMatchingInventory.getMatchingController().copy();
		this.controlFieldMatchingInventory = controlFieldMatchingInventory;
	}

	@Override
	public void addListener(@Nonnull final IContainerListener listener)
	{
		super.addListener(listener);
		if (!(listener instanceof EntityPlayerMP))
			return;
		NetworkHelper.addControlListener(windowId, this, (EntityPlayerMP) listener);
		NetworkHelper.addFieldListener(windowId, this, (EntityPlayerMP) listener);
		NetworkHelper.addMatchingListener(windowId, this, (EntityPlayerMP) listener);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		NetworkHelper.detectAndSendControlChanges(windowId, this);
		NetworkHelper.detectAndSendFieldChanges(windowId, this);
		NetworkHelper.detectAndSendMatchingChanges(windowId, this);
	}

	@Override
	public boolean canInteractWith(@Nonnull final EntityPlayer playerIn)
	{
		return controlFieldMatchingInventory.isUsableByPlayer(playerIn);
	}

	@Nonnull
	@Override
	public ControlController getContainerControlController()
	{
		return controlController;
	}

	@Nonnull
	@Override
	public ControlController getControlController()
	{
		return controlFieldMatchingInventory.getControlController();
	}

	@Nonnull
	@Override
	public FieldController getContainerFieldController()
	{
		return fieldController;
	}

	@Nonnull
	@Override
	public FieldController getFieldController()
	{
		return controlFieldMatchingInventory.getFieldController();
	}

	@Nonnull
	@Override
	public MatchingController getContainerMatchingController()
	{
		return matchingController;
	}

	@Nonnull
	@Override
	public MatchingController getMatchingController()
	{
		return controlFieldMatchingInventory.getMatchingController();
	}

	@Override
	public Collection<IContainerListener> getListeners()
	{
		return listeners;
	}

	@Override
	public void receiveNBT(@Nonnull NBTTagCompound nbtTagCompound)
	{
		fieldController.receiveNBT(nbtTagCompound);
	}

	@Override
	public void readNBT(@Nonnull NBTTagCompound smartNBT)
	{
		controlController.readNBT(smartNBT);
		fieldController.readNBT(smartNBT);
		matchingController.readNBT(smartNBT);
		controlFieldMatchingInventory.markDirty();
	}
}