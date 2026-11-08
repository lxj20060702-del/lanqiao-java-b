# 2026 省赛 Java B 组 C - 游戏指令解析器

> 题面来源：https://www.dotcpp.com/oj/problem3374.html 。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 题目描述

某款冒险游戏维护了一个指令库，其中包含 $n$ 条互不相同的完整指令。

玩家会依次输入 $m$ 条字符串作为输入指令。输入指令可以是完整指令本身，也可以是某条完整指令的前缀（从第 1 个字符开始连续匹配）。

对于每条输入指令 $s$，定义它与指令库的匹配结果如下：

- 若存在且仅存在一条完整指令 $c$ 满足：$s$ 是 $c$ 的前缀，则认为 $s$ 唯一匹配到 $c$，系统执行该指令，并输出 $c$。
- 若有两条或以上的完整指令都以 $s$ 作为前缀，则认为 $s$ 多重匹配，输出 `ambiguous`。
- 若指令库中不存在任何以 $s$ 为前缀的完整指令，则认为 $s$ 无法匹配，输出 `unknown`。

现在，请你对玩家输入的每条指令，按上述规则输出其匹配结果。

### 输入格式

第一行包含两个整数 $n,m$，分别表示指令库中的完整指令数量，以及玩家输入指令的数量。

接下来 $n$ 行，每行一个字符串，表示一条完整指令。

接下来 $m$ 行，每行一个字符串，表示一条输入指令。

### 输出格式

输出 $m$ 行。第 $i$ 行对应第 $i$ 条输入指令的匹配结果：

- 唯一匹配：输出对应的完整指令；
- 多重匹配：输出 `ambiguous`；
- 无法匹配：输出 `unknown`。

### 样例输入 1

```
6 5
attack
defend
move
magic
examine
exit
att
def
mov
ex
e
```

### 样例输出 1

```
attack
defend
move
ambiguous
ambiguous
```

### 样例说明 1

指令库为：attack、defend、move、magic、examine、exit。

- `att` 是 `attack` 的前缀，且仅匹配这一条，输出 `attack`。
- `def` 仅匹配 `defend`，输出 `defend`。
- `mov` 仅匹配 `move`，输出 `move`。
- `ex` 同时匹配 `examine` 与 `exit`，属于多重匹配，输出 `ambiguous`。
- `e` 同时匹配 `examine` 与 `exit`，输出 `ambiguous`。

### 样例输入 2

```
4 4
open
close
operate
unlock
op
clo
un
cast
```

### 样例输出 2

```
ambiguous
close
unlock
unknown
```

### 样例说明 2

- `op` 同时匹配 `open` 与 `operate`，输出 `ambiguous`。
- `clo` 仅匹配 `close`，输出 `close`。
- `un` 仅匹配 `unlock`，输出 `unlock`。
- `cast` 无法匹配任何完整指令，输出 `unknown`。

### 评测用例规模与约定

对于 $30\%$ 的评测用例，$n\le10$，$m\le10$；

对于 $60\%$ 的评测用例，$n\le50$，$m\le50$；

对于所有评测用例，$1\le n\le100$，$1\le m\le100$；

保证所有指令名称和输入指令的长度均在 $[1,20]$ 之间，所有字符串仅包含小写英文字母，指令库中的完整指令互不相同。

## 思路

1. 把所有完整指令插入一棵 **Trie（字典树）**，每个结点代表一个前缀。
2. 在每个结点上记录 `sub`：**该结点子树中包含多少条完整指令**（即有多少个指令以这个前缀开头）。
   - 先记录每个结点是否是某条指令的结尾（`endCnt`），然后按结点编号从大到小回推：子结点编号一定大于父结点，故 `sub[u] += sub[child]`，最后 `sub[u] += endCnt[u]`。
3. 对每条输入串 $s$ 在 Trie 上走：
   - 中途某个字符没有对应边 → 不存在任何以 $s$ 为前缀的指令，输出 `unknown`。
   - 走完后 `sub[p] == 0` → 能走到但子树里没有完整指令，仍是 `unknown`。
   - `sub[p] >= 2` → 至少两条指令以 $s$ 为前缀，输出 `ambiguous`。
   - `sub[p] == 1` → 从当前结点唯一地向下走到那个终止结点，输出该指令。
4. **正确性**：Trie 中从根到某结点的路径恰为该前缀，$s$ 的所有可能补全都落在该结点的子树内，因此子树的终止结点个数就是匹配的完整指令条数。三种情况与题目规则一一对应。

## 代码

```java
import java.io.*;
import java.util.*;

public class Main {
    static int[][] ch;
    static int[] sub;
    static int[] endCnt;
    static String[] word;
    static int tot;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        ch = new int[n * 20 + 5][26];
        sub = new int[n * 20 + 5];
        endCnt = new int[n * 20 + 5];
        word = new String[n * 20 + 5];
        tot = 1;
        for (int i = 0; i < n; i++) insert(br.readLine().trim());
        // 每个结点的子树中包含多少条完整指令（子结点编号一定大于父结点）
        for (int p = tot - 1; p >= 1; p--) {
            for (int c = 0; c < 26; c++) {
                if (ch[p][c] != 0) sub[p] += sub[ch[p][c]];
            }
            sub[p] += endCnt[p];
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            sb.append(query(br.readLine().trim())).append('\n');
        }
        System.out.print(sb);
    }

    static void insert(String s) {
        int p = 0;
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a';
            if (ch[p][c] == 0) ch[p][c] = tot++;
            p = ch[p][c];
        }
        endCnt[p]++;
        word[p] = s;
    }

    static String query(String s) {
        int p = 0;
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a';
            if (ch[p][c] == 0) return "unknown";
            p = ch[p][c];
        }
        if (sub[p] == 0) return "unknown";
        if (sub[p] >= 2) return "ambiguous";
        while (endCnt[p] == 0) {
            for (int c = 0; c < 26; c++) {
                if (ch[p][c] != 0 && sub[ch[p][c]] > 0) { p = ch[p][c]; break; }
            }
        }
        return word[p];
    }
}
```

## 复杂度

- 时间：建树 $O(\sum |c_i|)$，查询 $O(\sum |s_i| + 20)$，总计 $O(nL+mL)$，其中 $L\le20$。
- 空间：$O(nL\cdot26)$ 个 `int`，即约 $100\times20\times26$ 个槽位。

## 易错点

1. 不能只统计“以该结点结尾的指令数”，要统计**整个子树**中的终止结点数，否则 `att` 会误判成 `unknown`。
2. 空匹配（输入串走到根）不存在，因为输入长度 $\ge1$；但要注意“走得到但子树为空”这种情况仍应输出 `unknown`。
3. 字符串只含小写字母，映射 `c - 'a'` 即可，不要用 `HashMap` 增加常数。
4. 结点总数上界是 $n\times20+1$，开数组时不要只开 $n$。
5. 输出用 `StringBuilder` 一次性输出，避免大量 `System.out.println`。

## 验证

程序在样例 1 上运行输出：

```
attack
defend
move
ambiguous
ambiguous
```

在样例 2 上运行输出：

```
ambiguous
close
unlock
unknown
```

与题面给出的样例输出完全一致。
