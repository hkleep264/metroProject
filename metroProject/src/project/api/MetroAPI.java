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

	//API ��ȣ Key
	private static final String API_KEY = "%2BOQ3VCpTwnf%2BYN1nnO8EIvOPf%2BR%2BQyeB4R3R7I89r8Q";

	//�ν��Ͻ� ��ȯ�� ����
	private static MetroAPI instance;

	private MetroAPI()  throws ClassNotFoundException, SQLException{
	}

	/**
	 * Metro API �ν��Ͻ� ��ȯ �� ����
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
	 * RestAPI ȣ�� �ϴ� �޼ҵ�(Get ���)
	 * @param strUrl(API URL)
	 * @return JSONObject(Rest API ȣ�� ���)
	*/
	public JSONObject get(String strUrl) {
		String resultStr = "";
		strUrl += "&apiKey="+API_KEY;
		try {
			URL url = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
			con.setConnectTimeout(5000); //������ ����Ǵ� Timeout �ð� ����
			con.setReadTimeout(5000); // InputStream �о� ���� Timeout �ð� ����

			con.setRequestMethod("GET");



			//			URLConnection�� ���� doOutput �ʵ尪�� ������ ������ �����Ѵ�. 
			//			URL ������ ����¿� ���� �� �ִ�. 
			//			URL ������ ��¿����� ����Ϸ��� ��� DoOutput �÷��׸� true�� �����ϰ�, 
			//			�׷��� ���� ���� false�� �����ؾ� �Ѵ�. �⺻���� false�̴�.

			con.setDoOutput(false); 

			StringBuilder sb = new StringBuilder();

			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				//Stream�� ó������� �ϴ� �������� ����. 
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
	 * @�ۼ���: 2020. 11. 16
	 * @�ۼ���: ������
	 * @Method ����: ��ǥ �ֺ� ����ö�� �ִ��� Ȯ���ϴ� API ( ����: https://lab.odsay.com/guide/releaseReference#pointSearch )
	 * @param finalX
	 * @param finalY
	 * @param radius
	 * @return JSONObject(API ȣ�� ���)
			
	*/
	private JSONObject checkAround(double finalX, double finalY, int radius) {
		
		String url = "https://api.odsay.com/v1/api/pointSearch";
		String param = "lang=0&x="+finalX+"&y="+finalY+"&radius="+radius+"&stationClass=2";
		String set = url + "?"+param;
		JSONObject result = this.get(set);
		
		return result;
		
		
	}
	
	/**
	 * �߽��� ã�� �޼ҵ� (�ִ� �ּ� �Ÿ� ��� �� API �̿�)
	 * @param list(�� ���� x,y ��ǥ)
	 * @return ���̸�
	*/
	public String getMidPoint(ArrayList<HashMap<String,Object>> list) {
		
		double xMax = 0;
		double yMax = 0;
		
		double xMin = 0;
		double yMin = 0;
		
		double finalX = 0;
		double finalY = 0;
		
		//�� �ִ� �ּ� �� ���ϱ�
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
		
		//�߽� ��ǥ ã�� �� ����ö �� ã��
		JSONObject result = new JSONObject();
		int radius = 250;
		while(true) {
			//�ֺ� ���� ���ö����� �ݰ� 250m�� �����ϸ鼭 �� Ȯ��
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
