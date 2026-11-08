# 2025 省赛 Java B 组 F - 数组翻转

> 题面来源：https://www.dotcpp.com/oj/problem3316.html。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 问题描述

小明生成了一个长度为 n 的正整数数组 a₁, a₂, ..., aₙ，他可以选择连续的一段数 aₗ, aₗ₊₁, ..., aᵣ，如果其中所有数都相等即 aₗ = aₗ₊₁ = ... = aᵣ，那么他可以获得 (r − l + 1) × aₗ 的分数。

在选择之前，为了让分数尽可能大，他决定先选择数组中的一段区间，对其进行左右翻转。他想知道在对数组进行翻转之后他能获得的最大分数是多少？

提示：当翻转 aₗ 到 aᵣ 这段区间后，整个数组会变为 a₁, a₂, ..., aₗ₋₁, aᵣ, aᵣ₋₁, ..., aₗ₊₁, aₗ, aᵣ₊₁, ..., aₙ。

### 输入格式

输入共两行。

第一行为一个正整数 n。

第二行为 n 个由空格分开的正整数 a₁, a₂, ..., aₙ。

### 输出格式

输出共 1 行，一个整数表示答案。

### 样例输入

```text
7
4 4 3 3 2 1 3
```

### 样例输出

```text
9
```

### 样例说明

翻转区间 [5, 7]，数组变为 4, 4, 3, 3, 3, 1, 2，最大分数为选择三个 3。

### 评测用例规模与约定

对于 20% 的评测用例，`n ≤ 500`。

对于 100% 的评测用例，`n ≤ 10^6`，`ai ≤ 10^6`。

## 思路

1. 先把原数组划分成若干“极大连续相等段”，记为 `(数值 v, 长度 len)`。
2. 翻转一个区间，只会改变区间两个端点处的相邻关系，段内部的元素顺序虽然反转，但相等段的长度不变。因此翻转后新产生的“更长的相等段”，最多由两个原段拼接而成。
3. 关键观察：同一数值 `v` 的任意两段都可以被拼在一起。设这两段为第 p 段和第 q 段（p 在 q 左边），翻转从「第 p+1 段的开头」到「第 q 段的结尾」这一区间，第 q 段整体会被移到第 p 段右边，从而合并成长度为 `len[p] + len[q]` 的段。
4. 所以对每个数值 `v`，只需要保留它最长的两段长度 `top1[v]`、`top2[v]`，该数值能获得的最大分数为
   `v × (top1[v] + top2[v])`（只有一段时 `top2[v] = 0`）。
5. 所有数值取最大值即为答案。

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
        int[] a = new int[n];
        int maxV = 0;
        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
            if (a[i] > maxV) maxV = a[i];
        }

        int[] top1 = new int[maxV + 1];
        int[] top2 = new int[maxV + 1];
        int i = 0;
        while (i < n) {
            int j = i + 1;
            while (j < n && a[j] == a[i]) j++;
            int v = a[i];
            int len = j - i;
            if (len > top1[v]) {
                top2[v] = top1[v];
                top1[v] = len;
            } else if (len > top2[v]) {
                top2[v] = len;
            }
            i = j;
        }

        long ans = 0;
        for (int v = 1; v <= maxV; v++) {
            if (top1[v] == 0) continue;
            long score = (long) v * (top1[v] + top2[v]);
            if (score > ans) ans = score;
        }
        System.out.println(ans);
    }
}
```

## 复杂度

- 时间：`O(n + max(ai))`，扫描一遍数组并遍历数值范围。
- 空间：`O(max(ai))`，`max(ai) ≤ 10^6`。

## 易错点

- 分数为 `长度 × 数值`，最大可达 `10^6 × 10^6 = 10^12`，必须用 `long`。
- 每个数值要维护最长的两段，不能只维护最长的一段。
- 不要误以为只能合并原数组中相邻的两段；借助翻转，任意两段同值段都能合并。
- 一次翻转最多只能新拼接两段，不能把三段同值段并成一段。
- `n = 1` 时直接取该元素的值，程序同样适用。

## 验证

使用题面样例输入运行，实际输出：

```text
9
```

与样例输出一致（把两个 3 段合并后得到三个 3，分数 `3 × 3 = 9`）。
