# 2026 省赛 Java B 组 F - 擂台赛

> 题面来源：https://www.dotcpp.com/oj/problem3377.html 。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 题目描述

小蓝想要组织一场擂台赛，比赛共有 $n$ 位选手，编号为 $1$ 至 $n$。初始时，每位选手 $i$ 都在属于自己的第 $i$ 号擂台上。每位选手 $i$ 都有且仅有一个想要前往挑战的目标擂台 $a_i$。

比赛开始时，小蓝需要制定一个出战顺序。按照该顺序，对于每一位出战选手 $i$：

- 如果他的目标擂台 $a_i$ 目前未被关闭，他将离开自己的 $i$ 号擂台并前往 $a_i$ 号擂台发起挑战，同时关闭自己原先所在的擂台 $i$。
- 如果他的目标擂台 $a_i$ 已被关闭，则他无法发起挑战，只能留在原地。

小蓝希望设置一个出战顺序，使得最终被使用过（即作为挑战目标被成功前往）的擂台数量尽可能少。需要注意的是，留在原地的选手并不计入对擂台的使用。

请你帮小蓝计算，在最优的出战顺序下，最少有多少个擂台被使用过？

### 输入格式

输入共 2 行。

第一行为一个正整数 $n$，表示选手数量。

第二行为 $n$ 个正整数 $a_1,a_2,\dots,a_n$，表示每位选手的目标擂台编号。

### 输出格式

输出一个整数，表示最少被使用过的擂台数量。

### 样例输入

```
5
2 3 4 5 4
```

### 样例输出

```
2
```

### 样例说明

其中一种最优的出战顺序为：$1,3,2,5,4$。

1. 选手 1 挑战 2：目标擂台 2 未关闭，出战成功，关闭擂台 1，使用擂台 2；
2. 选手 3 挑战 4：目标擂台 4 未关闭，出战成功，关闭擂台 3，使用擂台 4；
3. 选手 2 挑战 3：目标擂台 3 已被选手 3 出战时关闭，选手 2 只能留在原地；
4. 选手 5 挑战 4：目标擂台 4 未关闭，出战成功，关闭擂台 5，使用擂台 4；
5. 选手 4 挑战 5：目标擂台 5 已被选手 5 出战时关闭，选手 4 只能留在原地。

最终被使用过的擂台为 $\{2,4\}$，共 2 个。

### 评测用例规模与约定

对于 $30\%$ 的数据，$n\le10$；

对于 $100\%$ 的数据，$1\le n\le10^6$，$1\le a_i\le n$ 且 $a_i\ne i$。

## 思路

每个选手 $i$ 指向 $a_i$，形成一个**函数图（每个点出度为 1）**：每个弱连通分量恰有一个有向环，环上挂着若干棵内向树（边指向环）。

### 1. 把“出战顺序”翻译成集合条件

记 $S$ 为**成功出战**的选手集合，$T=N\setminus S$ 为留在原地的选手集合。

- 选手 $i$ 成功 $\iff$ 轮到他时 $a_i$ 尚未关闭；
- 擂台 $i$ 被关闭 $\iff$ 选手 $i$ 成功（离开自己的擂台）。

由此：

- 若 $i\in T$（失败），则他的目标 $a_i$ 必须**已经**被关闭，而只有 $a_i$ 成功才会关闭自己的擂台，所以 $a_i\in S$。即
  $$\forall i\in T:\ a_i\in S.$$
  等价地，**$T$ 是独立集**：不存在边 $i\to a_i$ 两端都在 $T$ 中。
- 若某个分量的环上所有点都在 $S$ 中，则沿着环每条边都要求“$i$ 必须排在 $a_i$ 前面”，绕一圈就矛盾。因此**每个环至少要有一个点属于 $T$**。
- 反之，只要 $T$ 是独立集且与每个环相交，把每条边上的先后约束连起来不会成环（环上至少有一条反向边），按拓扑序出战即可实现。所以上述两条也是充分条件。

