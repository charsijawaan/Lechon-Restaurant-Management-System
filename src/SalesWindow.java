import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

class SalesWindow extends Activity {

    private DBHelper dbHelper;

    SalesWindow() {
        dbHelper = DBHelper.getDBHelper();
        initComponents();
    }

    private void initComponents() {
        setActivityName("Sales");

        SalesReportPanel salesReportPanel = new SalesReportPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Sales Report", salesReportPanel);

        addView(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private class SalesReportPanel extends JPanel {

        private String fromDate, toDate;
        private JLabel totalAmountLabel;
        private JTable reportTable;
        private DefaultTableModel tableModel;

        SalesReportPanel() {
            tableModel = new DefaultTableModel() {

                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            fromDate = "";
            toDate = "";
            initComponents();
        }

        private void initComponents() {
            setLayout(new BorderLayout());

            JPanel ctrlsPanel = new JPanel();
            ctrlsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            JButton fromDateBtn = new JButton("From Date");
            JTextField fromDateField = new JTextField(20);
            fromDateField.setEditable(false);
            fromDateField.setEnabled(false);
            fromDateBtn.addActionListener((e) -> new DatePicker((day, month, year, hour, minute) -> {
                fromDate = year + "-" + month + "-" + day;
                fromDateField.setText(fromDate);
            }));

            JButton toDateBtn = new JButton("To Date");
            JTextField toDateField = new JTextField(20);
            toDateField.setEditable(false);
            toDateField.setEnabled(false);
            toDateBtn.addActionListener((e) -> new DatePicker((day, month, year, hour, minute) -> {
                toDate = year + "-" + month + "-" + day;
                toDateField.setText(toDate);
            }));

            JButton generateReportBtn = new JButton("Generate Report");
            generateReportBtn.addActionListener((e) -> {
                if(!fromDate.isEmpty() && !toDate.isEmpty()) {
                    fillTable();
                }
                else {
                    AlertBox.show("Please enter \"From Date\" and \"To Date\" to proceed!");
                }
            });

            JButton printReportBtn = new JButton("Print Report");
            printReportBtn.addActionListener((e) -> {
                MessageFormat header = new MessageFormat("Report");
                MessageFormat footer = new MessageFormat("Page{0,number,integer}");
                try {
                    reportTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            ctrlsPanel.add(fromDateBtn);
            ctrlsPanel.add(fromDateField);
            ctrlsPanel.add(toDateBtn);
            ctrlsPanel.add(toDateField);
            ctrlsPanel.add(generateReportBtn);
            ctrlsPanel.add(printReportBtn);

            JPanel totalAmountsPanel = new JPanel();
            totalAmountsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            totalAmountLabel = new JLabel();
            totalAmountLabel.setText("Total Sales : 0");
            totalAmountLabel.setFont(new Font(totalAmountLabel.getFont().getName(), Font.BOLD, 16));
            totalAmountLabel.setForeground(Color.decode("#57bf13"));

            totalAmountsPanel.add(totalAmountLabel);

            reportTable = new JTable(tableModel);
            tableModel.addColumn("Customer Id");
            tableModel.addColumn("Customer Name");
            tableModel.addColumn("Address");
            tableModel.addColumn("Phone #");
            tableModel.addColumn("Date Ordered");
            tableModel.addColumn("Payment Method");
            tableModel.addColumn("Bill Amount");

            add(ctrlsPanel, BorderLayout.NORTH);
            add(new JScrollPane(reportTable), BorderLayout.CENTER);
            add(totalAmountsPanel, BorderLayout.SOUTH);
        }

        private void fillTable() {
            reportTable.removeAll();
            tableModel.setRowCount(0);
            try {
                int totalSales = 0;
                ResultSet resultSet;
                resultSet = dbHelper.fetchSalesReport(fromDate, toDate);

                while (resultSet.next()) {
                    String customerId = resultSet.getString("customer_id");
                    String customerName = resultSet.getString("customer_name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    String dateOrdered = resultSet.getString("date_ordered");
                    String paymentMethod = resultSet.getString("payment_method");
                    int price = resultSet.getInt("price");

                    totalSales += price;

                    tableModel.addRow(new Object[] {customerId, customerName, address,
                            phone, dateOrdered, paymentMethod, price});
                }
                totalAmountLabel.setText("Total Sales : " + totalSales);
            }
            catch (SQLException ex) {
                AlertBox.show("Some database error occurred!");
                ex.printStackTrace();
            }
        }

    }
}
