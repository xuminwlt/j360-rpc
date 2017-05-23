package me.j360.rpc.server;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Package: me.j360.rpc.server
 * User: min_xu
 * Date: 2017/5/23 下午2:46
 * 说明：方法管理器
 */
public class RPCServiceCallManager {


    private static ThreadPoolExecutor executor;
    private static BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>();

    public static void init() {
        executor = new ThreadPoolExecutor(
                16,
                16,
                60L, TimeUnit.SECONDS, blockingQueue,
                new CustomThreadFactory("worker-thread"));
    }

    public static void execute(RPCServiceTask task) {
        executor.submit(task);
    }

    public static class CustomThreadFactory implements ThreadFactory {
        private AtomicInteger threadNumber = new AtomicInteger(1);
        private String namePrefix;
        private ThreadGroup group;

        public CustomThreadFactory(String namePrefix) {
            SecurityManager s = System.getSecurityManager();
            this.group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            this.namePrefix = namePrefix + "-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            t.setDaemon(true);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }

    }
}
