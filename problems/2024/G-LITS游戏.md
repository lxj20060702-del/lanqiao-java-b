# 2024 省赛 Java B 组 G - LITS 游戏

> 题面来源：https://www.dotcpp.com/oj/problem3231.html。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 问题描述

俄罗斯方块是一款风靡全球的游戏，在游戏中有多种方格图案，我们只关注这四个经典的方块图案：LITS，如下图所示：

（原题此处为 L、I、T、S 四种方块的图片，无法直接转写，具体形状待核对，见来源链接；下文按标准 LITS 四连块理解，题面样例已验证该理解可解释题意。）

LITS 这四种方块都是由四个相同大小的小方块组成的。

现在给出一副大小为 N × N 的格子图，每个格子上都有一个数字 0/1，如果格子处的数字为 1 说明这个格子上有一个小方块，数字为 0 则没有。你需要判断从这个格子图上是否可以找出 LITS 四个方块图案（每个方块图案之间都是独立的，不存在不同的图案公用同一个小方块的情况）。其中 LITS 方块的形状旋转任意个 90 度都是合法的，但不允许翻转。

### 输入格式

第一行一个整数 T，表示有 T 组数据。

每组数据的第一行包含一个整数 N，表示格子图大小。

接下来输入 N 行，每行 N 个值为 0/1 的整数，表示格子布局。

### 输出格式

对于每组数据，输出一行包含一个字符串。如果此组数据满足题意则输出 “Yes”，否则输出 “No”。

### 样例输入

```text
2
5
1 1 1 1 1
1 0 1 1 0
1 0 0 0 1
1 0 1 0 1
1 1 1 1 1
5
1 0 0 1 1
1 1 1 1 1
1 1 1 1 0
1 1 1 0 1
0 1 1 1 1
```

### 样例输出

```text
No
Yes
```

### 样例说明

对于样例中的第二个格子图，一种 LITS 的摆放方案如下：

```text
1 0 0 1 1
L S T T T
L S S T 0
L L S 0 1
0 I I I I
```

### 评测用例规模与约定

- 对于 30% 的评测用例：1 ≤ N ≤ 5。
- 对于 60% 的评测用例：1 ≤ N ≤ 10。
- 对于 100% 的评测用例：1 ≤ T ≤ 10，1 ≤ N ≤ 50。

## 思路

1. 四种方块都是四连块，先写出一个基础形状，再旋转 0°/90°/180°/270° 得到全部合法朝向（**只旋转、不翻转**），并对坐标归一化去重：
   - L：`(0,0),(1,0),(2,0),(2,1)`，共 4 种朝向；
   - I：`(0,0),(0,1),(0,2),(0,3)`，共 2 种朝向；
   - T：`(0,0),(0,1),(0,2),(1,1)`，共 4 种朝向；
   - S：`(0,1),(0,2),(1,0),(1,1)`，共 2 种朝向（其镜像 Z 非法）。
2. 对每种朝向，枚举它在 N×N 网格中的所有平移位置，若 4 个格子全为 1，就记录为一个候选摆放（把格子编码为 `r*N+c`）。
3. 对四种方块做回溯搜索：依次尝试放置 L、I、T、S，要求四块的格子互不重叠。为减少搜索量，先按“候选数量从少到多”排序再搜索。
4. 搜索到四块都放下时输出 `Yes`，全部尝试失败输出 `No`。多余的 1 格子允许存在（题面只要求“找出四个图案”，不要求覆盖所有 1；样例 2 中有 20 个 1，而四块只占 16 个）。
5. 正确性：候选集合穷尽了所有合法朝向和位置；回溯穷尽了所有不重叠的四块组合，因此只要存在合法摆放一定能找到。

## 代码

