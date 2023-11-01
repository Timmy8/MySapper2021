import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class StartWindow extends JFrame{
	public static GameField d = null;
	private static Music music = new Music();
	private JTextField
	rows = new JTextField("15", 10);

	public StartWindow() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(200,120);
		setLocation(800,400);
		setTitle("My personal sapper");
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Size"));
		p.add(rows);
		add(BorderLayout.NORTH, p);
		JButton start = new JButton("Start!");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fieldSize = Integer.parseInt(rows.getText());
				if (fieldSize < 10){
					JOptionPane.showMessageDialog(StartWindow.super.rootPane, "Слишком маленький размер поля");
					return;
				} else if (fieldSize > 20){
					JOptionPane.showMessageDialog(StartWindow.super.rootPane, "Слишком большой	 размер поля");
					return;
				}
				//setVisible(false);
				Start(fieldSize);
			}
		});
		JButton records = new JButton("Records");
		setLayout(new FlowLayout());
		add(start);
		add(records);
		setVisible(true);
	}
	
	
	public static void main(String[] args) throws Exception{
		new StartWindow();
	}
	
	public static void Start(int rows) {
		if (d != null)
			d.dispose();
		d = new GameField(rows, music);
		d.setResizable(false);
		d.toFront();
		d.setVisible(true);
	}
	public static void Start(GameField gf) {
		if (d != null)
			d.dispose();
		d = gf;
		d.setResizable(false);
		d.toFront();
		d.setVisible(true);
	}
}
