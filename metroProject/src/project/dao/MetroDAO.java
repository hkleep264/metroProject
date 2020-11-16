package project.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import project.vo.MetroVo;

public class MetroDAO {

	//DB ID
	private static final String USERNAME = "admin";
	//DB PW
	private static final String PASSWORD = "admin1234";
	//DB URL
	private static final String URL = "jdbc:mysql://localhost/metroProject";
	//DB 연결 커넥터
	private Connection conn;

	//인스턴스 반환용 변수
	private static MetroDAO instance;

	private MetroDAO()  throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
	}

	/**
	 * Metro DAO 인스턴스 반환 및 생성
	 * @return MetroDAO Instance
	 * @throws ClassNotFoundException
	 * @throws SQLException
	*/
	public static MetroDAO getInstance() throws ClassNotFoundException, SQLException {
		if(instance == null) {
			instance = new MetroDAO();
		}
		return instance;
	}

	public void close() {
		instance = null;
	}

	/**
	 * 지하철 호선 개수 조회
	 * @return 호선 개수
	 * @throws SQLException
	*/
	public int getLineListCnt() throws SQLException{
		
		String sql = "SELECT COUNT(*) AS cnt FROM metro GROUP BY line_no";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		int result = 0;

		while(rs.next()) {

			result = rs.getInt(1);
		}

		rs.close();
		ps.close();


		return result;
	}

	/**
	 * 지하철 호선 리스트 조회
	 * @return 지하철 호선 리스트
	 * @throws SQLException
	*/
	public ArrayList<MetroVo> getLineList() throws SQLException{

		String sql = "SELECT line_no FROM metro GROUP BY line_no";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		ArrayList<MetroVo> list = new ArrayList<MetroVo>();

		while(rs.next()) {
			MetroVo vo = new MetroVo();
			vo.setLine_no(rs.getString(1));

			list.add(vo);
		}

		rs.close();
		ps.close();


		return list;
	}

	/**
	 * 지하철 호선 별 역 개수 조회
	 * @param line(지하철 호선 이름)
	 * @return 지하철 역 개수
	 * @throws SQLException
	*/
	public int getStationListCnt(String line) throws SQLException{

		String sql = "SELECT COUNT(*) FROM metro WHERE line_no = ? AND !ISNULL(x) AND !ISNULL(y)";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, line);
		ResultSet rs = ps.executeQuery();
		int result = 0;

		while(rs.next()) {
			result = rs.getInt(1);
		}

		rs.close();
		ps.close();


		return result;
	}


	/**
	 * 지하철 호선 별 역 조회
	 * @param line(지하철 호선)
	 * @return 역 정보 리스트
	 * @throws SQLException
	*/
	public ArrayList<MetroVo> getStationList(String line) throws SQLException{

		String sql = "SELECT line_no,station_cd,station_nm_ko, fr_code FROM metro WHERE line_no = ? AND !ISNULL(x) AND !ISNULL(y)";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, line);
		ResultSet rs = ps.executeQuery();
		ArrayList<MetroVo> list = new ArrayList<MetroVo>();

		while(rs.next()) {
			MetroVo vo = new MetroVo();
			vo.setLine_no(rs.getString(1));
			vo.setStation_cd(rs.getString(2));
			vo.setStation_nm_ko(rs.getString(3));
			vo.setFr_code(rs.getString(4));

			list.add(vo);
		}

		rs.close();
		ps.close();


		return list;
	}

	/**
	 * 지하철 고유 코드 조회
	 * @param stationName(역 이름), line(호선 이름)
	 * @return 지하철 역 상세 정보
	 * @throws SQLException
	*/
	public MetroVo getStationFrCode(String stationName, String line) throws SQLException {

		String sql = "SELECT fr_code,x,y FROM metro WHERE station_nm_ko = ? AND line_no=? AND !ISNULL(x) AND !ISNULL(y)";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, stationName);
		ps.setString(2, line);
		ResultSet rs = ps.executeQuery();
		MetroVo vo = new MetroVo();
		while(rs.next()) {
			
			vo.setFr_code(rs.getString(1));
			vo.setX(rs.getString(2));
			vo.setY(rs.getString(3));
		}

		rs.close();
		ps.close();


		return vo;

	}
	
	/**
	 * 지하철 역 상세 정보 조회 (역 고유 코드 이용)
	 * @param frCode(역 고유 코드)
	 * @return 지하철 역 정보
	 * @throws SQLException
	*/
	public MetroVo getStationInfoByFrCode(String frCode) throws SQLException {

		String sql = " SELECT line_no,station_cd,station_nm_ko, fr_code FROM metro WHERE fr_code = ? ";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, frCode);
		ResultSet rs = ps.executeQuery();
		MetroVo vo = new MetroVo();
		while(rs.next()) {
			
			vo.setLine_no(rs.getString(1));
			vo.setStation_nm_ko(rs.getString(3));
		}

		rs.close();
		ps.close();

		return vo;

	}
	

}
