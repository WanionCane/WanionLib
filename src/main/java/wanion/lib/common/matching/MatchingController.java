package wanion.lib.common.matching;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import wanion.lib.common.IController;
import wanion.lib.common.ICopyable;
import wanion.lib.common.ISmartNBT;
import wanion.lib.common.WContainer;
import wanion.lib.common.field.IField;
import wanion.lib.network.NetworkHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MatchingController implements IController<MatchingController, Matching>, ICopyable<MatchingController>
{
	private final Int2ObjectMap<Matching> matchingControlMap = new Int2ObjectOpenHashMap<>();
	private final Collection<Matching> values = matchingControlMap.values();
	private final IInventory inventory;

	public MatchingController(@Nonnull final IInventory inventory)
	{
		this.inventory = inventory;
	}

	public MatchingController(@Nonnull final IInventory inventory, final Matching... matchings)
	{
		this(inventory);
		add(matchings);
	}

	public MatchingController(@Nonnull final IInventory inventory, @Nonnull final List<Matching> matchingList)
	{
		this(inventory);
		matchingList.forEach(this::add);
	}

	public MatchingController(@Nonnull final IInventory inventory, @Nonnull final Int2ObjectMap<Matching> matchingMap)
	{
		this(inventory);
		matchingControlMap.putAll(matchingMap);
	}

	public void add(@Nonnull final Matching... matchings)
	{
		for (final Matching matching : matchings)
			matchingControlMap.put(matching.hashCode(), matching);
	}

	public Collection<Matching> getInstances()
	{
		return values;
	}

	public Matching getMatching(final int number)
	{
		return matchingControlMap.get(number);
	}

	@Nonnull
	@Override
	public List<Matching> compareContents(@Nonnull final MatchingController otherMatchingController)
	{
		final List<Matching> differences = new ArrayList<>();
		for (final Matching matching : values) {
			final Matching otherMatching = otherMatchingController.matchingControlMap.get(matching.hashCode());
			if (!matching.equals(otherMatching))
				differences.add(matching);
		}
		return differences;
	}

	@Override
	public void addListener(final int windowId,  @Nonnull final WContainer<?> wContainer, @Nonnull final EntityPlayerMP entityPlayerMP)
	{
		NetworkHelper.addMatchingListener(windowId, wContainer, entityPlayerMP);
	}

	@Override
	public void detectAndSendChanges(final int windowId, @Nonnull final WContainer<?> wContainer)
	{
		NetworkHelper.detectAndSendMatchingChanges(windowId, wContainer);
	}

	@Nonnull
	@Override
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound matchingNBT = new NBTTagCompound();
		final NBTTagList matchingTagList = new NBTTagList();
		matchingNBT.setTag("matching", matchingTagList);
		values.forEach(matching -> matchingTagList.appendTag(matching.writeNBT()));
		return matchingNBT;
	}

	@Override
	public void afterWriteNBT(@Nonnull final NBTTagCompound smartNBT)
	{
		final NBTTagList matchingTagList = smartNBT.getTagList("matching", 10);
		if (matchingTagList.hasNoTags())
			return;
		for (int i = 0; i < matchingTagList.tagCount(); i++) {
			final NBTTagCompound matchingTag = matchingTagList.getCompoundTagAt(i);
			final Matching matching = matchingControlMap.get(matchingTag.getInteger("number"));
			if (matching != null)
				matching.afterWriteNBT(matchingTag);
		}
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound smartNBT)
	{
		final NBTTagList matchingTagList = smartNBT.getTagList("matching", 10);
		if (matchingTagList.hasNoTags())
			return;
		for (int i = 0; i < matchingTagList.tagCount(); i++) {
			final NBTTagCompound matchingTag = matchingTagList.getCompoundTagAt(i);
			final Matching matching = matchingControlMap.get(matchingTag.getInteger("number"));
			if (matching != null)
				matching.readNBT(matchingTag);
		}
		inventory.markDirty();
	}

	@Nonnull
	@Override
	public MatchingController copy()
	{
		return new MatchingController(inventory, values.stream().map(Matching::copy).collect(Collectors.toList()));
	}
}