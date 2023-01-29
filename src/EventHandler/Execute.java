package EventHandler;

import javax.swing.SwingUtilities;

public class Execute {
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new MainGUI();
			}
		});
	}
}
