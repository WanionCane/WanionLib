package wanion.lib.common.matching;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.common.ICopyable;
import wanion.lib.common.ISmartNBT;
import wanion.lib.common.control.IControlNameable;
import wanion.lib.common.matching.matcher.AbstractMatcher;
import wanion.lib.common.matching.matcher.ItemStackMatcher;
import wanion.lib.common.matching.matcher.MatcherEnum;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractMatching<M extends AbstractMatching<M>> implements ISmartNBT, ICopyable<M>, IControlNameable
{
	protected final List<ItemStack> itemStacks;
	protected final int number;
	protected AbstractMatcher<?> matcher = new ItemStackMatcher(this);

	public AbstractMatching(@Nonnull final List<ItemStack> itemStacks, final int number)
	{
		this(itemStacks, number, null);
	}

	public AbstractMatching(@Nonnull final List<ItemStack> itemStacks, final int number, final NBTTagCompound tagToRead)
	{
		this.itemStacks = itemStacks;
		this.number = number;
		if (tagToRead != null)
			readNBT(tagToRead);
	}

	public final void resetMatcher()
	{
		this.matcher = getDefaultMatcher();
	}

	@Nonnull
	public abstract AbstractMatcher<?> getDefaultMatcher();

	public abstract void nextMatcher();

	public final AbstractMatcher<?> getMatcher()
	{
		return matcher;
	}

	public final void setMatcher(@Nonnull final AbstractMatcher<?> matcher)
	{
		this.matcher = matcher.validate();
	}

	public final boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		return matcher.matches(otherItemStack);
	}

	public final void validate()
	{
		this.matcher = matcher.validate();
	}

	public final ItemStack getStack()
	{
		return itemStacks.get(number);
	}

	@Override
	@Nonnull
	public final NBTTagCompound writeNBT()
	{
		final NBTTagCompound matcherNBT = new NBTTagCompound();
		matcherNBT.setInteger("number", number);
		matcherNBT.setString("matcherType", matcher.getMatcherEnum().getLowerCaseName());
		matcherNBT.setTag("matcher", matcher.writeNBT());
		customWriteNBT(matcherNBT);
		return matcherNBT;
	}

	@Override
	public final void readNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		final AbstractMatcher<?> matcher = MatcherEnum.getMatcherEnumByName(nbtTagCompound.getString("matcherType")).getMatcher(this);
		matcher.readNBT(nbtTagCompound.getCompoundTag("matcher"));
		setMatcher(matcher);
		customReadNBT(nbtTagCompound);
	}

	public void customWriteNBT(@Nonnull final NBTTagCompound nbtTagCompound) {}
	public void customReadNBT(@Nonnull final NBTTagCompound nbtTagCompound) {}

	@Override
	@Nonnull
	public final String getControlName()
	{
		return "wanionlib.matching.control";
	}

	public final int getNumber()
	{
		return number;
	}

	@Override
	public final int hashCode()
	{
		return number;
	}
}