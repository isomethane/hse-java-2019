package ru.hse.mnmalysheva.hashtable;

/** Chained hash table with string keys and values */
public class HashTable {
    /** Key-value pair */
    private static class Pair {
        private final String key;
        private String value;

        private Pair(String key, String value) {
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

    /** Throw IllegalArgumentException if key is null. */
    private static void checkKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
    }

    /** Throw IllegalArgumentException if value is null. */
    private static void checkValue(String value) {
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
        return numOfKeys;
    }

    /** Check if table contains key. */
    public boolean contains(String key) {
        return get(key) != null;
    }

    /** Get value from table.
     * @return value if table contains key, null otherwise.
     */
    public String get(String key) {
        checkKey(key);

        var result = (Pair) table[getHash(key)].find(key);
        return result == null ? null : result.value;
    }

    /** Put key to table.
     * @return removed value if table contained key, null otherwise.
     */
    public String put(String key, String value) {
        checkKey(key);
        checkValue(value);

        var list = table[getHash(key)];
        var prev = (Pair) list.find(key);

        if (prev != null) {
            var prevValue = prev.value;
            prev.value = value;
            return prevValue;
        }

        list.add(new Pair(key, value));
        numOfKeys++;
        if (numOfKeys >= table.length) {
            resize(table.length * 2);
        }
        return null;
    }

    /** Remove key from table.
     * @return removed value if table contained key, null otherwise.
     */
    public String remove(String key) {
        checkKey(key);

        var removed = (Pair) table[getHash(key)].remove(key);
        if (removed == null) {
            return null;
        }
        numOfKeys--;
        if (numOfKeys * 4 < table.length) {
            resize(Math.max(EMPTY_TABLE_SIZE, table.length / 2));
        }
        return removed.value;
    }

    /** Remove all keys. */
    public void clear() {
        initTable(EMPTY_TABLE_SIZE);
    }
}
