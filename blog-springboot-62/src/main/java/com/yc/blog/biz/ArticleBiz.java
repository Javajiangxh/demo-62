package com.yc.blog.biz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yc.blog.bean.Article;
import com.yc.blog.bean.ArticleExample;
import com.yc.blog.bean.ArticleExample.Criteria;
import com.yc.blog.dao.ArticleMapper;

@Service
public class ArticleBiz {
	
	@Resource
	private ArticleMapper am;
	
	public Article todayHotTopic() {
		ArticleExample ae = new ArticleExample();
		ae.setOrderByClause("id desc");
		PageHelper.startPage(1,1);
		List<Article> list = am.selectByExampleWithBLOBs(ae);
		return list.isEmpty() ? null : list.get(0);
	}

	public Page<Article> findByPage(Article article,int page, int size) {
		ArticleExample ae = new ArticleExample();
		Criteria c = ae.createCriteria();
		
		if(article.getAuthor() != null && article.getAuthor().isEmpty() == false) {
			c.andAuthorLike("%" + article.getAuthor() +"%");
		}
		if(article.getTitle() != null && article.getTitle().isEmpty() == false) {
			c.andTitleLike("%" + article.getTitle() +"%");
		}
		if(article.getLabel() != null && article.getLabel().isEmpty() == false) {
			c.andLabelLike("%" + article.getLabel() +"%");
		}
		
		Page<Article> p = PageHelper.startPage(page,size);
		am.selectByExampleWithBLOBs(ae);
		return p;
	}
	
	public Page<Article> findByPage(int categoryid,int page, int size) {
		ArticleExample ae = new ArticleExample();
		ae.setOrderByClause("id desc");
		
		//判断分类categoryid是否大于0
		if(categoryid > 0) {
			ae.createCriteria().andCategoryidEqualTo(categoryid);
		}
		
		Page<Article> p = PageHelper.startPage(page,size);
		am.selectByExampleWithBLOBs(ae);
		return p;
	}
	

	public Article findArticle(int id) {
		Article article = am.selectByPrimaryKey(id);
		return article;
	}

	public void save(Article article) {
		if(article.getId() == null) {
			am.insertSelective(article);
		}else {
			am.updateByPrimaryKeyWithBLOBs(article);
		}
	}
}
