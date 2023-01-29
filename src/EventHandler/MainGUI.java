package EventHandler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.*;

class CalendarDataManager{ // 6*7배열에 나타낼 달력 값을 구하는 class
	
	static final int CAL_WIDTH = 7;
	final static int CAL_HEIGHT = 6;
	int calDates[][] = new int[CAL_HEIGHT][CAL_WIDTH];
	int calYear;
	int calMonth;
	int calDayOfMon;
	int idate;
	int time;
	String weatherdic = "./image/";
	String date = "";
	final int calLastDateOfMonth[]={31,28,31,30,31,30,31,31,30,31,30,31};
	int calLastDate;
	Calendar today = Calendar.getInstance();
	Calendar cal;
	writefile Writefile = new writefile();
	readfile Readfile = new readfile();
	weather Weather = new weather(); 
	
	public CalendarDataManager(){ 
		setToday(); 
	}
	public void setToday(){
		calYear = today.get(Calendar.YEAR); 
		calMonth = today.get(Calendar.MONTH);
		calDayOfMon = today.get(Calendar.DAY_OF_MONTH);
		makeCalData(today);
	}
	private void makeCalData(Calendar cal){
		// 1일의 위치와 마지막 날짜를 구함 
		int calStartingPos = (cal.get(Calendar.DAY_OF_WEEK)+7-(cal.get(Calendar.DAY_OF_MONTH))%7)%7;
		if(calMonth == 1) calLastDate = calLastDateOfMonth[calMonth] + leapCheck(calYear);
		else calLastDate = calLastDateOfMonth[calMonth];
		// 달력 배열 초기화
		for(int i = 0 ; i<CAL_HEIGHT ; i++){
			for(int j = 0 ; j<CAL_WIDTH ; j++){
				calDates[i][j] = 0;
			}
		}
		// 달력 배열에 값 채워넣기
		for(int i = 0, num = 1, k = 0 ; i<CAL_HEIGHT ; i++){
			if(i == 0) k = calStartingPos;
			else k = 0;
			for(int j = k ; j<CAL_WIDTH ; j++){
				if(num <= calLastDate) calDates[i][j]=num++;
			}
		}
	}
	private int leapCheck(int year){ //윤년인지 확인하는 함수
		if(year%4 == 0 && year%100 != 0 || year%400 == 0) return 1;
		else return 0;
	}
	public void moveMonth(int mon){ // 현재달로 부터 n달 전후를 받아 달력 배열을 만드는 함수(1년은 +12, -12달로 이동 가능)
		calMonth += mon;
		if(calMonth>11) while(calMonth>11){
			calYear++;
			calMonth -= 12;
		} else if (calMonth<0) while(calMonth<0){
			calYear--;
			calMonth += 12;
		}
		cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);
		makeCalData(cal);
	}
}

public class MainGUI extends CalendarDataManager{ 
	// 창 구성요소와 배치도
	JFrame mainFrame;
	
	JPanel calOpPanel;
		JButton todayBut;
		JLabel todayLab;
		JButton lYearBut;
		JButton lMonBut;
		JLabel curMMYYYYLab;
		JButton nMonBut;
		JButton nYearBut;
		JButton plusMemo;
		ListenForCalOpButtons lForCalOpButtons = new ListenForCalOpButtons();
		PlusMemoButton plusMemobtn = new PlusMemoButton();
	
	JPanel calPanel;
		JButton weekDaysName[];
		JButton dateButs[][] = new JButton[6][7];
		listenForDateButs lForDateButs = new listenForDateButs(); 
		
	ImageIcon cloudy = new ImageIcon("image/cloudy.png");
	ImageIcon little_cloudy = new ImageIcon("image/little_cloudy.png");
	ImageIcon rainnsnow = new ImageIcon("image/rainnsnow.png");
	ImageIcon rainy = new ImageIcon("image/rainy.png");
	ImageIcon snow = new ImageIcon("image/snow.png");
	ImageIcon sunny = new ImageIcon("image/sunny.png");
	
	String fontColor = "";
	String copydate = "";
	String memoAmount = new String();
	final String WEEK_DAY_NAME[] = { "일", "월", "화", "수", "목", "금", "토" };
	final String title = "Road Manager";
	
