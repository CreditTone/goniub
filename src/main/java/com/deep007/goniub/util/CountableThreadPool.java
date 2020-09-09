package com.deep007.goniub.util;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 可计数的线程池
 */
public class CountableThreadPool{

	private final int threadNum;

	private ScheduledThreadPoolExecutor executorService;
	
	private final Queue<Runnable> runnables = new LinkedBlockingDeque<>();
	
	private final Thread readThread;
	
	private boolean shutdown = false;

	public CountableThreadPool(int corePoolSize) {
		this.threadNum = corePoolSize;
		executorService = new ScheduledThreadPoolExecutor(threadNum + 10);
		readThread = new Thread() {
			public void run() {
				while(!shutdown) {
					try {
						CountableThreadPool.this.waitIdleThread();
					} catch (Exception e) {
						e.printStackTrace();
					}
					Runnable runnable = runnables.poll();
					if (runnable != null) {
						executorService.execute(runnable);
						continue;
					}else {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
		};
		readThread.start();
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
		runnables.add(runnable);
	}
	
	public long getTaskCount() {
		return executorService.getTaskCount();
	}

	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	public void shutdown() {
		while(!runnables.isEmpty()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		executorService.shutdown();
		try {
			while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
				// The thread pool has no closing
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		shutdown = true;
	}

	public int getIdleThreadCount() {
		return executorService.getMaximumPoolSize() - getThreadAlive();
	}

}
