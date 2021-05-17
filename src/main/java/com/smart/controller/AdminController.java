package com.smart.controller;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lowagie.text.DocumentException;
import com.smart.dao.AdminRepository;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Admin;
import com.smart.entities.Contact;
import com.smart.entities.ContactPDFExporter1;
import com.smart.entities.User;
import com.smart.helper.Message;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	

	@RequestMapping("/adminlogin")
	public String adminLogin(Model model)
	{
		model.addAttribute("title", "Admin-login");
		return "admin_login";
	}
	
	@GetMapping("/admin-login")
	public String adminSigning(@RequestParam("username") String username,@RequestParam("password") String password,Model model,HttpServletRequest request)
	{
		try {
			
			Admin adminUser = adminRepository.getUserByUserName(username);
			

			if(adminUser.getEmail().equals(username) && adminUser.getPassword().equals(password) && adminUser.getRole().equals("Admin"))
			{
				model.addAttribute("adminUser", adminUser);
			
				HttpSession s= request.getSession();
				s.setAttribute("admin", adminUser);
				s.setAttribute("message", new Message("You Successfully Logged In", "success"));
				return "redirect:/admin/dashboard";
			}
			else 
			{
				return "redirect:/admin/adminlogin?change=invalid credential try another one";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/admin/adminlogin?change=User Not Found"; 
		}
	}

	@RequestMapping("/dashboard")
	public String dashboard(Admin admin, Model model, HttpSession session)
	{
		admin=(Admin)session.getAttribute("admin");
		System.out.println("admin is "+admin);
		model.addAttribute("adminUser",admin);
		
		long count1 = userRepository.count();
		long count2 = contactRepository.count();
		
		model.addAttribute("count1", count1);
		model.addAttribute("count2", count2);
		

		return "admin/admin_dashboard";
	}
	
	@RequestMapping("/viewuser")
	public String viewUser(Admin admin, Model model, HttpSession session)
	{
		admin=(Admin)session.getAttribute("admin");
		System.out.println("admin is "+admin);
		model.addAttribute("adminUser",admin);
		
		List<User> userList = userRepository.findAll();
		model.addAttribute("userList",userList); 
		
		return "admin/viewuser";
	}
	
	@RequestMapping("/viewusercontacts")
	public String viewUserContacts(Admin admin, Model model, HttpSession session)
	{
		admin=(Admin)session.getAttribute("admin");
		System.out.println("admin is "+admin);
		model.addAttribute("adminUser",admin);
		List<Contact> userContactList =contactRepository.findAll();
		model.addAttribute("userContactList",userContactList); 
		
		return "admin/viewusercontacts";
	}
	
	@RequestMapping("/logout")
	public String Logout(Admin admin, Model model, HttpSession session)
	{
		admin=(Admin)session.getAttribute("admin");
		System.out.println("admin is "+admin);
		session.invalidate();
		
		return "redirect:/admin/adminlogin?logout=You are logged out";
	}

	//user delete handler
	@GetMapping("/delete/{uid}")
	public String deleteUser(@PathVariable("uid") Integer id, Model model,HttpSession session) 
	{
		try {
			userRepository.deleteById(id);
			
			session.setAttribute("message", new Message("Users is deleted", "alert-success"));
			
			return "redirect:/admin/viewuser";
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			session.setAttribute("message", new Message("Something went wrong", "alert-danger"));
			
			return "redirect:/admin/viewuser";
			
		}
		
	}
	
	

	//export pdf
	@GetMapping("/export-pdf")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException
	{
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	
		String currentDateTime = dateFormatter.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_"+currentDateTime+".pdf";
		
		response.setHeader(headerKey, headerValue);
		
		//get the user list using user
		 List<User> listofuser = userRepository.findAll();
		
		ContactPDFExporter1 exporter = new ContactPDFExporter1(listofuser);
		
		exporter.export(response);
		
	}



	
}
