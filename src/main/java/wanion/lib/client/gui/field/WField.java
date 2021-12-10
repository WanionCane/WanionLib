package wanion.lib.client.gui.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.Reference;
import wanion.lib.client.gui.WGuiContainer;
import wanion.lib.client.gui.WElement;
import wanion.lib.common.field.IField;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public abstract class WField<F extends IField<F>> extends WElement<WField<F>>
{
	protected static final ResourceLocation DEFAULT_RESOURCE_LOCATION = Reference.GUI_TEXTURES;

	protected final F field;

	public WField(@Nonnull final F field, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final int width, final int height)
	{
		super(wGuiContainer, x, y, width, height);
		this.field = field;
	}

	public F getField()
	{
		return field;
	}
}