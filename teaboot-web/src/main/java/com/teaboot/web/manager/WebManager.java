package com.teaboot.web.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.teaboot.web.server.NettyServiceTemplate;

public class WebManager {
	ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	private static class WebManagerContainer {
		private static WebManager instance = new WebManager();
	}

	public static WebManager getInstance() {
		return WebManagerContainer.instance;
	}

	public static void addTask(Runnable t) {
		getInstance().cachedThreadPool.submit(t);

	}

	public static void addServer(NettyServiceTemplate server) {
		getInstance().cachedThreadPool.submit(new Thread() {
			@Override
			public void run() {
				try {
					server.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
