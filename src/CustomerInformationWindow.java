import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

class CustomerInformationWindow extends Activity {

    private DBHelper dbHelper;

    CustomerInformationWindow() {
        super();
        dbHelper = DBHelper.getDBHelper();
        initComponents();
    }

    private void initComponents() {
        setActivityName("Customer Information Window");
        setDefaultCloseOperation(Activity.DISPOSE_ON_CLOSE);

        SearchCustomerPanel searchCustomerPanel = new SearchCustomerPanel();
        SearchCustomerByNamePanel searchCustomerByNamePanel = new SearchCustomerByNamePanel();
        UpdateCustomerPanel updateCustomerPanel = new UpdateCustomerPanel();
        DeleteCustomerPanel deleteCustomerPanel = new DeleteCustomerPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        tabbedPane.add(searchCustomerPanel, "Search Customer");
        tabbedPane.add(searchCustomerByNamePanel, "Search Customer By Name");
        tabbedPane.add(updateCustomerPanel, "Update Customer");
        tabbedPane.add(deleteCustomerPanel, "Delete Customer");

        addView(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private class SearchCustomerPanel extends JPanel {

        JTable table;
        DefaultTableModel tableModel;

        SearchCustomerPanel() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.add(new JLabel("Search"));
            JTextField searchField = new JTextField(20);
            panel.add(searchField);

            JButton searchBtn = new JButton("Search by Phone number");
            panel.add(searchBtn);

            searchBtn.addActionListener((e) -> {
                String phone = searchField.getText();
                try {
                    Long.parseLong(phone);
                    ResultSet resultSet = dbHelper.getCustomerInfo(phone);
                    if(!resultSet.next())
                        AlertBox.show("No customer found");
                    else {
                        String id = resultSet.getString("customer_id");
                        String name = resultSet.getString("customer_name");
                        String pic = resultSet.getString("customer_pic");
                        String address = resultSet.getString("customer_address");
                        resultSet.close();

                        JFrame frame = new JFrame("Customer Info " + name);
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        frame.setResizable(false);
                        frame.setLayout(new BorderLayout());

                        JPanel mainPanel = new JPanel();
                        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel7 = new JPanel(new BorderLayout());

                        JTextField idField = new JTextField(20);
                        idField.setText(id);
                        idField.setEditable(false);
                        panel1.add(new JLabel("ID"));
                        panel1.add(idField);

                        JTextField nameField = new JTextField(20);
                        nameField.setText(name);
                        nameField.setEditable(false);
                        panel2.add(new JLabel("Name"));
                        panel2.add(nameField);

                        JButton picBtn = new JButton("Open Pic");
                        picBtn.addActionListener((e2) -> {
                            if(!pic.equals("null")) {
                                File file = new File(pic);
                                if(file.exists()) {
                                    Desktop desktop = Desktop.getDesktop();
                                    try {
                                        desktop.open(file);
                                    }
                                    catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                            else {
                                AlertBox.show("Picture not available");
                            }
                        });
                        panel3.add(picBtn);

                        JTextField addressField = new JTextField(20);
                        addressField.setText(address);
                        addressField.setEditable(false);
                        panel4.add(new JLabel("Address"));
                        panel4.add(addressField);

                        JLabel totalSalesLabel = new JLabel("Total Sales of this Customer : 0");
                        totalSalesLabel.setForeground(Color.decode("#57bf13"));
                        totalSalesLabel.setFont(new Font(totalSalesLabel.getFont().getName(), Font.BOLD, 16));
                        panel5.add(totalSalesLabel);

                        JLabel rowCountLabel = new JLabel("Customer Total Orders : 0");
                        rowCountLabel.setForeground(Color.decode("#57bf13"));
                        rowCountLabel.setFont(new Font(rowCountLabel.getFont().getName(), Font.BOLD, 16));
                        panel6.add(rowCountLabel);

                        DefaultTableModel tableModel = new DefaultTableModel() {
                            @Override
                            public boolean isCellEditable(int row, int col) {
                                return false;
                            }
                        };
                        table = new JTable(tableModel);
                        tableModel.addColumn("Order ID");
                        tableModel.addColumn("Address");
                        tableModel.addColumn("Date Ordered");
                        tableModel.addColumn("Date Needed");
                        tableModel.addColumn("Payment Method");
                        tableModel.addColumn("Order Type");
                        tableModel.addColumn("Staff Member Name");

                        JScrollPane scrollPane = new JScrollPane(table);
                        scrollPane.setPreferredSize(new Dimension(500, 180));

                        resultSet = dbHelper.getAllTimeCustomerOrderInformation(Integer.parseInt(id));
                        int totalSalesOfThisCustomer = 0;
                        int rowCount = 0;
                        while(resultSet.next()) {
                            String orderID = resultSet.getString("id");
                            int price = dbHelper.getTotalBill(Integer.parseInt(orderID)).getInt("total_bill");
                            String orderAddress = resultSet.getString("address");
                            String dateOrdered = resultSet.getString("date_ordered");
                            String dateNeeded = resultSet.getString("date_needed");
                            String paymentMethod = resultSet.getString("payment_method");
                            String orderType = resultSet.getString("order_type");
                            String staffMemberName = resultSet.getString("staff_member_name");
                            tableModel.addRow(new Object[]{Integer.parseInt(orderID), orderAddress,dateOrdered, dateNeeded,
                                    paymentMethod, orderType, staffMemberName});
                            totalSalesOfThisCustomer += price;
                            rowCount++;
                        }
                        totalSalesLabel.setText("Total Sales of this Customer : " + totalSalesOfThisCustomer);
                        rowCountLabel.setText("Total Completed Orders of this Customer : " + rowCount);
                        resultSet.close();

                        panel7.add(scrollPane, BorderLayout.CENTER);

                        mainPanel.add(panel1);
                        mainPanel.add(panel2);
                        mainPanel.add(panel3);
                        mainPanel.add(panel4);
                        mainPanel.add(panel5);
                        mainPanel.add(panel6);
                        mainPanel.add(panel7);

                        frame.add(mainPanel);

                        frame.setVisible(true);
                    }
                }
                catch (Exception ex) {
                    AlertBox.show("Enter valid customer number");
                    ex.printStackTrace();
                }
            });
            add(panel);
        }

    }

    private class SearchCustomerByNamePanel extends JPanel {

        JTable usersTable;

        SearchCustomerByNamePanel() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new BorderLayout());

            JPanel ctrlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel tablePanel = new JPanel(new BorderLayout());

            JLabel searchLabel = new JLabel("Search Customer by name");
            JTextField searchField = new JTextField(20);

            DefaultTableModel usersTableModel;
            usersTableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            usersTable = new JTable(usersTableModel);

            usersTableModel.addColumn("Customer ID");
            usersTableModel.addColumn("Customer Name");
            usersTableModel.addColumn("Customer Phone");
            usersTableModel.addColumn("Customer Address");

            JButton searchBtn = new JButton("Search");
            searchBtn.addActionListener((e) -> {

                usersTable.removeAll();
                usersTableModel.setRowCount(0);
                String searchedName = searchField.getText();
                try {
                    ResultSet resultSet = dbHelper.getCustomerByName(searchedName);
                    while(resultSet.next()) {
                        String id = resultSet.getString("customer_id");
                        String name = resultSet.getString("customer_name");
                        String phone = resultSet.getString("customer_phone");
                        String address = resultSet.getString("customer_address");

                        usersTableModel.addRow(new Object[] {
                                id, name, phone, address
                        });
                    }
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            usersTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        JTable target = (JTable) e.getSource();
                        int row = target.getSelectedRow();

                        String phone = (String) usersTable.getModel().getValueAt(row, 2);
                        try {
                            ResultSet resultSet = dbHelper.getCustomerInfo(phone);
                            if(!resultSet.next())
                                AlertBox.show("No customer found");
                            else {
                                String id = resultSet.getString("customer_id");
                                String name = resultSet.getString("customer_name");
                                String pic = resultSet.getString("customer_pic");
                                String address = resultSet.getString("customer_address");
                                resultSet.close();

                                JFrame frame = new JFrame("Customer Info " + name);
                                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                frame.setResizable(false);
                                frame.setLayout(new BorderLayout());

                                JPanel mainPanel = new JPanel();
                                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                                JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                                JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                                JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                                JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                                JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                                JPanel panel6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                                JPanel panel7 = new JPanel(new BorderLayout());

                                JTextField idField = new JTextField(20);
                                idField.setText(id);
                                idField.setEditable(false);
                                panel1.add(new JLabel("ID"));
                                panel1.add(idField);

                                JTextField nameField = new JTextField(20);
                                nameField.setText(name);
                                nameField.setEditable(false);
                                panel2.add(new JLabel("Name"));
                                panel2.add(nameField);

                                JButton picBtn = new JButton("Open Pic");
                                picBtn.addActionListener((e2) -> {
                                    if(!pic.equals("null")) {
                                        File file = new File(pic);
                                        if(file.exists()) {
                                            Desktop desktop = Desktop.getDesktop();
                                            try {
                                                desktop.open(file);
                                            }
                                            catch (IOException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    }
                                    else {
                                        AlertBox.show("Picture not available");
                                    }
                                });
                                panel3.add(picBtn);

                                JTextField addressField = new JTextField(20);
                                addressField.setText(address);
                                addressField.setEditable(false);
                                panel4.add(new JLabel("Address"));
                                panel4.add(addressField);

                                JLabel totalSalesLabel = new JLabel("Total Sales of this Customer : 0");
                                totalSalesLabel.setForeground(Color.decode("#57bf13"));
                                totalSalesLabel.setFont(new Font(totalSalesLabel.getFont().getName(), Font.BOLD, 16));
                                panel5.add(totalSalesLabel);

                                JLabel rowCountLabel = new JLabel("Customer Total Orders : 0");
                                rowCountLabel.setForeground(Color.decode("#57bf13"));
                                rowCountLabel.setFont(new Font(rowCountLabel.getFont().getName(), Font.BOLD, 16));
                                panel6.add(rowCountLabel);

                                DefaultTableModel tableModel2 = new DefaultTableModel() {
                                    @Override
                                    public boolean isCellEditable(int row, int col) {
                                        return false;
                                    }
                                };
                                JTable table2 = new JTable(tableModel2);
                                tableModel2.addColumn("Order ID");
                                tableModel2.addColumn("Address");
                                tableModel2.addColumn("Date Ordered");
                                tableModel2.addColumn("Date Needed");
                                tableModel2.addColumn("Payment Method");
                                tableModel2.addColumn("Order Type");
                                tableModel2.addColumn("Staff Member Name");

                                JScrollPane scrollPane = new JScrollPane(table2);
                                scrollPane.setPreferredSize(new Dimension(500, 180));

                                resultSet = dbHelper.getAllTimeCustomerOrderInformation(Integer.parseInt(id));
                                int totalSalesOfThisCustomer = 0;
                                int rowCount = 0;
                                while(resultSet.next()) {
                                    String orderID = resultSet.getString("id");
                                    int price = dbHelper.getTotalBill(Integer.parseInt(orderID)).getInt("total_bill");
                                    String orderAddress = resultSet.getString("address");
                                    String dateOrdered = resultSet.getString("date_ordered");
                                    String dateNeeded = resultSet.getString("date_needed");
                                    String paymentMethod = resultSet.getString("payment_method");
                                    String orderType = resultSet.getString("order_type");
                                    String staffMemberName = resultSet.getString("staff_member_name");
                                    tableModel2.addRow(new Object[]{Integer.parseInt(orderID), orderAddress,dateOrdered, dateNeeded,
                                            paymentMethod, orderType, staffMemberName});
                                    totalSalesOfThisCustomer += price;
                                    rowCount++;
                                }
                                totalSalesLabel.setText("Total Sales of this Customer : " + totalSalesOfThisCustomer);
                                rowCountLabel.setText("Total Completed Orders of this Customer : " + rowCount);
                                resultSet.close();

                                panel7.add(scrollPane, BorderLayout.CENTER);

                                mainPanel.add(panel1);
                                mainPanel.add(panel2);
                                mainPanel.add(panel3);
                                mainPanel.add(panel4);
                                mainPanel.add(panel5);
                                mainPanel.add(panel6);
                                mainPanel.add(panel7);

                                frame.add(mainPanel);

                                frame.setVisible(true);
                            }
                        }
                        catch (Exception ex) {
                            AlertBox.show("Enter valid customer number");
                            ex.printStackTrace();
                        }

                    }
                }
            });

            ctrlsPanel.add(searchLabel);
            ctrlsPanel.add(searchField);
            ctrlsPanel.add(searchBtn);

            JScrollPane scrollPane = new JScrollPane(usersTable);
            scrollPane.setPreferredSize(new Dimension(500, 180));
            tablePanel.add(scrollPane);

            add(ctrlsPanel,  BorderLayout.NORTH);
            add(tablePanel, BorderLayout.CENTER);
        }
    }

    private class UpdateCustomerPanel extends JPanel {

        JFrame frame;

        UpdateCustomerPanel() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JTextField searchField = new JTextField(20);
            JButton searchBtn = new JButton("Search");
            searchBtn.addActionListener((e) -> {
                String phone = searchField.getText();
                try {
                    ResultSet resultSet = dbHelper.getCustomerInfo(phone);
                    if(!resultSet.next()) {
                        AlertBox.show("No customer found");
                    }
                    else {
                        String id = resultSet.getString("customer_id");
                        String name = resultSet.getString("customer_name");
                        String address = resultSet.getString("customer_address");
                        resultSet.close();

                        frame = new JFrame("Update customer information");
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        frame.setResizable(false);
                        frame.setLayout(new BorderLayout());

                        JPanel mainUpdatePanel = new JPanel();
                        mainUpdatePanel.setLayout(new BoxLayout(mainUpdatePanel, BoxLayout.Y_AXIS));
                        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));

                        JTextField idField = new JTextField(20);
                        idField.setEditable(false);
                        idField.setText(id);
                        panel1.add(new JLabel("Customer ID"));
                        panel1.add(idField);

                        JTextField nameField = new JTextField(20);
                        nameField.setText(name);
                        panel2.add(new JLabel("Customer name"));
                        panel2.add(nameField);

                        JTextField phoneField = new JTextField(20);
                        phoneField.setText(phone);
                        panel3.add(new JLabel("Customer phone"));
                        panel3.add(phoneField);

                        JTextField addressField = new JTextField(20);
                        addressField.setText(address);
                        panel4.add(new JLabel("Customer address"));
                        panel4.add(addressField);

                        JButton updateBtn = new JButton("Update");
                        updateBtn.addActionListener((e2) -> {
                            String updatedName = nameField.getText();
                            String updatedPhone = phoneField.getText();
                            try {
                                Integer.parseInt(updatedPhone);
                                String updatedAddress = addressField.getText();
                                try {
                                    dbHelper.updateCustomerDetails(Integer.parseInt(id), updatedName, updatedPhone, updatedAddress);
                                    AlertBox.show("Customer details updated");
                                    frame.dispose();
                                    frame = null;
                                    System.gc();
                                }
                                catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                                AlertBox.show("Enter valid phone number");
                            }
                        });
                        panel5.add(updateBtn);

                        mainUpdatePanel.add(panel1);
                        mainUpdatePanel.add(panel2);
                        mainUpdatePanel.add(panel4);
                        mainUpdatePanel.add(panel3);
                        mainUpdatePanel.add(panel5);

                        frame.add(mainUpdatePanel, BorderLayout.CENTER);

                        frame.setVisible(true);

                    }
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            panel.add(new JLabel("Enter phone number"));
            panel.add(searchField);
            panel.add(searchBtn);

            add(panel);
        }
    }

    private class DeleteCustomerPanel extends JPanel {

        DeleteCustomerPanel() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JTextField deleteField = new JTextField(20);
            JButton deleteBtn = new JButton("Delete");
            deleteBtn.addActionListener((e) -> {
                String phone = deleteField.getText();
                try {
                    ResultSet resultSet = dbHelper.checkAvailableAccount(phone);
                    if(!resultSet.next())
                        AlertBox.show("No customer account found");
                    else {
                        dbHelper.deleteCustomerAccount(phone);
                        AlertBox.show("Account deleted");
                        deleteField.setText("");
                    }
                    resultSet.close();
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            panel.add(new JLabel("Delete customer by phone number"));
            panel.add(deleteField);
            panel.add(deleteBtn);

            add(panel);
        }
    }

}
