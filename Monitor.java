import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor
{
	private int golden_queue;
	private int regular_queue;
	private int golden_aboard;
	private int regular_aboard;
	private int capacity;
	private boolean car_is_ready;

	private final Lock lock;
	private final Condition ride_ready;
	private final Condition ride_queue_golden;
	private final Condition ride_queue_regular;

	public Monitor(int capacity)
	{
		this.capacity = capacity;
		golden_queue = 0;
		regular_queue = 0;
		golden_aboard = 0;
		regular_aboard = 0;
		car_is_ready = false;

		lock = new ReentrantLock(true);
		ride_ready = lock.newCondition();
		ride_queue_golden = lock.newCondition();
		ride_queue_regular = lock.newCondition();
	}

	public void get_ride(char ticket_type, int number) throws InterruptedException
	{
		lock.lock();
		try{
			if(car_is_ready)
			{
				if(regular_queue == 0 && golden_queue == 0) System.out.println("Passengers arriving for the next ride:");
				System.out.println("P"+number+" ("+ticket_type+")");
				
				if(ticket_type == 'G') { 
					golden_queue++;
					if(golden_queue + regular_queue > capacity) regular_queue--;				
				}
				else regular_queue++;

				if(golden_queue + regular_queue == capacity)
				{
					golden_aboard = golden_queue; regular_aboard = regular_queue;
					golden_queue = 0; regular_queue = 0;
					ride_ready.signal();
					car_is_ready = false;
				}
				if(ticket_type == 'G') ride_queue_golden.await();
				else ride_queue_regular.await();
			}
		} finally {
			lock.unlock();
		}

	}

	public void load_car() throws InterruptedException
	{
		lock.lock();
		try{
			car_is_ready = true;
			ride_ready.await();
		} finally {
			lock.unlock();
		}
	}

	public void unload_car()
	{
		lock.lock();
		try{
			for(int i = 0; i < golden_aboard; i++) ride_queue_golden.signal();
			for(int i = 0; i < regular_aboard; i++) ride_queue_regular.signal();
			System.out.println("Last Ride passengers: "+ golden_aboard +"G and "+regular_aboard+"R.");
			golden_aboard = 0;
			regular_aboard = 0;
		} finally {
			lock.unlock();
		}
	}
}