import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 基于ConnectionManagerV1存在的问题，可以提出下面解决方案：
 * 既然不需要在线程之间共享这个变量，可以直接这样处理，
 * 在每个需要使用数据库连接的方法中具体使用时才创建数据库链接，然后在方法调用完毕再释放这个连接。
 *
 * 但是，这样的解决方案也存在问题：
 * 这样处理确实也没有任何问题，由于每次都是在方法内部创建的连接，那么线程之间自然不存在线程安全问题。
 * 但是这样会有一个致命的影响：导致服务器压力非常大，并且严重影响程序执行性能。由于在方法中需要频繁地开启和关闭数据库连接，
 * 这样不尽严重影响程序执行效率，还可能导致服务器压力巨大。
 *
 */
public class ConnectionManagerV2 {

    private Connection connection = null;

    public Connection openConnection() throws Exception {
        connection = DriverManager.getConnection("");
        return connection;
    }

    public void closeConnection() throws Exception {
        if (connection != null)
            connection.close();
    }
}

class Dao {
    public void insert() throws Exception {
        ConnectionManagerV2 connectionManager = new ConnectionManagerV2();
        Connection connection = connectionManager.openConnection();

        // 使用connection进行一系列操作

        connectionManager.closeConnection();
    }
}