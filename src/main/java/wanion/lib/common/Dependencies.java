package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import com.google.common.reflect.TypeToken;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@SuppressWarnings("unchecked")
public class Dependencies<D>
{
	private final Map<Class<? extends D>, D> dependencies = new IdentityHashMap<>();
	private final Collection<D> instances = Collections.unmodifiableCollection(dependencies.values());
	private final Map<Class<? extends D>, IDependenceWatcher<? extends D>> dependenciesWatchers = new IdentityHashMap<>();

	public Dependencies() {}

	public Dependencies(final D... dependencies)
	{
		add(dependencies);
	}

	public Dependencies(@Nonnull final Collection<D> dependencies)
	{
		add(dependencies);
	}

	public Dependencies(@Nonnull final Dependencies<D> dependencies)
	{
		add(dependencies.getInstances());
	}

	public final <I extends D> void add(final Class<? extends I> typeClass)
	{
		if (dependencies.containsKey(typeClass))
			return;
		final IDependenceWatcher<? extends D> IDependenceWatcher = dependenciesWatchers.get(typeClass);
		if (IDependenceWatcher != null) {
			add(IDependenceWatcher.instantiate());
			return;
		}
		try {
			final Constructor<?> constructor = typeClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			add((I) constructor.newInstance());
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public final <I extends D> void add(final I instance)
	{
		final Class<I> typeClass = instance != null ? (Class<I>) instance.getClass() : null;
		if (dependencies.containsKey(typeClass))
			return;
		dependencies.put(typeClass, instance);
	}

	public final <I extends D> void add(final I... instances)
	{
		for (final I instance : instances)
			add(instance);
	}

	public final <I extends D> void add(@Nonnull final Collection<I> instances)
	{
		instances.forEach(this::add);
	}

	public final <I extends D> void forceAdd(@Nonnull final I instance)
	{
		dependencies.put((Class<? extends D>) instance.getClass(), instance);
	}

	public final <I extends D> void forceAdd(@Nonnull final I... instances)
	{
		for (final I instance : instances)
			dependencies.put((Class<? extends D>)instance.getClass(), instance);
	}

	public final <I extends D> void forceAdd(@Nonnull final Collection<I> instances)
	{
		instances.forEach(instance -> dependencies.put((Class<? extends D>) instance.getClass(), instance));
	}

	public final <I extends D> I get(final Class<I> typeClass)
	{
		final I instance = (I) dependencies.get(typeClass);
		if (instance != null)
			return instance;
		add(typeClass);
		return (I) dependencies.get(typeClass);
	}

	public final <I extends D> I get(@Nonnull final I instance)
	{
		final Class<I> typeClass = (Class<I>) instance.getClass();
		return get(typeClass);
	}

	public final Collection<D> getDependencies()
	{
		return (Collection<D>) dependencies.keySet();
	}

	public final Collection<D> getInstances()
	{
		return instances;
	}

	public final boolean contains(final Class<? extends D> typeClass)
	{
		return dependencies.containsKey(typeClass);
	}

	@Nonnull
	public final <I extends D> List<I> compareContents(@Nonnull final Dependencies<D> dependencies)
	{
		if (!getDependencies().equals(dependencies.getDependencies()))
			return Collections.emptyList();
		final List<I> differences = new ArrayList<>();
		for (final D dependency : instances)
			if (!dependency.equals(dependencies.get(dependency)))
				differences.add((I) dependency);
		return differences;
	}

	public final <C extends D> void subscribe(final Class<C> dClass, final IDependenceWatcher<C> dependenceWatcher)
	{
		if (!dependenciesWatchers.containsKey(dClass))
			dependenciesWatchers.put(dClass, dependenceWatcher);
	}

	@FunctionalInterface
	public interface IDependenceWatcher<W>
	{
		@Nonnull
		W instantiate();
	}
}