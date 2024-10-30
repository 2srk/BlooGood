import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Properties;

public class ConfigurationPage {

    public static void main() {
        EventQueue.invokeLater(() -> {
            try {
                ConfigurationPage window = new ConfigurationPage();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private JFrame frame;
    private JTextField ftpHostField;
    private JTextField ftpUsernameField;
    private JPasswordField ftpPasswordField;
    private JTextField blogPhpUrlField;
    private JPasswordField blogDbPhpPasswordField;
    private ConfigManager configManager;

    public ConfigurationPage() {
        configManager = new ConfigManager();
        configManager.loadProperties("config.properties");
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("FTP Host"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        ftpHostField = new JTextField(20);
        ftpHostField.setText(configManager.getProperty("FTP Host"));
        panel.add(ftpHostField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("FTP Username"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        ftpUsernameField = new JTextField(20);
        ftpUsernameField.setText(configManager.getProperty("FTP Username"));
        panel.add(ftpUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("FTP Password"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        ftpPasswordField = new JPasswordField(20);
        ftpPasswordField.setText(configManager.getProperty("FTP Password"));
        panel.add(ftpPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Blog PHP URL"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        blogPhpUrlField = new JTextField(20);
        blogPhpUrlField.setText(configManager.getProperty("Blog PHP URL"));
        panel.add(blogPhpUrlField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Blog DB PHP Password"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        blogDbPhpPasswordField = new JPasswordField(20);
        blogDbPhpPasswordField.setText(configManager.getProperty("Blog DB PHP Password"));
        panel.add(blogDbPhpPasswordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configManager.setProperty("FTP Host", ftpHostField.getText());
                configManager.setProperty("FTP Username", ftpUsernameField.getText());
                configManager.setProperty("FTP Password", new String(ftpPasswordField.getPassword()));
                configManager.setProperty("Blog PHP URL", blogPhpUrlField.getText());
                configManager.setProperty("Blog DB PHP Password", new String(blogDbPhpPasswordField.getPassword()));
                configManager.saveProperties("config.properties");
            }
        });
        panel.add(saveButton, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    class ConfigManager {
        private Properties configProps = new Properties();

        public void setProperty(String key, String value) {
            configProps.setProperty(key, value);
        }

        public void saveProperties(String fileName) {
            try (OutputStream output = new FileOutputStream(fileName)) {
                configProps.store(output, null);
                System.out.println("Configuration saved to " + fileName);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        public void loadProperties(String fileName) {
            try (InputStream input = new FileInputStream(fileName)) {
                configProps.load(input);
                System.out.println("Configuration loaded from " + fileName);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        public String getProperty(String key) {
            return configProps.getProperty(key, "");
        }
    }
}
