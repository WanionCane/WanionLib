package wanion.lib.common.matching.matcher;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import wanion.lib.common.matching.Matching;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum MatcherEnum
{
	ITEM_STACK(ItemStackMatcher.class),
	ANY_DAMAGE(AnyDamageMatcher.class),
	NBT(NbtMatcher.class),
	ORE_DICT(OreDictMatcher.class);

	final Class<? extends AbstractMatcher> matcherClass;

	MatcherEnum(@Nonnull final Class<? extends AbstractMatcher> matcherClass)
	{
		this.matcherClass = matcherClass;
	}

	@Nonnull
	public AbstractMatcher getMatcher(@Nonnull final Matching matching)
	{
		try {
			final Constructor<? extends AbstractMatcher> constructor = matcherClass.getDeclaredConstructor(Matching.class);
			return constructor.newInstance(matching);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new RuntimeException("This Should never happen: " + e);
		}
	}
}