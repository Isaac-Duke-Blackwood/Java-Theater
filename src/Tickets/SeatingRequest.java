/* Name: Isaac Blackwood 
 * NetID: idb170030
 * This program should meet the specifications outlined in pdf in this folder
 */
package Tickets;


import java.util.Scanner;
import static java.lang.System.out;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Character;

public class SeatingRequest 
{
	private int preferredRow = 0; //the actual row number as in the diagram
	private char preferredStartingSeat = '\0'; //the actual seat letter as in the diagram
	private int numberOfAdultTickets = 0, numberOfChildTickets = 0, numberOfSeniorTickets = 0;
	private int totalNumberOfSeatsRequested = 0;
	private static final int ASCII_A = 65;
	private Theater auditorium = null;
	private static final float ADULT_PRICE = 10f, CHILD_PRICE = 5f, SENIOR_PRICE = 7.50f;
	private static final boolean ODD = true, EVEN = false;
	
	//constructors
 	SeatingRequest(Theater theater, Scanner userInput) //sets all of the class variables
	{
		setTheater(theater);
		setPreferredRow(userInput);
		setPreferredStartingSeat(userInput);
		setNumberOfAdultTickets(userInput);
		setNumberOfChildTickets(userInput);
		setNumberOfSeniorTickets(userInput);	
		totalNumberOfSeatsRequested = numberOfAdultTickets + numberOfChildTickets + numberOfSeniorTickets;
	}
 	private SeatingRequest(Theater theater, SeatingRequest oldRequest, int newRow, char newSeat) // for use with suggestBest()
 	{
 		setTheater(theater);
 		preferredRow = newRow;
 		preferredStartingSeat = newSeat;
 		numberOfAdultTickets = oldRequest.numberOfAdultTickets;
 		numberOfChildTickets = oldRequest.numberOfChildTickets;
 		numberOfSeniorTickets = oldRequest.numberOfSeniorTickets;
		totalNumberOfSeatsRequested = numberOfAdultTickets + numberOfChildTickets + numberOfSeniorTickets; 		
 	}

	//Validate whether or not request can be fulfilled
	public boolean valid()
	{	
		if(totalNumberOfSeatsRequested > auditorium.getSeatsPerRow())
		{
			return false;
		}
		//For the total number of seats, step through the array starting at the starting seat
		char[][] seatingChart = auditorium.getSeatingChart();
		for (int currentSeat = 1; currentSeat <= totalNumberOfSeatsRequested; currentSeat++)
		{
			boolean inBounds = ((currentSeat + (int)preferredStartingSeat - ASCII_A <= auditorium.getSeatsPerRow()) && (currentSeat + (int)preferredStartingSeat - ASCII_A >= 0));
			if(!inBounds) 
			{
				//the current seat does not exist on this row
				return false;
			}
			boolean seatIsEmpty = (seatingChart[preferredRow - 1][(int)preferredStartingSeat - ASCII_A - 1 + currentSeat] == '.');//the second array index is determined by converting the seat character to ASCII and subtracting 65 or 'A' in ASCII and subtracting 1 because the current seat starts at 1
			if(!seatIsEmpty) 
			{
				//the current seat is not empty (full or stepped out of the row)
				return false;
			}
		}
		//None of the requested seats were occupied
		return true;
	}
	
