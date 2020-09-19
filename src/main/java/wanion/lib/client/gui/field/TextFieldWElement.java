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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.client.gui.WGuiContainer;
import wanion.lib.client.gui.interaction.WGInteraction;
import wanion.lib.client.gui.interaction.WGKeyInteraction;
import wanion.lib.client.gui.interaction.WGMouseInteraction;
import wanion.lib.common.INBTMessage;
import wanion.lib.common.IUpdatable;
import wanion.lib.common.field.text.TextField;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

// My Implementation of the Vanilla GuiTextBox, with several adjustments to fit my needs.
@SideOnly(Side.CLIENT)
public class TextFieldWElement extends WField<TextField> implements INBTMessage, IUpdatable
{
	/** Has the current text being edited on the textbox. */
	private final TextField textField;
	private final int maxStringLength = 32;
	private int cursorCounter;
	private boolean enableBackgroundDrawing = true;
	/** if true the textbox can lose focus by clicking elsewhere on the screen */
	private boolean canLoseFocus = true;
	/** If this value is true along with isEnabled, keyTyped will process the keys. */
	private boolean isFocused;
	/** If this value is true along with isFocused, keyTyped will process the keys. */
	private boolean typingEnabled = true;
	/** The current character index that should be used as start of the rendered text. */
	private int lineScrollOffset;
	private int cursorPosition;
	/** other selection position, maybe the same as the cursor */
	private int selectionEnd;
	/** Called to check if the text is valid */
	private final Predicate<String> validator = s -> true;
	/** AFK Things */
	protected final double secondsToAFK;
	protected double previousSeconds;
	protected double counter;

	public TextFieldWElement(@Nonnull final TextField textField, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final int width, final int height)
	{
		super(textField, wGuiContainer, x, y, width, height);
		this.textField = textField;
		this.secondsToAFK = 10;
		this.previousSeconds = System.nanoTime() / (double) 1000000000;
	}

	@Override
	public boolean canInteractWith(@Nonnull final WGInteraction wgInteraction)
	{
		return textField.canInteractWith(wgInteraction.getEntityPlayer());
	}

	public void setTextFieldText(@Nonnull final String content)
	{
		typingEnabled = false;
		textField.sendTextFieldNBT(getEntityPlayer(), true, content);
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
			s = s + textField.getContent().substring(0, i);
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
			s = s + textField.getContent().substring(j);
		if (this.validator.test(s))
		{
			setTextFieldText(s);
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
				this.writeText("");
			else
				this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
		}
	}

