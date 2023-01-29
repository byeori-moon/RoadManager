package EventHandler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;


public class EventsKit implements ActionListener, MouseListener {
	
	public int Atime = 0;
	public int time = 0;
	private JFrame f = new JFrame();
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	private JPanel p = new JPanel(gb);
	private JScrollPane sp = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private MapsKit mapsKit = new MapsKit();
	
	private JLabel titleLabel, startLabel, endLabel, repeatLabel, srcPlaceLabel, dstPlaceLabel, inviteeLabel, memoLabel, dateLabel;
	private JLabel titleInfo, startInfo, endInfo, srcPlaceInfo, dstPlaceInfo, inviteeInfo, memoInfo, src, dst, mapImage, ago, next;
	private JTextField title, srcPlace, dstPlace, invitee;
	private JTextArea memo;
	private JButton directions, submit, cancel, modify, delet;
	private JComboBox startAmpm, startHr, startMin, endAmpm, endHr, endMin, month, day, repeatOption;
	
	private String titleStr="", memoStr="", inviteeStr="", scrPlaceStr="", dstPlaceStr="", startAmpmStr="", startHrStr="", startMinStr="", 
				   endAmpmStr="", endHrStr="", endMinStr="", repeatOptionStr="", startTime="", endTime="", monthStr="", dayStr="";
	private String repOption[] = {"안 함","매일", "매주", "매월", "매년"};
	private String ampm[] = {"오전", "오후"};
	private String hr[] = new String[12], min[] = new String[61], m[] = new String[12], d[] = new String[31];
	
	public String maindate = ""; 
	
	writefile Writefile = new writefile();
	readfile Readfile = new readfile();
	
	public EventsKit() {
		for(int i = 0; i < 12; i++) hr[i] = Integer.toString(i + 1);
		for(int i = 0; i < 61; i++) min[i] = Integer.toString(i);
		for(int i = 0; i < 12; i++) m[i] = Integer.toString(i + 1);
		for(int i = 0; i < 31; i++) d[i] = Integer.toString(i + 1);
		
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setPreferredSize(new Dimension(480, 600));
		f.setVisible(true);
		f.setResizable(false);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		sp.getVerticalScrollBar().setUnitIncrement(10);
	}
	
	public void addEvent() {
		p.removeAll();
		f.setTitle("이벤트 추가/수정");
		
		// Title
		titleLabel = new JLabel("제목");
		title = new JTextField(titleStr);
		setPos(titleLabel, 0, 0, 1, 1);
		setPos(title, 1, 0, 2, 1);
		
		//월-일
		dateLabel = new JLabel("월/일");
		JPanel mdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		month = new JComboBox(m);
		day = new JComboBox(d);
		mdPanel.add(new JLabel("월 "));
		mdPanel.add(month);
		mdPanel.add(new JLabel("일 "));
		mdPanel.add(day);
		setPos(dateLabel, 0, 1, 1, 1);
		setPos(mdPanel, 1, 1, 1, 1);
		// Begin
		startLabel = new JLabel("시작 시간");
		JPanel sPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		startAmpm = new JComboBox(ampm);
		startHr = new JComboBox(hr);
		startMin = new JComboBox(min);
		sPanel.add(startAmpm);
		sPanel.add(new JLabel(" "));
		sPanel.add(startHr);
		sPanel.add(new JLabel("시 "));
		sPanel.add(startMin);
		sPanel.add(new JLabel("분"));
		setPos(startLabel, 0, 2, 1, 1);
		setPos(sPanel, 1, 2, 2, 1);
		
		// End
		endLabel = new JLabel("종료 시간");
		JPanel ePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		endAmpm = new JComboBox(ampm);
		endHr = new JComboBox(hr);
		endMin = new JComboBox(min);
		ePanel.add(endAmpm);
		ePanel.add(new JLabel(" "));
		ePanel.add(endHr);
		ePanel.add(new JLabel("시 "));
		ePanel.add(endMin);
		ePanel.add(new JLabel("분"));
		setPos(endLabel, 0, 3, 1, 1);
		setPos(ePanel, 1, 3, 2, 1);
		
		// Repeat
		repeatLabel = new JLabel("반복");
		repeatOption = new JComboBox(repOption);
		setPos(repeatLabel, 0, 4, 1, 1);
		setPos(repeatOption, 1, 4, 2, 1);
		
		// Place
		srcPlaceLabel = new JLabel("출발지");
		srcPlace = new JTextField(scrPlaceStr);
		setPos(srcPlaceLabel, 0, 5, 1, 1);
		setPos(srcPlace, 1, 5, 2, 1);
		
		dstPlaceLabel = new JLabel("목적지");
		dstPlace = new JTextField(dstPlaceStr);
		setPos(dstPlaceLabel, 0, 6, 1, 1);
		setPos(dstPlace, 1, 6, 2, 1);
		
		// Invitees
		inviteeLabel = new JLabel("일행");
		invitee = new JTextField(inviteeStr);
		setPos(inviteeLabel, 0, 7, 1, 1);
		setPos(invitee, 1, 7, 2, 1);
		
		// Memo
		memoLabel = new JLabel("메모");
		memo = new JTextArea(10, 20);
		memo.setLineWrap(true);
		setPos(memoLabel, 0, 8, 1, 1);
		setPos(memo, 1, 8, 2, 5);
		
		
		// Submit / Cancel
		JPanel scPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		submit = new JButton("추가");
		cancel = new JButton("취소");
		submit.addActionListener(this);
		cancel.addActionListener(this);
		scPanel.add(submit);
		scPanel.add(cancel);
		setPos(scPanel, 2, 15, 1, 1);
		
		f.add(sp);
		f.pack();
	}
	
