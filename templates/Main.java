import java.io.*;
import java.util.*;

/**
 * 蓝桥杯提交用主类骨架。
 *
 * 注意：
 * 1. 提交时代码里的类名必须是 Main
 * 2. 不要写 package 语句
 * 3. 本地测试时可配合 templates/FastIO.java 一起编译
 */
public class Main {

    public static void main(String[] args) throws IOException {
        FastIO in = new FastIO();
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int T = in.nextInt();          // 只有一组数据时删掉这一行
        while (T-- > 0) {
            int n = in.nextInt();
            long ans = 0;
            for (int i = 0; i < n; i++) {
                ans += in.nextLong();
            }
            out.println(ans);
        }

        out.flush();
    }
}
