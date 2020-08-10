package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.common.control.ControlController;
import wanion.lib.common.control.IControl;
import wanion.lib.common.control.IControlContainer;
import wanion.lib.common.matching.IMatchingContainer;
import wanion.lib.common.matching.Matching;
import wanion.lib.common.matching.MatchingController;
import wanion.lib.network.NetworkHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class ControlMatchingContainer extends Container implements IControlContainer, IMatchingContainer
{
	private final ControlController controlController;
	private final MatchingController matchingController;
	private final IControlMatchingInventory controlMatchingInventory;

	public ControlMatchingContainer(@Nonnull final IControlMatchingInventory controlMatchingInventory)
	{
		this.controlController = new ControlController(controlMatchingInventory, controlMatchingInventory.getControlController().getInstances().stream().<IControl<?>>map(IControl::copy).collect(Collectors.toList()));
		this.matchingController = new MatchingController(controlMatchingInventory, controlMatchingInventory.getMatchingController().getInstances().stream().map(Matching::copy).collect(Collectors.toList()));
		this.controlMatchingInventory = controlMatchingInventory;
	}

	@Override
	public void addListener(@Nonnull final IContainerListener listener)
	{
		super.addListener(listener);
		if (!(listener instanceof EntityPlayerMP))
			return;
		NetworkHelper.addControlListener(windowId, this, (EntityPlayerMP) listener);
		NetworkHelper.addMatchingListener(windowId, this, (EntityPlayerMP) listener);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		NetworkHelper.detectAndSendControlChanges(windowId, this);
		NetworkHelper.detectAndSendMatchingChanges(windowId, this);
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
		return controlMatchingInventory.getControlController();
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
		return controlMatchingInventory.getMatchingController();
	}

	@Override
	public Collection<IContainerListener> getListeners()
	{
		return listeners;
	}

	@Override
	public void smartNBTSync(@Nonnull NBTTagCompound smartNBT)
	{
		controlController.smartNBTSync(smartNBT);
		matchingController.smartNBTSync(smartNBT);
		controlMatchingInventory.markDirty();
	}
}