	//Reserve the seats and change the file
	public void reserve() throws FileNotFoundException
	{
		//create the default array of the seats for the request
		Seat[] requestedSeats = new Seat[totalNumberOfSeatsRequested];
		for(int seat = 0, adults = numberOfAdultTickets, children = numberOfChildTickets, seniors = numberOfSeniorTickets; seat < totalNumberOfSeatsRequested; seat++)
		{
			char ticketType = '\0';
			//set the ticket type
			if(adults > 0)
			{
				ticketType = 'A';
				adults--;
			}
			else if(children > 0)
			{
				ticketType = 'C';
				children--;
			}
			else if(seniors > 0)
			{
				ticketType = 'S';
				seniors--;
			}
			
			//Initialize the seat
			requestedSeats[seat] = new Seat(preferredRow, (char)((int)preferredStartingSeat + seat), ticketType);
			
		}

		//pass the array of seats to the other reserve function
		reserve(requestedSeats);
	}
	public void reserve(Seat[] listOfSeats) throws FileNotFoundException //also calls display for price info
	{
		//for each seat, change the file to reserve the seat
		char[][] seatingChart = auditorium.getSeatingChart();
		for (int seat = 0; seat < listOfSeats.length; seat++)
		{
			int rowIndex = listOfSeats[seat].getRow() - 1;
			int seatIndex = ((int)listOfSeats[seat].getSeat() - ASCII_A);
			char seatType = listOfSeats[seat].getType();
			
			//update the seatingChart array
			seatingChart[rowIndex][seatIndex] = seatType; 
			
		}

		//print to the file
		PrintWriter writer = new PrintWriter(auditorium.getFile());
		int numberOfDigitsInMaxRowNumber = Integer.toString(auditorium.getRows()).length();
		for (int count = 1; count <= numberOfDigitsInMaxRowNumber + 1; count++) //print spaces before header
		{
			writer.print(' ');
		}
		for (int i = 0; i < auditorium.getSeatsPerRow(); i++) //print header
		{
			writer.print((char)(ASCII_A + i));//prints upper case letters starting at 'A' (ASCII 65)
		}
		writer.println(); //move to next line
		
		for (int currentRow = 1; currentRow <= auditorium.getRows(); currentRow++) //print each row
		{
			//print the number and spaces at left of the row
			for (int space = 1; space <= numberOfDigitsInMaxRowNumber - Integer.toString(currentRow).length() - 1; space++) //should print the correct number of spaces for the row before the number so that all the columns are correctly aligned
			{
				writer.print(' ');
			}
			
			//print the row number followed by a space
			writer.print(Integer.toString(currentRow) + " ");
			
			//print the actual seats for the row
			for (int currentSeat = 0; currentSeat < auditorium.getSeatsPerRow(); currentSeat++)
			{
				writer.print(seatingChart[currentRow - 1][currentSeat]); //converts current row to index
			}
			
			//print new line character
			writer.println();
		}
		writer.close();
		
		//Display price information
		displayPrice();
	}
	