### 2. 把目标转化为“未使用的擂台数”

一个擂台 $j$ 被使用，当且仅当存在 $i\in S$ 使 $a_i=j$。因此

$$j\ \text{未被使用}\iff j\ \text{的所有前驱都在 }T.$$

答案 $=n-\text{（最多能让多少个擂台未被使用）}$。于是问题变成：选一个独立集 $T$，与每个环相交，最大化“所有前驱都在 $T$ 中”的结点数。

### 3. 树上 DP

先用拓扑剥叶（Kahn）把非环结点按“儿子先于父亲”的顺序处理。对结点 $u$ 定义：

- `dpT[u]`：$u\in T$ 时子树内的最大“未使用”数；
- `dp0[u]`：$u\notin T$ 且所有儿子都在 $T$ 中（此时 $u$ 未被使用）；
- `dp1[u]`：$u\notin T$ 且至少一个儿子不在 $T$ 中（此时 $u$ 被使用）。

设 $C(u)$ 为儿子集合：

```
dpT[u] = (u 无儿子 ? 1 : 0) + Σ max(dp0[c], dp1[c])
dp0[u] = 1 + Σ dpT[c]
dp1[u] = 在“至少一个儿子取 dp0/dp1”前提下的 Σ max(dp0,dp1,dpT)
```

`dpT[u]` 中“无儿子则 $+1$”是因为没有前驱，自然未被使用；若 $u\in T$，儿子都不能在 $T$，故儿子只能取 `dp0/dp1`。

### 4. 环上 DP

环上结点还要把**环上前驱**算进“所有前驱”里。对环上结点 $j$ 用它的树儿子预计算：

- `A[j] = Σ dpT[c]`（$j\notin T$ 且树儿子全在 $T$）；
- `B[j] = Σ` 至少一个树儿子不在 $T$ 的最优值；
- `C[j] = Σ max(dp0[c],dp1[c])`（$j\in T$）。

环上按顺序做线性 DP，状态为“当前点是否在 $T$”和“是否已出现过 $T$”，并枚举首点的状态与首点前驱（即尾点）是否在 $T$，以正确处理首点的加成和首尾相邻限制。约束：**相邻两点不能同时在 $T$**，且**至少一个点在 $T$**。

最后答案为 $n-\sum$（各分量最大未使用数）。

## 代码

