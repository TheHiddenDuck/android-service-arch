/*
 * Copyright (C) 2012 Alexander Osmanov (http://www.perfectearapp.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package ru.evilduck.framework.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public abstract class SFThreadPoolIntentService extends Service {

	private static final int SHUTDOWN_REQUEST = 0;

	private ExecutorService executorService;

	private volatile int executesCounter = 0;

	private Handler shutdownCooldownHandler = new Handler() {
		public void handleMessage(Message msg) {
			stopSelf();
		};
	};

	@Override
	public void onCreate() {
		super.onCreate();

		executorService = setupConfigurator().configure();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		executorService.shutdown();
	}

	/**
	 * Configure a proper thread pool using one of
	 * {@link #asFixedThreadPool(int)}, {@link #asCachedThreadPool()} or
	 * {@link #asQueue()} methods
	 * 
	 * @return
	 */
	protected abstract PoolConfigurator setupConfigurator();

	protected abstract void onHandleIntent(Intent intent);

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		synchronized (shutdownCooldownHandler) {
			executesCounter++;
			shutdownCooldownHandler.removeMessages(SHUTDOWN_REQUEST);
		}
		executorService.execute(new CommandExecutorCallable(intent));

		return START_NOT_STICKY;
	}

	private class CommandExecutorCallable implements Runnable {

		private static final int SHUTDOWN_COOLDOWN = 5000;

		private Intent intent;

		public CommandExecutorCallable(Intent intent) {
			this.intent = intent;
		}

		@Override
		public void run() {
			onHandleIntent(intent);

			synchronized (shutdownCooldownHandler) {
				if (--executesCounter == 0) {
					shutdownCooldownHandler.sendEmptyMessageDelayed(
							SHUTDOWN_REQUEST, SHUTDOWN_COOLDOWN);
				}
			}
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// Configuration management

	protected interface PoolConfigurator {
		ExecutorService configure();
	}

	protected static PoolConfigurator asFixedThreadPool(final int threadCount) {
		return new PoolConfigurator() {
			@Override
			public ExecutorService configure() {
				return Executors.newFixedThreadPool(threadCount);
			}
		};
	}

	protected static PoolConfigurator asCachedThreadPool() {
		return new PoolConfigurator() {
			@Override
			public ExecutorService configure() {
				return Executors.newCachedThreadPool();
			}
		};
	}

	protected static PoolConfigurator asQueue() {
		return new PoolConfigurator() {
			@Override
			public ExecutorService configure() {
				return Executors.newSingleThreadExecutor();
			}
		};
	}

}
