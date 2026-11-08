import java.util.*;

/**
 * 图论常用模板：存图、BFS、DFS、Dijkstra、Floyd、Kruskal、拓扑排序。
 * 节点编号 0 ~ n-1，边用 int[]{u, v, w} 表示。
 */
public class GraphTemplates {

    /** 建邻接表。directed = false 表示无向图（每条边存两次） */
    public static List<List<int[]>> buildGraph(int n, int[][] edges, boolean directed) {
        List<List<int[]>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            g.get(e[0]).add(new int[]{e[1], e[2]});
            if (!directed) {
                g.get(e[1]).add(new int[]{e[0], e[2]});
            }
        }
        return g;
    }

    /** BFS 求单源最短边数（无权图），不可达为 -1 */
    public static int[] bfs(List<List<int[]>> g, int start) {
        int n = g.size();
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        Deque<Integer> queue = new ArrayDeque<>();
        dist[start] = 0;
        queue.offer(start);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int[] e : g.get(u)) {
                int v = e[0];
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    queue.offer(v);
                }
            }
        }
        return dist;
    }

    /** DFS 标记连通块（递归版，适合深度不大的图） */
    public static void dfs(List<List<int[]>> g, int u, boolean[] visited) {
        visited[u] = true;
        for (int[] e : g.get(u)) {
            if (!visited[e[0]]) {
                dfs(g, e[0], visited);
            }
        }
    }

    /** Dijkstra 单源最短路（边权非负），不可达为 INF */
    public static long[] dijkstra(List<List<int[]>> g, int start) {
        int n = g.size();
        long inf = Long.MAX_VALUE / 4;
        long[] dist = new long[n];
        Arrays.fill(dist, inf);
        dist[start] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        pq.offer(new long[]{start, 0});
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int u = (int) cur[0];
            if (cur[1] > dist[u]) {
                continue;
            }
            for (int[] e : g.get(u)) {
                int v = e[0];
                long nd = dist[u] + e[1];
                if (nd < dist[v]) {
                    dist[v] = nd;
                    pq.offer(new long[]{v, nd});
                }
            }
        }
        return dist;
    }

    /** Floyd 多源最短路，返回距离矩阵；不可达为 INF */
    public static long[][] floyd(int n, int[][] edges) {
        long inf = Long.MAX_VALUE / 4;
        long[][] d = new long[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(d[i], inf);
            d[i][i] = 0;
        }
        for (int[] e : edges) {
            d[e[0]][e[1]] = Math.min(d[e[0]][e[1]], e[2]);
            d[e[1]][e[0]] = Math.min(d[e[1]][e[0]], e[2]);
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (d[i][k] >= inf) {
                    continue;
                }
                for (int j = 0; j < n; j++) {
                    if (d[k][j] < inf && d[i][k] + d[k][j] < d[i][j]) {
                        d[i][j] = d[i][k] + d[k][j];
                    }
                }
            }
        }
        return d;
    }

    /** Kruskal 最小生成树，返回总权重；图不连通返回 -1 */
    public static long kruskal(int n, int[][] edges) {
        Edge[] es = new Edge[edges.length];
        for (int i = 0; i < edges.length; i++) {
            es[i] = new Edge(edges[i][0], edges[i][1], edges[i][2]);
        }
        Arrays.sort(es);
        DSU dsu = new DSU(n);
        long total = 0;
        int used = 0;
        for (Edge e : es) {
            if (dsu.union(e.u, e.v)) {
                total += e.w;
                if (++used == n - 1) {
                    break;
                }
            }
        }
        return used == n - 1 ? total : -1;
    }

    /** 拓扑排序（有向图），有环返回 null */
    public static int[] topoSort(List<List<int[]>> g) {
        int n = g.size();
        int[] indegree = new int[n];
        for (List<int[]> list : g) {
            for (int[] e : list) {
                indegree[e[0]]++;
            }
        }
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        int[] order = new int[n];
        int idx = 0;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order[idx++] = u;
            for (int[] e : g.get(u)) {
                if (--indegree[e[0]] == 0) {
                    queue.offer(e[0]);
                }
            }
        }
        return idx == n ? order : null;
    }

    static class Edge implements Comparable<Edge> {
        int u, v, w;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(w, o.w);
        }
    }
}
