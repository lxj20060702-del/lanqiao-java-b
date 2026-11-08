import java.util.*;

/**
 * 数论常用模板：gcd、exgcd、快速幂、逆元、素数筛、质因数分解、组合数。
 */
public class MathUtil {

    public static long gcd(long a, long b) {
        return b == 0 ? Math.abs(a) : gcd(b, a % b);
    }

    /** 最小公倍数：先除后乘，避免溢出 */
    public static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    /** 扩展欧几里得：返回 {g, x, y}，满足 a*x + b*y = g */
    public static long[] exgcd(long a, long b) {
        if (b == 0) {
            return new long[]{a, 1, 0};
        }
        long[] r = exgcd(b, a % b);
        return new long[]{r[0], r[2], r[1] - (a / b) * r[2]};
    }

    /** 快速幂：base^exp % mod */
    public static long modPow(long base, long exp, long mod) {
        long result = 1 % mod;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = result * base % mod;
            }
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }

    /** 逆元：mod 为质数且 a 与 mod 互质（费马小定理） */
    public static long modInverse(long a, long mod) {
        return modPow(a, mod - 2, mod);
    }

    /** 判断素性，O(√n) */
    public static boolean isPrime(long n) {
        if (n < 2) {
            return false;
        }
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /** 埃氏筛：返回长度为 n+1 的数组，isPrime[i] 表示 i 是否为素数 */
    public static boolean[] sieve(int n) {
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, 2, n + 1, true);
        for (int i = 2; (long) i * i <= n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        return isPrime;
    }

    /** 质因数分解：返回「质因子 -> 指数」，按因子升序 */
    public static Map<Long, Integer> primeFactors(long n) {
        Map<Long, Integer> map = new LinkedHashMap<>();
        for (long i = 2; i * i <= n; i++) {
            while (n % i == 0) {
                map.merge(i, 1, Integer::sum);
                n /= i;
            }
        }
        if (n > 1) {
            map.merge(n, 1, Integer::sum);
        }
        return map;
    }

    // ---------------- 组合数 ----------------

    private static long[] fact;
    private static long[] invFact;

    /** 预处理 1! ~ n! 及其逆元，mod 必须是质数 */
    public static void initComb(int n, long mod) {
        fact = new long[n + 1];
        invFact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % mod;
        }
        invFact[n] = modPow(fact[n], mod - 2, mod);
        for (int i = n; i >= 1; i--) {
            invFact[i - 1] = invFact[i] * i % mod;
        }
    }

    /** C(n, k) % mod */
    public static long comb(int n, int k, long mod) {
        if (k < 0 || k > n) {
            return 0;
        }
        return fact[n] * invFact[k] % mod * invFact[n - k] % mod;
    }

    /** 排列数 A(n, k) % mod */
    public static long perm(int n, int k, long mod) {
        if (k < 0 || k > n) {
            return 0;
        }
        return fact[n] * invFact[n - k] % mod;
    }
}
