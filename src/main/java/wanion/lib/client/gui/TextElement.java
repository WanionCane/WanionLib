package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.client.gui.interaction.WInteraction;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public class TextElement extends WElement<TextElement>
{
	public final static int DEFAULT_COLOR = 0x404040;
	public final static Supplier<Integer> DEFAULT_COLOR_SUPPLIER = () -> DEFAULT_COLOR;
	private final Supplier<String> textSupplier;
	private final FontRenderer fontRenderer;
	private final TextAnchor textAnchor;
	private Supplier<Integer> colorSupplier;

	public TextElement(@Nonnull final Supplier<String> textSupplier, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		this(textSupplier, wGuiContainer, x, y, TextAnchor.LEFT);
	}

	public TextElement(@Nonnull final Supplier<String> textSupplier, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, @Nonnull final TextAnchor textAnchor)
	{
		super(wGuiContainer, x, y);
		this.textSupplier = textSupplier;
		this.fontRenderer = getFontRenderer();
		this.textAnchor = textAnchor;
		this.colorSupplier = DEFAULT_COLOR_SUPPLIER;
		setForegroundCheck((interaction) -> false);
	}

	public final int getColor()
	{
		return colorSupplier.get();
	}

	@Nonnull
	public TextElement setColorSupplier(@Nonnull final Supplier<Integer> colorSupplier)
	{
		this.colorSupplier = colorSupplier;
		return this;
	}

	@Nonnull
	public TextElement setDefaultColorSupplier()
	{
		this.colorSupplier = DEFAULT_COLOR_SUPPLIER;
		return this;
	}

	@Override
	public int getX()
	{
		return super.getX() + textAnchor.getX(this, textSupplier.get());
	}

	public int getWidth()
	{
		return fontRenderer.getStringWidth(textSupplier.get());
	}

	@Override
	public int getHeight()
	{
		return fontRenderer.FONT_HEIGHT;
	}

	@Override
	public void draw(@Nonnull final WInteraction wInteraction)
	{
		fontRenderer.drawString(textSupplier.get(), getUsableX(), getUsableY(), getColor());
	}

	public enum TextAnchor
	{
		LEFT,
		MIDDLE,
		RIGHT;

		public int getX(@Nonnull final TextElement textElement, @Nonnull final String text)
		{
			switch (textElement.textAnchor)
			{
				case MIDDLE:
					return -(textElement.fontRenderer.getStringWidth(text) / 2);
				case RIGHT:
					return -textElement.fontRenderer.getStringWidth(text);
				default:
					return 0;
			}
		}
	}
}