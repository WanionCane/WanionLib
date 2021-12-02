package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final class Util
{
	private Util() {}

	@SuppressWarnings("unchecked")
	public static <T, E extends T> E getField(Class clas, String name, Object instance, Class<T> expectedClass)
	{
		try {
			final Field field = clas.getDeclaredField(name);
			field.setAccessible(true);
			return (E) expectedClass.cast(field.get(instance));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T, E extends T> E getField(Class clas, String unobfuscatedName, String obfuscatedName, Object instance, Class<T> expectedClass)
	{
		try {
			Field field;
			try {
				field = clas.getDeclaredField(obfuscatedName);
			} catch (Exception e) {
				field = clas.getDeclaredField(unobfuscatedName);
			}
			field.setAccessible(true);
			return (E) expectedClass.cast(field.get(instance));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <E> void setField(Class clas, String name, Object instance, E newInstance)
	{
		try {
			final Field field = clas.getDeclaredField(name);
			field.setAccessible(true);
			field.set(instance, newInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Nonnull
	public static String getModName(@Nonnull final ItemStack itemStack)
	{
		Item item;
		if (itemStack == null || (item = itemStack.getItem()) == null)
			return "";
		return item.delegate.name().getResourceDomain();
	}

	public static boolean isFromVanilla(@Nonnull final ItemStack itemStack)
	{
		return getModName(itemStack).equals("minecraft");
	}

	public static boolean areFromSameMod(@Nonnull final ItemStack stack1, @Nonnull final ItemStack stack2)
	{
		return getModName(stack1).equals(getModName(stack2));
	}

	public static <O> void fillArray(@Nonnull final O[] array, @Nonnull final O defaultInstance)
	{
		Arrays.fill(array, defaultInstance);
	}

	@SuppressWarnings("unchecked")
	public static <O> O[] treeDimArrayToTwoDimArray(@Nonnull final O[][] treeDimArray)
	{
		final List<O> oList = new ArrayList<>();
		for (final O[] twoDimArray : treeDimArray)
			if (twoDimArray != null)
				oList.addAll(Arrays.asList(twoDimArray));
		return (O[]) oList.toArray();
	}

	public static ItemStack getStackFromIngredient(@Nonnull final Ingredient ingredient)
	{
		final ItemStack[] stacks = ingredient.getMatchingStacks();
		return stacks.length > 0 ? stacks[0] : ItemStack.EMPTY;
	}

	public static String getOreNameFromOreIngredient(@Nonnull final OreIngredient oreIngredient)
	{
		final NonNullList<ItemStack> ores = getField(OreIngredient.class, "ores", oreIngredient, NonNullList.class);
		final List<NonNullList<ItemStack>> idToStackUn = Util.getField(OreDictionary.class, "idToStackUn", null, List.class);
		if (idToStackUn == null)
			return null;
		for (int i = 0; i < idToStackUn.size(); i++) {
			final NonNullList<ItemStack> stackList = idToStackUn.get(i);
			if (stackList == ores)
				return OreDictionary.getOreName(i);
		}
		return null;
	}

	public static <R> Predicate<R> not(@Nonnull final Predicate<R> predicate)
	{
		return predicate.negate();
	}

	@SuppressWarnings("ConstantConditions")
	public static boolean itemStackHasOres(@Nonnull final ItemStack itemStack)
	{
		return Util.getField(OreDictionary.class, "stackToId", null, Map.class).containsKey(MetaItem.get(itemStack));
	}
}