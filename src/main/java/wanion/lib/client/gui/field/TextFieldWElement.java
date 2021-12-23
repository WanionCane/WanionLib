package wanion.lib.client.gui.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.client.gui.WGuiContainer;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.client.gui.interaction.WKeyInteraction;
import wanion.lib.client.gui.interaction.WMouseInteraction;
import wanion.lib.common.INBTMessage;
import wanion.lib.common.IUpdatable;
import wanion.lib.common.field.text.TextField;

import javax.annotation.Nonnull;

// My Implementation of the Vanilla GuiTextField, with several adjustments to fit my needs.
@SideOnly(Side.CLIENT)
public class TextFieldWElement extends WField<TextField> implements INBTMessage, IUpdatable
{
    private int cursorCounter;
    private final boolean enableBackgroundDrawing;
    /** If this value is true along with isEnabled, keyTyped will process the keys. */
    private boolean isFocused;
    /** If this value is true along with isFocused, keyTyped will process the keys. */
    private boolean typingEnabled = true;
    /** The current character index that should be used as start of the rendered text. */
    private int lineScrollOffset;
    private int cursorPosition;
    /** other selection position, maybe the same as the cursor */
    private int selectionEnd;

    public TextFieldWElement(@Nonnull final TextField textField, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final int width, final int height)
    {
        this(textField, wGuiContainer, x, y, width, height, true);
        setInteractionCheck((interaction) -> (isFocused || interaction.isHovering(this)) && field.canInteractWith(interaction.getEntityPlayer()));
    }

    public TextFieldWElement(@Nonnull final TextField textField, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final int width, final int height, final boolean enableBackgroundDrawing)
    {
        super(textField, wGuiContainer, x, y, width, height);
        this.enableBackgroundDrawing = enableBackgroundDrawing;
        setForegroundCheck(interaction -> interaction.isHovering(this) && !canInteractWith(interaction));
        setTooltipSupplier((interaction, stackSupplier) -> Lists.newArrayList(field.getHoveringText(interaction)));
    }

    /**
     * Sets the text of the textbox, and moves the cursor to the end.
     */
    public void setText(String textIn)
    {
        setTextField(textIn.length() > 32 ? textIn.substring(0, 32) : textIn);
        this.setCursorPositionEnd();
    }

    private void setTextField(String text)
    {
        field.setContent(text);
        typingEnabled = false;
        field.sendTextFieldNBT(getWindowID(), getEntityPlayer(), true, text);
    }

    /**
     * Returns the contents of the textbox
     */
    public String getText()
    {
        return field.getContent();
    }

    /**
     * returns the text between the cursor and selectionEnd
     */
    public String getSelectedText()
    {
        return getText().substring(Math.min(this.cursorPosition, this.selectionEnd), Math.max(this.cursorPosition, this.selectionEnd));
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
        int k = 32 - this.field.length() - (i - j);
        if (!this.field.isEmpty())
            s = s + getText().substring(0, i);
        int l;
        if (k < s1.length()) {
            s = s + s1.substring(0, k);
            l = k;
        } else {
            s = s + s1;
            l = s1.length();
        }
        if (!this.field.isEmpty() && j < this.field.length())
            s = s + getText().substring(j);
        setTextField(s);
        this.moveCursorBy(i - this.selectionEnd + l);
    }

