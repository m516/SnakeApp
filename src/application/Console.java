package application;

public class Console{
	//private static String[] lines = new String[64];
	//private static int numLines = 0;
	//private static TextArea gui = new TextArea("Welcome!");
	public static void init() {
		//gui.setEditable(false);
		//lines = new String[64];
		//numLines = 0;
	}
	public static void addText(String str) {
		/*
		numLines ++;
		if(numLines == lines.length) {
			for(int i = 0; i < lines.length-1; i ++) {
				lines[i] = lines[i+1];
			}
			numLines--;
		}
		lines[numLines] = str;
		String strings = "";
		for(int i = numLines; i >= 0; i --){
			String s = lines[i];
			if(s != null)	strings += s + "\n";
		}
		gui.setText(strings);
		 */
		System.out.println(str);
	}
}
