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
	//DB ���� Ŀ����
	private Connection conn;
	//�ν��Ͻ� ��ȯ�� ����
	private static MemberDAO instance;
	
	private MemberDAO()  throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
	}
	
	/**
	 * Member DAO �ν��Ͻ� ��ȯ �� ����
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
	 * ���� Id/PW �´��� Ȯ��(�α���)
	 * @param vo(Id,Pw)
	 * @return ���� �ϴ� ����:true ��������:false
	 * @throws SQLException
	*/
	public boolean login(MemberVo vo) throws SQLException {
		
		//�α���
		//SHA1(MD5()): �ܹ��� ��ȣȭ �Լ�
		String sql = "SELECT * FROM member WHERE id = ? AND pw = SHA1(MD5(?))";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, vo.getId());
		ps.setString(2, vo.getPw());
		
		ResultSet rs = ps.executeQuery();
		
		//���� �ִ��� üũ �� �ִٸ� true ���ٸ� false
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
	 * �ش� ���̵� �ִ��� Ȯ��(�ߺ�Ȯ��)
	 * @param id(���̵�)
	 * @return ���� �ϴ� ����:true ��������:false
	 * @throws SQLException
	*/
	public boolean isId(String id) throws SQLException{

		//�ߺ� üũ
		String sql = "SELECT * FROM member WHERE id = ? ";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		
		//���� �ִ��� üũ �� �ִٸ� true ���ٸ� false
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
	 * ȸ������ 
	 * @param vo(���� ����)
	 * @return ����: true ����: false
	 * @throws SQLException
	*/
	public boolean signUp(MemberVo vo) throws SQLException{
		
		//ȸ������
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
