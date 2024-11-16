package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.BlogUser;
import com.example.demo.entities.Blogs;
import com.example.demo.repository.BlogUserRepo;
import com.example.demo.repository.BlogsRepo;

@Service
public class BlogUserService {

	@Autowired
	private BlogUserRepo userRepo;

	@Autowired
	private BlogsRepo blogsRepo;

	public BlogUser login(String username, String password) {

		BlogUser user = userRepo.findByUsername(username);

		if (user == null)
			return null;

		if (user.getPassword().equals(password))
			return user;
		else
			return null;
	}

	public boolean createBlog(Integer blogUserId, String blogName, String blogDescription) {

		Optional<BlogUser> blogUser = userRepo.findById(blogUserId);

		if (!blogUser.isPresent())
			return false;

		Blogs blog = new Blogs();

		blog.setName(blogName);
		blog.setBody(blogDescription);
		blog.setUser(blogUser.get());

		blogsRepo.save(blog);

		return true;

	}

	public boolean editBlog(Integer blogId, String updatedBlogName, String updatedBlogDescription) {

		Optional<Blogs> optionalBlog = blogsRepo.findById(blogId);

		if (!optionalBlog.isPresent())
			return false;

		Blogs blog = optionalBlog.get();

		blog.setName(updatedBlogName);
		blog.setBody(updatedBlogDescription);

		blogsRepo.save(blog);

		return true;

	}

	public boolean deleteBlog(Integer blogId) {

		Optional<Blogs> optionalBlog = blogsRepo.findById(blogId);
		Blogs blog = optionalBlog.get();
		blog.setUser(null);
		blogsRepo.save(blog);

		return true;
	}

	public Blogs findByBlogId(Integer id) {

		Optional<Blogs> blog = blogsRepo.findById(id);
		if (blog.isPresent())
			return blog.get();
		else
			return null;
	}

	public List<Blogs> fetchAllBlogs(Integer userId) {

		List<Blogs> blogsList = blogsRepo.findByUserUserId(userId);

		return blogsList;
	}

	public Map<String, Long> getWordFrequency(List<Blogs> blogs) {
		Map<String, Long> wordCount = new HashMap<>();
		for (Blogs blog : blogs) {
			String[] words = blog.getBody().split("\\W+");
			for (String word : words) {
				word = word.toLowerCase();
				wordCount.put(word, wordCount.getOrDefault(word, 0L) + 1);
			}
		}
		return wordCount;
	}

}
