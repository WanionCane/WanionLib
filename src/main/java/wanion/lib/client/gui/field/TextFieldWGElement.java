package wanion.lib.client.gui.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.client.gui.WGContainer;
import wanion.lib.client.gui.interaction.WGInteraction;
import wanion.lib.common.field.text.TextField;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class TextFieldWGElement extends WGField<TextField>
{
	private final TextField textField;

	public TextFieldWGElement(@Nonnull final TextField textField, @Nonnull final WGContainer<?> wgContainer, int x, int y, int width, int height)
	{
		super(textField, wgContainer, x, y, width, height);
		this.textField = textField;
	}

	@Override
	public void draw(@Nonnull final WGInteraction wgInteraction)
	{
		//GuiTextField;
		//getField()
	}
}