package wanion.lib.common.matching.matcher;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.common.matching.AbstractMatching;

import javax.annotation.Nonnull;

public final class EnumMatcher extends AbstractMatcher<EnumMatcher>
{
	private MatcherEnum matcherEnum;

	public EnumMatcher(@Nonnull final AbstractMatching<?> matching)
	{
		this(matching, MatcherEnum.DISABLED);
	}

	public EnumMatcher(@Nonnull final AbstractMatching<?> matching, @Nonnull final MatcherEnum matcherEnum)
	{
		super(matching);
		this.matcherEnum = matcherEnum;
	}

	@Override
	@Nonnull
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound matchingNbt = super.writeNBT();
		if (matcherEnum != MatcherEnum.DISABLED)
			matchingNbt.setString("enum", matcherEnum.lowerCaseName);
		return matchingNbt;
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		matcherEnum = nbtTagCompound.hasKey("enum") ? MatcherEnum.getMatcherEnumByName(nbtTagCompound.getString("enum")) : MatcherEnum.DISABLED;
	}

	@Nonnull
	@Override
	public EnumMatcher copy()
	{
		return new EnumMatcher(matching, matcherEnum);
	}

	@Nonnull
	@Override
	public MatcherEnum getMatcherEnum()
	{
		return MatcherEnum.ENUM;
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> validate()
	{
		return this;
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		return matcherEnum.accepts(otherItemStack);
	}
}