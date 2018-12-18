package wanion.lib.network;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.WanionLib;
import wanion.lib.common.control.IControl;
import wanion.lib.common.control.IControlContainer;
import wanion.lib.common.control.IControlControllerProvider;
import wanion.lib.common.matching.IMatchingContainer;
import wanion.lib.common.matching.IMatchingControllerProvider;
import wanion.lib.common.matching.Matching;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkHelper
{
	private NetworkHelper() {}

	public static void addControlListener(final int windowId, @Nonnull final IControlControllerProvider controlControllerProvider, @Nonnull final EntityPlayerMP entityPlayerMP)
	{
		final NBTTagCompound nbtTagCompound = new NBTTagCompound();
		controlControllerProvider.getControlController().getInstances().forEach(control -> control.writeToNBT(nbtTagCompound));
		WanionLib.networkWrapper.sendTo(new ControlSync(windowId, nbtTagCompound), entityPlayerMP);
	}

	public static void detectAndSendControlChanges(final int windowId, @Nonnull final IControlContainer controlContainer)
	{
		final List<IControl> controlList = controlContainer.getControlController().compareContents(controlContainer.getContainerControlController());
		if (!controlList.isEmpty()) {
			controlContainer.getContainerControlController().forceAdd(controlList.stream().<IControl>map(IControl::copy).collect(Collectors.toList()));
			final NBTTagCompound nbtTagCompound = new NBTTagCompound();
			controlList.forEach(control -> control.writeToNBT(nbtTagCompound));
			for (final IContainerListener containerListener : controlContainer.getListeners())
				if (containerListener instanceof EntityPlayerMP)
					WanionLib.networkWrapper.sendTo(new ControlSync(windowId, nbtTagCompound), (EntityPlayerMP) containerListener);
		}
	}


	public static void addMatchingListener(final int windowId, @Nonnull final IMatchingControllerProvider matchingControllerProvider, @Nonnull final EntityPlayerMP entityPlayerMP)
	{
		final NBTTagCompound nbtTagCompound = new NBTTagCompound();
		matchingControllerProvider.getMatchingController().getInstances().forEach(control -> control.writeToNBT(nbtTagCompound));
		WanionLib.networkWrapper.sendTo(new MatchingSync(windowId, nbtTagCompound), entityPlayerMP);
	}

	public static void detectAndSendMatchingChanges(final int windowId, @Nonnull final IMatchingContainer matchingContainer)
	{
		final List<Matching> controlList = matchingContainer.getMatchingController().compareContents(matchingContainer.getContainerMatchingController());
		if (!controlList.isEmpty()) {
			controlList.stream().map(Matching::copy).collect(Collectors.toList()).forEach(matchingContainer.getContainerMatchingController()::add);
			final NBTTagCompound nbtTagCompound = new NBTTagCompound();
			controlList.forEach(control -> control.writeToNBT(nbtTagCompound));
			for (final IContainerListener containerListener : matchingContainer.getListeners())
				if (containerListener instanceof EntityPlayerMP)
					WanionLib.networkWrapper.sendTo(new MatchingSync(windowId, nbtTagCompound), (EntityPlayerMP) containerListener);
		}
	}
}