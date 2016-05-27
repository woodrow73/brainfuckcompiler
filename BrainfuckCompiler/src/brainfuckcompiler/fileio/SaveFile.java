package brainfuckcompiler.fileio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**executed when saveFile button clicked
 * saves data from the input text area to desired location**/
public class SaveFile implements ActionListener {
	
	/**the program's input, the data that is saved**/
	public JTextArea input;
	
	public SaveFile(JTextArea input) {
		this.input = input;
	}
	
	public void actionPerformed(ActionEvent e) {
		Config.loadConfig();
		JFileChooser saveFile = new JFileChooser();
		try{
			File bfDirectory = new File((String)Config.settings.get(Config.bfRoot));
			//handles non-existant directories
			saveFile.setCurrentDirectory(bfDirectory);
		}catch(Exception e1) {
			e1.printStackTrace();
		}
		int userOption = saveFile.showSaveDialog(null);

		if(userOption == JFileChooser.APPROVE_OPTION) {
			File saveLocation = saveFile.getSelectedFile();
			if(saveLocation.exists()) {
				int overwriteDecision = JOptionPane.showConfirmDialog(null, 
						"File "+saveLocation.getAbsolutePath()+"\n"+
						"already exists. Overwrite this file?");
				if(overwriteDecision == JOptionPane.YES_OPTION) {
					//continue to make/overwrite file with printwriter
				}
				else { //end method
					JOptionPane.showMessageDialog(null, "save canceled");
					return;
				}
			}
			String dataToSave = input.getText();
			PrintWriter pw = null;
			try{
				pw = new PrintWriter(saveLocation);
				pw.print(dataToSave);
				JOptionPane.showMessageDialog(null, "File saved");
			}catch(IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "There was an error"+
						" while saving");
			}finally {
				if(pw != null) {
					pw.close();
				}
			}
		}
	}
}