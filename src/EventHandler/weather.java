package EventHandler;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

	
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.Math;
import java.io.*;import java.text.SimpleDateFormat;import java.util.*;

	public class weather
	{
		private static int index = 0; // 현재 인덱스 저장용
		
		//The basic x,y value is the index of CAU
		private static int x=60;
		private static int y=125;
		
		private static int[] get_index = {};
		private static String[] v = new String[5];

		//날씨 정보 받아오기
		public static String getWeather()
		{
			HttpURLConnection con = null;
			String s = null; // 에러 메시지
			
			try
			{
				LocalDateTime t = LocalDateTime.now().minusMinutes(30); // 현재 시각 30분전
				
				URL url = new URL(
					"http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"
					+ "?ServiceKey=0BBPvHJhmv3Q4iW1QIat64D3%2BpRzSV8BlcIYtudRNA0jNQZYTWbqQXORr%2BI%2BbyPKpeuOK67Sqt3NwDpugsQojw%3D%3D" // 서비스키
					+ "&numOfRows=60" // 한 페이지 결과 수 (10개 카테고리값 * 6시간)
					+ "&base_date=" + t.format(DateTimeFormatter.ofPattern("yyyyMMdd"))  // 발표 날짜
					+ "&base_time=" + t.format(DateTimeFormatter.ofPattern("HHmm")) // 발표 시각
					+ "&nx=" + x// 예보지점의 X 좌표값
					+ "&ny=" + y // 예보지점의 Y 좌표값
				);
				
				con = (HttpURLConnection)url.openConnection();
				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());

				boolean ok = false; // <resultCode>00</resultCode> 획득 여부
				
				Element e;
				NodeList ns = doc.getElementsByTagName("header");
				if (ns.getLength() > 0)
				{
					e = (Element)ns.item(0);
					if ("00".equals(e.getElementsByTagName("resultCode").item(0).getTextContent()))
						ok = true; // 성공 여부
					else // 에러 메시지
						s = e.getElementsByTagName("resultMsg").item(0).getTextContent();
				}
					
				if (ok)
				{
					String fd = null, ft = null; // 가장 빠른 예보 시각
					String pty = null; // 강수형태
					String sky = null; // 하늘상태
					String cat; // category
					String val; // fcstValue
					
					ns = doc.getElementsByTagName("item");
					for (int i = 0; i < ns.getLength(); i++)
					{
						e = (Element)ns.item(i);
						
						if (ft == null)
						{ // 가져올 예보 시간 결정
							fd = e.getElementsByTagName("fcstDate").item(0).getTextContent(); // 예보 날짜
							//fd = sdformat.format(date);			
							
							ft = e.getElementsByTagName("fcstTime").item(0).getTextContent(); // 예보 시각
						}
						else if (!fd.equals(e.getElementsByTagName("fcstDate").item(0).getTextContent()) ||
								!ft.equals(e.getElementsByTagName("fcstTime").item(0).getTextContent()))
								continue; // 결정된 예보 시각과 같은 시각의 것만 취한다.
						
						cat = e.getElementsByTagName("category").item(0).getTextContent(); // 자료구분코드
						val = e.getElementsByTagName("fcstValue").item(0).getTextContent(); // 예보 값
						
						if ("PTY".equals(cat)) pty = val; // 강수형태
						else if ("SKY".equals(cat)) sky = val; // 하늘상태
						else if ("T1H".equals(cat)) v[3] = val; // 기온
						else if ("REH".equals(cat)) v[4] = val; // 습도
					}
					
					v[0] = fd;
					v[1] = ft;
					
					if ("0".equals(pty))
					{ // 강수형태 없으면, 하늘상태로 판단
						if ("1".equals(sky)) v[2] = "sunny";
						else if ("3".equals(sky)) v[2] = "littlecloudy";
						else if ("4".equals(sky)) v[2] = "cloudy";
					}
					else if ("1".equals(pty)) v[2] = "rainy";
					else if ("2".equals(pty)) v[2] = "rainnsnow";
					else if ("3".equals(pty)) v[2] = "snow";
					else if ("5".equals(pty)) v[2] = "rainy";
					else if ("6".equals(pty)) v[2] = "rainnsnow";
					else if ("7".equals(pty)) v[2] = "snow";
					return v[2];
		        }
			}
			catch (Exception e)
			{
				s = e.getMessage();
			}
			
			if (con != null)
				con.disconnect();

			return s;
		}
	}