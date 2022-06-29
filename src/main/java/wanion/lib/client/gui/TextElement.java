package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
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
	private boolean drawShadow;
	private TextAnchor textAnchor;
	private Supplier<Integer> colorSupplier;

	public TextElement(@Nonnull final String text, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		this(() -> text, wGuiContainer, x, y, false);
	}

	public TextElement(@Nonnull final String text, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final boolean drawShadow)
	{
		this(() -> text, wGuiContainer, x, y, drawShadow);
	}

	public TextElement(@Nonnull final Supplier<String> textSupplier, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		this(textSupplier, wGuiContainer, x, y, false);
	}

	public TextElement(@Nonnull final Supplier<String> textSupplier, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final boolean drawShadow)
	{
		super(wGuiContainer, x, y);
		this.textSupplier = textSupplier;
		this.fontRenderer = getFontRenderer();
		this.drawShadow = drawShadow;
		this.textAnchor = TextAnchor.LEFT;
		this.colorSupplier = DEFAULT_COLOR_SUPPLIER;
		setForegroundCheck((interaction) -> false);
	}

	public final boolean getDrawShadow()
	{
		return drawShadow;
	}

	@Nonnull
	public final TextElement setDrawShadow(final boolean drawShadow)
	{
		this.drawShadow = drawShadow;
		return this;
	}

	@Nonnull
	public final TextAnchor getTextAnchor()
	{
		return textAnchor;
	}

	@Nonnull
	public final TextElement setTextAnchor(@Nonnull final TextAnchor textAnchor)
	{
		this.textAnchor = textAnchor;
		return this;
	}

	@Nonnull
	public final TextElement setColor(final int color)
	{
		this.colorSupplier = () -> color;
		return this;
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
	public final TextElement setDefaultColorSupplier()
	{
		this.colorSupplier = DEFAULT_COLOR_SUPPLIER;
		return this;
	}

	@Nonnull
	public String getText()
	{
		return textSupplier.get();
	}

	@Override
	public int getX()
	{
		return textAnchor.getX(this);
	}

	public int getWidth()
	{
		return fontRenderer.getStringWidth(getText());
	}

	@Override
	public int getHeight()
	{
		return fontRenderer.FONT_HEIGHT;
	}

	@Override
	public void draw(@Nonnull final WInteraction wInteraction)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		fontRenderer.drawString(getText(), getUsableX(), getUsableY(), getColor(), drawShadow);
	}

	public enum TextAnchor
	{
		LEFT(textElement -> textElement.x),
		MIDDLE(textElement -> textElement.x - (textElement.fontRenderer.getStringWidth(textElement.getText()) / 2)),
		RIGHT(textElement -> textElement.x - textElement.fontRenderer.getStringWidth(textElement.getText()));

		private final ITextAnchor iTextAnchor;

		TextAnchor(@Nonnull final ITextAnchor iTextAnchor)
		{
			this.iTextAnchor = iTextAnchor;
		}

		public int getX(@Nonnull final TextElement textElement)
		{
			return iTextAnchor.getX(textElement);
		}

		private interface ITextAnchor
		{
			int getX(@Nonnull final TextElement textElement);
		}
	}
}