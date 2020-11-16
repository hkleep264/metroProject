package project.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class MetroAPI {

	//API 암호 Key
	private static final String API_KEY = "%2BOQ3VCpTwnf%2BYN1nnO8EIvOPf%2BR%2BQyeB4R3R7I89r8Q";

	//인스턴스 반환용 변수
	private static MetroAPI instance;

	private MetroAPI()  throws ClassNotFoundException, SQLException{
	}

	/**
	 * Metro API 인스턴스 반환 및 생성
	 * @return MetroAPI Instance
	 * @throws ClassNotFoundException
	 * @throws SQLException
	*/
	public static MetroAPI getInstance() throws ClassNotFoundException, SQLException {
		if(instance == null) {
			instance = new MetroAPI();
		}
		return instance;
	}

	public void close() {
		instance = null;
	}

	/**
	 * RestAPI 호출 하는 메소드(Get 방식)
	 * @param strUrl(API URL)
	 * @return JSONObject(Rest API 호출 결과)
	*/
	public JSONObject get(String strUrl) {
		String resultStr = "";
		strUrl += "&apiKey="+API_KEY;
		try {
			URL url = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
			con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
			con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정

			con.setRequestMethod("GET");



			//			URLConnection에 대한 doOutput 필드값을 지정된 값으로 설정한다. 
			//			URL 연결은 입출력에 사용될 수 있다. 
			//			URL 연결을 출력용으로 사용하려는 경우 DoOutput 플래그를 true로 설정하고, 
			//			그렇지 않은 경우는 false로 설정해야 한다. 기본값은 false이다.

			con.setDoOutput(false); 

			StringBuilder sb = new StringBuilder();

			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				//Stream을 처리해줘야 하는 귀찮음이 있음. 
				BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
				resultStr = sb.toString();
			} else {
			}

		} catch (Exception e) {
		}

		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse( resultStr );
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject result = (JSONObject) obj;

		return result;
	}
	
	/**
	 * @Method Name: checkAround
	 * @작성일: 2020. 11. 16
	 * @작성자: 이현규
	 * @Method 설명: 좌표 주변 지하철이 있는지 확인하는 API ( 참조: https://lab.odsay.com/guide/releaseReference#pointSearch )
	 * @param finalX
	 * @param finalY
	 * @param radius
	 * @return JSONObject(API 호출 결과)
			
	*/
	private JSONObject checkAround(double finalX, double finalY, int radius) {
		
		String url = "https://api.odsay.com/v1/api/pointSearch";
		String param = "lang=0&x="+finalX+"&y="+finalY+"&radius="+radius+"&stationClass=2";
		String set = url + "?"+param;
		JSONObject result = this.get(set);
		
		return result;
		
		
	}
	
	/**
	 * 중심점 찾기 메소드 (최대 최소 거리 계산 후 API 이용)
	 * @param list(역 정보 x,y 좌표)
	 * @return 역이름
	*/
	public String getMidPoint(ArrayList<HashMap<String,Object>> list) {
		
		double xMax = 0;
		double yMax = 0;
		
		double xMin = 0;
		double yMin = 0;
		
		double finalX = 0;
		double finalY = 0;
		
		//각 최대 최소 값 구하기
		for(int i=0; i<list.size(); i++) {
			if(i == 0) {
				xMax += Double.parseDouble(list.get(i).get("x").toString());
				yMax += Double.parseDouble(list.get(i).get("y").toString());
				
				xMin += Double.parseDouble(list.get(i).get("x").toString());
				yMin += Double.parseDouble(list.get(i).get("y").toString());
			}else {
				double tempX = Double.parseDouble(list.get(i).get("x").toString());
				double tempY = Double.parseDouble(list.get(i).get("y").toString());
				
				if(tempX > xMax ) {
					xMax = tempX;
				}
				if(tempY > yMax) {
					yMax = tempY;
				}
				
				if(tempX < xMin ) {
					xMin = tempX;
				}
				if(tempY < yMin) {
					yMin = tempY;
				}
			}
		}
		finalX = xMin + ((xMax - xMin) /2 );
		finalY = yMin + ((yMax - yMin) /2 );
		
		//중심 좌표 찾은 후 지하철 역 찾기
		JSONObject result = new JSONObject();
		int radius = 250;
		while(true) {
			//주변 역이 나올때까지 반경 250m씩 증가하면서 역 확인
			result = this.checkAround(finalX, finalY, radius);
			radius += 250;
			
			JSONObject jsonResult = (JSONObject) result.get("result");
			int count = Integer.parseInt(jsonResult.get("count").toString());
			
			if(count > 0 ) {
				break;
			}
		}
		
		JSONObject jsonResult = (JSONObject) result.get("result");
		int count = Integer.parseInt(jsonResult.get("count").toString());
		String resultStation = "";
		
		if(count >0 ) {
			JSONArray resultArr =  (JSONArray) jsonResult.get("station");
				JSONObject parse = (JSONObject) resultArr.get(0);
				String stationName = parse.get("stationName").toString();
				
				resultStation = stationName;
		}
		return resultStation;
		
	}


}
