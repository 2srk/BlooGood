import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class BlogAddGUI {
    private static Properties config;
    private static File[] selectedFiles = new File[2];

    public static void main() {
        config = loadConfig();
        JFrame frame = new JFrame("Add New Blog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Add New Blog");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(titleLabel, gbc);

        JLabel nameLabel = new JLabel("Blog Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        frame.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        nameField.setText("Write your Blog Name here");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        frame.add(nameField, gbc);

        JLabel textLabel = new JLabel("Blog Text:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        frame.add(textLabel, gbc);

        JTextArea textArea = new JTextArea(10, 20);
        textArea.setText("Write your Blog Text here");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(new JScrollPane(textArea), gbc);

        JButton addImageButton = new JButton("Add Image");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        frame.add(addImageButton, gbc);

        JLabel imageStatusLabel = new JLabel("0/2 Image Uploaded");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        frame.add(imageStatusLabel, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(Color.GREEN);
        saveButton.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        frame.add(saveButton, gbc);

        addImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFiles = fileChooser.getSelectedFiles();
                    if (selectedFiles.length > 2) {
                        JOptionPane.showMessageDialog(null, "Please select only 2 images.");
                    } else {
                        imageStatusLabel.setText(selectedFiles.length + "/2 Image Uploaded");
                    }
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String blogTitle = nameField.getText();
                String blogText = textArea.getText();
                String currentTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

                // Collect image file names
                String[] fileNames = new String[selectedFiles.length];
                for (int i = 0; i < selectedFiles.length; i++) {
                    fileNames[i] = selectedFiles[i].getName();
                    try {
                        uploadFileToFTP(selectedFiles[i]);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

                // Send data to PHP script
                sendDataToPHP(blogTitle, blogText, currentTime, fileNames, config.getProperty("Blog DB PHP Password"));
            }
        });

        frame.setVisible(true);
    }

    private static void uploadFileToFTP(File file) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(config.getProperty("FTP Host"), 21);
            ftpClient.login(config.getProperty("FTP Username"), config.getProperty("FTP Password"));
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            try (FileInputStream inputStream = new FileInputStream(file)) {
                boolean done = ftpClient.storeFile(file.getName(), inputStream);
                if (done) {
                    System.out.println("The file is uploaded successfully.");
                }
            }
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    private static void sendDataToPHP(String blogTitle, String blogText, String currentTime, String[] fileNames, String password) {
        try {
            URL url = new URL(config.getProperty("Blog PHP URL"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            String postData = "title=" + blogTitle + "&content=" + blogText + "&date=" + currentTime + "&image1=" + (fileNames.length > 0 ? fileNames[0] : "") + "&image2=" + (fileNames.length > 1 ? fileNames[1] : "") + "&password=" + password;
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes());
                os.flush();
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Data sent successfully");
            } else {
                System.out.println("Failed to send data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Properties loadConfig() {
        Properties configProps = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            configProps.load(input);
            System.out.println("Configuration loaded from config.properties");
        } catch (IOException io) {
            io.printStackTrace();
        }
        return configProps;
    }
}
