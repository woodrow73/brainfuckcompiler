package brainfuckcompiler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import brainfuckcompiler.compile.Compile;
import brainfuckcompiler.fileio.Config;
import brainfuckcompiler.fileio.LoadFile;
import brainfuckcompiler.fileio.SaveFile;

/**Creates an interface for loading/saving/running brainfuck files*/
public class GUI {
	
	public JFrame jframe;
	public JPanel jpanel;
	public JTextArea input;
	public JTextArea output;
	public JButton compile;
	public JButton saveFile;
	public JButton loadFile;
	public JButton settings;
	public JButton config;
	public JButton reloadConfig;
	
	/**Initializes and opens the gui*/
	public GUI() {
		jframe = new JFrame();
		jframe.setSize(1140, 500);
		jframe.setTitle("Brainfuck Compiler");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setLocationRelativeTo(null);
		jframe.setLayout(new BorderLayout());
		
		jpanel = new JPanel();
		jpanel.setLayout(new BorderLayout());
		
		input = new JTextArea();
		input.setBorder(BorderFactory.createTitledBorder(
				new TitledBorder("Program Input")));
		input.setBackground(Color.GRAY.brighter());
		input.setForeground(Color.BLUE);
		input.setFont(new Font("Monospaced", Font.PLAIN, 
				(Integer)Config.settings.get(Config.fontSize)));
		input.setLineWrap(true);
		JScrollPane inputScroll = new JScrollPane(input);
		jpanel.add(inputScroll, BorderLayout.CENTER);
		
		JPanel outputPanel = new JPanel();
		outputPanel.setPreferredSize(new Dimension(300, 250));
		outputPanel.setLayout(new BorderLayout());
		output = new JTextArea();
		output.setBackground(Color.BLACK);
		output.setForeground(Color.GREEN);
		output.setBorder(BorderFactory.createTitledBorder(
				new TitledBorder(null, "Program Output", 0, 0, null, Color.GRAY.brighter())));
		//output.setPreferredSize(new Dimension(300, 250)); <- prevents scroll panel
		output.setFont(new Font("Monospaced", Font.PLAIN, 
				(Integer)Config.settings.get(Config.fontSize)));
		output.setLineWrap(true);
		JScrollPane outputScroll = new JScrollPane(output);
		outputPanel.add(outputScroll, BorderLayout.CENTER);
		jpanel.add(outputPanel, BorderLayout.WEST);
		
		loadFile = new JButton("Load File");
		loadFile.setBackground(Color.CYAN.darker());
		loadFile.setForeground(Color.BLACK);
		loadFile.addActionListener(new LoadFile(input));
		jpanel.add(loadFile, BorderLayout.EAST);
		
		saveFile = new JButton("Save Brainfuck File");
		saveFile.addActionListener(new SaveFile(input));
		saveFile.setBackground(Color.CYAN);
		jpanel.add(saveFile, BorderLayout.NORTH);
		
		compile = new JButton("Run Program");
		compile.addActionListener(new CompileButton());
		compile.setBackground(Color.DARK_GRAY);
		compile.setForeground(Color.ORANGE);
		compile.setPreferredSize(new Dimension(100, 50));
		jpanel.add(compile, BorderLayout.SOUTH);
		
		jframe.add(jpanel, BorderLayout.CENTER);
		
		config = new JButton("Open Config");
		config.setBackground(Color.BLACK);
		config.setForeground(Color.GREEN);
		config.addActionListener(new OpenConfig());
		
		reloadConfig = new JButton("Reload Config");
		
		jframe.add(config, BorderLayout.SOUTH);
		jframe.setVisible(true);
	}
	
	/**Begins to compile and run code from the input text area**/
	private class CompileButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Config.loadConfig();
			String bfCode = "";
			String rawInputData = input.getText();
			char[] validBFChars = (boolean)Config.settings.get(Config.questionAllowed) 
					? new char[]{ '<','>','.',',','+','-','[',']','?' } :
						new char[]{ '<','>','.',',','+','-','[',']' };
			for(int i = 0; i < rawInputData.length(); i++) {
				boolean isValidChar = false;
				for(int j = 0; j < validBFChars.length; j++) {
					if(rawInputData.charAt(i) == validBFChars[j]) {
						isValidChar = true;
						break;
					}
				}
				if(isValidChar) {
					bfCode += String.valueOf(rawInputData.charAt(i));
				}
			}
			//compile
			Compile compile = new Compile(bfCode);
			compile.compile(output);
		}
	}
	
	/**opens config file*/
	private class OpenConfig implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Config.loadConfig();
			File configFile = new File("bfconfig.txt");
			if(!configFile.exists()) {
				JOptionPane.showMessageDialog(null, "Config file cannot be found");
				return;
			}
			Desktop d = Desktop.getDesktop();
			try {
				d.open(configFile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
