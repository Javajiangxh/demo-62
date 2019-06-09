package com.yc.blog.biz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yc.blog.bean.Category;
import com.yc.blog.dao.CategoryMapper;

@Service
public class CategoryBiz {
	
	@Resource
	CategoryMapper cm;
	
	public List<Category> findAll(){
		List<Category> list = cm.selectByExample(null);
		return list;
	}
}
