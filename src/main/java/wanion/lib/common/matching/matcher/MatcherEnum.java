package wanion.lib.common.matching.matcher;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import wanion.lib.common.matching.AbstractMatching;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public enum MatcherEnum
{
	EMPTY(EmptyMatcher.class),
	ITEM_STACK(ItemStackMatcher.class),
	ANY_DAMAGE(AnyDamageMatcher.class),
	NBT(NbtMatcher.class),
	MOD(ModMatcher.class),
	ORE_DICT(OreDictMatcher.class);

	private final static Map<String, MatcherEnum> nameToMatcherEnum = new HashMap<>();

	static {
		for (final MatcherEnum matcherEnum : values())
			nameToMatcherEnum.put(matcherEnum.lowerCaseName, matcherEnum);
	}

	final Class<? extends AbstractMatcher<? extends AbstractMatcher<?>>> matcherClass;
	final String lowerCaseName;

	MatcherEnum(@Nonnull final Class<? extends AbstractMatcher<?>> matcherClass)
	{
		this.matcherClass = matcherClass;
		this.lowerCaseName = name().toLowerCase();
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