package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.Reference;
import wanion.lib.client.gui.interaction.WInteraction;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public class LetterElement extends WElement<LetterElement>
{
	private final String letter;
	private final FontRenderer fontRenderer;
	private final Supplier<Integer> letterX, letterY;

	public LetterElement(final char letter, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		super(wGuiContainer, x, y, 18, 18);
		this.letter = TextFormatting.BOLD + Character.toString(letter);
		this.fontRenderer = getFontRenderer();
		this.letterX = () -> getUsableX() + 9 -(fontRenderer.getStringWidth(Character.toString(letter)) / 2);
		this.letterY = () -> getUsableY() + 10 -(fontRenderer.FONT_HEIGHT / 2);
	}

	@Override
	public void draw(@Nonnull final WInteraction wInteraction)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		fontRenderer.drawStringWithShadow(letter, letterX.get(), letterY.get(), 0xFFFFFF);
	}
}