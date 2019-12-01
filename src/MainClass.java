import javax.swing.*;

public class MainClass {

    static {
        setUiTHeme();
    }

    public static void main(String[] args) {
        boolean TESTING_MODE = false;
        DBHelper dbHelper = DBHelper.getDBHelper();
        boolean connected = dbHelper.openConnection();

        // Launch the application
        if(connected) {
            new Dashboard();
        }
        else
            AlertBox.show("Unable to connect to pure lake database.");

    }

    private static void setUiTHeme() {
        boolean nimbusFound = false;
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    nimbusFound = true;
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            if(!nimbusFound) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
