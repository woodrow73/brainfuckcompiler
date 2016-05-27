package brainfuckcompiler.fileio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**loads a bf or txt file to input text area**/
public class LoadFile implements ActionListener {
	
	/**Text area data is loaded to**/
	public JTextArea input;
	
	public LoadFile(JTextArea input) {
		this.input = input;
	}
	
	public void actionPerformed(ActionEvent e) {
		Config.loadConfig();
		JFileChooser jfc = new JFileChooser();
		try{
			File bfDirectory = new File((String)Config.settings.get(Config.bfRoot));
			//handles non-existant directories
			jfc.setCurrentDirectory(bfDirectory);
		}catch(Exception e1) {
			e1.printStackTrace();
		}
		int userOption = jfc.showOpenDialog(null);
		if(userOption == JFileChooser.APPROVE_OPTION) {
			if(input.getText().length() > 0) {
				int overwriteDecision = JOptionPane.showConfirmDialog(null,
						"This action will overwrite the input text area.\n"+
								"Is this ok?");
				if(overwriteDecision == JOptionPane.YES_OPTION) {
					//continue to overwrite
				}
				else { //end method
					JOptionPane.showMessageDialog(null, "Loading cancelled");
					return;
				}
			}
			File chosen = jfc.getSelectedFile();
			BufferedReader br = null;
			FileReader fr = null;
			try{
				fr = new FileReader(chosen);
				br = new BufferedReader(fr);
				String data = "", line = "";
				
				while((line = br.readLine()) != null) {
					data += line+"\n";
				}
				input.setText(data);
			}catch(IOException e1) {
				e1.printStackTrace();
			}finally {
				if(br != null) {
					try {
						br.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if(fr != null) {
					try {
						fr.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}