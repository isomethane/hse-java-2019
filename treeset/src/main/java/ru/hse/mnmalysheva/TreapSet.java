package ru.hse.mnmalysheva;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/** Cartesian tree based set. */
public class TreapSet<E> extends AbstractSet<E> implements MyTreeSet<E> {
    private static Random random = new Random(30);

    private int treeVersion;
    private Node nullNode = new Node();
    private Node root = nullNode;
    private MyTreeSet<E> descendingSet = new DescendingSet();
    private Comparator<? super E> comparator;

    /** {@link TreeSet#TreeSet()} **/
    public TreapSet() {}

    /** {@link TreeSet#TreeSet(Comparator)} **/
    public TreapSet(@Nullable Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    /** {@link TreeSet#size()} **/
    @Override
    public int size() {
        return root.size;
    }

    /** {@link TreeSet#isEmpty()} **/
    @Override
    public boolean isEmpty() {
        return root == nullNode;
    }

    /** {@link TreeSet#clear()} **/
    @Override
    public void clear() {
        root = nullNode;
        treeVersion++;
    }

    /** {@link TreeSet#contains(Object)} **/
    @Override
    public boolean contains(@Nullable Object element) {
        return !isEmpty() && compare(element, find(element).data) == 0;
    }

    /** {@link TreeSet#add(Object)} **/
    @Override
    public boolean add(@Nullable E element) {
        if (contains(element)) {
            return false;
        }
        NodePair splited = split(root, element);
        root = merge(merge(splited.left, new Node(element)), splited.right);
        treeVersion++;
        return true;
    }

    /** {@link TreeSet#remove(Object)} **/
    @Override
    public boolean remove(@Nullable Object element) {
        var node = find(element);
        if (root == nullNode || compare(element, node.data) != 0) {
            return false;
        }
        var nextNode = nextNode(node);
        if (nextNode == nullNode) {
            root = split(root, node.data).left;
        } else {
            NodePair splited = split(root, node.data);
            root = merge(splited.left, split(splited.right, nextNode.data).right);
        }
        treeVersion++;
        return true;
    }

    /** {@link TreeSet#iterator()} **/
    @Override
    public @NotNull Iterator<E> iterator() {
        return new TreeIterator(firstNode(root));
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull Iterator<E> descendingIterator() {
        return new DescendingTreeIterator(lastNode(root));
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull MyTreeSet<E> descendingSet() {
        return descendingSet;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E first() {
        if (root == nullNode) {
            throw new NoSuchElementException("Set is empty.");
        }
        return firstNode(root).data;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E last() {
        if (root == nullNode) {
            throw new NoSuchElementException("Set is empty.");
        }
        return lastNode(root).data;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E lower(@Nullable E element) {
        var node = find(element);
        if (node == nullNode || compare(node.data, element) < 0) {
            return node.data;
        }
        return previousNode(node).data;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E higher(@Nullable E element) {
        var node = find(element);
        if (node == nullNode || compare(node.data, element) > 0) {
            return node.data;
        }
        return nextNode(node).data;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E floor(@Nullable E element) {
        var node = find(element);
        if (node == nullNode || compare(node.data, element) <= 0) {
            return node.data;
        }
        return previousNode(node).data;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E ceiling(@Nullable E element) {
        var node = find(element);
        if (node == nullNode || compare(node.data, element) >= 0) {
            return node.data;
        }
        return nextNode(node).data;
    }

    private NodePair split(Node root, E element) {
        if (root == nullNode) {
            return new NodePair(nullNode, nullNode);
        }
        if (compare(element, root.data) <= 0) {
            NodePair result = split(root.left, element);
            root.setLeft(result.right);
            result.setRight(root);
            return result;
        }
        NodePair result = split(root.right, element);
        root.setRight(result.left);
        result.setLeft(root);
        return result;
    }

    private Node merge(Node left, Node right) {
        if (left == nullNode || right == nullNode) {
            return left != nullNode ? left : right;
        }
        if (left.priority > right.priority) {
            left.setRight(merge(left.right, right));
            return left;
        }
        right.setLeft(merge(left, right.left));
        return right;
    }

    private Node find(Node root, Object element) {
        if (compare(root.data, element) < 0) {
            return root.right == nullNode ? root : find(root.right, element);
        }
        if (compare(root.data, element) > 0) {
            return root.left == nullNode ? root : find(root.left, element);
        }
        return root;
    }

    private Node find(Object element) {
        return root == nullNode ? nullNode : find(root, element);
    }

    private Node firstNode(Node root) {
        var node = root;
        while (node.left != nullNode) {
            node = node.left;
        }
        return node;
    }

    private Node lastNode(Node root) {
        var node = root;
        while (node.right != nullNode) {
            node = node.right;
        }
        return node;
    }

    private Node previousNode(Node node) {
        if (node.left != nullNode) {
            return lastNode(node.left);
        }
        while (node.parent != nullNode && node != node.parent.right) {
            node = node.parent;
        }
        return node.parent;
    }

    private Node nextNode(Node node) {
        if (node.right != nullNode) {
            return firstNode(node.right);
        }
        while (node.parent != nullNode && node != node.parent.left) {
            node = node.parent;
        }
        return node.parent;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object first, Object second) {
        if (comparator == null) {
            return ((Comparable) first).compareTo(second);
        }
        return comparator.compare((E) first, (E) second);
    }

    private class Node {
        private Node left;
        private Node right;
        private Node parent;
        private E data;
        private int priority;
        private int size;

        // null node constructor
        private Node() {
            left = this;
            right = this;
            parent = this;
        }

        private Node(E data) {
            size = 1;
            this.data = data;
            left = nullNode;
            right = nullNode;
            parent = nullNode;
            priority = random.nextInt();
        }

        private void setLeft(Node left) {
            if (left != nullNode) {
                left.parent = this;
            }
            this.left = left;
            size = left.size + right.size + 1;
        }

        private void setRight(Node right) {
            if (right != nullNode) {
                right.parent = this;
            }
            this.right = right;
            size = left.size + right.size + 1;
        }
    }

    private class NodePair {
        private Node left;
        private Node right;

        private NodePair(Node left, Node right) {
            this.left = left;
            this.right = right;
            left.parent = nullNode;
            right.parent = nullNode;
        }

        private void setLeft(Node left) {
            this.left = left;
            left.parent = nullNode;
        }

        private void setRight(Node right) {
            this.right = right;
            right.parent = nullNode;
        }
    }

    private class TreeIterator implements Iterator<E> {
        protected Node next;
        private final int version;

        private TreeIterator(Node next) {
            this.next = next;
            version = treeVersion;
        }

        private void checkState() {
            if (version != treeVersion) {
                throw new ConcurrentModificationException("Iterator invalidated after modification.");
            }
        }

        protected void goNext() {
            next = nextNode(next);
        }

        @Override
        public boolean hasNext() {
            checkState();
            return next != nullNode;
        }

        @Override
        public E next() {
            checkState();
            if (!hasNext()) {
                throw new NoSuchElementException("The iteration has no more elements.");
            }
            E result = next.data;
            goNext();
            return result;
        }
    }

    private class DescendingTreeIterator extends TreeIterator implements Iterator<E> {
        private DescendingTreeIterator(Node next) {
            super(next);
        }

        @Override
        protected void goNext() {
            next = previousNode(next);
        }
    }

    private class DescendingSet extends AbstractSet<E> implements MyTreeSet<E> {
        @Override
        public int size() {
            return TreapSet.this.size();
        }

        @Override
        public boolean add(E e) {
            return TreapSet.this.add(e);
        }

        @Override
        public boolean remove(Object o) {
            return TreapSet.this.remove(o);
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return TreapSet.this.descendingIterator();
        }

        @Override
        public @NotNull Iterator<E> descendingIterator() {
            return TreapSet.this.iterator();
        }

        @Override
        public @NotNull MyTreeSet<E> descendingSet() {
            return TreapSet.this;
        }

        @Override
        public E first() {
            return TreapSet.this.last();
        }

        @Override
        public E last() {
            return TreapSet.this.first();
        }

        @Override
        public E lower(E e) {
            return TreapSet.this.higher(e);
        }

        @Override
        public E higher(E e) {
            return TreapSet.this.lower(e);
        }

        @Override
        public E floor(E e) {
            return TreapSet.this.ceiling(e);
        }

        @Override
        public E ceiling(E e) {
            return TreapSet.this.floor(e);
        }
    }
}
