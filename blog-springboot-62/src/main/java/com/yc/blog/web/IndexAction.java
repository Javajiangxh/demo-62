package com.yc.blog.web;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yc.blog.bean.Article;
import com.yc.blog.biz.ArticleBiz;
import com.yc.blog.biz.CategoryBiz;
import com.yc.blog.utils.Util;

@Controller
public class IndexAction {

	@Resource
	CategoryBiz cb;
	
	@Resource
	ArticleBiz ab;
	
	@ModelAttribute
	public void initData(Model model) {
		//所有页面都用得到的数据
		model.addAttribute("categoryList",cb.findAll());
	}
	
	@GetMapping(value= {"index","/"})
	public String index(Model model,@RequestParam(defaultValue="1") int page,
			@RequestParam(defaultValue="5") int size) {
		//查今日最新文章
		Article a = ab.todayHotTopic();
		String content = Util.subTag(a.getContent());
		a.setContent(content);
		model.addAttribute("todayArticle",a);
		
		/**
		 * 最新发布
		 */
		model.addAttribute("newList", ab.findByPage(0,page,size));
		return "index";
	}
	
	@GetMapping("category")
	public String category(Model model,int id,@RequestParam(defaultValue="1") int page,
			@RequestParam(defaultValue="5") int size) {
		model.addAttribute("articleList", ab.findByPage(id, page, size));
		return "category";
	}
	
	@GetMapping("article")
	public String article(Model model,int id) {
		model.addAttribute("article",ab.findArticle(id));
		return "article";
	}
	
	@RequestMapping("manager")
	public String manager() {
		return "manager";
	}
	
	@RequestMapping("articleMgr")
	public String articleMgr() {
		return "articleMgr";
	}
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${mail.fromMail.addr}")
	private String from;
	
	public void sendSimpleMail(String to,String subject,String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		mailSender.send(message);
	}
	
	@GetMapping("email")
	@ResponseBody
	public String send(String to,String content) {
		sendSimpleMail(to, "我是你爸爸", content);
		return "发送成功！";
	}
}
