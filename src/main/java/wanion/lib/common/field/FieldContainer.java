package wanion.lib.common.field;

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

public  class FieldContainer extends Container implements IFieldContainer
{
	private final FieldController fieldController;
	private final IFieldInventory fieldInventory;

	public FieldContainer(@Nonnull final IFieldInventory fieldInventory)
	{
		this.fieldController = new FieldController(fieldInventory, fieldInventory.getFieldController().getInstances().stream().<IField<?>>map(IField::copy).collect(Collectors.toList()));
		this.fieldInventory = fieldInventory;
	}

	@Override
	public void addListener(@Nonnull final IContainerListener listener)
	{
		super.addListener(listener);
		if (!(listener instanceof EntityPlayerMP))
			return;
		NetworkHelper.addFieldListener(windowId, this, (EntityPlayerMP) listener);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		NetworkHelper.detectAndSendFieldChanges(windowId, this);
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return fieldInventory.isUsableByPlayer(playerIn);
	}

	@Nonnull
	@Override
	public FieldController getFieldController()
	{
		return fieldInventory.getFieldController();
	}

	@Nonnull
	@Override
	public FieldController getContainerFieldController()
	{
		return fieldController;
	}

	@Nonnull
	@Override
	public Collection<IContainerListener> getListeners()
	{
		return listeners;
	}

	@Override
	public void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		fieldController.receiveNBT(nbtTagCompound);
	}

	@Override
	public void readNBT(@Nonnull NBTTagCompound smartNBT)
	{
		fieldController.readNBT(smartNBT);
		fieldInventory.markDirty();
	}
}