package io.github.lukas2005.DeviceModApps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class UnmodifiableList<E> extends ArrayList<E> {

	public UnmodifiableList(Collection<? extends E> c) {
		super(c);
	}

	@Override
	public boolean add(Object o) {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}

	@Override
	public void add(int index, Object element) {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}

	@Override
	public boolean addAll(Collection c) {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}

	@Override
	public boolean addAll(int index, Collection c) {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}

	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}

	@Override
	public boolean removeAll(Collection c) {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}

	@Override
	public boolean removeIf(Predicate filter) {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}

	@Override
	public void replaceAll(UnaryOperator<E> operator) {
		throw new UnsupportedOperationException("This is an UnmodifiableList!");
	}
}
