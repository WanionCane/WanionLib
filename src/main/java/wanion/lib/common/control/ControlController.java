package wanion.lib.common.control;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import wanion.lib.common.Dependencies;
import wanion.lib.common.IController;
import wanion.lib.common.ICopyable;
import wanion.lib.common.WContainer;
import wanion.lib.network.NetworkHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class ControlController extends Dependencies<IControl<?>> implements IController<ControlController, IControl<?>>, ICopyable<ControlController>
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
	@Nonnull
	@Override
	public List<IControl<?>> compareContents(@Nonnull final ControlController otherController)
	{
		return super.compareContents(otherController);
	}

	@Override
	public void addListener(final int windowId,  @Nonnull final WContainer<?> wContainer, @Nonnull final EntityPlayerMP entityPlayerMP)
	{
		NetworkHelper.addControlListener(windowId, wContainer, entityPlayerMP);
	}

	@Override
	public void detectAndSendChanges(final int windowId, @Nonnull final WContainer<?> wContainer)
	{
		NetworkHelper.detectAndSendControlChanges(windowId, wContainer);
	}

	@Nonnull
	@Override
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound controlNBT = new NBTTagCompound();
		final NBTTagList controlTagList = new NBTTagList();
		controlNBT.setTag("control", controlTagList);
		getInstances().forEach(control -> controlTagList.appendTag(control.writeNBT()));
		return controlNBT;
	}

	@Override
	public void afterWriteNBT(@Nonnull final NBTTagCompound smartNBT)
	{
		final NBTTagList controlTagList = smartNBT.getTagList("control", 10);
		if (controlTagList.hasNoTags())
			return;
		getInstances().forEach(control -> {
			for (int i = 0; i < controlTagList.tagCount(); i++)
				control.afterWriteNBT(controlTagList.getCompoundTagAt(i));
		});
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound smartNBT)
	{
		final NBTTagList controlTagList = smartNBT.getTagList("control", 10);
		if (controlTagList.hasNoTags())
			return;
		getInstances().forEach(control -> {
			for (int i = 0; i < controlTagList.tagCount(); i++)
				control.readNBT(controlTagList.getCompoundTagAt(i));
		});
		inventory.markDirty();
	}

	@Nonnull
	@Override
	public ControlController copy()
	{
		return new ControlController(inventory, getInstances().stream().<IControl<?>>map(IControl::copy).collect(Collectors.toList()));
	}


}