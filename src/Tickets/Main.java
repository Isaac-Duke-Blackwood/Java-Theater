/* Name: Isaac Blackwood 
 * NetID: idb170030
 * This program should meet the specifications outlined in pdf in this folder
 */
package Tickets;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import static java.lang.System.out;
import java.io.FileNotFoundException;

public class Main 
{

	public static void main(String[] args) 
	{
		final int RESERVE_SEATS = 1;
		final int EXIT = 2;
		final String FILE_NAME = "A1.txt";
		boolean quit = false;
		Scanner userInput = new Scanner(System.in);
		
		while (!quit) //display main menu until user quits
		{
			out.println("1. Reserve Seats \n2. Exit");
			try 
			{
				switch (userInput.nextInt()) 
				{
				//lets user reserve seats
				case RESERVE_SEATS:		ReserveSeats.choiceReserveSeats(userInput,FILE_NAME); 
										break;
				//lets user exit the program
				case EXIT: 				quit = true;
										Exit.choiceExit(FILE_NAME);
										break;
				//conveys error message
				default: 				out.println("Invalid choice, please try again.");
				}
			}
			catch (InputMismatchException ex)
			{
				userInput.hasNextLine(); //Discard input
				out.println("Please enter an integer.");
			}
			catch (FileNotFoundException ex) //from getSeatingChart()
			{
				out.println("Seating chart could not be found.\nPlease check that the file is named \"A1.txt\" and located in the proper directory.");
			}
			catch (NoSuchElementException ex)
			{
				out.println("Element could not be found.");
			}
		}
		userInput.close();
	}
}
