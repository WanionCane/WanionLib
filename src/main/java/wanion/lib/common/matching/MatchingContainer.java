package wanion.lib.common.matching;

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

public abstract class MatchingContainer extends Container implements IMatchingContainer
{
	private final MatchingController matchingController;
	private final IMatchingInventory matchingInventory;

	public MatchingContainer(@Nonnull final IMatchingInventory matchingInventory)
	{
		this.matchingController = new MatchingController(matchingInventory, matchingInventory.getMatchingController().getInstances().stream().map(Matching::copy).collect(Collectors.toList()));
		this.matchingInventory = matchingInventory;
	}

	@Override
	public void addListener(@Nonnull final IContainerListener listener)
	{
		super.addListener(listener);
		if (!(listener instanceof EntityPlayerMP))
			return;
		NetworkHelper.addMatchingListener(windowId, this, (EntityPlayerMP) listener);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		NetworkHelper.detectAndSendMatchingChanges(windowId, this);
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return matchingInventory.isUsableByPlayer(playerIn);
	}

	@Nonnull
	@Override
	public MatchingController getContainerMatchingController()
	{
		return matchingController;
	}

	@Override
	public Collection<IContainerListener> getListeners()
	{
		return listeners;
	}

	@Nonnull
	@Override
	public MatchingController getMatchingController()
	{
		return matchingInventory.getMatchingController();
	}

	@Override
	public void smartNBTSync(@Nonnull final NBTTagCompound smartNBT)
	{
		matchingController.smartNBTSync(smartNBT);
	}
}