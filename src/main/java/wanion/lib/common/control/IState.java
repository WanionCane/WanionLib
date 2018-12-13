package wanion.lib.common.control;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IState<S extends IState>
{
	@Nonnull
	S getNextState();

	@Nonnull
	S getPreviousState();

	@Nullable
	default Pair<Integer, Integer> getTexturePos(boolean hovered)
	{
		return null;
	}
}