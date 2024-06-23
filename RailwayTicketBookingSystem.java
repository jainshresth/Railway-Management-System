import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RailwayTicketBookingSystem {
    private Connection connection;
    private Scanner scanner;

    // public Main() {
    //     // Establish database connection
    //     try {
    //         connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/assignment", "root", "Aapk_9798");
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }
    public int getStationId(String stationName) {
    int stationId = -1; // Initialize with a default value

    try {
        // Query the database to get the station ID for the given station name
        String query = "SELECT StationID FROM Station WHERE Station_Name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, stationName);
        ResultSet resultSet = statement.executeQuery();

        // Check if the station name exists in the database
        if (resultSet.next()) {
            stationId = resultSet.getInt("StationID");
        } else {
            // If the station name is not found, handle it based on your requirement
            // For example, you can throw an exception, return a default value, or log the error
            System.out.println("Station not found: " + stationName);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle SQLException appropriately (e.g., log the error, throw custom exception)
    }

    return stationId;
}


    public RailwayTicketBookingSystem(){
        String url = "jdbc:mysql://localhost:3306/assignment";
        String username = "root";
        String password = "Aapk_9798";
        scanner = new Scanner(System.in);

        // Try to establish a database connection
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Attempt to connect to the database
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established successfully.");

            // Close the connection
            // connection.close();
            // System.out.println("Database connection closed.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading MySQL JDBC driver: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    // Method for user registration
    public boolean registerUser(String username, String password, String email, String phoneNumber) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO User (Username, Password, Email, Phone_Number) VALUES (?, ?, ?, ?)");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method for user login
    public boolean loginUser(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE Username = ? AND Password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String a = resultSet.getString("Password");
                if(a.equals(password))
                return true;
            return false;
            }
            return false; // Return true if a row is found, indicating successful login
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to search for trains
//     public void searchTrain(String travelDate, int sourceStationID, int destinationStationID) {
//     try {
//         // Query to retrieve trains running on the specified date and passing through the specified stations
//         String query = "SELECT DISTINCT t.TrainID, t.Train_Name, s.Station_Name AS Source_Station, d.Station_Name AS Destination_Station " +
//                        "FROM Train t " +
//                        "INNER JOIN TrainSchedule ts1 ON t.TrainID = ts1.TrainID " +
//                        "INNER JOIN TrainSchedule ts2 ON t.TrainID = ts2.TrainID " +
//                        "INNER JOIN Station s ON ts1.StationID = s.StationID " +
//                        "INNER JOIN Station d ON ts2.StationID = d.StationID " +
//                        "WHERE ts1.StationID = ? AND ts2.StationID = ? AND ? BETWEEN ts1.Arrival_Time AND ts2.Departure_Time";

//         PreparedStatement statement = connection.prepareStatement(query);
//         statement.setInt(1, sourceStationID);
//         statement.setInt(2, destinationStationID);
//         statement.setString(3, travelDate);

//         ResultSet resultSet = statement.executeQuery();

//         if (!resultSet.isBeforeFirst()) {
//             System.out.println("No trains found on the specified date and passing through the specified stations.");
//         } else {
//             System.out.println("Trains available on " + travelDate + " from station " + sourceStationID + " to station " + destinationStationID + ":");
//             while (resultSet.next()) {
//                 int trainID = resultSet.getInt("TrainID");
//                 String trainName = resultSet.getString("Train_Name");
//                 String sourceStation = resultSet.getString("Source_Station");
//                 String destinationStation = resultSet.getString("Destination_Station");
//                 System.out.println("Train ID: " + trainID + ", Train Name: " + trainName + ", Source Station: " + sourceStation + ", Destination Station: " + destinationStation);
//             }
//         }
//     } catch (SQLException e) {
//         e.printStackTrace();
//     }
// }
 public void searchTrains(String source, String destination, String travelDate) {
    try {
        String query = "SELECT t.* FROM Train t " +
                       "INNER JOIN Schedule s1 ON t.TrainID = s1.TrainID " +
                       "INNER JOIN Station src ON s1.StationID = src.StationID " +
                       "INNER JOIN Schedule s2 ON t.TrainID = s2.TrainID " +
                       "INNER JOIN Station dest ON s2.StationID = dest.StationID " +
                       "WHERE src.Station_Name = ? AND dest.Station_Name = ? AND s1.Date = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, source);
        statement.setString(2, destination);
        statement.setString(3, travelDate);

        ResultSet resultSet = statement.executeQuery();

        // Print the results
        while (resultSet.next()) {
            // Print train details
            System.out.println("Available Trains: " + resultSet.getInt("TrainID"));
            // Print other train details as needed
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}






    // Method to book tickets
//   public boolean bookTicket(int userID, int trainID, String travelClass, String bookingDate, int sourceStationID, int destinationStationID, String seatPreference) {
//     try {
//         // Check if the seat preference is available, if not, allocate any available seat
//         String seatQuery = "SELECT * FROM Seat WHERE TrainID = ? AND Class = ? AND Availability_Status = 'Available' AND Seat_Type = ?";
//         PreparedStatement seatStatement = connection.prepareStatement(seatQuery);
//         seatStatement.setInt(1, trainID);
//         seatStatement.setString(2, travelClass);
//         seatStatement.setString(3, seatPreference);
//         ResultSet seatResultSet = seatStatement.executeQuery();

//         int seatID;
//         if (seatResultSet.next()) {
//             seatID = seatResultSet.getInt("SeatID");
//         } else {
//             // If the preferred seat type is not available, allocate any available seat and set booking status to "Waiting"
//             String anySeatQuery = "SELECT * FROM Seat WHERE TrainID = ? AND Class = ? AND Availability_Status = 'Available'";
//             PreparedStatement anySeatStatement = connection.prepareStatement(anySeatQuery);
//             anySeatStatement.setInt(1, trainID);
//             anySeatStatement.setString(2, travelClass);
//             ResultSet anySeatResultSet = anySeatStatement.executeQuery();
//             if (anySeatResultSet.next()) {
//                 seatID = anySeatResultSet.getInt("SeatID");
//             } else {
//                 // No available seats found, set booking status to "Waiting"
//                 System.out.println("No available seats found. Setting booking status to 'Waiting'.");
//                 String waitingQuery = "INSERT INTO Booking (UserID, TrainID, Booking_Date, Status, Source_StationID, Destination_StationID, SeatID) VALUES (?, ?, ?, 'Waiting', ?, ?, ?)";
//                 PreparedStatement waitingStatement = connection.prepareStatement(waitingQuery);
//                 waitingStatement.setInt(1, userID);
//                 waitingStatement.setInt(2, trainID);
//                 waitingStatement.setString(3, bookingDate);
//                 waitingStatement.setInt(4, sourceStationID);
//                 waitingStatement.setInt(5, destinationStationID);
//                 waitingStatement.setInt(6, seatID);
//                 int rowsInserted = waitingStatement.executeUpdate();
//                 return rowsInserted > 0;
//             }
//         }

//         // Book the ticket using the selected seat
//         String query = "INSERT INTO Booking (UserID, TrainID, Booking_Date, Status, Source_StationID, Destination_StationID, SeatID) VALUES (?, ?, ?, 'Booked', ?, ?, ?)";
//         PreparedStatement statement = connection.prepareStatement(query);
//         statement.setInt(1, userID);
//         statement.setInt(2, trainID);
//         statement.setString(3, bookingDate);
//         statement.setInt(4, sourceStationID);
//         statement.setInt(5, destinationStationID);
//         statement.setInt(6, seatID);
//         int rowsInserted = statement.executeUpdate();

//         if (rowsInserted > 0) {
//             // Update the availability status of the booked seat
//             String updateSeatQuery = "UPDATE Seat SET Availability_Status = 'Booked' WHERE SeatID = ?";
//             PreparedStatement updateSeatStatement = connection.prepareStatement(updateSeatQuery);
//             updateSeatStatement.setInt(1, seatID);
//             updateSeatStatement.executeUpdate();

//             // Proceed to payment immediately after booking
//             System.out.print("Enter payment amount: ");
//             double paymentAmount = scanner.nextDouble();
//             scanner.nextLine(); // Consume newline
//             System.out.print("Enter payment method: ");
//             String paymentMethod = scanner.nextLine();
//             boolean paymentProcessed = processPayment(userID, paymentAmount, paymentMethod);
//             if (!paymentProcessed) {
//                 // Delete the booking if payment fails
//                 System.out.println("Payment failed. Deleting booking...");
//                 cancelTicket(userID); // Cancel the ticket that was just booked
//                 return false;
//             }
//         }

//         return rowsInserted > 0;
//     } catch (SQLException e) {
//         e.printStackTrace();
//         return false;
//     }
// // }
//    public boolean bookTicket(int userID, int trainID, String travelClass, String bookingDate, int sourceStationID, int destinationStationID, String seatPreference) {
//     int seatID = -1; // Initialize seatID with a default value
//     try {
//         // Check if the seat preference is available, if not, allocate any available seat
//         String seatQuery = "SELECT * FROM Seat WHERE TrainID = ? AND Class = ? AND Availability_Status = 'Available' AND Seat_Type = ?";
//         PreparedStatement seatStatement = connection.prepareStatement(seatQuery);
//         seatStatement.setInt(1, trainID);
//         seatStatement.setString(2, travelClass);
//         seatStatement.setString(3, seatPreference);
//         ResultSet seatResultSet = seatStatement.executeQuery();

//         if (seatResultSet.next()) {
//             seatID = seatResultSet.getInt("SeatID");
//         } else {
//             // If the preferred seat type is not available, allocate any available seat and set booking status to "Waiting"
//             String anySeatQuery = "SELECT * FROM Seat WHERE TrainID = ? AND Class = ? AND Availability_Status = 'Available'";
//             PreparedStatement anySeatStatement = connection.prepareStatement(anySeatQuery);
//             anySeatStatement.setInt(1, trainID);
//             anySeatStatement.setString(2, travelClass);
//             ResultSet anySeatResultSet = anySeatStatement.executeQuery();
//             if (anySeatResultSet.next()) {
//                 seatID = anySeatResultSet.getInt("SeatID");
//             } else {
//                 // No available seats found, set booking status to "Waiting"
//                 System.out.println("No available seats found. Setting booking status to 'Waiting'.");
//                 String waitingQuery = "INSERT INTO Booking (UserID, TrainID, Booking_Date, Status, Source_StationID, Destination_StationID) VALUES (?, ?, ?, 'Waiting', ?, ?)";
//                 PreparedStatement waitingStatement = connection.prepareStatement(waitingQuery);
//                 waitingStatement.setInt(1, userID);
//                 waitingStatement.setInt(2, trainID);
//                 waitingStatement.setString(3, bookingDate);
//                 waitingStatement.setInt(4, sourceStationID);
//                 waitingStatement.setInt(5, destinationStationID);
//                 // waitingStatement.setInt(6, seatID); // Assigning seatID here
//                 int rowsInserted = waitingStatement.executeUpdate();
//                 return rowsInserted > 0;
//             }
//         }

//         // Book the ticket using the selected seat
//         String query = "INSERT INTO Booking (UserID, TrainID, Booking_Date, Status, Source_StationID, Destination_StationID) VALUES (?, ?, ?, 'Booked', ?, ?)";
//         PreparedStatement statement = connection.prepareStatement(query);
//         statement.setInt(1, userID);
//         statement.setInt(2, trainID);
//         statement.setString(3, bookingDate);
//         statement.setInt(4, sourceStationID);
//         statement.setInt(5, destinationStationID);
//         // statement.setInt(6, seatID); // Assigning seatID here
//         int rowsInserted = statement.executeUpdate();

//         if (rowsInserted > 0) {
//             // Update the availability status of the booked seat
//             String updateSeatQuery = "UPDATE Seat SET Availability_Status = 'Booked' WHERE SeatID = ?";
//             PreparedStatement updateSeatStatement = connection.prepareStatement(updateSeatQuery);
//             updateSeatStatement.setInt(1, seatID);
//             updateSeatStatement.executeUpdate();

//             // Proceed to payment immediately after booking
//             System.out.print("Enter payment amount: ");
//             double paymentAmount = scanner.nextDouble();
//             scanner.nextLine(); // Consume newline
//             System.out.print("Enter payment method: ");
//             String paymentMethod = scanner.nextLine();
//             boolean paymentProcessed = processPayment(userID, paymentAmount, paymentMethod);
//             if (!paymentProcessed) {
//                 // Delete the booking if payment fails
//                 System.out.println("Payment failed. Deleting booking...");
//                 cancelTicket(userID); // Cancel the ticket that was just booked
//                 return false;
//             }
//         }

//         return rowsInserted > 0;
//     } catch (SQLException e) {
//         e.printStackTrace();
//         return false;
//     }
// }
public boolean bookTicket(int userID, int trainID, String travelClass, String bookingDate, int sourceStationID, int destinationStationID, String seatPreference) {
    int seatID = -1; // Initialize seatID with a default value
    int bookingID = -1;
    // Connection connection = null;
    PreparedStatement statement = null;
    PreparedStatement personStatement = null;
    
    try {
        connection.setAutoCommit(false);
        // connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        // connection.setAutoCommit(false); // Start transaction

        // Prompt user to enter passenger details and assign seats
                // Book the ticket with status "Booked"
        String query = "INSERT INTO Booking (UserID, TrainID, Booking_Date, Status, Source_StationID, Destination_StationID, Class) VALUES (?, ?, ?, 'Booked', ?, ?, ?)";
        statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, userID);
        statement.setInt(2, trainID);
        statement.setString(3, bookingDate);
        statement.setInt(4, sourceStationID);
        statement.setInt(5, destinationStationID);
        statement.setString(6,travelClass);
        int rowsInser = statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            bookingID = generatedKeys.getInt(1);
        }

        System.out.println("Enter Number of Passengers");
        int numOfPassengers =scanner.nextInt(); 
        scanner.nextLine();
        for (int i = 1; i <= numOfPassengers; i++) {
            System.out.println("Passenger " + i + " details:");
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter age: ");
            int age = scanner.nextInt();
            System.out.println("Enter Seat Preference");
            seatPreference= scanner.nextLine();
            scanner.nextLine(); // Consume newline
            
            // Check if the seat preference is available, if not, allocate any available seat
            String seatQuery = "SELECT * FROM Seat WHERE TrainID = ? AND Class = ? AND Availability_Status = 'Available' AND Seat_Type = ?";
            PreparedStatement seatStatement = connection.prepareStatement(seatQuery);
            seatStatement.setInt(1, trainID);
            seatStatement.setString(2, travelClass);
            seatStatement.setString(3, seatPreference);
            ResultSet seatResultSet = seatStatement.executeQuery();

            if (seatResultSet.next()) {
                seatID = seatResultSet.getInt("SeatID");
            String personQuery = "INSERT INTO Person (BookingID, UserID, Name, Age, Status, SeatID) VALUES (?, ?, ?, ?, ?, ?)";
            personStatement = connection.prepareStatement(personQuery);
            personStatement.setInt(1, bookingID);
            personStatement.setInt(2, userID);
            personStatement.setString(3, name);
            personStatement.setInt(4, age);
            personStatement.setString(5, "Confirmed");
            personStatement.setInt(6, seatID);
            int rowsInserted = personStatement.executeUpdate();
            if (rowsInserted <= 0) {
                connection.rollback(); // Rollback transaction
                return false;
            }
            } else {
                // If the preferred seat type is not available, allocate any available seat and set booking status to "Waiting"
                String anySeatQuery = "SELECT * FROM Seat WHERE TrainID = ? AND Class = ? AND Availability_Status = 'Available'";
                PreparedStatement anySeatStatement = connection.prepareStatement(anySeatQuery);
                anySeatStatement.setInt(1, trainID);
                anySeatStatement.setString(2, travelClass);
                ResultSet anySeatResultSet = anySeatStatement.executeQuery();
                if (anySeatResultSet.next()) {
                    seatID = anySeatResultSet.getInt("SeatID");
                    String personQuery = "INSERT INTO Person (BookingID, UserID, Name, Age, Status, SeatID) VALUES (?, ?, ?, ?, ?, ?)";
                    personStatement = connection.prepareStatement(personQuery);
                    personStatement.setInt(1, bookingID);
                    personStatement.setInt(2, userID);
                    personStatement.setString(3, name);
                    personStatement.setInt(4, age);
                    personStatement.setString(5, "Confirmed");
                    personStatement.setInt(6, seatID);
                    int rowsInserted = personStatement.executeUpdate();
                    if (rowsInserted <= 0) {
                        connection.rollback(); // Rollback transaction
                        return false;
                    }
                } else {
                    // No available seats found, set booking status to "Waiting"
                    System.out.println("No available seats found. Setting booking status to 'Waiting'.");
                    //String waitingQuery = "INSERT INTO Booking (UserID, TrainID, Booking_Date, Status, Source_StationID, Destination_StationID) VALUES (?, ?, ?, 'Waiting', ?, ?)";
                    String personQuery = "INSERT INTO Person (BookingID, UserID, Name, Age, Status) VALUES (?, ?, ?, ?, 'Waiting')";
                    personStatement = connection.prepareStatement(personQuery);
                    personStatement.setInt(1, bookingID);
                    personStatement.setInt(2, userID);
                    personStatement.setString(3, name);
                    personStatement.setInt(4, age);
                    // personStatement.setString(5, "Waiting");
                    //personStatement.setInt(6, seatID);
                    int rowsInserted = personStatement.executeUpdate();
                    // if (rowsInserted <= 0) {
                    //     connection.rollback(); // Rollback transaction
                    //     return false;
                    // }
                }
            }

            // Insert person entry with allocated seat
            // String personQuery = "INSERT INTO Person (BookingID, UserID, Name, Age, Status, SeatID) VALUES (?, ?, ?, ?, ?, ?)";
            // personStatement = connection.prepareStatement(personQuery);
            // personStatement.setInt(1, bookingID);
            // personStatement.setInt(2, userID);
            // personStatement.setString(3, name);
            // personStatement.setInt(4, age);
            // personStatement.setString(5, "Booked");
            // personStatement.setInt(6, seatID);
            // int rowsInserted = personStatement.executeUpdate();
            // if (rowsInserted <= 0) {
            //     connection.rollback(); // Rollback transaction
            //     return false;
            // }

            //Update the availability status of the booked seat
            String updateSeatQuery = "UPDATE Seat SET Availability_Status = 'Booked' WHERE SeatID = ?";
            PreparedStatement updateSeatStatement = connection.prepareStatement(updateSeatQuery);
            updateSeatStatement.setInt(1, seatID);
            int updateResult = updateSeatStatement.executeUpdate();
            // if (updateResult <= 0) {
            //     connection.rollback(); // Rollback transaction
            //     return false;
            // }
        }



        if (rowsInser > 0) {
            connection.commit(); // Commit transaction
            
            // Proceed to payment immediately after booking
            double paymentAmount = calculateFare(sourceStationID,destinationStationID,trainID , numOfPassengers,travelClass);
            System.out.println("Fare: " + paymentAmount);
            System.out.print("Enter payment method: ");
            String paymentMethod = scanner.nextLine();
            boolean paymentProcessed = processPayment(bookingID, paymentAmount, paymentMethod);
            if (!paymentProcessed) {
                // Delete the booking if payment fails
                System.out.println("Payment failed. Deleting booking...");
                cancelTicket(userID); // Cancel the ticket that was just booked
                return false;
            }
        }

        return rowsInser > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        try {
            if (connection != null) {
                connection.rollback(); // Rollback transaction on exception
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    } //finally {
    //     // Close resources
    //     try {
    //         if (statement != null) {
    //             statement.close();
    //         }
    //         if (personStatement != null) {
    //             personStatement.close();
    //         }
    //         if (connection != null) {
    //             connection.setAutoCommit(true); // Reset auto-commit
    //             connection.close();
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }
}







    // Method to modify booking details
    public boolean modifyBooking(int bookingID, int newSourceStationID, int newDestinationStationID) {
    try {
        // Check if the booking exists77777777777777
        String checkBookingQuery = "SELECT * FROM Booking WHERE BookingID = ?";
        PreparedStatement checkBookingStatement = connection.prepareStatement(checkBookingQuery);
        checkBookingStatement.setInt(1, bookingID);
        ResultSet bookingResultSet = checkBookingStatement.executeQuery();
        
        if (!bookingResultSet.next()) {
            // Booking doesn't exist
            System.out.println("Booking does not exist.");
            return false;
        }

        // Update the booking with new source and destination station IDs
        String query = "UPDATE Booking SET Source_StationID = ?, Destination_StationID = ? WHERE BookingID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, newSourceStationID);
        statement.setInt(2, newDestinationStationID);
        statement.setInt(3, bookingID);
        
        int rowsUpdated = statement.executeUpdate();
        
        if (rowsUpdated > 0) {
            System.out.println("Booking modified successfully.");
            return true;
        } else {
            System.out.println("Failed to modify booking.");
            return false;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    // Method to cancel tickets
//    public boolean cancelTicket(int bookingID) {
//     try {
//         // Retrieve the booking details before canceling
//         String query = "SELECT TrainID, Source_StationID, Destination_StationID FROM Booking WHERE BookingID = ?";
//         PreparedStatement statement = connection.prepareStatement(query);
//         statement.setInt(1, bookingID);
//         ResultSet resultSet = statement.executeQuery();
//         int trainID = 0, sourceStationID = 0, destinationStationID = 0;
//         if (resultSet.next()) {
//             trainID = resultSet.getInt("TrainID");
//             sourceStationID = resultSet.getInt("Source_StationID");
//             destinationStationID = resultSet.getInt("Destination_StationID");
//         }
        
//         // Count the number of passengers for this booking
//         query = "SELECT COUNT(*) AS num_passengers FROM Person WHERE BookingID = ?";
//         statement = connection.prepareStatement(query);
//         statement.setInt(1, bookingID);
//         resultSet = statement.executeQuery();
//         int numPassengers = 0;
//         if (resultSet.next()) {
//             numPassengers = resultSet.getInt("num_passengers");
//         }
        
//         // Cancel the booking
//         String cancelQuery = "DELETE FROM Booking WHERE BookingID = ?";
//         PreparedStatement cancelStatement = connection.prepareStatement(cancelQuery);
//         cancelStatement.setInt(1, bookingID);
//         int rowsDeleted = cancelStatement.executeUpdate();
        
//         // Delete corresponding person entries
//         String deletePersonQuery = "DELETE FROM Person WHERE BookingID = ?";
//         PreparedStatement deletePersonStatement = connection.prepareStatement(deletePersonQuery);
//         deletePersonStatement.setInt(1, bookingID);
//         int rowsDeletedPerson = deletePersonStatement.executeUpdate();

//         // If the booking is canceled successfully, return true
//             // Update the availability status of seats from "Booked" to "Available"
//             String updateSeatQuery = "UPDATE Seat SET Availability_Status = 'Available' WHERE TrainID = ? AND SeatID IN (SELECT SeatID FROM Person WHERE BookingID = ?)";
//             PreparedStatement updateSeatStatement = connection.prepareStatement(updateSeatQuery);
//             updateSeatStatement.setInt(1, trainID);
//             updateSeatStatement.setInt(2, bookingID);
//             int seatsUpdated = updateSeatStatement.executeUpdate();
            
//             // Check for waiting bookings and try to confirm them
//             String waitingQuery = "SELECT p.SeatID, p.BookingID FROM Person p LEFT JOIN Booking b ON p.BookingID = b.BookingID WHERE p.Status = 'Waiting' AND b.TrainID = ? AND b.Source_StationID = ? AND b.Destination_StationID = ? AND b.Class = ? LIMIT ?";
//             PreparedStatement waitingStatement = connection.prepareStatement(waitingQuery);
//             waitingStatement.setInt(1, trainID);
//             waitingStatement.setInt(2, sourceStationID);
//             waitingStatement.setInt(3, destinationStationID);
//             waitingStatement.setString(4, travelClass); // Set the travel class of the canceled booking
//             waitingStatement.setInt(5, numPassengers); // Limit the number of waiting bookings to the number of canceled passengers
//             ResultSet waitingResultSet = waitingStatement.executeQuery();
//             while (waitingResultSet.next()) {
//                 int seatID = waitingResultSet.getInt("SeatID");
//                 int waitingBookingID = waitingResultSet.getInt("BookingID");
//                 String name = waitingResultSet.nextLine("Name");
//                 // Update the status of the waiting booking to "Confirmed"
//                 String updateBookingQuery = "UPDATE Person SET Status = 'Confirmed' WHERE BookingID = ? AND Name = ?";
//                 PreparedStatement updateBookingStatement = connection.prepareStatement(updateBookingQuery);
//                 updateBookingStatement.setInt(1, waitingBookingID);
//                 updateBookingStatement.setInt(2,name)
//                 int rowsUpdated = updateBookingStatement.executeUpdate();
//                 if (rowsUpdated <= 0) {
//                     // If any waiting booking couldn't be confirmed, return false
//                     return false;
//                 }
//                 // Update the availability status of the seat to "Booked"
//                 String updateSeatStatusQuery = "UPDATE Seat SET Availability_Status = 'Booked' WHERE SeatID = ?";
//                 PreparedStatement updateSeatStatusStatement = connection.prepareStatement(updateSeatStatusQuery);
//                 updateSeatStatusStatement.setInt(1, seatID);
//                 int seatStatusUpdated = updateSeatStatusStatement.executeUpdate();
//                 if (seatStatusUpdated <= 0) {
//                     // If seat status couldn't be updated, return false
//                     return false;
//                 }
//             }
//             // If all waiting bookings are successfully confirmed or no waiting bookings exist, return true
//             return true;
//     } catch (SQLException e) {
//         e.printStackTrace();
//         return false;
//     }
// }
// 
public boolean cancelTicket(int bookingID) {
    try {
        // Retrieve the booking details before canceling
        int seatID = -1;
        String bookingQuery = "SELECT TrainID, Source_StationID, Destination_StationID, Booking_Date, Class FROM Booking WHERE BookingID = ?";
        PreparedStatement bookingStatement = connection.prepareStatement(bookingQuery);
        bookingStatement.setInt(1, bookingID);
        ResultSet bookingResultSet = bookingStatement.executeQuery();

        int trainID = 0, sourceStationID = 0, destinationStationID = 0;
        String seatClass = "";
        String bookingDate = "";
        if (bookingResultSet.next()) {
            trainID = bookingResultSet.getInt("TrainID");
            sourceStationID = bookingResultSet.getInt("Source_StationID");
            destinationStationID = bookingResultSet.getInt("Destination_StationID");
            seatClass = bookingResultSet.getString("Class");
            bookingDate = bookingResultSet.getString("Booking_Date");
        }

        // Count the number of passengers for this booking
        String query = "SELECT COUNT(*) AS num_passengers FROM Person WHERE BookingID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, bookingID);
        ResultSet result = statement.executeQuery();
        int numPassengers = 0;
        if (result.next()) {
            numPassengers = result.getInt("num_passengers");
        }

        // Update the availability status of seats from "Booked" to "Available"
        String updateSeatQuery = "UPDATE Seat SET Availability_Status = 'Available' WHERE TrainID = ? AND SeatID IN (SELECT SeatID FROM Person WHERE BookingID = ?)";
        PreparedStatement updateSeatStatement = connection.prepareStatement(updateSeatQuery);
        updateSeatStatement.setInt(1, trainID);
        updateSeatStatement.setInt(2, bookingID);
        int seatsUpdated = updateSeatStatement.executeUpdate();

        // Delete corresponding person entries
        String deletePersonQuery = "DELETE FROM Person WHERE BookingID = ?";
        PreparedStatement deletePersonStatement = connection.prepareStatement(deletePersonQuery);
        deletePersonStatement.setInt(1, bookingID);
        int rowsDeletedPerson = deletePersonStatement.executeUpdate();

        // Delete Payment
        String deletePayment = "DELETE FROM Payment WHERE BookingID = ?";
        PreparedStatement deletePaymentStatement = connection.prepareStatement(deletePayment);
        deletePaymentStatement.setInt(1, bookingID);
        int rowsDeletedPayment = deletePaymentStatement.executeUpdate();
        //Delete Porter
        String delete = "DELETE FROM Porter WHERE BookingID = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(delete);
        deleteStatement.setInt(1, bookingID);
        rowsDeletedPayment = deleteStatement.executeUpdate();

        // Cancel the booking
        String cancelQuery = "DELETE FROM Booking WHERE BookingID = ?";
        PreparedStatement cancelStatement = connection.prepareStatement(cancelQuery);
        cancelStatement.setInt(1, bookingID);
        int rowsDeleted = cancelStatement.executeUpdate();

        // If the booking is canceled successfully, update seat availability based on passenger status
        if (rowsDeleted > 0) {
            // Check for waiting bookings and try to confirm them
            String waitingQuery = "SELECT P.BookingID, P.Name FROM Person P JOIN Booking B ON P.BookingID = B.BookingID WHERE P.Status = 'Waiting' AND B.TrainID = ? AND B.Class = ? AND B.Booking_Date = ? LIMIT ?";
            PreparedStatement waitingStatement = connection.prepareStatement(waitingQuery);
            waitingStatement.setInt(1, trainID);
            waitingStatement.setString(2, seatClass);
            waitingStatement.setString(3, bookingDate);
            waitingStatement.setInt(4, numPassengers);
            ResultSet waitingResultSet = waitingStatement.executeQuery();

            while (waitingResultSet.next()) {
                int waitingBookingID = waitingResultSet.getInt("BookingID");
                String name = waitingResultSet.getString("Name");
                String anySeatQuery = "SELECT SeatID FROM Seat WHERE TrainID = ? AND Class = ? AND Availability_Status = 'Available' LIMIT 1";
                PreparedStatement anySeatStatement = connection.prepareStatement(anySeatQuery);
                anySeatStatement.setInt(1, trainID);
                anySeatStatement.setString(2, seatClass);
                ResultSet anySeatResultSet = anySeatStatement.executeQuery();
                
                if (anySeatResultSet.next()) {
                    seatID = anySeatResultSet.getInt("SeatID");
                    // Update the status of the waiting booking to "Confirmed"
                    String updateBookingQuery = "UPDATE Person SET Status = 'Confirmed', SeatID = ? WHERE BookingID = ? AND Name = ?";
                    PreparedStatement updateBookingStatement = connection.prepareStatement(updateBookingQuery);
                    updateBookingStatement.setInt(1, seatID);
                    updateBookingStatement.setInt(2, waitingBookingID);
                    updateBookingStatement.setString(3, name);
                    int rowsUpdated = updateBookingStatement.executeUpdate();

                    // if (rowsUpdated <= 0) {
                    //     // If any waiting booking couldn't be confirmed, return false
                    //     return false;
                    // }

                    // Update the availability status of the seat to "Booked"
                    String updateSeatStatusQuery = "UPDATE Seat SET Availability_Status = 'Booked' WHERE SeatID = ?";
                    PreparedStatement updateSeatStatusStatement = connection.prepareStatement(updateSeatStatusQuery);
                    updateSeatStatusStatement.setInt(1, seatID);
                    int seatStatusUpdated = updateSeatStatusStatement.executeUpdate();

                //     if (seatStatusUpdated <= 0) {
                //         // If seat status couldn't be updated, return false
                //         return false;
                //     }
                 } //else {
                //     // If no available seats for waiting booking, return false
                //     return false;
                // }
            }
            // If all waiting bookings are successfully confirmed or no waiting bookings exist, return true
            return true;
        } else {
            return false; // Return false if the booking couldn't be canceled
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}







    // Method to view booking history
//     public void viewBookingHistory(int userID) {
//     try {
//         String query = "SELECT B.BookingID, B.TrainID, B.Booking_Date, P.Name, P.Age, P.Status AS Passenger_Status " +
//                        "FROM Booking B JOIN Person P ON B.BookingID = P.BookingID " +
//                        "WHERE B.UserID = ?";
//         PreparedStatement statement = connection.prepareStatement(query);
//         statement.setInt(1, userID);
//         ResultSet resultSet = statement.executeQuery();

//         System.out.println("Booking History:");
//         System.out.println("------------------------------------------------------");
//         System.out.printf("| %-10s | %-8s | %-12s | %-20s | %-4s | %-10s |\n", 
//                             "BookingID", "TrainID", "Booking Date", "Passenger Name", "Age", "Passenger Status");
//         System.out.println("------------------------------------------------------");

//         while (resultSet.next()) {
//             int bookingID = resultSet.getInt("BookingID");
//             int trainID = resultSet.getInt("TrainID");
//             String bookingDate = resultSet.getString("Booking_Date");
//             String passengerName = resultSet.getString("Name");
//             int passengerAge = resultSet.getInt("Age");
//             String passengerStatus = resultSet.getString("Passenger_Status");

//             System.out.printf("| %-10d | %-8d | %-12s | %-20s | %-4d | %-10s |\n", 
//                                 bookingID, trainID, bookingDate, passengerName, passengerAge, passengerStatus);
//         }

//         System.out.println("------------------------------------------------------");
//     } catch (SQLException e) {
//         e.printStackTrace();
//         // Handle SQLException appropriately (e.g., log the error, throw custom exception)
//     }
// }
public void viewBookingHistory(int userID) {
    try {
        String query = "SELECT B.BookingID, B.TrainID, B.Booking_Date,S.Seat_Type,S.Class, P.Name, P.Age, P.Status AS Passenger_Status, M.Meal_Preference, " +
                       "CASE WHEN Pr.PorterID IS NOT NULL THEN 'Assigned' ELSE 'Not Assigned' END AS Porter_Assigned " +
                       "FROM Booking B " +
                       "JOIN Person P ON B.BookingID = P.BookingID " +
                       "LEFT JOIN Porter Pr ON B.BookingID = Pr.BookingID " +
                       "LEFT JOIN Meal M ON B.BookingID = M.BookingID " +
                       "LEFT JOIN Seat S ON S.SeatID = P.SeatID " +
                       "WHERE B.UserID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userID);
        ResultSet resultSet = statement.executeQuery();

        System.out.println("Booking History:");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-8s | %-12s | %-20s | %-4s | %-14s | %-15s | %-18s | %-18s | %-18s |\n", 
                            "BookingID", "TrainID", "Booking Date", "Passenger Name", "Age", "Passenger Status", "Porter Assigned", "Meal Preference","Class" ,"Seat Type");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        while (resultSet.next()) {
            int bookingID = resultSet.getInt("BookingID");
            int trainID = resultSet.getInt("TrainID");
            String bookingDate = resultSet.getString("Booking_Date");
            String passengerName = resultSet.getString("Name");
            int passengerAge = resultSet.getInt("Age");
            String passengerStatus = resultSet.getString("Passenger_Status");
            String porterAssigned = resultSet.getString("Porter_Assigned");
            String mealPreference = resultSet.getString("Meal_Preference");
            String cl = resultSet.getString("Class");
            String se = resultSet.getString("Seat_Type");

            System.out.printf("| %-10d | %-8d | %-12s | %-20s | %-4d | %-16s | %-15s | %-18s | %-18s | %-18s |\n", 
                                bookingID, trainID, bookingDate, passengerName, passengerAge, passengerStatus, porterAssigned, mealPreference,cl,se);
        }

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle SQLException appropriately (e.g., log the error, throw custom exception)
    }
}





    // Method to check train schedule
    public void printTrainSchedule(int trainID) {
    try {
        // Query to retrieve the train schedule with station names
        String query = "SELECT Schedule.Time, Station.Station_Name " +
                       "FROM Schedule " +
                       "JOIN Station ON Schedule.StationID = Station.StationID " +
                       "WHERE Schedule.TrainID = ? ";
                       
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, trainID);
        ResultSet resultSet = statement.executeQuery();
        
        System.out.println("Train Schedule for Train ID: " + trainID);
        System.out.println("-----------------------------------------------------");
        System.out.printf("%-20s %-20s\n", "Station Name", "Time");
        System.out.println("-----------------------------------------------------");
        
        // Print each station's schedule
        while (resultSet.next()) {
            String stationName = resultSet.getString("Station_Name");
            String arrivalTime = resultSet.getString("Time");
            
            System.out.printf("%-20s %-20s\n", stationName, arrivalTime);
        }
        
        System.out.println("-----------------------------------------------------");
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // Method to purchase travel insurance
    public boolean purchaseInsurance(int bookingID, String coverage) {
        try {
            // Execute SQL query to purchase travel insurance
            String query = "INSERT INTO Insurance (BookingID, Coverage) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);
            statement.setString(2, coverage);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public  double calculateFare(int sourceStationID, int destinationStationID, int trainID,int no, String travelClass) {
        // Get the distance between the stations from the Schedule table
        int a = 0;
        if(travelClass.equals("First Class"))
            a = 7;
        else 
            a = 5;
        int distance = getDistance(sourceStationID, destinationStationID,trainID);
        
        // Calculate fare
        double fare = distance * a *no+ 34.67;
        
        return fare;
    }
    
    // Function to get distance between stations from the Schedule table
    private  int getDistance(int sourceStationID, int destinationStationID, int trainID) {
        int distance = 0;
        int sdistance = 0;
        int ddistance = 0;
        try{
            String query = "SELECT Distance FROM Schedule WHERE StationID = ? AND TrainID = ?";
            String q = "SELECT Distance FROM Schedule WHERE StationID = ? AND TrainID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, sourceStationID);
            statement.setInt(2, trainID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                sdistance = resultSet.getInt("Distance");
            }
            statement = connection.prepareStatement(q);
            statement.setInt(1, destinationStationID);
            statement.setInt(2, trainID);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ddistance = resultSet.getInt("Distance");
            }
            distance = ddistance - sdistance;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distance;
    }

    // Method to select meal preferences
    public boolean selectMealPreferences(int bookingID, String mealPreference) {
        try {
            // Execute SQL query to select meal preferences
            String query = "INSERT INTO Meal (BookingID, Meal_Preference) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);
            statement.setString(2, mealPreference);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to choose seat preferences
    public boolean chooseSeatPreferences(int bookingID, int seatID) {
        try {
            // Execute SQL query to choose seat preferences
            String query = "UPDATE Person SET SeatID = ? WHERE BookingID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, seatID);
            statement.setInt(2, bookingID);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to check PNR status
    public String checkPNRStatus(int bookingID) {
        try {
            // Execute SQL query to retrieve PNR status
            String query = "SELECT PNR_No FROM Booking WHERE BookingID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("PNR_No");
            } else {
                return "PNR not found";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    // Method to process payments
    public boolean processPayment(int bookingID, double amount, String paymentMethod) {
        try {
            // Execute SQL query to process payment
            String query = "INSERT INTO Payment (BookingID, Amount, Payment_Method) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);
            statement.setDouble(2, amount);
            statement.setString(3, paymentMethod);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to provide feedback
    public boolean provideFeedback(int userID, int bookingID, int rating, String comments) {
        try {
            // Execute SQL query to provide feedback
            String query = "INSERT INTO Feedback (UserID, BookingID, Rating, Comments) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            statement.setInt(2, bookingID);
            statement.setInt(3, rating);
            statement.setString(4, comments);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to request porter service
   public boolean assignPorter(int bookingID, String portingStation) {
    PreparedStatement porterStatement = null;
    ResultSet porterResultSet = null;

    try {
        // Retrieve the station ID and train ID from the booking table using the provided bookingID
        String stationQuery = "SELECT Source_StationID, Destination_StationID, TrainID FROM Booking WHERE BookingID = ?";
        PreparedStatement stationStatement = connection.prepareStatement(stationQuery);
        stationStatement.setInt(1, bookingID);
        ResultSet stationResultSet = stationStatement.executeQuery();
        
        int stationID = -1; // Initialize with a default value
        int trainID = -1; // Initialize with a default value
        if (stationResultSet.next()) {
            if (portingStation.equalsIgnoreCase("source")) {
                stationID = stationResultSet.getInt("Source_StationID");
            } else if (portingStation.equalsIgnoreCase("destination")) {
                stationID = stationResultSet.getInt("Destination_StationID");
            } else {
                System.out.println("Invalid porting station specified.");
                return false;
            }
            trainID = stationResultSet.getInt("TrainID");
        } else {
            System.out.println("Invalid booking ID.");
            return false;
        }

        // Check if a porter is available for the given station and train
        String porterQuery = "SELECT PorterID FROM Porter WHERE StationID = ? AND TrainID = ? AND BookingID IS NULL";
        porterStatement = connection.prepareStatement(porterQuery);
        porterStatement.setInt(1, stationID);
        porterStatement.setInt(2, trainID);
        porterResultSet = porterStatement.executeQuery();

        if (porterResultSet.next()) {
            int porterID = porterResultSet.getInt("PorterID");
            
            // Assign the porter to the booking
            String assignPorterQuery = "UPDATE Porter SET BookingID = ? WHERE PorterID = ?";
            PreparedStatement assignPorterStatement = connection.prepareStatement(assignPorterQuery);
            assignPorterStatement.setInt(1, bookingID);
            assignPorterStatement.setInt(2, porterID);
            int rowsUpdated = assignPorterStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Porter assigned successfully to BookingID: " + bookingID);
                return true;
            } else {
                System.out.println("Failed to assign porter to BookingID: " + bookingID);
                return false;
            }
        } else {
            System.out.println("No available porter for StationID: " + stationID + " and TrainID: " + trainID);
            return false;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    } finally {
        // Close resources
        try {
            if (porterResultSet != null) {
                porterResultSet.close();
            }
            if (porterStatement != null) {
                porterStatement.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}


    public int findUserID(String username, String password) {
    try {
        String query = "SELECT UserID FROM User WHERE Username = ? AND Password = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            return resultSet.getInt("UserID");
        } else {
            // User not found
            return -1;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return -1;
    }
}


    public static void main(String[] args) {
      // String url = "jdbc:mysql://localhost:3306/assignment";
      //   String username = "root";
      //   String password = "Aapk_9798";

      //   // Try to establish a database connection
      //   try {
      //       // Load the MySQL JDBC driver
      //       Class.forName("com.mysql.cj.jdbc.Driver");

      //       // Attempt to connect to the database
      //       System.out.println("Connecting to database...");
      //       Connection connection = DriverManager.getConnection(url, username, password);
      //       System.out.println("Database connection established successfully.");

      //       // Close the connection
      //       // connection.close();
      //       // System.out.println("Database connection closed.");
      //   } catch (ClassNotFoundException e) {
      //       System.err.println("Error loading MySQL JDBC driver: " + e.getMessage());
      //   } catch (SQLException e) {
      //       System.err.println("Error connecting to the database: " + e.getMessage());
      //   }
      //}
        RailwayTicketBookingSystem bookingSystem = new RailwayTicketBookingSystem();
        Scanner scanner = new Scanner(System.in);
boolean loggedIn = false;
int User = 0;
        while (!loggedIn) {
            System.out.println("\nRailway Ticket Booking System");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("16. Exit");
            int c = scanner.nextInt();
            scanner.nextLine();
            
            

                switch(c){
                     case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter phone number: ");
                    String phoneNumber = scanner.nextLine();
                    boolean registered = bookingSystem.registerUser(username, password, email, phoneNumber);
                    if (registered) {
                        System.out.println("User registered successfully.");
                    } else {
                        System.out.println("Failed to register user.");
                    }
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();
                    loggedIn = bookingSystem.loginUser(loginUsername, loginPassword);
                    if (loggedIn) {
                        System.out.println("User logged in successfully.");
                        User = bookingSystem.findUserID(loginUsername,loginPassword);
                    } else {
                        System.out.println("Login failed. Invalid username or password.");
                    }
                    break;
                case 16:
                    System.out.println("Exiting program...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
            while(true){
            System.out.println("3. Search Trains");
            System.out.println("4. Book Ticket");
            System.out.println("5. Modify Booking");
            System.out.println("6. Cancel Ticket");
            System.out.println("7. View Booking History");
            System.out.println("8. Check Train Schedule");
            System.out.println("9. Purchase Insurance");
            System.out.println("10. Select Meal Preferences");
            System.out.println("11. Choose Seat Preferences");
            System.out.println("12. Check PNR Status");
            System.out.println("13. Process Payment");
            System.out.println("14. Provide Feedback");
            System.out.println("15. Request Porter Service");
            System.out.println("16. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
           case 3:
    System.out.print("Enter source: ");
    String source = scanner.nextLine();
    System.out.print("Enter destination: ");
    String destination = scanner.nextLine();
    System.out.print("Enter travel date (YYYY-MM-DD): ");
    String travelDate = scanner.nextLine();

    // Invoke the modified searchTrain method and retrieve the ResultSet
    bookingSystem.searchTrains(source, destination, travelDate);
    break;

            case 4:
        System.out.print("Enter travel class: ");
        String bookingTravelClass = scanner.nextLine();
        System.out.print("Enter booking date: ");
        String bookingDate = scanner.nextLine();
        System.out.print("Enter source station: ");
        String ssource= scanner.nextLine();
        int sourceStationID = bookingSystem.getStationId(ssource);
        System.out.print("Enter destination station: ");
       String ddestination = scanner.nextLine();
        int destinationStationID =  bookingSystem.getStationId(ddestination);
       bookingSystem.searchTrains(ssource, ddestination, bookingDate);
         System.out.print("Enter train ID: ");
        int trainID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter seat preference: ");
        String seatPreference = scanner.nextLine();
                     
        // Attempt to book the ticket
        boolean booked = bookingSystem.bookTicket(User, trainID, bookingTravelClass, bookingDate, sourceStationID, destinationStationID, seatPreference);
        if (booked) {
            System.out.println("Ticket booked successfully.");
        } else {
            System.out.println("Failed to book ticket.");
        }
    break;



            case 5:
                System.out.print("Enter booking ID: ");
                int modifyBookingID = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                // System.out.print("Enter new booking date: ");
                // String newBookingDate = scanner.nextLine();
                System.out.print("Enter new source station ID: ");
                int newSourceStationID = scanner.nextInt();
                System.out.print("Enter new destination station ID: ");
                int newDestinationStationID = scanner.nextInt();
                boolean modified = bookingSystem.modifyBooking(modifyBookingID, newSourceStationID, newDestinationStationID);
                if (modified) {
                    System.out.println("Booking modified successfully.");
                } else {
                    System.out.println("Failed to modify booking.");
                }
                break;
            case 6:
                System.out.print("Enter booking ID to cancel: ");
                int cancelBookingID = scanner.nextInt();
                boolean canceled = bookingSystem.cancelTicket(cancelBookingID);
                if (canceled) {
                    System.out.println("Ticket canceled successfully.");
                } else {
                    System.out.println("Failed to cancel ticket.");
                }
                break;
            case 7:
                // System.out.print("Enter user ID to view booking history: ");
                // int viewUserID = scanner.nextInt();
                bookingSystem.viewBookingHistory(User);
                // Process the ResultSet
                break;
            case 8:
                System.out.print("Enter train ID to check schedule: ");
                int checkTrainID = scanner.nextInt();
                bookingSystem.printTrainSchedule(checkTrainID);
                // Process the ResultSet
                break;
            case 9:
                System.out.print("Enter booking ID for insurance: ");
                int insuranceBookingID = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter insurance coverage: ");
                String coverage = scanner.nextLine();
                boolean insurancePurchased = bookingSystem.purchaseInsurance(insuranceBookingID, coverage);
                if (insurancePurchased) {
                    System.out.println("Insurance purchased successfully.");
                } else {
                    System.out.println("Failed to purchase insurance.");
                }
                break;
            case 10:
                System.out.print("Enter booking ID for meal preference: ");
                int mealBookingID = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter meal preference: ");
                String mealPreference = scanner.nextLine();
                boolean mealSelected = bookingSystem.selectMealPreferences(mealBookingID, mealPreference);
                if (mealSelected) {
                    System.out.println("Meal preference selected successfully.");
                } else {
                    System.out.println("Failed to select meal preference.");
                }
                break;
            case 11:
                System.out.print("Enter booking ID for seat preference: ");
                int seatBookingID = scanner.nextInt();
                System.out.print("Enter seat ID: ");
                int seatID = scanner.nextInt();
                boolean seatChosen = bookingSystem.chooseSeatPreferences(seatBookingID, seatID);
                if (seatChosen) {
                    System.out.println("Seat preference chosen successfully.");
                } else {
                    System.out.println("Failed to choose seat preference.");
                }
                break;
            case 12:
                System.out.print("Enter booking ID to check PNR status: ");
                int checkBookingID = scanner.nextInt();
                String pnrStatus = bookingSystem.checkPNRStatus(checkBookingID);
                System.out.println("PNR Status: " + pnrStatus);
                break;
            // case 13:
            //     System.out.print("Enter booking ID for payment: ");
            //     int paymentBookingID = scanner.nextInt();
            //     System.out.print("Enter payment amount: ");
            //     double paymentAmount = scanner.nextDouble();
            //     scanner.nextLine(); // Consume newline
            //     System.out.print("Enter payment method: ");
            //     String paymentMethod = scanner.nextLine();
            //     // boolean paymentProcessed = bookingSystem.processPayment(paymentBookingID, paymentAmount, paymentMethod);
            //     if (paymentProcessed) {
            //         System.out.println("Payment processed successfully.");
            //     } else {
            //         System.out.println("Failed to process payment.");
            //     }
            //     break;
            case 14:
                System.out.print("Enter user ID: ");
                int feedbackUserID = scanner.nextInt();
                System.out.print("Enter booking ID: ");
                int feedbackBookingID = scanner.nextInt();
                System.out.print("Enter rating: ");
                int rating = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter comments: ");
                String comments = scanner.nextLine();
                boolean feedbackProvided = bookingSystem.provideFeedback(feedbackUserID, feedbackBookingID, rating, comments);
                if (feedbackProvided) {
                    System.out.println("Feedback provided successfully.");
                } else {
                    System.out.println("Failed to provide feedback.");
                }
                break;
            case 15:
                System.out.print("Enter booking ID: ");
                int porterBookingID = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter station(Source/Destination): ");
                String porterStationID = scanner.nextLine();
                 boolean porterRequested = bookingSystem.assignPorter(porterBookingID,porterStationID);
                if (porterRequested) {
                    System.out.println("Porter service requested successfully.");
                } else {
                    System.out.println("Failed to request porter service.");
                }
                break;
            case 16:
                System.out.println("Exiting program...");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
            }
        }

        }
    }
