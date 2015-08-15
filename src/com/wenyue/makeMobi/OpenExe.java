package com.wenyue.makeMobi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OpenExe {
	public static void openExe(String[] cmds) {
		Runtime rn = Runtime.getRuntime();
		Process p = null;
		try {
//			String[] cmds = {"F:/javaWeb/KindleTest/Kindlegen.exe",htmlPath};
			p = rn.exec(cmds);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(),"UTF-8"));
			String msg = null;
			while ((msg = br.readLine()) != null) {
				System.out.println(msg);
			}
		} catch (Exception e) {
			System.out.println("Error exec!");
		}
	}

}
