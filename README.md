# metroProject

## 프로그램 주제 
지하철 중간지점 찾아주기

## 핵심 기능
로그인/회원가입

위치 기반 지하철 중간지점 찾아주기 

## YouTube
[![Watch the video](https://img.youtube.com/vi/DoV3HBcHchE/hqdefault.jpg)](https://youtu.be/DoV3HBcHchE)

## JAVADOC

https://hkleep264.github.io/metroProject/metroProject/doc/index.html


## Java

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
		finalX = xMin + ((xMax - xMin) /2 );   //유저들의 중간지점 찾기(위도)
		finalY = yMin + ((yMax - yMin) /2 );  //유저들의 중간지점 찾기(경도)
		
		.....
		
	}
  
  
  private JSONObject checkAround(double finalX, double finalY, int radius) {
		
    //중간 지점 주변 지하철 역 찾기
		String url = "https://api.odsay.com/v1/api/pointSearch";
		String param = "lang=0&x="+finalX+"&y="+finalY+"&radius="+radius+"&stationClass=2";
		String set = url + "?"+param;
		JSONObject result = this.get(set);
		
		return result;
		
	}
  
## 힘들었던 점
지하철 역 사이 좌표 지점 찾기 알고리즘 만드는 부분

여전히 이쁘지는 않은 UI
