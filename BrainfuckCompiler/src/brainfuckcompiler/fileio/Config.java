package brainfuckcompiler.fileio;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * @author wood
 * 
 * serves to load settings from config file, and to create
 * one if none present
 */
public class Config {
	
	/**Holds the associative array of settings*/
	public static Map<String, Object> settings = new HashMap<String, Object>();
	
	/**key for root brainfuck folder path**/
	public static String bfRoot = "root brainfuck folder path";
	
	/**key for gui text area font size**/
	public static String fontSize = "text area font size";
	
	/**Number slots in memory*/
	public static String memorySlots = "number indexes in memory (note indexes wrap,"+
										" doing < at 0 moves pointer to max index)";
	
	/**Whether ? is allowed in bf code (prints number at current index)*/
	public static String questionAllowed = "allow use of ? in brainfuck code"+
			" (outputs # at current slot) (true/false)";
	
	/**Whether traditional brainfuck limitations of # 0-255 should be followed*/
	public static String numLimit = "should brainfuck numbers be limited to"
			+ " traditional values 0-255 (note #s wrap, 0 - = 255) (true/false)";
	
	/**Whether program output should be displayed on temp txt file or not*/
	public static String txtOutput = "also display output in temp txt file (true/false)";
	
	/**Loads settings**/
	public static void loadConfig() {
		File file = new File("bfconfig.txt");
		if(!file.exists()) {
			PrintWriter pw = null;
			try{
				pw = new PrintWriter("bfconfig.txt");
				
				pw.println(bfRoot+": null");
				settings.put(bfRoot, "null");
				
				pw.println(fontSize+": 14");
				settings.put(fontSize, 14);
				
				pw.println(memorySlots+": 3200");
				settings.put(memorySlots, 3200);
				
				pw.println(questionAllowed+": false");
				settings.put(questionAllowed, false);
				
				pw.println(numLimit+": true");
				settings.put(numLimit, true);
				
				pw.println(txtOutput+": false");
				settings.put(txtOutput, true);
				
				pw.println("");
				pw.println("notes: ..\r"+
						"\nauthor: wood, github: https://github.com/woodrow73");
				
			}catch(IOException e) {
				e.printStackTrace();
			}finally {
				if(pw != null) {
					pw.close();
				}
			}
			JOptionPane.showMessageDialog(null, "A config file has"+
					" been created locally at "+
					new File("bfconfig.txt").getAbsolutePath()+
					"\nUse to change settings.");
		}
		else {
			Scanner in = null;
			try{
				in = new Scanner(file);
				String line = in.nextLine();
				settings.put(bfRoot,
						line.substring(line.indexOf(":")+1).trim());
				
				line = in.nextLine();
				settings.put(fontSize, Integer.parseInt(
								line.substring(line.indexOf(":")+1).trim()));
				
				line = in.nextLine();
				settings.put(memorySlots, Integer.parseInt(
						line.substring(line.indexOf(":")+1).trim()));
				
				line = in.nextLine();
				settings.put(questionAllowed, 
						line.substring(line.indexOf(":")+1).trim().
						toLowerCase().charAt(0) == 'f' ? false : true);
				
				line = in.nextLine();
				settings.put(numLimit,
						line.substring(line.indexOf(":")+1).trim().
						toLowerCase().charAt(0) == 'f' ? false : true);
				
				line = in.nextLine();
				settings.put(txtOutput,
						line.substring(line.indexOf(":")+1).trim().
						toLowerCase().charAt(0) == 'f' ? false : true);
						
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, "error reading config file. "+
						"\ndelete config and restart program to reset to defaults.");
				e.printStackTrace();
			}finally {
				if(in != null) {
					in.close();
				}
			}
		}
	}
	
}
