package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nonnull;
import java.util.List;

public interface IController<C extends ICopyable<C>, O> extends ICopyable<C>, ISmartNBT
{
    @Nonnull
    List<O> compareContents(@Nonnull final C otherController);

    void addListener(final int windowId, @Nonnull final WContainer<?> wContainer, @Nonnull final EntityPlayerMP entityPlayerMP);

    void detectAndSendChanges(final int windowId, @Nonnull final WContainer<?> wContainer);
}