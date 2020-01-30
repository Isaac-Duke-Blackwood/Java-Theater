/* Name: Isaac Blackwood 
 * NetID: idb170030
 * This program should meet the specifications outlined in pdf in this folder
 */
package Tickets;

import java.util.Scanner;
import static java.lang.System.out;
import java.io.File;
import java.io.FileNotFoundException;


public class Theater 
{
	private int numberOfSeatsPerRow = 0;
	private int numberOfRows = 0;
	private File inputFile = null;
	private char[][] seatingChart; //Stores empty seats as '.' and adult, child and senior tickets as 'A' 'C' and 'S' respectively.
	private final static int ASCII_A = 65;
	
	//constructor
	Theater(File inFile) throws FileNotFoundException
	{	
		inputFile = inFile;
		//get number of seats per row
		numberOfSeatsPerRow = countSeatsPerRow();
		
		//get the number of rows
		numberOfRows = countRows();
		
		//Create an 2 dimensional array that houses each seat in it as one part of the array
		seatingChart = new char[numberOfRows][numberOfSeatsPerRow];
		
		//create a scanner to traverse the file
		Scanner fileInput = new Scanner(inputFile);
		fileInput.nextLine(); //skip the header- note that this will not work if the file is ever in the wrong format
		
		//step through the file and fill the array
		for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) 
		{
			String currentRow = fileInput.nextLine(); // create a string of the characters in the whole row
			int seatIndex = 0; //reset to zero with each next row
			for (int stringIndex = 0; stringIndex < currentRow.length(); stringIndex++)
			{
				char currentCharacter = currentRow.charAt(stringIndex);
				
				//Add current character to the array if it is part of the seating chart
				if ( currentCharacter == '.' || currentCharacter == 'A' || currentCharacter== 'C' || currentCharacter == 'S')
				{
					seatingChart[rowIndex][seatIndex] = currentCharacter; 
					seatIndex++;
				}
			}
		}
		
		//close the scanner
		fileInput.close();
	}
	
	//Display function
	public void display()
	{
		int numberOfDigitsInMaxRowNumber = Integer.toString(numberOfRows).length();
		for (int count = 1; count <= numberOfDigitsInMaxRowNumber + 1; count++) //print spaces before header
		{
			out.print(' ');
		}
		for (int i = 0; i < numberOfSeatsPerRow; i++) //print header
		{
			out.print((char)(ASCII_A + i));//prints upper case letters starting at 'A' (ASCII 65)
		}
		out.println(); //move to next line
		
		for (int currentRow = 1; currentRow <= numberOfRows; currentRow++) //print each row
		{
			//print the number and spaces at left of the row
			for (int space = 1; space <= numberOfDigitsInMaxRowNumber - Integer.toString(currentRow).length() - 1; space++) //should print the correct number of spaces for the row before the number so that all the columns are correctly aligned
			{
				out.print(' ');
			}
			
			//print the row number followed by a space
			out.print(Integer.toString(currentRow) + " ");
			
			//print the actual seats for the row
			for (int currentSeat = 1; currentSeat <= numberOfSeatsPerRow; currentSeat++)
			{
				//check if empty or full
				if (seatingChart[currentRow-1][currentSeat-1] != '.') //converts to indices before comparison
				{
					out.print('#');
				}
				else
				{
					out.print('.');
				}
			}
			
			//print new line character
			out.println();
		}
	}

	//Functions for determining theater size
	private int countSeatsPerRow() throws FileNotFoundException
	{
		Scanner fileInput = new Scanner(inputFile);
		
		//get number of seats per row
		String header = fileInput.next();
		int numberOfSeatsPerRow = header.length(); //Note that this will not work if the theater has more than 26 seats per row - it just counts the number of letters in the header.
		
		fileInput.close();
		return numberOfSeatsPerRow;
	}
	private int countRows() throws FileNotFoundException
	{
		Scanner fileInput = new Scanner(inputFile);
		
		//get the number of rows
		int numberOfRows = -1; //start at -1 to subtract the header
		boolean endOfFile = false;
		while (!endOfFile)
		{
			if(fileInput.hasNextLine())
			{
				//The file has a next line so skip to the next one and increment the row count
				fileInput.nextLine();
				numberOfRows++;
			}
			else
			{
				//file has ended
				endOfFile = true;
			}
		}
		fileInput.close();
		return numberOfRows;
	}

	//Getters
	public int getSeatsPerRow()
	{
		return numberOfSeatsPerRow;
	}
	public int getRows()
	{
		return numberOfRows;
	}
	public char[][] getSeatingChart()
	{
		return seatingChart;
	}
	public File getFile()
	{
		return inputFile;
	}
	public int getTotalSeats()
	{
		return numberOfRows * numberOfSeatsPerRow;
	}

	//Counts number of each type of seat and returns them by setting the passed variables by reference
	public void countNumberOfEachTypeOfSeat(TicketHolder ticketHolder)
	{
		int adultTicketsSold = 0, childTicketsSold = 0, seniorTicketsSold = 0;
		for (int rowIndex = 0; rowIndex < getRows(); rowIndex++)
		{
			for (int seatIndex = 0; seatIndex < getSeatsPerRow(); seatIndex++)
			{
				//If the seat is sold then add to ticket count based on type
				if (seatingChart[rowIndex][seatIndex] == 'A')
				{
					ticketHolder.adultTickets(++adultTicketsSold);
				}
				else if (seatingChart[rowIndex][seatIndex] == 'C')
				{
					ticketHolder.childTickets(++childTicketsSold);
				}
				else if (seatingChart[rowIndex][seatIndex] == 'S')
				{
					ticketHolder.seniorTickets(++seniorTicketsSold);
				}
			}
		}
	}
}
