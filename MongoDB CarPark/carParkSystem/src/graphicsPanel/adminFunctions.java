package graphicsPanel;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

import Connection.connection_database;
import com.mongodb.client.*;
import org.bson.Document;


public class adminFunctions extends JFrame implements ActionListener {

    private JPanel panel, centerPanel;
    private JLabel picLabel, levelLabel, slotLabel, separateLabel, categoryLabel,cnicLabel;
    private JTextField slotTextfield,cnicTextfield;
    private String[] levels = {"...", "Level 1", "Level 2", "Level 3"};
    private String[] category = {"...", "SUV", "Car", "Bike"};
    private JComboBox<String> levelBox = new JComboBox<>(levels);
    private JComboBox<String> categoryBox = new JComboBox<>(category);
    private ImageIcon admin = new ImageIcon("admin.png");
    private DefaultTableModel model;
    private JButton exitVehicle, changeButton, changePassword,okbutton;
    MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    public adminFunctions() {
        super("ADMIN FUNCTIONS");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setVisible(false);

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        panel.setPreferredSize(new Dimension(160, getHeight()));
        panel.setBackground(Color.GRAY);

        picLabel = new JLabel(admin);
        picLabel.setPreferredSize(new Dimension(72, 72));

        levelLabel = new JLabel("View Level");
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        levelLabel.setPreferredSize(new Dimension(100, 20));

        levelBox.addActionListener(this);
        levelBox.setPreferredSize(new Dimension(100, 20));


        slotLabel = new JLabel("Slot to Exit:");
        slotLabel.setPreferredSize(new Dimension(100, 20));
        slotLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        slotTextfield = new JTextField();
        slotTextfield.setPreferredSize(new Dimension(100, 20));

        cnicLabel = new JLabel("Enter CNIC: ");
        cnicLabel.setPreferredSize(new Dimension(100,20));
        cnicLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cnicTextfield = new JTextField();
        cnicTextfield.setPreferredSize(new Dimension(100,20));


        exitVehicle = new JButton("Exit");
        exitVehicle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exitVehicle.setPreferredSize(new Dimension(80, 20));
        exitVehicle.addActionListener(this);
        exitVehicle.setFocusable(false);

        separateLabel = new JLabel("--------------");
        separateLabel.setPreferredSize(new Dimension(90, 20));
        separateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        changeButton = new JButton("Change Rates");
        changeButton.setPreferredSize(new Dimension(120, 20));
        changeButton.addActionListener(this);
        changeButton.setFocusable(false);

        okbutton = new JButton("OK");
        okbutton.setPreferredSize(new Dimension(120, 20));
        okbutton.addActionListener(this);
        okbutton.setFocusable(false);


        changePassword = new JButton("Change Pass");
        changePassword.setPreferredSize(new Dimension(120, 20));
        changePassword.addActionListener(this);
        changePassword.setFocusable(false);


        panel.add(picLabel);
        panel.add(levelLabel);
        panel.add(levelBox);
        panel.add(slotLabel);
        panel.add(slotTextfield);
        panel.add(exitVehicle);
        panel.add(separateLabel);
        panel.add(changeButton);
        panel.add(changePassword);
        panel.add(cnicLabel);
        panel.add(cnicTextfield);
        panel.add(okbutton);


        add(panel, BorderLayout.WEST);


        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedLevel = (String) levelBox.getSelectedItem();
        centerPanel.removeAll();
        if (selectedLevel.equals("Level 1")) {
            try{
                model = new DefaultTableModel();
                model.addColumn("Booking-ID");
                model.addColumn("Car-Name");
                model.addColumn("LevelNo");
                model.addColumn("SlotNo");
                model.addColumn("Category");
                model.addColumn("Number-Plate");
                model.addColumn("Username");
                model.addColumn("Arrival-Time");

                loadDataFromMongoDB1();




            }
            catch(Exception e1){
                e1.printStackTrace();
            }

            centerPanel.setVisible(true);
            JTable table = new JTable(model);

            JScrollPane pane = new JScrollPane(table);
            centerPanel.add(pane);
            add(centerPanel, BorderLayout.CENTER);

            revalidate();



        }
        if (selectedLevel.equals("Level 2")) {
            try{

                model = new DefaultTableModel();
                model.addColumn("Booking-ID");
                model.addColumn("Car-Name");
                model.addColumn("LevelNo");
                model.addColumn("SlotNo");
                model.addColumn("Category");
                model.addColumn("Number-Plate");
                model.addColumn("Username");
                model.addColumn("Arrival-Time");

                loadDataFromMongoDB2();


            }
            catch(Exception e1){
                e1.printStackTrace();
            }

            centerPanel.setVisible(true);
            JTable table = new JTable(model);

            JScrollPane pane = new JScrollPane(table);
            centerPanel.add(pane);
            add(centerPanel, BorderLayout.CENTER);

            revalidate();

        }
        if (selectedLevel.equals("Level 3")) {
            try{

                model = new DefaultTableModel();
                model.addColumn("Booking-ID");
                model.addColumn("Car-Name");
                model.addColumn("LevelNo");
                model.addColumn("SlotNo");
                model.addColumn("Category");
                model.addColumn("Number-Plate");
                model.addColumn("Username");
                model.addColumn("Arrival-Time");

                loadDataFromMongoDB3();


            }
            catch(Exception e1){
                e1.printStackTrace();
            }
            centerPanel.setVisible(true);
            JTable table = new JTable(model);

            JScrollPane pane = new JScrollPane(table);
            centerPanel.add(pane);
            add(centerPanel, BorderLayout.CENTER);

            revalidate();

        }
        if (e.getSource() == exitVehicle) {
            if (slotTextfield.getText().isEmpty()) {
                String soundPath = "error.wav";
                try {
                    File soundFile = new File(soundPath);
                    AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(ai);
                    clip.start();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(new JFrame(), "Empty Field Entry");

            } else {
                int slotNumber = Integer.parseInt(slotTextfield.getText());
                connection_database c = new connection_database();

                if (selectedLevel.equals("Level 1")) {


                    if (slotNumber >= 0 && slotNumber < 16) {
                        try {
                            if (isOccupiedorNot1(slotNumber)) {
                                deleteBookingAdmin(slotNumber);
                                updateSlotStatus4(slotNumber);
                                JOptionPane.showMessageDialog(new JFrame(), "VEHICLE REMOVED");
                            } else {
                                String soundPath = "error.wav";
                                try {
                                    File soundFile = new File(soundPath);
                                    AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                                    Clip clip = AudioSystem.getClip();
                                    clip.open(ai);
                                    clip.start();
                                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                                    ex.printStackTrace();
                                }
                                JOptionPane.showMessageDialog(new JFrame(), "SLOT IS ALREADY EMPTY");
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        String soundPath = "error.wav";
                        try {
                            File soundFile = new File(soundPath);
                            AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                            Clip clip = AudioSystem.getClip();
                            clip.open(ai);
                            clip.start();
                        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                            ex.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(new JFrame(), "PLEASE SELECT SLOTS FROM 1-15");
                    }
                }
                if (selectedLevel.equals("Level 2")) {
                    if (slotNumber >= 0 && slotNumber < 11) {
                        try {
                            if (isOccupiedorNot2(slotNumber)) {
                                deleteBookingAdmin(slotNumber);
                                updateSlotStatus5(slotNumber);
                                JOptionPane.showMessageDialog(new JFrame(), "VEHICLE REMOVED");
                            } else {
                                String soundPath = "error.wav";
                                try {
                                    File soundFile = new File(soundPath);
                                    AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                                    Clip clip = AudioSystem.getClip();
                                    clip.open(ai);
                                    clip.start();
                                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                                    ex.printStackTrace();
                                }
                                JOptionPane.showMessageDialog(new JFrame(), "SLOT IS ALREADY EMPTY");
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        String soundPath = "error.wav";
                        try {
                            File soundFile = new File(soundPath);
                            AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                            Clip clip = AudioSystem.getClip();
                            clip.open(ai);
                            clip.start();
                        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                            ex.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(new JFrame(), "PLEASE SELECT SLOTS FROM 1-10");
                    }
                }
                if (selectedLevel.equals("Level 3")) {
                    if (slotNumber >= 0 && slotNumber < 6) {
                        try {
                            if (isOccupiedorNot3(slotNumber)) {
                                deleteBookingAdmin(slotNumber);
                                updateSlotStatus6(slotNumber);
                                JOptionPane.showMessageDialog(new JFrame(), "VEHICLE REMOVED");
                            } else {
                                String soundPath = "error.wav";
                                try {
                                    File soundFile = new File(soundPath);
                                    AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                                    Clip clip = AudioSystem.getClip();
                                    clip.open(ai);
                                    clip.start();
                                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                                    ex.printStackTrace();
                                }
                                JOptionPane.showMessageDialog(new JFrame(), "SLOT IS ALREADY EMPTY");
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        String soundPath = "error.wav";
                        try {
                            File soundFile = new File(soundPath);
                            AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                            Clip clip = AudioSystem.getClip();
                            clip.open(ai);
                            clip.start();
                        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                            ex.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(new JFrame(), "PLEASE SELECT SLOTS FROM 1-5");
                    }
                }
            }
        }
        if (e.getSource() == changeButton) {
            new changeRatesPanel();
        }
        if (e.getSource() == changePassword) {
            try{

                Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/CARPARK","root","admin");

                PreparedStatement preparedStatement=connection.prepareStatement("select * from Customer");
                ResultSet resultSet= preparedStatement.executeQuery();
                model=buildTableModel(resultSet);


            }
            catch(Exception e1){
                e1.printStackTrace();
            }
            centerPanel.setVisible(true);
            JTable table = new JTable(model);

            JScrollPane pane = new JScrollPane(table);
            centerPanel.add(pane);
            add(centerPanel, BorderLayout.CENTER);

            revalidate();

//            String newPass = JOptionPane.showInputDialog(new JFrame(), "Enter new password: ");
//            if (newPass.isEmpty()) {
//                String soundPath = "error.wav";
//                try {
//                    File soundFile = new File(soundPath);
//                    AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
//                    Clip clip = AudioSystem.getClip();
//                    clip.open(ai);
//                    clip.start();
//                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
//                    ex.printStackTrace();
//                }
//                JOptionPane.showMessageDialog(new JFrame(), "Empty Field Entry");
//            } else {
//                ArrayList<Person> adminTemp = new ArrayList<>();
//                personFileReadWrite.LoadArrayListPerson("admin");
//                adminTemp = personFileReadWrite.getArrayList();
//                for (Person p : adminTemp) {
//                    p.setPassword(newPass);
//                }
//                personFileReadWrite.writeToFile(adminTemp, "admin");
//                JOptionPane.showMessageDialog(new JFrame(),"Password Updated");
//            }
//
        }
    }
    private static DefaultTableModel buildTableModel(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();

            // Get column names
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Get data rows
            Object[][] data = new Object[100][columnCount];
            int rowCount = 0;
            while (resultSet.next() && rowCount < 100) {
                for (int i = 1; i <= columnCount; i++) {
                    data[rowCount][i - 1] = resultSet.getObject(i);
                }
                rowCount++;
            }

            // Create a DefaultTableModel
            return new DefaultTableModel(data, columnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void loadDataFromMongoDB1() {
        {
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("booking");

            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                int bookingID = document.getInteger("idBooking");
                String carName = document.getString("CarName");
                String levelType = document.getString("LevelType");
                int slotNumber = document.getInteger("SlotNum");
                String category = document.getString("Category");
                String numberPlate = document.getString("Num_Plate");
                String username = document.getString("Customer_username");
                String time = document.getString("Arrival_time");
                if (levelType.equals("Level-1")) {

                    // Create a TableRow instance
                    TableRow row = new TableRow(bookingID, carName, levelType, slotNumber, category, numberPlate, username, time);

                    // Add row data to the table model
                    model.addRow(new Object[]{row.getBookingId(), row.getCarName(), row.getLevelType(), row.getSlotNo(), row.getCategory(), row.getNumPlate(), row.getUsername(), row.getTime()});
                }
                }
        }


    }
    private void loadDataFromMongoDB2() {

        {
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("booking");

            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                int bookingID = document.getInteger("idBooking");
                String carName = document.getString("CarName");
                String levelType = document.getString("LevelType");
                int slotNumber = document.getInteger("SlotNum");
                String category = document.getString("Category");
                String numberPlate = document.getString("Num_Plate");
                String username = document.getString("Customer_username");
                String time = document.getString("Arrival_time");
                if (levelType.equals("Level-2")) {

                    // Create a TableRow instance
                    TableRow row = new TableRow(bookingID, carName, levelType, slotNumber, category, numberPlate, username, time);

                    // Add row data to the table model
                    model.addRow(new Object[]{row.getBookingId(), row.getCarName(), row.getLevelType(), row.getSlotNo(), row.getCategory(), row.getNumPlate(), row.getUsername(), row.getTime()});
                }
            }
        }


    }
    private void loadDataFromMongoDB3() {
        {
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("booking");

            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                int bookingID = document.getInteger("idBooking");
                String carName = document.getString("CarName");
                String levelType = document.getString("LevelType");
                int slotNumber = document.getInteger("SlotNum");
                String category = document.getString("Category");
                String numberPlate = document.getString("Num_Plate");
                String username = document.getString("Customer_username");
                String time = document.getString("Arrival_time");
                if (levelType.equals("Level-3")) {

                    // Create a TableRow instance
                    TableRow row = new TableRow(bookingID, carName, levelType, slotNumber, category, numberPlate, username, time);

                    // Add row data to the table model
                    model.addRow(new Object[]{row.getBookingId(), row.getCarName(), row.getLevelType(), row.getSlotNo(), row.getCategory(), row.getNumPlate(), row.getUsername(), row.getTime()});
                }
            }
        }


    }
    private static class TableRow {
        private final int bookingId;
        private final String carName;
        private final String levelType;
        private final int slotNo;
        private final String category;
        private final String numPlate;
        private final String username;
        private final String time;


        public TableRow(int bookingId, String carName,String levelType,int slotNo,String category,String numPlate,String username,String time) {
            this.bookingId = bookingId;
            this.carName = carName;
            this.levelType = levelType;
            this.slotNo = slotNo;
            this.category = category;
            this.numPlate = numPlate;
            this.username = username;
            this.time = time;

        }
        public int getBookingId() {
            return bookingId;
        }
        public int getSlotNo() {
            return slotNo;
        }
        public String getCarName() {
            return carName;
        }
        public String getCategory() {
            return category;
        }
        public String getLevelType() {
            return levelType;
        }
        public String getNumPlate() {
            return numPlate;
        }
        public String getTime() {
            return time;
        }
        public String getUsername() {
            return username;
        }
    }
    public boolean deleteBookingAdmin(int slotNo) {
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = mongoDatabase.getCollection("booking");

            // Define the filter for the delete operation
            Document filter = new Document("SlotNum", slotNo);

            // Perform the delete operation
            collection.deleteOne(filter);

            // Check if any document was deleted
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isOccupiedorNot1(int slotno) {
        try {

            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level1");

            Document query = new Document("slot_id", slotno);
            MongoCursor<Document> cursor = collection.find(query).iterator();

            if (cursor.hasNext()) {
                String isOccupied = cursor.next().getString("status");
                if (isOccupied.equals("OCCUPIED")) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isOccupiedorNot2(int slotno) {
        try {

            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level2");

            Document query = new Document("slot_id", slotno);
            MongoCursor<Document> cursor = collection.find(query).iterator();

            if (cursor.hasNext()) {
                String isOccupied = cursor.next().getString("status");
                if (isOccupied.equals("OCCUPIED")) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isOccupiedorNot3(int slotno) {
        try {

            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level3");

            Document query = new Document("slot_id", slotno);
            MongoCursor<Document> cursor = collection.find(query).iterator();

            if (cursor.hasNext()) {
                String isOccupied = cursor.next().getString("status");
                if (isOccupied.equals("OCCUPIED")) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateSlotStatus4(int slotno) {
        try {

            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level1");

            // Define the filter for the update
            Document filter = new Document("slot_id", slotno);

            // Define the update
            Document update = new Document("$set", new Document("status", "NOT OCCUPIED"));

            // Perform the update
            collection.updateOne(filter, update);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean updateSlotStatus5(int slotno) {
        try {
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level2");

            // Define the filter for the update
            Document filter = new Document("slot_id", slotno);

            // Define the update
            Document update = new Document("$set", new Document("status", "NOT OCCUPIED"));

            // Perform the update
            collection.updateOne(filter, update);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean updateSlotStatus6(int slotno) {
        try {
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level3");

            // Define the filter for the update
            Document filter = new Document("slot_id", slotno);

            // Define the update
            Document update = new Document("$set", new Document("status", "NOT OCCUPIED"));

            // Perform the update
            collection.updateOne(filter, update);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public static void main(String[] args) {
        new adminFunctions();
    }
}
