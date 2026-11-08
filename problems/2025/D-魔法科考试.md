# 2025 省赛 Java B 组 D - 魔法科考试

> 题面来源：https://www.dotcpp.com/oj/problem3314.html。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 问题描述

小明正在参加魔法科的期末考试，考生需要根据给定的口诀组合出有效的魔法。其中，老师给定了 n 个上半部分口诀 a₁, a₂, ..., aₙ 和 m 个下半部分口诀 b₁, b₂, ..., bₘ，均用整数表示。完整的口诀包含一个上半部分口诀和一个下半部分口诀，当选用两个口诀 aᵢ 和 bⱼ，将组合出完整口诀 S = aᵢ + bⱼ。

当 S 满足 S ≤ n + m 且 S 为质数时，魔法是有效的。魔法的种类只和 S 的大小有关。如果每个上半部分口诀和每个下半部分口诀在不同的组合中可以重复使用，小明想知道一共可能组合出多少种不同的有效魔法？

### 输入格式

输入共三行。

第一行为两个正整数 n, m。

第二行为 n 个由空格分开的正整数 a₁, a₂, ..., aₙ。

第三行为 m 个由空格分开的正整数 b₁, b₂, ..., bₘ。

### 输出格式

输出共 1 行，一个整数表示答案。

### 样例输入

```text
3 4
2 3 10
3 4 5 1
```

### 样例输出

```text
3
```

### 样例说明

可以组合出 3、5、7 这三个有效魔法。

### 评测用例规模与约定

对于 20% 的评测用例，`n, m ≤ 200`。

对于 60% 的评测用例，`n, m ≤ 2000`。

对于 100% 的评测用例，`n, m ≤ 20000`，`ai, bi ≤ 20000`。

## 思路

1. 有效的魔法只由和 `S` 决定，要求 `S` 是质数且 `S ≤ n + m`。
2. 用埃氏筛筛出 `[2, n + m]` 内的所有质数。
3. 用布尔数组记录 A、B 中实际出现过的数值（同一个值重复出现不会产生新的和）。
4. 对每个质数 `S`，枚举 A 中出现过的不同数值 `a`（只需 `a < S`，保证 `b = S - a ≥ 1`），检查 `S - a` 是否在 B 中出现。只要存在一对，就说明 `S` 可以被组合出来，答案加一并跳出。
5. 因为每个 `S` 只统计一次，所以得到的是不同有效魔法的种类数。

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

    static final int MAXV = 20000;

    public static void main(String[] args) {
        FastReader in = new FastReader();
        int n = in.nextInt();
        int m = in.nextInt();
        boolean[] hasA = new boolean[MAXV + 1];
        boolean[] hasB = new boolean[MAXV + 1];
        int[] listA = new int[n];
        int cntA = 0;
        for (int i = 0; i < n; i++) {
            int a = in.nextInt();
            if (!hasA[a]) {
                hasA[a] = true;
                listA[cntA++] = a;
            }
        }
        for (int i = 0; i < m; i++) {
            hasB[in.nextInt()] = true;
        }
        Arrays.sort(listA, 0, cntA);

        int limit = n + m;
        boolean[] prime = new boolean[limit + 1];
        Arrays.fill(prime, true);
        if (limit >= 0) prime[0] = false;
        if (limit >= 1) prime[1] = false;
        for (int i = 2; (long) i * i <= limit; i++) {
            if (prime[i]) {
                for (int j = i * i; j <= limit; j += i) {
                    prime[j] = false;
                }
            }
        }

        int ans = 0;
        for (int s = 2; s <= limit; s++) {
            if (!prime[s]) continue;
            for (int i = 0; i < cntA; i++) {
                int a = listA[i];
                if (a >= s) break;
                int b = s - a;
                if (b >= 1 && b <= MAXV && hasB[b]) {
                    ans++;
                    break;
                }
            }
        }
        System.out.println(ans);
    }
}
```

## 复杂度

- 时间：埃氏筛 `O((n+m) log log(n+m))`；枚举部分为 `O(π(n+m) × |distinct(A)|)`，最坏约 `4.2 × 10^3 × 2 × 10^4 ≈ 8.4 × 10^7`。
- 空间：`O(n + m + 20000)`。

## 易错点

- 上界是 `S ≤ n + m`，不是 `max(ai) + max(bi)`，两者含义不同。
- 需要统计不同的 `S`，同一个 `S` 有多种组合时只能计一次。
- 1 不是质数，筛法初始化时要把 0 和 1 置为 `false`。
- 枚举 `a` 时若 `a ≥ S`，则 `b ≤ 0`，不可能在 B 中出现，及时跳过。
- 输入中的数值可能重复，去重后枚举可避免重复判断，但不影响答案。

## 验证

使用题面样例输入运行，实际输出：

```text
3
```

与样例输出一致（有效魔法为 3、5、7）。
