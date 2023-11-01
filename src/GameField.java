	import javax.swing.*;
	import java.awt.*;
	import java.awt.event.*;
	import java.util.Random;

	public class GameField extends JDialog{
		private boolean[] mines;
		private JMenuBar menu;
		private Random rand = new Random(17);

		public GameField(int rows, Music music) {
			menu = new JMenuBar();
			JMenu m = new JMenu("File");
			JMenuItem mm = new JMenuItem("Restart");
			mm.addActionListener(new Restart(rows));
			m.add(mm);
			m.addSeparator();
			mm = new JMenuItem("Save");
			m.add(mm);
			mm = new JMenuItem("Open");
			m.add(mm);
			m.addSeparator();
			mm = new JMenuItem("Exit");
			mm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			m.add(mm);
			menu.add(m);
			m = new JMenu("Setting");
			mm = new JMenuItem("Color of number");
			mm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Color c = JColorChooser.showDialog(GameField.this, "Choose new color", Color.MAGENTA);
					SapButton.setColor(c);
					repaint();
				}
			});
			m.add(mm);
			m.addSeparator();
			m.add(music);
			menu.add(m);
			setJMenuBar(menu);
	//---------------------------------------------------------------------
			mines = new boolean[rows*rows];
			int n = rows*rows;
			int t = rand.nextInt(n);
			for (int i = 0; i < n/4; i++)
				if (!mines[t])
					mines[rand.nextInt(n)] = true;
				else i--;
			SapButton.setMines(mines);
			setTitle("Welcome to the sapper game!");
			setLayout(new GridLayout(rows, rows));
			for (int i = 0; i < rows * rows; i++)
				add(new SapButton(rows));
			setBounds(700, 400, rows * 20, rows * 20);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}

		class Restart implements ActionListener {
			int rows;
			public Restart(int rows) {
				this.rows = rows;
			}
			public void actionPerformed(ActionEvent e) {
				StartWindow.Start(rows);
			}
		}
	}

