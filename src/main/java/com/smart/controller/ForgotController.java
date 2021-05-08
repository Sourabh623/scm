package com.smart.controller;

import java.security.SecureRandom;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.services.EmailService;

@Controller
public class ForgotController {
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//email id open form handler
	@RequestMapping("/forgot")
	public String openEmailFormHandler(Model model)
	{
		model.addAttribute("title", "Forgot Password");
		return "forgot_email_form";
	}
	
	@RequestMapping(value = "/send-otp" , method = RequestMethod.POST)
	public String sendOtpHandler(@RequestParam("email") String email, HttpSession session)
	{
		System.out.println("email is "+email);
		
		//check the user are exits our database
		User existUser = userRepository.getUserByUserName(email);
		
		if(existUser==null)
		{
			//send error message
			session.setAttribute("message","User does not exist with email !!");
        	return "forgot_email_form";
		}
		
		// create instance of SecureRandom class
        SecureRandom rand = new SecureRandom();
        // Generate random integers in range 0 to 99999
        int otp = rand.nextInt(999999);
        
        // Print random integers
        System.out.println("Random Integers: " + otp);
        
        //write code here send opt to email
        String subject="OTP FROM SCM";
        
        String message="<div style='border:1px solid red; padding:30px;'>"
        		+ "<h1>"
        		+ "OTP IS "
        		+ "<Strong style='font-size: 150%; margin-left:30px;'>"+otp
        		+ "</Strong>"
        		+ "</h1>"
        		+ "</div>";
        
        //set the email which to send 
        String to=email;
        
        boolean flag = emailService.sendEmail(subject, message, to);
        
        if(flag)
        {
        	// to return the change password form
        	session.setAttribute("myotp", otp);
        	session.setAttribute("email", email);
        	return "verify_otp_form";
        }
        else
        {
        	//error
        	session.setAttribute("message","Please check your email id!!");
        	return "forgot_email_form";
        }
		
		
	}
	
	//verify opt form handler
	@PostMapping("/verify-otp")
	public String verifyOtpHandler(@RequestParam("otp") int otp,HttpSession session)
	{
		int myOtp=(int)session.getAttribute("myotp");
		String email=(String)session.getAttribute("email");
		
		if(myOtp==otp)
		{
			//check the user are exits our database
			User existUser = userRepository.getUserByUserName(email);
			
			if(existUser==null)
			{
				//send error message
				session.setAttribute("message","User does not exist with email !!");
	        	return "forgot_email_form";
			}
			//return the password change form
			return "password_change_form";
		}
		else
		{
			session.setAttribute("message","You are entered wrong otp try again");
			return "verify_otp_form";
		}
		
		
	}
	
	//change old password
	@PostMapping("/change-old-password")
	public String changeOldPasswordHandler(@RequestParam("newpassword") String newpassword, HttpSession session)
	{
		String email=(String)session.getAttribute("email");
		
	    User existUser = userRepository.getUserByUserName(email);
	    
	    existUser.setPassword(bCryptPasswordEncoder.encode(newpassword));
	    
	    userRepository.save(existUser);
		
		return "redirect:/signin?change=password successfully changed..";
	}
	

}
