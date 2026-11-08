# 2024 省赛 Java B 组 F - 星际旅行

> 题面来源：https://www.dotcpp.com/oj/problem3230.html。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 问题描述

小明国庆节准备去某星系进行星际旅行，这个星系里一共有 n 个星球，其中布置了 m 道双向传送门，第 i 道传送门可以连接 a_i，b_i 两颗星球（题面原文为「a_i, b_i 且任意两颗星球之间最多只有一个传送门」，此处疑似排版遗漏不等号，按常识理解为 a_i ≠ b_i，待核对）。

他看中了一款 “旅游盲盒”，一共有 Q 个盲盒，第 i 个盲盒里的旅行方案规定了旅行的起始星球 x_i 和最多可以使用传送门的次数 y_i。只要从起始星球出发，使用传送门不超过规定次数能到达的所有星球都可以去旅行。

小明关心在每个方案中有多少个星球可以旅行到。小明只能在这些盲盒里随机选一个购买，他想知道能旅行到的不同星球的数量的期望是多少。

### 输入格式

输入共 m + Q + 1 行。

第一行为三个正整数 n，m，Q。

后面 m 行，每行两个正整数 a_i，b_i。

后面 Q 行，每行两个整数 x_i，y_i。

### 输出格式

输出共一行，一个浮点数（四舍五入保留两位小数）。

### 样例输入

```text
3 2 3
1 2
2 3
2 1
2 0
1 1
```

### 样例输出

```text
2.00
```

### 样例说明

第一个盲盒可以旅行到 1, 2, 3。

第二个盲盒可以旅行到 2。

第三个盲盒可以旅行到 1, 2。

所以期望是 (3 + 1 + 2)/3 = 2.00。

### 评测用例规模与约定

- 对于 20% 的评测用例，保证 n ≤ 300。
- 对于 100% 的评测用例，保证 n ≤ 1000，m ≤ min{n(n−1)/2, 5n}，Q ≤ 50000，0 ≤ y_i ≤ n。

## 思路

1. 传送门是双向的，且每使用一次传送门相当于沿一条边移动一步。因此“使用不超过 y 次传送门能到达的星球”就是从起点出发、**最短距离 ≤ y** 的所有节点（包括起点自身，距离 0）。
2. 图是无权图，用 BFS 求最短路。若对每个查询单独 BFS，复杂度为 `O(Q(n+m))`，最坏约 5×10^7，尚可但偏慢。
3. 注意到不同的起点最多只有 n 个。对每个起点 s 做一次 BFS：
   - 统计 `cnt[d]`：到 s 距离恰好为 d 的节点数；
   - 做前缀和 `reach[s][y] = Σ_{d=0}^{y} cnt[d]`，即答案。
4. 每个查询 O(1) 查表，总复杂度 `O(n(n+m) + Q)`。
5. 正确性：BFS 在无权图中求出的就是最短传送门使用次数；距离 ≤ y 与“使用不超过 y 次”等价，因此统计前缀和恰好是可达星球数。

## 代码

```java
import java.io.*;
import java.util.*;

public class Main {
    static int n;
    static ArrayList<Integer>[] adj;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());

        adj = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) adj[i] = new ArrayList<Integer>();
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            adj[a].add(b);
            adj[b].add(a);
        }

        // reach[x][y] = 从 x 出发，使用不超过 y 次传送门能到达的星球数
        int[][] reach = new int[n + 1][n + 1];
        int[] dist = new int[n + 1];
        int[] cnt = new int[n + 1];
        int[] queue = new int[n + 1];
        for (int s = 1; s <= n; s++) {
            Arrays.fill(dist, -1);
            Arrays.fill(cnt, 0);
            int head = 0, tail = 0;
            queue[tail++] = s;
            dist[s] = 0;
            while (head < tail) {
                int u = queue[head++];
                cnt[dist[u]]++;
                for (int v : adj[u]) {
                    if (dist[v] == -1) {
                        dist[v] = dist[u] + 1;
                        queue[tail++] = v;
                    }
                }
            }
            int acc = 0;
            for (int y = 0; y <= n; y++) {
                if (y <= n - 1) acc += cnt[y];
                reach[s][y] = acc;
            }
        }

        long sum = 0;
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            if (y > n) y = n;
            sum += reach[x][y];
        }
        System.out.printf("%.2f%n", (double) sum / q);
    }
}
```

## 复杂度

- 时间：`O(n(n+m) + Q)`，`n ≤ 1000, m ≤ 5n`，约 6×10^6 次 BFS 遍历。
- 空间：`O(n^2 + n + m)`，`reach` 表约 10^6 个 int。

## 易错点

1. **y 的范围**：`y` 可以等于 0（只能到起点）或大于最长最短路；查表时要把 `y` 截断到 n，避免越界。
2. **不连通图**：BFS 距离为 −1 的节点不能计入答案。
3. **期望不能提前取整**：先把所有查询的答案累加成 `long`，最后才除以 Q。
4. **输出格式**：要求四舍五入保留两位小数，用 `printf("%.2f%n", ...)`；Java 的 `%f` 使用 HALF_UP，符合“四舍五入”。
5. **起点自身算一个**：距离 0 的节点也要计入。

## 验证

将代码写入 `/tmp/verify2024/F/Main.java`，用 `javac --release 8 -encoding UTF-8` 编译，用题面样例输入运行，实际输出：

```text
2.00
```

与样例输出一致。另做 100 组随机小图与逐查询 BFS 的暴力对拍（按 ROUND_HALF_UP 比较），全部通过；最大规模 `n=1000, m=5000, Q=50000` 随机数据运行约 0.3 秒。