	/**
	 * Deletes the given number of characters from the current cursor's position, unless there is currently a selection,
	 * in which case the selection is deleted instead.
	 */
	public void deleteFromCursor(int num)
	{
		if (this.textField.isEmpty())
			return;
		if (this.selectionEnd != this.cursorPosition)
			this.writeText("");
		else {
			boolean flag = num < 0;
			int i = flag ? this.cursorPosition + num : this.cursorPosition;
			int j = flag ? this.cursorPosition : this.cursorPosition + num;
			String s = "";
			if (i >= 0)
				s = textField.getContent().substring(0, i);
			if (j < textField.getContent().length())
				s = s + textField.getContent().substring(j);
			if (this.validator.test(s)) {
				setTextFieldText(s);
				if (flag)
					this.moveCursorBy(num);
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
		int j = Math.abs(n);
		for (int k = 0; k < j; ++k)
		{
			if (!(n < 0))
			{
				int l = textField.getContent().length();
				i = textField.getContent().indexOf(32, i);

				if (i == -1)
					i = l;
				else while (skipWs && i < l && textField.getContent().charAt(i) == ' ')
						++i;
			}
			else
			{
				while (skipWs && i > 0 && textField.getContent().charAt(i - 1) == ' ')
					--i;
				while (i > 0 && textField.getContent().charAt(i - 1) != ' ')
					--i;
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
	@Override
	public void interaction(@Nonnull final WGKeyInteraction wgKeyInteraction)
	{
		final char typedChar = wgKeyInteraction.getKey();
		final int keyCode = wgKeyInteraction.getKeyCode();
		if (GuiScreen.isKeyComboCtrlA(wgKeyInteraction.getKeyCode())) {
			this.setCursorPositionEnd();
			this.setSelectionPos(0);
		} else if (GuiScreen.isKeyComboCtrlA(keyCode)) {
			this.setCursorPositionEnd();
			this.setSelectionPos(0);
			wgKeyInteraction.notProceed();
		} else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
			GuiScreen.setClipboardString(this.getSelectedText());
			wgKeyInteraction.notProceed();
		} else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
			if (this.typingEnabled)
				this.writeText(GuiScreen.getClipboardString());
			wgKeyInteraction.notProceed();
		} else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
			GuiScreen.setClipboardString(this.getSelectedText());
			if (this.typingEnabled)
				this.writeText("");
			wgKeyInteraction.notProceed();
		} else {
			switch (keyCode) {
				case 14:
					if (GuiScreen.isCtrlKeyDown()) {
						if (this.typingEnabled)
							this.deleteWords(-1);
					} else if (this.typingEnabled)
						this.deleteFromCursor(-1);
					wgKeyInteraction.notProceed();
				case 199:

					if (GuiScreen.isShiftKeyDown())
						this.setSelectionPos(0);
					else
						this.setCursorPositionZero();
					wgKeyInteraction.notProceed();
				case 203:

					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown())
							this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
						else
							this.setSelectionPos(this.getSelectionEnd() - 1);
					} else if (GuiScreen.isCtrlKeyDown())
						this.setCursorPosition(this.getNthWordFromCursor(-1));
					else
						this.moveCursorBy(-1);
					wgKeyInteraction.notProceed();
				case 205:
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown())
							this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
						else
							this.setSelectionPos(this.getSelectionEnd() + 1);
					} else if (GuiScreen.isCtrlKeyDown())
						this.setCursorPosition(this.getNthWordFromCursor(1));
					else
						this.moveCursorBy(1);
					wgKeyInteraction.notProceed();
				case 207:
					if (GuiScreen.isShiftKeyDown())
						this.setSelectionPos(textField.length());
					else
						this.setCursorPositionEnd();
					wgKeyInteraction.notProceed();
				case 211:
					if (GuiScreen.isCtrlKeyDown()) {
						if (this.typingEnabled)
							this.deleteWords(1);
					} else if (this.typingEnabled)
						this.deleteFromCursor(1);
					wgKeyInteraction.notProceed();
				default:
					if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
						if (this.typingEnabled)
							this.writeText(Character.toString(typedChar));
						wgKeyInteraction.notProceed();
					}
			}
		}
	}

	/**
	 * Called when mouse is clicked, regardless as to whether it is over this button or not.
	 */
	@Override
	public void interaction(@Nonnull final WGMouseInteraction mouseInteraction)
	{
		final boolean hovering = mouseInteraction.isHovering(this);
		if (this.canLoseFocus)
			this.setFocused(mouseInteraction, hovering);
		if (this.isFocused && hovering && mouseInteraction.getMouseButton() == 0)
		{
			int i = mouseInteraction.getMouseX() - this.x;
			if (this.enableBackgroundDrawing)
				i -= 4;
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
		int i = this.typingEnabled || canInteractWith(wgInteraction) ? 14737632 : 7368816;
		int j = this.cursorPosition - this.lineScrollOffset;
		int k = this.selectionEnd - this.lineScrollOffset;
		String s = getFontRenderer().trimStringToWidth(textField.getContent().substring(this.lineScrollOffset), this.getWidth());
		boolean flag = j >= 0 && j <= s.length();
		boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
		int l = this.enableBackgroundDrawing ? this.x + 4 : this.x;
		int i1 = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
		int j1 = l;
		if (k > s.length())
			k = s.length();
		if (!s.isEmpty()) {
			String s1 = flag ? s.substring(0, j) : s;
			j1 = getFontRenderer().drawStringWithShadow(s1, (float) l, (float) i1, i);
		}
		boolean flag2 = this.cursorPosition < textField.getContent().length() || textField.getContent().length() >= this.getMaxStringLength();
		int k1 = j1;
		if (!flag)
			k1 = j > 0 ? l + this.width : l;
		else if (flag2) {
			k1 = j1 - 1;
			--j1;
		}
		if (flag1) {
			if (flag2)
				Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + getFontRenderer().FONT_HEIGHT, -3092272);
			else
				getFontRenderer().drawStringWithShadow("_", (float) k1, (float) i1, i);
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
			endX = this.x + this.width;
		if (startX > this.x + this.width)
			startX = this.x + this.width;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos(startX, endY, 0.0D).endVertex();
		bufferbuilder.pos(endX, endY, 0.0D).endVertex();
		bufferbuilder.pos(endX, startY, 0.0D).endVertex();
		bufferbuilder.pos(startX, startY, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
	}

	@Override
	public void drawForegroundLayer(@Nonnull final WGInteraction interaction)
	{
		if (!interaction.isHovering(this) || canInteractWith(interaction))
			return;
		wGuiContainer.drawHoveringText(textField.getHoveringText(interaction.getEntityPlayer()), getTooltipX(interaction), getTooltipY(interaction));
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
	 * Sets focus to this gui element
	 */
	public void setFocused(@Nonnull final WGMouseInteraction mouseInteraction, final boolean isFocusedIn)
	{
		if (!canInteractWith(mouseInteraction))
			return;
		if (isFocusedIn && !this.isFocused)
			this.cursorCounter = 0;
		textField.sendTextFieldNBT(mouseInteraction.getEntityPlayer(), (this.isFocused = isFocusedIn));
		if (Minecraft.getMinecraft().currentScreen != null)
			Minecraft.getMinecraft().currentScreen.setFocused(isFocusedIn);
	}

	/**
	 * Getter for the focused field
	 */
	public boolean isFocused()
	{
		return this.isFocused;
	}

	/**
	 * Sets whether this text box loses focus when something other than it is clicked.
	 */
	public void setCanLoseFocus(boolean canLoseFocusIn)
	{
		this.canLoseFocus = canLoseFocusIn;
	}

	/**
	 * Sets whether this text box is enabled. Disabled text boxes cannot be typed in.
	 */
	public void setTypingEnabled(boolean enabled)
	{
		this.typingEnabled = enabled;
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
			position = i;
		if (position < 0)
			position = 0;
		this.selectionEnd = position;
		if (getFontRenderer() != null)
		{
			if (this.lineScrollOffset > i)
				this.lineScrollOffset = i;
			int j = this.getWidth();
			String s = getFontRenderer().trimStringToWidth(textField.getContent().substring(this.lineScrollOffset), j);
			int k = s.length() + this.lineScrollOffset;
			if (position == this.lineScrollOffset)
				this.lineScrollOffset -= getFontRenderer().trimStringToWidth(textField.getContent(), j, true).length();
			if (position > k)
				this.lineScrollOffset += position - k;
			else if (position <= this.lineScrollOffset)
				this.lineScrollOffset -= this.lineScrollOffset - position;
			this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
		}
	}

	@Override
	public void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		if (nbtTagCompound.getString("fieldName").equals(textField.getFieldName()))
			typingEnabled = true;
	}

	@Override
	public void update(double seconds)
	{
		if (counter > 0) {
			counter -= seconds - previousSeconds;
			if (counter <= 0)
				setFocused(new WGMouseInteraction(getWGuiContainer()), false);
		}
		previousSeconds = seconds;
	}
}