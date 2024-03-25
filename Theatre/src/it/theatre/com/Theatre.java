package it.theatre.com;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Theatre implements Serializable {
    // initialize required rows
    private static int[] row1 = new int[12];
    private static int[] row2 = new int[16];
    private static int[] row3 = new int[20];
    private static List<Ticket> tickets = new ArrayList<>();

    public static void main(String[] args)
    {
        // calling method to initialize 3 seat arrays
        initializeSeats(row1);
        initializeSeats(row2);
        initializeSeats(row3);

        // calling method to start program
        startMenu();
    }

    // this method starts the main menu and display welcome message
    public static void startMenu()
    {
        // get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        // print welcome message
        System.out.println();
        System.out.println("***********************************************");
        System.out.println("*                                             *");
        System.out.println("*        WELCOME TO THE NEW THEATRE           *");
        System.out.println("*                                             *");
        System.out.println("*              Date: " + formattedDateTime.substring(0, 10) + "               *");
        System.out.println("*              Time: " + formattedDateTime.substring(11) + "                 *");
        System.out.println("*                                             *");
        System.out.println("***********************************************");

        // print user manual
        System.out.println("\n---------------- USER MANUAL ------------------");
        System.out.println("Please select an option:");
        System.out.println("1) Buy a ticket");
        System.out.println("2) Print seating area");
        System.out.println("3) Cancel ticket");
        System.out.println("4) List available seats");
        System.out.println("5) Save to file");
        System.out.println("6) Load from file");
        System.out.println("7) Print ticket information and total price");
        System.out.println("8) Sort tickets by price");
        System.out.println("0) Quit");
        System.out.println("-----------------------------------------------");

        // calling method to validate user option
        int selectedChoice = validateMenuInputs();

        switch (selectedChoice)
        {
            case 1:
                buy_ticket();
                break;
            case 2:
                print_seating_area();
                break;
            case 3:
                cancel_ticket();
                break;
            case 4:
                show_available();
                break;
            case 5:
                save();
                break;
            case 6:
                load();
                break;
            case 7:
                show_tickets_info();
                break;
            case 8:
                sort_tickets();
                break;
            case 0:
                System.out.println("Program ended successfully...");
                System.exit(0);
                break;
        }
    }

    // this method handles buy ticket operation
    public static void buy_ticket()
    {
        if (checkEmptySeats(row1) || checkEmptySeats(row2) || checkEmptySeats(row3))
        {
            System.out.println("\n-------NEW SEAT RESERVATION--------");
            // Prompt user for row and seat number
            int row = rowInputValidator("Enter row number (1-3): ");
            int seat = seatInputValidator(row);
            int[] selectedRow = getRow(row);
            if (selectedRow != null && checkSelectedSeatIsEmpty(selectedRow, seat))
            {
                // validate and getting user details to confirm the ticket
                String name = userInputValidator("Please enter your name: ");
                String surname = userInputValidator("Please enter your surname: ");
                String email = userInputValidator("Please enter email name: ");
                double ticketPrice = ticketPriceInputValidator();
                Person person = new Person(name,surname,email);
                Ticket ticket = new Ticket(row,seat,ticketPrice,person);
                tickets.add(ticket);
                selectedRow[seat-1] = 1;
                System.out.println("Reservation successful - Seat " + seat + " in Row " + row);
                ticket.print();
                startMenu();

            }
            else
            {
                System.out.println("Sorry selected Seat " + seat + " in Row " + row + " is already occupied!");
                buy_ticket();
            }
        }
        else {
            System.out.println("Sorry there are no any seats available at the moment!");
            startMenu();
        }
    }

    // this method display the seating plan
    public static void print_seating_area()
    {
        System.out.println("\n-------NEW THEATRE SEATING PLAN--------\n");
        System.out.println("               ***********");
        System.out.println("                * STAGE *");
        System.out.println("               ***********");

        // Print the seats with appropriate spacing
        for (int j = 0; j < row1.length; j++)
        {
            if (j == 0)
            {
                System.out.print("             "); // Add gap for row 1
            }
            else if (j == row1.length / 2) {
                System.out.print("  ");
            }
            if (row1[j] == 0)
            {
                System.out.print("O");
            }
            else
            {
                System.out.print("X");
            }
        }
        System.out.println(); // Move to the next row

        // Print the seats with appropriate spacing
        for (int j = 0; j < row2.length; j++)
        {
            if (j == 0)
            {
                System.out.print("           "); // Add gap for row 2
            }
            else if (j == row2.length / 2)
            {
                System.out.print("  ");
            }
            if (row2[j] == 0) {
                System.out.print("O");
            }
            else
            {
                System.out.print("X");
            }
        }
        System.out.println(); // Move to the next row

        // Print the seats with appropriate spacing
        for (int j = 0; j < row3.length; j++)
        {
            if (j == 0)
            {
                System.out.print("          "); // Add gap for row 3
            }
            else if (j == row3.length / 2)
            {
                System.out.print("  ");
            }
            if (row3[j] == 0)
            {
                System.out.print("O");
            }
            else
            {
                System.out.print("X");
            }
        }
        System.out.println(); // Move to the next row
        startMenu();
    }

    // method to validate and handle ticket cancellation
    public static void cancel_ticket()
    {
        if (checkIsAllEmpty())
        {
            System.out.println("Sorry all the seats are empty!!");
            startMenu();
        }
        else
        {
            System.out.println("\n-------TICKET CANCELLATION--------");
            // validate and Prompt user for row and seat number
            int row = rowInputValidator("Enter row number (1-3) to cancel : ");
            int seat = seatInputValidator(row);
            int[] selectedRow = getRow(row);
            if (selectedRow != null && !checkSelectedSeatIsEmpty(selectedRow, seat))
            {
                boolean isDeleted = deleteTicket(row,seat);
                if (isDeleted)
                {
                    selectedRow[seat-1] = 0;
                    System.out.println("Reservation cancellation successful - Seat " + seat + " in Row " + row);
                    startMenu();
                }
                else
                {
                    System.out.println("Ticket was not found for Row " + row + " & Seat " + seat);
                    cancel_ticket();
                }

            }
            else
            {
                System.out.println("Sorry selected Seat " + seat + " in Row " + row + " is already empty!");
                cancel_ticket();
            }
        }
    }

    // method to display available seat lists
    public static void show_available() {
        System.out.println("\n-------AVAILABLE SEAT LIST--------");
        System.out.println("Seats available in row 1: " + get_available_seats(row1));
        System.out.println("Seats available in row 2: " + get_available_seats(row2));
        System.out.println("Seats available in row 3: " + get_available_seats(row3));
        startMenu();
    }

    // method to save program data (Serialization)
    public static void save() {
        try {
            // create a file output stream to write the objects to a file
            FileOutputStream fileOut = new FileOutputStream("theater_rows_state.ser");
            // create an object output stream to write the arrays as objects to the file
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            // write the arrays to the file
            out.writeObject(row1);
            out.writeObject(row2);
            out.writeObject(row3);
            // close the streams
            out.close();
            fileOut.close();
            System.out.println("Theater state saved to theater_rows_state.ser");
            saveTickets();
            startMenu();
        } catch (IOException e) {
            System.out.println("Error saving theater state: " + e.getMessage());
            startMenu();
        }
    }

    // method to load program data (deserialization)
    public static void load() {
        try {
            // create a file input stream to read the objects from the file
            FileInputStream fileIn = new FileInputStream("theater_rows_state.ser");
            // create an object input stream to read the arrays from the file
            ObjectInputStream in = new ObjectInputStream(fileIn);
            // read the arrays from the file and assign them to the corresponding variables
            row1 = (int[]) in.readObject();
            row2 = (int[]) in.readObject();
            row3 = (int[]) in.readObject();
            // close the streams
            in.close();
            fileIn.close();
            System.out.println("Theater state loaded from theater_state.ser");
            loadTickets();
            startMenu();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading theater seat data state: " + e.getMessage());
            startMenu();
        }
    }

    // method to save program ticket data (serialization)
    public static void saveTickets() {
        try {
            FileOutputStream fileOut = new FileOutputStream("tickets.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tickets);
            out.close();
            fileOut.close();
            System.out.println("Tickets saved.");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    // method to load program ticket data (deserialization)
    @SuppressWarnings("unchecked")
    public static void loadTickets() {
        try {
            FileInputStream fileIn = new FileInputStream("tickets.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tickets = (ArrayList<Ticket>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Tickets loaded.");
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading theater tickets: " + e.getMessage());
            startMenu();
        }
    }

    // method to display ticket detailed tickets data
    public static void show_tickets_info() {
        System.out.println("\n-------VIEW TICKET DETAILS--------");
        double total = 0.0;

        for (Ticket ticket: tickets) {
            ticket.print();
            System.out.println("----------------------------------------------------------");
            total += ticket.getPrice();
        }

        System.out.println("Total price of all tickets: £" + total);
        System.out.println("*********************");
        startMenu();
    }

    // method display sorted ticket data based on the price (ascending-cheapest first)
    public static void sort_tickets() {
        System.out.println("\n-------VIEW TICKET DETAILS (PRICE-ASCENDING)--------");
        List<Ticket> sortedTickets = new ArrayList<>((tickets));
        Collections.sort(sortedTickets);

        double total = 0.0;
        for (Ticket ticket: sortedTickets) {
            ticket.print();
            System.out.println("----------------------------------------------------------");
            total += ticket.getPrice();
        }

        System.out.println("Total price of all tickets: £" + total);
        System.out.println("*********************");
        startMenu();
    }

    // this method initialize unoccupied seats
    public static void initializeSeats(int[] row)
    {
        // Set all seats to free (0)
        for (int i = 0; i < row.length; i++)
        {
            row[i] = 0;
        }
    }

    // this method validate user input and returns a selected option in the start menu
    public static int validateMenuInputs()
    {
        Scanner y = new Scanner(System.in);
        while (true)
        {
            try {
                System.out.print("Select an Option to Continue: ");
                String input = y.nextLine().trim();
                int choice = Integer.parseInt(input);
                if (choice >= 0 && choice < 9)
                {
                    return choice;
                }
                else
                {
                    System.out.println("Invalid Number!\n");
                }
            }
            catch (Exception e){
                System.out.println("Invalid Data Type!\n");
            }
        }
    }

    // this method returns a validated user input.
    public static String userInputValidator(String question)
    {
        Scanner y = new Scanner(System.in);
        while (true){
            try {
                System.out.print(question);
                String input = y.nextLine().trim();
                if (input.length() > 0)
                {
                    return input;
                }
                else
                {
                    System.out.println("Invalid Entry!");
                }
            }
            catch (Exception e){
                System.out.println("Invalid Data Type!");
            }
        }
    }

    // method to check if the entered row num is valid
    public static int rowInputValidator(String message)
    {
        Scanner input = new Scanner(System.in);
        while (true)
        {
            try {
                System.out.print(message);
                String choice = input.nextLine().trim();
                int rowId = Integer.parseInt(choice);
                if (rowId >= 1 && rowId <= 3)
                {
                    return rowId;
                }
                else
                {
                    System.out.println("Invalid Row Number!");
                }
            }
            catch (Exception e){
                System.out.println("Invalid Data Type!");
            }
        }
    }
    // method to check if the entered ticket price is valid
    public static double ticketPriceInputValidator()
    {
        Scanner input = new Scanner(System.in);
        while (true){
            try {
                System.out.print("Please Enter ticket price: ");
                String choice = input.nextLine().trim();
                double position = Double.parseDouble(choice);
                if (position > 0){
                    return position;
                }
                else {
                    System.out.println("Invalid price!");
                }
            }
            catch (Exception e){
                System.out.println("Invalid Data Type!");
            }
        }
    }

    // method to check if the entered seat ID is valid
    public static int seatInputValidator(int rowNumber)
    {
        Scanner input = new Scanner(System.in);
        while (true){
            try {

                if (rowNumber == 1)
                {
                    System.out.print("Enter seat number (1-12 for row 1): ");
                    String choice = input.nextLine().trim();
                    int seatID = Integer.parseInt(choice);
                    if (seatID >=1 && seatID <= 12){
                            return seatID;
                    }
                    else
                    {
                        System.out.println("Invalid Row Number!");
                    }
                }
                else if (rowNumber == 2)
                {
                    System.out.print("Enter seat number (1-16 for row 2): ");
                    String choice = input.nextLine().trim();
                    int seatID = Integer.parseInt(choice);
                    if (seatID >=1 && seatID <= 16){
                        return seatID;
                    }
                    else
                    {
                        System.out.println("Invalid Row Number!");
                    }
                }
                else if (rowNumber == 3)
                {
                    System.out.print("Enter seat number (1-20 for row 3): ");
                    String choice = input.nextLine().trim();
                    int seatID = Integer.parseInt(choice);
                    if (seatID >=1 && seatID <= 20)
                    {
                        return seatID;
                    }
                    else
                    {
                        System.out.println("Invalid Row Number!");
                    }
                }
                else
                {
                    System.out.println("Invalid Seat Number!");
                }
            }
            catch (Exception e){
                System.out.println("Invalid Data Type!");
            }
        }
    }

    // method to check if the seats are available for a given row
    public static boolean checkEmptySeats(int [] row)
    {
        for (int j : row) {
            if (j == 0) {
                return true;
            }
        }
        return false;
    }

    // method to check if all rooms are empty
    public static boolean checkIsAllEmpty()
    {
        int emptyCount = 0;
        int[][] rows = {row1, row2, row3};
        for (int[] row : rows) {
            for (int i : row) {
                if (i == 0) {
                    emptyCount = emptyCount + 1;
                }
            }
        }
        return emptyCount == 48;
    }

    // method to check if seat is empty by row and seat ID
    public static boolean checkSelectedSeatIsEmpty(int [] row, int seatId)
    {
        return row[seatId-1] == 0;
    }

    public static int[] getRow(int num)
    {
        if (num == 1)
        {
            return row1;
        }
        else if (num == 2)
        {
            return row2;
        }
        else if (num == 3)
        {
            return row3;
        }
        return null;
    }

    // method to format and display available seat lists
    public static String get_available_seats(int[] row) {
        StringBuilder available_seats = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            if (row[i] == 0) {
                available_seats.append(i + 1).append(", ");
            }
        }
        // Remove the last comma and space
        if (!available_seats.toString().equals("")) {
            available_seats = new StringBuilder(available_seats.substring(0, available_seats.length() - 2));
        }
        return available_seats.toString();
    }

    // method to validate and check if ticket is deleted successfully
    public static boolean deleteTicket(int row,int seat)
    {
        for(Ticket ticket: tickets)
        {
            if (ticket.getRow() == row && ticket.getSeat() == seat)
            {
                tickets.remove(ticket);
                return true;
            }
        }
        return false;
    }
}
