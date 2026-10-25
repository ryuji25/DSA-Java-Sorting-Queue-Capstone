import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    static final int PARKING_CAPACITY = 30;
    static final ArrayList<String> plateNumbers = new ArrayList<>();
    static final ArrayList<String> vehicleTypes = new ArrayList<>();
    static final ArrayList<String> driverNames = new ArrayList<>();
    static final ArrayList<String> entryTimes = new ArrayList<>();
    static final ArrayList<Integer> priorityNumbers = new ArrayList<>();
    static final ArrayList<String> parkedVehicles = new ArrayList<>();
    static final Queue<String> entryQueue = new ArrayDeque<>();
    static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        seedData();
        boolean running = true;

        while (running) {
            showMainMenu();
            int choice = readMenuChoice();

            switch (choice) {
                case 1:
                    boolean registrationMenu = true;
                    while (registrationMenu) {
                        System.out.println();
                        System.out.println("[Sub-Menu 1: Vehicle Registration]");
                        System.out.println("1. Register new vehicle");
                        System.out.println("2. Display all registered vehicles");
                        System.out.println("3. Search vehicle by plate number");
                        System.out.println("4. Back to Main Menu");
                        System.out.print("Choose an option: ");

                        switch (readMenuChoice()) {
                            case 1:
                                registerVehicle();
                                break;
                            case 2:
                                displayRecords();
                                break;
                            case 3:
                                searchByPlate();
                                break;
                            case 4:
                                registrationMenu = false;
                                break;
                            default:
                                System.out.println("Invalid option. Please choose 1-4.");
                        }
                    }
                    break;
                case 2:
                    boolean queueMenu = true;
                    while (queueMenu) {
                        System.out.println();
                        System.out.println("[Sub-Menu 2: Queue Operations]");
                        System.out.println("1. Display waiting queue");
                        System.out.println("2. Peek next vehicle in queue");
                        System.out.println("3. Back to Main Menu");
                        System.out.print("Choose an option: ");

                        switch (readMenuChoice()) {
                            case 1:
                                displayQueue();
                                break;
                            case 2:
                                peekNextVehicle();
                                break;
                            case 3:
                                queueMenu = false;
                                break;
                            default:
                                System.out.println("Invalid option. Please choose 1-3.");
                        }
                    }
                    break;
                case 3:
                    boolean sortingMenu = true;
                    while (sortingMenu) {
                        System.out.println();
                        System.out.println("[Sub-Menu 3: Sorting Records]");
                        System.out.println("1. Sort records by plate number (Bubble Sort)");
                        System.out.println("2. Sort records by vehicle type (Selection Sort)");
                        System.out.println("3. Sort records by entry time (Insertion Sort)");
                        System.out.println("4. Back to Main Menu");
                        System.out.print("Choose an option: ");

                        switch (readMenuChoice()) {
                            case 1:
                                bubbleSortByPlate();
                                break;
                            case 2:
                                selectionSortByType();
                                break;
                            case 3:
                                insertionSortByEntryTime();
                                break;
                            case 4:
                                sortingMenu = false;
                                break;
                            default:
                                System.out.println("Invalid option. Please choose 1-4.");
                        }
                    }
                    break;
                case 4:
                    allowNextVehicleToEnter();
                    break;
                case 5:
                    vehicleExit();
                    break;
                case 6:
                    running = false;
                    System.out.println("Parking Entry System closed.");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-6.");
            }
        }

        input.close();
    }

    static void showMainMenu() {
        System.out.println();
        System.out.println("===================================================");
        System.out.println("                     MAIN MENU");
        System.out.printf("  [ Capacity: %d | Parked: %d | Waiting in Queue: %d ]%n",
                PARKING_CAPACITY, parkedVehicles.size(), entryQueue.size());
        System.out.println("===================================================");
        System.out.println("1. Vehicle Registration");
        System.out.println("2. Queue Operations");
        System.out.println("3. Sorting Records");
        System.out.println("4. Allow vehicle to enter");
        System.out.println("5. Vehicle exit (Free parking slot)");
        System.out.println("6. Exit");
        System.out.println();
        System.out.print("Choose an option: ");
    }

    static int readMenuChoice() {
        try {
            return Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    static void seedData() {
        addRecord("ABC-101", "Car", "Alice Morgan", "08:15", 1);
        parkedVehicles.add("ABC-101");
        addRecord("DEF-202", "Motorcycle", "Brian Lee", "08:30", 2);
        parkedVehicles.add("DEF-202");
        addRecord("GHI-303", "Van", "Carla Reyes", "08:45", 3);
        parkedVehicles.add("GHI-303");
        addRecord("JKL-404", "Car", "David Kim", "09:00", 1);
        parkedVehicles.add("JKL-404");
        addRecord("MNO-505", "Truck", "Emma Wilson", "09:15", 2);
        parkedVehicles.add("MNO-505");
        addRecord("PQR-606", "Motorcycle", "Farid Hassan", "09:30", 3);
        entryQueue.offer("PQR-606");
    }

    static void addRecord(String plateNumber, String vehicleType, String driverName,
                          String entryTime, int priorityNumber) {
        plateNumbers.add(plateNumber);
        vehicleTypes.add(vehicleType);
        driverNames.add(driverName);
        entryTimes.add(entryTime);
        priorityNumbers.add(priorityNumber);
    }

    static void registerVehicle() {
        System.out.println();
        System.out.println("Register new vehicle");
        System.out.print("Enter plate number: ");
        String plateNumber = input.nextLine().trim().toUpperCase();

        if (plateNumber.isEmpty()) {
            System.out.println("Plate number cannot be empty.");
            return;
        }
        if (findRecordIndex(plateNumber) >= 0) {
            System.out.println("A vehicle with that plate number is already registered.");
            return;
        }

        System.out.print("Enter vehicle type: ");
        String vehicleType = input.nextLine().trim();
        if (vehicleType.isEmpty()) {
            System.out.println("Vehicle type cannot be empty.");
            return;
        }

        System.out.print("Enter driver name: ");
        String driverName = input.nextLine().trim();
        if (driverName.isEmpty()) {
            System.out.println("Driver name cannot be empty.");
            return;
        }

        String entryTime = readEntryTime();
        if (entryTime == null) {
            return;
        }

        int priorityNumber = readPriority();
        if (priorityNumber == -1) {
            return;
        }

        addRecord(plateNumber, vehicleType, driverName, entryTime, priorityNumber);
        entryQueue.offer(plateNumber);
        System.out.println("Vehicle registered and added to the waiting queue.");
    }

    static String readEntryTime() {
        System.out.print("Enter entry time (HH:mm): ");
        String entryTime = input.nextLine().trim();
        if (!entryTime.matches("(?:[01]\\d|2[0-3]):[0-5]\\d")) {
            System.out.println(
                    "Invalid time. Enter a valid time in 24-hour HH:mm format (for example, 09:30).");
            return null;
        }
        return entryTime;
    }

    static int readPriority() {
        System.out.print("Enter priority (1-3): ");
        String value = input.nextLine().trim();

        try {
            int priority = Integer.parseInt(value);
            if (priority >= 1 && priority <= 3) {
                return priority;
            }
        } catch (NumberFormatException exception) {
            // The validation message below handles non-numeric input.
        }

        System.out.println("Invalid priority. Enter a whole number from 1 to 3.");
        return -1;
    }

    static void displayRecords() {
        if (plateNumbers.isEmpty()) {
            System.out.println("No vehicle records are available.");
            return;
        }

        System.out.println();
        System.out.printf("%-12s %-14s %-22s %-10s %-8s%n",
                "Plate", "Type", "Driver", "Entry Time", "Priority");
        System.out.println("---------------------------------------------------------------------");
        for (int index = 0; index < plateNumbers.size(); index++) {
            printRecord(index);
        }
    }

    static void printRecord(int index) {
        System.out.printf("%-12s %-14s %-22s %-10s %-8d%n",
                plateNumbers.get(index), vehicleTypes.get(index), driverNames.get(index),
                entryTimes.get(index), priorityNumbers.get(index));
    }

    static void searchByPlate() {
        System.out.print("Enter plate number to search: ");
        String plateNumber = input.nextLine().trim();
        int index = findRecordIndex(plateNumber);

        if (index < 0) {
            System.out.println("No registered vehicle found with plate number " + plateNumber + ".");
            return;
        }

        System.out.println();
        System.out.printf("%-12s %-14s %-22s %-10s %-8s%n",
                "Plate", "Type", "Driver", "Entry Time", "Priority");
        System.out.println("---------------------------------------------------------------------");
        printRecord(index);
    }

    static int findRecordIndex(String plateNumber) {
        for (int index = 0; index < plateNumbers.size(); index++) {
            if (plateNumbers.get(index).equalsIgnoreCase(plateNumber)) {
                return index;
            }
        }
        return -1;
    }

    static void displayQueue() {
        if (entryQueue.isEmpty()) {
            System.out.println("The waiting queue is empty.");
            return;
        }

        System.out.println();
        System.out.println("Waiting queue (front to back):");
        int position = 1;
        for (String plateNumber : entryQueue) {
            int recordIndex = findRecordIndex(plateNumber);
            if (recordIndex >= 0) {
                System.out.printf("%d. %s - %s (%s)%n", position, plateNumber,
                        vehicleTypes.get(recordIndex), driverNames.get(recordIndex));
            } else {
                System.out.printf("%d. %s - record not found%n", position, plateNumber);
            }
            position++;
        }
    }

    static void peekNextVehicle() {
        String plateNumber = entryQueue.peek();
        if (plateNumber == null) {
            System.out.println("The waiting queue is empty; there is no next vehicle.");
            return;
        }

        int recordIndex = findRecordIndex(plateNumber);
        if (recordIndex >= 0) {
            System.out.println("Next vehicle: " + plateNumber + " - "
                    + vehicleTypes.get(recordIndex) + ", driver " + driverNames.get(recordIndex));
        } else {
            System.out.println("Next plate number: " + plateNumber + " (vehicle record not found).");
        }
    }

    static void allowNextVehicleToEnter() {
        if (entryQueue.isEmpty()) {
            System.out.println("The waiting queue is empty; no vehicle can enter.");
            return;
        }
        if (parkedVehicles.size() >= PARKING_CAPACITY) {
            System.out.println("Parking is full. The next vehicle remains in the waiting queue.");
            return;
        }

        String plateNumber = entryQueue.poll();
        int recordIndex = findRecordIndex(plateNumber);
        parkedVehicles.add(plateNumber);
        if (recordIndex >= 0) {
            System.out.println("Vehicle " + plateNumber + " (" + vehicleTypes.get(recordIndex)
                    + "), driven by " + driverNames.get(recordIndex) + ", has entered and is parked.");
        } else {
            System.out.println("Vehicle " + plateNumber + " has entered, but its record was not found.");
        }
    }

    static void vehicleExit() {
        if (parkedVehicles.isEmpty()) {
            System.out.println("There are no parked vehicles to exit.");
            return;
        }

        System.out.println("Currently parked vehicles:");
        for (int index = 0; index < parkedVehicles.size(); index++) {
            System.out.printf("%d. %s%n", index + 1, parkedVehicles.get(index));
        }

        System.out.print("Enter the number of the vehicle exiting: ");
        int selection;
        try {
            selection = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException exception) {
            System.out.println("Invalid selection. Enter a vehicle number from the list.");
            return;
        }

        if (selection >= 1 && selection <= parkedVehicles.size()) {
            String plateNumber = parkedVehicles.remove(selection - 1);
            System.out.println("Vehicle " + plateNumber + " has exited. One parking slot is now free.");
        } else {
            System.out.println("Invalid selection. Choose a number from 1 to " + parkedVehicles.size() + ".");
        }
    }

    static void bubbleSortByPlate() {
        System.out.println("\nRecords before sorting by plate:");
        displayRecords();

        for (int pass = 0; pass < plateNumbers.size() - 1; pass++) {
            for (int index = 0; index < plateNumbers.size() - 1 - pass; index++) {
                if (plateNumbers.get(index).compareToIgnoreCase(plateNumbers.get(index + 1)) > 0) {
                    swapRecords(index, index + 1);
                }
            }
        }

        System.out.println("\nRecords after sorting by plate (Bubble Sort):");
        displayRecords();
    }

    static void selectionSortByType() {
        System.out.println("\nRecords before sorting by type:");
        displayRecords();

        for (int start = 0; start < vehicleTypes.size() - 1; start++) {
            int smallest = start;
            for (int index = start + 1; index < vehicleTypes.size(); index++) {
                if (vehicleTypes.get(index).compareToIgnoreCase(vehicleTypes.get(smallest)) < 0) {
                    smallest = index;
                }
            }

            if (smallest != start) {
                swapRecords(start, smallest);
            }
        }

        System.out.println("\nRecords after sorting by type (Selection Sort):");
        displayRecords();
    }

    static void insertionSortByEntryTime() {
        System.out.println("\nRecords before sorting by entry time:");
        displayRecords();

        for (int index = 1; index < entryTimes.size(); index++) {
            for (int current = index; current > 0
                    && entryTimes.get(current - 1).compareTo(entryTimes.get(current)) > 0; current--) {
                swapRecords(current - 1, current);
            }
        }

        System.out.println("\nRecords after sorting by entry time (Insertion Sort):");
        displayRecords();
    }

    static void swapRecords(int firstIndex, int secondIndex) {
        swap(plateNumbers, firstIndex, secondIndex);
        swap(vehicleTypes, firstIndex, secondIndex);
        swap(driverNames, firstIndex, secondIndex);
        swap(entryTimes, firstIndex, secondIndex);
        swap(priorityNumbers, firstIndex, secondIndex);
    }

    static <T> void swap(ArrayList<T> values, int firstIndex, int secondIndex) {
        T value = values.get(firstIndex);
        values.set(firstIndex, values.get(secondIndex));
        values.set(secondIndex, value);
    }
}
