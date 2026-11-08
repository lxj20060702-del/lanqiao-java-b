import java.io.*;
import java.util.*;

/**
 * 快速读入：比 Scanner 快一个量级，适合 10^5 以上的输入。
 * 用法：
 *   FastIO in = new FastIO();
 *   int n = in.nextInt();
 *   long x = in.nextLong();
 *   String s = in.next();
 */
public class FastIO {

    private final BufferedReader br;
    private StringTokenizer st;

    public FastIO() {
        this(System.in);
    }

    public FastIO(InputStream in) {
        br = new BufferedReader(new InputStreamReader(in));
    }

    /** 读下一个以空白分隔的 token，读到 EOF 返回 null */
    public String next() {
        while (st == null || !st.hasMoreTokens()) {
            String line;
            try {
                line = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) {
                return null;
            }
            st = new StringTokenizer(line);
        }
        return st.nextToken();
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    /**
     * 读一整行。
     * 注意：如果前面刚用过 nextInt() 等按 token 读取的方法，
     * 当前行可能还有残留内容，需要自行处理。
     */
    public String nextLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** 判断是否还有数据（多组输入到 EOF 时可用） */
    public boolean hasNext() {
        return next() != null;
    }
}
