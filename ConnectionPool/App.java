import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class App {
    public static void main(String[] args) throws InterruptedException {
        DBPool dbPool = new DBPool();
        System.out.println("Dang khoi tao connection pool--------------");
        dbPool.setPool();
        System.out.println("Khoi tao thanh cong-------------");
        Connection conn = dbPool.checkConnection();
        System.out.println("Kiem tra mot connection trong pool");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println("Dang ket noi..............");
            List<User> list = new LinkedList<User>();
            String query = "select * from user;";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                Integer id = new Integer(rs.getInt(1));
                String user = rs.getString(2);
                String pass = rs.getString(3);
                Integer isActive = new Integer(rs.getInt(4));
                User u = new User(id, user, pass, isActive);
                list.add(u);
            }
            list.forEach(user1 -> System.out.println(user1.getId()+" "+user1.getName()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbPool.swapConnection(conn);
            System.out.println("Chuyen trang thai connection sang READY");
            //Chuyen sang READY khi su dung xong
        }
    }
}
