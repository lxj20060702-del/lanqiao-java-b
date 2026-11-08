# 2024 省赛 Java B 组 C - 分布式队列

> 题面来源：https://www.dotcpp.com/oj/problem3227.html。题面版权归蓝桥杯大赛组委会所有，此处仅用于学习交流。

## 题目

### 问题描述

小蓝最近学习了一种神奇的队列：分布式队列。简单来说，分布式队列包含 N 个节点（编号为 0 至 N − 1，其中 0 号为主节点），其中只有一个主节点，其余为副节点。主/副节点中都各自维护着一个队列，当往分布式队列中添加元素时都是由主节点完成的（每次都会添加元素到队列尾部）；副节点只负责同步主节点中的队列。可以认为主/副节点中的队列是一个长度无限的一维数组，下标为 0, 1, 2, 3, ...，同时副节点中的元素的同步顺序和主节点中的元素添加顺序保持一致。

由于副本的同步速度各异，因此为了保障数据的一致性，元素添加到主节点后，需要同步到所有的副节点后，才具有可见性。

给出一个分布式队列的运行状态，所有的操作都按输入顺序执行。你需要回答在某个时刻，队列中有多少个元素具有可见性。

### 输入格式

第一行包含一个整数 N，表示节点个数。接下来包含多行输入，每一行包含一个操作，操作类型共有以下三种：

1. `add element`：表示这是一个添加操作，将元素 element 添加到队列中；
2. `sync follower_id`：表示这是一个同步操作，follower_id 号副节点会从主节点中同步下一个自己缺失的元素；
3. `query`：查询操作，询问当前分布式队列中有多少个元素具有可见性。

### 输出格式

对于每一个 query 操作，输出一行，包含一个整数表示答案。

### 样例输入

```text
3
add 1
add 2
query
add 1
sync 1
sync 1
sync 2
query
sync 1
query
sync 2
sync 2
sync 1
query
```

### 样例输出

```text
0
1
1
3
```

### 样例说明

- 执行到第一个 query 时，队列内容如下：

  ```text
  0：[1,2]
  1：[]
  2：[]
  ```

  两个副节点中都无元素，因此答案为 0。

- 执行到第二个 query 时，队列内容如下：

  ```text
  0：[1,2,1]
  1：[1,2]
  2：[1]
  ```

  只有下标为 0 的元素被所有节点同步，因此答案为 1。

- 执行到第三个 query 时，队列内容如下：

  ```text
  0：[1,2,1]
  1：[1,2,1]
  2：[1]
  ```

  只有下标为 0 的元素被所有节点同步，因此答案为 1。

- 执行到第四个 query 时，队列内容如下：

  ```text
  0：[1,2,1]
  1：[1,2,1]
  2：[1,2,1]
  ```

  三个元素都被所有节点同步，因此答案为 3。

### 评测用例规模与约定

- 对于 30% 的评测用例：1 ≤ 输入的操作数 ≤ 100。
- 对于 100% 的评测用例：1 ≤ 输入的操作数 ≤ 2000，1 ≤ N ≤ 10，1 ≤ follower_id < N，0 ≤ element ≤ 10^5。

## 思路

1. 所有节点中的队列都是主节点队列的前缀，且元素顺序完全一致。因此一个元素“具有可见性”当且仅当它的下标小于所有节点的队列长度。
2. 于是某时刻可见元素个数 = `min(主节点长度, 所有副节点长度)`。主节点的长度就是 `add` 操作的次数。
3. 维护 `master`（add 次数）和数组 `len[i]`（第 i 个副节点已同步的元素个数）：
   - `add`：`master++`；
   - `sync id`：若 `len[id] < master`，则 `len[id]++`（同步下一个缺失元素）；若已同步完则不产生变化；
   - `query`：输出 `min(master, len[1], ..., len[N-1])`。
4. 正确性：副节点的队列始终是主队列的前缀，长度即其已同步的元素个数；所有节点都拥有的前缀长度就是各长度的最小值，这正是可见元素的数量。

## 代码

```java
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        int master = 0;              // 主节点队列长度
        int[] len = new int[n];      // 各副节点已同步的元素个数
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("add")) {
                master++;
            } else if (line.startsWith("sync")) {
                int id = Integer.parseInt(line.substring(5).trim());
                if (len[id] < master) len[id]++;
            } else { // query
                int mn = master;
                for (int i = 1; i < n; i++) {
                    if (len[i] < mn) mn = len[i];
                }
                sb.append(mn).append('\n');
            }
        }
        System.out.print(sb);
    }
}
```

## 复杂度

- 时间：`O(M · N)`，其中 M 为操作数（≤ 2000），N ≤ 10；每个 query 需要扫一遍副节点。
- 空间：`O(N)`。

## 易错点

1. **可见数量的定义**：是“所有节点都已同步的前缀长度”，即最小长度，而不是主队列长度。
2. **sync 的边界**：副节点已追上主节点时再 sync 应视为无操作，不能让它超过 `master`；否则会多算可见元素。
3. **元素值无关**：只需记录长度，不需要真的存储元素值。
4. **输入读到 EOF**：操作行数没有单独给出，要一直读到文件结束；同时注意跳过空行。
5. **节点编号从 0 开始**：副节点是 1 ~ N−1，主节点 0 不参与 `sync`。

## 验证

将代码写入 `/tmp/verify2024/C/Main.java`，用 `javac --release 8 -encoding UTF-8` 编译，用题面样例输入运行，实际输出：

```text
0
1
1
3
```

与样例输出完全一致。
