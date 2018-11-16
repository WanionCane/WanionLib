package wanion.lib.common.control;

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
import wanion.lib.WanionLib;
import wanion.lib.network.ControlsContainerSync;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ControlsContainer extends Container implements IControlsProvider
{
	private final Controls controls;

	public ControlsContainer(@Nonnull final Controls controls)
	{
		this.controls = new Controls(controls.getInstances().stream().<IControl>map(IControl::copy).collect(Collectors.toList()));
	}

	@Override
	public void addListener(final IContainerListener listener)
	{
		super.addListener(listener);
		if (!(listener instanceof EntityPlayerMP))
			return;
		final NBTTagCompound nbtTagCompound = new NBTTagCompound();
		controls.getInstances().forEach(control -> control.writeToNBT(nbtTagCompound));
		WanionLib.networkWrapper.sendTo(new ControlsContainerSync(nbtTagCompound), (EntityPlayerMP) listener);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		final List<IControl> controlList = getControls().compareContents(controls);
		if (!controlList.isEmpty()) {
			controls.forceAdd(controlList.stream().<IControl>map(IControl::copy).collect(Collectors.toList()));
			final NBTTagCompound nbtTagCompound = new NBTTagCompound();
			controlList.forEach(control -> control.writeToNBT(nbtTagCompound));
			for (final IContainerListener containerListener : listeners)
				if (containerListener instanceof EntityPlayerMP)
					WanionLib.networkWrapper.sendTo(new ControlsContainerSync(nbtTagCompound), (EntityPlayerMP) containerListener);
		}
	}

	public final void sync(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		getControls().getInstances().forEach(control -> control.readFromNBT(nbtTagCompound));
	}
}