```java
import java.io.*;
import java.util.*;

public class Main {
    static final int NEG = -1000000000;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        int[] a = new int[n];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) a[i] = Integer.parseInt(st.nextToken()) - 1;

        int[] indeg = new int[n];
        for (int i = 0; i < n; i++) indeg[a[i]]++;
        int[] sumT = new int[n];      // 子结点 dpT 之和
        int[] sumBest = new int[n];   // 子结点 best 之和
        int[] mind = new int[n];      // 强制至少一个子结点不在 T 时的最小损失
        int[] cnt = new int[n];       // 已处理的子结点个数
        Arrays.fill(mind, Integer.MAX_VALUE);

        int[] queue = new int[n];
        int head = 0, tail = 0;
        for (int i = 0; i < n; i++) if (indeg[i] == 0) queue[tail++] = i;

        while (head < tail) {
            int u = queue[head++];
            int c = cnt[u];
            // dpT：u 在 T 里；子结点都不能在 T
            int dpT = (c == 0 ? 1 : 0) + sumBest[u];
            // dp0：u 不在 T，且所有子结点都在 T（u 不被使用）
            int dp0 = 1 + sumT[u];
            // dp1：u 不在 T，且至少一个子结点不在 T（u 被使用）
            int dp1 = (c == 0 ? NEG : sumBest[u] - mind[u]);
            int best = Math.max(dpT, Math.max(dp0, dp1));
            int out0 = Math.max(dp0, dp1);

            int p = a[u];
            sumT[p] += dpT;
            sumBest[p] += best;
            int diff = best - out0;
            if (diff < mind[p]) mind[p] = diff;
            cnt[p]++;
            if (--indeg[p] == 0) queue[tail++] = p;
        }

        // 剩下的结点都是环上的结点（indeg == 1）
        int[] A = new int[n];   // 环点不在 T 且所有树儿子在 T
        int[] B = new int[n];   // 环点不在 T 且至少一个树儿子不在 T
        int[] C = new int[n];   // 环点在 T
        int[] cyc = new int[n];
        for (int j = 0; j < n; j++) {
            if (indeg[j] > 0) {
                A[j] = sumT[j];
                B[j] = (cnt[j] == 0 ? NEG : sumBest[j] - mind[j]);
                C[j] = sumBest[j];
            }
        }

        int totalUnused = 0;
        for (int s = 0; s < n; s++) {
            if (indeg[s] == 0) continue;
            int L = 0;
            int u = s;
            while (indeg[u] > 0) {
                indeg[u] = 0;
                cyc[L++] = u;
                u = a[u];
            }
            int bestCycle = NEG;
            for (int sf = 0; sf < 2; sf++) {
                for (int sl = 0; sl < 2; sl++) {
                    if (sf == 1 && sl == 1) continue; // 首尾相邻，不能同时在 T
                    int base;
                    if (sf == 0) {
                        base = Math.max(A[cyc[0]] + (sl == 1 ? 1 : 0), B[cyc[0]]);
                    } else {
                        base = C[cyc[0]];
                    }
                    if (base <= NEG / 2) continue;
                    int[][] dp = new int[2][2];
                    dp[0][0] = dp[0][1] = dp[1][0] = dp[1][1] = NEG;
                    dp[sf][sf] = base;
                    for (int t = 1; t < L; t++) {
                        int j = cyc[t];
                        int[][] nd = new int[2][2];
                        nd[0][0] = nd[0][1] = nd[1][0] = nd[1][1] = NEG;
                        for (int ps = 0; ps < 2; ps++) {
                            for (int fl = 0; fl < 2; fl++) {
                                int cur = dp[ps][fl];
                                if (cur <= NEG / 2) continue;
                                int v0 = Math.max(A[j] + (ps == 1 ? 1 : 0), B[j]);
                                if (cur + v0 > nd[0][fl]) nd[0][fl] = cur + v0;
                                if (ps == 0 && cur + C[j] > nd[1][1]) nd[1][1] = cur + C[j];
                            }
                        }
                        dp = nd;
                    }
                    if (dp[sl][1] > bestCycle) bestCycle = dp[sl][1];
                }
            }
            totalUnused += bestCycle;
        }
        System.out.println(n - totalUnused);
    }
}
```

## 复杂度

- 时间：$O(n)$（拓扑剥叶 $O(n)$，环上 DP 每个点常数次）。
- 空间：$O(n)$，主要数组均为 `int`，$n=10^6$ 时约 30~40 MB。

## 易错点

1. 本题不是“最少成功人数”，而是“最少被使用的**擂台种类数**”，同一个擂台可以被多名选手成功挑战。
2. $T$ 必须是独立集（失败者的目标必须已成功），不能只满足“每个环至少一个失败者”。
3. 环上 DP 必须同时满足“相邻不同时在 $T$”和“至少一个在 $T$”，少了任一条件都会得到不可行解或错误值。
4. 环长可能为 2，首尾两条边会重复限制，枚举首尾状态时同样要禁止首尾同时为 1。
5. $n\le10^6$，不能递归、不能建 `ArrayList` 对象图；用数组 + 拓扑剥叶，否则会栈溢出或内存超限。
6. 所有 DP 值只与个数有关，`int` 足够；但下标转换注意 0-based。

## 验证

程序运行题面样例，输出：

```
2
```

与样例输出一致。

另外写了一个按全排列模拟的暴力程序，对 $n\le7$ 的随机函数图（共 150 组，满足 $a_i\ne i$）逐一对比，全部一致。还用 $n=10^6$ 的纯环数据在 `-Xmx128m` 下测试，运行约 0.4 秒、无内存溢出。
