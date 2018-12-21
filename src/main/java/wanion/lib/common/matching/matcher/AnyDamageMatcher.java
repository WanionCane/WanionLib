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
import wanion.lib.common.matching.Matching;

import javax.annotation.Nonnull;

public class AnyDamageMatcher extends AbstractMatcher
{
	public AnyDamageMatcher(@Nonnull final Matching matching)
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
	public AbstractMatcher validate()
	{
		final ItemStack itemStack = getStack();
		return itemStack.getHasSubtypes() || itemStack.isItemStackDamageable() ? this : new ItemStackMatcher(matching);
	}

	@Nonnull
	@Override
	public AbstractMatcher next()
	{
		final ItemStack itemStack = getStack();
		return matching.shouldUseNbt() && itemStack.hasTagCompound() ? new NbtMatcher(matching) : OreDictionary.getOreIDs(itemStack).length > 0 ? new OreDictMatcher(matching) : new ItemStackMatcher(matching);
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		final ItemStack itemStack = getStack();
		return !itemStack.isEmpty() && itemStack.getItem() == otherItemStack.getItem();
	}

	@Nonnull
	@Override
	public String format()
	{
		final ItemStack itemStack = getStack();
		final StringBuilder formatBuilder = new StringBuilder().append('<');
		formatBuilder.append(itemStack.getItem().getRegistryName()).append(":*>");
		if (itemStack.getCount() > 1)
			formatBuilder.append(" * ").append(itemStack.getCount());
		return formatBuilder.toString();
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj == this || obj instanceof AnyDamageMatcher;
	}
}