	//Suggest alternate seats and then call reservation function if the user accepts them (this is a big function but idk how to split it up in a way that makes sense to me)
	public void suggestBest(Scanner userInput) throws FileNotFoundException
	{
		//suggests best seats with prioritizing seats on that row, as close to center screen as possible (I assume they are supposed to sit next to one another)
		//determine the "middle" seat of the theater (true middle or the right seat of the middle 2)
		char middleSeatOfRow = (char)(ASCII_A + (auditorium.getSeatsPerRow()/2)); //the integer division should find the left seat of the middle two, or (seat to the left of the true middle for odd numbers of seats). The + 65 means the middle seat will be the true middle or the right seat of the middle 2 when converted back to a char
		
		//determine if the row has an odd or even number of seats
		boolean rowParity = determineRowParity();
		//determine if the row has an odd or even number of seats
		boolean requestParity = determineRequestParity();
		
		//determine the number of seats to the left of the center of the requested seats (allows to determine where the start of the reservation request is in relation to its center)
		int seatsToLeftOfSelectionCenter = (totalNumberOfSeatsRequested / 2);
		
		//determine the seat letter of the starting seat of the string of seats in the seatingChart array
		char startingSeatLetter = (char)((int)middleSeatOfRow - seatsToLeftOfSelectionCenter);

		boolean currentSeatsAreAvailable = false, noSeatsAreAvailable = false;
		int newRow = preferredRow;
		char newSeat = startingSeatLetter;
		int movementIterator = 0; //used for controlling the "movement" of new seat back and forth across the row in the theater as the seats get less optimal.
		
		do //loop through from best seats to worst seats on a row and see if any are free
		{
			newSeat = (char)((int)newSeat + movementIterator);
			
			//check to see if all possibilities for the starting seat have been scanned (step out right (towards 'Z')
			if((int)newSeat >= ASCII_A + auditorium.getSeatsPerRow())
			{
				//all of the seats have been scanned skip to end
				noSeatsAreAvailable = true;
			}
			else
			{
				//create a new request with a different starting seat to the right
				SeatingRequest newRequestRight = new SeatingRequest(auditorium, this, newRow, newSeat); 
				if(newRequestRight.valid())
				{
					//new seating request is valid
					currentSeatsAreAvailable = true;
					
					//would you like to reserve the best available seats?
					reserveBestAvailable(userInput, newRequestRight);
				}
				else
				{
					//new seating request is not valid
					movementIterator++;
				}
				
				boolean firstTimeThrough = (movementIterator == 1);
				boolean exceptionCase = (movementIterator == 1 && rowParity == ODD && requestParity == EVEN); //When the row has an odd number of seats and the user requests an even number of seats then the starting seat should "move" right first to make sure all spaces are covered before going out of bounds on the high end of the row (towards 'Z')
				if (!currentSeatsAreAvailable)
				{
					if(!(firstTimeThrough && exceptionCase))//don't execute the following code if seats are available, or its the first time through the loop UNLESS the row has an odd number of seats AND the request an even number of seats
					{
						newSeat = (char)((int)newSeat - movementIterator);
						
						//check to see if all possibilities for the starting seat have been scanned (step out left (before 'A"))
						if((int)newSeat < ASCII_A)
						{
							//all of the seats have been scanned skip to end
							noSeatsAreAvailable = true;
						}
						else
						{
							//create a new request with a different starting seat to the left
							SeatingRequest newRequestLeft = new SeatingRequest(auditorium, this, newRow, newSeat); 
							if(newRequestLeft.valid())
							{
								//new seating request is valid
								currentSeatsAreAvailable = true;
								
								//would you like to reserve the best available seats?
								reserveBestAvailable(userInput, newRequestLeft); //TODO make all reserve functions tell the user what seats they got
							}
							else
							{
								//new seating request is not valid
								movementIterator++;
							}
						}
					}
				}
			}
		} 
		while (!currentSeatsAreAvailable && !noSeatsAreAvailable);
		
		if(noSeatsAreAvailable)//No seats were available on the selected row
		{
		out.println("There are no alternative seats available on the chosen row.");
		}
	}
	
	//Odd or even
	private boolean determineRowParity()
	{
		boolean rowParity = ODD;
		if ((auditorium.getSeatsPerRow() % 2) == 0)
		{
			//the number of seats in the row is even
			rowParity = EVEN;
		}
		return rowParity;
	}
	private boolean determineRequestParity()
	{
		boolean requestParity = ODD;
		if ((totalNumberOfSeatsRequested % 2) == 0)
		{
			//the number of seats in the request is even
			requestParity = EVEN;
		}
		return requestParity;
	}
	
	//would you like to reserve the best available seats?
	private void reserveBestAvailable(Scanner userInput, SeatingRequest newRequest) throws FileNotFoundException
	{
		boolean inputValid = false;
		do
		{
			out.println("The best  seats available in the selected row are " + Integer.toString(newRequest.preferredRow) + Character.toString(newRequest.preferredStartingSeat) + " through " + Integer.toString(preferredRow) + Character.toString((char)((int)preferredStartingSeat + totalNumberOfSeatsRequested - 1)) + ".");
			out.println("Reserve alternate seats? (Y/N)?");
			char userChoice = Character.toUpperCase(userInput.next().charAt(0));
		if (userChoice == 'Y')
		{
			inputValid = true;
			newRequest.reserve();
		}
		else if (userChoice == 'N')
		{
			inputValid = true;
		}
		else 
		{
			out.println("Please enter \'Y\' or \'N\'");
		}
		}
		while(!inputValid);
	}
	
