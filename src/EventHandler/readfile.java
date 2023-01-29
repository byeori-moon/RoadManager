package EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class readfile{
	int find_another (String date, int where) {
		String copydate = "";
		int when = 0;
		int Atime = 0;
		int Btime = 0;
		int time = 0;
		writefile Writefile = new writefile();
		System.out.println(Atime);
		if(date.charAt(6) != 48) {
			time = 10*(date.charAt(6)-48);
		}
		time += date.charAt(7)-48;
		Atime = time;
		System.out.println(return_schedule(date, 1));
		System.out.println(date + "--1");
		if(return_schedule(date, 1).equals("#")) {
			for(int i = time+1;i < 25;i++) {
				time = 100*Integer.parseInt(date.substring(0,6)) + i;
				copydate = Integer.toString(time);
				if( !return_schedule(copydate, 1).equals("#")) {	//파일이 존재하면
						Atime = i;
						break;
				}
			}
		}
		System.out.println(Atime);
		if(where == 1) {
			for(int i = time+1;i < 25;i++) {
				time = 100*Integer.parseInt(date.substring(0,6)) + i;
				copydate = Integer.toString(time);
				if( !return_schedule(copydate, 1).equals("#")) {	//파일이 존재하면
					Atime = i;
					break;
				}
			}
		}
		else if (where == -1) {
			for(int i = time-1;i > -1;i--) {
				time = 100*Integer.parseInt(date.substring(0,6)) + i;
				copydate = Integer.toString(time);
				if(!return_schedule(copydate, 1).equals("#")) {	//파일이 존재하면
						Atime = i;
						break;
				}
			}
		}else {
			return Atime;
		}
		return Atime;
	}
	
	String return_schedule(String date/* yymmddtt 형식입니다.*/, int a /*1~9*/) {
		String dummy = "";
		String hr = "";
		int time = 0;
		int day = 0;
		int month = 0;
		int year = 0;
		year = 10*(date.charAt(0)-48);
		year += date.charAt(1)-48;
		if(date.charAt(2) != 48) {
			month = 10*(date.charAt(2)-48);
		}
		month += date.charAt(3)-48;				//48은 아스키코드 0
		if(date.charAt(4) != 48) {
			day = 10*(date.charAt(4)-48);
		}
		day += date.charAt(5)-48;
		if(date.charAt(6) != 48) {
			time = 10*(date.charAt(6)-48);
		}
		time += date.charAt(7)-48;

		System.out.println(date + "--4");
		System.out.println(time + "--5");
		String directory = "./data/B/"+Integer.toString(year) + "." + Integer.toString(month) 
							+ "." + Integer.toString(day) + "."+ Integer.toString(time) + ".txt";
		File file = new File(directory);
		
		if (!file.exists()) return "#";
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = "";
			for(int i=1; i < a ; i++) {
			    line = br.readLine(); //읽으며 이동
			}
			if(a != 9) {
				if(a == 7) {
					hr = br.readLine();
					line = hr.substring(0, 3) + "" + hr.substring(4);
					System.out.println("modified ------- "+line);
				}
				else line = br.readLine();
				return line;
			}
			else {
				while((line = br.readLine())!=null) {
					dummy += (line + "\r\n" ); 
				}
				return dummy;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}
}
