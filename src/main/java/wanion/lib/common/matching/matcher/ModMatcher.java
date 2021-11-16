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
import net.minecraft.util.text.TextFormatting;
import wanion.lib.common.Util;
import wanion.lib.common.matching.AbstractMatching;

import javax.annotation.Nonnull;

public final class ModMatcher extends AbstractMatcher<ModMatcher>
{
	private String modId;

	public ModMatcher(@Nonnull final AbstractMatching<?> matching)
	{
		super(matching);
	}

	@Override
	@Nonnull
	public ModMatcher copy()
	{
		final ModMatcher nModMatcher = new ModMatcher(matching);
		nModMatcher.readNBT(writeNBT());
		return nModMatcher;
	}

	@Override
	@Nonnull
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound matchingNbt = super.writeNBT();
		if (modId != null)
			matchingNbt.setString("modId", modId);
		return matchingNbt;
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		if (nbtTagCompound.hasKey("modId"))
			modId = nbtTagCompound.getString("modId");
	}

	@Nonnull
	@Override
	public MatcherEnum getMatcherEnum()
	{
		return MatcherEnum.MOD;
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> validate()
	{
		final ItemStack stack = getStack();
		if (!stack.isEmpty())
			modId = Util.getModName(stack);
		return modId != null ? this : matching.getDefaultMatcher();
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> next()
	{
		return matching.getDefaultMatcher();
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		final ItemStack stack = getStack();
		return !stack.isEmpty() && !otherItemStack.isEmpty() && Util.areFromSameMod(stack, otherItemStack);
	}

	@Nonnull
	@Override
	public String getDescription()
	{
		return super.getDescription() + " " + TextFormatting.GOLD + modId;
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj == this || (obj instanceof ModMatcher && modId != null && modId.equals(((ModMatcher) obj).modId));
	}
}