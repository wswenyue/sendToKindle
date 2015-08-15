package com.wenyue.download;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadHtml {  
  
    public static void main(String[] args){  
        try {  
//            System.out.println(DownloadHtml.getUrlDetail("http://www.baidu.com",true));  
            saveUrlFile("http://www.programgo.com/tag/java/394578930/5/", "D:\\1.html");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
      
    //��ȡ�����ļ���ת�浽fileDes�У�fileDes��Ҫ���ļ���׺��  
    public static void saveUrlFile(String fileUrl,String fileDes) throws Exception  
    {  
        File toFile = new File(fileDes);  
        if (toFile.exists())  
        {  
//          throw new Exception("file exist");  
            return;  
        }  
        toFile.createNewFile();  
        FileOutputStream outImgStream = new FileOutputStream(toFile);  
        outImgStream.write(getUrlFileData(fileUrl));  
        outImgStream.close();  
    }  
      
    //��ȡ���ӵ�ַ�ļ���byte����  
    public static byte[] getUrlFileData(String fileUrl) throws Exception  
    {  
        URL url = new URL(fileUrl);  
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
        httpConn.connect();  
        InputStream cin = httpConn.getInputStream();  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while ((len = cin.read(buffer)) != -1) {  
        	System.out.println(len);
            outStream.write(buffer, 0, len);  
        }  
        cin.close();  
        byte[] fileData = outStream.toByteArray();  
        outStream.close();  
        return fileData;  
    }  
      
    //��ȡ���ӵ�ַ���ַ����ݣ�wichSep�Ƿ��б��  
    public static String getUrlDetail(String urlStr,boolean withSep) throws Exception  
    {  
        URL url = new URL(urlStr);  
        HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();  
        httpConn.connect();  
        InputStream cin = httpConn.getInputStream();  
        BufferedReader reader = new BufferedReader(new InputStreamReader(cin,"UTF-8"));  
        StringBuffer sb = new StringBuffer();  
        String rl = null;  
        while((rl = reader.readLine()) != null)  
        {  
            if (withSep)  
            {  
                sb.append(rl).append(System.getProperty("line.separator"));  
            }  
            else  
            {  
                sb.append(rl);  
            }  
        }  
        return sb.toString();  
    }  
      
}  