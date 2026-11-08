# 2025 省赛 Java B 组 C - 电池分组

> 题面来源：https://www.dotcpp.com/oj/problem3313.html。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 问题描述

研究员小蓝受到实验室主任的指示，需要对实验室新研发的 N 个新型能量电池进行分组实验。这 N 个能量电池的能量值分别用 A₁, A₂, ..., A_N 表示，每个能量值都是一个整数。为了保证实验的安全性，小蓝需要将这 N 个能量电池分成两组，使得这两组能量电池的能量值异或和相等。

能量值的异或和计算方法如下：对于一个集合 S，其异或和等于集合中所有元素的按位异或结果。例如，集合 {1, 2, 3} 的异或和为 1 ⊕ 2 ⊕ 3 = 0，其中 ⊕ 表示异或运算。

现在，小蓝想知道，这 N 个能量电池能否分成两组，使得这两组能量电池的能量值异或和相等。注意，每组至少包含一个能量电池。

请你帮帮他！

### 输入格式

输入的第一行包含一个整数 T，表示测试用例的数量。

每个测试用例占两行：

第一行包含一个整数 N，表示能量电池的数量。

第二行包含 N 个整数 A₁, A₂, ..., A_N，表示每个能量电池的能量值。

### 输出格式

对于每个测试用例，输出一行。如果可以将能量电池分成两组，使得这两组能量电池的能量值异或和相等，则输出 “YES”；否则，输出 “NO”。

### 样例输入

```text
2
3
1 2 3
4
1 2 3 4
```

### 样例输出

```text
YES
NO
```

### 评测用例规模与约定

对于 30% 的评测用例，`1 ≤ T ≤ 10`，`2 ≤ N ≤ 100`，`1 ≤ Ai ≤ 10^3`。

对于 100% 的评测用例，`1 ≤ T ≤ 10^3`，`2 ≤ N ≤ 10^3`，`1 ≤ Ai ≤ 10^5`。

## 思路

1. 记全部元素的异或和为 `X`。如果存在一种分组，两组异或和都为 `y`，那么 `X = y ⊕ y = 0`。
2. 反过来，若 `X = 0`，任取一个非空真子集 `S`。因为 `N ≥ 2`，其补集也非空，且
   `xor(补集) = X ⊕ xor(S) = xor(S)`，所以两组异或和相等。
3. 因此答案与具体元素无关，只取决于总异或和是否为 0：为 0 输出 `YES`，否则输出 `NO`。

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
        int T = in.nextInt();
        StringBuilder sb = new StringBuilder();
        while (T-- > 0) {
            int n = in.nextInt();
            int xor = 0;
            for (int i = 0; i < n; i++) {
                xor ^= in.nextInt();
            }
            sb.append(xor == 0 ? "YES" : "NO").append('\n');
        }
        System.out.print(sb);
    }
}
```

## 复杂度

- 时间：`O(ΣN)`，所有测试用例的元素总数不超过 `10^6`。
- 空间：`O(1)`（不计输入缓冲与输出缓冲）。

## 易错点

- 不要只看元素总和或元素个数的奇偶性，判定条件是总异或和是否为 0。
- 题目要求每组至少一个元素；`N ≥ 2` 保证了取一个非空真子集时补集也非空。
- 多组数据，每个测试用例的异或和都要重新初始化为 0。
- 输出必须是大写的 `YES` / `NO`。
- 使用按行读取时要注意一行可能包含多个整数，故用 `StringTokenizer` 逐个取。

## 验证

使用题面样例输入运行，实际输出：

```text
YES
NO
```

与样例输出完全一致。
