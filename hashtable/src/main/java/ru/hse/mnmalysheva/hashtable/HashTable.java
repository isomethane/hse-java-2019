package ru.hse.mnmalysheva.hashtable;

import org.jetbrains.annotations.NotNull;

/** Chained hash table with string keys and values */
public class HashTable {
    private static final int DEFAULT_TABLE_SIZE = 1000;
    private List<Entry>[] table;
    private int numberOfKeys;

    public HashTable() {
        initTable(DEFAULT_TABLE_SIZE);
    }

    /** Number of keys in table. */
    public int size() {
        return numberOfKeys;
    }

    /** Check if table contains key. */
    public boolean contains(@NotNull String key) {
        return get(key) != null;
    }

    /** Get value from table.
     * @return value if table contains key, null otherwise.
     */
    public String get(@NotNull String key) {
        for (var entry : table[getHash(key)]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    /** Put key to table.
     * @return removed value if table contained key, null otherwise.
     */
    public String put(@NotNull String key, @NotNull String value) {
        var list = table[getHash(key)];
        Entry previousEntry = null;
        String previousValue = null;
        for (Entry entry : list) {
            if (entry.key.equals(key)) {
                previousEntry = entry;
                previousValue = entry.value;
                break;
            }
        }
        if (previousEntry != null) {
            previousEntry.value = value;
        } else {
            list.add(new Entry(key, value));
            numberOfKeys++;
            adjustTableSize();
        }
        return previousValue;
    }

    /** Remove key from table.
     * @return removed value if table contained key, null otherwise.
     */
    public String remove(@NotNull String key) {
        String removedValue = null;
        for (var iterator = table[getHash(key)].iterator(); iterator.hasNext(); ) {
            var entry = iterator.next();
            if (entry.key.equals(key)) {
                removedValue = entry.value;
                iterator.remove();
                numberOfKeys--;
                adjustTableSize();
                break;
            }
        }
        return removedValue;
    }

    /** Remove all keys. */
    public void clear() {
        initTable(DEFAULT_TABLE_SIZE);
        numberOfKeys = 0;
    }

    private int getHash(@NotNull String key) {
        return Math.abs(key.hashCode() % table.length);
    }

    private void initTable(int size) {
        @SuppressWarnings("unchecked")
        var listArray = (List<Entry>[]) new List[size];
        table = listArray;
        for (int i = 0; i < size; i++) {
            table[i] = new List<>();
        }
    }

    private void resize(int size) {
        var oldTable = table;
        initTable(size);
        for (var list : oldTable) {
            for (var entry : list) {
                table[getHash(entry.key)].add(entry);
            }
        }
    }

    private void adjustTableSize() {
        if (numberOfKeys >= table.length) {
            resize(table.length * 2);
        } else if (table.length > DEFAULT_TABLE_SIZE && numberOfKeys * 4 < table.length) {
            resize(table.length / 2);
        }
    }

    private static class Entry {
        private final String key;
        private String value;

        private Entry(@NotNull String key, @NotNull String value) {
            this.key = key;
            this.value = value;
        }
    }
}
