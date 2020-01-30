/* Name: Isaac Blackwood 
 * NetID: idb170030
 * This program should meet the specifications outlined in pdf in this folder
 */
package Tickets;

public class Seat 
{
	private int row = 0;
	private char seat = '\0';
	private char type = '\0';
	
	Seat(int rowNumber, char seatLetter, char ticketType) 
	{
		setRow(rowNumber);
		setSeat(seatLetter);
		setType(ticketType);
	}
	
	//getters
	public int getRow() 
	{
		return row;
	}
	public char getSeat()
	{
		return seat;
	}
	public char getType()
	{
		return type;
	}
	
	//setters
	public void setRow(int rowNumber)
	{
		row = rowNumber;
	}
	public void setSeat(char seatLetter)
	{
		seat = seatLetter;
	}
	public void setType(char ticketType)
	{
		type = ticketType;
	}
}
