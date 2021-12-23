package wanion.lib.client.gui.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import com.google.common.collect.Lists;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.WanionLib;
import wanion.lib.client.gui.WGuiContainer;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.client.gui.interaction.WMouseInteraction;
import wanion.lib.common.INBTMessage;
import wanion.lib.common.field.CheckBox;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class CheckBoxWElement extends WField<CheckBox>
{
	public CheckBoxWElement(@Nonnull final CheckBox field, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		super(field, wGuiContainer, x, y, 18, 18);
		setTooltipSupplier((interaction, stackSupplier) -> Lists.newArrayList(field.getHoveringText(interaction)));
	}

	@Override
	public void draw(@Nonnull final WInteraction wInteraction)
	{
		final boolean isHovering = wInteraction.isHovering(this);
		getTextureManager().bindTexture(DEFAULT_RESOURCE_LOCATION);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		Gui.drawModalRectWithCustomSizedTexture(getUsableX(), getUsableY(), !isHovering ? 36 : 54, !field.isChecked() ? 72 : 90, width, height, 128, 128);
	}

	@Override
	public void interaction(@Nonnull final WMouseInteraction wMouseInteraction)
	{
		INBTMessage.sendNBT(getWindowID(), field.toggle().writeNBT());
		playPressSound();
	}
}