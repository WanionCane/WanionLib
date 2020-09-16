package wanion.lib.common.matching.matcher;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.common.ICopyable;
import wanion.lib.common.ISmartNBT;
import wanion.lib.common.matching.Matching;

import javax.annotation.Nonnull;

public abstract class AbstractMatcher<M extends AbstractMatcher<M>> implements ISmartNBT, ICopyable<M>
{
	protected Matching matching;

	public AbstractMatcher(@Nonnull final Matching matching)
	{
		this.matching = matching;
	}

	public ItemStack getStack()
	{
		return matching.getStack();
	}

	@Nonnull
	@Override
	public NBTTagCompound writeNBT() { return new NBTTagCompound();	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound nbtTagCompound) {}

	@Nonnull
	public abstract MatcherEnum getMatcherEnum();

	@Nonnull
	public abstract AbstractMatcher<?> validate();

	@Nonnull
	public abstract AbstractMatcher<?> next();

	public abstract boolean matches(@Nonnull ItemStack otherItemStack);

	@Nonnull
	public abstract String ctFormat();

	@Nonnull
	public String getDescription()
	{
		return I18n.format("wanionlib.matching.matcher." + getMatcherEnum().name().toLowerCase());
	}
}