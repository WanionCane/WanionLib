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

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
			} catch (Exception e){
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

	public static String getModName(final ItemStack itemStack)
	{
		Item item;
		if (itemStack == null || (item = itemStack.getItem()) == null)
			return "";
		return item.delegate.name().getResourceDomain();
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

	public static ItemStack getStackFromIngredient(final Ingredient ingredient)
	{
		final ItemStack[] stacks = ingredient.getMatchingStacks();
		return stacks.length > 0 ? stacks[0] : ItemStack.EMPTY;
	}
}