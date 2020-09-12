package com.deep007.goniub.util;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

/**
 * 可计数的线程池
 */
public class CountableThreadPool{

	private final int threadNum;

	private ThreadPoolExecutor executorService;
	
	private final Queue<PriorityTask> runnables = new PriorityBlockingQueue<>();
	
	private final Thread readThread;
	
	private boolean shutdown = false;

	public CountableThreadPool(int corePoolSize) {
		this.threadNum = corePoolSize < 1? 1:corePoolSize;
		executorService = new ThreadPoolExecutor(threadNum, threadNum,
	            0L, TimeUnit.MILLISECONDS,
	            new LinkedBlockingQueue<Runnable>());
		executorService.setRejectedExecutionHandler(new CallerRunsPolicy());
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
	
	public static abstract class PriorityTask implements Runnable, Comparable<PriorityTask> {
		
		private final int priority;
		
		public PriorityTask(int priority) {
			this.priority = priority;
		}
		
		@Override
		public int compareTo(PriorityTask o) {
			return this.priority <= o.priority ? 1 : -1;
		}
		
	}
	
	public void execute(Runnable runnable) {
		execute(runnable, 0);
	}

	public void execute(final Runnable runnable, int priority) {
		runnables.add(new PriorityTask(priority) {
			
			@Override
			public void run() {
				runnable.run();
			}
		});
	}
	
	public long getTaskCount() {
		return executorService.getTaskCount();
	}

	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	public void shutdown() {
		waitAllReady();
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
	
	
	public void waitAllReady() {
		while (!runnables.isEmpty() || getThreadAlive() > 0) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