	public void viewEvent(String date, int num) {
		maindate = date;
		p.removeAll();
		f.setTitle("이벤트 세부사항");
		
		// Title
		titleLabel = new JLabel("제목");
		titleInfo = new JLabel(Readfile.return_schedule(date, 1));
		setPos(titleLabel, 0, 1, 1, 1);
		setPos(titleInfo, 1, 1, 1, 1);
				
		// Begin
		startLabel = new JLabel("시작 시간");
		startInfo = new JLabel(Readfile.return_schedule(date, 7));
		setPos(startLabel, 0, 2, 1, 1);
		setPos(startInfo, 1, 2, 1, 1);
		
		// End
		endLabel = new JLabel("종료 시간");
		endInfo = new JLabel(Readfile.return_schedule(date, 8));
		setPos(endLabel, 0, 3, 1, 1);
		setPos(endInfo, 1, 3, 1, 1);
				
		// Invitees
		inviteeLabel = new JLabel("일행");
		inviteeInfo = new JLabel(Readfile.return_schedule(date, 5));
		setPos(inviteeLabel, 0, 4, 1, 1);
		setPos(inviteeInfo, 1, 4, 1, 1);
		
		// Map
		src = new JLabel("출발지");
		srcPlaceInfo = new JLabel(Readfile.return_schedule(date, 3));
		if(srcPlaceInfo.getText().isBlank()) srcPlaceInfo.setText("중앙대학교");
		dst = new JLabel("목적지");
		dstPlaceInfo = new JLabel(Readfile.return_schedule(date, 4));
		if(dstPlaceInfo.getText().isBlank()) dstPlaceInfo.setText("상도");
		
		mapImage = mapsKit.viewPlace(Readfile.return_schedule(date, 4));
		mapImage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		setPos(src, 0, 5, 1, 1);
		setPos(srcPlaceInfo, 1, 5, 1, 1);
		setPos(dst, 0, 6, 1, 1);
		setPos(dstPlaceInfo, 1, 6, 1, 1);
		setPos(mapImage, 0, 7, 3, 1);
		
		JPanel scPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		modify = new JButton("수정");
		delet = new JButton("삭제");
		directions = new JButton("경로 보기");
		directions.addActionListener(this);
		modify.addActionListener(this);
		delet.addActionListener(this);
		scPanel.add(modify);
		scPanel.add(delet);
		scPanel.add(directions);
		setPos(scPanel, 2, 8, 1, 1);
		
		// Memo 
		memoLabel = new JLabel("메모");
		memoInfo = new JLabel(Readfile.return_schedule(date, 9));
		setPos(memoLabel, 0, 9, 1, 1);
		setPos(memoInfo, 1, 9, 1, 2);
		
		//추가한 부분
		JPanel movePanel = new JPanel(new BorderLayout());
		JPanel temp = new JPanel(new BorderLayout());
		movePanel.add(temp, BorderLayout.NORTH);
		ago = new JLabel("<html><font size = 5>&lt 이전</font></html>");
		next = new JLabel("<html><font size = 5>다음 &gt</font></html>");
		ago.addMouseListener(this);
		next.addMouseListener(this);
		temp.add(ago, BorderLayout.WEST);
		temp.add(next, BorderLayout.EAST);
		setPos(movePanel, 0, 0, 4, 1);
		
		f.add(sp);
		f.pack();
	}
	
