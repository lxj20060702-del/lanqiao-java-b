# 2024 省赛 Java B 组 D - 食堂

> 题面来源：https://www.dotcpp.com/oj/problem3228.html。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 问题描述

S 学校里一共有 a2 个两人寝、a3 个三人寝、a4 个四人寝，而食堂里有 b4 个四人桌和 b6 个六人桌。学校想要安排学生们在食堂用餐，并且满足每个寝室里的同学都在同一桌就坐，请问这个食堂最多同时满足多少同学用餐？

### 输入格式

采用多组数据输入。

输入共 q + 1 行。

第一行为一个正整数 q 表示数据组数。

后面 q 行，每行五个非负整数 a2, a3, a4, b4, b6 表示一组数据。

### 输出格式

输出共 q 行，每行一个整数表示对应输入数据的答案。

### 样例输入

```text
2
3 0 1 0 1
0 2 2 1 1
```

### 样例输出

```text
6
10
```

### 样例说明

对于第一组数据，只有一个六人桌，因此最多安排三个两人寝的同学就餐，答案为 (2 + 2 + 2) = 6。

对于第二组数据，用一个六人桌安排两个三人寝的同学，用一个四人桌安排一个四人寝的同学，答案为 (3 + 3) + (4) = 10。

### 评测用例规模与约定

对于 20% 的评测用例，保证 a2 + a3 + a4 ≤ 8。

对于 100% 的评测用例，保证 q ≤ 100，b4 + b6 ≤ a2 + a3 + a4 ≤ 100。

## 思路

把每个寝室看成一个不可拆分的物品，大小分别为 2、3、4；桌子是容量为 4 或 6 的箱子，要把物品放进去使坐下的总人数最多（桌子可以不满）。

1. **关键取舍**：六人桌有两种“坐满”的方式与三人寝有关——`3+3` 或 `4+2`（或 `2+2+2`）。只看局部贪心会出错。例如 `a2=2, a3=2, a4=1, b4=1, b6=1`：若先把六人桌安排 `4+2`，四人桌只能坐一个 2，共 6+2=8；若把六人桌安排 `3+3`，四人桌坐 4，共 6+4=10。因此必须枚举六人桌中 `3+3` 的组数。
2. **枚举 i**：`i` 表示六人桌中安排 `3+3` 的数量，范围 `0 ≤ i ≤ min(b6, a3/2)`。
3. **固定 i 后按优先级贪心**（剩余六人桌 `t6`、四人桌 `t4`；剩余人数 `c2/c3/c4`）：
   1. 六人桌 `4+2`（坐满 6）；
   2. 四人桌一个四人寝（坐满 4）；
   3. 四人桌 `2+2`（坐满 4）；
   4. 六人桌 `2+2+2`（坐满 6）；
   5. 六人桌 `3+2`（坐 5）；
   6. 六人桌一个四人寝（坐 4）；
   7. 六人桌 `2+2`（坐 4）；
   8. 四人桌一个三人寝（坐 3）；
   9. 六人桌一个三人寝（坐 3）；
   10. 四人桌一个二人寝（坐 2）；
   11. 六人桌一个二人寝（坐 2）。
4. 对所有 `i` 取最大值。`3+3` 的数量是唯一会显著改变后续分配的决策，枚举它之后，其余“同样是坐满 4/6 个座位”的选择彼此等价或已被更优组合覆盖，因此贪心不会漏掉最优解。
5. 该贪心已用穷举所有 `a2,a3,a4,b4,b6 ∈ [0,4]` 的 3125 组数据与精确暴力对拍，结果完全一致（0 处不一致）。

## 代码

```java
import java.io.*;
import java.util.*;

public class Main {
    static int greedy(int a2, int a3, int a4, int b4, int b6) {
        int best = 0;
        int maxPair = Math.min(b6, a3 / 2);
        for (int i = 0; i <= maxPair; i++) {
            int t6 = b6 - i, t4 = b4;
            int c2 = a2, c3 = a3 - 2 * i, c4 = a4;
            int cur = 6 * i;
            int k;
            // 六人桌 4+2
            k = Math.min(t6, Math.min(c4, c2));
            cur += 6 * k; t6 -= k; c4 -= k; c2 -= k;
            // 四人桌放一个四人寝
            k = Math.min(t4, c4);
            cur += 4 * k; t4 -= k; c4 -= k;
            // 四人桌 2+2
            k = Math.min(t4, c2 / 2);
            cur += 4 * k; t4 -= k; c2 -= 2 * k;
            // 六人桌 2+2+2
            k = Math.min(t6, c2 / 3);
            cur += 6 * k; t6 -= k; c2 -= 3 * k;
            // 六人桌 3+2
            k = Math.min(t6, Math.min(c3, c2));
            cur += 5 * k; t6 -= k; c3 -= k; c2 -= k;
            // 六人桌单个四人寝
            k = Math.min(t6, c4);
            cur += 4 * k; t6 -= k; c4 -= k;
            // 六人桌 2+2
            k = Math.min(t6, c2 / 2);
            cur += 4 * k; t6 -= k; c2 -= 2 * k;
            // 四人桌一个三人寝
            k = Math.min(t4, c3);
            cur += 3 * k; t4 -= k; c3 -= k;
            // 六人桌一个三人寝
            k = Math.min(t6, c3);
            cur += 3 * k; t6 -= k; c3 -= k;
            // 四人桌一个二人寝
            k = Math.min(t4, c2);
            cur += 2 * k; t4 -= k; c2 -= k;
            // 六人桌一个二人寝
            k = Math.min(t6, c2);
            cur += 2 * k; t6 -= k; c2 -= k;
            if (cur > best) best = cur;
        }
        return best;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(br.readLine().trim());
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < q; t++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a2 = Integer.parseInt(st.nextToken());
            int a3 = Integer.parseInt(st.nextToken());
            int a4 = Integer.parseInt(st.nextToken());
            int b4 = Integer.parseInt(st.nextToken());
            int b6 = Integer.parseInt(st.nextToken());
            sb.append(greedy(a2, a3, a4, b4, b6)).append('\n');
        }
        System.out.print(sb);
    }
}
```

## 复杂度

- 时间：`O(q · min(b6, a3/2))`，每组数据最多枚举 100 次，每次常数次贪心，总计约 `O(100q)`。
- 空间：`O(1)`。

## 易错点

1. **桌子可以空着**：不是必须坐满，答案是“最多能坐下多少人”，装不下的寝室可以放弃。
2. **六人桌的 `3+3` 与 `4+2` 必须枚举取舍**，只按一种优先级贪心会漏解（见思路中的反例）。
3. **容量限制**：`4+4=8 > 6`、`3+4=7 > 6` 都不能放进六人桌。
4. **四人寝与二人寝的搭配**：`4+2` 能坐满六人桌，应优先于把四人寝单独放。
5. **多组数据**：每组都要重新初始化所有计数，不能复用上一组的数组。

## 验证

将代码写入 `/tmp/verify2024/D/Main.java`，用 `javac --release 8 -encoding UTF-8` 编译，用题面样例输入运行，实际输出：

```text
6
10
```

与样例输出一致。另外编写了精确暴力程序 `/tmp/verify2024/D/Brute.java`（逐桌枚举所有合法摆法并记忆化），对所有 `a2,a3,a4,b4,b6 ∈ [0,4]` 的 3125 组输入与本题贪心对拍，输出 `mismatches=0`。
