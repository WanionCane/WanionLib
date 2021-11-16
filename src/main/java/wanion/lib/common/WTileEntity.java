package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import wanion.lib.common.control.ControlController;
import wanion.lib.common.field.FieldController;
import wanion.lib.common.matching.MatchingController;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class WTileEntity extends TileEntity implements ISidedInventory
{
    private final Dependencies<IController<?, ?>> controllerHandler = new Dependencies<>();
    private final Collection<IController<?, ?>> controllers = controllerHandler.getInstances();
    private final Map<Capability<?>, Object> capabilitiesMap = new HashMap<>();
    private String customName = null;
    protected final NonNullList<ItemStack> itemStacks = getItemStacks();

    public WTileEntity()
    {
        controllerHandler.subscribe(ControlController.class, () -> new ControlController(this));
        controllerHandler.subscribe(FieldController.class, () -> new FieldController(this));
        controllerHandler.subscribe(MatchingController.class, () -> new MatchingController(this));
    }

    protected NonNullList<ItemStack> getItemStacks()
    {
        return NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
    }

    public <C> void addCapability(@Nonnull final Capability<C> capability, C cObj)
    {
        capabilitiesMap.put(capability, cObj);
    }

    @Nonnull
    public abstract String getDefaultName();

    public final <A extends IController<?, ?>> A getController(@Nonnull final Class<A> aClass)
    {
        return controllerHandler.get(aClass);
    }

    public final Collection<IController<?, ?>> getControllers()
    {
        return controllers;
    }

    public final <A extends IController<?, ?>> boolean hasController(@Nonnull final Class<A> aClass)
    {
        return controllerHandler.contains(aClass);
    }

    @Override
    public final void readFromNBT(@Nonnull final NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        readCustomNBT(nbtTagCompound);
    }

    public void readCustomNBT(@Nonnull final NBTTagCompound nbtTagCompound)
    {
        final NBTTagCompound displayTag = nbtTagCompound.getCompoundTag("display");
        if (displayTag.hasKey("Name"))
            this.customName = displayTag.getString("Name");
        final NBTTagList nbtTagList = nbtTagCompound.getTagList("Contents", 10);
        if (nbtTagList.hasNoTags())
            return;
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            final NBTTagCompound slotCompound = nbtTagList.getCompoundTagAt(i);
            final int slot = slotCompound.getShort("Slot");
            if (slot >= 0 && slot < getSizeInventory())
                setInventorySlotContents(slot, new ItemStack(slotCompound));
        }
        controllers.forEach(controller -> controller.readNBT(nbtTagCompound));
    }

    @Nonnull
    @Override
    public final NBTTagCompound writeToNBT(@Nonnull final NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        return writeCustomNBT(nbtTagCompound);
    }

    public NBTTagCompound writeCustomNBT(@Nonnull final NBTTagCompound nbtTagCompound)
    {
        if (customName != null) {
            final NBTTagCompound nameNBT = new NBTTagCompound();
            nameNBT.setString("Name", customName);
            nbtTagCompound.setTag("display", nameNBT);
        }
        final NBTTagList nbtTagList = new NBTTagList();
        final int max = getSizeInventory();
        for (int i = 0; i < max; i++) {
            final ItemStack itemStack = getStackInSlot(i);
            if (itemStack.isEmpty())
                continue;
            final NBTTagCompound slotCompound = new NBTTagCompound();
            slotCompound.setShort("Slot", (short) i);
            nbtTagList.appendTag(itemStack.writeToNBT(slotCompound));
        }
        controllers.forEach(controller -> nbtTagCompound.merge(controller.writeNBT()));
        controllers.forEach(controller -> controller.afterWriteNBT(nbtTagCompound));
        if (!nbtTagList.hasNoTags())
            nbtTagCompound.setTag("Contents", nbtTagList);
        return nbtTagCompound;
    }

    @Nonnull
    public final ITextComponent getDisplayName()
    {
        return new TextComponentTranslation(getName());
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull final EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(final int index, @Nonnull final ItemStack itemStackIn, @Nonnull final EnumFacing direction)
    {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(final int index, @Nonnull final ItemStack stack, @Nonnull final EnumFacing direction)
    {
        return false;
    }

    @Override
    public boolean isEmpty()
    {
        return itemStacks.stream().allMatch(ItemStack::isEmpty);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(final int index)
    {
        return itemStacks.get(index);
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(final int index, final  int count)
    {
        final ItemStack slotStack = itemStacks.get(index);
        if (slotStack.isEmpty())
            return ItemStack.EMPTY;
        final ItemStack newStack = slotStack.copy();
        newStack.setCount(count);
        slotStack.setCount(slotStack.getCount() - count);
        if ((slotStack.getCount()) == 0)
            itemStacks.set(index, ItemStack.EMPTY);
        markDirty();
        return newStack;
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(final int index)
    {
        final ItemStack itemStack = itemStacks.get(index);
        itemStacks.set(index, ItemStack.EMPTY);
        markDirty();
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(final int index, @Nonnull final ItemStack stack)
    {
        itemStacks.set(index, stack);
        markDirty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public final boolean isUsableByPlayer(@Nonnull final EntityPlayer player)
    {
        return world.getTileEntity(pos) == this && player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(@Nonnull final EntityPlayer player) {}

    @Override
    public void closeInventory(@Nonnull final EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(final int index, @Nonnull final ItemStack stack)
    {
        return true;
    }

    @Override
    public final int getField(final int id)
    {
        return 0;
    }

    @Override
    public final void setField(final int id, final int value) {}

    @Override
    public final int getFieldCount()
    {
        return 0;
    }

    @Override
    public final void clear() {}

    @Nonnull
    @Override
    public final String getName()
    {
        return hasCustomName() ? customName : getDefaultName();
    }

    @Override
    public final boolean hasCustomName()
    {
        return customName != null;
    }

    @Override
    public boolean hasCapability(@Nonnull final Capability<?> capability, final EnumFacing facing)
    {
        return capabilitiesMap.containsKey(capability) || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull final Capability<T> capability, final EnumFacing facing)
    {
        return capabilitiesMap.containsKey(capability) ? (T) capabilitiesMap.get(capability) : super.getCapability(capability, facing);
    }
}