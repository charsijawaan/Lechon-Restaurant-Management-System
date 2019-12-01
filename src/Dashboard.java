import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class Dashboard extends Activity {

    private DBHelper dbHelper;

    Dashboard() {
        super();
        this.dbHelper = DBHelper.getDBHelper();
        initComponents();
    }

    private void initComponents() {
        setActivityName("Main Menu");
        setDefaultCloseOperation(Activity.EXIT_ON_CLOSE);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(1, 2));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        OptionButton orderBtn = new OptionButton("Order");
        OptionButton customerInfoBtn = new OptionButton("Customer Information");
        OptionButton salesBtn = new OptionButton("Sales");
        OptionButton settingBtn = new OptionButton("Settings");

        orderBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Dashboard.this.setVisible(false);
                OrderWindow window = new OrderWindow();
                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        Dashboard.this.setVisible(true);
                    }
                });
            }
        });

        customerInfoBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Dashboard.this.setVisible(false);
                CustomerInformationWindow window = new CustomerInformationWindow();
                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        Dashboard.this.setVisible(true);
                    }
                });
            }
        });

        salesBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Dashboard.this.setVisible(false);
                SalesWindow window = new SalesWindow();
                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        Dashboard.this.setVisible(true);
                    }
                });
            }
        });

        settingBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Dashboard.this.setVisible(false);
                SettingsWindow window = new SettingsWindow();
                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        Dashboard.this.setVisible(true);
                    }
                });
            }
        });

        JPanel panel1 = new JPanel();
        panel1.add(orderBtn);

        JPanel panel2 = new JPanel();
        panel2.add(customerInfoBtn);

        JPanel panel3 = new JPanel();
        panel3.add(salesBtn);

        JPanel panel4 = new JPanel();
        panel4.add(settingBtn);

        optionsPanel.add(panel1);
        optionsPanel.add(panel2);
        optionsPanel.add(panel3);
        optionsPanel.add(panel4);

        addView(optionsPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private class OptionButton extends JPanel {

        private JLabel label;

        OptionButton(String text) {
            label = new JLabel(text, JLabel.CENTER);
            label.setForeground(Color.white);
            label.setFont(new Font(this.getFont().getName(), Font.BOLD, 17));

            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(250,125));
            setBackground(Color.decode("#50abe8"));
            setOpaque(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    setBackground(Color.decode("#50abe8"));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    setBackground(Color.decode("#4c93c3"));
                }
            });

            add(label, BorderLayout.CENTER);
        }
    }
}
