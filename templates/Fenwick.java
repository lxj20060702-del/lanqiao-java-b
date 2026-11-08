/**
 * 树状数组（Fenwick Tree）：单点修改 + 前缀和查询，O(log n)。
 * 下标从 1 开始。
 */
public class Fenwick {

    private final long[] tree;
    private final int n;

    public Fenwick(int n) {
        this.n = n;
        this.tree = new long[n + 1];
    }

    /** 第 i 个位置加 delta（i 从 1 开始） */
    public void add(int i, long delta) {
        for (; i <= n; i += i & -i) {
            tree[i] += delta;
        }
    }

    /** 前缀和：a[1] + ... + a[i] */
    public long prefixSum(int i) {
        long sum = 0;
        for (; i > 0; i -= i & -i) {
            sum += tree[i];
        }
        return sum;
    }

    /** 区间和：a[l] + ... + a[r] */
    public long rangeSum(int l, int r) {
        if (l > r) {
            return 0;
        }
        return prefixSum(r) - prefixSum(l - 1);
    }
}
