package wanion.lib.common.matching.matcher;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import wanion.lib.common.Util;
import wanion.lib.common.matching.AbstractMatching;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static wanion.lib.common.Util.not;

public enum MatcherEnum
{
	DISABLED(DisabledMatcher.class),
	ENUM(EnumMatcher.class),
	EMPTY(EmptyMatcher.class, ItemStack::isEmpty),
	ORE_DICT(OreDictMatcher.class, Util::itemStackHasOres),
	NBT(NbtMatcher.class, ItemStack::hasTagCompound, ORE_DICT),
	ANY_DAMAGE(AnyDamageMatcher.class, Util::isDamageable, NBT),
	ITEM_STACK(ItemStackMatcher.class, not(ItemStack::isEmpty), ANY_DAMAGE),
	DAMAGED(DamagedMatcher.class, ItemStack::isItemDamaged),
	VANILLA(VanillaMatcher.class, Util::isFromVanilla),
	MOD(ModMatcher.class, not(Util::isFromVanilla));

	private final static Map<String, MatcherEnum> nameToMatcherEnum = new HashMap<>();
	private final static Map<MatcherEnum, MatcherEnum> matcherToNextMatcher = new EnumMap<>(MatcherEnum.class);

	static {
		for (final MatcherEnum matcherEnum : values()) {
			nameToMatcherEnum.put(matcherEnum.lowerCaseName, matcherEnum);
			final MatcherEnum nextMatcherEnum = matcherEnum.nextMatcherEnum;
			matcherToNextMatcher.put(matcherEnum, nextMatcherEnum != null ? nextMatcherEnum : ITEM_STACK);
		}
	}

	final Class<? extends AbstractMatcher<? extends AbstractMatcher<?>>> matcherClass;
	private final Predicate<ItemStack> accepts;
	private final MatcherEnum nextMatcherEnum;
	final String lowerCaseName;

	MatcherEnum(@Nonnull final Class<? extends AbstractMatcher<?>> matcherClass)
	{
		this(matcherClass, i -> false, null);
	}

	MatcherEnum(@Nonnull final Class<? extends AbstractMatcher<?>> matcherClass, @Nonnull final Predicate<ItemStack> accepts)
	{
		this(matcherClass, accepts, null);
	}

	MatcherEnum(@Nonnull final Class<? extends AbstractMatcher<?>> matcherClass, @Nonnull final Predicate<ItemStack> accepts, final MatcherEnum matcherEnum)
	{
		this.matcherClass = matcherClass;
		this.accepts = accepts;
		this.lowerCaseName = name().toLowerCase();
		this.nextMatcherEnum = matcherEnum;
	}

	public boolean accepts(@Nonnull final ItemStack itemStack)
	{
		return accepts.test(itemStack);
	}

	@Nonnull
	public MatcherEnum getNextMatcherEnum(@Nonnull final AbstractMatcher<?> abstractMatcher)
	{
		return !abstractMatcher.canMoveOn() ? this : matcherToNextMatcher.get(this);
	}

	@Nonnull
	public static MatcherEnum getMatcherEnumByName(@Nonnull final String name)
	{
		final MatcherEnum matcherEnum = nameToMatcherEnum.get(name);
		return matcherEnum != null ? matcherEnum : EMPTY;
	}

	@Nonnull
	public AbstractMatcher<?> getMatcher(@Nonnull final AbstractMatching<?> matching)
	{
		final AbstractMatcher<?> matcher = matching.getMatcher();
		if (matcherClass.isInstance(matcher))
			return matcher;
		try {
			final Constructor<? extends AbstractMatcher<?>> constructor = matcherClass.getDeclaredConstructor(AbstractMatching.class);
			return constructor.newInstance(matching);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new RuntimeException("This Should never happen: " + e);
		}
	}

	public String getLowerCaseName()
	{
		return lowerCaseName;
	}
}