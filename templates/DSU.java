/**
 * 并查集：路径压缩 + 按大小合并，近似 O(1)。
 */
public class DSU {

    private final int[] parent;
    private final int[] size;
    private int components;

    /** 0-indexed：节点编号 0 ~ n-1 */
    public DSU(int n) {
        parent = new int[n];
        size = new int[n];
        components = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];   // 路径压缩
            x = parent[x];
        }
        return x;
    }

    /** 合并成功返回 true，已经连通返回 false */
    public boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return false;
        }
        if (size[ra] < size[rb]) {
            int t = ra;
            ra = rb;
            rb = t;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
        components--;
        return true;
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    /** 当前连通块数量 */
    public int componentCount() {
        return components;
    }

    /** x 所在连通块的大小 */
    public int sizeOf(int x) {
        return size[find(x)];
    }
}
