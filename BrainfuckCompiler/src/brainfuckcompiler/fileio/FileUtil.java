package brainfuckcompiler.fileio;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtil {
	
	/**
	 * creates and opens a temporary file
	 * @param data string to place in the temp file
	 */
	public static void tempFile(String data) {
		PrintWriter pw = null;
		String fileName = "temp429857234.txt";
		try{
			pw = new PrintWriter(fileName);
			pw.println(data);
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(pw != null) {
				pw.close();
			}
		}
		
		Desktop d = Desktop.getDesktop();
		try{
			d.open(new File(fileName));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
