/**
 * 字符串模板：KMP 匹配、字符串哈希。
 */
public class StringUtil {

    /** KMP 前缀函数（next 数组）：pi[i] 表示 s[0..i] 的最长相等前后缀长度 */
    public static int[] prefixFunction(String s) {
        int n = s.length();
        int[] pi = new int[n];
        for (int i = 1; i < n; i++) {
            int j = pi[i - 1];
            while (j > 0 && s.charAt(i) != s.charAt(j)) {
                j = pi[j - 1];
            }
            if (s.charAt(i) == s.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        return pi;
    }

    /** 返回 pattern 在 text 中第一次出现的下标，找不到返回 -1 */
    public static int indexOf(String text, String pattern) {
        if (pattern.isEmpty()) {
            return 0;
        }
        int[] pi = prefixFunction(pattern);
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = pi[j - 1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            if (j == pattern.length()) {
                return i - pattern.length() + 1;
            }
        }
        return -1;
    }

    /** 统计 pattern 在 text 中出现次数（可重叠） */
    public static int countOccurrences(String text, String pattern) {
        if (pattern.isEmpty()) {
            return 0;
        }
        int[] pi = prefixFunction(pattern);
        int j = 0;
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = pi[j - 1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            if (j == pattern.length()) {
                count++;
                j = pi[j - 1];      // 允许重叠匹配
            }
        }
        return count;
    }

    // ---------------- 单哈希 ----------------

    public static final long BASE = 131;
    public static final long MOD = 1_000_000_007L;

    /** 预处理 s 的哈希前缀和幂次 */
    public static long[][] buildHash(String s) {
        int n = s.length();
        long[] h = new long[n + 1];
        long[] pow = new long[n + 1];
        pow[0] = 1;
        for (int i = 0; i < n; i++) {
            h[i + 1] = (h[i] * BASE + s.charAt(i)) % MOD;
            pow[i + 1] = pow[i] * BASE % MOD;
        }
        return new long[][]{h, pow};
    }

    /** 子串 s[l..r) 的哈希值 */
    public static long substringHash(long[] h, long[] pow, int l, int r) {
        long res = (h[r] - h[l] * pow[r - l]) % MOD;
        return res < 0 ? res + MOD : res;
    }

    public static boolean isPalindrome(String s) {
        int i = 0, j = s.length() - 1;
        while (i < j) {
            if (s.charAt(i++) != s.charAt(j--)) {
                return false;
            }
        }
        return true;
    }
}
