package wanion.lib.common.matching.matcher;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import wanion.lib.common.Util;
import wanion.lib.common.matching.AbstractMatching;
import wanion.lib.common.matching.Matching;

import javax.annotation.Nonnull;

public final class AnyDamageMatcher extends AbstractMatcher<AnyDamageMatcher>
{
	public AnyDamageMatcher(@Nonnull final AbstractMatching<?> matching)
	{
		super(matching);
	}

	@Nonnull
	@Override
	public MatcherEnum getMatcherEnum()
	{
		return MatcherEnum.ANY_DAMAGE;
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> validate()
	{
		return Util.isDamageable(getStack()) ? this : matching.getDefaultMatcher();
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		final ItemStack itemStack = getStack();
		return !itemStack.isEmpty() && itemStack.getItem() == otherItemStack.getItem();
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj instanceof AnyDamageMatcher;
	}

	@Nonnull
	@Override
	public AnyDamageMatcher copy()
	{
		return new AnyDamageMatcher(matching);
	}
}