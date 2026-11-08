# 2026 省赛 Java B 组 G - 星座导航校准器

> 题面来源：https://www.dotcpp.com/oj/problem3378.html 。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 题目描述

深空探测器在执行任务时需要依靠星座导航系统进行精确定位。该系统由若干颗导航卫星组成，每颗卫星都有固定的轨道位置和信号强度。

为了确保导航精度，需要选择一组卫星构成 “导航星座”，使得：

1. 两颗卫星距离过近会产生干扰，降低导航精度；
2. 星座必须保持连通性（通信半径为 $R$，任意两颗卫星都能通过直接或间接通信到达）；
3. 在综合考虑以上因素后，使总导航精度最大化。

### 导航精度计算规则

设选定的导航星座包含卫星集合 $S=\{s_1,s_2,\dots,s_k\}$，每颗卫星 $s_i$ 位于坐标 $(x_i,y_i)$，信号强度为 $p_i$。

**基础精度**：每颗卫星贡献的基础精度为其信号强度 $p_i$。

**几何加成**：考虑星座的几何分布，计算所有卫星对之间的几何加成：

$$\text{GeometricBonus}=\sum_{i=1}^{k-1}\sum_{j=i+1}^{k}\frac{p_i\times p_j}{\sqrt{d_{ij}^2+1}}$$

其中 $d_{ij}=\sqrt{(x_i-x_j)^2+(y_i-y_j)^2}$ 是卫星 $s_i$ 和 $s_j$ 之间的欧几里得距离。

**连通性约束**：

- 如果两颗卫星距离 $d_{ij}\le R$，则它们可以直接通信。
- 整个星座必须保持连通（任意两颗卫星都能通过直接或间接通信到达）。

**干扰惩罚**：如果两颗卫星距离过近（$d_{ij}<T$），会产生信号干扰，精度减少：

$$\text{InterferencePenalty}=\sum_{i=1}^{k-1}\sum_{j=i+1}^{k}\mathbf{1}_{d_{ij}<T}\cdot(T-d_{ij})\times\min(p_i,p_j)$$

其中 $\mathbf{1}_{d_{ij}<T}$ 是指示函数，当条件满足时为 1，否则为 0；对每一对不同的卫星（$i<j$）各计算一次干扰惩罚。

**总导航精度**：

$$\text{TotalPrecision}=\sum_{i=1}^{k}p_i+\text{GeometricBonus}-\text{InterferencePenalty}$$

给定 $N$ 颗候选卫星的坐标和信号强度，以及通信半径 $R$ 和干扰阈值 $T$，从中选择若干颗卫星组成导航星座，使得：

1. 星座保持连通性（通信半径为 $R$）；
2. 总导航精度最大化；
3. 星座中至少包含 $K$ 颗卫星。

> 注：上述三个公式在原题页面中以图片形式给出，本文已按原图还原，并用两组样例的数值逐项核对通过。

### 输入格式

第一行包含四个整数 $N,K,R,T$，分别表示候选卫星数量、最少卫星数量、通信半径和干扰阈值。

接下来 $N$ 行，每行包含三个整数 $x_i,y_i,p_i$，表示第 $i$ 颗卫星的坐标和信号强度。

### 输出格式

输出一行，包含一个整数，表示能够达到的最大导航精度（向下取整）。

### 样例输入 1

```
3 2 10 3
0 0 5
5 0 8
0 5 6
```

### 样例输出 1

```
39
```

### 样例说明 1

最优解：选择卫星 $\{1,2,3\}$（编号从 1 开始），即坐标为 $(0,0)$、$(5,0)$、$(0,5)$ 的三颗卫星。

连通性验证：所有卫星对都能直接通信，星座连通。

精度计算：

1. **基础精度**：$5+8+6=19$。
2. **几何加成**：
   - 卫星 1-2：$\dfrac{5\times8}{\sqrt{5^2+1}}=\dfrac{40}{\sqrt{26}}\approx7.84$；
   - 卫星 1-3：$\dfrac{5\times6}{\sqrt{5^2+1}}=\dfrac{30}{\sqrt{26}}\approx5.88$；
   - 卫星 2-3：$\dfrac{8\times6}{\sqrt{50+1}}=\dfrac{48}{\sqrt{51}}\approx6.72$。

   总几何加成：$7.84+5.88+6.72=20.44$。
