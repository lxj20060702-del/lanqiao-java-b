# 2026 省赛 Java B 组 E - 共享单车

> 题面来源：https://www.dotcpp.com/oj/problem3376.html 。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 题目描述

小蓝的工作是管理共享单车。现在，他要将 $n$ 辆没有被妥善停放的共享单车，搬运到 $m$ 个停车点。

小蓝的管理区域是一条街道，可以将其视为一条数轴。第 $i$ 辆单车位于位置 $a_i$，第 $j$ 个停车点位于位置 $b_j$。

将一辆位于位置 $x$ 的单车搬运到位置 $y$ 的停车点，需要消耗 $|x-y|$ 的体力。

每个停车点最多只能停放一辆单车。已知 $n\le m$，因此一定可以为每辆单车分配一个停车点。你需要计算：在合理分配单车与停车点的情况下，小蓝至少需要花费多少体力。

### 输入格式

输入共 3 行。

第一行包含两个正整数 $n,m$，分别表示单车的数量和停车点的数量。

第二行包含 $n$ 个正整数 $a_1,a_2,\dots,a_n$，表示每辆单车所在的位置。

第三行包含 $m$ 个正整数 $b_1,b_2,\dots,b_m$，表示各个停车点所在的位置。

### 输出格式

输出一行一个正整数，表示小蓝至少需要花费的体力。

### 样例输入 1

```
3 4
1 3 7
2 4 5 8
```

### 样例输出 1

```
3
```

### 样例说明 1

一种最优分配方案如下：

- 将位置为 1 的单车搬运到位置为 2 的停车点；
- 将位置为 3 的单车搬运到位置为 4 的停车点；
- 将位置为 7 的单车搬运到位置为 8 的停车点。

此时总花费为：

$$|1-2|+|3-4|+|7-8|=1+1+1=3.$$

因此，最少需要花费的体力为 3。

### 样例输入 2

```
3 4
3 1 3
5 2 2 8
```

### 样例输出 2

```
4
```

### 评测用例规模与约定

对于 $40\%$ 的评测用例，$n,m\le8$。

另有 $20\%$ 的评测用例，$n=m$。

对于所有的评测用例，$1\le n\le m\le5000$，$1\le a_i,b_i\le10^9$。

## 思路

1. **排序不改变最优性**：把 $a$ 和 $b$ 都从小到大排序。在一维数轴上，最优分配一定不会出现“交叉”——若 $a_i\le a_j$ 却把 $a_i$ 分给更右的停车点、$a_j$ 分给更左的停车点，交换两者不会增大总距离（绝对值不等式）。因此可以按排序后的顺序做匹配。
2. **动态规划**：设 $dp[j]$ 表示“前 $i$ 辆单车分配完，且只使用前 $j$ 个停车点”的最小代价。转移时，对第 $i$ 辆单车：
   - 不使用第 $j$ 个停车点：$dp[i][j]=dp[i][j-1]$；
   - 使用第 $j$ 个停车点：$dp[i][j]=dp[i-1][j-1]+|a_i-b_j|$。
   两者取较小值。
3. 初值 $dp[0][0]=0$，$dp[0][j>0]=0$（0 辆单车代价为 0）；其余为无穷大。答案为 $dp[n][m]$。
4. 用滚动数组把第一维压掉，注意每层重新填充无穷大，且 $dp[j-1]$ 必须来自上一层的值——代码中先整行新建 `ndp`，再用旧的 `dp` 转移，天然满足。

**正确性**：排序后，任何最优匹配都可以通过若干次“消除交叉”的交换变成不交叉匹配，且代价不增；不交叉匹配恰好对应从前往后依次选取停车点的方案，DP 枚举了所有这样的方案，故 DP 的最优值就是全局最优值。

## 代码

```java
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        long[] a = new long[n];
        long[] b = new long[m];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) a[i] = Long.parseLong(st.nextToken());
        st = new StringTokenizer(br.readLine());
        for (int j = 0; j < m; j++) b[j] = Long.parseLong(st.nextToken());
        Arrays.sort(a);
        Arrays.sort(b);
        final long INF = Long.MAX_VALUE / 4;
        long[] dp = new long[m + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;
        for (int i = 1; i <= n; i++) {
            long[] ndp = new long[m + 1];
            Arrays.fill(ndp, INF);
            for (int j = 1; j <= m; j++) {
                // 第 i 辆单车停到第 j 个停车点，或前 i 辆只用前 j-1 个点
                long use = dp[j - 1] == INF ? INF : dp[j - 1] + Math.abs(a[i - 1] - b[j - 1]);
                ndp[j] = Math.min(ndp[j - 1], use);
            }
            dp = ndp;
        }
        System.out.println(dp[m]);
    }
}
```

## 复杂度

- 时间：$O(nm)$，最坏 $5000\times5000=2.5\times10^7$，排序另加 $O((n+m)\log(n+m))$。
- 空间：$O(m)$（滚动数组）。

## 易错点

1. 位置最大 $10^9$，距离累加最大约 $5000\times10^9=5\times10^{12}$，必须用 `long`，`int` 会溢出。
2. 必须先对 $a$、$b$ 排序；不排序直接按输入顺序配对不是最优。
3. 转移时 `ndp[j-1]` 与 `dp[j-1]` 不能混用：前者是“第 $i$ 辆不用第 $j$ 个点”，后者是“前 $i-1$ 辆用前 $j-1$ 个点”。
4. 无穷大不要直接用 `Long.MAX_VALUE` 相加，会溢出成负数；用 `Long.MAX_VALUE/4` 之类的安全值。
5. 样例 2 中单车与停车点都有重复坐标，排序后 DP 会自动处理，不要做去重。

## 验证

程序运行样例 1，输出：

```
3
```

运行样例 2，输出：

```
4
```

与题面样例输出完全一致。
