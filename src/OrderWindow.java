import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

class OrderWindow extends Activity {

    private DBHelper dbHelper;

    OrderWindow() {
        super();
        dbHelper = DBHelper.getDBHelper();
        initComponents();
    }

    private void initComponents() {
        setActivityName("Order Window");
        setDefaultCloseOperation(Activity.DISPOSE_ON_CLOSE);

        PlaceOrderPanel placeOrderPanel = new PlaceOrderPanel();
        ActiveOrderPanel activeOrderPanel = new ActiveOrderPanel();
        SearchOrderPanel searchOrderPanel = new SearchOrderPanel();
        UpdateOrderPanel updateOrderPanel = new UpdateOrderPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        tabbedPane.add(placeOrderPanel, "Place Order");
        tabbedPane.add(activeOrderPanel, "Active Orders");
        tabbedPane.add(updateOrderPanel, "Update Order");
        tabbedPane.add(searchOrderPanel, "Search Order");
        tabbedPane.addChangeListener((e) -> activeOrderPanel.fillTable());

        addView(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private class PlaceOrderPanel extends JPanel {

        private File file;
        File tempFile;
        JTextField photoField;

        PlaceOrderPanel() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel panel6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel panel7 = new JPanel();
            panel7.setLayout(new BoxLayout(panel7, BoxLayout.Y_AXIS));
            JPanel panel8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel panel9 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel panel10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel panel11 = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JTextField nameField = new JTextField(20);
            panel1.add(new JLabel("*Name"));
            panel1.add(nameField);

            photoField = new JTextField(20);
            photoField.setEditable(false);
            JButton choosePicBtn = new JButton("Choose Pic");
            choosePicBtn.addActionListener((e) -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(this);

                tempFile = fileChooser.getSelectedFile();

                if(tempFile != null) {
                    String filename = tempFile.getName();
                    photoField.setText(filename);
                    choosePicBtn.setEnabled(false);
                }
            });

            panel2.add(new JLabel("Photo"));
            panel2.add(photoField);
            panel2.add(choosePicBtn);

            JTextField deliveryAddressField = new JTextField(20);
            panel3.add(new JLabel("*Delivery Address"));
            panel3.add(deliveryAddressField);

            JTextField contactField = new JTextField(20);
            panel4.add(new JLabel("*Contact Number"));
            panel4.add(contactField);

            JTextField dateOrderedField = new JTextField(20);
            dateOrderedField.setEditable(false);
            JButton selectDateBtn = new JButton("Select Date");
            selectDateBtn.addActionListener((e) -> {
                new DatePicker((day, month, year, hour, minute) -> {
                    String currentDate = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                    dateOrderedField.setText(currentDate);
                });
            });
            panel5.add(new JLabel("*Date Ordered"));
            panel5.add(dateOrderedField);
            panel5.add(selectDateBtn);

            JTextField dateNeededField = new JTextField(20);
            dateNeededField.setEditable(false);
            JButton selectNeededDateBtn = new JButton("Select Date");
            selectNeededDateBtn.addActionListener((e) -> {
                new DatePicker((day, month, year, hour, minute) -> {
                    String date = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                    dateNeededField.setText(date);
                });
            });
            panel6.add(new JLabel("     *Date Needed"));
            panel6.add(dateNeededField);
            panel6.add(selectNeededDateBtn);

            JButton addItemBtn = new JButton("Add Item");
            addItemBtn.addActionListener((e) -> {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                ButtonGroup sizeGroup = new ButtonGroup();
                JCheckBox smallBox = new JCheckBox("Small");
                JCheckBox regularBox = new JCheckBox("Regular");
                JCheckBox mediumBox = new JCheckBox("Medium");
                JCheckBox largeBox = new JCheckBox("Large");
                JCheckBox XLBox = new JCheckBox("XL");
                JCheckBox jumboBox = new JCheckBox("Jumbo");
                JCheckBox megaBox = new JCheckBox("Mega");

                smallBox.setSelected(true);

                sizeGroup.add(smallBox);
                sizeGroup.add(regularBox);
                sizeGroup.add(mediumBox);
                sizeGroup.add(largeBox);
                sizeGroup.add(XLBox);
                sizeGroup.add(jumboBox);
                sizeGroup.add(megaBox);

                JButton deleteBtn = new JButton("Remove");
                deleteBtn.addActionListener((event) -> {
                    panel7.remove(itemPanel);
                    panel7.repaint();
                    panel7.revalidate();
                });

                itemPanel.add(new JLabel("*Select Category :    "));
                itemPanel.add(smallBox);
                itemPanel.add(regularBox);
                itemPanel.add(mediumBox);
                itemPanel.add(largeBox);
                itemPanel.add(XLBox);
                itemPanel.add(jumboBox);
                itemPanel.add(megaBox);
                itemPanel.add(deleteBtn);

                panel7.add(itemPanel);
                panel7.repaint();
                panel7.revalidate();
            });

            JPanel addItemBtnPanel = new JPanel();
            addItemBtnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            addItemBtnPanel.add(addItemBtn);

            ButtonGroup sizeGroup = new ButtonGroup();
            JCheckBox smallBox = new JCheckBox("Small");
            JCheckBox regularBox = new JCheckBox("Regular");
            JCheckBox mediumBox = new JCheckBox("Medium");
            JCheckBox largeBox = new JCheckBox("Large");
            JCheckBox XLBox = new JCheckBox("XL");
            JCheckBox jumboBox = new JCheckBox("Jumbo");
            JCheckBox megaBox = new JCheckBox("Mega");

            smallBox.setSelected(true);

            sizeGroup.add(smallBox);
            sizeGroup.add(regularBox);
            sizeGroup.add(mediumBox);
            sizeGroup.add(largeBox);
            sizeGroup.add(XLBox);
            sizeGroup.add(jumboBox);
            sizeGroup.add(megaBox);

            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            itemPanel.add(new JLabel("*Select Category :    "));
            itemPanel.add(smallBox);
            itemPanel.add(regularBox);
            itemPanel.add(mediumBox);
            itemPanel.add(largeBox);
            itemPanel.add(XLBox);
            itemPanel.add(jumboBox);
            itemPanel.add(megaBox);

            panel7.add(itemPanel);

            ButtonGroup paymentGroup = new ButtonGroup();
            JCheckBox cashOnDelivery = new JCheckBox("Cash on Delivery");
            JCheckBox cashOnPickUp = new JCheckBox("Cash on Pickup");
            JCheckBox remittance = new JCheckBox("Remittance Center");

            cashOnDelivery.setSelected(true);

            paymentGroup.add(cashOnDelivery);
            paymentGroup.add(cashOnPickUp);
            paymentGroup.add(remittance);

            panel8.add(new JLabel("*Mode of Payment :    "));
            panel8.add(cashOnDelivery);
            panel8.add(cashOnPickUp);
            panel8.add(remittance);

            ButtonGroup orderTypeGroup = new ButtonGroup();
            JCheckBox walkIn = new JCheckBox("Walk In");
            JCheckBox phoneReservation = new JCheckBox("Phone Reservation");

            walkIn.setSelected(true);

            orderTypeGroup.add(walkIn);
            orderTypeGroup.add(phoneReservation);

            panel9.add(new JLabel("*Order Type:   "));
            panel9.add(walkIn);
            panel9.add(phoneReservation);

            JTextField staffMemberNameField = new JTextField(20);
            panel10.add(new JLabel("*Staff Member Name"));
            panel10.add(staffMemberNameField);

            JButton placeOrder = new JButton("Place Order");
            placeOrder.addActionListener((e) -> {
                String name = nameField.getText();
                String pic = null;
                if(file != null)
                    pic = file.getAbsolutePath();
                String address = deliveryAddressField.getText();
                String phone = contactField.getText();

                try {
                    Long.parseLong(phone);
                    String dateOrdered = dateOrderedField.getText();
                    String dateNeeded = dateNeededField.getText();
                    List<String> sizes = new LinkedList<>();
                    List<String> prices = new LinkedList<>();
                    for(Component itemComponent : panel7.getComponents()) {
                        Component[] itemSubComponents = ((JPanel) itemComponent).getComponents();
                        for(int i = 1; i < itemSubComponents.length - 1; i++) {
                            JCheckBox checkBox = (JCheckBox) itemSubComponents[i];
                            if(checkBox.isSelected()) {
                                sizes.add(checkBox.getText());
                                break;
                            }
                        }
                    }

                    ResultSet resultSet = dbHelper.getPrice();
                    for(String size : sizes) {
                        prices.add(resultSet.getString(size.toLowerCase()));
                    }
                    resultSet.close();

                    String paymentMethod = "";
                    Enumeration elements = paymentGroup.getElements();
                    while (elements.hasMoreElements()) {
                        AbstractButton button = (AbstractButton)elements.nextElement();
                        if (button.isSelected()) {
                            paymentMethod = button.getText();
                            break;
                        }
                    }
                    String orderType = "";
                    elements = orderTypeGroup.getElements();
                    while (elements.hasMoreElements()) {
                        AbstractButton button = (AbstractButton)elements.nextElement();
                        if (button.isSelected()) {
                            orderType = button.getText();
                            break;
                        }
                    }
                    String staffMemberName = staffMemberNameField.getText();
                    if(name.length() == 0 || address.length() == 0 || phone.length() == 0 ||
                            dateOrdered.length() == 0 || dateNeeded.length() == 0 ||
                            sizes.size() == 0 || paymentMethod.length() == 0 ||
                            staffMemberName.length() == 0) {
                        AlertBox.show("Please fill out all the mandatory fields");
                    }
                    try {
                        ResultSet resultSet1 = dbHelper.checkAvailableAccount(phone);
                        if(!resultSet1.next()) {
                            dbHelper.makeNewAccount(name, pic, address, phone);
                            if(tempFile != null) {
                                Path from = tempFile.toPath();
                                Path to = Paths.get("img/" + dbHelper.getLastCustomerID()
                                        .getString("customer_id") + ".jpg");
                                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);

                                String path = "img/" + dbHelper.getLastCustomerID()
                                        .getString("customer_id") + ".jpg";
                                dbHelper.changePicNameInCustomerTable(Integer.parseInt(dbHelper.getLastCustomerID()
                                        .getString("customer_id")), path);
                            }
                        }
                        else {
                            if(tempFile != null) {
                                if(!resultSet1.getString("customer_pic").equals("null")) {
                                    Files.delete(Paths.get("img/" + resultSet1
                                            .getString("customer_id") + ".jpg"));
                                }
                                Path from = tempFile.toPath();
                                Path to = Paths.get("img/" + resultSet1
                                        .getString("customer_id") + ".jpg");
                                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);

                                String path = "img/" + resultSet1.getString("customer_id") + ".jpg";
                                dbHelper.changePicNameInCustomerTable(Integer.parseInt(dbHelper.getLastCustomerID()
                                        .getString("customer_id")), path);
                            }
                        }
                        resultSet1.close();

                        dbHelper.placeOrder(name, address, phone, dateOrdered,
                                dateNeeded, paymentMethod, orderType, staffMemberName,
                                Integer.parseInt(dbHelper.getCustomerIDByPhone(phone).getString("customer_id")));

                        String lastOrderId = dbHelper.getLastOrderID().getString("id");

                        String size;
                        String price;
                        for(int i = 0; i < sizes.size(); i++) {
                            size = sizes.get(i);
                            price = prices.get(i);
                            dbHelper.insertSubOrder(lastOrderId, size, price);
                        }

                        AlertBox.show("Order Placed. Order ID is " + lastOrderId);
                        tempFile = null;
                        choosePicBtn.setEnabled(true);
                        photoField.setText("");

                        nameField.setText("");
                        photoField.setText("");
                        deliveryAddressField.setText("");
                        contactField.setText("");
                        dateOrderedField.setText("");
                        dateNeededField.setText("");
                        smallBox.setSelected(true);
                        cashOnDelivery.setSelected(true);
                        staffMemberNameField.setText("");
                        choosePicBtn.setEnabled(true);

                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                catch (Exception ex) {
                    AlertBox.show("Please enter a valid contact number");
                    ex.printStackTrace();
                }
            });
            panel11.add(placeOrder);

            JScrollPane scrollPane = new JScrollPane(panel7);
            scrollPane.setPreferredSize(new Dimension(600, 100));

            add(panel1);
            add(panel2);
            add(panel3);
            add(panel4);
            add(panel5);
            add(panel6);
            add(addItemBtnPanel);
            add(scrollPane);
            add(panel8);
            add(panel9);
            add(panel10);
            add(panel11);
        }
    }

