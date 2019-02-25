package ru.hse.mnmalysheva.treeset;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

/** Cartesian tree based set. */
public class TreapSet<E> extends AbstractSet<E> implements MyTreeSet<E> {
    private static Random random = new Random(30);

    private int version;
    private Node nullNode = new Node();
    private Node root = nullNode;
    private MyTreeSet<E> descendingSet = new DescendingSet();
    private Comparator<? super E> comparator;

    {
        nullNode.left = nullNode;
        nullNode.right = nullNode;
        nullNode.parent = nullNode;
    }

    /** {@link TreeSet#TreeSet()} **/
    public TreapSet() {}

    /** {@link TreeSet#TreeSet(Comparator)} **/
    public TreapSet(@Nullable Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    /** {@link TreeSet#size()} **/
    @Override
    public int size() {
        return root.subtreeSize;
    }

    /** {@link TreeSet#clear()} **/
    @Override
    public void clear() {
        if (!isEmpty()) {
            root = nullNode;
            version++;
        }
    }

    /** {@link TreeSet#contains(Object)} **/
    @Override
    public boolean contains(@Nullable Object element) {
        return !isEmpty() && compare(element, descentTo(element).data) == 0;
    }

    /** {@link TreeSet#add(Object)} **/
    @Override
    public boolean add(@Nullable E element) {
        if (contains(element)) {
            return false;
        }
        NodePair splited = split(root, element);
        root = merge(merge(splited.left, new Node(element)), splited.right);
        version++;
        return true;
    }

    /** {@link TreeSet#remove(Object)} **/
    @Override
    public boolean remove(@Nullable Object element) {
        if (!contains(element)) {
            return false;
        }
        var node = descentTo(element);
        var nextNode = nextNode(node, Order.ASCENDING);
        if (nextNode == nullNode) {
            root = split(root, node.data).left;
        } else {
            NodePair splited = split(root, node.data);
            root = merge(splited.left, split(splited.right, nextNode.data).right);
        }
        version++;
        return true;
    }

    /** {@link TreeSet#iterator()} **/
    @Override
    public @NotNull Iterator<E> iterator() {
        return new TreeIterator(Order.ASCENDING);
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull Iterator<E> descendingIterator() {
        return new TreeIterator(Order.DESCENDING);
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull MyTreeSet<E> descendingSet() {
        return descendingSet;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E first() {
        return firstElement(Order.ASCENDING);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E last() {
        return firstElement(Order.DESCENDING);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E lower(@Nullable E element) {
        return boundingElement(element, c -> c < 0, Step.PREVIOUS);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E higher(@Nullable E element) {
        return boundingElement(element, c -> c > 0, Step.NEXT);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E floor(@Nullable E element) {
        return boundingElement(element, c -> c <= 0, Step.PREVIOUS);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E ceiling(@Nullable E element) {
        return boundingElement(element, c -> c >= 0, Step.NEXT);
    }

    private @NotNull NodePair split(@NotNull Node root, @Nullable E element) {
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

    private @NotNull Node merge(@NotNull Node left, @NotNull Node right) {
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

    private @NotNull Node descentTo(@NotNull Node root, @Nullable Object element) {
        if (compare(element, root.data) > 0) {
            return root.right == nullNode ? root : descentTo(root.right, element);
        }
        if (compare(element, root.data) < 0) {
            return root.left == nullNode ? root : descentTo(root.left, element);
        }
        return root;
    }

    private @NotNull Node descentTo(@Nullable Object element) {
        return root == nullNode ? nullNode : descentTo(root, element);
    }

    private @NotNull Node firstNode(@NotNull Node root, Order order) {
        var node = root;
        Node left;
        while ((left = node.getLeft(order)) != nullNode) {
            node = left;
        }
        return node;
    }

    private @NotNull Node nextNode(@NotNull Node node, Order order) {
        Node right = node.getRight(order);
        if (right != nullNode) {
            return firstNode(right, order);
        }
        while (node.parent != nullNode && node != node.parent.getLeft(order)) {
            node = node.parent;
        }
        return node.parent;
    }

    private @NotNull Node adjacentNode(@NotNull Node node, Step step) {
        return nextNode(node, (step == Step.NEXT) ? Order.ASCENDING : Order.DESCENDING);
    }

    private @Nullable E firstElement(Order order) {
        if (root == nullNode) {
            throw new NoSuchElementException("Set is empty.");
        }
        return firstNode(root, order).data;
    }

    private @Nullable E boundingElement(@Nullable E element, Predicate<Integer> boundPredicate, Step step) {
        var node = descentTo(element);
        if (node == nullNode || boundPredicate.test(compare(node.data, element))) {
            return node.data;
        }
        return adjacentNode(node, step).data;
    }

    @SuppressWarnings("unchecked")
    private int compare(@Nullable Object first, @Nullable E second) {
        if (comparator == null) {
            return ((Comparable<? super E>) Objects.requireNonNull(first)).compareTo(Objects.requireNonNull(second));
        }
        return comparator.compare((E) first, second);
    }

    private enum Order { ASCENDING, DESCENDING }

    private enum Step { PREVIOUS, NEXT }

    private class Node {
        private Node left;
        private Node right;
        private Node parent;
        private E data;
        private int priority;
        private int subtreeSize;

        private Node() {}

        private Node(@Nullable E data) {
            subtreeSize = 1;
            this.data = data;
            left = nullNode;
            right = nullNode;
            parent = nullNode;
            priority = random.nextInt();
        }

        private @NotNull Node getLeft(Order order) {
            return (order == Order.ASCENDING) ? left : right;
        }

        private @NotNull Node getRight(Order order) {
            return (order == Order.ASCENDING) ? right : left;
        }

        private void setLeft(@NotNull Node left) {
            if (left != nullNode) {
                left.parent = this;
            }
            this.left = left;
            subtreeSize = left.subtreeSize + right.subtreeSize + 1;
        }

        private void setRight(@NotNull Node right) {
            if (right != nullNode) {
                right.parent = this;
            }
            this.right = right;
            subtreeSize = left.subtreeSize + right.subtreeSize + 1;
        }
    }

    private class NodePair {
        private Node left;
        private Node right;

        private NodePair(@NotNull Node left, @NotNull Node right) {
            this.left = left;
            this.right = right;
            left.parent = nullNode;
            right.parent = nullNode;
        }

        private void setLeft(@NotNull Node left) {
            this.left = left;
            left.parent = nullNode;
        }

        private void setRight(@NotNull Node right) {
            this.right = right;
            right.parent = nullNode;
        }
    }

    private class TreeIterator implements Iterator<E> {
        private int treeVersion;
        private Order order;
        private Node previous;
        private Node next;
        private boolean canRemove;

        private TreeIterator(Order order) {
            this.order = order;
            next = firstNode(root, order);
            treeVersion = version;
        }

        @Override
        public boolean hasNext() {
            checkState();
            return next != nullNode;
        }

        @Override
        public @Nullable E next() {
            checkState();
            if (!hasNext()) {
                throw new NoSuchElementException("The iteration has no more elements.");
            }
            E result = next.data;
            goNext();
            return result;
        }

        @Override
        public void remove() {
            checkState();
            if (!canRemove) {
                if (previous == null) {
                    throw new IllegalStateException(
                            "Cannot remove element. The next method has not yet been called."
                    );
                }
                throw new IllegalStateException(
                        "Cannot remove element. " +
                        "The remove method has already been called after the last call to the next method."
                );
            }
            TreapSet.this.remove(previous.data);
            treeVersion = version;
            canRemove = false;
        }

        private void checkState() {
            if (treeVersion != version) {
                throw new ConcurrentModificationException("Iterator invalidated after modification.");
            }
        }

        private void goNext() {
            previous = next;
            next = nextNode(next, order);
            canRemove = true;
        }
    }

    private class DescendingSet extends AbstractSet<E> implements MyTreeSet<E> {
        @Override
        public int size() {
            return TreapSet.this.size();
        }

        @Override
        public void clear() {
            TreapSet.this.clear();
        }

        @Override
        public boolean contains(@Nullable Object element) {
            return TreapSet.this.contains(element);
        }

        @Override
        public boolean add(@Nullable E element) {
            return TreapSet.this.add(element);
        }

        @Override
        public boolean remove(@Nullable Object element) {
            return TreapSet.this.remove(element);
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
        public @Nullable E first() {
            return TreapSet.this.last();
        }

        @Override
        public @Nullable E last() {
            return TreapSet.this.first();
        }

        @Override
        public @Nullable E lower(@Nullable E element) {
            return TreapSet.this.higher(element);
        }

        @Override
        public @Nullable E higher(@Nullable E element) {
            return TreapSet.this.lower(element);
        }

        @Override
        public @Nullable E floor(@Nullable E element) {
            return TreapSet.this.ceiling(element);
        }

        @Override
        public @Nullable E ceiling(@Nullable E element) {
            return TreapSet.this.floor(element);
        }
    }
}
