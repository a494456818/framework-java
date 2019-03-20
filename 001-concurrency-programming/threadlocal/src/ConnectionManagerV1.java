import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 在单线程中，该段代码没有任何问题
 * 但是在多线程的环境中，存在以下问题：
 *  1. openConnection()和closeConnection()都没有进行同步，很有可能会在openConnection()创建多个连接connection
 *  2. connection是共享变量，那么必然在调用connection的地方需要使用到同步来保障线程的安全，因为很有可能出现，A线程调用openConnection()，
 *  B线程调用和closeConnection()。
 *
 *  所以，出于安全的考虑，该代码必须将两个方法进行同步处理，并且在调用connection的时候进行同步处理。
 *  这样会大大影响程序的执行效率，因为当一个线程对connection进行操作时，其他线程只能等待。
 *
 *  那么大家来仔细分析一下这个问题，这地方到底需不需要将connect变量进行共享？事实上，是不需要的。
 *  假如每个线程中都有一个connect变量，各个线程之间对connect变量的访问实际上是没有依赖关系的，
 *  即一个线程不需要关心其他线程是否对这个connect进行了修改的。
 */
public class ConnectionManagerV1 {

    private static Connection connection = null;

    public static Connection openConnection() throws Exception {
        if (connection == null) {
                connection = DriverManager.getConnection("");
        }
        return connection;
    }

    public static void closeConnection() throws Exception {
        if (connection != null)
            connection.close();
    }

}
