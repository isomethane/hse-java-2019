package ru.hse.mnmalysheva.hashtable;

/** Chained hash table with string keys and values */
public class HashTable {
    /** Key-value pair */
    private class Pair {
        String key;
        String value;

        Pair(String key, String value) {
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
                var p = (Pair)o;
                return key.equals(p.key);
            }
            if (o instanceof String) {
                return key.equals(o);
            }
            return false;
        }
    }

    private static final int EMPTY_TABLE_SIZE = 1;
    private List[] table;
    private int numOfKeys;

    /** String hash code cropped to size of array. */
    private int getHash(String key) {
        return Math.abs(key.hashCode() % table.length);
    }

    /** Throw exception if key is null. */
    private void checkKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
    }

    /** Throw exception if value is null. */
    private void checkValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }
    }

    /** Init array of specified size. */
    private void initTable(int size) {
        numOfKeys = 0;
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
                var p = (Pair)list.removeFirst();
                put(p.key, p.value);
            }
        }
    }

    public HashTable() {
        initTable(EMPTY_TABLE_SIZE);
    }

    /** Number of keys in table. */
    public int size() {
        return numOfKeys;
    }

    /** Check if table contains key. */
    boolean contains(String key) {
        return get(key) != null;
    }

    /** Get value from table.
     * @return value if table contains key, null otherwise.
     */
    String get(String key) {
        checkKey(key);

        var result = (Pair)table[getHash(key)].find(key);
        return result == null ? null : result.value;
    }

    /** Put key to table.
     * @return removed value if table contained key, null otherwise.
     */
    String put(String key, String value) {
        checkKey(key);
        checkValue(value);

        var data = new Pair(key, value);
        var list = table[getHash(key)];
        var prev = (Pair)list.remove(key);

        list.add(data);
        if (prev != null) {
            return prev.value;
        }
        numOfKeys++;
        if (numOfKeys >= table.length) {
            resize(table.length * 2);
        }
        return null;
    }

    /** Remove key from table.
     * @return removed value if table contained key, null otherwise.
     */
    String remove(String key) {
        checkKey(key);

        var removed = (Pair)table[getHash(key)].remove(key);
        if (removed != null) {
            numOfKeys--;
            if (numOfKeys * 4 < table.length) {
                resize(table.length / 2);
            }
            return removed.value;
        }
        return null;
    }

    /** Remove all keys. */
    void clear() {
        initTable(EMPTY_TABLE_SIZE);
        numOfKeys = 0;
    }
}