3. **干扰惩罚**：所有距离都 $\ge5>T=3$，无干扰惩罚。

总精度：$19+20.44-0=39.44$，向下取整为 39。

### 样例输入 2

```
5 3 5 3
0 0 1 0
2 0 8
4 0 6
10 0 12
1 4 9
```

> 注：Dotcpp 页面此处第 2 行原文显示为 `0 0 1 0`（只有 4 个整数）。根据样例说明中“基础精度 $10+8+6+9=33$”以及几何加成按 $10\times8$ 计算可知，第 1 颗卫星应为 `0 0 10`，页面疑为排版丢失了一个 0。本文件的验证使用 `0 0 10`。

### 样例输出 2

```
139
```

### 样例说明 2

最优解：选择卫星 $\{1,2,3,5\}$（编号从 1 开始），即坐标为 $(0,0)$、$(2,0)$、$(4,0)$、$(1,4)$ 的四颗卫星。

连通性验证：所有选中卫星对都能直接通信，星座连通。

精度计算：

1. **基础精度**：$10+8+6+9=33$。
2. **几何加成**：
   - 卫星 1-2：$\dfrac{10\times8}{\sqrt{2^2+1}}=\dfrac{80}{\sqrt{5}}\approx35.78$；
   - 卫星 1-3：$\dfrac{10\times6}{\sqrt{4^2+1}}=\dfrac{60}{\sqrt{17}}\approx14.55$；
   - 卫星 1-5：$\dfrac{10\times9}{\sqrt{17+1}}=\dfrac{90}{\sqrt{18}}\approx21.21$；
   - 卫星 2-3：$\dfrac{8\times6}{\sqrt{2^2+1}}=\dfrac{48}{\sqrt{5}}\approx21.47$；
   - 卫星 2-5：$\dfrac{8\times9}{\sqrt{17+1}}=\dfrac{72}{\sqrt{18}}\approx16.97$；
   - 卫星 3-5：$\dfrac{6\times9}{\sqrt{5^2+1}}=\dfrac{54}{\sqrt{26}}\approx10.59$。

   总几何加成：$35.78+14.55+21.21+21.47+16.97+10.59=120.57$。
3. **干扰惩罚**：
   - 卫星 1-2 距离 $2<T=3$，产生干扰：$(3-2)\times\min(10,8)=1\times8=8$；
   - 卫星 2-3 距离 $2<T=3$，产生干扰：$(3-2)\times\min(8,6)=1\times6=6$；
   - 其他卫星对距离都 $\ge3$，无干扰。

   总干扰惩罚：$8+6=14$。

总精度：$33+120.57-14=139.57$，向下取整为 139。

### 评测用例规模与约定

对于 $30\%$ 的数据：$N\le8$，$K\le3$；

对于 $60\%$ 的数据：$N\le12$，$K\le5$；

对于所有评测用例：

- $N\le15$，$K\le8$，$R\le20$，$T\le10$；
- 坐标范围：$0\le x_i,y_i\le100$；
- 信号强度：$1\le p_i\le20$；
- 保证存在至少 $K$ 颗卫星、且满足连通性约束的解。

## 思路

$N\le15$，直接**枚举所有非空子集**，共 $2^{15}-1=32767$ 个，对每个子集判断合法性并计算精度，取最大值。

1. **预处理**：对任意 $i<j$ 计算
   $$d_{ij}^2=(x_i-x_j)^2+(y_i-y_j)^2.$$
   由于坐标不超过 100，$d_{ij}^2\le20000$，`int` 足够。为了精确判断 $d_{ij}<T$，用整数比较 $d_{ij}^2<T^2$，避免浮点误差。
2. **连通性**：把选中的卫星看作点，若 $d_{ij}^2\le R^2$ 则连一条边，判断这些点是否在同一个连通块内。可用并查集，或从任一选中点做 BFS/DFS。
3. **计算精度**：
   - 基础精度：选中卫星的 $p_i$ 之和；
   - 几何加成：对每个选中对加上 $p_i p_j/\sqrt{d_{ij}^2+1}$；
   - 干扰惩罚：对每个选中对，若 $d_{ij}^2<T^2$，减去 $(T-\sqrt{d_{ij}^2})\cdot\min(p_i,p_j)$。
