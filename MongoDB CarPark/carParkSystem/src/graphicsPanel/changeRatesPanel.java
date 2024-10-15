package graphicsPanel;

import Connection.connection_database;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class changeRatesPanel extends JFrame implements ActionListener {
    private JLabel categoryLabel;
    private JLabel newRateBill;
    private JTextField newRateField;
    private JButton confirm;
    private String[] category = {"...", "SUV", "Car", "Bike"};
    private JComboBox<String> categoryBox = new JComboBox<>(category);
    MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    public changeRatesPanel() {
        super("CHANGE RATES");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(160, 15, 100, 20);
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        categoryBox.addActionListener(this);
        categoryBox.setBounds(140, 45, 100, 20);

        newRateBill = new JLabel("New Rate:");
        newRateBill.setBounds(155, 75, 100, 20);
        newRateBill.setFont(new Font("Segoe UI", Font.BOLD, 14));

        newRateField = new JTextField();
        newRateField.setBounds(140, 100, 100, 20);

        confirm = new JButton("Confirm ");
        confirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirm.setBounds(280, 130, 100, 20);
        confirm.addActionListener(this);
        confirm.setFocusable(false);

        add(categoryLabel);
        add(categoryBox);
        add(newRateBill);
        add(newRateField);
        add(confirm);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedCategory = (String) categoryBox.getSelectedItem();
        if (e.getSource() == confirm) {
            Double newRate = Double.parseDouble(newRateField.getText());
            if (selectedCategory.equals("...") || newRateField.getText().isEmpty()) {
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
                    connection_database c = new connection_database();
                    updateRates(selectedCategory,newRate);
                    JOptionPane.showMessageDialog(new JFrame(), "RATES UPDATED");
                    dispose();


            }
        }


    }
    public boolean updateRates(String category, double newRate) {
        try {
            // Assuming you have a MongoClient instance and a MongoDB database named "yourDatabase"
            // Make sure to replace "yourDatabase" with the actual name of your MongoDB database
            MongoDatabase database = mongoClient.getDatabase("carpark");

            // Assuming you have a collection named "Rates" in your MongoDB database
            // Make sure to replace "Rates" with the actual name of your MongoDB collection
            MongoCollection<Document> ratesCollection = database.getCollection("Rates");

            // Create a filter to find the document with the specified category
            Document filter = new Document("Category", category);

            // Create an update document to set the new rate
            Document update = new Document("$set", new Document("Rate", newRate));

            // Perform the update operation
            // If the document with the specified category is found and updated, rowAffected will be 1
            long rowAffected = ratesCollection.updateOne(filter, update).getModifiedCount();

            return rowAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

