package com.smart.controller;


import java.security.SecureRandom;
import java.util.Date;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.services.EmailService;

@Controller
public class HomeController {
	
	//Create Password Encoder object
	BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
	
	//Create UserRepository object
	@Autowired
	private UserRepository userRepository;
	
	//Create EmailService object
	@Autowired
	private EmailService emailService;
	
	//home handler
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	//about handler
	@RequestMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}
	@RequestMapping(value = "/contact-form", method = RequestMethod.POST)
	public String contact(@RequestParam("name")String name,@RequestParam("email")String email,
			@RequestParam("subject")String subject,@RequestParam("message")String message,Model model)
	{
		String from = email;
		
		message="<div style='border:1px solid red; padding:30px;'>"
				+ "<h1 style='border:1px solid red; padding:30px; text-align:center;text-transfrom:capitalize'>Message from smart contact manager</h1><br>"
				+ "<h3>Name : "+name+"</h3><br>"
				+ "<h3>Email : "+from+"</h3><br>"
				+ "<h3>Subject : "+subject+"</h3><br>"
				+ "<h3>Message : "+message+"</h3><br>"
				+ "</div>";
		
		boolean flag= emailService.contactSend(subject, message, from);
		
		if(flag)
		{
			System.out.println("Send Successfully");
			model.addAttribute("message","success");
			return "redirect:/contact-form#contact";
		}
		else
		{
			System.out.println("Message not sent");
			model.addAttribute("message","error");
			return "home";
		}

	}
	
	//sign up handler
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("title", "Signup - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	//handler for registering user
	@RequestMapping(value = "/do-signup", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult reBindingResult,
			Model model,
			HttpSession session)
	{
		try {
		
			if(reBindingResult.hasErrors())
			{
				System.out.println("Error is "+reBindingResult.toString());
				model.addAttribute("user",user);
				return "/signup";
			}
				
			//check the user are exist or not
			String email =user.getEmail();
			User existUser = userRepository.getUserByUserName(email);
			
			if(existUser!=null)
			{
				//send error message
				model.addAttribute("user",user);
				session.setAttribute("message", new Message("User Does Exist With Email Try Another One", "alert-danger"));
				return "redirect:/signup";
			}
			
			
			//setting the user data
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setAbout("i am using smartcontactmanager");
			user.setRegiDate(new Date());
			System.out.println("User is "+user);
			
			
			// create instance of SecureRandom class
	        SecureRandom rand = new SecureRandom();
	        // Generate random integers in range 0 to 999999999
	        int myOtp = rand.nextInt(999999);
	        
	        // Print random integers
	        System.out.println("Random Integers: " + myOtp);
	        
	        //write code here send opt to email
	        String subject="OTP FROM SCM";
			String to = user.getEmail();
			String message="<div style='border:1px solid red; padding:30px;'>"
			+ "<h1>"
			+ "SCM Send Verification Code"
			+ "<Strong style='font-size: 150%; margin-left:30px;'>"+myOtp
			+ "</Strong>"
			+ "</h1>"
			+ "</div>";
			
			//opt send to the user email
			boolean flag = emailService.sendEmail(subject, message, to);
			System.out.println("Send Successfully");	

			//verify user account 
			 if(flag)
		        {
				 	// to return the change password form
		        	session.setAttribute("myotp", myOtp);
		        	session.setAttribute("email", to);
		        	session.setAttribute("user", user);
		        	return "verify_otp_user";
		        }
		        else
		        {
		        	//error
		        	session.setAttribute("message","something went wrong!!");
		        	return "signup";
		        }
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Something went wrong "+e.getMessage(), "alert-danger"));
			return "redirect:/signup";
		}
	}
	
	//verify opt form handler
	@PostMapping("/verify-otp-user")
	public String verifyOtpHandler(@RequestParam("otp") int otp,HttpSession session)
	{
		int myOtp=(int)session.getAttribute("myotp");
		String email=(String)session.getAttribute("email");
		User user =(User)session.getAttribute("user");
		
		if(myOtp==otp)
		{
			//user save in the database
			userRepository.save(user);
			
			//check the user are exits our database
			User existUser = userRepository.getUserByUserName(email);
			
			if(existUser==null)
			{
				//send error message
				session.setAttribute("message","User does not exist with email in our database !!");
	        	return "signup";
			}
			//return login form
			return "redirect:/signin?change=signup successfully done login now";
		}
		else
		{
			session.setAttribute("message","You are entered wrong otp try again");
			return "verify_otp_user";
		}
		
	}
	
	
	//login handler
	@RequestMapping("/signin")
	public String login(Model model)
	{
		model.addAttribute("title", "login - Smart Contact Manager");
		return "login";
	}

	//test handler
		@RequestMapping("/test")
		public String test(Model model)
		{
			model.addAttribute("title", "test - Smart Contact Manager");
			return "test";
		}
	
}
