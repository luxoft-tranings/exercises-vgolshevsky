package com.luxoft.jva.multithreading.ch06_atomic;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;

/**
 * In this exercise we will play atomic ping-pong again:
 * <ul>
 * <li>Create classes {@link Ping} and {@link Pong} that implements {@link Runnable}.</li>
 * <li>Create class {@link Ball} that has two {@link AtomicInteger} fields ping and pong.</li>
 * </ul>
 * <p>
 * <p>
 * In loop
 * {@link Ping}:
 * <ul>
 * <li>Increase ping value by 1</li>
 * <li>Do nothing while current step != pong</li>
 * </ul>
 * <p>
 * <p>
 * {@link Pong}:
 * <ul>
 * <li>Do nothing while ping != current step</li>
 * <li>Increase pong value by 1</li>
 * </ul>
 *
 * @author BKuczynski.
 */
public class Exercise13 {

	public static final int MAXHOP = 10_000;
	public static void main(String[] args) {
		Exercise13.Ball ball = new Exercise13.Ball();

		Thread ping = new Thread(new Exercise13.Ping(ball));
		Thread pong = new Thread(new Exercise13.Pong(ball));

		ping.setName("ping");
		pong.setName("pong");

		ping.start();
		pong.start();
		try
		{
			ping.join();
			pong.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		out.println("ping " + ball.ping + ", pong " + ball.pong);
	}
	static class Ball
	{
		//		public int ping = -1;
		// 		public int pong = -1;
		public AtomicInteger ping = new AtomicInteger(-1);
		public AtomicInteger pong = new AtomicInteger(-1);
	}

	static class Ping implements Runnable
	{

		private final Exercise13.Ball ball;

		Ping(Exercise13.Ball ball)
		{
			this.ball = ball;
		}

		@Override
		public void run()
		{
			for (int i = 0; i < MAXHOP; i++)
			{
				ball.ping.set(i);
				while (i != ball.pong.get())
				{
					// Thread.yield();
				}
			}
		}
	}

	static class Pong implements Runnable
	{

		private final Exercise13.Ball ball;

		Pong(Exercise13.Ball ball)
		{
			this.ball = ball;
		}

		@Override
		public void run()
		{
			for (int i = 0; i < MAXHOP; i++)
			{
				while (ball.ping.get() != i)
				{
					// Thread.yield();
				}
				ball.pong.set(i);
			}
		}
	}

}

