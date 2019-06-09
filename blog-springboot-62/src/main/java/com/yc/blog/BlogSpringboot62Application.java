package com.yc.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yc.blog.dao")
public class BlogSpringboot62Application {

	public static void main(String[] args) {
		SpringApplication.run(BlogSpringboot62Application.class, args);
	}

}
