# 2026 省赛 Java B 组 H - 回程

> 题面来源：https://www.dotcpp.com/oj/problem3379.html 。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 题目描述

给定一张包含 $n$ 个点、$m$ 条无向边的图，点的编号为 $1\sim n$。每条边连接两个不同的点，且可能存在重边。每条无向边由三个整数 $u,v,w$ 描述，表示点 $u$ 与点 $v$ 之间有一条无向边，其代价为 $w$。

你需要从点 $n$ 出发，到达点 $1$。

图中有一个特殊点 $X$。当第一次到达点 $X$ 时，可以获得 3 次特殊机会。若 $X=n$，则表示出发时就已经获得这 3 次特殊机会。

在之后的行进过程中，每次经过一条边时，都可以选择是否使用一次特殊机会：

- 如果不使用，则经过这条边的代价为该边原本的代价 $w$；
- 如果使用，则这一次经过该边的代价变为 1。

特殊机会可以少用或不用，但总共最多只能使用 3 次，每次只能作用于当前经过的一条边。

现在，请你计算从点 $n$ 到点 $1$ 的最小总代价。若无法到达，则输出 $-1$。

### 输入格式

第一行包含三个整数 $n,m,X$，分别表示点数、边数和特殊点的位置。

接下来 $m$ 行，每行包含三个整数 $u,v,w$，表示一条连接点 $u$ 与点 $v$ 的无向边，其代价为 $w$。

### 输出格式

输出一个整数，表示从点 $n$ 到点 $1$ 的最小总代价。若无法到达，则输出 $-1$。

### 样例输入

```
6 6 6
1 2 5
2 4 5
1 3 2
3 4 100
4 5 10
5 6 20
```

### 样例输出

```
5
```

### 样例说明

由于 $X=6$，而出发点也是 $6$，因此一开始就已经获得了 3 次特殊机会。

一种最优走法为：$6\to5\to4\to3\to1$，对应边的原代价分别为 $20,10,100,2$。

在其中 3 条边上使用特殊机会，例如对代价为 $20,10,100$ 的三条边使用，则这三次经过的代价都变为 1，最后一条边仍支付原代价 2。

因此总代价为 $1+1+1+2=5$。

### 评测用例规模与约定

对于 $30\%$ 的评测用例，$n,m\le500$。

另有额外 $30\%$ 的评测用例，所有边的代价 $w$ 相同。

对于所有的评测用例，$1\le n\le2\times10^5$，$1\le m\le2\times10^5$，$1\le w\le10^9$，$1\le X,u,v\le n$。

保证图中不含自环，但可能存在重边。

## 思路

这是一个**分层图最短路**问题。

### 状态设计

一个状态由三部分组成：

- 当前所在点 $u$；
- 已经使用的特殊机会次数 $used\in\{0,1,2,3\}$；
- 是否已经到过 $X$（记为 $flag\in\{0,1\}$）。

到过 $X$ 之前没有任何特殊机会可用；第一次到达 $X$ 后 $flag$ 变为 1，并拥有 3 次机会。若 $X=n$，出发时 $flag=1$。

状态编号可以压成 `(u, used, flag)`，共 $8n$ 个状态。

### 转移

从状态 $(u,used,flag)$ 沿一条边 $(u,v,w)$：

- 若 $flag=0$（还没到过 $X$）：
  - 只能按原价走，新代价 $+w$；到达 $v$ 后若 $v=X$，新状态 $flag=1$、$used=0$。
- 若 $flag=1$：
  - 不使用机会：$(v,used,1)$，代价 $+w$；
  - 使用一次机会（$used<3$）：$(v,used+1,1)$，代价 $+1$。

起点：$(n,0,[X=n])$，距离为 0。
终点：点 $1$ 的所有 $used\in[0,3]$、$flag\in\{0,1\}$ 状态中的最小距离。

### 正确性

任意一条从 $n$ 到 $1$ 的路径，以及路径上对每条边是否使用机会的决策，都唯一对应分层图中的一条路径，代价一一对应；反之分层图中的每条路径也对应一个合法方案。因此最短路即为答案。

### 算法

边权为正（$w\ge1$，使用机会后为 1），用 **Dijkstra**。状态数 $8n$，转移数为 $8\cdot2m$，复杂度 $O((n+m)\log n)$。

由于 Java 的 `PriorityQueue` 装箱开销大，且状态可达 $1.6\times10^6$，代码中手写了一个以 `dist[]` 比较的二叉堆。

## 代码

