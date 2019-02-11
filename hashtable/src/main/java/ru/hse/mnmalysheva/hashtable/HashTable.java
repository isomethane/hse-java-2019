package ru.hse.mnmalysheva.hashtable;

import org.jetbrains.annotations.NotNull;

/** Chained hash table with string keys and values */
public class HashTable {
    /** Key-value pair */
    private static class Pair {
        private final String key;
        private String value;

        private Pair(@NotNull String key) {
            this.key = key;
        }

        private Pair(@NotNull String key, @NotNull String value) {
            this.key = key;
            this.value = value;
        }

        /** Compare the specified object with this pair for equality.
         * Pairs are equal if their key are equal.
         * Pair is equal to string if key is equal to this string.
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof Pair) {
                var p = (Pair) o;
                return key.equals(p.key);
            }
            return false;
        }
    }

    private static final int EMPTY_TABLE_SIZE = 1;
    private List[] table;
    private int numberOfKeys;

    /** String hash code cropped to size of array. */
    private int getHash(@NotNull String key) {
        return Math.abs(key.hashCode() % table.length);
    }

    /** Init array of specified size. */
    private void initTable(int size) {
        numberOfKeys = 0;
        table = new List[size];
        for (int i = 0; i < size; i++) {
            table[i] = new List();
        }
    }

    /** Change array size.
     * @param size New array size.
     */
    private void resize(int size) {
        var oldTable = table;
        initTable(size);
        for (var list : oldTable) {
            while (!list.isEmpty()) {
                var p = (Pair) list.removeFirst();
                put(p.key, p.value);
            }
        }
    }

    public HashTable() {
        initTable(EMPTY_TABLE_SIZE);
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
        var result = (Pair) table[getHash(key)].find(new Pair(key));
        return result == null ? null : result.value;
    }

    /** Put key to table.
     * @return removed value if table contained key, null otherwise.
     */
    public String put(@NotNull String key, @NotNull String value) {
        var list = table[getHash(key)];
        var data = new Pair(key, value);
        var previous = (Pair) list.find(data);

        if (previous != null) {
            var prevValue = previous.value;
            previous.value = value;
            return prevValue;
        }

        list.add(data);
        numberOfKeys++;
        if (numberOfKeys >= table.length) {
            resize(table.length * 2);
        }
        return null;
    }

    /** Remove key from table.
     * @return removed value if table contained key, null otherwise.
     */
    public String remove(@NotNull String key) {
        var removed = (Pair) table[getHash(key)].remove(new Pair(key));
        if (removed == null) {
            return null;
        }
        numberOfKeys--;
        if (numberOfKeys * 4 < table.length) {
            resize(Math.max(EMPTY_TABLE_SIZE, table.length / 2));
        }
        return removed.value;
    }

    /** Remove all keys. */
    public void clear() {
        initTable(EMPTY_TABLE_SIZE);
    }
}
