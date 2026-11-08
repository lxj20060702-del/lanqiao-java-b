# 2024 省赛 Java B 组 H - 拼十字

> 题面来源：https://www.dotcpp.com/oj/problem3232.html。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 问题描述

在 LQ 国神秘的古老森林，有一座被称为 “拼十字” 的神秘遗迹。据传，“拼十字” 是由古代文明建造的，它是一个巨大的石头结构，由两个巨大的矩形交叉叠放在一起，形成了一个庄严而神秘的十字形状。这个遗迹被认为是连接人类和神灵之间的通道，拥有神秘的力量和能量。

现在给出 N 个矩形，其中第 i 个矩形的长度和宽度分别问 l_i，w_i（原文如此，应为“分别为”），并且矩形颜色 c_i 为红 (0)/黄 (1)/蓝 (2) 中的一种。现在小蓝想知道在这 N 个矩形中有多少对矩形可以 “拼十字”？

两个矩形可以“拼十字”的充要条件是：

1. 两个矩形的颜色不同；
2. 矩形 1 的长度严格大于矩形 2 的长度并且矩形 1 的宽度严格小于矩形 2 的宽度。

注意，矩形长度和宽度属性是固定的，是不可以通过旋转矩形而发生转变的。

### 输入格式

第一行一个整数 N，表示有 N 个矩形。接下来 N 行，每行输入三个整数 l、w、c 表示一个矩形的长、宽和颜色。

### 输出格式

输出一个整数表示答案。由于答案可能会很大，所以你需要将答案对 10^9 + 7 取模之后输出。

### 样例输入

```text
5
1 10 0
6 6 0
8 6 1
6 10 0
1 2 1
```

### 样例输出

```text
2
```

### 样例说明

第 3 个矩形可以和第 1 个矩形拼十字，第 3 个矩形也可以和第 4 个矩形拼十字。所以一共有两对矩形可以拼十字，答案为 2。

### 评测用例规模与约定

- 对于 30% 的评测用例：1 ≤ N ≤ 5000。
- 对于 100% 的评测用例：1 ≤ N ≤ 10^5，1 ≤ l, w ≤ 10^5，0 ≤ c ≤ 2。

## 思路

1. 条件“矩形 1 的长度严格大于矩形 2，且矩形 1 的宽度严格小于矩形 2”本身带有方向。若枚举**有序对** `(i, j)`（i 作矩形 1，j 作矩形 2），满足条件的每一对无序矩形恰好被统计一次（长度大的那个只能当矩形 1），因此直接统计有序对即可，不需要除以 2。
2. 将矩形按长度 `l` 升序排序，并**把长度相同的矩形分成一组统一处理**：先对该组内的每个矩形统计答案，再把整组插入数据结构。这样查询时结构中只含长度严格更小的矩形。
3. 用树状数组维护已处理矩形的宽度分布：
   - `all`：所有已处理矩形按宽度计数；
   - `fw[c]`：颜色为 c 的已处理矩形按宽度计数。
   对当前矩形 `(l, w, c)`，已处理中满足 `w' > w` 的总数为 `processed - all.sum(w)`；其中与它颜色相同的有 `colorCnt[c] - fw[c].sum(w)`。两者相减就是“宽度更大且颜色不同”的数量，累加到答案。
4. 最后对 `10^9 + 7` 取模。
5. 正确性：排序 + 同长度延后插入保证长度严格大于；树状数组后缀计数保证宽度严格小于（即 `w' > w`）；颜色相减保证颜色不同。每个合法有序对只在其“长度较大者”被处理时统计一次。

## 代码

```java
import java.io.*;
import java.util.*;

public class Main {
    static final int MOD = 1000000007;

    static class Rect {
        int l, w, c;
        Rect(int l, int w, int c) { this.l = l; this.w = w; this.c = c; }
    }

    static class Fenwick {
        int n;
        int[] t;
        Fenwick(int n) { this.n = n; t = new int[n + 2]; }
        void add(int i, int v) {
            for (; i <= n; i += i & -i) t[i] += v;
        }
        int sum(int i) { // 前缀和 [1..i]
            int s = 0;
            for (; i > 0; i -= i & -i) s += t[i];
            return s;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        Rect[] a = new Rect[n];
        int maxW = 0;
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            a[i] = new Rect(l, w, c);
            maxW = Math.max(maxW, w);
        }
        Arrays.sort(a, new Comparator<Rect>() {
            public int compare(Rect x, Rect y) { return x.l - y.l; }
        });

        Fenwick[] fw = new Fenwick[3];
        for (int c = 0; c < 3; c++) fw[c] = new Fenwick(maxW + 1);
        Fenwick all = new Fenwick(maxW + 1);
        int[] colorCnt = new int[3];
        long ans = 0;

        int i = 0;
        while (i < n) {
            int j = i;
            while (j < n && a[j].l == a[i].l) j++;
            // 先查询这一组（保证长度严格小于），再统一插入
            for (int k = i; k < j; k++) {
                Rect r = a[k];
                int greaterAll = (i - all.sum(r.w));            // 之前所有 w > r.w 的个数
                int greaterSame = colorCnt[r.c] - fw[r.c].sum(r.w);
                ans += greaterAll - greaterSame;
                if (ans >= MOD) ans %= MOD;
            }
            for (int k = i; k < j; k++) {
                Rect r = a[k];
                all.add(r.w, 1);
                fw[r.c].add(r.w, 1);
                colorCnt[r.c]++;
            }
            i = j;
        }
        System.out.println(ans % MOD);
    }
}
```

## 复杂度

- 时间：`O(N log N + N log W)`，其中 `W ≤ 10^5` 为宽度最大值。
- 空间：`O(N + W)`。

## 易错点

1. **长度严格大于**：长度相同的矩形不能互相统计，必须“同组先查询、后插入”。
2. **宽度严格小于**：查询的是已处理中 `w' > w` 的数量，用“总数 − 前缀 ≤ w”实现；不要漏掉等号处理。
3. **颜色不同**：用总后缀数减去同色后缀数，注意 `colorCnt` 只统计已插入的矩形。
4. **答案取模**：使用 `long` 累加后再取模，避免中途溢出；`int` 只能存到约 2.1×10^9。
5. **不能旋转**：长和宽是固定的，不能交换后再比较。

## 验证

将代码写入 `/tmp/verify2024/H/Main.java`，用 `javac --release 8 -encoding UTF-8` 编译，用题面样例输入运行，实际输出：

```text
2
```

与样例输出一致。另做 200 组随机小数据与 `O(N^2)` 暴力对拍，全部通过；最大规模 `N=100000` 随机数据运行约 0.3 秒。
