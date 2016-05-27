package brainfuckcompiler.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import brainfuckcompiler.fileio.Config;
import brainfuckcompiler.fileio.FileUtil;

/**Stores the brainfuck program, and executes it*/
public class Compile {
	
	/**Brainfuck program*/
	public String code = "";
	
	/**Brainfuck memory*/
	public ArrayList<Integer> memory;
	
	/**Brainfuck memory slot count*/
	public int memorySlots;
	
	/**whether brainfuck 0-255 limit enforced*/
	public boolean numLimit;
	
	public Compile(String code) {
		this.code = code;
		memorySlots = (Integer)Config.settings.get(Config.memorySlots);
		memory = new ArrayList<Integer>(memorySlots);
		for(int i = 0; i < memorySlots; i++) {
			memory.add(0);
		}
	}
	
	/**Runs program*/
	public void compile(JTextArea outputArea) {
		outputArea.setText("");
		
		//Pairs up indexes of which brackets go together
		Map<Integer, Integer> startToEndBrackets = new HashMap<Integer, Integer>();
		Map<Integer, Integer> endToStartBrackets = new HashMap<Integer, Integer>();
		
		//stack of left brackets, right brackets match the top of the stack
		ArrayList<Integer> leftBracketStack = new ArrayList<Integer>();
		for(int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);
			if(c != '[' && c != ']') {
				continue;
			}
			if(c == '[') {
				leftBracketStack.add(i);
			}
			else if(c == ']') {
				if(leftBracketStack.size() == 0) {
					//TODO throw error, extra ] without matching [
				}
				startToEndBrackets.put(leftBracketStack.get(leftBracketStack.size()-1), i);
				endToStartBrackets.put(i, leftBracketStack.get(leftBracketStack.size()-1));
				leftBracketStack.remove(leftBracketStack.size()-1);
			}
		}
		
		String outputStr = "";
		int pointer = 0;
		for(int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);
			switch(c) {
			case '<':
				pointer = (pointer-1) == -1 ? memorySlots-1 : pointer-1;
				break;
			case '>':
				pointer = (pointer+1) == memorySlots ? 0 : pointer+1;
				break;
			case '+':
				if(numLimit) {
					memory.set(pointer, memory.get(pointer)+1 == 256 ? 0 :
							memory.get(pointer)+1);
				}
				else {
					memory.set(pointer, memory.get(pointer)+1);
				}
				break;
			case '-':
				if(numLimit) {
					memory.set(pointer, memory.get(pointer)-1 == -1 ? 255 :
							memory.get(pointer)-1);
				}
				else {
					memory.set(pointer, memory.get(pointer)-1);
				}
				break;
			case '[':
				i = memory.get(pointer) == 0 ? startToEndBrackets.get(i) : i;
				break;
			case ']':
				i = memory.get(pointer) == 0 ? i : endToStartBrackets.get(i);
				break;
			case '.':
				outputStr += outputArea.getText()+((char)((int)memory.get(pointer)));
				break;
			case ',':
				String inputNum = "";
				boolean nan = false;
				do{
					if(nan) {
						JOptionPane.showMessageDialog(null, "Number was not entered");
					}
					inputNum = JOptionPane.showInputDialog("Input: ");
					try{
						Integer.parseInt(inputNum);
					}catch(Exception e) {
						nan = true;
					}
				}while(nan);
				memory.set(pointer, Integer.parseInt(inputNum));
				break;
			case '?':
				//? are weeded out in GUI if(config.questionAllowed == false)
				outputStr += outputArea.getText()+
						String.valueOf(memory.get(pointer));
				break;
			}
		}
		
		outputArea.setText(outputStr);
		//set text at end greatly optimizes speed when there are lots of outputs
		
		if((boolean)Config.settings.get(Config.txtOutput)) {
			FileUtil.tempFile(outputStr);
		}
		
	}
	
}
