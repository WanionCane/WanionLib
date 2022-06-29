package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.client.gui.interaction.*;
import wanion.lib.common.WContainer;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

// W = Wanion
@SideOnly(Side.CLIENT)
public abstract class WElement<E extends WElement<E>>
{
	public final static ITooltipSupplier DEFAULT_WELEMENT_TOOLTIP_SUPPLIER = ((interaction, stackSupplier) -> Collections.emptyList());
	public final static Supplier<ItemStack> DEFAULT_ITEMSTACK_SUPPLIER = () -> ItemStack.EMPTY;
	private final Predicate<WInteraction> defaultCheck = wInteraction -> wInteraction.isHovering(this);
	private final Class<E> elementClass;
	private Predicate<WInteraction> foregroundCheck = defaultCheck;
	private Predicate<WInteraction> interactionCheck = defaultCheck;
	private Supplier<ItemStack> stackSupplier = DEFAULT_ITEMSTACK_SUPPLIER;
	private ITooltipSupplier tooltipSupplier = DEFAULT_WELEMENT_TOOLTIP_SUPPLIER;
	private WKeyInteraction.IWKeyInteraction keyInteraction = this::interact;
	private WMouseInteraction.IWMouseInteraction mouseInteraction = this::interact;;
	protected final WGuiContainer<?> wGuiContainer;

	protected final int width, height;
	protected int x, y;
	protected boolean enabled = true;

	// using this constructor, getX, getY, getWidth and getHeight must be overridden.
	public WElement(@Nonnull final WGuiContainer<?> wGuiContainer)
	{
		this(wGuiContainer, 0, 0);
	}

