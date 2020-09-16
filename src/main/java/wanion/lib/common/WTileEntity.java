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
import wanion.lib.common.control.ControlController;
import wanion.lib.common.field.FieldController;
import wanion.lib.common.matching.MatchingController;

import javax.annotation.Nonnull;

public abstract class WTileEntity extends TileEntity implements ISidedInventory
{
    private final Dependencies<IController<?, ?>> controllers = new Dependencies<>();
    private final boolean hasControls, hasFields, hasMatchings;
    private String customName = null;
    protected final NonNullList<ItemStack> itemStacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);

    public WTileEntity()
    {
        init();
        hasControls = hasController(ControlController.class);
        hasFields = hasController(FieldController.class);
        hasMatchings = hasController(MatchingController.class);
    }

    public abstract void init();

    @Nonnull
    public abstract String getDefaultName();

    public final <A extends IController<?, ?>> A getController(@Nonnull final Class<A> aClass)
    {
        return controllers.get(aClass);
    }

    public final <A extends IController<?, ?>> boolean hasController(@Nonnull final Class<A> aClass)
    {
        return controllers.contains(aClass);
    }

    public boolean hasControls()
    {
        return hasControls;
    }

    public boolean hasFields()
    {
        return hasFields;
    }

    public boolean hasMatchings()
    {
        return hasMatchings;
    }

    @Override
    public final void readFromNBT(@Nonnull final NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        final NBTTagCompound displayTag = nbtTagCompound.getCompoundTag("display");
        if (displayTag.hasKey("Name"))
            this.customName = displayTag.getString("Name");
        if (hasControls)
            getController(ControlController.class).readNBT(nbtTagCompound);
        if (hasFields)
            getController(FieldController.class).readNBT(nbtTagCompound);
        if (hasMatchings)
            getController(MatchingController.class).readNBT(nbtTagCompound);
        final NBTTagList nbtTagList = nbtTagCompound.getTagList("Contents", 10);
        if (nbtTagList.hasNoTags())
            return;
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            final NBTTagCompound slotCompound = nbtTagList.getCompoundTagAt(i);
            final int slot = slotCompound.getShort("Slot");
            if (slot >= 0 && slot < getSizeInventory())
                setInventorySlotContents(slot, new ItemStack(slotCompound));
        }
        readCustomNBT(nbtTagCompound);
    }

    public void readCustomNBT(@Nonnull final NBTTagCompound nbtTagCompound) {}

    @Nonnull
    @Override
    public final NBTTagCompound writeToNBT(@Nonnull final NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        if (customName != null) {
            final NBTTagCompound nameNBT = new NBTTagCompound();
            nameNBT.setString("Name", customName);
            nbtTagCompound.setTag("display", nameNBT);
        }
        if (hasControls)
            nbtTagCompound.merge(getController(ControlController.class).writeNBT());
        if (hasFields)
            nbtTagCompound.merge(getController(FieldController.class).writeNBT());
        if (hasMatchings)
            nbtTagCompound.merge(getController(MatchingController.class).writeNBT());
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
        if (!nbtTagList.hasNoTags())
            nbtTagCompound.setTag("Contents", nbtTagList);
        return writeCustomNBT(nbtTagCompound);
    }

    public NBTTagCompound writeCustomNBT(@Nonnull final NBTTagCompound nbtTagCompound)
    {
        return nbtTagCompound;
    }

    @Nonnull
    public ITextComponent getDisplayName()
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
    public int getSizeInventory()
    {
        return 0;
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

}