4. 若子集大小 $\ge K$ 且连通，就用它更新最大值。最终对最大值**向下取整**输出。

**为什么枚举可行**：$N\le15$ 时 $2^N\approx3.3\times10^4$，每个子集检查 $O(N^2)$ 条边，总运算量约 $7\times10^6$，远低于 1 秒限制。

**浮点处理**：所有加法用 `double`。最大值可能恰好是整数，浮点误差可能让 `Math.floor` 少 1，因此取 `Math.floor(best + 1e-9)`。

## 代码

```java
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        int r = Integer.parseInt(st.nextToken());
        int t = Integer.parseInt(st.nextToken());
        int[] x = new int[n], y = new int[n], p = new int[n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            x[i] = Integer.parseInt(st.nextToken());
            y[i] = Integer.parseInt(st.nextToken());
            p[i] = Integer.parseInt(st.nextToken());
        }
        long r2 = (long) r * r, t2 = (long) t * t;
        int[][] d2 = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int dx = x[i] - x[j], dy = y[i] - y[j];
                d2[i][j] = d2[j][i] = dx * dx + dy * dy;
            }
        }
        int full = 1 << n;
        double best = -1e18;
        int[] stack = new int[n];
        for (int mask = 1; mask < full; mask++) {
            if (Integer.bitCount(mask) < k) continue;
            // 连通性：在距离 <= R 的边上 BFS
            int first = Integer.numberOfTrailingZeros(mask);
            int top = 0;
            int seen = 0;
            stack[top++] = first;
            seen |= 1 << first;
            while (top > 0) {
                int u = stack[--top];
                for (int v = 0; v < n; v++) {
                    if ((mask >> v & 1) != 0 && (seen >> v & 1) == 0 && d2[u][v] <= r2) {
                        seen |= 1 << v;
                        stack[top++] = v;
                    }
                }
            }
            if (seen != mask) continue;
            double total = 0;
            for (int i = 0; i < n; i++) {
                if ((mask >> i & 1) == 0) continue;
                total += p[i];
                for (int j = i + 1; j < n; j++) {
                    if ((mask >> j & 1) == 0) continue;
                    double d = Math.sqrt(d2[i][j]);
                    total += (double) p[i] * p[j] / Math.sqrt((double) d2[i][j] + 1.0);
                    if (d2[i][j] < t2) total -= (t - d) * Math.min(p[i], p[j]);
                }
            }
            if (total > best) best = total;
        }
        System.out.println((long) Math.floor(best + 1e-9));
    }
}
```

## 复杂度

- 时间：$O(2^N\cdot N^2)$，$N=15$ 时约 $7\times10^6$ 次。
- 空间：$O(N^2)$ 存距离平方，外加 $O(N)$ 的栈。

## 易错点

1. **连通性不是“两两距离都 ≤ R”**：题目要求的是通过直接/间接通信可达，即图连通，不能只检查完全图。
2. 几何加成的分母是 $\sqrt{d_{ij}^2+1}$，不是 $d_{ij}$，也不是 $d_{ij}+1$。
3. 干扰惩罚的系数是 $(T-d_{ij})$，且只对 $d_{ij}<T$（严格小于）的点对计算；判断时用 $d^2<T^2$ 最稳。
4. 最终的“向下取整”要在累加完所有项之后做，不能对每一项单独取整。
5. 至少要选 $K$ 颗；$K=1$ 时单颗卫星也是合法星座。
6. 浮点误差：最后加 `1e-9` 再 `floor`，避免结果本应是整数却少 1。

## 验证

程序运行样例 1，输出：

```
39
```

程序用修正后的样例 2 输入（第 1 颗卫星为 `0 0 10`）运行，输出：

```
139
```

与题面两组样例输出完全一致，同时也验证了从原图还原出的三个公式（几何加成、干扰惩罚、总精度）在数值上正确。
