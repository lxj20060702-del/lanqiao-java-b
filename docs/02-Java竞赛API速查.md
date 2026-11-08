# Java 竞赛 API 速查

考场可直接复制。所有代码兼容 JDK 8+。

## 1. 输入输出

```java
// 数据量小：Scanner
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
String s = sc.next();

// 数据量大（> 10^5）：用 templates/FastIO.java
FastIO in = new FastIO();
int n = in.nextInt();

// 输出：拼接后一次输出，避免频繁 IO
StringBuilder sb = new StringBuilder();
sb.append(ans).append('\n');
System.out.print(sb);
```

注意：`nextInt()` 之后紧接 `nextLine()` 会读到空行，需要多读一次 `sc.nextLine()`。

## 2. 数组与排序

```java
int[] a = new int[n];
Arrays.fill(a, -1);                     // 填充
Arrays.sort(a);                          // 升序
Integer[] b = {3, 1, 2};
Arrays.sort(b, (x, y) -> y - x);         // 降序（对象数组才能用比较器）
Arrays.sort(b, Comparator.reverseOrder());

// 二维数组按第一列排序
int[][] g = new int[n][2];
Arrays.sort(g, (x, y) -> Integer.compare(x[0], y[0]));

// 数组转 List
List<Integer> list = new ArrayList<>();
for (int x : a) list.add(x);

// 复制
int[] c = Arrays.copyOf(a, n);
int[] d = Arrays.copyOfRange(a, l, r);   // [l, r)
```

## 3. 集合

```java
List<Integer> list = new ArrayList<>();
list.add(1); list.get(0); list.size();
list.sort((x, y) -> x - y);

Map<String, Integer> cnt = new HashMap<>();
cnt.put("a", cnt.getOrDefault("a", 0) + 1);
cnt.containsKey("a");

Map<Integer, Integer> ordered = new TreeMap<>();  // 按 key 有序

Set<Integer> set = new HashSet<>();
set.add(1); set.contains(1);

// 栈和队列统一用 ArrayDeque
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1); stack.pop(); stack.peek();

Deque<Integer> queue = new ArrayDeque<>();
queue.offer(1); queue.poll(); queue.peek();

// 优先队列（小根堆）
PriorityQueue<Integer> pq = new PriorityQueue<>();
// 大根堆
PriorityQueue<Integer> maxPq = new PriorityQueue<>((x, y) -> y - x);
// 存 int[] 时注意比较器不能直接相减，用 Integer.compare 或 Long.compare
PriorityQueue<long[]> pq2 = new PriorityQueue<>((x, y) -> Long.compare(x[1], y[1]));
```

## 4. 字符串

```java
String s = "abc";
s.length(); s.charAt(0);
s.substring(1, 3);              // [1, 3)
s.indexOf("b");
s.toCharArray();
s.equals("abc");                // 不能写 ==
String.join(",", list);
String.format("%.2f", 1.234);   // 保留两位小数（四舍五入）

StringBuilder sb = new StringBuilder();
sb.append("a").append(1);
sb.reverse();
sb.toString();

// 字符与数字
int x = '7' - '0';
char c = (char) ('a' + 3);

// 按空白切分
String[] parts = s.trim().split("\\s+");
```

## 5. 大数

```java
BigInteger a = new BigInteger("12345678901234567890");
BigInteger b = BigInteger.valueOf(100);
a.add(b); a.subtract(b); a.multiply(b); a.divide(b); a.mod(b);
a.compareTo(b);                  // <0 表示 a < b
a.gcd(b);
a.pow(10);
a.toString(2);                   // 转二进制

BigDecimal c = new BigDecimal("1.1");
c.add(new BigDecimal("2.2"));    // 精确小数
c.setScale(2, RoundingMode.HALF_UP);
```

## 6. 进制与位运算

```java
Integer.toBinaryString(10);      // "1010"
Integer.toHexString(255);        // "ff"
Integer.parseInt("1010", 2);     // 10
Integer.toString(10, 3);         // "101"

n >> 1;                          // 除以 2
n << 1;                          // 乘 2
n & 1;                           // 奇偶
Integer.bitCount(n);             // 1 的个数
Integer.lowestOneBit(n);         // 最低位的 1
(1 << k)                         // 2^k，注意 k >= 31 时溢出
1L << k                          // 用 long 更安全
```

## 7. 数学

```java
Math.abs(-1);
Math.max(a, b); Math.min(a, b);
Math.sqrt(x);
Math.pow(x, y);                  // 返回 double，会丢精度
Math.floor / ceil / round;
Math.PI;

// 常见溢出场景：用 long
long ans = (long) a * b;
```

## 8. 日期时间（JDK 8+）

```java
LocalDate d = LocalDate.of(2024, 2, 29);
d.getYear(); d.getMonthValue(); d.getDayOfMonth();
d.getDayOfWeek().getValue();     // 1=周一, 7=周日
d.plusDays(1); d.isLeapYear();

// 两个日期相差天数
long days = ChronoUnit.DAYS.between(d1, d2);
```

手写闰年：`(y % 4 == 0 && y % 100 != 0) || y % 400 == 0`

## 9. 常用小技巧

```java
// 数组最大最小值
int mx = Arrays.stream(a).max().getAsInt();
int mn = Arrays.stream(a).min().getAsInt();

// 二维坐标四个方向
int[] dx = {-1, 1, 0, 0};
int[] dy = {0, 0, -1, 1};

// 字符频次统计
int[] cnt = new int[26];
for (char ch : s.toCharArray()) cnt[ch - 'a']++;

// 去重后保持顺序
Set<Integer> seen = new LinkedHashSet<>(list);

// 整除向上取整
int need = (sum + k - 1) / k;
```
