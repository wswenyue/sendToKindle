<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Send To Kindle</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" type="text/css"
			href="http://7xj2ay.com1.z0.glb.clouddn.com/style.css">
			<link
				href="http://7xj2ay.com1.z0.glb.clouddn.com/fonts-googleapis1.css"
				rel="stylesheet" type="text/css">
				<link rel="stylesheet"
					href="http://7xj2ay.com1.z0.glb.clouddn.com/share_style.css">
					<link
						href="http://7xj2ay.com1.z0.glb.clouddn.com/fonts-googleapis2.css"
						rel="stylesheet" type="text/css">
</head>
<body>
	<div class="wrap">
		<div class="content">
			<div class="logo">
				<a href="index.html"><h1>Send To Kindle</h1></a>
			</div>
			<p>The charm of reading &#33;</p>
			<font color="#FFFFFF"> 请把wswenyue@126.com加入到您的kindle认可邮箱列表里 </font>
			<div class="form">
				<form id="contact-form"
					action="${pageContext.request.contextPath }/servlet/RegisterServlet"
					method="post">
					<input placeholder="Please enter Arctic URL" type="url" name="url"
						required> <input
						placeholder="Please enter your kindle email address" type="email"
						name="email" required></br> <input type="submit" name="submit"
							id="contact-submit" value="Submit">
				</form>

			</div>
			<div class="footer">
				<div class="social-icons">

					<!-- share  -->
					<div id="sns_share" class="cf">
						<span class="sns_share_to fl">分享到：</span> <a
							class="share_weixin share_icon fl" href="javascript:;"
							title="查看本文二维码，分享至微信"><em>二维码</em></a> <a
							class="share_tsina share_icon fl" href="javascript:;"
							title="分享到新浪微博"><em>新浪微博</em></a> <a
							class="share_tqq share_icon fl" href="javascript:;"
							title="分享到腾讯微博"><em>腾讯微博</em></a> <a
							class="share_renren share_icon fl" href="javascript:;"
							title="分享到人人网"><em>人人网</em></a> <a
							class="share_tqzone share_icon fl" href="javascript:;"
							title="分享到QQ空间"><em>QQ空间</em></a>

						<div class="wemcn" id="wemcn" style="display: none;">
							<div id="ewm" class="ewmDiv clearfix">
								<div class="rwmtext">
									<p>微信扫一扫，添加关注</p>
									<p>关注作者最新动态</p>
									<p>与作者沟通</p>
								</div>
							</div>
							<a class="share_icon" href="javascript:void(0)" id="ewmkg"></a> <i
								class="ewmsj share_icon"></i>
						</div>

					</div>

					<script src="http://7xj2ay.com1.z0.glb.clouddn.com/jquery.js"></script>
					<script
						src="http://7xj2ay.com1.z0.glb.clouddn.com/xuanfeng_sns_share.js"></script>
					<script>
						$(function() {
							var shareTitle = $(".post_title h1").text()
									+ "SendToKindle ( http://kindler.sturgeon.mopaas.com )";
							var sinaTitle = '分享 「' + shareTitle
									+ '」 （分享自@wswenyue）', renrenTitle = '分享 「'
									+ shareTitle + '」（分享自@wswenyue）', tqqTitle = '分享 「'
									+ shareTitle + '」（分享自@问月阁）', tqzoneTitle = '分享 「'
									+ shareTitle + '」（分享自@问月阁）';
							var picShare = encodeURIComponent($(".post_title")
									.data("thumb"));

							$('body')
									.xuanfengSnsShare(
											{
												tsina : {
													url : encodeURIComponent(window.location.href),
													title : sinaTitle,
													pic : picShare
												},
												renren : {
													url : encodeURIComponent(window.location.href),
													title : renrenTitle,
													pic : picShare
												},
												tqq : {
													url : encodeURIComponent(window.location.href),
													title : tqqTitle,
													pic : picShare
												},
												tqzone : {
													url : encodeURIComponent(window.location.href),
													title : tqzoneTitle,
													pic : picShare
												}
											});

							// 微信分享	
							$(".share_weixin")
									.click(
											function() {
												$("#ewmimg").remove();
												var thisURL = window.location.href, strwrite = "<img id='ewmimg' class='ewmimg'  src='http://7xj2ay.com1.z0.glb.clouddn.com/wchat.jpg' />";
												$("#ewm").prepend(strwrite);
												$("#wemcn").show();
											});
							$("#ewmkg").click(function() {
								$("#wemcn").hide();
							});

						});
					</script>

				</div>
			</div>
		</div>
	</div>

</body>
</html>