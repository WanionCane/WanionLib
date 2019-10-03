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
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;
import wanion.lib.common.matching.Matching;

import javax.annotation.Nonnull;

public class OreDictMatcher extends AbstractMatcher
{
	private final int[] ores = !matching.getStack().isEmpty() ? OreDictionary.getOreIDs(getStack()) : new int[0];
	private int actualOre = 0;

	public OreDictMatcher(@Nonnull final Matching matching)
	{
		super(matching);
	}

	@Override
	public void writeToNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		nbtTagCompound.setInteger("actualOre", actualOre);
	}

	@Override
	public void readFromNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		actualOre = nbtTagCompound.getInteger("actualOre");
	}

	@Nonnull
	@Override
	public MatcherEnum getMatcherEnum()
	{
		return MatcherEnum.ORE_DICT;
	}

	@Nonnull
	@Override
	public AbstractMatcher validate()
	{
		return ores.length > 0 && actualOre < ores.length ? this : new ItemStackMatcher(matching);
	}

	@Nonnull
	@Override
	public AbstractMatcher next()
	{
		return ++actualOre < ores.length ? this : new ItemStackMatcher(matching);
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		final int[] ores = OreDictionary.getOreIDs(otherItemStack);
		return ores.length != 0 && matches(ores);
	}

	@Nonnull
	@Override
	public String getDescription()
	{
		return I18n.format("wanionlib.matching.matcher." + getMatcherEnum().name().toLowerCase()) + " " + TextFormatting.GOLD + OreDictionary.getOreName(ores[actualOre]);
	}

	@Nonnull
	@Override
	public String ctFormat()
	{
		return "<ore:" + OreDictionary.getOreName(ores[actualOre]) + ">";
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj instanceof OreDictMatcher) {
			final OreDictMatcher oreDictMatcher = (OreDictMatcher) obj;
			if (this.actualOre != oreDictMatcher.actualOre || ores.length != oreDictMatcher.ores.length)
				return false;
			for (int i = 0; i < ores.length; i++)
				if (ores[i] != oreDictMatcher.ores[i])
					return false;
			return true;
		} else return false;
	}

	private boolean matches(final int[] ores)
	{
		final int actualOre = this.ores[this.actualOre];
		for (int ore : ores)
			if (ore == actualOre)
				return true;
		return false;
	}
}