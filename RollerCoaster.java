import java.util.ArrayList;
import java.util.Random;

public class RollerCoaster 
{
	private static int rides;
	private static boolean is_open;

	public static void main(String[] args)
	{
		int total_passengers, regular, golden, capacity, i = 0;
		ArrayList<Passenger> passengers = new ArrayList<Passenger>();

		if(!correct_number_of_arguments(args.length)) use();

		total_passengers = (regular = get_int_argument(args[0])) + (golden = get_int_argument(args[1]));
		capacity = get_int_argument(args[2]);
		rides = get_int_argument(args[3]);
		is_open = true;

		if(capacity > regular + golden) { System.out.println("Insufficient number of passengers (R + G < C)."); System.exit(0); }

		Monitor monitor = new Monitor(capacity);
		initiate_passengers(passengers, regular, golden, monitor);

		Car c = new Car(capacity, monitor);

		//Create threads
		for (Passenger p : passengers) 
		{ 
			(new Thread(p)).start();
			if(i == 0) { i++; (new Thread(c)).start(); }
		}
	}

	private static void initiate_passengers(ArrayList<Passenger> passengers, int regular, int golden, Monitor monitor)
	{
		int i;

		for(i = 0; i < golden; i++)
		{
			Passenger p = new Passenger(i + 1, 'G', monitor);
			passengers.add(p);
		}

		for(i = golden; i < golden + regular; i++)
		{
			Passenger p = new Passenger(i + 1, 'R', monitor);
			passengers.add(p);
		}
	}

	private static int get_int_argument(String argument)
	{
		if(!is_integer(argument)) return -1;
		return Integer.parseInt(argument);
	}

	//Must be integer number
	private static boolean is_integer(String str)
	{
		try {
			Integer.parseInt(str);
		} catch(NumberFormatException e) {
			return false;
		} catch(NullPointerException e) {
			return false;
		}
		return true;
	}

	private static void use()
	{
		System.out.print("Incorrect number of arguments. Use:\n\n\tjava RollerCoaster R G C N\n\nWhere:\n");
		System.out.print("R is an integer representing the total number of passengers with regular ticket.\n");
		System.out.print("G is an integer representing the total number of passengers with golden ticket.\n");
		System.out.print("C is an integer representing the capacity of the roller coaster car.\n");
		System.out.print("N is an integer representing the total number of rides that the car will do.\n");
		System.exit(0);
	}

	private static boolean correct_number_of_arguments(int length) { return length == 4; }

	public static int maximum_rides() { return rides; }
	public static boolean is_open() { return is_open; }
	public static void close() { is_open = false; }
}
