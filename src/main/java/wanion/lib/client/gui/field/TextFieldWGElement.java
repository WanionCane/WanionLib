package wanion.lib.client.gui.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.client.gui.WGContainer;
import wanion.lib.client.gui.interaction.WGInteraction;
import wanion.lib.client.gui.interaction.WGKeyInteraction;
import wanion.lib.client.gui.interaction.WGMouseInteraction;
import wanion.lib.common.field.text.TextField;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public class TextFieldWGElement extends WGField<TextField>
{
	/** Has the current text being edited on the textbox. */
	private final TextField textField;
	private int maxStringLength = 32;
	private int cursorCounter;
	private boolean enableBackgroundDrawing = true;
	/** if true the textbox can lose focus by clicking elsewhere on the screen */
	private boolean canLoseFocus = true;
	/** If this value is true along with isEnabled, keyTyped will process the keys. */
	private boolean isFocused;
	/** The current character index that should be used as start of the rendered text. */
	private int lineScrollOffset;
	private int cursorPosition;
	/** other selection position, maybe the same as the cursor */
	private int selectionEnd;
	private int enabledColor = 14737632;
	//private int disabledColor = 7368816;
	/** Called to check if the text is valid */
	private Predicate<String> validator = s -> true;

	public TextFieldWGElement(@Nonnull final TextField textField, @Nonnull final WGContainer<?> wgContainer, final int x, final int y, final int width, final int height)
	{
		super(textField, wgContainer, x, y, width, height);
		this.textField = textField;
	}

	/**
	 * Sets the text of the textbox, and moves the cursor to the end.
	 */
	public void setText(String textIn)
	{
		if (this.validator.test(textIn))
		{
			if (textIn.length() > this.maxStringLength)
			{
				this.textField.setContent(textIn.substring(0, this.maxStringLength));;
			}
			else
			{
				this.textField.setContent(textIn);
			}

			this.setCursorPositionEnd();
		}
	}

	/**
	 * returns the text between the cursor and selectionEnd
	 */
	public String getSelectedText()
	{
		return textField.getContent().substring(Math.min(this.cursorPosition, this.selectionEnd), Math.max(this.cursorPosition, this.selectionEnd));
	}

	/**
	 * Adds the given text after the cursor, or replaces the currently selected text if there is a selection.
	 */
	public void writeText(String textToWrite)
	{
		String s = "";
		String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
		int i = Math.min(this.cursorPosition, this.selectionEnd);
		int j = Math.max(this.cursorPosition, this.selectionEnd);
		int k = this.maxStringLength - textField.getContent().length() - (i - j);

		if (!this.textField.isEmpty())
		{
			s = s + textField.getContent().substring(0, i);
		}

		int l;

		if (k < s1.length())
		{
			s = s + s1.substring(0, k);
			l = k;
		}
		else
		{
			s = s + s1;
			l = s1.length();
		}

		if (!this.textField.isEmpty() && j < textField.getContent().length())
		{
			s = s + textField.getContent().substring(j);
		}

		if (this.validator.test(s))
		{
			textField.setContent(s);
			this.moveCursorBy(i - this.selectionEnd + l);
		}
	}

	/**
	 * Deletes the given number of words from the current cursor's position, unless there is currently a selection, in
	 * which case the selection is deleted instead.
	 */
	public void deleteWords(int num)
	{
		if (!this.textField.isEmpty())
		{
			if (this.selectionEnd != this.cursorPosition)
			{
				this.writeText("");
			}
			else
			{
				this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
			}
		}
	}

	/**
	 * Deletes the given number of characters from the current cursor's position, unless there is currently a selection,
	 * in which case the selection is deleted instead.
	 */
	public void deleteFromCursor(int num)
	{
		if (!this.textField.isEmpty())
		{
			if (this.selectionEnd != this.cursorPosition)
			{
				this.writeText("");
			}
			else
			{
				boolean flag = num < 0;
				int i = flag ? this.cursorPosition + num : this.cursorPosition;
				int j = flag ? this.cursorPosition : this.cursorPosition + num;
				String s = "";

				if (i >= 0)
				{
					s = textField.getContent().substring(0, i);
				}

				if (j < textField.getContent().length())
				{
					s = s + textField.getContent().substring(j);
				}

				if (this.validator.test(s))
				{
					textField.setContent(s);

					if (flag)
					{
						this.moveCursorBy(num);
					}
				}
			}
		}
	}

	/**
	 * Gets the starting index of the word at the specified number of words away from the cursor position.
	 */
	public int getNthWordFromCursor(int numWords)
	{
		return this.getNthWordFromPos(numWords, this.getCursorPosition());
	}

	/**
	 * Gets the starting index of the word at a distance of the specified number of words away from the given position.
	 */
	public int getNthWordFromPos(int n, int pos)
	{
		return this.getNthWordFromPosWS(n, pos, true);
	}

	/**
	 * Like getNthWordFromPos (which wraps this), but adds option for skipping consecutive spaces
	 */
	public int getNthWordFromPosWS(int n, int pos, boolean skipWs)
	{
		int i = pos;
		boolean flag = n < 0;
		int j = Math.abs(n);

		for (int k = 0; k < j; ++k)
		{
			if (!flag)
			{
				int l = textField.getContent().length();
				i = textField.getContent().indexOf(32, i);

				if (i == -1)
				{
					i = l;
				}
				else
				{
					while (skipWs && i < l && textField.getContent().charAt(i) == ' ')
					{
						++i;
					}
				}
			}
			else
			{
				while (skipWs && i > 0 && textField.getContent().charAt(i - 1) == ' ')
				{
					--i;
				}

				while (i > 0 && textField.getContent().charAt(i - 1) != ' ')
				{
					--i;
				}
			}
		}

		return i;
	}

	/**
	 * Moves the text cursor by a specified number of characters and clears the selection
	 */
	public void moveCursorBy(int num)
	{
		this.setCursorPosition(this.selectionEnd + num);
	}

	/**
	 * Sets the current position of the cursor.
	 */
	public void setCursorPosition(int pos)
	{
		this.cursorPosition = pos;
		int i = textField.getContent().length();
		this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
		this.setSelectionPos(this.cursorPosition);
	}

	/**
	 * Moves the cursor to the very start of this text box.
	 */
	public void setCursorPositionZero()
	{
		this.setCursorPosition(0);
	}

	/**
	 * Moves the cursor to the very end of this text box.
	 */
	public void setCursorPositionEnd()
	{
		this.setCursorPosition(textField.getContent().length());
	}

	/**
	 * Call this method from your GuiScreen to process the keys into the textbox
	 */
	public void interaction(@Nonnull final WGKeyInteraction wgKeyInteraction)
	{
		if (GuiScreen.isKeyComboCtrlA(wgKeyInteraction.getKeyCode()))
		{
			this.setCursorPositionEnd();
			this.setSelectionPos(0);
		}
		else if (GuiScreen.isKeyComboCtrlC(wgKeyInteraction.getKeyCode()))
		{
			GuiScreen.setClipboardString(this.getSelectedText());
		}
		else if (GuiScreen.isKeyComboCtrlV(wgKeyInteraction.getKeyCode()))
		{
			this.writeText(GuiScreen.getClipboardString());
		}
		else if (GuiScreen.isKeyComboCtrlX(wgKeyInteraction.getKeyCode()))
		{
			GuiScreen.setClipboardString(this.getSelectedText());
				this.writeText("");
		}
		else
		{
			switch (wgKeyInteraction.getKeyCode())
			{
				case 14:
					if (GuiScreen.isCtrlKeyDown())
						this.deleteWords(-1);
					else
						this.deleteFromCursor(-1);
					return;
				case 199:

					if (GuiScreen.isShiftKeyDown())
					{
						this.setSelectionPos(0);
					}
					else
					{
						this.setCursorPositionZero();
					}
					return;
				case 203:

					if (GuiScreen.isShiftKeyDown())
					{
						if (GuiScreen.isCtrlKeyDown())
						{
							this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
						}
						else
						{
							this.setSelectionPos(this.getSelectionEnd() - 1);
						}
					}
					else if (GuiScreen.isCtrlKeyDown())
					{
						this.setCursorPosition(this.getNthWordFromCursor(-1));
					}
					else
					{
						this.moveCursorBy(-1);
					}

					return;
				case 205:

					if (GuiScreen.isShiftKeyDown())
					{
						if (GuiScreen.isCtrlKeyDown())
						{
							this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
						}
						else
						{
							this.setSelectionPos(this.getSelectionEnd() + 1);
						}
					}
					else if (GuiScreen.isCtrlKeyDown())
					{
						this.setCursorPosition(this.getNthWordFromCursor(1));
					}
					else
					{
						this.moveCursorBy(1);
					}

					return;
				case 207:
					if (GuiScreen.isShiftKeyDown())
						this.setSelectionPos(textField.getContent().length());
					else
						this.setCursorPositionEnd();

					return;
				case 211:

					if (GuiScreen.isCtrlKeyDown())
						this.deleteWords(1);
					else
						this.deleteFromCursor(1);
					return;
				default:

					if (ChatAllowedCharacters.isAllowedCharacter(wgKeyInteraction.getKey()))
						this.writeText(Character.toString(wgKeyInteraction.getKey()));
			}
		}
	}

	/**
	 * Called when mouse is clicked, regardless as to whether it is over this button or not.
	 */
	public void interact(@Nonnull WGMouseInteraction wgMouseInteraction)
	{
		boolean flag = wgMouseInteraction.getMouseX() >= this.x && wgMouseInteraction.getMouseX() < this.x + this.width && wgMouseInteraction.getMouseY() >= this.y && wgMouseInteraction.getMouseY() < this.y + this.height;

		if (this.canLoseFocus)
		{
			this.setFocused(flag);
		}

		if (this.isFocused && flag && wgMouseInteraction.getMouseButton() == 0)
		{
			int i = wgMouseInteraction.getMouseX() - this.x;

			if (this.enableBackgroundDrawing)
			{
				i -= 4;
			}

			String s = getFontRenderer().trimStringToWidth(textField.getContent().substring(this.lineScrollOffset), this.getWidth());
			this.setCursorPosition(getFontRenderer().trimStringToWidth(s, i).length() + this.lineScrollOffset);
		}
	}

	/**
	 * Draws the textbox
	 */
	@Override
	public void draw(@Nonnull final WGInteraction wgInteraction)
	{
		if (this.getEnableBackgroundDrawing()) {
			Gui.drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
			Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
		}

		int i = this.enabledColor; // : this.disabledColor;
		int j = this.cursorPosition - this.lineScrollOffset;
		int k = this.selectionEnd - this.lineScrollOffset;
		String s = getFontRenderer().trimStringToWidth(textField.getContent().substring(this.lineScrollOffset), this.getWidth());
		boolean flag = j >= 0 && j <= s.length();
		boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
		int l = this.enableBackgroundDrawing ? this.x + 4 : this.x;
		int i1 = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
		int j1 = l;

		if (k > s.length()) {
			k = s.length();
		}

		if (!s.isEmpty()) {
			String s1 = flag ? s.substring(0, j) : s;
			j1 = getFontRenderer().drawStringWithShadow(s1, (float) l, (float) i1, i);
		}

		boolean flag2 = this.cursorPosition < textField.getContent().length() || textField.getContent().length() >= this.getMaxStringLength();
		int k1 = j1;

		if (!flag) {
			k1 = j > 0 ? l + this.width : l;
		} else if (flag2) {
			k1 = j1 - 1;
			--j1;
		}

		if (!s.isEmpty() && flag && j < s.length()) {
			j1 = getFontRenderer().drawStringWithShadow(s.substring(j), (float) j1, (float) i1, i);
		}

		if (flag1) {
			if (flag2) {
				Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + getFontRenderer().FONT_HEIGHT, -3092272);
			} else {
				getFontRenderer().drawStringWithShadow("_", (float) k1, (float) i1, i);
			}
		}

		if (k != j) {
			int l1 = l + getFontRenderer().getStringWidth(s.substring(0, k));
			this.drawSelectionBox(k1, i1 - 1, l1 - 1, i1 + 1 + getFontRenderer().FONT_HEIGHT);
		}
	}

	/**
	 * Draws the blue selection box.
	 */
	private void drawSelectionBox(int startX, int startY, int endX, int endY)
	{
		if (startX < endX)
		{
			int i = startX;
			startX = endX;
			endX = i;
		}

		if (startY < endY)
		{
			int j = startY;
			startY = endY;
			endY = j;
		}

		if (endX > this.x + this.width)
		{
			endX = this.x + this.width;
		}

		if (startX > this.x + this.width)
		{
			startX = this.x + this.width;
		}

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double)startX, (double)endY, 0.0D).endVertex();
		bufferbuilder.pos((double)endX, (double)endY, 0.0D).endVertex();
		bufferbuilder.pos((double)endX, (double)startY, 0.0D).endVertex();
		bufferbuilder.pos((double)startX, (double)startY, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
	}

	/**
	 * Sets the maximum length for the text in this text box. If the current text is longer than this length, the
	 * current text will be trimmed.
	 */
	public void setMaxStringLength(int length)
	{
		this.maxStringLength = length;
		if (textField.getContent().length() > length)
			textField.setContent(textField.getContent().substring(0, length));
	}

	/**
	 * returns the maximum number of character that can be contained in this textbox
	 */
	public int getMaxStringLength()
	{
		return this.maxStringLength;
	}

	/**
	 * returns the current position of the cursor
	 */
	public int getCursorPosition()
	{
		return this.cursorPosition;
	}

	/**
	 * Gets whether the background and outline of this text box should be drawn (true if so).
	 */
	public boolean getEnableBackgroundDrawing()
	{
		return this.enableBackgroundDrawing;
	}

	/**
	 * Sets whether or not the background and outline of this text box should be drawn.
	 */
	public void setEnableBackgroundDrawing(boolean enableBackgroundDrawingIn)
	{
		this.enableBackgroundDrawing = enableBackgroundDrawingIn;
	}

	/**
	 * Sets the color to use when drawing this text box's text. A different color is used if this text box is disabled.
	 */
	public void setTextColor(int color)
	{
		this.enabledColor = color;
	}

	/**
	 * Sets focus to this gui element
	 */
	public void setFocused(boolean isFocusedIn)
	{
		if (isFocusedIn && !this.isFocused)
		{
			this.cursorCounter = 0;
		}

		this.isFocused = isFocusedIn;

		if (Minecraft.getMinecraft().currentScreen != null)
		{
			Minecraft.getMinecraft().currentScreen.setFocused(isFocusedIn);
		}
	}

	/**
	 * Getter for the focused field
	 */
	public boolean isFocused()
	{
		return this.isFocused;
	}

	/**
	 * the side of the selection that is not the cursor, may be the same as the cursor
	 */
	public int getSelectionEnd()
	{
		return this.selectionEnd;
	}
	/**
	 * Sets the position of the selection anchor (the selection anchor and the cursor position mark the edges of the
	 * selection). If the anchor is set beyond the bounds of the current text, it will be put back inside.
	 */
	public void setSelectionPos(int position)
	{
		int i = textField.getContent().length();

		if (position > i)
		{
			position = i;
		}

		if (position < 0)
		{
			position = 0;
		}

		this.selectionEnd = position;

		if (getFontRenderer() != null)
		{
			if (this.lineScrollOffset > i)
			{
				this.lineScrollOffset = i;
			}

			int j = this.getWidth();
			String s = getFontRenderer().trimStringToWidth(textField.getContent().substring(this.lineScrollOffset), j);
			int k = s.length() + this.lineScrollOffset;

			if (position == this.lineScrollOffset)
			{
				this.lineScrollOffset -= getFontRenderer().trimStringToWidth(textField.getContent(), j, true).length();
			}

			if (position > k)
			{
				this.lineScrollOffset += position - k;
			}
			else if (position <= this.lineScrollOffset)
			{
				this.lineScrollOffset -= this.lineScrollOffset - position;
			}

			this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
		}
	}

	/**
	 * Sets whether this text box loses focus when something other than it is clicked.
	 */
	public void setCanLoseFocus(boolean canLoseFocusIn)
	{
		this.canLoseFocus = canLoseFocusIn;
	}
}