package wanion.lib.client.gui.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 */

import net.minecraft.entity.player.EntityPlayerMP;
import wanion.lib.client.gui.IWGElement;
import wanion.lib.common.field.IField;

import javax.annotation.Nonnull;

public abstract class WGField<F extends IField<F>> implements IWGElement
{
	private final F field;
	private final EntityPlayerMP entityPlayerMP;
	protected final int x, y, width, height;

	public WGField(F field, @Nonnull final EntityPlayerMP entityPlayerMP, final int x, final int y, final int width, final int height)
	{
		this.field = field;
		this.entityPlayerMP = entityPlayerMP;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public F getField()
	{
		return field;
	}

	public EntityPlayerMP getPlayer()
	{
		return entityPlayerMP;
	}

	@Override
	public int getX()
	{
		return x;
	}

	@Override
	public int getY()
	{
		return y;
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public void interact() {}
}