    /**
     * Deletes the given number of words from the current cursor's position, unless there is currently a selection, in
     * which case the selection is deleted instead.
     */
    public void deleteWords(int num)
    {
        if (!this.field.isEmpty())
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
        if (!field.isEmpty())
        {
            if (this.selectionEnd != this.cursorPosition)
                this.writeText("");
            else {
                boolean flag = num < 0;
                int i = flag ? this.cursorPosition + num : this.cursorPosition;
                int j = flag ? this.cursorPosition : this.cursorPosition + num;
                String s = "";

                if (i >= 0)
                    s = getText().substring(0, i);
                if (j < field.length())
                    s = s + getText().substring(j);
                setTextField(s);
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
        boolean flag = n < 0;
        int j = Math.abs(n);
        for (int k = 0; k < j; ++k)
        {
            if (!flag)
            {
                int l = this.field.length();
                i = getText().indexOf(32, i);

                if (i == -1)
                    i = l;
                else
                    while (skipWs && i < l && getText().charAt(i) == ' ')
                        ++i;
            }
            else
            {
                while (skipWs && i > 0 && getText().charAt(i - 1) == ' ')
                    --i;
                while (i > 0 && getText().charAt(i - 1) != ' ')
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
        this.setSelectionPos((this.cursorPosition = MathHelper.clamp(pos, 0, field.length())));
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
        this.setCursorPosition(this.field.length());
    }

    /**
     * Call this method from your GuiScreen to process the keys into the textbox
     */
    public void interaction(@Nonnull final WKeyInteraction keyInteraction)
    {
        char typedChar = keyInteraction.getKey();
        int keyCode = keyInteraction.getKeyCode();
        if (!this.isFocused)
            return;
        keyInteraction.notProceed();
        if (GuiScreen.isKeyComboCtrlA(keyCode))
        {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
        }
        else if (GuiScreen.isKeyComboCtrlC(keyCode))
        {
            GuiScreen.setClipboardString(this.getSelectedText());
        }
        else if (GuiScreen.isKeyComboCtrlV(keyCode))
        {
            if (this.typingEnabled)
                this.writeText(GuiScreen.getClipboardString());
        }
        else if (GuiScreen.isKeyComboCtrlX(keyCode))
        {
            GuiScreen.setClipboardString(this.getSelectedText());
            if (this.typingEnabled)
                this.writeText("");
        }
        else
        {
            switch (keyCode)
            {
                case 14:
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (this.typingEnabled)
                            this.deleteWords(-1);
                    } else if (this.typingEnabled)
                        this.deleteFromCursor(-1);
                    return;
                case 199:

                    if (GuiScreen.isShiftKeyDown())
                        this.setSelectionPos(0);
                    else
                        this.setCursorPositionZero();
                    return;
                case 203:
                    if (GuiScreen.isShiftKeyDown())
                    {
                        if (GuiScreen.isCtrlKeyDown())
                            this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                        else
                            this.setSelectionPos(this.getSelectionEnd() - 1);
                    }
                    else if (GuiScreen.isCtrlKeyDown())
                        this.setCursorPosition(this.getNthWordFromCursor(-1));
                    else
                        this.moveCursorBy(-1);
                    return;
                case 205:

                    if (GuiScreen.isShiftKeyDown())
                    {
                        if (GuiScreen.isCtrlKeyDown())
                            this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                        else
                            this.setSelectionPos(this.getSelectionEnd() + 1);
                    }
                    else if (GuiScreen.isCtrlKeyDown())
                        this.setCursorPosition(this.getNthWordFromCursor(1));
                    else
                        this.moveCursorBy(1);
                    return;
                case 207:
                    if (GuiScreen.isShiftKeyDown())
                        this.setSelectionPos(this.field.length());
                    else
                        this.setCursorPositionEnd();
                    return;
                case 211:
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (this.typingEnabled)
                            this.deleteWords(1);
                    } else if (this.typingEnabled)
                        this.deleteFromCursor(1);
                    return;
                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                        if (this.typingEnabled)
                            this.writeText(Character.toString(typedChar));
                    } else {
                        keyInteraction.proceed();
                    }
            }
        }
    }

    /**
     * Called when mouse is clicked, regardless as to whether it is over this button or not.
     */
    @Override
    public void interaction(@Nonnull final WMouseInteraction mouseInteraction)
    {
        final boolean hovering = mouseInteraction.isHovering(this);
        this.setFocused(mouseInteraction, hovering);
        if (this.isFocused && hovering && mouseInteraction.getMouseButton() == 0) {
            int i = mouseInteraction.getMouseX() - this.getUsableX();
            if (this.enableBackgroundDrawing)
                i -= 4;
            String s = getFontRenderer().trimStringToWidth(getText().substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(getFontRenderer().trimStringToWidth(s, i).length() + this.lineScrollOffset);
        }
    }

    /**
     * Draws the textbox
     */
    @Override
    public void draw(@Nonnull final WInteraction interaction)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        if (enableBackgroundDrawing) {
            Gui.drawRect(this.getUsableX() - 1, this.getUsableY() - 1, this.getUsableX() + this.width + 1, this.getUsableY() + this.height + 1, -7631989);
            Gui.drawRect(this.getUsableX(), this.getUsableY(), this.getUsableX() + this.width + 1, this.getUsableY() + this.height + 1, -1);
            Gui.drawRect(this.getUsableX() - 1, this.getUsableY() - 1, this.getUsableX() + this.width, this.getUsableY() + this.height, -13158601);
            Gui.drawRect(this.getUsableX(), this.getUsableY(), this.getUsableX() + this.width, this.getUsableY() + this.height, -7631989);
        }
        FontRenderer fontRenderer = getFontRenderer();
        int j = this.cursorPosition - this.lineScrollOffset;
        int k = this.selectionEnd - this.lineScrollOffset;
        String s = fontRenderer.trimStringToWidth(getText().substring(this.lineScrollOffset), this.getWidth());
        boolean flag = j >= 0 && j <= s.length();
        boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
        int l = this.enableBackgroundDrawing ? getUsableX() + 4 : getUsableX();
        int i1 = this.enableBackgroundDrawing ? getUsableY() + (this.height - 8) / 2 : getUsableY();
        int j1 = l;
        if (!s.isEmpty()) {
            String s1 = flag ? s.substring(0, j) : s;
            j1 = fontRenderer.drawStringWithShadow(s1, (float) l, (float) i1, 0xFFFFFF);
        }
        boolean flag2 = this.cursorPosition < field.length() || field.length() >= 32;
        int k1 = j1;
        if (!flag)
            k1 = j > 0 ? l + this.width : l;
        else if (flag2) {
            k1 = j1 - 1;
            --j1;
        }
        if (!s.isEmpty() && flag && j < s.length())
            fontRenderer.drawStringWithShadow(s.substring(j), (float) j1, (float) i1, 0xFFFFFF);
        if (flag1) {
            if (flag2)
                Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
            else
                fontRenderer.drawStringWithShadow("_", (float) k1, (float) i1, 0xFFFFFF);
        }
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
     * Sets focus to this gui element
     */
    public void setFocused(@Nonnull final WMouseInteraction mouseInteraction, final boolean isFocusedIn)
    {
        if (!canInteractWith(mouseInteraction))
            return;
        if (isFocusedIn && !this.isFocused)
            this.cursorCounter = 0;
        field.sendTextFieldNBT(getWindowID(), mouseInteraction.getEntityPlayer(), (typingEnabled = (this.isFocused = isFocusedIn)));
        if (Minecraft.getMinecraft().currentScreen != null)
            Minecraft.getMinecraft().currentScreen.setFocused(isFocusedIn);
    }

    /**
     * the side of the selection that is not the cursor, may be the same as the cursor
     */
    public int getSelectionEnd()
    {
        return this.selectionEnd;
    }

    /**
     * returns the width of the textbox depending on if background drawing is enabled
     */
    @Override
    public int getWidth()
    {
        return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    /**
     * Sets the position of the selection anchor (the selection anchor and the cursor position mark the edges of the
     * selection). If the anchor is set beyond the bounds of the current text, it will be put back inside.
     */
    public void setSelectionPos(int position)
    {
        int i = field.length();
        position = MathHelper.clamp(position, 0, i);
        this.selectionEnd = position;
        final FontRenderer fontRenderer = getFontRenderer();
        if (fontRenderer == null)
            return;
        if (this.lineScrollOffset > i)
            this.lineScrollOffset = i;
        int j = this.getWidth();
        String s = fontRenderer.trimStringToWidth(getText().substring(this.lineScrollOffset), j);
        int k = s.length() + this.lineScrollOffset;
        if (position == this.lineScrollOffset)
            this.lineScrollOffset -= fontRenderer.trimStringToWidth(getText(), j, true).length();
        if (position > k)
            this.lineScrollOffset += position - k;
        else if (position <= this.lineScrollOffset)
            this.lineScrollOffset -= this.lineScrollOffset - position;
        this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
    }

    @Override
    public void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound)
    {
        if (!nbtTagCompound.getString("fieldName").equals(field.getFieldName()))
            return;
        // probably unnecessary, but just in case.
        try {
            Thread.sleep(20);
        } catch (InterruptedException ignored) {}
        typingEnabled = true;
    }

    @Override
    public void update(double seconds)
    {
        final String text = getText();
        this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, text.length());
        this.selectionEnd = MathHelper.clamp(this.selectionEnd, 0, text.length());
    }
}