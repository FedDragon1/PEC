package pec.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class AutoClipboard {
	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(7500);
		for(int x = 5604; x < 6500; x++) {
			StringSelection selection = new StringSelection(Integer.toString(x));
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
			System.out.println(x);
			Thread.sleep(12950);
		}
	}
}
