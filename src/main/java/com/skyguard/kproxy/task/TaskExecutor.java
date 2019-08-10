package com.skyguard.kproxy.task;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskExecutor {

    private ListeningExecutorService service;

    private ThreadPoolExecutor threadPoolExecutor;

    private TaskExecutor() {

        int nThreads=Runtime.getRuntime().availableProcessors();

        threadPoolExecutor=new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadPoolExecutor.DiscardPolicy());

        service = MoreExecutors
                .listeningDecorator(threadPoolExecutor);

    }

    private static class SingletonHolder {
        static final TaskExecutor instance = new TaskExecutor();
    }

    public static TaskExecutor getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * @return the service
     */
    public ListeningExecutorService getService() {
        return service;
    }

    /**
     * 获取活跃的线程数
     * @return
     */
    public int getActiveCount(){
        return threadPoolExecutor.getActiveCount();
    }





}
