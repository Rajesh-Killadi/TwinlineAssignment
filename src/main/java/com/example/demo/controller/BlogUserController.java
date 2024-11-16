package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.BlogForm;
import com.example.demo.entities.BlogUser;
import com.example.demo.entities.Blogs;
import com.example.demo.entities.LoginForm;
import com.example.demo.service.BlogUserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BlogUserController {

	@Autowired
	private BlogUserService service;

	@GetMapping(value = "/")
	public String loginPage(Model model) {

		LoginForm loginForm = new LoginForm();

		model.addAttribute("login", loginForm);

		return "login.html";
	}

	@GetMapping(value = "/create")
	public String createBlog(Model model) {

		BlogForm blogForm = new BlogForm();

		model.addAttribute("create", blogForm);
		

		return "createBlog.html";
	}

	@GetMapping(value = "/edit/{id}")
	public String editBlog(Model model, @PathVariable Integer id) {
 
		Blogs blog = service.findByBlogId(id);
		
		BlogForm blogForm = new BlogForm();
		blogForm.setId(blog.getId());
		blogForm.setName(blog.getName());
		blogForm.setBody(blog.getBody());

		model.addAttribute("blogForm", blogForm);

		return "editBlog.html";

	}

	@GetMapping(value = "/dashboard")
	public String dashboardPage(Model model, HttpSession session) {

		Object attribute = session.getAttribute("id");

		Integer id = (Integer) attribute;

		List<Blogs> blogsList = service.fetchAllBlogs(id);

		model.addAttribute("blogsList", blogsList);

		return "dashboard.html";

	}
	
	@GetMapping(value = "/report")
	public String report(Model model, HttpSession session) {
		
		Object id = session.getAttribute("id");
		List<Blogs> blogsList = service.fetchAllBlogs((Integer)id);
		
		Map<String, Long> map = service.getWordFrequency(blogsList);
		
      List<String> frequentWords = map.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .limit(5)
                .map(e -> e.getKey())
                .collect(Collectors.toList());
        
        model.addAttribute("frequentWords",frequentWords);
        
        return "report.html";		
	}
	
	@GetMapping(value = "/blog/{id}")
	public String viewBlog(Model model, @PathVariable Integer id) {
		
		Blogs blog = service.findByBlogId(id);
		
		BlogForm blogForm = new BlogForm();

		blogForm.setName(blog.getName());
		blogForm.setBody(blog.getBody());

		model.addAttribute("blog", blogForm);
		
		return "viewBlog.html";
		
		
	}

	@PostMapping(value = "login")
	public String login(Model model, LoginForm form, HttpSession session) {

		BlogUser user = service.login(form.getUserName(), form.getPassword());

		if (user != null) {

			session.setAttribute("id", user.getUserId());

			return "redirect:/dashboard";

		} else {

			model.addAttribute("message", "Invalid Credentials");

			LoginForm loginForm = new LoginForm();

			model.addAttribute("login", loginForm);

			return "login.html";

		}

	}

	@PostMapping(value = "/create")
	public String createBlog(Model model, BlogForm blogForm, HttpSession session) {

		Object attribute = session.getAttribute("id");
		Integer id = (Integer) attribute;
		service.createBlog(id, blogForm.getName(), blogForm.getBody());

		BlogForm blog = new BlogForm();

		model.addAttribute("create", blog);
		model.addAttribute("message", "New Blog Created");

		return "createBlog.html";

	}

	@PostMapping(value = "/edit")
	public String editBlog(Model model, BlogForm blogForm) {

		service.editBlog(blogForm.getId(), blogForm.getName(), blogForm.getBody());

		model.addAttribute("message", "Blog Updated Sucessfully");
		BlogForm blog = new BlogForm();

		model.addAttribute("blogForm", blog);

		return "editblog.html";

	}
	
	@GetMapping(value ="/delete/{id}")
	public String deleteBlog(Model model, @PathVariable Integer id) {
		
		service.deleteBlog(id);
		
		return "redirect:/dashboard";
		
		
	}
	@GetMapping(value = "/logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		return "redirect:/";
	}

}
