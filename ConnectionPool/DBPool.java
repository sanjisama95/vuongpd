
import javax.swing.plaf.PanelUI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DBPool {
    private final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost:3306/qluser";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "123456";
    private final int MAX_CONNECTIONS = 10;
    private final int MIN_CONNECTIONS = 2;
    private Lock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();
    private final long TIME_OUT = (long)3e9; //milis
    private final Integer READY_STATUS = new Integer(1); //dang cho
    private final Integer RUNNING_STATUS = new Integer(0); //dang chay

    public static Map<Connection, Integer> pool = new LinkedHashMap<Connection, Integer>();

    //tao mot connection
    public Connection makeDBConnection() {
        Connection conn = null;
        try {
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //khoi tao pool ban dau voi min conection
    public void setPool(){
        while (pool.size()<=MIN_CONNECTIONS){
            Connection conn = makeDBConnection();
            pool.put(conn, READY_STATUS);
        }
    }
    //day connection vao pool vs trang thai running
    public void putConnection(Connection conn){
        conn = makeDBConnection();
        pool.put(conn, RUNNING_STATUS);
    }


    //chuyen trang thai cua connection
    public void swapConnection(Connection conn){
        if (pool.get(conn).equals(RUNNING_STATUS)){
            pool.put(conn,READY_STATUS);
        }
        else pool.put(conn,RUNNING_STATUS);
    }

    //kiem tra connection trong pool
    public Connection checkConnection() throws InterruptedException {
        Connection conn = null;
        if (pool.size()<MAX_CONNECTIONS){
            for(Connection x : pool.keySet()){
                if (pool.get(x).equals(READY_STATUS)){
                    swapConnection(x);
                    return x;
                }
                else{
                    putConnection(conn);
                    return conn;
                }
            }
        }
        else if (pool.size()==MAX_CONNECTIONS&&!pool.containsValue(READY_STATUS)){
            waitConnection(conn);
        }
        return conn;
    }

    //doi connection khi pool day
    public void waitConnection(Connection conn){

    }
}
