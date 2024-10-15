package graphicsPanel;
import Connection.connection_database;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Objects;
import java.text.DecimalFormat;
import java.time.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class exitSignIn extends JFrame implements ActionListener {


    private JLabel usernameLabel, picLabel;
    private JTextField usernameTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordTextField;
    private JButton signInButton;
    private Font myFont = new Font("Segoe UI", Font.PLAIN, 14);
    private ImageIcon user = new ImageIcon("user.png");
    MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    public exitSignIn() {
        super("USER LOGIN WINDOW");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        usernameLabel = new JLabel("USERNAME:");
        usernameTextField = new JTextField();
        usernameLabel.setBounds(100, 35, 80, 20);
        usernameTextField.setBounds(180, 35, 100, 20);

        passwordLabel = new JLabel("PASSWORD:");
        passwordTextField = new JPasswordField();
        passwordLabel.setBounds(100, 75, 100, 20);
        passwordTextField.setBounds(180, 75, 100, 20);
        signInButton = new JButton("SIGN IN");

        signInButton.setBounds(160, 115, 100, 20);
        signInButton.setFocusable(false);
        signInButton.addActionListener(this);

        picLabel = new JLabel(user);
        picLabel.setBounds(10, 20, 72, 72);


        add(usernameLabel);
        add(usernameTextField);
        add(passwordLabel);
        add(passwordTextField);
        add(signInButton);
        add(picLabel);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = usernameTextField.getText();
        char[] pass = passwordTextField.getPassword();
        String password = String.copyValueOf(pass);
        if (e.getSource() == signInButton) {
            if (str.isEmpty() || password.isEmpty()) {
                String soundPath = "error.wav";
                try {
                    File soundFile = new File(soundPath);
                    AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(ai);
                    clip.start();
                }
                catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex){
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(new JFrame(), "EMPTY FIELD ENTRY");
            } else {
                Boolean check=false;
                if (verifyCustomer(str,password)) {
                    check = true;
                }

                if (check) {
                    String user =  str;
                    String bID = JOptionPane.showInputDialog(new JFrame(), "Enter your Booking-ID: ");
                    if (bID.isEmpty()) {
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
                    }
                    int b_id = Integer.parseInt(bID);
                    deleteEfficient(b_id,user);

                    dispose();
                } else {
                    String soundPath = "error.wav";
                    try {
                        File soundFile = new File(soundPath);
                        AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                        Clip clip = AudioSystem.getClip();
                        clip.open(ai);
                        clip.start();
                    }
                    catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex){
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(new JFrame(), "WRONG CREDENTIALS");
                }

            }
        }
    }
    public boolean verifyCustomer(String username, String password){
    try{
        MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
        MongoCollection<Document> collection = mongoDatabase.getCollection("customer");
        Document customerDocument = collection.find(Filters.eq("username",username)).first();
        if (customerDocument != null & Objects.equals(customerDocument.get("password"),password)) {
            return true;
        }else
            return false;

    }catch (Exception e){
        e.printStackTrace();
        return false;
    }
    }
    public boolean deleteBooking(int bID) {
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = mongoDatabase.getCollection("booking");

            // Define the filter for the delete operation
            Document filter = new Document("idBooking", bID);

            // Perform the delete operation
            collection.deleteOne(filter);

            // Check if any document was deleted
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public double getRate(String category) {
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = mongoDatabase.getCollection("rates");

            // Define the filter for the query
            Document filter = new Document("Category", category);

            // Retrieve the document based on the filter
            Document rateDocument = collection.find(filter).first();

            if (rateDocument != null) {
                return rateDocument.getDouble("Rate");
            } else {
                // Handle the case where no rate is found for the category
                return 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
    public boolean billShow(int bookID) {
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = mongoDatabase.getCollection("booking");

            // Define the filter for the query
            Document filter = new Document("idBooking", bookID);

            // Retrieve the document based on the filter
            Document bookingDocument = collection.find(filter).first();

            if (bookingDocument != null) {
                String nameDB = bookingDocument.getString("CarName");
                String categoryDB = bookingDocument.getString("Category");
                String plateDB = bookingDocument.getString("Num_Plate");
                String arrTimeString = bookingDocument.getString("Arrival_time");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(arrTimeString, formatter);
                Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                long arrTimeMillis = instant.toEpochMilli();

                Instant instant1 = Instant.ofEpochMilli(System.currentTimeMillis());
                DateTimeFormatter formatter1= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
                String depTimestamp = formatter1.format(instant1);


                double time = System.currentTimeMillis() / 1000.0 - arrTimeMillis / 1000.0;
                double rates = getRate(categoryDB);
                double bill = time * rates;

                DecimalFormat dF = new DecimalFormat("0.00");
                String formattedBill = dF.format(bill);

                JOptionPane.showMessageDialog(new JFrame(), "NAME: " + nameDB + "\n" + "NUM-PLATE: " + plateDB + "\n" + "ARRIVAL-TIME: " + arrTimeString + "\n" + "EXIT-TIME: " + depTimestamp + "\n" + "Bill: " + formattedBill + " PKR");
                return true;
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Invalid Booking ID");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteEfficient(int bID, String username) {
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
            MongoCollection<Document> collection = mongoDatabase.getCollection("booking");

            // Define the filter for the delete operation
            Document filter = new Document("idBooking", bID);

            // Retrieve the document based on the filter
            Document bookingDocument = collection.find(filter).first();

            if (bookingDocument != null) {
                String levelDB = bookingDocument.getString("LevelType");
                String usernameDB = bookingDocument.getString("Customer_username");
                int slotDB = bookingDocument.getInteger("SlotNum");

                if (usernameDB.equals(username)) {


                    if (levelDB.equals("Level-1")) {
                        billShow(bID);
                        deleteBooking(bID);
                        updateSlotStatus4(slotDB);
                    } else if (levelDB.equals("Level-2")) {
                        billShow(bID);
                        deleteBooking(bID);
                        updateSlotStatus5(slotDB);
                    } else {
                        billShow(bID);
                        deleteBooking(bID);
                        updateSlotStatus6(slotDB);
                    }

                    return true;
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "INVALID BOOKING-ID");
                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "INVALID BOOKING-ID");
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
        new exitSignIn();
    }

}