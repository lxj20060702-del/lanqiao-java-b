/**
 * 二分模板：手写 lower/upper bound + 二分答案框架。
 */
public class BinarySearch {

    /** 第一个 >= target 的下标，不存在返回 n */
    public static int lowerBound(int[] a, int target) {
        int l = 0, r = a.length;
        while (l < r) {
            int mid = (l + r) >>> 1;
            if (a[mid] < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    /** 第一个 > target 的下标，不存在返回 n */
    public static int upperBound(int[] a, int target) {
        int l = 0, r = a.length;
        while (l < r) {
            int mid = (l + r) >>> 1;
            if (a[mid] <= target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    /** 统计 target 出现次数 */
    public static int count(int[] a, int target) {
        return upperBound(a, target) - lowerBound(a, target);
    }

    /**
     * 二分答案框架：把数组分成 k 段连续子数组，使「段和的最大值」最小。
     * 返回这个最小值。
     */
    public static long minMaxSplit(int[] a, int k) {
        long lo = 0, hi = 0;
        for (int x : a) {
            lo = Math.max(lo, x);
            hi += x;
        }
        while (lo < hi) {
            long mid = (lo + hi) >>> 1;
            if (canSplit(a, mid, k)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    /** 每段和不超过 limit 时，能否用不超过 k 段装下（要求元素非负） */
    private static boolean canSplit(int[] a, long limit, int k) {
        int parts = 1;
        long sum = 0;
        for (int x : a) {
            if (sum + x > limit) {
                parts++;
                sum = x;
            } else {
                sum += x;
            }
        }
        return parts <= k;
    }
}
