import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

import javax.swing.*;
import javax.swing.border.*;

public class Music extends JMenu implements AutoCloseable{
	private final String[] songs = {
			"INXS - New Sensation.wav",
			"Afterjourney & Boom - The Luxury Life.wav", 
			"Lil Tjay - F.N.wav",
			"Joey Bada$$ - The Light.wav",
			"Dizzee Rascal, Chip - L.L.L.L. (Love Life Live Large).wav",
			"2Pac feat. Talent - Changes.wav"
	};
	private final String[] icons = {
			"PlayIcon.png",
			"NextIcon.png",
			"RepeatIcon.png",
			"StopIcon.png",
			"PlaylistIcon.png",
			"AddIcon.png",
			"ClearIcon.png",
			"VolumeIcon.png"
	};
	private final String  IPath= System.getProperty("user.dir") + "\\src\\Icons\\";
	private final String SPath = System.getProperty("user.dir") + "\\src\\Sound\\";
	private JMenuItem item;
	private ArrayList<String> playlist = new ArrayList<>(Arrays.asList(songs));
	private AudioInputStream stream = null;
	private Clip clip = null;
	private FloatControl volumeControl = null;
	private boolean playing = false, released;
	private File song = new File(SPath + playlist.get(0));
	
	public Music() {
		super("Music");
		try {
			clip = AudioSystem.getClip();
			stream = AudioSystem.getAudioInputStream(song);
			clip.open(stream);
			volumeControl  = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			setVolume(35);
			released = true;
		} catch(IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
			System.err.println("Find exception: " + exc);
			released = false;
			close();
		}
		
		item = new JMenuItem("Play");
		item.setIcon(new ImageIcon(IPath + icons[0]));
		item.addActionListener(new Play());
		add(item);
		item = new JMenuItem("Next");
		item.setIcon(new ImageIcon(IPath + icons[1]));
		item.addActionListener(new Next());
		add(item);
		item = new JMenuItem("Repeat");
		item.setIcon(new ImageIcon(IPath + icons[2]));
		item.addActionListener(new Repeat());
		add(item);
		item = new JMenuItem("Stop");
		item.setIcon(new ImageIcon(IPath + icons[3]));
		item.addActionListener(new Stop());
		add(item);
		addSeparator();
		item = new JMenuItem("Show playlist");
		item.setIcon(new ImageIcon(IPath + icons[4]));
		item.addActionListener(new ShowPlaylist());
		add(item);
		item = new JMenuItem("Add to  playlist");
		item.setIcon(new ImageIcon(IPath + icons[5]));
		item.addActionListener(new AddToPlaylist());
		add(item);
		item = new JMenuItem("Clear playlist");
		item.setIcon(new ImageIcon(IPath + icons[6]));
		item.addActionListener(new ClearPlaylist());
		add(item);
		addSeparator();
		item = new JMenuItem("Change volume");
		item.setIcon(new ImageIcon(IPath + icons[7]));
		item.addActionListener(new VolumeChanger());
		add(item);	
	}
//-------------------------------Methods---------------------------------------	
	public void close() {
		if (clip != null)
			clip.close();
		if (stream != null)
			try {
				stream.close();
			} catch(IOException e) {e.printStackTrace();}
	}
	public void setVolume(int x) {
		float vol;
		vol = (float)x/(float)100;
		if (x < 0) vol = 0;
		if (x > 100) vol = 1;
		float min = volumeControl.getMinimum();
		float max = volumeControl.getMaximum();
		volumeControl.setValue((max - min) * vol + min);
	}
	public int getVolume() {
		float v = volumeControl.getValue();
		float min = volumeControl.getMinimum();
		float max = volumeControl.getMaximum();
		return (int)(((v-min)/(max-min)) * 100);
	}
//--------------------------------Listeners----------------------------------------------------------
	class Play implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (released && !playing) {
				clip.start();
				playing = true;
			}
			if (playlist.size() == 0)
				JOptionPane.showMessageDialog(Music.this, "Playlist is clear");
		}
	}
	class Next implements ActionListener{
		private int number = 1;
		public void actionPerformed(ActionEvent e) {
			if (playlist.size() == 0)
				JOptionPane.showMessageDialog(Music.this, "Playlist is clear");
			if ( number == playlist.size())
				number = 0;
			
			close();
			song = new File(SPath + playlist.get(number++));
			try {
				clip = AudioSystem.getClip();
				stream = AudioSystem.getAudioInputStream(song);
				clip.open(stream);
				volumeControl  = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
				released = true;
			} catch(IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
				System.err.println(exc);
				released = false;
				close();
			}
			if (released) {
				clip.setFramePosition(0);
				clip.start();
				playing = true;
				
			}
			
		}
	}
	class Repeat implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (released && playing) {
				clip.stop();
				clip.setFramePosition(0);
				clip.start();
				playing = true;
			}
		}
	}
	class Stop implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (playing) {
				clip.stop();
				playing = false;
			}
		}
	}
	class ShowPlaylist implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JDialog d = new JDialog();
			d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			d.setLocation(800, 400);
			d.setSize(400, playlist.size() * 50);
			d.setLayout(new FlowLayout());
			for (int i = 0; i < playlist.size(); i++) {
				JLabel l = new JLabel(playlist.get(i));
				l.setSize(400, 50);
				l.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 15));
				d.add(l);
			}
			d.setVisible(true);
		}
	}
	class ClearPlaylist implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			playing = false;
			released = false;
			close();
			playlist.clear();
		}
	}
	class AddToPlaylist implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			c.setDialogTitle("Choose co,position in .wav format");
			c.showOpenDialog(Music.this);
			File f = c.getSelectedFile();
			if (f!= null)
				if (f.getName().matches(".+.wav$")) {
					playlist.add(f.getName());
				}
				else
					JOptionPane.showMessageDialog(Music.this, "Not correct file");
		}
	}
	class VolumeChanger implements ActionListener{
		JDialog d;
		public VolumeChanger(){
				d = new JDialog();
				d.setSize(10, 270);
				d.setLocation(800, 400);
				//d.setLayout(new GridLayout(2,1));
				JSlider js = new javax.swing.JSlider(0, 100, getVolume());
				js.setMajorTickSpacing(25);
				js.setName("volume");
				js.setOrientation(VERTICAL);
				js.setPaintTicks(true);
				d.add(BorderLayout.NORTH,js);
				JButton b = new JButton("OK");
				b.addActionListener(e -> {
					setVolume(js.getValue());
					d.dispose();
				});
				d.add(BorderLayout.PAGE_END,b);
		}
		
		public void actionPerformed(ActionEvent e) {	
			d.setVisible(true);
		}
	}
}
