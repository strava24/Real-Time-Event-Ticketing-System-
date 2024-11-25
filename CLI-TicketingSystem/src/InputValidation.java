// This will be a utility class used just to get valid inputs from the user

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public final class InputValidation {

    private static final Scanner input = new Scanner(System.in);

    // To prevent object initialization cause this is a utility class
    private InputValidation() {}

    /**
     * Method to get valid input from the user as total tickets
     * @return the totalTickets
     */
    public static int getValidTotalTickets() {
        int totalTickets = 0;

        while (totalTickets <= 0) {
            System.out.print("Enter the total number of tickets on the event: ");
            try {
                totalTickets = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Total ticket count should be an integer.");
                input.nextLine(); // To clear the buffer
            }

            if (totalTickets < 0) {
                System.out.println("Total number of tickets cannot be less than 0.");
            } else if (totalTickets == 0) {
                System.out.println("Cannot host an event without any tickets.");
            }
        }

        return totalTickets;

    }

    /**
     * Method to get valid ticketReleaseRate and valid customerRetrievalRate
     * @param rateType to differentiate ticketReleaseRate and customerRetrievalRate
     * @return either the ticketReleaseRate or customerRetrievalRate
     */
    public static long getValidRate(String rateType) {
        long rate = 0;

        while (rate <= 0) {
            System.out.print("Enter the ticket " + rateType + " for the event (in milli seconds): ");
            try {
                rate = input.nextLong();
            } catch (InputMismatchException e) {
                System.out.println("Ticket " + rateType + " should be an integer.");
                input.nextLine(); // To clear the buffer
            }

            if (rate < 0) {
                System.out.println("Ticket  " + rateType + "  cannot be less than 0.");
            } else if (rate == 0 && rateType.equals("release rate")) {
                System.out.println("Cannot host an event without releasing any tickets.");
            } else if (rate == 0 && rateType.equals("retrieval rate")) {
                System.out.println("Ticket retrieval is mandatory to simulate automation in the system.");
            }

        }

        return rate;
    }


    /**
     * Method to get valid maxTicketCapacity for the system
     * @param totalTickets - to compare and make sure maxTicketCapacity doesn't exceed totalTickets
     * @return maxTicketCapacity
     */
    public static int getValidMaxTicketCapacity(int totalTickets) {
        int maxTicketCapacity = 0;

        while (maxTicketCapacity <= 0 || maxTicketCapacity > totalTickets) {
            System.out.print("Enter the max ticket capacity for the ticket pool: ");

            try {
                maxTicketCapacity = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Max ticket capacity must be an integer.");
                input.nextLine(); // To clean the buffer
            }

            if (maxTicketCapacity < 0) {
                System.out.println("Max ticket capacity cannot be less than 0.");
            } else if (maxTicketCapacity == 0) {
                System.out.println("Cannot host an event without releasing any tickets.");
            } else if (maxTicketCapacity > totalTickets) {
                System.out.println("Max ticket capacity of a ticket pool cannot be greater than the total number of tickets for the event. ");
            }
        }

        return maxTicketCapacity;

    }

    /**
     * This method is to get a valid index among to choose a valid index among the configurations
     * @param configs - list of Configurations
     * @return - returns a valid configuration if there is no configurations saved on the DB returns null
     */
    public static int getValidIndex(List<Configuration> configs) {
        for (int i = 0; i < configs.size(); i++) {
            System.out.println(i + " - " + configs.get(i).toString() + "\n"); // Printing all the configurations on the backend along with the index
        }

        int index = 0;
        boolean notValid;

        do {
            notValid = true;
            System.out.print("Enter the index of your chosen configuration: ");
            try {
                index = input.nextInt();

                if (index < 0 || index > configs.size() - 1) {
                    notValid = false;
                    System.out.println("There is no such index!");
                }

            } catch (InputMismatchException e) {
                notValid = false;
                System.out.println("Please enter a valid index");
                input.nextLine(); // cleaning the buffer
            }
        } while (!notValid);

        return index;

    }

}
