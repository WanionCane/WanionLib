package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

public interface IUpdatable
{
    default void update()
    {
        update(System.nanoTime() / (double) 1000000000);
    }

    void update(final double seconds);
}