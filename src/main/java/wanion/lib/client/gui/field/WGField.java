package wanion.lib.client.gui.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.client.gui.WGContainer;
import wanion.lib.client.gui.WGElement;
import wanion.lib.common.field.IField;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public abstract class WGField<F extends IField<F>> extends WGElement
{
	protected final F field;

	public WGField(@Nonnull final F field, @Nonnull final WGContainer<?> wgContainer, final int x, final int y, final int width, final int height)
	{
		super(wgContainer, x, y, width, height);
		this.field = field;
	}

	public F getField()
	{
		return field;
	}
}