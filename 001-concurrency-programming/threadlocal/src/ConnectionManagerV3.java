/**
 * 那么这种情况下使用ThreadLocal是再适合不过的了，因为ThreadLocal在每个线程中对该变量会创建一个副本，
 * 即每个线程内部都会有一个该变量，且在线程内部任何地方都可以使用，线程之间互不影响，这样一来就不存在线程安全问题，也不会严重影响程序执行性能。
 * <p>
 * 使用ThreadLocal时，它存在的问题是：
 * 虽然ThreadLocal能够解决上面说的问题，但是由于在每个线程中都创建了副本，
 * 所以要考虑它对资源的消耗，比如内存的占用会比不使用ThreadLocal要大。
 */

// 面通过一个例子来证明通过ThreadLocal能达到在每个线程中创建变量副本的效果：

class Test {
    static ThreadLocal<Long> longLocal = new ThreadLocal<>();
    static ThreadLocal<String> stringLocal = new ThreadLocal<>();

    public static void set(long l, String str) {
        longLocal.set(l);
        stringLocal.set(str);
    }

    public static long getLong() {
        return longLocal.get();
    }

    public static String getString() {
        return stringLocal.get();
    }
}

public class ConnectionManagerV3 {

    public static void main(String[] args) {

        Test.set(Thread.currentThread().getId(),Thread.currentThread().getName());

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Test.set(Thread.currentThread().getId(),Thread.currentThread().getName());
                System.out.println(Test.getLong()+"..."+Test.getString());
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Test.set(Thread.currentThread().getId(),Thread.currentThread().getName());
                System.out.println(Test.getLong()+"..."+Test.getString());
            }
        });

        // 可以看出，不同线程中，通过同一个threadLocal获取到的值是不一样的。
        thread1.start();
        thread2.start();
        System.out.println(Test.getLong()+"..."+Test.getString());
    }

}
