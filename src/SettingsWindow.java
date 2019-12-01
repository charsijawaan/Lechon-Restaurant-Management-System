import javax.swing.*;
import java.awt.*;
import java.sql.*;

class SettingsWindow extends Activity {
    private DBHelper dbHelper;

    SettingsWindow() {
        super();
        dbHelper = DBHelper.getDBHelper();
        initComponents();
    }

    private void initComponents() {
        setActivityName("Settings Window");
        setDefaultCloseOperation(Activity.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel8 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String small = "", regular = "", medium = "", large = "", xl = "",jumbo = "",mega = "";

        try {
            ResultSet resultSet = dbHelper.getPrice();
            small = resultSet.getString("small");
            regular = resultSet.getString("regular");
            medium = resultSet.getString("medium");
            large = resultSet.getString("large");
            xl = resultSet.getString("xl");
            jumbo = resultSet.getString("jumbo");
            mega = resultSet.getString("mega");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        JTextField smallField = new JTextField(20);
        smallField.setText("P" + small);
        panel1.add(new JLabel("Small"));
        panel1.add(smallField);

        JTextField regularField = new JTextField(20);
        regularField.setText("P" + regular);
        panel2.add(new JLabel("Regular"));
        panel2.add(regularField);

        JTextField mediumField = new JTextField(20);
        mediumField.setText("P" + medium);
        panel3.add(new JLabel("Medium"));
        panel3.add(mediumField);

        JTextField largeField = new JTextField(20);
        largeField.setText("P" + large);
        panel4.add(new JLabel("Large"));
        panel4.add(largeField);

        JTextField XLField = new JTextField(20);
        XLField.setText("P" + xl);
        panel5.add(new JLabel("XL"));
        panel5.add(XLField);

        JTextField jumboField = new JTextField(20);
        jumboField.setText("P" + jumbo);
        panel6.add(new JLabel("Jumbo"));
        panel6.add(jumboField);

        JTextField megaField = new JTextField(20);
        megaField.setText("P" + mega);
        panel7.add(new JLabel("Mega"));
        panel7.add(megaField);

        JButton updateBtn = new JButton("Update Prices");
        updateBtn.addActionListener((e) -> {

            String updatedSmall = smallField.getText();
            updatedSmall = updatedSmall.replaceAll("[^\\d]", "");

            String updatedRegular = regularField.getText();
            updatedRegular = updatedRegular.replaceAll("[^\\d]", "");

            String updatedMedium = mediumField.getText();
            updatedMedium = updatedMedium.replaceAll("[^\\d]", "");

            String updatedLarge = largeField.getText();
            updatedLarge = updatedLarge.replaceAll("[^\\d]", "");

            String updatedXL = XLField.getText();
            updatedXL = updatedXL.replaceAll("[^\\d]", "");

            String updatedJumbo = jumboField.getText();
            updatedJumbo = updatedJumbo.replaceAll("[^\\d]", "");

            String updatedMega = megaField.getText();
            updatedMega = updatedMega.replaceAll("[^\\d]", "");

            try {
                dbHelper.updatedPrices(Integer.parseInt(updatedSmall), Integer.parseInt(updatedRegular),
                        Integer.parseInt(updatedMedium), Integer.parseInt(updatedLarge),
                        Integer.parseInt(updatedXL), Integer.parseInt(updatedJumbo),
                        Integer.parseInt(updatedMega));

                AlertBox.show("Prices Updated");
                dispose();

            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        panel8.add(updateBtn);

        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        mainPanel.add(panel4);
        mainPanel.add(panel5);
        mainPanel.add(panel6);
        mainPanel.add(panel7);
        mainPanel.add(panel8);

        addView(mainPanel, BorderLayout.CENTER);
        setVisible(true);

    }
}
