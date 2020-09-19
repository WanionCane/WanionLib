package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import wanion.lib.client.gui.interaction.WGInteraction;
import wanion.lib.client.gui.interaction.WGKeyInteraction;
import wanion.lib.client.gui.interaction.WGMouseInteraction;
import wanion.lib.common.*;
import wanion.lib.common.control.ControlController;
import wanion.lib.common.control.IControl;
import wanion.lib.common.field.FieldController;
import wanion.lib.common.field.IField;
import wanion.lib.common.matching.Matching;
import wanion.lib.common.matching.MatchingController;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public abstract class WGuiContainer<T extends WTileEntity> extends GuiContainer implements INBTMessage
{
	private final ResourceLocation guiTextureLocation;
	private final WContainer<T> wContainer;
	private final List<WElement> wElements = new ArrayList<>();
	protected final Slot firstPlayerSlot = inventorySlots.getSlot(inventorySlots.inventorySlots.size() - 36);

	public WGuiContainer(@Nonnull final WContainer<T> wContainer, @Nonnull final ResourceLocation guiTextureLocation)
	{
		super(wContainer);
		this.wContainer = wContainer;
		this.guiTextureLocation = guiTextureLocation;
	}

	public void addElement(@Nonnull final WElement element)
	{
		wElements.add(element);
	}

	@Nonnull
	public WContainer<T> getContainer()
	{
		return wContainer;
	}

	@Nonnull
	public T getTile()
	{
		return wContainer.getTile();
	}

	public final <C extends IController<?, ?>> C getController(@Nonnull final Class<C> aClass)
	{
		return getTile().getController(aClass);
	}

	@Nonnull
	public <C extends IControl<C>> IControl<C> getControl(@Nonnull final Class<C> cClass)
	{
		return getController(ControlController.class).get(cClass);
	}

	@Nonnull
	public IField<?> getField(@Nonnull final String fieldName)
	{
		return getController(FieldController.class).getField(fieldName);
	}

	@Nonnull
	public Matching getMatching(final int matchingNumber)
	{
		return getController(MatchingController.class).getMatching(matchingNumber);
	}

	@Nonnull
	public EntityPlayer getEntityPlayer()
	{
		return Minecraft.getMinecraft().player;
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		final WGInteraction interaction = new WGInteraction(this, mouseX, mouseY);
		// I couldn't find a better place for the line below =(
		getUpdatableElements().forEach(IUpdatable::update);
		getEnabledElements().forEach(element -> element.draw(interaction));
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected final void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(guiTextureLocation);
		final boolean smallGui = xSize < 256 && ySize < 256;
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, smallGui ? 256 : Math.max(xSize, ySize), smallGui ? 256 : Math.max(xSize, ySize));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
	{
		fontRenderer.drawString(I18n.format(wContainer.getTileName()), 7, 7, 0x404040);
		fontRenderer.drawString(I18n.format("container.inventory"), firstPlayerSlot.xPos - 1, firstPlayerSlot.yPos - 11, 0x404040);
		final WGInteraction interaction = new WGInteraction(this, mouseX, mouseY);
		getEnabledElements().forEach(element -> element.drawForegroundLayer(interaction));
		for (final GuiButton guibutton : this.buttonList)
			if (guibutton.isMouseOver())
				guibutton.drawButtonForegroundLayer(mouseX, mouseY);
	}
	@Override
	protected void keyTyped(final char typedChar, final int keyCode) throws IOException
	{
		final WGKeyInteraction keyInteraction = new WGKeyInteraction(this);
		if (interact(keyInteraction))
			super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException
	{
		final WGMouseInteraction mouseInteraction = new WGMouseInteraction(this);
		if (interact(mouseInteraction))
			super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	private boolean interact(@Nonnull final WGInteraction wgInteraction)
	{
		for (final WElement element : getInteractableElements(wgInteraction)) {
			if (wgInteraction.proceed())
				element.interaction(wgInteraction);
		}
		return wgInteraction.proceed();
	}

	private Collection<WElement> getEnabledElements()
	{
		return wElements.stream().filter(WElement::isEnabled).collect(Collectors.toList());
	}

	private Collection<WElement> getInteractableElements(@Nonnull final WGInteraction wgInteraction)
	{
		return wElements.stream().filter(element -> element.isEnabled() && element.canInteractWith(wgInteraction)).collect(Collectors.toList());
	}

	private Collection<IUpdatable> getUpdatableElements()
	{
		return wElements.stream().filter(IUpdatable.class::isInstance).map(IUpdatable.class::cast).collect(Collectors.toList());
	}

	@Override
	public void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		wElements.stream().filter(INBTMessage.class::isInstance).forEach(element -> ((INBTMessage)element).receiveNBT(nbtTagCompound));
	}
}