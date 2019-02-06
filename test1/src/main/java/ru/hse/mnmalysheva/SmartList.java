package ru.hse.mnmalysheva;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** List optimized for small arrays. */
public class SmartList<E> extends AbstractList<E> implements List<E> {
    private int size;
    private Object data;

    /** Add element to array of size <= 5. */
    private void addToArray(int index, E element) {
        for (int i = size - 1; i >= index; i--) {
            ((Object[]) data)[i + 1] = ((Object[]) data)[i];
        }
        ((Object[]) data)[index] = element;
    }

    /** Remove element from array of size <= 5. */
    private void removeFromArray(int index) {
        for (int i = index + 1; i < size; i++) {
            ((Object[]) data)[i - 1] = ((Object[]) data)[i];
        }
    }

    /** Default constructor creates empty list. */
    public SmartList() {}

    /** Add all elements from collection to list. */
    public SmartList(Collection<? extends E> c) {
        this.addAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            @SuppressWarnings("unchecked")
            var result = (E) data;
            data = element;
            return result;
        }
        if (size <= 5) {
            @SuppressWarnings("unchecked")
            var result = (E) ((Object []) data)[index];
            ((Object []) data)[index] = element;
            return result;
        }
        @SuppressWarnings("unchecked")
        var arrayList = (ArrayList<E>) data;
        return arrayList.set(index, element);
    }

    /** {@inheritDoc} */
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 0) {
            data = element;
        } else if (size == 1) {
            var prev = data;
            data = new Object[5];
            ((Object[]) data)[0] = prev;
            addToArray(index, element);
        } else if (size < 5) {
            addToArray(index, element);
        }
        else if (size == 5) {
            var arrayList = new ArrayList<E>();
            for (int i = 0; i < 5; i++) {
                @SuppressWarnings("unchecked")
                var e = (E) ((Object[]) data)[i];
                arrayList.add(e);
            }
            arrayList.add(index, element);
            data = arrayList;
        } else {
            @SuppressWarnings("unchecked")
            var arrayList = (ArrayList<E>) data;
            arrayList.add(index, element);
        }
        size++;
    }

    /** {@inheritDoc} */
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Object result = null;
        if (size == 1) {
            result = data;
            data = null;
        } else if (size == 2) {
            result = ((Object[]) data)[index];
            data = ((Object[]) data)[index == 0 ? 1 : 1];
        } else if (size <= 5) {
            removeFromArray(index);
        }
        else if (size == 6) {
            @SuppressWarnings("unchecked")
            var arrayList = (ArrayList<E>) data;
            result = arrayList.remove(index);
            data = new Object[5];
            for (int i = 0 ; i < 5; i++) {
                ((Object []) data)[i] = arrayList.get(i);
            }
        } else {
            @SuppressWarnings("unchecked")
            var arrayList = (ArrayList<E>) data;
            result = arrayList.remove(index);
        }
        size--;
        @SuppressWarnings("unchecked")
        var eResult = (E) result;
        return eResult;
    }

    /** {@inheritDoc} */
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            @SuppressWarnings("unchecked")
            var result = (E) data;
            return result;
        }
        if (size <= 5) {
            @SuppressWarnings("unchecked")
            var result = (E) ((Object []) data)[index];
            return result;
        }
        @SuppressWarnings("unchecked")
        var arrayList = (ArrayList<E>) data;
        return arrayList.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        return size;
    }
}