	public void setPos(JComponent obj, int x, int y, int w, int h) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = w;
		c.gridheight = h;
		c.insets = new Insets(10, 10, 10, 10);
		gb.setConstraints(obj, c);
		p.add(obj);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == directions) {
			mapsKit.viewDirections(srcPlaceInfo.getText(), dstPlaceInfo.getText());
		}
		if(e.getSource() == submit) {
			titleStr = title.getText();
			inviteeStr = invitee.getText();
			memoStr = memo.getText();
			scrPlaceStr = srcPlace.getText();
			dstPlaceStr = dstPlace.getText();
			startAmpmStr = startAmpm.getItemAt(startAmpm.getSelectedIndex()).toString();
			startHrStr = startHr.getItemAt(startHr.getSelectedIndex()).toString();
			startMinStr = startMin.getItemAt(startMin.getSelectedIndex()).toString();
			endAmpmStr = endAmpm.getItemAt(endAmpm.getSelectedIndex()).toString();
			endHrStr = endHr.getItemAt(endHr.getSelectedIndex()).toString();
			endMinStr = endMin.getItemAt(endMin.getSelectedIndex()).toString();
			repeatOptionStr = repeatOption.getItemAt(repeatOption.getSelectedIndex()).toString();
			
			monthStr = month.getItemAt(month.getSelectedIndex()).toString();
			dayStr = day.getItemAt(day.getSelectedIndex()).toString();
			if(monthStr.length()==1) monthStr = "0"+monthStr;
			if(dayStr.length()==1) dayStr = "0"+dayStr;
			if(startHrStr.length()==1) startHrStr = "0"+startHrStr;
			maindate = "22"+monthStr+dayStr+startHrStr;
			System.out.println(maindate+"--7");
			
			startTime = startAmpmStr+" "+startHrStr+"시 "+startMinStr+"분";
			endTime = endAmpmStr+" "+endHrStr+"시 "+endMinStr+"분";
			
			Writefile.write_schedule(titleStr, maindate, scrPlaceStr, dstPlaceStr, inviteeStr, repeatOptionStr, startTime, endTime, memoStr);
			f.dispose();
		}
		if(e.getSource() == modify) {
			addEvent();
		}
		if(e.getSource() == delet) {
			Writefile.delete_schedule(maindate);
			f.dispose();
		}	
		if(e.getSource() == cancel) {
			f.dispose();
		}
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == ago) {
			System.out.println(maindate + "--8");
			Atime = Readfile.find_another(maindate, -1);
			time = 100*Integer.parseInt(maindate.substring(0,6)) + Atime;
			maindate = Integer.toString(time);
			viewEvent(maindate,Atime);
		}
		if(e.getSource() == next) {
			System.out.println(maindate + "--8");
			Atime = Readfile.find_another(maindate, 1);
			time = 100*Integer.parseInt(maindate.substring(0,6)) + Atime;
			maindate = Integer.toString(time);
			viewEvent(maindate,Atime);
		}
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
}