```java
import java.io.*;
import java.util.*;

public class Main {
    static int n;
    static int[][] g;
    static boolean[][] used;
    static ArrayList<int[]>[] cand; // cand[s]：第 s 种方块的所有合法摆放（4 个格子编码）
    static int[] order;             // 按候选数从小到大搜索

    // 生成一种基础形状的所有旋转（不允许翻转），去重后返回每种旋转的 4 个 (r,c)
    static ArrayList<int[][]> rotations(int[][] base) {
        ArrayList<int[][]> res = new ArrayList<int[][]>();
        int[][] cur = base;
        for (int t = 0; t < 4; t++) {
            int minR = Integer.MAX_VALUE, minC = Integer.MAX_VALUE;
            for (int[] p : cur) {
                minR = Math.min(minR, p[0]);
                minC = Math.min(minC, p[1]);
            }
            int[][] norm = new int[4][2];
            for (int i = 0; i < 4; i++) {
                norm[i][0] = cur[i][0] - minR;
                norm[i][1] = cur[i][1] - minC;
            }
            Arrays.sort(norm, new Comparator<int[]>() {
                public int compare(int[] a, int[] b) {
                    if (a[0] != b[0]) return a[0] - b[0];
                    return a[1] - b[1];
                }
            });
            boolean dup = false;
            for (int[][] e : res) {
                boolean same = true;
                for (int i = 0; i < 4; i++)
                    if (e[i][0] != norm[i][0] || e[i][1] != norm[i][1]) { same = false; break; }
                if (same) { dup = true; break; }
            }
            if (!dup) res.add(norm);
            // 旋转
            int[][] nxt = new int[4][2];
            for (int i = 0; i < 4; i++) {
                nxt[i][0] = cur[i][1];
                nxt[i][1] = -cur[i][0];
            }
            cur = nxt;
        }
        return res;
    }

    static boolean dfs(int idx) {
        if (idx == 4) return true;
        int s = order[idx];
        for (int[] place : cand[s]) {
            boolean ok = true;
            for (int code : place) {
                int r = code / n, c = code % n;
                if (used[r][c]) { ok = false; break; }
            }
            if (!ok) continue;
            for (int code : place) used[code / n][code % n] = true;
            if (dfs(idx + 1)) return true;
            for (int code : place) used[code / n][code % n] = false;
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine().trim());
        StringBuilder out = new StringBuilder();

        int[][][] bases = new int[4][][];
        bases[0] = new int[][]{{0, 0}, {1, 0}, {2, 0}, {2, 1}};       // L
        bases[1] = new int[][]{{0, 0}, {0, 1}, {0, 2}, {0, 3}};       // I
        bases[2] = new int[][]{{0, 0}, {0, 1}, {0, 2}, {1, 1}};       // T
        bases[3] = new int[][]{{0, 1}, {0, 2}, {1, 0}, {1, 1}};       // S

        while (T-- > 0) {
            n = Integer.parseInt(br.readLine().trim());
            g = new int[n][n];
            for (int i = 0; i < n; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < n; j++) g[i][j] = Integer.parseInt(st.nextToken());
            }

            cand = new ArrayList[4];
            for (int s = 0; s < 4; s++) {
                cand[s] = new ArrayList<int[]>();
                ArrayList<int[][]> rots = rotations(bases[s]);
                for (int[][] shape : rots) {
                    int maxR = 0, maxC = 0;
                    for (int[] p : shape) {
                        maxR = Math.max(maxR, p[0]);
                        maxC = Math.max(maxC, p[1]);
                    }
                    for (int r = 0; r + maxR < n; r++) {
                        for (int c = 0; c + maxC < n; c++) {
                            int[] place = new int[4];
                            boolean ok = true;
                            for (int i = 0; i < 4; i++) {
                                int rr = r + shape[i][0], cc = c + shape[i][1];
                                if (g[rr][cc] != 1) { ok = false; break; }
                                place[i] = rr * n + cc;
                            }
                            if (ok) cand[s].add(place);
                        }
                    }
                }
            }

            boolean possible = true;
            for (int s = 0; s < 4; s++) if (cand[s].isEmpty()) possible = false;
            if (!possible) {
                out.append("No\n");
                continue;
            }

            final ArrayList<int[]>[] cc = cand;
            Integer[] idx = {0, 1, 2, 3};
            Arrays.sort(idx, new Comparator<Integer>() {
                public int compare(Integer a, Integer b) {
                    return cc[a].size() - cc[b].size();
                }
            });
            order = new int[4];
            for (int i = 0; i < 4; i++) order[i] = idx[i];

            used = new boolean[n][n];
            out.append(dfs(0) ? "Yes\n" : "No\n");
        }
        System.out.print(out);
    }
}
```

## 复杂度

- 预处理：每种形状的候选摆放不超过 `4N^2` 个（4 种朝向 × N² 个平移），对四种形状共 `O(N^2)`。
- 搜索：理论最坏为四种形状候选数的乘积 `O(N^8)`，但按候选数排序并做不重叠剪枝后实际远小于此；N ≤ 50、T ≤ 10 时对稠密/随机数据均能在 0.1 秒内完成。空间 `O(N^2)`（网格、访问标记与候选表）。

## 易错点

1. **只能旋转、不能翻转**：S 与 Z 互为镜像，若把 Z 也加入候选会得到错误答案；L 也同理。
2. **四种方块各用一次**，不能重复使用同一种，也不能只用其中几种。
3. **允许有多余的 1 格子**：不要求覆盖所有 1；样例 2 中就有 4 个 1 未被使用。
4. **输入是空格分隔的整数**，不是一整行字符串；用 `StringTokenizer` 逐 token 读。
5. **边界检查**：枚举平移时用 `r + maxR < n`、`c + maxC < n`，避免越界。

## 验证

将代码写入 `/tmp/verify2024/G/Main.java`，用 `javac --release 8 -encoding UTF-8` 编译，用题面样例输入运行，实际输出：

```text
No
Yes
```

与样例输出一致。另构造“四块分别放在互不相邻位置”的 10×10 用例，输出 `Yes`；对 50×50 全 1 网格及 10 组随机 50×50 网格（密度 0.3~0.9）测试，输出均正确且总耗时约 0.1 秒。
