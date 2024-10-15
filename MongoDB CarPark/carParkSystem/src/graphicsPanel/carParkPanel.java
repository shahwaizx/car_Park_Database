package graphicsPanel;

import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import Connection.connection_database;
import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
public class carParkPanel extends JFrame implements ActionListener {

    private JLabel carNameLabel, numPlateLabel, categoryLabel, levelsLabel, slotLabel;

    private JTextField carNameTextfield, numPlateTextfield, slotTextfield;
    private JButton confirmButton;
    private JPanel panel,centerPanel;

    private String[] levels = {"...", "Level 1", "Level 2", "Level 3"};
    private String[] category = {"...", "SUV", "Car", "Bike"};

    private JComboBox<String> comboBox = new JComboBox<>(levels);
    private JComboBox<String> categoryBox = new JComboBox<>(category);

    private DefaultTableModel model;
    public static String currentUser;
    MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    public carParkPanel(String user) {
        super("CAR PARKING PANEL");
        this.currentUser = user;
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setVisible(false);

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        panel.setPreferredSize(new Dimension(160, getHeight()));
        panel.setBackground(Color.GRAY);


        carNameLabel = new JLabel("Car Name:");
        carNameLabel.setPreferredSize(new Dimension(100, 20));
        carNameTextfield = new JTextField();
        carNameTextfield.setPreferredSize(new Dimension(100, 20));

        numPlateLabel = new JLabel("Num-Plate:");
        numPlateLabel.setPreferredSize(new Dimension(100, 20));

        numPlateTextfield = new JTextField();
        numPlateTextfield.setPreferredSize(new Dimension(100, 20));

        levelsLabel = new JLabel("Levels:");
        levelsLabel.setPreferredSize(new Dimension(100, 20));

        comboBox.addActionListener(this);
        comboBox.setPreferredSize(new Dimension(100, 20));

        slotLabel = new JLabel("Slot#:");
        slotLabel.setPreferredSize(new Dimension(100, 20));

        slotTextfield = new JTextField();
        slotTextfield.setPreferredSize(new Dimension(100, 20));

        categoryLabel = new JLabel("Category:");
        categoryLabel.setPreferredSize(new Dimension(100, 20));

        categoryBox.addActionListener(this);
        categoryBox.setPreferredSize(new Dimension(100, 20));

        confirmButton = new JButton("CONFIRM");
        confirmButton.setPreferredSize(new Dimension(100, 20));
        confirmButton.setFocusable(false);
        confirmButton.addActionListener(this);


        panel.add(carNameLabel);
        panel.add(carNameTextfield);
        panel.add(numPlateLabel);
        panel.add(numPlateTextfield);
        panel.add(levelsLabel);
        panel.add(comboBox);
        panel.add(slotLabel);
        panel.add(slotTextfield);
        panel.add(categoryLabel);
        panel.add(categoryBox);
        panel.add(confirmButton);

        add(panel, BorderLayout.WEST);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedLevel = (String) comboBox.getSelectedItem();
        String categorySelect = (String) categoryBox.getSelectedItem();
        centerPanel.removeAll();


        if (selectedLevel.equals("Level 1")) {

            try{
                model = new DefaultTableModel();
                model.addColumn("Slot Number");
                model.addColumn("Status");

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
                model.addColumn("Slot Number");
                model.addColumn("Status");

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
                model.addColumn("Slot Number");
                model.addColumn("Status");

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
        if (e.getSource() == confirmButton) {
            String vehicle = carNameTextfield.getText();
            String numberPlate = numPlateTextfield.getText();
            int slotNumber = 0;
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
                JOptionPane.showMessageDialog(new JFrame(), "EMPTY FIELD ENTRY");

            } else {
                slotNumber = Integer.parseInt(slotTextfield.getText());
                if (vehicle.isEmpty() || numberPlate.isEmpty() || categorySelect.equals("...") || selectedLevel.equals("...")) {
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
                    JOptionPane.showMessageDialog(new JFrame(), "EMPTY FIELD ENTRY");
                } else {
                    if (selectedLevel.equals("Level 1")) {
                        if (slotNumber > 0 && slotNumber < 16) {
                            try {

                                if (!isOccupiedorNot1(slotNumber)) {
                                    bookRide(vehicle,"Level-1",slotNumber,categorySelect,numberPlate,currentUser);
                                    updateSlotStatus1(slotNumber);
                                    JOptionPane.showMessageDialog(new JFrame(), "VEHICLE PARKED SUCCESSFULLY");
                                    dispose();
                                } else {
                                    String soundPath = "error.wav";
                                    try {
                                        File soundFile = new File(soundPath);
                                        AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                                        Clip clip = AudioSystem.getClip();
                                        clip.open(ai);
                                        clip.start();
                                    } catch (UnsupportedAudioFileException | IOException |
                                             LineUnavailableException ex) {
                                        ex.printStackTrace();
                                    }
                                    JOptionPane.showMessageDialog(new JFrame(), "SLOT IS OCCUPIED");
                                }
                            }catch (Exception e1){
                                e1.printStackTrace();
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
                            JOptionPane.showMessageDialog(new JFrame(), "PLEASE SELECT SLOT FROM 1-15");
                        }
                    }
                    if (selectedLevel.equals("Level 2")) {
                        if (slotNumber > 0 && slotNumber < 11) {
                            try{
                            if (!isOccupiedorNot2(slotNumber)) {
                                bookRide(vehicle,"Level-2",slotNumber,categorySelect,numberPlate,currentUser);
                                updateSlotStatus2(slotNumber);
                                JOptionPane.showMessageDialog(new JFrame(),"VEHICLE PARKED SUCCESSFULLY");
                                dispose();

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
                                JOptionPane.showMessageDialog(new JFrame(), "SLOT IS OCCUPIED");
                            }}
                            catch(Exception e2){
                                e2.printStackTrace();
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
                            JOptionPane.showMessageDialog(new JFrame(), "PLEASE SELECT SLOT FROM 1-10");
                        }
                    }
                    if (selectedLevel.equals("Level 3")) {
                        if (slotNumber > 0 && slotNumber < 6) {
                            try {
                                if (!isOccupiedorNot3(slotNumber)) {
                                    bookRide(vehicle,"Level-3",slotNumber,categorySelect,numberPlate,currentUser);
                                    updateSlotStatus3(slotNumber);
                                    JOptionPane.showMessageDialog(new JFrame(), "VEHICLE PARKED SUCCESSFULLY");
                                    dispose();

                                } else {
                                    String soundPath = "error.wav";
                                    try {
                                        File soundFile = new File(soundPath);
                                        AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                                        Clip clip = AudioSystem.getClip();
                                        clip.open(ai);
                                        clip.start();
                                    } catch (UnsupportedAudioFileException | IOException |
                                             LineUnavailableException ex) {
                                        ex.printStackTrace();
                                    }
                                    JOptionPane.showMessageDialog(new JFrame(), "SLOT IS OCCUPIED");
                                }
                            }
                            catch (Exception e3){
                                e3.printStackTrace();
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
                            JOptionPane.showMessageDialog(new JFrame(), "PLEASE SELECT SLOT FROM 1-5");
                        }

                    }
                }


            }


        }
    }
    private void loadDataFromMongoDB1() {

         {
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level1");

            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                int slotNumber = document.getInteger("slot_id");
                String status = document.getString("status");

                // Create a TableRow instance
                carParkPanel.TableRow row = new carParkPanel.TableRow(slotNumber, status);

                // Add row data to the table model
                model.addRow(new Object[]{row.getSlotNumber(), row.getStatus()});
            }
        }


    }
    private void loadDataFromMongoDB2() {
         {
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level2");


            MongoCursor<Document> cursor = collection.find().iterator();

            while (cursor.hasNext()) {
                Document document = cursor.next();
                int slotNumber = document.getInteger("slot_id");
                String status = document.getString("status");

                // Create a TableRow instance
                carParkPanel.TableRow row = new carParkPanel.TableRow(slotNumber, status);

                // Add row data to the table model
                model.addRow(new Object[]{row.getSlotNumber(), row.getStatus()});
            }
        }


    }
    private void loadDataFromMongoDB3() {

        {
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level3");

            MongoCursor<Document> cursor = collection.find().iterator();

            while (cursor.hasNext()) {
                Document document = cursor.next();
                int slotNumber = document.getInteger("slot_id");
                String status = document.getString("status");

                // Create a TableRow instance
                carParkPanel.TableRow row = new carParkPanel.TableRow(slotNumber, status);

                // Add row data to the table model
                model.addRow(new Object[]{row.getSlotNumber(), row.getStatus()});
            }
        }

    }
    // Custom class to represent table rows
    private static class TableRow {
        private final int slotNumber;
        private final String status;

        public TableRow(int slotNumber, String status) {
            this.slotNumber = slotNumber;
            this.status = status;
        }

        public int getSlotNumber() {
            return slotNumber;
        }

        public String getStatus() {
            return status;
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
    public boolean bookRide(String carName,String levelType,int slotNo,String category,String NumPlate,String username){
        try{
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("booking");
            Document lastbookingID = collection.find()
                    .sort(Sorts.descending("idBooking"))
                    .limit(1)
                    .first();
            int lastBookingId;
            if (lastbookingID == null){
                lastBookingId = 0;
            }else{
             lastBookingId = lastbookingID.getInteger("idBooking");}
            System.out.println("last id: " + lastBookingId);


            int newBookingId = lastBookingId + 1;
            System.out.println("new id: " + newBookingId);
            Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
            String formattedTimestamp = formatter.format(instant);

            Document bookingDocument = new Document();
            bookingDocument.append("idBooking",newBookingId)
                           .append("CarName",carName)
                           .append("LevelType",levelType)
                           .append("SlotNum",slotNo)
                           .append("Category",category)
                           .append("Num_Plate",NumPlate)
                           .append("Customer_username",username)
                           .append("Arrival_time",formattedTimestamp);
            collection.insertOne(bookingDocument);
            JOptionPane.showMessageDialog(new JFrame(),"YOUR BOOKING-ID IS: " + newBookingId);

            return true;

        }catch(Exception e1){
            System.out.println(e1.getMessage());
            return false;
        }
    }

    public boolean updateSlotStatus1(int slotno) {
        try {
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level1");

            // Define the filter for the update
            Document filter = new Document("slot_id", slotno);

            // Define the update
            Document update = new Document("$set", new Document("status", "OCCUPIED"));

            // Perform the update
            collection.updateOne(filter, update);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean updateSlotStatus2(int slotno) {
        try {
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level2");

            // Define the filter for the update
            Document filter = new Document("slot_id", slotno);

            // Define the update
            Document update = new Document("$set", new Document("status", "OCCUPIED"));

            // Perform the update
            collection.updateOne(filter, update);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean updateSlotStatus3(int slotno) {
        try {
            MongoDatabase database = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = database.getCollection("level3");

            // Define the filter for the update
            Document filter = new Document("slot_id", slotno);

            // Define the update
            Document update = new Document("$set", new Document("status", "OCCUPIED"));

            // Perform the update
            collection.updateOne(filter, update);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}