	// using this constructor, getWidth and getHeight must be overridden.
	public WElement(@Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		this(wGuiContainer, x, y, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public WElement(@Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final int width, final int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.wGuiContainer = wGuiContainer;
		this.elementClass = (Class<E>) getClass();
	}

	@Nonnull
	public Predicate<WInteraction> getForegroundCheck()
	{
		return foregroundCheck;
	}

	@Nonnull
	public final E setForegroundCheck(@Nonnull final Predicate<WInteraction> foregroundCheck)
	{
		this.foregroundCheck = foregroundCheck;
		return elementClass.cast(this);
	}

	@Nonnull
	public final E setDefaultChecks()
	{
		return setDefaultForegroundCheck().setDefaultInteractionCheck();
	}

	@Nonnull
	public final E setDefaultForegroundCheck()
	{
		this.foregroundCheck = defaultCheck;
		return elementClass.cast(this);
	}

	@Nonnull
	public ITooltipSupplier getTooltipSupplier()
	{
		return tooltipSupplier;
	}

	@Nonnull
	public final E setTooltipSupplier(@Nonnull final ITooltipSupplier tooltipSupplier)
	{
		this.tooltipSupplier = tooltipSupplier;
		return elementClass.cast(this);
	}

	@Nonnull
	public final E setDefaultTooltipSupplier()
	{
		this.tooltipSupplier = DEFAULT_WELEMENT_TOOLTIP_SUPPLIER;
		return elementClass.cast(this);
	}

	@Nonnull
	public Supplier<ItemStack> getItemStackSupplier()
	{
		return stackSupplier;
	}

	@Nonnull
	public final E setItemStackSupplier(@Nonnull final Supplier<ItemStack> stackSupplier)
	{
		this.stackSupplier = stackSupplier;
		return elementClass.cast(this);
	}

	@Nonnull
	public final E setDefaultItemStackSupplier()
	{
		this.stackSupplier = DEFAULT_ITEMSTACK_SUPPLIER;
		return elementClass.cast(this);
	}

	@Nonnull
	public final List<String> getTooltip(@Nonnull final WInteraction interaction)
	{
		return getTooltipSupplier().getTooltip(interaction, getItemStackSupplier());
	}

	public int getX()
	{
		return x;
	}

	public final void setX(final int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public final void setY(final int y)
	{
		this.y = y;
	}

	public final int getUsableX() {
		return wGuiContainer.getGuiLeft() + getX();
	}

	public final int getUsableY()
	{
		return wGuiContainer.getGuiTop() + getY();
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public final int getWindowID()
	{
		return getWContainer().windowId;
	}

	@Nonnull
	public final WGuiContainer<?> getWGuiContainer()
	{
		return wGuiContainer;
	}

	@Nonnull
	public final WContainer<?> getWContainer()
	{
		return wGuiContainer.getContainer();
	}

	@Nonnull
	public final EntityPlayer getEntityPlayer()
	{
		return wGuiContainer.getEntityPlayer();
	}

	public final boolean isEnabled()
	{
		return enabled;
	}

	// disabling will make it disappear on screen, and no interaction call will be send to it.
	public final void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

	@Nonnull
	public final Predicate<WInteraction> getInteractionCheck()
	{
		return this.interactionCheck;
	}

	@Nonnull
	public final E setInteractionCheck(@Nonnull final Predicate<WInteraction> wInteractionPredicate)
	{
		this.interactionCheck = wInteractionPredicate;
		return elementClass.cast(this);
	}

	@Nonnull
	public final E setDefaultInteractionCheck()
	{
		this.interactionCheck = defaultCheck;
		return elementClass.cast(this);
	}

	@Nonnull
	public final E setKeyInteraction(@Nonnull final WKeyInteraction.IWKeyInteraction IWKeyInteraction)
	{
		this.keyInteraction = IWKeyInteraction;
		return elementClass.cast(this);
	}

	@Nonnull
	public final E setMouseInteraction(@Nonnull final WMouseInteraction.IWMouseInteraction IWMouseInteraction)
	{
		this.mouseInteraction = IWMouseInteraction;
		return elementClass.cast(this);
	}

	public final boolean canInteractWith(@Nonnull final WInteraction wInteraction)
	{
		return interactionCheck.test(wInteraction);
	}

	public final void interaction(@Nonnull final WInteraction wInteraction)
	{
		if (wInteraction instanceof WKeyInteraction)
			keyInteraction.interact((WKeyInteraction) wInteraction);
		else if (wInteraction instanceof WMouseInteraction)
			mouseInteraction.interact((WMouseInteraction) wInteraction);
	}

	public void interact(@Nonnull final WKeyInteraction wKeyInteraction) {}

	public void interact(@Nonnull final WMouseInteraction wMouseInteraction) {}

	public abstract void draw(@Nonnull final WInteraction wInteraction);

	public final void drawForeground(@Nonnull final WInteraction interaction)
	{
		List<String> tooltip;
		if (foregroundCheck.test(interaction) && !(tooltip = getTooltip(interaction)).isEmpty()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F);
			wGuiContainer.drawHoveringText(tooltip, getTooltipX(interaction), getTooltipY(interaction));
		}
	}

	public int getTooltipX(@Nonnull final WInteraction wInteraction)
	{
		return wInteraction.getMouseX() - wGuiContainer.getGuiLeft();
	}

	public int getTooltipY(@Nonnull final WInteraction wInteraction)
	{
		return wInteraction.getMouseY() - wGuiContainer.getGuiTop();
	}

	public final SoundHandler getSoundHandler()
	{
		return Minecraft.getMinecraft().getSoundHandler();
	}

	public final TextureManager getTextureManager()
	{
		return Minecraft.getMinecraft().getTextureManager();
	}

	public final FontRenderer getFontRenderer()
	{
		return Minecraft.getMinecraft().fontRenderer;
	}

	public final void playPressSound()
	{
		getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	public final void playPressSound(@Nonnull final ISound sound)
	{
		getSoundHandler().playSound(sound);
	}

	protected final void bindTexture(@Nonnull final ResourceLocation resourceLocation)
	{
		getTextureManager().bindTexture(resourceLocation);
	}
}