# 2025 省赛 Java B 组 E - 爆破

> 题面来源：https://www.dotcpp.com/oj/problem3315.html。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 问题描述

小明正在参加一场爆破工作。人们在地面上放置了 n 个爆炸魔法阵，第 i 个魔法阵的圆心坐标为 (xᵢ, yᵢ)，半径为 rᵢ。如果两个魔法阵相交，则它们可以一起引爆；如果两个魔法阵不相交，则可以再使用一条魔法回路将它们的边缘连接起来。小明想知道最少需要布置总长度多长的魔法回路才能使得所有的魔法阵可以一起引爆？

### 输入格式

输入共 n + 1 行。

第一行为一个正整数 n。

后面 n 行，每行三个整数表示 xᵢ, yᵢ, rᵢ。

### 输出格式

输出共 1 行，一个浮点数表示答案（四舍五入保留两位小数）。

### 样例输入

```text
4
0 0 1
2 0 2
-3 0 1
4 4 1
```

### 样例输出

```text
2.47
```

### 样例说明

使用魔法回路连接第 1、3 个魔法阵，长度为 1。

使用魔法回路连接第 2、4 个魔法阵，长度为 2√5 − 3 = 1.47。

总长度 2.47。

### 评测用例规模与约定

对于 40% 的评测用例，`n ≤ 500`。

对于 100% 的评测用例，`n ≤ 5000`，`|xi|, |yi| ≤ 2000`，`0 < ri ≤ 20`。

## 思路

1. 把每个魔法阵看成一个顶点。两个魔法阵 i、j 如果相交或相切，即
   `dist(i,j) ≤ rᵢ + rⱼ`，它们可以直接一起引爆，连接代价为 0。
2. 如果不相交，需要一条长度为 `dist(i,j) - rᵢ - rⱼ` 的回路把两者的边缘连起来。统一写成边权
   `w(i,j) = max(0, sqrt((xᵢ-xⱼ)² + (yᵢ-yⱼ)²) - rᵢ - rⱼ)`。
3. “所有魔法阵可以一起引爆”等价于这些顶点在以上边权下连通，因此最少总长度就是该完全图的最小生成树权值。
4. `n ≤ 5000`，完全图边数为 `O(n²)`。不必显式存边，直接用 Prim 算法，在选点后现场计算到其余点的距离并更新，时间 `O(n²)`、空间 `O(n)`。

## 代码

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    String line = br.readLine();
                    if (line == null) return null;
                    st = new StringTokenizer(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) {
        FastReader in = new FastReader();
        int n = in.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        int[] r = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.nextInt();
            y[i] = in.nextInt();
            r[i] = in.nextInt();
        }

        double[] best = new double[n];
        Arrays.fill(best, Double.POSITIVE_INFINITY);
        boolean[] used = new boolean[n];
        best[0] = 0.0;
        double ans = 0.0;

        for (int it = 0; it < n; it++) {
            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!used[i] && (u == -1 || best[i] < best[u])) {
                    u = i;
                }
            }
            used[u] = true;
            ans += best[u];
            for (int v = 0; v < n; v++) {
                if (used[v]) continue;
                long dx = (long) x[u] - x[v];
                long dy = (long) y[u] - y[v];
                double d = Math.sqrt((double) dx * dx + (double) dy * dy) - r[u] - r[v];
                if (d < 0) d = 0;
                if (d < best[v]) best[v] = d;
            }
        }

        System.out.printf("%.2f%n", ans);
    }
}
```

## 复杂度

- 时间：Prim 算法 `O(n²)`，`n = 5000` 时约 `2.5 × 10^7` 次操作。
- 空间：`O(n)`（不存储完全图的边）。

## 易错点

- 两个圆相交时边权应为 0，忘记 `max(0, ...)` 会得到负权。
- 包含、内切等情形也要按“距离 ≤ 半径和”处理为 0。
- 坐标差的平方要用 `long` 或 `double`，避免中间量溢出。
- 输出要求四舍五入保留两位小数，使用 `printf("%.2f")`。
- `n = 1` 时不需要任何回路，答案应为 `0.00`。

## 验证

使用题面样例输入运行，实际输出：

```text
2.47
```

与样例输出一致。
