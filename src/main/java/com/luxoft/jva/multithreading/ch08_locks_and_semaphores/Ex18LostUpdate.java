package com.luxoft.jva.multithreading.ch08_locks_and_semaphores;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO
 * Use locks to prevent data loss.
 * Try: ReentrantLock and ReentrantReadWriteLock
 */
public class Ex18LostUpdate
{
    public static void main(String[] args) throws InterruptedException
    {
        Amount amount = new Amount(1000L);
        Thread cashMachine = new Thread(new CashMachine(amount));
        Thread taxCollector = new Thread(new TaxCollector(amount));

        System.out.printf("Initial state of amount is %s\n", amount.getBalance());

        cashMachine.start();
        taxCollector.start();

        cashMachine.join();
        taxCollector.join();

        System.out.printf("Final state of amount is: %s\n", amount.getBalance());
    }


    private static class Amount
    {
        ReentrantLock lock = new ReentrantLock();
        private long value;

        public Amount(long value)
        {
            lock.lock();
            this.value = value;
            lock.unlock();
        }

        public void deposit(long amount)
        {
            lock.lock();
            value += amount;
            lock.unlock();
        }

        public void withdraw(long amount)
        {
            lock.lock();
            deposit(-amount);
            lock.unlock();
        }

        public long getBalance()
        {
            return value;
        }
    }

    private static class CashMachine implements Runnable
    {
        private Amount amount;

        public CashMachine(Amount amount)
        {
            this.amount = amount;
        }

        @Override
        public void run()
        {
            for (int i = 0; i < 1_000_000; i++)
            {
                amount.withdraw(1L);
            }
        }
    }

    private static class TaxCollector implements Runnable
    {
        private Amount amount;

        public TaxCollector(Amount amount)
        {
            this.amount = amount;
        }

        @Override
        public void run()
        {
            for (int i = 0; i < 1_000_000; i++)
            {
                amount.deposit(1L);
            }
        }
    }
}
