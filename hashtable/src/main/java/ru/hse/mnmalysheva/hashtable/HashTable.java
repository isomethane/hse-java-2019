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
                Pair p = (Pair) o;
                return key.equals(p.key);
            }
            if (o instanceof String) {
                return key.equals(o);
            }
            return false;
        }
    }

    private final int DEFAULT_TABLE_SIZE = 1000007;
    private List[] table;
    private int numOfKeys;

    /** Sting hash code cropped to size of array. */
    private int getHash(String key) {
        return key.hashCode() % table.length;
    }

    /** Init array. */
    private void initTable(int size) {
        numOfKeys = 0;
        table = new List[size];
        for (int i = 0; i < size; i++) {
            table[i] = new List();
        }
    }

    public HashTable() {
        initTable(DEFAULT_TABLE_SIZE);
    }

    public HashTable(int size) {
        initTable(size);
    }

    /** Number of keys in table. */
    public int size() {
        return numOfKeys;
    }

    /** Change array size.
     * @param size New array size.
     */
    public void resize(int size) {
        List[] oldTable = table;
        initTable(size);
        for (List l : oldTable) {
            while (!l.isEmpty()) {
                Pair p = (Pair) l.removeFirst();
                put(p.key, p.value);
            }
        }
    }

    /** Check if table contains key. */
    boolean contains(String key) {
        return get(key) != null;
    }

    /** Get value from table.
     * @return value if table contains key, null otherwise.
     */
    String get(String key) {
        Pair result = (Pair) table[getHash(key)].find(key);
        return result == null ? null : result.value;
    }

    /** Put key to table.
     * @return removed value if table contained key, null otherwise.
     */
    String put(String key, String value) {
        Pair data = new Pair(key, value);
        List l = table[getHash(key)];
        Pair prev = (Pair) l.remove(key);
        l.add(data);
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
        Pair removed = (Pair) table[getHash(key)].remove(key);
        if (removed != null) {
            numOfKeys--;
            return removed.value;
        }
        return null;
    }

    /** Remove all keys. */
    void clear() {
        initTable(DEFAULT_TABLE_SIZE);
        numOfKeys = 0;
    }
}
