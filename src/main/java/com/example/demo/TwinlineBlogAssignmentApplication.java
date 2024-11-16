package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.entities.BlogUser;
import com.example.demo.entities.Blogs;
import com.example.demo.repository.BlogUserRepo;
import com.example.demo.repository.BlogsRepo;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class TwinlineBlogAssignmentApplication {
	
	@Autowired
	private BlogUserRepo userRepo;
	@Autowired
	private BlogsRepo blogsRepo;

	public static void main(String[] args) {
		SpringApplication.run(TwinlineBlogAssignmentApplication.class, args);
	}
	
	@PostConstruct
    public void insertUser() {
        BlogUser user = new BlogUser();
        
        user.setUsername("twinline");
        user.setPassword("twinline");
        BlogUser savedUser = userRepo.save(user);
        
        Blogs blog1 = new Blogs();
        blog1.setBody("TwinLine Business Solutions is a software company based in Gurgaon, India, specializing in providing end-to-end software solutions for the banking, lending, and finance sectors. They focus on delivering efficient mobile applications, data analytics tools, and bespoke software solutions to address the unique needs of financial and non-banking financial companies (NBFCs). With a team of experts from leading IT firms, TwinLine offers innovative solutions to streamline operations, enhance customer experience, and improve financial processes for their clients.");
        blog1.setName("Twinline");
        blog1.setUser(savedUser);
        
        Blogs blog2 = new Blogs();
        blog2.setBody("Hello, My name is rajesh and I am person who is very Interested about our ancient history and I love playing chess. coming to my skillset I'm proficint in Java, Springboot, Aws, Sql, Html, Css, Javascript");
        blog2.setName("Rajesh Killadi");
        blog2.setUser(savedUser);
        
        blogsRepo.save(blog1);
        blogsRepo.save(blog2);
    }

}
