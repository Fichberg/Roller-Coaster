public class Car implements Runnable
{
	private int capacity;
	private Monitor monitor;

	public Car(int capacity, Monitor monitor)
	{
		this.capacity = capacity;
		this.monitor = monitor;
	}

	public void run()
	{
		int ride = 0;

		while(ride != RollerCoaster.maximum_rides())
		{
			try{
				monitor.load_car();
				ride(ride + 1); ride++;
				monitor.unload_car();	
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

		RollerCoaster.close();
	}

	private void ride(int ride) throws InterruptedException
	{
		System.out.println("Ride "+ ride +" is about to start! Wooohooooooo!");
		Thread.sleep(2000);
		System.out.println("Ride "+ ride +" has finished! Woooah brooo! Awesome!");
	}
}