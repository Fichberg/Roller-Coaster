public class Passenger implements Runnable
{
	private int number;
	private char ticket_type;
	private Monitor monitor;

	public Passenger(int number, char ticket_type, Monitor monitor)
	{
		this.number = number;
		this.ticket_type = ticket_type;
		this.monitor = monitor;
	}

	public void run()
	{
		while(RollerCoaster.is_open())
		{
			try{
				monitor.get_ride(ticket_type, number);			
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	} 
}