﻿package com.wenyue.download;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.mozilla.universalchardet.UniversalDetector;



public class HtmlExtractorImpl implements HtmlExtractor {

    public HtmlResult result = null;
    private int maxBytes = 1000000 / 2;
    public Pattern p;
    public Matcher m;
    public String format_html; // 其实是非格式化，如果需要自己改动
    public Boolean DEBUG=false;
    public HtmlResult extractContent(String url) {
	String html = "";
	String temptitle = "";
	
	// 获取网页页面
	try {
	    html = fetchHtmlString(url, 30000);
//	    //System.out.println(html);
	} catch (Exception e) {
	    //System.out.println("获取网页异常,网络问题");
	    return new HtmlResult("fail", "获取网页异常,网络问题", url);
	}
	if (html.length() == 0) {
	    //System.out.println("无法获取网页信息");
	    return new HtmlResult("fail", "无法获取源网页内容", url);
	}
	String str = html.toString();
//	//System.out.println(str);
	/*
	 * //获取文本页面 InputStreamReader inputReader = null; BufferedReader
	 * bufferReader = null; StringBuffer strBuffer = new StringBuffer(); try
	 * { InputStream inputStream = new FileInputStream(url); inputReader =
	 * new InputStreamReader(inputStream); bufferReader = new
	 * BufferedReader(inputReader);
	 * 
	 * // 读取一行 String line = null;
	 * 
	 * while ((line = bufferReader.readLine()) != null) {
	 * strBuffer.append(line); }
	 * 
	 * } catch (IOException e) { //System.out.println("*******"+e); } String
	 * str=strBuffer.toString();
	 */
	// 直接赋值
	// String str;
	//str = "<meta name="description" content=";
         str = str.toLowerCase();
	 str = str.replace("\r", "");
	 str = str.replace("\n", ""); 

	 
	 /**
	  * 除去html注释，这里改进了原作者的
	  * */
	 if(str != null){
	 Pattern pattern = Pattern.compile("<!--[\\w\\W\r\\n]*?-->");
	 Matcher matcher = pattern.matcher(str);
	 str = matcher.replaceAll("");;
//	 //System.out.println(str);
	 }
	 
	 
	 
	 if(str!=null)  //根据网页的视觉呈现来添加回车换行符号
	 {
	     int i=1;
	     StringBuilder sb=new StringBuilder(str);
	     p = Pattern.compile("(<br>|<br />|</p>|</tr>|</table>|</form>|</div>|</head>)\\s*");
	     m = p.matcher(str);
	     while (m.find()) {
		 sb.insert(m.end()+i-1, "\n");
		 i++;
	     }
	     Pattern p2=Pattern.compile("<h1>|<h2>");
	     Matcher m2=p2.matcher(sb.toString());
	     i=0;
	     while(m2.find())
	     {
		 sb.insert(m2.end()+i-4, "\n\n");
		 i+=2;
	     }
             str=sb.toString();
	  }
	
	if (str != null) {
	    p = Pattern.compile(".*<title>(.*?)</title>.*");
	    m = p.matcher(str);
	    while (m.find()) {
		temptitle= m.group(1);
	    }
	}
	if (str != null) // 去除style标签
	{
	    p = Pattern.compile("(?is)<style[^>]*?>.*?<\\/style>");
	    m = p.matcher(str);
	    str = m.replaceAll("");
	}
	if (str != null) // 去除html的转义字符
	{
	    p = Pattern.compile("&[a-z]*;?");
	    m = p.matcher(str);
	    str = m.replaceAll("");
	}
	
	if (str != null) // 去除script代码
	{
	    p = Pattern.compile("(?is)<script[^>]*?>.*?<\\/script>");
	    m = p.matcher(str);
	    str = m.replaceAll("");
	}
	
	if(str!=null)  //根据文本逻辑加上换行符
	{
	    int i=1;
	    StringBuilder sb=new StringBuilder(str);
	    p = Pattern.compile("(</[a-z0-9]+>\\s*)+[^\n]");
	    m = p.matcher(str);
	    while (m.find()) 
	    {
	        if(sb.charAt(m.end()+i-3)!='\n')
	        {
	            sb.insert(m.end()+i-2, "\n");
		    i++;
	        } 
	    }
	    p=Pattern.compile("</span>|</strong>|</b>");
	    m=p.matcher(sb.toString());
	    i=0;
            while(m.find())
            {
        	if(m.end()!=sb.length() && sb.charAt(m.end()-i) == '\n')
        	{
        	    sb.deleteCharAt(m.end()-i);
        	    i++;
        	}
        	
            }
	    str=sb.toString();
	}
	format_html = str;
//	//System.out.println("输出格式化的html");
//	//System.out.println(str);
	
	String[] html_blocks = str.split("\n");
	List<String> tag_blocks = new ArrayList<String>();
	for (int i = 0; i < html_blocks.length; i++) {
	    tag_blocks.add(html_blocks[i].trim());
	}
	if (str != null) // 去除所有不带换行的标签
	{
	    p = Pattern.compile("<[^>\n]*>");
	    m = p.matcher(str);
	    str = m.replaceAll("");
	}
	if (!str.isEmpty()) // 去除所有带换行的标签
	{
	    p = Pattern.compile("<[^>\n]*\n|[^<\n]*>\n");
	    m = p.matcher(str);
	    str = m.replaceAll("\n");
	}
	else{
	     //System.out.println("提取之后的文本为空！");
	     return result;
	}
	
        String[] str_blocks = str.split("\n");
	List<String> word_blocks = new ArrayList<String>();
	for (int i = 0; i < str_blocks.length; i++) {
	    word_blocks.add(str_blocks[i].trim());
	}
	
	if(word_blocks.isEmpty())
	{
	    //System.out.println("提取之后的文本为空！");
	    return result;
	}
	print_test(word_blocks);
	int em_raw=0;
	for(int i=0;i<word_blocks.size();i++)
	{
	    if(word_blocks.get(i).length()==0)
		em_raw++;
	}
	////System.out.println(word_blocks.size()+"***"+em_raw);
	int k=em_raw/(word_blocks.size()-em_raw)+2;
    
	List<ContentBlock> contents = new ArrayList<ContentBlock>();
	Get_Content(contents,word_blocks,tag_blocks,k);   //根据行块粗略的得到可能是正文的块，相关信息记录在contents里面
	result=ExtractMainContent(contents,word_blocks,tag_blocks);
	if(temptitle.length()!=0 && result !=null)
	{
	    result.setUrl(url);
	    if(result.getTitle()==null)
	        result.setTitle(temptitle);
	}
	//extractMainText(blocks, titles, 4);
	//System.out.println("步长****"+k);
	return result;
    }
    private HtmlResult ExtractMainContent(List<ContentBlock> contents,List<String> blocks,List<String> tag_blocks)
    {
	HtmlResult temp_result=new HtmlResult();
	int longst=0;
	if(contents.isEmpty())
	{
	    temp_result.setState("fail");
	    temp_result.setMsg("页面行块提取失败");
	    return temp_result;
	}   
	
	for(int i=0;i<contents.size();i++)
	{
	    if (contents.get(i).getlength() > contents.get(longst).getlength()) 
	    {
	        longst = i;
	    }
            if(contents.get(i).getlink()!=0) //字数和链接比
            {
        	if(contents.get(i).getlength()/contents.get(i).getlink()>50)
                    contents.get(i).weight+=4;
        	else if(contents.get(i).getlength()/contents.get(i).getlink()>20)
                    contents.get(i).weight+=2;
        	else
        	    contents.get(i).weight-=20;
            }
            else{
        	contents.get(i).weight+=5;
            }
            //行和链接比
            if((float)(contents.get(i).getEnd()-contents.get(i).getStart()-contents.get(i).getemptyrow())/contents.get(i).getlink()>2.5)
        	contents.get(i).weight+=2;
            else
        	contents.get(i).weight-=50;
            //如果这个content只有一个段落则减分
            if(contents.get(i).getStart() == contents.get(i).getEnd())
        	contents.get(i).weight-=5;
        }
	int tt;
        tt=contents.size()-1;
	if(tt>1)
	{
	    contents.get(longst).weight+=5;//绝对字长最大
	    contents.get(0).weight-=4;
	    contents.get(tt).weight-=5;     //相对位置，在头尾的字块减
	}
	for(int i=0;i<contents.size();i++)
	{
	    if (contents.get(i).getweight() > contents.get(longst).getweight()) 
	    {
	        longst = i;   
	    }
	    //if(DEBUG)
	        //System.out.println("权值是***"+contents.get(i).getweight());
	}
	if(contents.get(longst).getweight()<80)
	{
	    temp_result.setState("false");
	    temp_result.setMsg("很可能是列表页面");
	    return temp_result;
	}
	else{
	    for(int i=longst;i<contents.size()-1;i++)
	    {
		int s=contents.get(i+1).getStart();
		int e=contents.get(i).getEnd();
		int judge=1;
		if(s-e<=11)
		{
		    for(int count=e+1;count<s;count++)
		    {
			if(blocks.get(count).length()!=0)
			{
			    judge=0;
			    break;
			}
		    }
		}
		else{
		    break;
		}
		if(judge == 1)
		{
		    if(contents.get(longst).getweight()-contents.get(i+1).getweight()<=25)
		    {
			int tend=contents.get(i+1).getEnd();
			contents.get(longst).setEnd(tend);
		    }
		}
		else{
		    break;
		}
	    }
	    for(int i=longst;i>0;i--)
	    {
		int s=contents.get(i).getStart();
		int e=contents.get(i-1).getEnd();
		int judge=1;
		if(s-e<=11)
		{
		    for(int count=e+1;count<s;count++)
		    {
			if(blocks.get(count).length()!=0)
			{
			    judge=0;
			    break;
			}
		    }
		}
		else{
		    break;
		}
		if(judge == 1)
		{
		    if(contents.get(longst).getweight()-contents.get(i-1).getweight()<=25)
		    {
			int tend=contents.get(i-1).getStart();
			contents.get(longst).setStart(tend);
		    }
		}
		else{
		    break;
		}
	    }
	    Get_Detail(contents.get(longst),temp_result,blocks,tag_blocks);
	    StringBuffer currentText= new StringBuffer();
	    for (int count = contents.get(longst).getStart(); count <= contents.get(longst).getEnd(); count++) 
	    {
		if(blocks.get(count).length()!=0)
		{
		    currentText.append(blocks.get(count));
		    currentText.append("\n");
		}
	    }
	    temp_result.setText(currentText.toString());
	    temp_result.setState("ok");
	    
	    return temp_result;
	}
    }
    private void Get_Detail(ContentBlock longst,HtmlResult result,List<String> blocks,List<String> tag_blocks)
    {
	int tt=longst.getStart();
	int raw=0;
	int maxline=99999;
	int minline=longst.getStart();
	while(tt >=0 && raw <15)
	{
	    while(tt >=0 && blocks.get(tt).length()<=1)
		tt--;
	    raw++;
	    if (tt >= 0) 
	    {
		String temptt = blocks.get(tt);
		p = Pattern.compile("[0-9]{2,4}[年/-][0-9]{1,2}[月/-][0-9]{1,4}[日]");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setDate(m.group());
		    if (tt < maxline)
			maxline = tt;
		    break;
		}
		p = Pattern.compile("(来源|来源于)[:：\\s]([^\\s]*)\\s*");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setFrom(m.group(2));
		    if (tt < maxline)
			maxline = tt;
		    break;
		}
		p = Pattern.compile("(笔者|编辑|作者|记者)[:：\\s]([^\"“]*)\\s*");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setAuth(m.group(2));
		    if (tt < maxline)
			maxline = tt;
		    break;
		}
		p = Pattern.compile("[\u4e00-\u9fa5]+(公司|网)$");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setCompany(m.group());
		    break;
		}
		p = Pattern.compile("(关键字|关键词)[:：\\s]([\u4e00-\u9fa5]*)");
		m = p.matcher(temptt);
		while (m.find()) {
		    if (tt < maxline)
			maxline = tt;
		    result.addkeywords(m.group(1));
		    break;
		}
		tt--;
	    }
	}
	if(result.getDate()==null)
	{
	    raw=0;
	    tt=longst.getStart()+1;
	    while(tt <longst.getEnd() && raw <8)
            {
		while (tt <longst.getEnd() && blocks.get(tt).length() == 0)
		    tt++;
		raw++;
		String temptt = blocks.get(tt);
		if(result.getDate()==null)
		{
		    p = Pattern.compile("[0-9]{2,4}[年/-][0-9]{1,2}[月/-][0-9]{1,4}[日]*");
		    m = p.matcher(temptt);
		    while (m.find()) {
			result.setDate(m.group());
			if (tt < maxline)
			   maxline = tt;
			if (tt > minline && tt <longst.getlongst_row())
			    minline = tt+1;
			break;
		   }
		}
		p = Pattern.compile("(来源|来源于)[:：]\\s?([^\\s]*)\\s*");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setFrom(m.group(2));
		    if (tt < maxline)
			maxline = tt;
		    if (tt > minline && tt <longst.getlongst_row())
			minline = tt+1;
		    break;
		}
		p = Pattern.compile("(笔者|编辑|作者|记者)[:：\\s]([^\"“]+)\\s+");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setAuth(m.group(2));
		    if (tt < maxline)
			maxline = tt;
		    if (tt > minline && tt <longst.getlongst_row())
			minline = tt+1;
		    break;
		}
		p = Pattern.compile("[\u4e00-\u9fa5]+(公司|网)$");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setCompany(m.group());
		    break;
		}
		p = Pattern.compile("(关键字|关键词)[:：\\s]([\u4e00-\u9fa5]*)");
		m = p.matcher(temptt);
		while (m.find()) {
		    if (tt < maxline)
			maxline = tt;
		    if (tt > minline && tt <longst.getlongst_row())
			minline = tt+1;
		    result.addkeywords(m.group(1));
		    break;
		}
		if(temptt.contains("摘要") && tt >minline && tt<longst.getlongst_row())
		{
		    minline = tt;
		}
		tt++;
	    }
	}
	
	if(maxline != 99999)
        {
	   int j=maxline-1;
	   int r=0;
	   String[] titles=new String[3];
	   int[] weight={100,100,100};
	   Pattern pt2=Pattern.compile("<h1>");
           Matcher mt2;
	   while(j>=0 && r<3)
	   {
	       while(j>=0 &&blocks.get(j).length()<=4)
		   j--;
	       if(j>=0)
	       {
		   titles[r]=blocks.get(j);
		   mt2=pt2.matcher(tag_blocks.get(j));
		   if(mt2.find())
		   {
		       weight[r]+=10;
		   }
	       }
	       
	       r++;
	       j--;
	   }
	   for (int i = 0; i <3 ; i++) 
	   {
	       if (titles[i] == null || titles[i].length() > 100)
	       {
	           weight[i] = 0;
	           continue;
	       }
	       if (titles[i].length() > 50 || titles[i].length() < 6)
	       {
	           weight[i] -= 10;
	       }
	       if( titles[i].contains("http://www")||titles[i].contains("链接"))
	       {
	           weight[i] -= 10;
	       }
	       if (titles[i].contains("位置") || titles[i].contains("新闻")||titles[i].contains("网")|| titles[i].contains("报")||titles[i].contains(">")) 
	       {
		   weight[i] -= 4;
	       }
	       if (titles[i].contains("标题")||titles[i].contains("题目")) 
	       {
	           weight[i] += 5;	
	       }
	       p = Pattern.compile("[\\._|,:：，、0-9a-z]");
	       m = p.matcher(titles[i]);
	       while (m.find()) {
	           weight[i] -= 1;
	       }
	   }
	   int max = 0;
	   for (int m = 0; m < 3; m++) {
	        ////System.out.println("!!!!!!!!!!!!!!!!!!!"+titles[m]);
	        ////System.out.println(weight[m]);
	        if (weight[m] > weight[max])
	        max = m;
	   }
	   if(weight[max]>96)
	       result.setTitle(titles[max]);
	}
	if(result.getDate()==null)
	{
	    raw=0;
	    for(int mm=longst.getEnd();mm<blocks.size() && raw<3;mm++)
	    {
		while (mm<blocks.size() && blocks.get(mm).length() == 0)
		    mm++;
		raw++;
		if(mm!=blocks.size())
		{
		    String temptt = blocks.get(mm);
		    p = Pattern
			    .compile("[0-9]{2,4}[年/-][0-9]{1,2}[月/-][0-9]{1,4}[日]*");
		    m = p.matcher(temptt);
		    while (m.find()) {
			result.setDate(m.group());
			break;
		    }
		    p = Pattern.compile("(来源|来源于)[:：\\s]([^\\s]*)\\s*");
		    m = p.matcher(temptt);
		    while (m.find()) {
			result.setFrom(m.group(2));
			break;
		    }
		    p = Pattern.compile("(笔者|编辑|作者|记者)[:：\\s]([\u4e00-\u9fa5]*)\\s+");
		    m = p.matcher(temptt);
		    while (m.find()) {
			result.setAuth(m.group(2));
			break;
		    }
		}
	    }
	}
	//System.out.println("maxline"+maxline+"minline+"+minline+"longstline"+longst.getlongst_row());
	longst.setStart(minline);
    }
    private void Get_Content(List<ContentBlock> contents,List<String> blocks,List<String> tag_blocks,int k)
    {
	int len = 0;
	int threshold=0;
	int start=0, end=0;
	List<Block> candidates = new ArrayList<Block>();
	Block current = null;
	ContentBlock temp=null;
	for (int i = 0; i + k <= blocks.size(); i++) {
	    len = 0;
	    for (int j = i; j < i + k; j++) {
		len = len + blocks.get(j).length();
	    }
	    current = new Block();
	    current.setStart(i);
	    current.setEnd(i + k - 1);
	    current.setlength(len);
	    candidates.add(current);
	    current = null;
	}
	if (candidates.size() == 0) {
	    result.setState("fail");
	    result.setMsg("行块无法获得");
	}
	else{
	    Block longst = candidates.get(0);
	    for (Block b : candidates) {
		if (b.getlength() > longst.getlength()) {
		    longst = b;
		}
	    }
	    threshold=longst.getlength()/3;
	    int i=0;
	    int start_judge=1;
	    while(i<candidates.size())
	    {
		if(start_judge==1)
		{
		    if(candidates.get(i).getlength()>=10)
		    {
			start=i;
			start_judge=0;
		    }
		}
		if(candidates.get(i).getlength()>=threshold)
		{
		    temp=new ContentBlock();
		}
		if(start_judge==0)
		{
		    if(candidates.get(i).getlength()<=4)
		    {
			end=i;
			start_judge=1;
			if(temp!=null)
			{
                            if(start==96)
                        	DEBUG=true;
                            else
                        	DEBUG=false;
			    temp.setStart(start);
			    temp.setEnd(end-1);
			    int templen=0;
			    int templonst=start+k-1;
			    for (int j = start+k-1; j <=end-1; j++) {
				templen = templen + blocks.get(j).length();
				if(blocks.get(j).length()>blocks.get(templonst).length())
				    templonst=j;
			    }
			    temp.setlongst_row(templonst);
			    temp.setlength(templen);
			    Get_Articleweight(temp,blocks,tag_blocks);
			    contents.add(temp);
			    //System.out.println("start****"+(start));
			    //System.out.println("end****"+(end-1));
			    temp=null;
			}
		    }
		    if(i==candidates.size()-1)
		    {
			start_judge=1;
			if(temp!=null)
			{
			    temp.setStart(start);
			    temp.setEnd(i);
			    int templen=0;
			    int templonst=start+k-1;
			    for (int j = start+k-1; j <=end-1; j++) {
				templen = templen + blocks.get(j).length();
				if(blocks.get(j).length()>blocks.get(templonst).length())
				    templonst=j;
			    }
			    temp.setlongst_row(templonst);
			    temp.setlength(templen);
			    Get_Articleweight(temp,blocks,tag_blocks);
			    contents.add(temp);
			    //System.out.println("start****"+(start));
			    //System.out.println("end****"+(end-1));
			    temp=null;
			}
		    }
		}
		i++;
	    }
	}
    }
    private void Get_Articleweight(ContentBlock temp,List<String> word_blocks,List<String> tag_blocks)
    {
	Pattern pt=Pattern.compile("，|。|？|！|；|、");
	Matcher mt;
	Pattern pt2=Pattern.compile("<a\\s*[^>]*>");
	Matcher mt2;
	Pattern pt3=Pattern.compile("(电话|手机|联系人|地址|传真|mail|邮编|邮箱|Tel|Fax|版权|邮件)\\s?([:：])");
	Matcher mt3;
	Pattern pt4=Pattern.compile("[京浙]\\s*icp\\s*[证备]?");
	Matcher mt4;
	Pattern pt5=Pattern.compile("[\u4e00-\u9fa5]|[\\x21-\\x7E]");
	Matcher mt5;
	int link_num=0;
	int empty_row=0;
	int temp_weight=temp.getweight();
	for(int i=temp.getStart();i<=temp.getEnd();i++) //有标点符号权值加2
	{
	    if(word_blocks.get(i).length()>=2)
	    {
		mt=pt.matcher(word_blocks.get(i));
		while(mt.find())
		{
		    if(DEBUG)
			    //System.out.println("有标点符号，权值加2");
		    temp_weight+=2;
		}
		mt3=pt3.matcher(word_blocks.get(i));
		while(mt3.find())
		{
		    if(DEBUG)
			    //System.out.println("有联系方式，权值减5");
		    temp_weight-=8;
		    
		}
		mt4=pt4.matcher(word_blocks.get(i));
		while(mt4.find())
		{
		    if(DEBUG)
			    //System.out.println("含有京证，可能是尾部，权值减15");
		    temp_weight-=50;
		}
		mt5=pt5.matcher(word_blocks.get(i));
		int utfword=0;
		while(mt5.find())
		{
		    utfword++;
		}
		int temprow=temp.getEnd()-temp.getStart()-temp.getemptyrow();
		if(utfword!=0 && temprow != 0)
		   temp_weight-=((word_blocks.get(i).length()-utfword)*80/temprow/utfword);
	    }
	    else{
		empty_row++;
	    }
	    mt2=pt2.matcher(tag_blocks.get(i));
	    while(mt2.find())                          //有链接，权值减2
	    {
		if(DEBUG)
		    //System.out.println("有链接，权值减2");
		temp_weight-=2;
		link_num++;
	    }
	    
	}
	temp.setlink(link_num);
	temp.setweight(temp_weight);
	temp.setemptyrow(empty_row);
    }
    private void print_test(List<String> blocks) {
	for (int i = 0; i < blocks.size(); i++) {
	    // //System.out.println(i);
	    //System.out.println(blocks.get(i));
	}
    }
    
    private String fetchHtmlString(String url, int timeout)
	    throws MalformedURLException, IOException {
	HttpURLConnection connection = createHttpConnection(url, timeout);
	connection.setInstanceFollowRedirects(true);
	String encoding = connection.getContentEncoding();

	InputStream is;
	if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
	    is = new GZIPInputStream(connection.getInputStream());
	} else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
	    is = new InflaterInputStream(connection.getInputStream(),
		    new Inflater(true));
	} else {
	    is = connection.getInputStream();
	}
	String streamEncoding=null;
	byte[] data = streamToData(is);
	String strtemp=connection.getContentType();
	int m = strtemp.indexOf("charset=");
        if (m != -1) {
            streamEncoding = strtemp.substring(m + 8).replace("]", "");
        }
	////System.out.println("streamEncoding is "+streamEncoding);
	if(streamEncoding == null){
	    streamEncoding = detectEncoding(data);
	}
	if (data == null || streamEncoding == null) {
	    //System.out.println("streamEncoding is null");
	    return "";
	}
	return new String(data, streamEncoding);
    }

    private HttpURLConnection createHttpConnection(String url, int timeout)
	    throws MalformedURLException, IOException {
	HttpURLConnection connection = (HttpURLConnection) new URL(url)
		.openConnection(Proxy.NO_PROXY);
	connection
		.setRequestProperty(
			"User-Agent",
			"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	connection
		.setRequestProperty("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	connection.setRequestProperty("content-charset", "UTF-8");
	connection.setRequestProperty("Cache-Control", "max-age=0");
	connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
	connection.setConnectTimeout(timeout);
	connection.setReadTimeout(timeout);
	return connection;
    }

    private String detectEncoding(byte[] data) {
	UniversalDetector detector = new UniversalDetector(null);
	detector.handleData(data, 0, data.length);
	detector.dataEnd();
	String encoding = detector.getDetectedCharset();
	detector.reset();
	return encoding;
    }

    private byte[] streamToData(InputStream is) {
	BufferedInputStream in = null;
	try {
	    in = new BufferedInputStream(is, 2048);
	    ByteArrayOutputStream output = new ByteArrayOutputStream();

	    int bytesRead = output.size();
	    byte[] arr = new byte[2048];
	    while (true) {
		if (bytesRead >= maxBytes) {
		    break;
		}
		int n = in.read(arr);
		if (n < 0)
		    break;
		bytesRead += n;
		output.write(arr, 0, n);
	    }
	    return output.toByteArray();
	} catch (SocketTimeoutException e) {

	    return null;
	} catch (IOException e) {

	    return null;
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (Exception e) {
		}
	    }
	}
    }
}
