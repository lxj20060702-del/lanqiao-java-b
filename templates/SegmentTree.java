/**
 * 线段树：区间加 + 区间求和，带 lazy 标记，O(log n)。
 * 下标从 1 开始。
 */
public class SegmentTree {

    private final long[] tree;
    private final long[] lazy;
    private final int n;

    public SegmentTree(int n) {
        this.n = n;
        this.tree = new long[4 * n + 5];
        this.lazy = new long[4 * n + 5];
    }

    /** 用数组 a（0-indexed）建树 */
    public SegmentTree(long[] a) {
        this(a.length);
        build(1, 1, n, a);
    }

    private void build(int node, int l, int r, long[] a) {
        if (l == r) {
            tree[node] = a[l - 1];
            return;
        }
        int mid = (l + r) >>> 1;
        build(node << 1, l, mid, a);
        build(node << 1 | 1, mid + 1, r, a);
        tree[node] = tree[node << 1] + tree[node << 1 | 1];
    }

    /** 区间 [ql, qr] 每个数加 val */
    public void rangeAdd(int ql, int qr, long val) {
        update(1, 1, n, ql, qr, val);
    }

    private void update(int node, int l, int r, int ql, int qr, long val) {
        if (ql <= l && r <= qr) {
            tree[node] += val * (r - l + 1);
            lazy[node] += val;
            return;
        }
        pushDown(node, l, r);
        int mid = (l + r) >>> 1;
        if (ql <= mid) {
            update(node << 1, l, mid, ql, qr, val);
        }
        if (qr > mid) {
            update(node << 1 | 1, mid + 1, r, ql, qr, val);
        }
        tree[node] = tree[node << 1] + tree[node << 1 | 1];
    }

    /** 区间 [ql, qr] 求和 */
    public long rangeSum(int ql, int qr) {
        return query(1, 1, n, ql, qr);
    }

    private long query(int node, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[node];
        }
        pushDown(node, l, r);
        int mid = (l + r) >>> 1;
        long sum = 0;
        if (ql <= mid) {
            sum += query(node << 1, l, mid, ql, qr);
        }
        if (qr > mid) {
            sum += query(node << 1 | 1, mid + 1, r, ql, qr);
        }
        return sum;
    }

    private void pushDown(int node, int l, int r) {
        if (lazy[node] == 0) {
            return;
        }
        if (l != r) {
            int mid = (l + r) >>> 1;
            int left = node << 1;
            int right = left | 1;
            tree[left] += lazy[node] * (mid - l + 1);
            lazy[left] += lazy[node];
            tree[right] += lazy[node] * (r - mid);
            lazy[right] += lazy[node];
        }
        lazy[node] = 0;
    }
}
