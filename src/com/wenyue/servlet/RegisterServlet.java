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
			System.out.println("��Ҫ���ص���ַ�ǣ�"+url);
			System.out.println(request.getContextPath());
			User user = new User();
			user.setEmail(email);
//			user.setPassword(password);
//			user.setUsername(username);
//			user.setUploadPath(uploadPath);
			user.setUrl(url);
			System.out.println("��ʼ����---");
			
			System.out.println("���û���Ϣע�ᵽ���ݿ���");
			Sendmail send = new Sendmail(user,MakeMobi.DownloadAndMaker(url));
			send.start();
			
			
			request.setAttribute("message", "��ϲ����ע��ɹ��������Ѿ�����һ�����ע����Ϣ�ĵ����ʼ�������գ����û���յ�������������ԭ�򣬹�һ������յ��ˣ���");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		
		}catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "ע��ʧ�ܣ���");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}