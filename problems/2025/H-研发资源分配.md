# 2025 省赛 Java B 组 H - 研发资源分配

> 题面来源：https://www.dotcpp.com/oj/problem3318.html。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 问题描述

在蓝桥科技公司，A 部门和 B 部门正在竞争一种新型 AI 芯片的研发资源。为了公平分配资源，公司设计了一个为期 N 天的分配方案：

每天早上，A 部门和 B 部门各自提交一个需求等级（从 1 到 N 的整数）。提交等级较高的部门获得当天的资源，资源份额等于当天的日期编号（第 1 天为 1 单位，第 2 天为 2 单位，依次递增）。若两部门提交的等级相同，则当天资源作废，双方均无法获得资源。

每个部门必须在 N 天内使用 1 到 N 的所有等级，且每个等级只能使用一次。

有趣的是，A 部门在 B 部门内部安插了一名 “间谍”，提前获知了 B 部门的需求等级提交顺序，记为排列 (P₁, P₂, ..., P_N)，其中 Pᵢ 表示 B 部门在第 i 天提交的需求等级。

现在，请你帮助 A 部门分析，在已知 B 部门需求等级顺序的情况下，A 部门的总资源份额减去 B 部门的总资源份额的差值最大可以是多少？

### 输入格式

第一行包含一个整数 N，表示分配方案的天数。

第二行包含 N 个整数 P₁, P₂, ..., P_N，表示 B 部门在第 1 天到第 N 天提交的需求等级。

### 输出格式

输出一个整数，表示 A 部门的总资源份额减去 B 部门的总资源份额的最大差值。

### 样例输入

```text
3
1 3 2
```

### 样例输出

```text
2
```

### 样例说明

A 部门可以选择排列 [2, 1, 3]：

- 第 1 天：A(= 2) > B(= 1)，A 获得 1 单位资源；
- 第 2 天：A(= 1) < B(= 3)，B 获得 2 单位资源；
- 第 3 天：A(= 3) > B(= 2)，A 获得 3 单位资源。

两者的差值为 4 − 2 = 2。

### 评测用例规模与约定

对于 20% 的评测用例，`1 ≤ N ≤ 11`，`1 ≤ Pi ≤ N`，`P₁, P₂, ..., P_N` 各不相同。

对于 100% 的评测用例，`1 ≤ N ≤ 10^5`，`1 ≤ Pi ≤ N`，`P₁, P₂, ..., P_N` 各不相同。

## 思路

1. 由于 `P` 是排列，按“需求等级”重新编号：令 `w[p]` 表示 B 在第几天提交了等级 `p`。于是第 `p` 等级的权重就是 `w[p]`。
2. 问题变成：给每个等级 `p` 分配一个 A 的等级 `x`，使 `Σ w[p] × sgn(x − p)` 最大。
3. 把分配看成一个排列的环分解。若一个环不是长度为 1 的自环，则环内 `Σ(x − p) = 0`，所以至少存在一条“下降边”；特别地，环中的最大等级无法映射到更大的等级，必然是下降边。
4. 对某个环，若除最大值外全部上升、最大值下降，则贡献为 `Σ_{p<max} w[p] − w[max]`；不可能比这更好，因为任何环的下降边总权重至少包含其最大值的 `w`。因此同一个点集放在一个环里最优。
5. 设“平局”的等级集合为 `F`，其余等级集合为 `R`，令 `m = max(R)`。那么所有 `p > m` 必须是平局；而 `p < m` 若改成平局，会由 `+w[p]` 变成 0，只会更差。因此最优的 `F` 一定是后缀 `{m+1, ..., N}`。
6. 此时把 `1..m` 放成一个环 `1 → 2 → ... → m → 1`，贡献为
   `Σ_{p=1}^{m-1} w[p] − w[m] = prefix[m] − 2 × w[m]`。
7. 还要考虑全部平局（差值为 0）的情况，故答案为
   `max(0, max_{1≤m≤N} (prefix[m] − 2 × w[m]))`。

## 代码

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        int[] w = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int p = in.nextInt();
            w[p] = i;
        }

        long pre = 0;
        long ans = 0;
        for (int m = 1; m <= n; m++) {
            pre += w[m];
            long cur = pre - 2L * w[m];
            if (cur > ans) ans = cur;
        }
        System.out.println(ans);
    }
}
```

## 复杂度

- 时间：`O(N)`。
- 空间：`O(N)`。

## 易错点

- 结果可能达到 `O(N²)` 量级（`N = 10^5` 时约 `5 × 10^9`），必须使用 `long`。
- `w` 是 `P` 的逆排列：`w[P[i]] = i`，不要直接拿 `P[i]` 当权重。
- 答案是 `max(0, ...)`，因为 A 总可以选择与 B 完全相同的排列，使差值至少为 0。
- 每个非平凡环的最大等级必定是下降边，这是推导的核心，不要漏掉。
- 平局集合只需考虑后缀，不必枚举任意子集。

## 验证

使用题面样例输入运行，实际输出：

```text
2
```

与样例输出一致。另外对 `N ≤ 7` 的全部排列与暴力枚举所有 A 的排列进行过对照，结果完全一致。