	//display price
	public void displayPrice()
	{
		if (totalNumberOfSeatsRequested != 0)
		{
			float price = (numberOfAdultTickets * ADULT_PRICE + numberOfChildTickets * CHILD_PRICE + numberOfSeniorTickets * SENIOR_PRICE);
			out.print("Thank you! That will be $");
			out.printf("%.2f", price);
			out.println('.');
			out.println("You have seats " + Integer.toString(preferredRow) + Character.toString(preferredStartingSeat) + " through " + Integer.toString(preferredRow) + Character.toString((char)((int)preferredStartingSeat + totalNumberOfSeatsRequested - 1)) + ".");
		}
	}
	
	//setters
	private void setTheater(Theater theater)
	{
		auditorium = theater;
	}
	private void setPreferredRow(Scanner userInput)
	{
		//get the preferred Row from the user
				boolean goodRow = false;
				do 
				{
					out.println("Select your row.");
					preferredRow = userInput.nextInt();
					
					if (preferredRow < 1 || preferredRow > auditorium.getRows()) // validate whether or not the row is in the theater
					{
						//row is not in the theater
						out.println("The row you selected is not in the theater. Please select an available row between 1 and " + Integer.toString(auditorium.getRows()));
					}
					else 
					{
						goodRow = true;
					}
				}
				while (!goodRow);
	}
	private void setPreferredStartingSeat(Scanner userInput)
	{
		//get the preferred Starting Seat from the user
				boolean goodStartingSeat = false;
				do 
				{
					out.println("Select the letter of your starting seat. (That is the left-most seat)");
					preferredStartingSeat = Character.toUpperCase(userInput.next().charAt(0)); //should get only the first character entered by the user as an uppercase letter
					
					if (preferredStartingSeat < 'A' || preferredStartingSeat > (char)(auditorium.getSeatsPerRow() + ASCII_A - 1)) // validate whether or not the seat is in the theater (second comparison converts to character from the number of seats per row plus 64 because 65 is 'A')
					{
						//row is not in the theater
						out.println("The starting seat you selected is not in the theater. Please select an available seat between A and " + Character.toString((char)(auditorium.getSeatsPerRow() + ASCII_A - 1)));
					}
					else 
					{
						goodStartingSeat = true;
					}
				}
				while (!goodStartingSeat);
	}
	private void setNumberOfAdultTickets(Scanner userInput)
	{
		//get the Number Of Adult Tickets from the user (this and the child and senior ticket loops could probably be their own function but this seemed easier)
				boolean goodNumberOfAdultTickets = false;
				do 
				{
					out.println("How many adult tickets would you like?");
					numberOfAdultTickets = userInput.nextInt();
					
					if (numberOfAdultTickets < 0 ) // only validates that at least 0 adult tickets are bought
					{
						//row is not in the theater
						out.println("The number of adult tickets must be at least 0.");
					}
					else 
					{
						goodNumberOfAdultTickets = true;
					}
				}
				while (!goodNumberOfAdultTickets);
	}
	private void setNumberOfChildTickets(Scanner userInput)
	{
		//get the Number Of Child Tickets from the user
				boolean goodNumberOfChildTickets = false;
				do 
				{
					out.println("How many child tickets would you like?");
					numberOfChildTickets = userInput.nextInt();
					
					if (numberOfChildTickets < 0 ) // only validates that at least 0 child tickets are bought
					{
						//row is not in the theater
						out.println("The number of child tickets must be at least 0.");
					}
					else 
					{
						goodNumberOfChildTickets = true;
					}
				}
				while (!goodNumberOfChildTickets);
	}
	private void setNumberOfSeniorTickets(Scanner userInput)
	{
		//get the Number Of Senior Tickets from the user
		boolean goodNumberOfSeniorTickets = false;
		do 
		{
			out.println("How many senior tickets would you like?");
			numberOfSeniorTickets = userInput.nextInt();
			
			if (numberOfSeniorTickets < 0 ) // only validates that at least 0 senior tickets are bought
			{
				//row is not in the theater
				out.println("The number of senior tickets must be at least 0.");
			}
			else 
			{
				goodNumberOfSeniorTickets = true;
			}
		}
		while (!goodNumberOfSeniorTickets);
	}

}

