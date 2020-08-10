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
import wanion.lib.common.field.IField;
import wanion.lib.common.field.IFieldContainer;
import wanion.lib.common.field.IFieldControllerProvider;
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
		final NBTTagCompound smartNBT = new NBTTagCompound();
		final NBTTagCompound controlNBT = new NBTTagCompound();
		smartNBT.setTag("control", controlNBT);
		controlControllerProvider.getControlController().getInstances().forEach(control -> control.writeToNBT(controlNBT));
		WanionLib.networkWrapper.sendTo(new SmartNBTSync(windowId, smartNBT), entityPlayerMP);
	}

	public static void detectAndSendControlChanges(final int windowId, @Nonnull final IControlContainer controlContainer)
	{
		final List<IControl<?>> controlList = controlContainer.getControlController().compareContents(controlContainer.getContainerControlController());
		if (!controlList.isEmpty()) {
			controlContainer.getContainerControlController().forceAdd(controlList.stream().<IControl<?>>map(IControl::copy).collect(Collectors.toList()));
			final NBTTagCompound smartNBT = new NBTTagCompound();
			final NBTTagCompound controlNBT = new NBTTagCompound();
			smartNBT.setTag("control", controlNBT);
			controlList.forEach(control -> control.writeToNBT(controlNBT));
			for (final IContainerListener containerListener : controlContainer.getListeners())
				if (containerListener instanceof EntityPlayerMP)
					WanionLib.networkWrapper.sendTo(new SmartNBTSync(windowId, smartNBT), (EntityPlayerMP) containerListener);
		}
	}

	public static void addFieldListener(final int windowId, @Nonnull final IFieldControllerProvider fieldControllerProvider, @Nonnull final EntityPlayerMP entityPlayerMP)
	{
		final NBTTagCompound smartNBT = new NBTTagCompound();
		final NBTTagCompound fieldNBT = new NBTTagCompound();
		smartNBT.setTag("field", fieldNBT);
		fieldControllerProvider.getFieldController().getInstances().forEach(field -> field.writeToNBT(fieldNBT));
		WanionLib.networkWrapper.sendTo(new SmartNBTSync(windowId, smartNBT), entityPlayerMP);
	}

	public static void detectAndSendFieldChanges(final int windowId, @Nonnull final IFieldContainer fieldContainer)
	{
		final List<IField<?>> fieldList = fieldContainer.getFieldController().compareContents(fieldContainer.getContainerFieldController());
		if (!fieldList.isEmpty()) {
			fieldList.stream().map(IField::copy).collect(Collectors.toList()).forEach(fieldContainer.getContainerFieldController()::add);
			final NBTTagCompound smartNBT = new NBTTagCompound();
			final NBTTagCompound fieldNBT = new NBTTagCompound();
			smartNBT.setTag("field", fieldNBT);
			fieldList.forEach(field -> field.writeToNBT(fieldNBT));
			for (final IContainerListener containerListener : fieldContainer.getListeners())
				if (containerListener instanceof EntityPlayerMP)
					WanionLib.networkWrapper.sendTo(new SmartNBTSync(windowId, smartNBT), (EntityPlayerMP) containerListener);
		}
	}

	public static void addMatchingListener(final int windowId, @Nonnull final IMatchingControllerProvider matchingControllerProvider, @Nonnull final EntityPlayerMP entityPlayerMP)
	{
		final NBTTagCompound smartNBT = new NBTTagCompound();
		final NBTTagCompound matchingNBT = new NBTTagCompound();
		smartNBT.setTag("matching", matchingNBT);
		matchingControllerProvider.getMatchingController().getInstances().forEach(control -> control.writeToNBT(matchingNBT));
		WanionLib.networkWrapper.sendTo(new SmartNBTSync(windowId, smartNBT), entityPlayerMP);
	}

	public static void detectAndSendMatchingChanges(final int windowId, @Nonnull final IMatchingContainer matchingContainer)
	{
		final List<Matching> matchingList = matchingContainer.getMatchingController().compareContents(matchingContainer.getContainerMatchingController());
		if (!matchingList.isEmpty()) {
			matchingList.stream().map(Matching::copy).collect(Collectors.toList()).forEach(matchingContainer.getContainerMatchingController()::add);
			final NBTTagCompound smartNBT = new NBTTagCompound();
			final NBTTagCompound matchingNBT = new NBTTagCompound();
			smartNBT.setTag("matching", matchingNBT);
			matchingList.forEach(control -> control.writeToNBT(matchingNBT));
			for (final IContainerListener containerListener : matchingContainer.getListeners())
				if (containerListener instanceof EntityPlayerMP)
					WanionLib.networkWrapper.sendTo(new SmartNBTSync(windowId, smartNBT), (EntityPlayerMP) containerListener);
		}
	}
}