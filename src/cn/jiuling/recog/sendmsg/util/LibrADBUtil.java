package cn.jiuling.recog.sendmsg.util;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.loocme.sys.util.FileUtil;

public class LibrADBUtil {
	public static final String USERNAME = "omm";
	public static final String PASSWD = "Gaussdba@Mpp";

	public static Connection GetConnection() {
		// 驱动类。
		String driver = "org.postgresql.Driver";
		// 数据库连接描述符。
		String sourceURL = "jdbc:postgresql://116.66.187.62:25308/postgres";
		Connection conn = null;
		try {
			// 加载驱动。
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		try {
			// 创建连接。
			conn = DriverManager.getConnection(sourceURL, USERNAME, PASSWD);
			System.out.println("Connection succeed!");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return conn;
	}

	// 执行普通SQL语句，创建jdbc_test1表。
	@SuppressWarnings("unused")
	public static void CreateTable(Connection conn,String type) {
		String vlprPath = LibrADBUtil.class.getClassLoader().getResource("libra/createVlprTable.txt").getPath();
		String vlprSqlStr = txt2String(new File(vlprPath));
		String objextPath = LibrADBUtil.class.getClassLoader().getResource("libra/createObjextTable.txt").getPath();
		String objextSqlStr = txt2String(new File(objextPath));
		Statement stmt = null;
		try {
			String sql = "";
			if("objext".equals(type)){
				sql = objextSqlStr;
			}else{
				sql = vlprSqlStr;
			}
			stmt = conn.createStatement();
			// add ctrl+c handler
			//Runtime.getRuntime().addShutdownHook(new ExitHandler(stmt));
			// 执行普通SQL语句。
			int rc = stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}
    /**
     * 读取txt文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public  static String txt2String(File file){
    	String result = FileUtil.read(file);
        return result;
    }
	// 执行预处理语句，批量插入数据。
	public static void BatchInsertData(Connection conn) {
		PreparedStatement pst = null;
		try {// 生成预处理语句。
			pst = conn.prepareStatement("INSERT INTO jdbc_test1 VALUES (?,?)");
			for (int i = 0; i < 3; i++) {
				// 添加参数。
				pst.setInt(1, i);
				pst.setString(2, "data " + i);
				pst.addBatch();
			}
			// 执行批处理。
			pst.executeBatch();
			pst.close();
		} catch (SQLException e) {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}

	// 执行预编译语句，更新数据。
	@SuppressWarnings("unused")
	public static void ExecPreparedSQL(Connection conn) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("UPDATE jdbc_test1 SET col2 = ? WHERE col1 = 1");
			pstmt.setString(1, "new Data");
			int rowcount = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}

	// 执行存储过程。
	public static void ExecCallableSQL(Connection conn) {
		CallableStatement cstmt = null;
		try {
			cstmt = conn.prepareCall("{? = CALL TESTPROC(?,?,?)}");
			cstmt.setInt(2, 50);
			cstmt.setInt(1, 20);
			cstmt.setInt(3, 90);
			cstmt.registerOutParameter(4, Types.INTEGER); // 注册out类型的参数，类型为整型。
			cstmt.execute();
			int out = cstmt.getInt(4); // 获取out参数
			System.out.println("The CallableStatment TESTPROC returns:" + out);
			cstmt.close();
		} catch (SQLException e) {
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}
	public static void testResultSet(Connection connection,String type) throws SQLException{
        //获取id=2的customers数据表的记录，并打印
        Statement statement = null;
        ResultSet rs = null;
        try {
            //1.获取Connection
            //2.获取Statement
            statement = connection.createStatement();
            if("vlpr".equals(type)){
            	 //3.准备Sql
                String sql = "SELECT serialnumber,ImageURL,vehicleBrand FROM vlpr_result where serialnumber= 432142314234333 ";
                //4.执行查询，得到ResultSet
                rs = statement.executeQuery(sql);
                //5.处理ResultSet
                while(rs.next()){
                    //rs.get+数据库中对应的类型+(数据库中对应的列别名)
                    String id = rs.getString("serialnumber");
                    String name = rs.getString("ImageURL");
                    String vehicleBrand = rs.getString("vehicleBrand");
                    System.out.println(id);
                    System.out.println(name);
                    System.out.println(vehicleBrand);
                }
            }else{
            	 //3.准备Sql
                String sql = "select id from vlpr_result where  serialnumber = '11123223'";
                //4.执行查询，得到ResultSet
                rs = statement.executeQuery(sql);
                //5.处理ResultSet
                while(rs.next()){
                    //rs.get+数据库中对应的类型+(数据库中对应的列别名)
                	//int id = rs.getInt("count");
                    long count = rs.getLong("id");
                    
                    System.out.println(count);
                    //System.out.println(name);
                }
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //6.关闭数据库相应的资源
        	connection.close();
        }
    }
	/**
	 * 主程序，逐步调用各静态方法。
	 * 
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// 创建数据库连接。
		Connection conn = GetConnection();
		// 创建表。
		//CreateTable(conn,"objext");
		// 批插数据。
		 testResultSet(conn,"objext");
		// 执行预编译语句，更新数据。
		//ExecPreparedSQL(conn);
		// 执行存储过程。
		/*ExecCallableSQL(conn);*/
		// 关闭数据库连接。
		/*try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}
}
