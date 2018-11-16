package wanion.lib.common.control;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import wanion.lib.common.Dependencies;

import javax.annotation.Nonnull;
import java.util.Collection;

public final class Controls extends Dependencies<IControl>
{
	public Controls() {}

	public Controls(final IControl... dependencies)
	{
		super(dependencies);
	}

	public Controls(@Nonnull final Collection<IControl> dependencies)
	{
		super(dependencies);
	}

	public Controls(@Nonnull final Controls controls)
	{
		super(controls);
	}
}