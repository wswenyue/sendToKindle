package com.wenyue.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class CreateTxt {
	
	 @SuppressWarnings("null")
	public static void Creat(String title,String content, String p) throws Exception{
		 
//		 content = content.replace("\n", "</br>");
	     String namePath = p+"out/"+title+".txt";
	     File file = new File(namePath);
         PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(file),false,"UTF-8");
			ps.println(title);// ���ļ���д���ַ���
			ps.append(content);// �����еĻ���������ַ���
			ps.flush();
			ps.close();
		} catch (FileNotFoundException e) {
			System.out.println("����txt����");
			e.printStackTrace();
			ps.close();
		}
	 }

}
