package com.deep007.goniub.util;

import java.io.Closeable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 可计数的线程池
 */
public class CountableThreadPool implements Closeable {

	private final int threadNum;

	private ScheduledThreadPoolExecutor executorService;

	public CountableThreadPool(int corePoolSize) {
		this.threadNum = corePoolSize;
		executorService = new ScheduledThreadPoolExecutor(threadNum + 10);
	}

	public int getThreadAlive() {
		return executorService.getActiveCount();
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void waitIdleThread() throws Exception {
		long startTime = System.currentTimeMillis();
		while (getThreadAlive() >= 500) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - startTime > 1000 * 60 * 30) {
				throw new Exception("超过半小时无可用线程,threadAlive:" + getThreadAlive());
			}
		}
	}

	public void execute(final Runnable runnable) {
		executorService.execute(runnable);
	}

	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	public void shutdown() {
		executorService.shutdown();
	}

	public int getIdleThreadCount() {
		return executorService.getMaximumPoolSize() - getThreadAlive();
	}

	@Override
	public void close() {
		executorService.shutdown();
		try {
			while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
				// The thread pool has no closing
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
