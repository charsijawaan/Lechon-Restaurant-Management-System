import java.sql.*;

class DBHelper {

    private static final DBHelper dbHelper = new DBHelper();
    private Connection c;

    private DBHelper() {
    }

    static DBHelper getDBHelper() {
        return dbHelper;
    }

    boolean openConnection() {
        try {
            c = DriverManager.getConnection("jdbc:sqlite:" + Constants.DB_NAME);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    void placeOrder(String name, String address, String phone, String dateOrdered, String dateNeeded,
                    String paymentMethod, String orderType,
                    String staff_member_name, int customerID) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "INSERT INTO orders ('name', 'address', 'phone', 'date_ordered', 'date_needed'," +
                " 'payment_method', 'order_type', 'staff_member_name', 'status', 'customer_id')" +
                "VALUES('"+name+"', '"+address+"', '"+phone+"', '"+dateOrdered+"'," +
                " '"+dateNeeded+"', '"+paymentMethod+"', '"+orderType+"'," +
                " '"+staff_member_name+"', 1, "+customerID+")";
        stmt.execute(sql);
    }

    void insertSubOrder(String orderId, String size, String price) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "INSERT INTO sub_orders(order_id, size, price)" +
                " VALUES (" + orderId + ", '" + size + "', " + price + ")";
        stmt.execute(sql);
    }

    ResultSet getLastOrderID() throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM orders WHERE id = (SELECT MAX(id)  FROM orders)";
        return stmt.executeQuery(sql);
    }

    ResultSet getLastCustomerID() throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM customer WHERE customer_id = (SELECT MAX(customer_id)  FROM customer)";
        return stmt.executeQuery(sql);
    }

    ResultSet getActiveOrders() throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM orders WHERE status = 1";
        return stmt.executeQuery(sql);
    }

    ResultSet getOrderByID(int id) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM orders WHERE id = " + id +" ";
        return stmt.executeQuery(sql);
    }

    void updateOrder(int id, String name, String address, String phone, String dateOrdered,
                     String dateNeeded, String paymentMethod, String orderType, String staffMemberName, String status) throws SQLException{
        Statement stmt = c.createStatement();
        String sql = "UPDATE orders SET name = '"+name+"', address = '"+address+"', phone = '"+phone+"'," +
                " date_ordered = '"+dateOrdered+"', date_needed = '"+dateNeeded+"', payment_method = '"+paymentMethod+"'," +
                "  order_type = '"+orderType+"', staff_member_name = '"+staffMemberName+"', status = '"+status+"' WHERE id = "+id+"";
        stmt.execute(sql);
    }

    void updateSubOrder(int rowid, String size, String price) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "UPDATE sub_orders SET size = '"+size+"', price = "+price+" WHERE rowid = "+rowid+"";
        System.out.println(sql);
        stmt.execute(sql);
    }

    ResultSet getPrice() throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM prices";
        return stmt.executeQuery(sql);
    }

    void updatedPrices(int small, int regular, int medium, int large,
                       int xl, int jumbo, int mega) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "UPDATE prices SET small = "+small+", regular = "+regular+"," +
                " medium = "+medium+", large = "+large+", xl = "+xl+"," +
                " jumbo = "+jumbo+", mega = "+mega+" ";
        stmt.execute(sql);
    }

    ResultSet checkAvailableAccount(String phone) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM customer WHERE customer_phone = '"+phone+"'";
        return stmt.executeQuery(sql);
    }

    void makeNewAccount(String name, String pic, String address, String phone) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "INSERT INTO customer(customer_name, customer_pic, customer_address, customer_phone)" +
                " VALUES('"+name+"', '"+pic+"', '"+address+"', '"+phone+"')";
        stmt.execute(sql);
    }

    ResultSet getCustomerInfo(String phone) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM customer WHERE customer_phone = '"+phone+"'";
        return stmt.executeQuery(sql);
    }

    ResultSet getCustomerIDByPhone(String phone) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT customer_id FROM customer WHERE customer_phone = '"+phone+"'";
        return stmt.executeQuery(sql);
    }

    void changePicNameInCustomerTable(int id, String path) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "UPDATE customer SET customer_pic = '"+path+"' WHERE customer_id = "+id+"";
        stmt.execute(sql);
    }

    ResultSet getAllTimeCustomerOrderInformation(int id) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM orders WHERE customer_id = "+id+" AND status = 2";
        return stmt.executeQuery(sql);
    }

    void updateCustomerDetails(int id, String name, String phone, String address) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "UPDATE customer SET customer_name = '"+name+"', customer_phone = '"+phone+"'," +
                " customer_address = '"+address+"' WHERE customer_id = "+id+" ";
        stmt.execute(sql);
    }

    void deleteCustomerAccount(String phone) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "DELETE FROM customer WHERE customer_phone = '"+phone+"'";
        stmt.execute(sql);
        sql = "DELETE FROM orders WHERE phone = '"+phone+"'";
        stmt.execute(sql);
    }

    ResultSet fetchSalesReport(String fromDate, String toDate) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT orders.customer_id, customer.customer_name, orders.address, orders.phone," +
                " orders.date_ordered, orders.payment_method, orders.price FROM orders INNER JOIN " +
                "customer ON orders.customer_id = customer.customer_id WHERE orders.status = 2 AND " +
                "orders.date_ordered >= datetime('" + fromDate + " 00:00') AND orders.date_ordered <= " +
                "datetime('" + toDate + " 23:59')";

        return stmt.executeQuery(sql);
    }

    ResultSet getCustomerByName(String name) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM customer WHERE customer_name LIKE '"+name+"%'";
        return stmt.executeQuery(sql);
    }

    ResultSet getTotalBill(int orderID) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT SUM(s.price) AS total_bill FROM orders o" +
                " INNER JOIN sub_orders s ON s.order_id=o.id" +
                " WHERE o.id = " + orderID;
        return stmt.executeQuery(sql);
    }

    ResultSet getSubOrderSize(String id) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM sub_orders WHERE order_id = "+id+" ";
        return  stmt.executeQuery(sql);
    }

}