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
import wanion.lib.common.WContainer;
import wanion.lib.common.matching.matcher.EmptyMatcher;
import wanion.lib.network.NetworkHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MatchingController implements IController<MatchingController, AbstractMatching<?>>
{
	private final Int2ObjectMap<AbstractMatching<?>> matchingControlMap = new Int2ObjectOpenHashMap<>();
	private final Collection<AbstractMatching<?>> values = matchingControlMap.values();
	private final IInventory inventory;

	public MatchingController(@Nonnull final IInventory inventory)
	{
		this.inventory = inventory;
	}

	public MatchingController(@Nonnull final IInventory inventory, final AbstractMatching<?>... matchings)
	{
		this(inventory);
		add(matchings);
	}

	public MatchingController(@Nonnull final IInventory inventory, @Nonnull final List<AbstractMatching<?>> matchingList)
	{
		this(inventory);
		matchingList.forEach(this::add);
	}

	public MatchingController(@Nonnull final IInventory inventory, @Nonnull final Int2ObjectMap<AbstractMatching<?>> matchingMap)
	{
		this(inventory);
		matchingControlMap.putAll(matchingMap);
	}

	public void add(@Nonnull final AbstractMatching<?>... matchings)
	{
		for (final AbstractMatching<?> matching : matchings)
			matchingControlMap.put(matching.getNumber(), matching);
	}

	public Collection<AbstractMatching<?>> getInstances()
	{
		return values;
	}

	public AbstractMatching<?> getMatching(final int number)
	{
		return matchingControlMap.get(number);
	}

	@Nonnull
	@Override
	public List<AbstractMatching<?>> compareContents(@Nonnull final MatchingController otherMatchingController)
	{
		final List<AbstractMatching<?>> differences = new ArrayList<>();
		for (final AbstractMatching<?> matching : values) {
			final AbstractMatching<?> otherMatching = otherMatchingController.matchingControlMap.get(matching.hashCode());
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
			final AbstractMatching<?> matching = matchingControlMap.get(matchingTag.getInteger("number"));
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
			final AbstractMatching<?> matching = matchingControlMap.get(matchingTag.getInteger("number"));
			if (matching != null)
				matching.readNBT(matchingTag);
		}
		inventory.markDirty();
	}

	@Nonnull
	@Override
	public MatchingController copy()
	{
		return new MatchingController(inventory, values.stream().map(AbstractMatching::copy).collect(Collectors.toList()));
	}
}