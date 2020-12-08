package com.luxoft.jva.multithreading.ch06_atomic;

import static java.lang.System.out;

/**
 * In this exercise we will play volatile ping-pong:
 * <ul>
 * <li>Create classes {@link Ping} and {@link Pong} that implements {@link Runnable}.</li>
 * <li>Create class {@link Ball} that has two volatile fields ping and pong.</li>
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
public class Exercise12 {

	public static final int MAXHOP = 10_000_000;
	public static void main(String[] args) {
		Ball ball = new Ball();

		Thread ping = new Thread(new Ping(ball));
		Thread pong = new Thread(new Pong(ball));

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
//		public int pong = -1;
		public volatile int ping = -1;
		public volatile int pong = -1;
	}

	static class Ping implements Runnable
	{

		private final Ball ball;

		Ping(Ball ball)
		{
			this.ball = ball;
		}

		@Override
		public void run()
		{
			for (int i = 0; i < MAXHOP; i++)
			{
				ball.ping = i;
				while (i != ball.pong)
				{
					// Thread.yield();
				}
			}
		}
	}

	static class Pong implements Runnable
	{

		private final Ball ball;

		Pong(Ball ball)
		{
			this.ball = ball;
		}

		@Override
		public void run()
		{
			for (int i = 0; i < MAXHOP; i++)
			{
				while (ball.ping != i)
				{
					// Thread.yield();
				}
				ball.pong = i;
			}
		}
	}

}
