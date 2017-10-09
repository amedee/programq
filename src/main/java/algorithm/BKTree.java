package algorithm;

import java.util.*;

/**
 * Created by joris on 9/24/17.
 */
public class BKTree<T> {

    /**
     * define metric for object of type T
     * @param <T>
     */
    public interface Metric<T>
    {
        int distance(T obj0, T obj1);
    }

    /**
     * define a node type for a BK tree
     * @param <T>
     */
    static class Node<T>
    {
        private Map<Integer, Node<T>> children;
        private Node parent;
        private T data;

        public Node(T obj)
        {
            this.data = obj;
            this.children = new HashMap<>();
            this.parent = null;
        }

        public boolean add(Node<T> c, Metric<T> m)
        {
            int dist = m.distance(c.data, data);
            if(dist == 0)
                return false;
            if(children.containsKey(dist))
                return children.get(dist).add(c, m);
            else {
                children.put(dist, c);
                c.parent = this;
                return true;
            }
        }

        public Collection<Node<T>> get(T obj, Metric<T> m, int tolerance)
        {
            Set<Node<T>> out = new HashSet<>();

            // check self
            int distance = m.distance(data, obj);
            if(distance < tolerance)
                out.add(this);

            // easy case : no children
            if(children.isEmpty())
                return out;

            // recursion
            int lowerbound = Math.max(1, distance - tolerance);
            int upperbound = distance + tolerance;
            for(int i = lowerbound ; i < upperbound ; i++)
            {
                if(children.containsKey(i))
                {
                    Node<T> child = children.get(i);
                    out.addAll(child.get(obj, m, tolerance));
                }
            }

            // default
            return out;
        }

        public int size()
        {
            int s = 0;
            for(Node<T> n : children.values())
                s += n.size();
            s++; // add self
            return s;
        }
    }

    private Metric<T> metric = null;
    private Node<T> root = null;

    public BKTree(Metric m)
    {
        this.metric = m;
    }

    public boolean add(T obj)
    {
        if(root == null)
        {
            root = new Node<>(obj);
            return true;
        }
        else
        {
            return root.add(new Node<>(obj), metric);
        }
    }

    public boolean addAll(Collection<T> other)
    {
        boolean treeChanged = false;
        for(T obj : other)
            treeChanged |= add(obj);
        return treeChanged;
    }

    public boolean contains(T obj)
    {
        return contains(obj, 0);
    }

    public boolean contains(T obj, int maxDistance)
    {
        return !get(obj, maxDistance).isEmpty();
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    public int size()
    {
        if(root == null)
            return 0;
        return root.size();
    }

    public Collection<T> get(T obj, int maxDistance)
    {
        if(root == null)
            return new HashSet<>();
        else {
            Set<T> out = new HashSet<>();
            for(Node<T> n : root.get(obj, metric, maxDistance))
            {
                out.add(n.data);
            }
            return out;
        }
    }
}
