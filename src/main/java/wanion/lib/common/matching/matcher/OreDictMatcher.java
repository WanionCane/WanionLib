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
import net.minecraftforge.oredict.OreDictionary;
import wanion.lib.common.matching.AbstractMatching;

import javax.annotation.Nonnull;

public final class OreDictMatcher extends AbstractMatcher<OreDictMatcher>
{
	private String oreName;
	private int[] ores;
	private int actualOre;

	public OreDictMatcher(@Nonnull final AbstractMatching<?> matching)
	{
		this(matching, null);
	}

	public OreDictMatcher(@Nonnull final AbstractMatching<?> matching, final String oreName)
	{
		super(matching);
		this.oreName = oreName;
	}

	@Nonnull
	@Override
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound matchingNbt = super.writeNBT();
		matchingNbt.setString("oreName", oreName != null ? oreName : "");
		return matchingNbt;
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		oreName = nbtTagCompound.getString("oreName");
	}

	@Nonnull
	@Override
	public MatcherEnum getMatcherEnum()
	{
		return MatcherEnum.ORE_DICT;
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> validate()
	{
		final ItemStack matchingStack = getStack();
		if (!matchingStack.isEmpty() && this.ores == null)
			this.ores = OreDictionary.getOreIDs(matchingStack);
		if (this.ores == null)
			return matching.getDefaultMatcher();
		if ((oreName == null || oreName.isEmpty()) && ores.length > 0) {
			actualOre = 0;
			this.oreName = OreDictionary.getOreName(getOre());
		}
		if (this.ores != null && !(oreName == null || oreName.isEmpty()) && actualOre < this.ores.length && OreDictionary.getOres(oreName).stream().anyMatch(stack -> OreDictionary.itemMatches(stack, matchingStack, false))) {
			for (int x = 0; x < ores.length; x++) {
				if (OreDictionary.getOreName(ores[x]).equals(oreName)) {
					this.actualOre = x;
					return this;
				}
			}
		}
		return matching.getDefaultMatcher();
	}

	@Override
	public boolean canMoveOn()
	{
		if (++actualOre >= ores.length)
			return true;
		this.oreName = OreDictionary.getOreName(ores[actualOre]);
		return false;
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
		return super.getDescription() + " " + TextFormatting.GOLD + oreName;
	}

	public int getOre()
	{
		return ores[actualOre];
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj instanceof OreDictMatcher) {
			final OreDictMatcher oreDictMatcher = (OreDictMatcher) obj;
			if (!oreName.equals(oreDictMatcher.oreName))
				return false;
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

	@Nonnull
	@Override
	public OreDictMatcher copy()
	{
		final OreDictMatcher nOreDictMatcher = new OreDictMatcher(matching);
		nOreDictMatcher.readNBT(writeNBT());
		return nOreDictMatcher;
	}
}