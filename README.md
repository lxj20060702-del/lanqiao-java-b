# 蓝桥杯 Java 大学 B 组备赛

个人备赛仓库：**历年真题题解 + 常用算法模板 + 考点笔记 + 进度追踪**。

目标：省赛稳拿省一，国赛冲国奖。

## 目录结构

```
lanqiao-java-b/
├── docs/                 备赛文档
│   ├── 00-备赛路线.md         阶段计划与每周安排
│   ├── 01-考点清单.md         按模块拆解的考点与掌握度
│   ├── 02-Java竞赛API速查.md  考场可直接抄的 API
│   ├── 03-考场策略与检查清单.md
│   └── 04-DP模板速查.md       背包 / LIS / LCS / 区间DP
├── templates/            Java 模板（可直接复制进考场）
│   ├── Main.java            提交用主类骨架
│   ├── FastIO.java          快速读入
│   ├── DSU.java             并查集
│   ├── Fenwick.java         树状数组
│   ├── SegmentTree.java     线段树（区间加 + 区间和）
│   ├── GraphTemplates.java  BFS / DFS / Dijkstra / Kruskal / 拓扑
│   ├── MathUtil.java        gcd / 快速幂 / 素数筛 / 组合数
│   ├── StringUtil.java      KMP / 字符串哈希
│   └── BinarySearch.java    二分查找与二分答案
├── problems/             真题题解
│   ├── TEMPLATE.md          题解笔记模板
│   ├── 2026/
│   └── 2024/
└── .gitignore
```

## 快速开始

```bash
# 编译全部模板
javac -encoding UTF-8 -d out templates/*.java

# 本地跑一道题（把题目输入放在 in.txt）
java -cp out Main < in.txt
```

**注意**：蓝桥杯提交时主类必须叫 `Main`，且不要带 `package` 语句。

## 模板清单

| 模板 | 用途 | 复杂度 |
|---|---|---|
| `FastIO` | 大数据量输入，比 `Scanner` 快一个量级 | - |
| `DSU` | 并查集，连通性 / Kruskal | 近似 O(1) |
| `Fenwick` | 单点改 + 前缀和 / 区间和 | O(log n) |
| `SegmentTree` | 区间加 + 区间求和 | O(log n) |
| `GraphTemplates` | 邻接表建图、遍历、最短路、最小生成树、拓扑排序 | 见注释 |
| `MathUtil` | 最大公约数、快速幂、逆元、素数筛、组合数 | - |
| `StringUtil` | KMP 匹配、字符串哈希 | O(n+m) |
| `BinarySearch` | 手写 lower/upper bound、二分答案 | O(log n) |

## 真题进度

省赛真题按年份刷，做完在对应目录写题解（用 `problems/TEMPLATE.md`）。

| 年份 | 届次 | 省赛 | 国赛 | 备注 |
|---|---|---|---|---|
| 2026 | 第十七届 | □ | □ | |
| 2025 | 第十六届 | □ | □ | |
| 2024 | 第十五届 | □ | □ | 8 题：报数游戏、类斐波那契循环数、分布式队列、食堂、最优分组、星际旅行、LITS游戏、拼十字 |
| 2023 | 第十四届 | □ | □ | |
| 2022 | 第十三届 | □ | □ | |
| 2021 | 第十二届 | □ | □ | |
| 2020 | 第十一届 | □ | □ | |
| 2019 | 第十届 | □ | □ | |
| 2018 | 第九届 | □ | □ | |
| 2017 | 第八届 | □ | □ | |
| 2016 | 第七届 | □ | □ | |
| 2015 | 第六届 | □ | □ | |
| 2014 | 第五届 | □ | □ | |
| 2013 | 第四届 | □ | □ | |

## 每日流程

1. 30 分钟：默写一个模板（不看源文件，写完再对答案）
2. 90 分钟：限时做 1 道真题
3. 30 分钟：写题解到 `problems/<年份>/`，记录错因

## 参考资源

- 蓝桥杯官网：<https://dasai.lanqiao.cn/>
- 蓝桥云课题库：<https://www.lanqiao.cn/problems/>
- Dotcpp 蓝桥杯真题：<https://www.dotcpp.com/oj/>
- 题解仓库参考：[liuchuo/Lanqiao](https://github.com/liuchuo/Lanqiao)、[hujuny/LanQiaoCup](https://github.com/hujuny/LanQiaoCup)
