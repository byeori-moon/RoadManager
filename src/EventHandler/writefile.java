package EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class writefile{
	void correction_schedule_number(String date/* yymmddtt 형식입니다.*/, int a /* +-1*/) {	//일정의 갯수 수정
		System.out.println("--9");
		String dummy = "";
		int position = 0; // day
		int month = 0;
		int year = 0;
		String path = "";
		year = 10*(date.charAt(0)-48); //22061501 48 -> ASCII '0'
		year += date.charAt(1)-48;
		if(date.charAt(2) != 48) {
			month = 10*(date.charAt(2)-48);
		}
		month += date.charAt(3)-48;
		if(date.charAt(4) != 48) {
			position = 10*(date.charAt(4)-48); // charAt(6) 이라고 되어 있던 오류 수
		}
		position += date.charAt(5)-48;
		String fixed = "";
		String directory = "./data/A/"+ Integer.toString(year) + "." + Integer.toString(month) + ".txt";
		String copydirectory = "./data/A/11." + Integer.toString(month) + ".txt";
		System.out.println(directory + "--10");
		System.out.println(position + "--11");
		File file = new File(directory);
		try {
			System.out.println(file.getCanonicalPath() + "--12");
			 if (file.createNewFile()) { // 파일이 존재하는지 조사
				 System.out.println("--13");
				 File copyfile = new File(copydirectory);
				 BufferedReader co = new BufferedReader(new InputStreamReader(new FileInputStream(copyfile)));
				 String copydummy = "";
				 String copyline = "";
				 while((copyline = co.readLine())!=null) {
						copydummy += (copyline + "\r\n" ); 
					}
				 FileWriter bo = new FileWriter(directory);
					bo.write(copydummy);			
					bo.close();
					co.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = "";
			for(int i=1; i < position ; i++) {
				System.out.println(line + "--14");
			    line = br.readLine(); //읽으며 이동
			    dummy += (line + "\r\n" );
			}
			String fixData = br.readLine();
			if(a == 1) {
				System.out.println(fixData + "--15");
				fixed += fixData.charAt(0)+""+fixData.charAt(1)+""+fixData.charAt(2)+""+ Integer.toString((fixData.charAt(3)-47));
				System.out.println(fixed + "--15");
				dummy += (fixed + "\r\n" );
			}
			else if(fixData.charAt(3) > 48) {
				fixed += fixData.charAt(0)+""+fixData.charAt(1)+""+fixData.charAt(2)+""+ Integer.toString((fixData.charAt(3)-49));
				dummy += (fixed + "\r\n" );
			}else {
			dummy += (fixData + "\r\n" );	
			}
			while((line = br.readLine())!=null) {
				dummy += (line + "\r\n" ); 
			}
			FileWriter dx = new FileWriter(directory);
			dx.write(dummy);			
			dx.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	int return_schedule_number(String date/* yymmddtt 형식입니다.*/){			//일정의 갯수 반환\
		int position = 0;
		int month = 0;
		int year = 0;
		year = 10*(date.charAt(0)-48);
		year += date.charAt(1)-48;
		if(date.charAt(2) != 48) {
			month = 10*(date.charAt(2)-48);
		}
		month += date.charAt(3)-48;				//48은 아스키코드 0
		if(date.charAt(4) != 48) {
			position = 10*(date.charAt(4)-48);
		}
		position += (date.charAt(5)-48);
		String fixed = "";
		String directory = "./data/A/"+ Integer.toString(year) + "." + Integer.toString(month) + ".txt";
		File file = new File(directory);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = "";
			for(int i=1; i < position ; i++) {
			    line = br.readLine(); //읽으며 이동
			}
			line = br.readLine();
			br.close();
			return line.charAt(3)-48;
		} catch (Exception e) {
			e.printStackTrace();
		}
		int num = -1;	//error
		return num;
	}

	void write_schedule(String main, String date/* yymmddtt 형식입니다.*/, String start,
			String end, String party, String repeat,String time_start, String time_end, String memo){
		String dummy = "";
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
		String directory = "./data/B/"+ Integer.toString(year) + "." + Integer.toString(month) 
							+ "." + Integer.toString(day) + "."+ Integer.toString(time) + ".txt";
		File file = new File(directory);
		try {
			 file.createNewFile();//파일 생성
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dummy += (main + "\r\n" );
			dummy += (date + "\r\n" );
			dummy += (start + "\r\n" );
			dummy += (end + "\r\n" );
			dummy += (party + "\r\n" );
			dummy += (repeat + "\r\n" );
			dummy += (time_start + "\r\n" );
			dummy += (time_end + "\r\n" );
			dummy += memo;
			FileWriter dx = new FileWriter(directory);
			dx.write(dummy);
			dx.close();
			correction_schedule_number(date, 1);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	void delete_schedule(String date/* yymmddtt 형식입니다.*/) {
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
		String directory = "./data/B/"+Integer.toString(year) + "." + Integer.toString(month) 
							+ "." + Integer.toString(day)+ "."+ Integer.toString(time) + ".txt";
		File file = new File(directory);
		if( file.exists() ){
			System.gc();
			System.runFinalization();
			file.delete();
		}
		correction_schedule_number(date, -1);
	}
}
