package brainfuckcompiler.main;

import brainfuckcompiler.fileio.Config;
import brainfuckcompiler.gui.GUI;

/**
 * 
 * @author wood
 * 
 * Provides a GUI to edit, compile, load, and save brainfuck files
 * 
 */
public class BrainfuckCompiler {
	
	public BrainfuckCompiler() {
		
		Config.loadConfig();
		new GUI();
		
	}
	
	public static void main(String[] args) {
		new BrainfuckCompiler();
	}
}