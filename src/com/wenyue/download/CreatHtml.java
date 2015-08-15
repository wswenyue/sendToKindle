package com.wenyue.download;

import java.io.FileWriter;
import java.io.IOException;
 



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.wenyue.makeMobi.OpenExe;



public class CreatHtml{
 
  public static void Creat(String title,String content, String p){
    	
	     content = content.replace("\n", "</br>");
	     String namePath = null;
		// Parse HTML String using JSoup library
        String HTMLSTring = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + " <meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\" /> "
                + "<title>"
                + title 
                + "</title>"
                + "</head>"
                + "<body>"
                + "<h1>"
                + title
                + "</h1>"
                + "<p>"
                + content 
                + "</p>"
                + "</body>"
                + "</html>";
 
        Document html = Jsoup.parse(HTMLSTring);
        FileWriter writer;
        try {
        	namePath = p+"out/1.html";
			writer = new FileWriter(namePath);
			System.out.println("字符编码："+writer.getEncoding());
			writer.write(html.toString());
			writer.flush();
			writer.close();
			
			String[] cmds = {p+"out/kindlegen.exe","1.html"};
			
			OpenExe.openExe(cmds);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
       
  }    
 
 
}