    private class ActiveOrderPanel extends JPanel {

        private JTable table;
        DefaultTableModel tableModel;

        ActiveOrderPanel() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel panel1 = new JPanel(new BorderLayout());

            tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            table = new JTable(tableModel);
            tableModel.addColumn("Order ID");
            tableModel.addColumn("Name");
            tableModel.addColumn("Address");
            tableModel.addColumn("Phone");
            tableModel.addColumn("Date Ordered");
            tableModel.addColumn("Date Needed");
            tableModel.addColumn("Total Bill Amount");
            tableModel.addColumn("Payment Method");
            tableModel.addColumn("Order Type");
            tableModel.addColumn("Staff Member Name");
            tableModel.addColumn("Customer ID");


            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(500, 180));
            panel1.add(scrollPane, BorderLayout.CENTER);

            add(panel1);
        }

        void fillTable() {
            table.removeAll();
            tableModel.setRowCount(0);
            try {
                ResultSet resultSet = dbHelper.getActiveOrders();
                while(resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int price = dbHelper.getTotalBill(id).getInt("total_bill");
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    String dateOrdered = resultSet.getString("date_ordered");
                    String dateNeeded = resultSet.getString("date_needed");
                    String paymentMethod = resultSet.getString("payment_method");
                    String orderType = resultSet.getString("order_type");
                    String staffMemberName = resultSet.getString("staff_member_name");
                    String customerID = resultSet.getString("customer_id");
                    tableModel.addRow(new Object[]{id, name, address, phone, dateOrdered,
                            dateNeeded, "P " + price, paymentMethod, orderType, staffMemberName, customerID});
                }
                resultSet.close();
            }
            catch (SQLException e) {
                AlertBox.show("Some database error occurred.");
                e.printStackTrace();
                System.exit(0);
            }
        }

    }

    private class UpdateOrderPanel extends JPanel {

        JFrame frame = new JFrame("Update Order");
        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField dateOrderedField = new JTextField(20);
        JTextField dateNeededField = new JTextField(20);
        JLabel updateLabel = new JLabel("Update order By ID");
        JCheckBox smallBox = new JCheckBox("Small");
        JCheckBox regularBox = new JCheckBox("Regular");
        JCheckBox mediumBox = new JCheckBox("Medium");
        JCheckBox largeBox = new JCheckBox("Large");
        JCheckBox XLBox = new JCheckBox("XL");
        JCheckBox jumboBox = new JCheckBox("Jumbo");
        JCheckBox megaBox = new JCheckBox("Mega");
        JCheckBox cashOnDelivery = new JCheckBox("Cash on Delivery");
        JCheckBox cashOnPickUp = new JCheckBox("Cash on Pickup");
        JCheckBox remittance = new JCheckBox("Remittance Center");
        JCheckBox walkIn = new JCheckBox("Walk In");
        JCheckBox phoneReservation = new JCheckBox("Phone Reservation");
        JCheckBox inProgressBox = new JCheckBox("In Progress");
        JCheckBox completedBox = new JCheckBox("Completed");
        JTextField staffMemberNameField = new JTextField(20);

        UpdateOrderPanel() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JTextField searchField = new JTextField(20);
            JButton searchBtn = new JButton("Search");
            searchBtn.addActionListener((e) -> {
                String id = searchField.getText();
                try {
                    ResultSet resultSet = dbHelper.getOrderByID(Integer.parseInt(id));
                    if(!resultSet.next()) {
                        AlertBox.show("No order found for this ID");
                        searchField.setText("");
                    }
                    else {
                        String name = resultSet.getString("name");
                        String address = resultSet.getString("address");
                        String phone = resultSet.getString("phone");
                        String dateOrdered = resultSet.getString("date_ordered");
                        String dateNeeded = resultSet.getString("date_needed");
                        String paymentMethod = resultSet.getString("payment_method");
                        String orderType = resultSet.getString("order_type");
                        String staffMemberName = resultSet.getString("staff_member_name");
                        String status = resultSet.getString("status");

                        resultSet.close();

                        frame = new JFrame("Update Order");
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        frame.setResizable(false);
                        frame.setLayout(new BorderLayout());

                        JPanel mainUpdatePanel = new JPanel();
                        mainUpdatePanel.setLayout(new BoxLayout(mainUpdatePanel, BoxLayout.Y_AXIS));
                        JPanel panel11 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel12 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel13 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel14 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel15 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel16 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel sizePanel = new JPanel();
                        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.Y_AXIS));
                        JPanel panel17 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel18 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel19 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel20 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel21 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JPanel panel22 = new JPanel(new FlowLayout(FlowLayout.LEFT));

                        idField.setText(id);
                        idField.setEditable(false);
                        panel11.add(new JLabel("ID"));
                        panel11.add(idField);

                        nameField.setText(name);
                        panel12.add(new JLabel("Name"));
                        panel12.add(nameField);

                        addressField.setText(address);
                        panel13.add(new JLabel("Address"));
                        panel13.add(addressField);

                        phoneField.setText(phone);
                        panel14.add(new JLabel("Phone"));
                        panel14.add(phoneField);

                        dateOrderedField.setText(dateOrdered);
                        dateOrderedField.setEditable(false);
                        panel15.add(new JLabel("Date Ordered"));
                        panel15.add(dateOrderedField);

                        dateNeededField.setText(dateNeeded);
                        panel16.add(new JLabel("Date Needed"));
                        panel16.add(dateNeededField);

                        List <Integer> rowIDList = new LinkedList<>();
                        ResultSet resultSet1 = dbHelper.getSubOrderSize(id);
                        while(resultSet1.next()) {
                            JPanel itemPanel = new JPanel();
                            itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                            ButtonGroup sizeGroup = new ButtonGroup();
                            JCheckBox smallBox = new JCheckBox("Small");
                            JCheckBox regularBox = new JCheckBox("Regular");
                            JCheckBox mediumBox = new JCheckBox("Medium");
                            JCheckBox largeBox = new JCheckBox("Large");
                            JCheckBox XLBox = new JCheckBox("XL");
                            JCheckBox jumboBox = new JCheckBox("Jumbo");
                            JCheckBox megaBox = new JCheckBox("Mega");

                            String size = resultSet1.getString("size");
                            int rowid = resultSet1.getInt("rowid");
                            rowIDList.add(rowid);

                            switch (size) {
                                case "Small":
                                    smallBox.setSelected(true);
                                    break;
                                case "Regular":
                                    regularBox.setSelected(true);
                                    break;
                                case "Medium":
                                    mediumBox.setSelected(true);
                                    break;
                                case "Large":
                                    largeBox.setSelected(true);
                                    break;
                                case "XL":
                                    XLBox.setSelected(true);
                                    break;
                                case "Jumbo":
                                    jumboBox.setSelected(true);
                                    break;
                                case "Mega":
                                    megaBox.setSelected(true);
                                    break;
                            }

                            sizeGroup.add(smallBox);
                            sizeGroup.add(regularBox);
                            sizeGroup.add(mediumBox);
                            sizeGroup.add(largeBox);
                            sizeGroup.add(XLBox);
                            sizeGroup.add(jumboBox);
                            sizeGroup.add(megaBox);


                            JButton deleteBtn = new JButton("Remove");
                            deleteBtn.addActionListener((event) -> {
                                sizePanel.remove(itemPanel);
                                sizePanel.repaint();
                                sizePanel.revalidate();
                            });

                            itemPanel.add(smallBox);
                            itemPanel.add(regularBox);
                            itemPanel.add(mediumBox);
                            itemPanel.add(largeBox);
                            itemPanel.add(XLBox);
                            itemPanel.add(jumboBox);
                            itemPanel.add(megaBox);
                            itemPanel.add(deleteBtn);

                            sizePanel.add(itemPanel);
                            sizePanel.repaint();
                            sizePanel.revalidate();
                        }
                        resultSet1.close();

                        JScrollPane scrollPane = new JScrollPane(sizePanel);
                        scrollPane.setPreferredSize(new Dimension(600, 100));

                        ButtonGroup paymentGroup = new ButtonGroup();

                        switch (paymentMethod) {
                            case "Cash on Delivery":
                                cashOnDelivery.setSelected(true);
                                break;
                            case "Cash on Pickup":
                                cashOnPickUp.setSelected(true);
                                break;
                            case "Remittance Center":
                                remittance.setSelected(true);
                                break;
                        }

                        paymentGroup.add(cashOnDelivery);
                        paymentGroup.add(cashOnPickUp);
                        paymentGroup.add(remittance);

                        panel18.add(new JLabel("Mode of Payment :   "));
                        panel18.add(cashOnDelivery);
                        panel18.add(cashOnPickUp);
                        panel18.add(remittance);

                        ButtonGroup orderGroup = new ButtonGroup();

                        switch (orderType) {
                            case "Walk In":
                                walkIn.setSelected(true);
                                break;
                            case "Phone Reservation":
                                phoneReservation.setSelected(true);
                                break;
                        }

                        orderGroup.add(walkIn);
                        orderGroup.add(phoneReservation);

                        panel19.add(new JLabel("Order Type :   "));
                        panel19.add(walkIn);
                        panel19.add(phoneReservation);

                        staffMemberNameField.setText(staffMemberName);
                        panel20.add(new JLabel("Staff Member Name"));
                        panel20.add(staffMemberNameField);

                        ButtonGroup statusGroup = new ButtonGroup();
                        statusGroup.add(inProgressBox);
                        statusGroup.add(completedBox);

                        if(status.equals("1"))
                            inProgressBox.setSelected(true);
                        else
                            completedBox.setSelected(true);
                        panel21.add(new JLabel("Status"));
                        panel21.add(inProgressBox);
                        panel21.add(completedBox);

                        if (!(this instanceof SearchOrderPanel)) {
                            panel22.add(new JButton(new AbstractAction("Update") {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String updatedName = nameField.getText();
                                    String updatedAddress = addressField.getText();
                                    String updatedPhone = phoneField.getText();
                                    String updatedDateOrdered = dateOrderedField.getText();
                                    String updatedDateNeeded = dateNeededField.getText();;
                                    try {
                                        Long.parseLong(updatedPhone);
                                        List<String> sizes = new LinkedList<>();
                                        List<String> prices = new LinkedList<>();

                                        for(Component itemComponent : sizePanel.getComponents()) {
                                            Component[] itemSubComponents = ((JPanel) itemComponent).getComponents();
                                            for(int i = 1; i < itemSubComponents.length - 1; i++) {
                                                JCheckBox checkBox = (JCheckBox) itemSubComponents[i];
                                                if(checkBox.isSelected()) {
                                                    sizes.add(checkBox.getText());
                                                    break;
                                                }
                                            }
                                        }
                                        ResultSet resultSet = dbHelper.getPrice();
                                        for(String size : sizes) {
                                            prices.add(resultSet.getString(size.toLowerCase()));
                                        }
                                        resultSet.close();

                                        Enumeration elements;
                                        String updatedPaymentMethod = "";
                                        elements = paymentGroup.getElements();
                                        while (elements.hasMoreElements()) {
                                            AbstractButton button = (AbstractButton)elements.nextElement();
                                            if (button.isSelected()) {
                                                updatedPaymentMethod = button.getText();
                                                break;
                                            }
                                        }

                                        String updatedOrderType = "";
                                        elements = orderGroup.getElements();
                                        while (elements.hasMoreElements()) {
                                            AbstractButton button = (AbstractButton)elements.nextElement();
                                            if (button.isSelected()) {
                                                updatedOrderType = button.getText();
                                                break;
                                            }
                                        }
                                        String updatedStaffMemberName = staffMemberNameField.getText();
                                        String updatedStatus = "";
                                        elements = statusGroup.getElements();
                                        while (elements.hasMoreElements()) {
                                            AbstractButton button = (AbstractButton)elements.nextElement();
                                            if (button.isSelected()) {
                                                updatedStatus = button.getText();
                                                if(updatedStatus.equals("Completed"))
                                                    updatedStatus = "2";
                                                else
                                                    updatedStatus = "1";
                                                break;
                                            }
                                        }
                                        try {
                                            dbHelper.updateOrder(Integer.parseInt(id), updatedName, updatedAddress,
                                                    updatedPhone, updatedDateOrdered, updatedDateNeeded, updatedPaymentMethod, updatedOrderType,
                                                    updatedStaffMemberName, updatedStatus);

                                            for(int i = 0; i < sizes.size(); i++) {
                                                dbHelper.updateSubOrder(rowIDList.get(i), sizes.get(i), prices.get(i));
                                            }

                                            AlertBox.show("Order details updated");
                                            frame.dispose();
                                            frame = null;
                                            System.gc();
                                        }
                                        catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                    catch (Exception ex) {
                                        AlertBox.show("Enter Valid contact number");
                                        ex.printStackTrace();
                                    }
                                }
                            }) {

                            });
                        }


                        mainUpdatePanel.add(panel11);
                        mainUpdatePanel.add(panel12);
                        mainUpdatePanel.add(panel14);
                        mainUpdatePanel.add(panel13);
                        mainUpdatePanel.add(panel15);
                        mainUpdatePanel.add(panel16);
                        mainUpdatePanel.add(scrollPane);
                        mainUpdatePanel.add(panel17);
                        mainUpdatePanel.add(panel18);
                        mainUpdatePanel.add(panel19);
                        mainUpdatePanel.add(panel20);
                        mainUpdatePanel.add(panel21);
                        mainUpdatePanel.add(panel22);

                        frame.add(mainUpdatePanel, BorderLayout.CENTER);

                        frame.setVisible(true);
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    AlertBox.show("Enter valid order ID");
                    searchField.setText("");
                }

            });
            panel.add(updateLabel);
            panel.add(searchField);
            panel.add(searchBtn);

            add(panel);
        }

    }

    private class SearchOrderPanel extends UpdateOrderPanel {

        SearchOrderPanel() {
            initComponents();
        }

        private void initComponents() {
                frame.setTitle("Details of order");
                nameField.setEditable(false);
                addressField.setEditable(false);
                phoneField.setEditable(false);
                dateOrderedField.setEditable(false);
                dateNeededField.setEditable(false);
                smallBox.setEnabled(false);
                regularBox.setEnabled(false);
                mediumBox.setEnabled(false);
                largeBox.setEnabled(false);
                XLBox.setEnabled(false);
                jumboBox.setEnabled(false);
                megaBox.setEnabled(false);
                cashOnDelivery.setEnabled(false);
                cashOnPickUp.setEnabled(false);
                remittance.setEnabled(false);
                walkIn.setEnabled(false);
                phoneReservation.setEnabled(false);
                inProgressBox.setEnabled(false);
                completedBox.setEnabled(false);
                staffMemberNameField.setEditable(false);
                updateLabel.setText("Search order by ID");

        }
    }

    private static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    private static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return formatter.format(date);
    }
}