/* Name: Isaac Blackwood 
 * NetID: idb170030
 * This program should meet the specifications outlined in pdf in this folder
 */
package Tickets;

import static java.lang.System.out;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Exit 
{
	private static final float ADULT_PRICE = 10f, CHILD_PRICE = 5f, SENIOR_PRICE = 7.50f;
	
	public static void choiceExit(String fileName) throws FileNotFoundException
	{
		//open the input file
		File inputFile = new File(fileName); 
		
		//Create a theater object
		Theater auditorium1 = new Theater(inputFile);
		
		//Count total seats
		int totalSeatsInAuditorium = auditorium1.getTotalSeats();
		TicketHolder ticketHolder = new TicketHolder();
		auditorium1.countNumberOfEachTypeOfSeat(ticketHolder);
		
		//display the contents of the theater
		Scanner fileInput = new Scanner(inputFile);
		out.println("    THEATER: \"" + fileName +"\"");
		while(fileInput.hasNextLine())
		{
			out.println(fileInput.nextLine());
		}
		out.println();
		fileInput.close();
		
		//calculate the total cost and display it
		int numberOfAdultTickets = ticketHolder.adultTickets(), numberOfChildTickets = ticketHolder.childTickets(), numberOfSeniorTickets = ticketHolder.seniorTickets();
		float price = (numberOfAdultTickets * ADULT_PRICE + numberOfChildTickets * CHILD_PRICE + numberOfSeniorTickets * SENIOR_PRICE);
		out.println("Total Seats in Auditorium: " + Integer.toString(totalSeatsInAuditorium));
		out.println("Total Tickets Sold: " + Integer.toString(ticketHolder.totalTickets()));
		out.println("Adult Tickets Sold: " + Integer.toString(ticketHolder.adultTickets()));
		out.println("Child Tickets Sold: " + Integer.toString(ticketHolder.childTickets()));
		out.println("Senior Tickets Sold: " + Integer.toString(ticketHolder.seniorTickets()));
		out.print("Total Ticket Sales: $");
		out.printf("%.2f", price);
		out.println();
	}
	
	
}
