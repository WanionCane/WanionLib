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
import net.minecraftforge.oredict.OreDictionary;
import wanion.lib.common.NBTUtils;
import wanion.lib.common.matching.Matching;

import javax.annotation.Nonnull;

public class NbtMatcher extends AbstractMatcher
{
	public NbtMatcher(@Nonnull final Matching matching)
	{
		super(matching);
	}

	@Nonnull
	@Override
	public MatcherEnum getMatcherEnum()
	{
		return MatcherEnum.NBT;
	}

	@Nonnull
	@Override
	public AbstractMatcher validate()
	{
		return getStack().hasTagCompound() ? this : new ItemStackMatcher(matching);
	}

	@Nonnull
	@Override
	public AbstractMatcher next()
	{
		return OreDictionary.getOreIDs(getStack()).length > 0 ? new OreDictMatcher(matching) : new ItemStackMatcher(matching);
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		final ItemStack itemStack = getStack();
		return otherItemStack.hasTagCompound() && itemStack.isItemEqual(otherItemStack) && ItemStack.areItemStackTagsEqual(itemStack, otherItemStack);
	}

	@Nonnull
	@Override
	public String ctFormat()
	{
		final ItemStack itemStack = getStack();
		final boolean greaterThanOne = itemStack.getCount() > 1;
		final StringBuilder formatBuilder = new StringBuilder();
		if (greaterThanOne)
			formatBuilder.append('(');
		formatBuilder.append('<').append(itemStack.getItem().getRegistryName());
		if (itemStack.getItemDamage() > 0)
			formatBuilder.append(':').append(itemStack.getItemDamage());
		formatBuilder.append('>');
		if (greaterThanOne)
			formatBuilder.append(" * ").append(itemStack.getCount()).append(')');
		final NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
		formatBuilder.append(".withTag(");
		if (nbtTagCompound != null)
			formatBuilder.append(NBTUtils.formatNbt(nbtTagCompound));
		formatBuilder.append(')');
		return formatBuilder.toString();
	}
}