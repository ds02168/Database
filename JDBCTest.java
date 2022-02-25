package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBCTest {
	
	public static Connection conn;
	public static PreparedStatement query;
	public static ResultSet rest;
	
	public static void main(String[] args) {
		int sid;
		int deptno;
		Scanner scanner = new Scanner(System.in);
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException e) {
			System.err.println("ClassNotFoundException : " + e.getMessage());
		}
		
		//2. 학번을 입력받아 그 학생의 모든 속성을 출력
		System.out.print("학번? : ");
		sid = scanner.nextInt();
		selectSQL(sid);
		
		
		//3. 학과 번호를 입력받아 그 학과에 재학중인 학생의 성적을 0.01 더하라.
		System.out.print("학과 번호? : ");
		deptno = scanner.nextInt();
		updateSQL(deptno);
		
		
		//4. (학과번호, 학번)의 오름차순으로 모든 학생의 학과번호, 학번, 이름, 성적을 출력하라.
		showAll();
	}
	
	
	public static void selectSQL(int sid) {
		openDB();
		String sql = "select * from student where sid = ?";
		try {
			query = conn.prepareStatement(sql);
			query.setInt(1, sid);
			rest = query.executeQuery();
			
			while(rest.next()) {
				System.out.println("SID : " + rest.getInt(1) + ", SNAME : " + rest.getString("SNAME")
				+ ", DEPTNO : " + rest.getInt("deptno") + ", ADVISOR : " + rest.getInt("ADVISOR")
				+ ", GEN : " + rest.getString("GEN") + ", ADDR : " + rest.getString("addr")
				+ ", BIRTHDATE : " + rest.getDate("BIRTHDATE") + ", GRADE : " + rest.getInt("grade"));
			}
			
		}catch(SQLException sqle) {
			System.err.println("SQLException : " + sqle);
		}
		closeDB();
	}
	
	public static void updateSQL(int deptno) {
		openDB();
		String sql = "UPDATE student SET GRADE = GRADE+0.01 WHERE deptno = ?";
		try {
			query = conn.prepareStatement(sql);
			query.setInt(1, deptno);
			query.executeUpdate();
		}catch(SQLException sqle) {
			System.err.println("SQLException : " + sqle);
		}
		closeDB();
	}
	
	public static void showAll() {
		openDB();
		String sql = "select deptno, sid, sname, grade from student order by deptno, sid";
		try {
			query = conn.prepareStatement(sql);
			rest = query.executeQuery();
			
			while(rest.next()) {
				System.out.println("DEPTNO : " + rest.getInt(1) + ", SID : " + rest.getInt(2)
				+ ", SNAME : " + rest.getString(3) + ", GRADE : " + rest.getFloat(4));
			}
		}catch(SQLException sqle) {
			System.err.println("SQLException : " + sqle);
		}
		closeDB();
	}
	
	//1. DB 연결은 ("jdbc:oracle:thin:@//localhost:1521/xepdb1", "scott", "tiger") 로 고정. 
	public static void openDB() {
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1","scott","tiger");
		}catch(SQLException sqle) {
			System.err.println("SQLException : " + sqle);
		}
	}
	
	
	
	public static void closeDB() {
		if(rest != null) {
			try {
				rest.close();
			}catch(SQLException sqle) {
				System.err.println("SQLException : " + sqle);
			}
		}
		
		if(query != null) {
			try {
				query.close();
			}catch(SQLException sqle) {
				System.err.println("SQLException : " + sqle);
			}
		}
		
		if(conn != null) {
			try {
				conn.close();
			}catch(SQLException sqle) {
				System.err.println("SQLException : " + sqle);
			}
		}
	}
}
