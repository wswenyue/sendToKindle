package com.wenyue.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wenyue.domain.User;
import com.wenyue.mail.Sendmail;
import com.wenyue.makeMobi.MakeMobi;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//	String uploadPath = this.getServletContext().getRealPath("/WEB-INF/upload");
		
		try{
//			String username = request.getParameter("username");
//			String password = request.getParameter("password");
			String email = request.getParameter("email");
			String url = request.getParameter("url");
			System.out.println("需要下载的网址是："+url);
			System.out.println(request.getContextPath());
			User user = new User();
			user.setEmail(email);
//			user.setPassword(password);
//			user.setUsername(username);
//			user.setUploadPath(uploadPath);
			user.setUrl(url);
			System.out.println("开始制作---");
			
			System.out.println("把用户信息注册到数据库中");
			Sendmail send = new Sendmail(user,MakeMobi.DownloadAndMaker(url));
			send.start();
			
			
			request.setAttribute("message", "恭喜您，注册成功，我们已经发了一封带了注册信息的电子邮件，请查收，如果没有收到，可能是网络原因，过一会儿就收到了！！");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		
		}catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "注册失败！！");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}