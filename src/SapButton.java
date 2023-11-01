import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class SapButton extends JPanel{
	private enum Cell{OPEN, CLOSED}
	private static Color color = Color.RED;
	Cell cell;
	private static boolean[] mines;
	private static int count = 0;
	private final int id = count++;
	private final 	int rows;
	
	public SapButton(int row) {
		addMouseListener(new ML());
		rows = row;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int
			x1 = 0, y1 = 0,
			x2 = getSize().width - 1,
			y2 = getSize().height - 1;
		String IPath = System.getProperty("user.dir") +  "\\src\\Icons\\";
		g.drawRect(x1, y1, x2, y2);
		if (cell == Cell.OPEN) {
			int x = getSize().width;
			int y = getSize().height;
			if (mines[id]) {
				try {
					g.drawImage(ImageIO.read(new File(IPath + "BackgroundIcon.jpg")),(x-15)/2, (y-15)/2, null);
					JOptionPane.showMessageDialog(this, "Good game!");
					//StartWindow.d.dispose();
				}catch(Exception e) { System.err.println(e);}
			} else {
				int k = 0;
				int t = rows*rows;
				if (id+1 < t && mines[id+1]) k++;
				if (id-1 >= 0 && mines[id-1]) k++;
				if (id+rows-1 < t && mines[id+rows-1]) k++;
				if (id+rows < t && mines[id+rows]) k++;
				if (id+rows+1 < t && mines[id+rows+1]) k++;
				if (id-rows-1 >= 0 && mines[id-rows-1]) k++;
				if (id-rows >= 0 && mines[id-rows]) k++;
				if (id-rows+1 >= 0 && mines[id -rows+1]) k++;
				
				g.setColor(color);
				if (k != 0) {
					g.setFont(new Font("Serif", Font.BOLD, 12));
					g.drawString(k+"", (x-5)/2, (y+10)/2);
				}
				else 
					try{
						g.drawImage(ImageIO.read(new File(IPath + "BackgroundIcon.jpg")),(x-15)/2, (y-15)/2, null);
					} catch(Exception e) {System.err.println(e);}
			}
		}
	}
	
	public static void setMines(boolean[] mine) {
		count = 0;
		mines = mine;
	}
	public static void setColor(Color c) {
		if (c != null)
			color = c;
	}
	
	class ML extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			cell = Cell.OPEN;
			repaint();
		}
	}
}