	public MainGUI(){
		mainFrame = new JFrame(title);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(840,700);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		
		calOpPanel = new JPanel();
			todayBut = new JButton("Today");
			todayBut.setToolTipText("오늘의 일정 보기");
			todayBut.setPreferredSize(new Dimension(200,50));
			todayBut.addActionListener(lForCalOpButtons);
			todayLab = new JLabel(today.get(Calendar.MONTH)+1+"/"+today.get(Calendar.DAY_OF_MONTH)+"/"+today.get(Calendar.YEAR));
			todayLab.setFont(todayLab.getFont().deriveFont(30f));
			lYearBut = new JButton("<<");
			lYearBut.setToolTipText("Previous Year");
			lYearBut.addActionListener(lForCalOpButtons);
			lMonBut = new JButton("<");
			lMonBut.setToolTipText("Previous Month");
			lMonBut.addActionListener(lForCalOpButtons);
			curMMYYYYLab = new JLabel("<html><table width=100><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+" / "+calYear+"</th></tr></table></html>");
			nMonBut = new JButton(">");
			nMonBut.setToolTipText("Next Month");
			nMonBut.addActionListener(lForCalOpButtons);
			nYearBut = new JButton(">>");
			nYearBut.setToolTipText("Next Year");
			nYearBut.addActionListener(lForCalOpButtons);
			calOpPanel.setLayout(new GridBagLayout());
			plusMemo=new JButton("일정 추가");
			plusMemo.setPreferredSize(new Dimension(200,50));
			plusMemo.addActionListener(plusMemobtn);
			GridBagConstraints calOpGC = new GridBagConstraints();
			calOpGC.gridx = 1;
			calOpGC.gridy = 1;
			calOpGC.gridwidth = 2;
			calOpGC.gridheight = 1;
			calOpGC.weightx = 1;
			calOpGC.weighty = 1;
			calOpGC.insets = new Insets(10,10,0,0);
			calOpGC.anchor = GridBagConstraints.WEST;
			calOpGC.fill = GridBagConstraints.NONE;
			calOpPanel.add(todayBut,calOpGC);
			calOpGC.gridwidth = 3;
			calOpGC.gridx = 3;
			calOpGC.gridy = 1;
			calOpGC.anchor = GridBagConstraints.CENTER;
			calOpPanel.add(todayLab,calOpGC);
			calOpGC.gridwidth = 3;
			calOpGC.gridx = 6;
			calOpGC.gridy = 1;
			calOpGC.anchor = GridBagConstraints.EAST;
			calOpPanel.add(plusMemo,calOpGC);
			calOpGC.anchor = GridBagConstraints.CENTER;
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 1;
			calOpGC.gridy = 2;
			calOpPanel.add(lYearBut,calOpGC);
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 2;
			calOpGC.gridy = 2;
			calOpPanel.add(lMonBut,calOpGC);
			calOpGC.gridwidth = 2;
			calOpGC.gridx = 3;
			calOpGC.gridy = 2;
			calOpPanel.add(curMMYYYYLab,calOpGC);
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 5;
			calOpGC.gridy = 2;
			calOpPanel.add(nMonBut,calOpGC);
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 6;
			calOpGC.gridy = 2;
			calOpPanel.add(nYearBut,calOpGC);
		
		calPanel=new JPanel();
			weekDaysName = new JButton[7];
			for(int i=0 ; i<CAL_WIDTH ; i++){
				weekDaysName[i]=new JButton(WEEK_DAY_NAME[i]);
				weekDaysName[i].setBorderPainted(false);
				weekDaysName[i].setContentAreaFilled(false);
				weekDaysName[i].setForeground(Color.WHITE);
				if(i == 0) weekDaysName[i].setBackground(new Color(200, 50, 50));
				else if (i == 6) weekDaysName[i].setBackground(new Color(50, 100, 200));
				else weekDaysName[i].setBackground(new Color(150, 150, 150));
				weekDaysName[i].setOpaque(true);
				weekDaysName[i].setFocusPainted(false);
				calPanel.add(weekDaysName[i]);
			}
			for(int i=0 ; i<CAL_HEIGHT ; i++){
				for(int j=0 ; j<CAL_WIDTH ; j++){
					dateButs[i][j]=new JButton();
					dateButs[i][j].setContentAreaFilled(false);
					dateButs[i][j].setBackground(Color.WHITE);
					dateButs[i][j].setOpaque(true);
					dateButs[i][j].addActionListener(lForDateButs);
					calPanel.add(dateButs[i][j]);
				}
			}
			calPanel.setLayout(new GridLayout(0,7,2,2));
			calPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			showCal();

		//calOpPanel, calPanel을  frameSubPanelWest에 배치
		JPanel frameSubPanelWest = new JPanel();
		Dimension calOpPanelSize = calOpPanel.getPreferredSize();
		calOpPanelSize.height = 180;
		calOpPanel.setPreferredSize(calOpPanelSize);
		frameSubPanelWest.setLayout(new BorderLayout());
		frameSubPanelWest.add(calOpPanel,BorderLayout.NORTH);
		frameSubPanelWest.add(calPanel,BorderLayout.CENTER);

		Dimension frameSubPanelWestSize = frameSubPanelWest.getPreferredSize();
		frameSubPanelWestSize.width = 820;
		frameSubPanelWest.setPreferredSize(frameSubPanelWestSize);
		
		//frame에 전부 배치
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(frameSubPanelWest, BorderLayout.WEST);
		mainFrame.setVisible(true);

		focusToday(); 
	}
	
	private void focusToday(){
			dateButs[today.get(Calendar.WEEK_OF_MONTH)-1][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
	}
	
	private void showCal(){
		for(int i=0;i<CAL_HEIGHT;i++){
			for(int j=0;j<CAL_WIDTH;j++){
				fontColor="black";
				if(j==0) fontColor="red";
				else if(j==6) fontColor="blue";
				
				idate = 1000000*(calYear - 2000) + 10000*(calMonth + 1) + 100*calDates[i][j]+0;
				date =  Integer.toString(idate);
				memoAmount = "(" + Writefile.return_schedule_number(date/* yymmddtt 형식입니다.*/)+ ")";
				File f =new File("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDates[i][j]<10?"0":"")+calDates[i][j]+".txt");
				dateButs[i][j].setText("<html><font color="+fontColor+">"+calDates[i][j]+"</b></font><br><font size=1>"+memoAmount+"</font></html>");

				JLabel todayMark = new JLabel("<html><font color=green>*</html>");
				dateButs[i][j].removeAll();
				if(calMonth == today.get(Calendar.MONTH) &&
						calYear == today.get(Calendar.YEAR) &&
						calDates[i][j] == today.get(Calendar.DAY_OF_MONTH)){
					dateButs[i][j].add(todayMark);
					dateButs[i][j].setToolTipText("Today");
					switch(weather.getWeather()) {
				    case "littlecloudy": dateButs[i][j].setIcon(little_cloudy);	
				         				 break;
				    case "rainnsnow":	 dateButs[i][j].setIcon(rainnsnow);
				         				 break;
				    case "rainy":		 dateButs[i][j].setIcon(rainy);
			         					 break;
				    case "snow": 		 dateButs[i][j].setIcon(snow);
				    					 break;
				    case "sunny": 		 dateButs[i][j].setIcon(sunny);
			         					 break;
				    default: 
				         				 break;
					}
				}
				
				if(calDates[i][j] == 0) dateButs[i][j].setVisible(false);
				else dateButs[i][j].setVisible(true);
			}
		}
	}
	
	private class PlusMemoButton implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			EventsKit kit = new EventsKit();
			kit.addEvent();
		}
	}
	
	private class ListenForCalOpButtons implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			for(int i=0 ; i<CAL_HEIGHT ; i++){//오늘 날짜에만 아이콘이 뜨도록 함 
				for(int j=0 ; j<CAL_WIDTH ; j++){
					dateButs[i][j].setIcon(null);		
				}
			}
			if(e.getSource() == todayBut){
				setToday();
				lForDateButs.actionPerformed(e);
				focusToday();
			}
			else if(e.getSource() == lYearBut) moveMonth(-12);
			else if(e.getSource() == lMonBut) moveMonth(-1);
			else if(e.getSource() == nMonBut) moveMonth(1);
			else if(e.getSource() == nYearBut) moveMonth(12);
			
			curMMYYYYLab.setText("<html><table width=100><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+" / "+calYear+"</th></tr></table></html>");
			showCal();
		}
	}
	
	private class listenForDateButs implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int k=0,l=0;
			for(int i=0 ; i<CAL_HEIGHT ; i++){
				for(int j=0 ; j<CAL_WIDTH ; j++){
					if(e.getSource() == dateButs[i][j]){ 
						k=i;
						l=j;
					}
				}
			}
			
			if(e.getSource() == todayBut) {
				k = today.get(Calendar.WEEK_OF_MONTH)-1;
				l = today.get(Calendar.DAY_OF_WEEK)-1;
			}
			
			if(!(k ==0 && l == 0)) calDayOfMon = calDates[k][l]; 
			cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);	
			
			idate = 1000000*(calYear - 2000) + 10000*(calMonth + 1) + 100*calDates[k][l]+0;
			date =  Integer.toString(idate);
			System.out.println(date);
			time = Readfile.find_another(date, 0);
			System.out.println(time + "--2");
			time = 100*Integer.parseInt(date.substring(0,6)) + time;
			copydate = Integer.toString(time);
			System.out.println(copydate + "--3");
			
			if(Writefile.return_schedule_number(copydate) > 0) {
				EventsKit kit = new EventsKit();
				kit.viewEvent(copydate, 1);
			}
		}
	}
}