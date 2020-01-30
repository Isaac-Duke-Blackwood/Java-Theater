/* Name: Isaac Blackwood 
 * NetID: idb170030
 * This program should meet the specifications outlined in pdf in this folder
 */
package Tickets;


public class TicketHolder 
{
	private int adult = 0;
	private int child = 0;
	private int senior = 0;

	//getters
	public int adultTickets()
	{
		return adult;
	}
	public int childTickets()
	{
		return child;
	}
	public int seniorTickets()
	{
		return senior;
	}
	public int totalTickets()
	{
		return adult + child + senior;
	}

	//setters (return the value set)
	public int adultTickets(int adultTickets)
	{
		adult = adultTickets;
		return adultTickets;
	}
	public int childTickets(int childTickets)
	{
		child = childTickets;
		return childTickets;
	}
	public int seniorTickets(int seniorTickets)
	{
		adult = seniorTickets;
		return seniorTickets;
	}
}