```java
import java.io.*;
import java.util.*;

public class Main {
    static int[] head, to, nxt;
    static long[] wt;
    static int ecnt;
    static long[] dist;
    static int[] heap;
    static int hs;

    static void addEdge(int u, int v, long w) {
        to[ecnt] = v; wt[ecnt] = w; nxt[ecnt] = head[u]; head[u] = ecnt++;
    }

    static void push(int s) {
        if (hs == heap.length) heap = Arrays.copyOf(heap, hs * 2);
        heap[hs] = s;
        int i = hs++;
        while (i > 0) {
            int p = (i - 1) >> 1;
            if (dist[heap[p]] <= dist[heap[i]]) break;
            int tmp = heap[p]; heap[p] = heap[i]; heap[i] = tmp;
            i = p;
        }
    }

    static int pop() {
        int res = heap[0];
        heap[0] = heap[--hs];
        int i = 0;
        while (true) {
            int l = i * 2 + 1, r = l + 1, small = i;
            if (l < hs && dist[heap[l]] < dist[heap[small]]) small = l;
            if (r < hs && dist[heap[r]] < dist[heap[small]]) small = r;
            if (small == i) break;
            int tmp = heap[i]; heap[i] = heap[small]; heap[small] = tmp;
            i = small;
        }
        return res;
    }

    static void relax(int s, long nd) {
        if (nd < dist[s]) {
            dist[s] = nd;
            push(s);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int X = Integer.parseInt(st.nextToken()) - 1;

        head = new int[n];
        Arrays.fill(head, -1);
        to = new int[2 * m];
        wt = new long[2 * m];
        nxt = new int[2 * m];
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken()) - 1;
            int v = Integer.parseInt(st.nextToken()) - 1;
            long w = Long.parseLong(st.nextToken());
            addEdge(u, v, w);
            addEdge(v, u, w);
        }

        int states = n * 8; // node * 8 + used * 2 + gotX
        dist = new long[states];
        Arrays.fill(dist, Long.MAX_VALUE);
        heap = new int[16];
        hs = 0;

        int start = (n - 1) * 8 + (X == n - 1 ? 1 : 0); // used = 0
        dist[start] = 0;
        push(start);

        while (hs > 0) {
            int s = pop();
            long d = dist[s];
            int node = s >> 3;
            int used = (s >> 1) & 3;
            int flag = s & 1;
            for (int e = head[node]; e != -1; e = nxt[e]) {
                int v = to[e];
                long w = wt[e];
                if (flag == 0) {
                    int nf = (v == X ? 1 : 0);
                    relax((v << 3) | nf, d + w);
                } else {
                    relax((v << 3) | (used << 1) | 1, d + w);
                    if (used < 3) relax((v << 3) | ((used + 1) << 1) | 1, d + 1);
                }
            }
        }

        long ans = Long.MAX_VALUE;
        for (int u = 0; u < 4; u++)
            for (int f = 0; f < 2; f++)
                ans = Math.min(ans, dist[u * 2 + f]);
        System.out.println(ans == Long.MAX_VALUE ? -1 : ans);
    }
}
```

## 复杂度

- 时间：$O((n+m)\log n)$，常数因子为 8 层状态。
- 空间：$O(n+m)$，`dist` 为 $8n$ 个 `long`，邻接表为 $2m$ 条边。

## 易错点

1. 边权可达 $10^9$、路径长度可达 $2\times10^{14}$，距离必须用 `long`。
2. 到过 $X$ 之前**不能**使用机会；$X=n$ 时出发即拥有机会，起点状态要单独处理。
3. 到达 $X$ 的这条边不能用机会（此时还没获得机会），从 $X$ 出发的边才可以。
4. 使用机会后代价固定为 1，不是 $w-1$、也不是“边权减 1”。
5. 终点的 `used` 可以是 0~3 任意值，`flag` 也可能是 0（从未经过 $X$ 就到达了 1），不要只查某一个状态。
6. 图可能不连通或根本无法到达 1，此时输出 $-1$；不要输出 `Long.MAX_VALUE`。
7. 有重边，邻接表正常存即可，不要手动去重。

## 验证

程序运行题面样例，输出：

```
5
```

与样例输出一致。

另外用 Python 的 `heapq` 写了同样状态分层的最短路作对照，对 $n\le7$、$m\le10$ 的 120 组随机图（含 $X$ 随机、重边、不可达情况）逐一比较，结果全部一致。用 $n=m=2\times10^5$ 的随机数据在 `-Xmx128m` 下测试，运行约 0.2 秒。
