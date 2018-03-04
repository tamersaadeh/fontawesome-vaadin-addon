package it.saadeh.fontawesome;

import java.util.*;

import com.vaadin.ui.Label;

public class FontAwesomeList extends Label implements List<FontAwesomeItem> {
	private ListType type;

	private ArrayList<FontAwesomeItem> items = new ArrayList<>();
	private StringBuilder sb = new StringBuilder();

	public FontAwesomeList(ListType type) {
		this.type = type;
	}

	@Override
	public String getValue() {
		sb.append('<');
		sb.append(type);
		sb.append(" class=\"");
		sb.append("fa-");
		sb.append(type);
		sb.append("\">");

		for (FontAwesomeItem item : items) {
			sb.append(item.getValue());
		}

		sb.append("</");
		sb.append(type);
		sb.append('>');
		return sb.toString();
	}

	@Override
	public int size() {
		return items.size();
	}

	// The following is only for convenience

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return items.contains(o);
	}

	@Override
	public Iterator<FontAwesomeItem> iterator() {
		return items.iterator();
	}

	@Override
	public Object[] toArray() {
		return items.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return items.toArray(a);
	}

	@Override
	public boolean add(FontAwesomeItem item) {
		return items.add(item);
	}

	@Override
	public boolean remove(Object o) {
		return items.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return items.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends FontAwesomeItem> c) {
		return items.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends FontAwesomeItem> c) {
		return items.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return items.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return items.retainAll(c);
	}

	@Override
	public void clear() {
		items.clear();
	}

	@Override
	public FontAwesomeItem get(int index) {
		return items.get(index);
	}

	@Override
	public FontAwesomeItem set(int index, FontAwesomeItem element) {
		return items.set(index, element);
	}

	@Override
	public void add(int index, FontAwesomeItem element) {
		items.add(index, element);
	}

	@Override
	public FontAwesomeItem remove(int index) {
		return items.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return items.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return items.lastIndexOf(o);
	}

	@Override
	public ListIterator<FontAwesomeItem> listIterator() {
		return items.listIterator();
	}

	@Override
	public ListIterator<FontAwesomeItem> listIterator(int index) {
		return items.listIterator(index);
	}

	@Override
	public List<FontAwesomeItem> subList(int fromIndex, int toIndex) {
		return items.subList(fromIndex, toIndex);
	}

	public enum ListType {
		ORDERED("ol"), UNORDERED("ul");

		final String type;

		ListType(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return type;
		}

	}
}
