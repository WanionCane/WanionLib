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
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MatchingController
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
		for (final Matching matching : matchings)
			add(matching);
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

	public void add(@Nonnull final Matching matching)
	{
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

	public void syncMatching(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		matchingControlMap.values().forEach(matchingControl -> matchingControl.readFromNBT(nbtTagCompound));
		inventory.markDirty();
	}
}