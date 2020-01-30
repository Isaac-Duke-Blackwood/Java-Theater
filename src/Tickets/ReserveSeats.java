/* Name: Isaac Blackwood 
 * NetID: idb170030
 * This program should meet the specifications outlined in pdf in this folder
 */
package Tickets;

import java.util.NoSuchElementException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import static java.lang.System.out;

public class ReserveSeats
{
	//this will display the prompts for the user to reserve seats and help them reserve them
	public static void choiceReserveSeats(Scanner userInput, String fileName) throws NoSuchElementException, FileNotFoundException
	{
		try 
		{
			//open the input file
			File inputFile = new File(fileName); 
			
			//Create a theater object
			Theater auditorium1 = new Theater(inputFile); 
			
			//Display the theater on the screen
			auditorium1.display();
			
			//Get necessary user input
			SeatingRequest request1 = new SeatingRequest(auditorium1, userInput);
			
			//Validate seating arrangement
			if (request1.valid()) 
			{
				//Reserve seats and write it to file
				request1.reserve();
			}
			else
			{
				//user request invalid. so suggest next best seats
				out.println("We're sorry! Your seat request could not be fulfilled.");
				request1.suggestBest(userInput); //suggestBest() asks the user whether or not they want to reserve the suggested seats, and if they do, reserves them.
			}
			
			//skips a line
			out.println();
		}
		catch (FileNotFoundException ex)
		{
			out.println("File \"A1.txt\" could not be found. Please make sure the seating chart is properly named and in the correct directory.");
		}
		catch (NoSuchElementException ex)
		{
			out.println("An error occurred while reading the file. Check to make sure the seating chart file is in the improper format and not empty.");
		}
		
	}
}