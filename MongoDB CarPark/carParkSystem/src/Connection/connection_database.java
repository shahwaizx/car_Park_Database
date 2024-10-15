package Connection;
import java.sql.*;
import java.text.DecimalFormat;



import javax.swing.*;


public class connection_database {
    public static String url="jdbc:mysql://localhost:3306/CARPARK";
    public static final String username="root";
    public static final String password="admin";
    public static Connection connection;
    public static String currentUser;


    public connection_database(){
        makeConnection();
    }

    public static void makeConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection=DriverManager.getConnection(url,username,password);

        }catch(Exception e){
            e.printStackTrace();

        }
    }
    public Boolean verifyAdmin(String username,String password){
        try {
            PreparedStatement preparedStatement=connection.prepareStatement("Select * from Admin");
            ResultSet resultSet=preparedStatement.executeQuery();
            String usernameDB=null;
            String passwordDB=null;
            while(resultSet.next()){
                 usernameDB =resultSet.getString("username");
                 passwordDB=resultSet.getString("password");

            }
            if(usernameDB.equalsIgnoreCase(username) && passwordDB.equalsIgnoreCase(password)){
                return true;

            }else{
                return false;
            }




        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public Boolean verifyCustomer(String username, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Customer WHERE username = ?");
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if there is at least one row in the result set
            if (resultSet.next()) {
                String usernameDB = resultSet.getString("username");
                System.out.println(usernameDB);
                String passwordDB = resultSet.getString("password");
                currentUser = resultSet.getString("username");

                if (usernameDB.equalsIgnoreCase(username) && passwordDB.equalsIgnoreCase(password)) {
                    System.out.println("check11");
                    return true;
                } else {
                    return false;
                }
            } else {
                // No rows found with the given username
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public Boolean verifyBooking(String username, int Id,String level,int slotNo) {
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Booking WHERE idBooking = ?");
            preparedStatement.setInt(1, Id);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if there is at least one row in the result set
            if (resultSet.next()) {
                String usernameDB = resultSet.getString("Customer_username");
                System.out.println(usernameDB);
                String levelDB = resultSet.getString("LevelType");
                int slotDB = resultSet.getInt("SlotNum");





                if (usernameDB.equalsIgnoreCase(username) && levelDB.equalsIgnoreCase(level) && slotDB == slotNo ) {
                    System.out.println("check11");
                    return true;
                } else {
                    return false;
                }
            } else {
                // No rows found with the given username
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getUser(){
        return currentUser;
    }
    public boolean registerCustomer(String name, String cnic, String phoneNo, String username, String password) {
        try {
            // Check if the username is already taken
            if (isUsernameTaken(username)) {
                System.out.println("Username is already taken. Please choose a different one.");
                return false;
            }

            // If the username is not taken, proceed with the registration
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Customer (name, cnic, phone_no, username, password) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, cnic);
            preparedStatement.setString(3, phoneNo);
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, password);

            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insertion was successful
            if (rowsAffected > 0) {
                // Retrieve the auto-generated customer_id
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int customerId = generatedKeys.getInt(1);
                    System.out.println("Registration successful. Customer ID: " + customerId);
                } else {
                    System.out.println("Registration successful, but unable to retrieve Customer ID.");
                }
                return true;
            } else {
                System.out.println("Registration failed");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean bookRide(String carName,String levelType,int slotNo,String category,String NumPlate,String username){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO BOOKING(CarName, LevelType, SlotNum, Category, Num_Plate, Customer_username, Arrival_time) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,carName);
            preparedStatement.setString(2,levelType);
            preparedStatement.setInt(3, slotNo);
            preparedStatement.setString(4,category);
            preparedStatement.setString(5,NumPlate);
            preparedStatement.setString(6,username);

            long currentTimeMillis = System.currentTimeMillis();
            // Create a Timestamp object
            Timestamp timestamp = new Timestamp(currentTimeMillis);

            // Set the Timestamp parameter
            preparedStatement.setTimestamp(7, timestamp);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if the insertion was successful
            if (rowsAffected > 0) {
                // Retrieve the auto-generated customer_id
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int bookingId = generatedKeys.getInt(1);
                    System.out.println("Booking successful. Booking ID: " + bookingId);
                    JOptionPane.showMessageDialog(new JFrame(),"YOUR BOOKING-ID IS: " + bookingId);
                } else {
                    System.out.println("Booking successful, but unable to retrieve Booking ID.");
                }
                return true;
            } else {
                System.out.println("Booking failed");
                return false;
            }

        }catch(Exception e1){
            System.out.println(e1.getMessage());
            return false;
        }
    }
    public boolean billShow(int bookID){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT CarName,Category,Num_Plate,Arrival_time FROM Booking WHERE idBooking = ?");
            preparedStatement.setInt(1,bookID);
            ResultSet resultSet = preparedStatement.executeQuery();
            String nameDB = null;
            String categoryDB = null;
            String plateDB = null;
            Timestamp arrTime = null;
            while(resultSet.next()){
                nameDB =resultSet.getString("CarName");
                categoryDB=resultSet.getString("Category");
                plateDB = resultSet.getString("Num_Plate");
                arrTime = resultSet.getTimestamp("Arrival_time");

            }

            double time = System.currentTimeMillis()/1000.0 - (double) arrTime.getTime()/1000.0;
            double rates = getRate(categoryDB);
            double bill= time * rates;
//            if (categoryDB.equals("SUV")){
//                bill= time * 0.05;
//            } else if (categoryDB.equals("Car")) {
//                bill = time * 0.03;
//            }else{
//                bill = time * 0.01;
//            }

            DecimalFormat dF = new DecimalFormat("0.00");
            String formattedBill = dF.format(bill);
            JOptionPane.showMessageDialog(new JFrame(), "NAME: " + nameDB + "\n" + "NUM-PLATE: " + plateDB + "\n" + "Bill: " + formattedBill + " PKR");
            return true;





        }
        catch(Exception e3){
            e3.printStackTrace();
            return false;
        }
    }
    public double getRate(String category) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Rate FROM Rates WHERE Category = ?");
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("Rate");
            } else {
                // Handle the case where no rate is found for the category
                return 0.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
    public boolean updateRates(String category, double newRate){
        try{
            PreparedStatement preparedStatement =connection.prepareStatement("UPDATE Rates SET Rate = ? WHERE Category = ?");
            preparedStatement.setDouble(1,newRate);
            preparedStatement.setString(2,category);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                return true;
            } else {
                return false;
            }

    } catch(Exception e11){
            e11.printStackTrace();
            return false;
        }
    }
    public boolean deleteBookingAdmin(int slotNo) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM Booking WHERE SlotNum = ?");
            preparedStatement.setInt(1, slotNo);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e4){
            e4.printStackTrace();
            return false;
        }

    }
    public boolean deleteEfficient(int bID, String username){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT LevelType,Customer_username, SlotNum from Booking WHERE idBooking = ?");
            preparedStatement.setInt(1,bID);
            ResultSet resultSet = preparedStatement.executeQuery();
            String levelDB = null;
            String usernameDB = null;
            int slotDB = 0;
            if(resultSet.next()){
                levelDB =resultSet.getString("LevelType");
                usernameDB=resultSet.getString("Customer_username");
                slotDB = resultSet.getInt("SlotNum");
                connection_database c = new connection_database();
                if (usernameDB.equals(username)){
                    if (levelDB.equals("Level-1")){
                        c.billShow(bID);
                        c.deleteBooking(bID);
                        c.updateSlotStatus4(slotDB);
                    } else if (levelDB.equals("Level-2")) {
                        c.billShow(bID);
                        c.deleteBooking(bID);
                        c.updateSlotStatus5(slotDB);
                    }
                    else{
                        c.billShow(bID);
                        c.deleteBooking(bID);
                        c.updateSlotStatus6(slotDB);
                    }
                    return true;
                }
                else{
                    JOptionPane.showMessageDialog(new JFrame(), "INVALID BOOKING-ID");
                }

            }
            else{
                JOptionPane.showMessageDialog(new JFrame(), "INVALID BOOKING-ID");
            }

            return true;


        }
        catch(Exception e8){
            e8.printStackTrace();
            return false;
        }
    }
    public boolean deleteBooking(int bID) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM Booking WHERE idBooking = ?");
            preparedStatement.setInt(1, bID);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e4){
            e4.printStackTrace();
            return false;
        }

    }


    private boolean isUsernameTaken(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Customer WHERE username = ?");
        preparedStatement.setString(1, username);

        ResultSet resultSet = preparedStatement.executeQuery();

        // Check if there is at least one row in the result set
        return resultSet.next();
    }
    public boolean isOccupiedorNot1(int slotno) throws SQLException{
        try{
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Level1 WHERE slot_id = ?");
        preparedStatement.setInt(1,slotno);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            String isOccupied = resultSet.getString("status");
            if(isOccupied.equals("OCCUPIED"))
            return true;
        }
        return false;}
        catch (Exception e6){
            e6.printStackTrace();
            return false;
        }

    }
    public boolean isOccupiedorNot2(int slotno) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Level2 WHERE slot_id = ?");
        preparedStatement.setInt(1,slotno);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            String isOccupied = resultSet.getString("status");
            if(isOccupied.equals("OCCUPIED"))
                return true;
        }
        return false;

    }
    public boolean isOccupiedorNot3(int slotno) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Level3 WHERE slot_id = ?");
        preparedStatement.setInt(1,slotno);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            String isOccupied = resultSet.getString("status");
            if(isOccupied.equals("OCCUPIED"))
                return true;
        }
        return false;

    }
    public boolean updateSlotStatus1(int slotno) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Level1 SET status = 'OCCUPIED' WHERE slot_id = ?");
        preparedStatement.setInt(1,slotno);
        int rowAffected = preparedStatement.executeUpdate();
        if (rowAffected > 0){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean updateSlotStatus4(int slotno) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Level1 SET status = 'NOT OCCUPIED' WHERE slot_id = ?");
            preparedStatement.setInt(1, slotno);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e5){
            e5.printStackTrace();
            return false;
        }
    }
    public boolean updateSlotStatus2(int slotno) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Level2 SET status = 'OCCUPIED' WHERE slot_id = ?");
        preparedStatement.setInt(1,slotno);
        int rowAffected = preparedStatement.executeUpdate();
        if (rowAffected > 0){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean updateSlotStatus5(int slotno) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Level2 SET status = 'NOT OCCUPIED' WHERE slot_id = ?");
            preparedStatement.setInt(1, slotno);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e5){
            e5.printStackTrace();
            return false;
        }
    }

    public boolean updateSlotStatus3(int slotno) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Level3 SET status = 'OCCUPIED' WHERE slot_id = ?");
        preparedStatement.setInt(1,slotno);
        int rowAffected = preparedStatement.executeUpdate();
        if (rowAffected > 0){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean updateSlotStatus6(int slotno) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Level3 SET status = 'NOT OCCUPIED' WHERE slot_id = ?");
            preparedStatement.setInt(1, slotno);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e5){
            e5.printStackTrace();
            return false;
        }
    }






    public static void main(String[] args) {
        connection_database connectionDatabase=new connection_database();
        System.out.println(connectionDatabase.registerCustomer("Hamayu","1234512345670","03400013408","hamayu111","12345"));



    }













}
