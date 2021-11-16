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
import net.minecraft.nbt.NBTTagList;
import wanion.lib.WanionLib;
import wanion.lib.common.control.IControl;
import wanion.lib.common.control.IControlContainer;
import wanion.lib.common.control.IControlControllerProvider;
import wanion.lib.common.field.IField;
import wanion.lib.common.field.IFieldContainer;
import wanion.lib.common.field.IFieldControllerProvider;
import wanion.lib.common.matching.AbstractMatching;
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
		final NBTTagList controlTagList = new NBTTagList();
		smartNBT.setTag("control", controlTagList);
		controlControllerProvider.getControlController().getInstances().forEach(control -> controlTagList.appendTag(control.writeNBT()));
		WanionLib.networkWrapper.sendTo(new SmartNBTMessage(windowId, smartNBT), entityPlayerMP);
	}

	public static void detectAndSendControlChanges(final int windowId, @Nonnull final IControlContainer controlContainer)
	{
		final List<IControl<?>> controlList = controlContainer.getControlController().compareContents(controlContainer.getContainerControlController());
		if (!controlList.isEmpty()) {
			controlContainer.getContainerControlController().forceAdd(controlList.stream().<IControl<?>>map(IControl::copy).collect(Collectors.toList()));
			final NBTTagCompound smartNBT = new NBTTagCompound();
			final NBTTagList controlTagList = new NBTTagList();
			smartNBT.setTag("control", controlTagList);
			controlList.forEach(control -> controlTagList.appendTag(control.writeNBT()));
			for (final IContainerListener containerListener : controlContainer.getListeners())
				if (containerListener instanceof EntityPlayerMP)
					WanionLib.networkWrapper.sendTo(new SmartNBTMessage(windowId, smartNBT), (EntityPlayerMP) containerListener);
		}
	}

	public static void addFieldListener(final int windowId, @Nonnull final IFieldControllerProvider fieldControllerProvider, @Nonnull final EntityPlayerMP entityPlayerMP)
	{
		final NBTTagCompound smartNBT = new NBTTagCompound();
		final NBTTagList fieldTagList = new NBTTagList();
		smartNBT.setTag("field", fieldTagList);
		fieldControllerProvider.getFieldController().getInstances().forEach(field -> fieldTagList.appendTag(field.writeNBT()));
		WanionLib.networkWrapper.sendTo(new SmartNBTMessage(windowId, smartNBT), entityPlayerMP);
	}

	public static void detectAndSendFieldChanges(final int windowId, @Nonnull final IFieldContainer fieldContainer)
	{
		final List<IField<?>> fieldList = fieldContainer.getFieldController().compareContents(fieldContainer.getContainerFieldController());
		if (!fieldList.isEmpty()) {
			fieldList.stream().map(IField::copy).collect(Collectors.toList()).forEach(fieldContainer.getContainerFieldController()::add);
			final NBTTagCompound smartNBT = new NBTTagCompound();
			final NBTTagList fieldTag = new NBTTagList();
			smartNBT.setTag("field", fieldTag);
			fieldList.forEach(field -> fieldTag.appendTag(field.writeNBT()));
			for (final IContainerListener containerListener : fieldContainer.getListeners())
				if (containerListener instanceof EntityPlayerMP)
					WanionLib.networkWrapper.sendTo(new SmartNBTMessage(windowId, smartNBT), (EntityPlayerMP) containerListener);
		}
	}

	public static void addMatchingListener(final int windowId, @Nonnull final IMatchingControllerProvider matchingControllerProvider, @Nonnull final EntityPlayerMP entityPlayerMP)
	{
		final NBTTagCompound smartNBT = new NBTTagCompound();
		final NBTTagList fieldTagList = new NBTTagList();
		smartNBT.setTag("matching", fieldTagList);
		matchingControllerProvider.getMatchingController().getInstances().forEach(matching -> fieldTagList.appendTag(matching.writeNBT()));
		WanionLib.networkWrapper.sendTo(new SmartNBTMessage(windowId, smartNBT), entityPlayerMP);
	}

	public static void detectAndSendMatchingChanges(final int windowId, @Nonnull final IMatchingContainer matchingContainer)
	{
		final List<AbstractMatching<?>> matchingList = matchingContainer.getMatchingController().compareContents(matchingContainer.getContainerMatchingController());
		if (!matchingList.isEmpty()) {
			matchingList.stream().map(AbstractMatching::copy).collect(Collectors.toList()).forEach(matchingContainer.getContainerMatchingController()::add);
			final NBTTagCompound smartNBT = new NBTTagCompound();
			final NBTTagList matchingTag = new NBTTagList();
			smartNBT.setTag("matching", matchingTag);
			matchingList.forEach(matching -> matchingTag.appendTag(matching.writeNBT()));
			for (final IContainerListener containerListener : matchingContainer.getListeners())
				if (containerListener instanceof EntityPlayerMP)
					WanionLib.networkWrapper.sendTo(new SmartNBTMessage(windowId, smartNBT), (EntityPlayerMP) containerListener);
		}
	}
}