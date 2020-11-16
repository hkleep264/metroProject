package project.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import project.vo.MemberVo;


public class MemberDAO {
	
	//DB ID
	private static final String USERNAME = "admin";
	//DB PW
	private static final String PASSWORD = "admin1234";
	//DB URL
	private static final String URL = "jdbc:mysql://localhost/metroProject";
	//DB 연결 커넥터
	private Connection conn;
	//인스턴스 반환용 변수
	private static MemberDAO instance;
	
	private MemberDAO()  throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
	}
	
	/**
	 * Member DAO 인스턴스 반환 및 생성
	 * @return MemberDAO Instance
	 * @throws ClassNotFoundException
	 * @throws SQLException
	*/
	public static MemberDAO getInstance() throws ClassNotFoundException, SQLException {
		if(instance == null) {
			instance = new MemberDAO();
		}
		return instance;
	}
	
	public void close() {
		instance = null;
	}
	
	/**
	 * 유저 Id/PW 맞는지 확인(로그인)
	 * @param vo(Id,Pw)
	 * @return 존재 하는 유저:true 없는유저:false
	 * @throws SQLException
	*/
	public boolean login(MemberVo vo) throws SQLException {
		
		//로그인
		//SHA1(MD5()): 단방향 암호화 함수
		String sql = "SELECT * FROM member WHERE id = ? AND pw = SHA1(MD5(?))";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, vo.getId());
		ps.setString(2, vo.getPw());
		
		ResultSet rs = ps.executeQuery();
		
		//유저 있는지 체크 후 있다면 true 없다면 false
		if(rs.next()) {
			
			rs.close();
			ps.close();
			return true;
		}else {
			rs.close();
			ps.close();
			return false;
		}
		
	}
	
	/**
	 * 해당 아이디가 있는지 확인(중복확인)
	 * @param id(아이디)
	 * @return 존재 하는 유저:true 없는유저:false
	 * @throws SQLException
	*/
	public boolean isId(String id) throws SQLException{

		//중복 체크
		String sql = "SELECT * FROM member WHERE id = ? ";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		
		//유저 있는지 체크 후 있다면 true 없다면 false
		if(rs.next()) {
			
			rs.close();
			ps.close();
			return true;
		}else {
			rs.close();
			ps.close();
			return false;
		}
	}
	
	
	/**
	 * 회원가입 
	 * @param vo(유저 정보)
	 * @return 성공: true 실패: false
	 * @throws SQLException
	*/
	public boolean signUp(MemberVo vo) throws SQLException{
		
		//회원가입
		String sql = "INSERT INTO member (name, id, pw, reg_date) VALUES(?,?,SHA1(MD5(?)),DEFAULT)";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, vo.getName());
		ps.setString(2, vo.getId());
		ps.setString(3, vo.getPw());
		
		
		if(ps.executeUpdate() >0) {
			ps.close();
			return true;
		}else {
			ps.close();
			return false;
		}
	}

}
