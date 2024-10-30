import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class BlogManagementGUI {
    private static Properties config;

    public static void main(String[] args) {
        config = loadConfig();
        JFrame frame = new JFrame("Blogood");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Set layout manager
        frame.setLayout(new BorderLayout());

        // Top buttons
        JPanel topPanel = new JPanel();
        JButton addButton = new JButton("Add New +");
        JButton configButton = new JButton("Config");
        topPanel.add(addButton);
        topPanel.add(configButton);

        // Table data
        String[] columnNames = {"No", "Name", "Created On"};
        Object[][] data = getDataFromPHP();
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Add components to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BlogAddGUI newClass = new BlogAddGUI();
                BlogAddGUI.main();
            }
        });

        configButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ConfigurationPage newClass = new ConfigurationPage();
                ConfigurationPage.main();
            }
        });
    }

    private static Object[][] getDataFromPHP() {
        try {
            String urlString = config.getProperty("Blog PHP URL");
            if (urlString == null || !urlString.startsWith("http")) {
                throw new IllegalArgumentException("Invalid URL in configuration");
            }
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Print the response to debug
            System.out.println("Response: " + response.toString());

            JSONArray jsonArray = new JSONArray(response.toString());
            Object[][] data = new Object[jsonArray.length()][3];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                data[i][0] = jsonObject.getInt("blog_id");
                data[i][1] = jsonObject.getString("blog_name");
                data[i][2] = jsonObject.getString("date");
            }
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return new Object[][]{};
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
