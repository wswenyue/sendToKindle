package com.wenyue.makeMobi;
import com.wenyue.download.HtmlExtractorImpl;
import com.wenyue.download.HtmlResult;
import com.wenyue.mail.CreateTxt;

public class MakeMobi {
	public static HtmlResult DownloadAndMaker(String url) throws Exception {

		HtmlExtractorImpl e = new HtmlExtractorImpl();
		HtmlResult r = e.extractContent(url);

		String path = Thread.currentThread().getContextClassLoader()
				.getResource("").toURI().getPath();
		String[] s = path.split("classes");
		String p = s[0];

		if (r != null) {
			if (r.getState() == "ok") {
				System.out.println("时间是：" + r.getDate());
				System.out.println("作者是：" + r.getAuth());
				System.out.println("来源是：" + r.getFrom());
				System.out.println("公司是：" + r.getCompany());
				System.out.println("标题是：" + r.getTitle());
				System.out.println(r.getText());

//				CreatHtml.Creat(r.getTitle(), r.getText(), p);
				CreateTxt.Creat(r.getTitle(), r.getText(), p);

			} else {

				System.out.println(r.getState());
				System.out.println(r.getMsg());
				System.out.println(r.getText());
			}
		}
		
		return r;
	}

}
