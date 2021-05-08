package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lowagie.text.DocumentException;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.ContactPDFExporter;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	//method adding to common data for all handlers
	@ModelAttribute
	public void addCommonData(Model model, Principal principal)
	{
		String userName = principal.getName();
		
		System.out.println("UserName is "+userName);
		
		//get the user using user name (email)
		User user = userRepository.getUserByUserName(userName);
		
		System.out.println("user is "+user);
		
		model.addAttribute("user",user);
	}

	//user home index
	@RequestMapping("/dashboard")
	public String userDashboard(Model model, Principal principal )
	{
		model.addAttribute("title", "User Dashboard");
		
		User currentUser = userRepository.getUserByUserName(principal.getName());
		
		List<Contact> contacts = currentUser.getContacts();
		
		int clist = contacts.size();
		
		model.addAttribute("clist", clist);
		
		return "normal/user_dashboard";
	}
	
	//open add form handler
	@GetMapping("/add-contact")
	public String openFormHandler(Model model) 
	{
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String contactHandler(@ModelAttribute Contact contact, 
			@RequestParam("profileImage") MultipartFile file,
		    Principal principal, Model model, HttpSession session) 
	{
		
		try {

		
			String name = principal.getName();
			
			//fetching the current user
			User user = userRepository.getUserByUserName(name);
			
			//processing and uploading file...
			
			if(file.isEmpty())
			{
				//if the file is empty then try out message
				System.out.println("File is empty");
				contact.setImage("defaults.png");
			}
			else
			{
				//upload the file to the img folder and update the file name to the contact
				
				SecureRandom secureRandom = new SecureRandom();
				
				int number = secureRandom.nextInt(1000000);//Now it will return random number  
				
				String str=String.valueOf(number);//number change to the string
				
				System.out.println(str);
				
				contact.setImage(file.getOriginalFilename()+str);
				
				File saveFile = new ClassPathResource("static/img").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename()+str);
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				System.out.println("image is uploaded");
				
			}
			
				//set the current user in the contact
				contact.setRegiDate(new Date());
				
				//set the current user in the contact
				contact.setUser(user);
				
				//fetching the contact details and add contacts list
				user.getContacts().add(contact);		
				
				//store contact to the database
				userRepository.save(user);
				
				System.out.println("Data Added to the database");
				
				//success message
				session.setAttribute("message", new Message("Contact is added successfully!! Add more..", "success"));
				
				return "redirect:/user/add-contact";
			
			} catch (Exception e) {
				
				System.out.println("Error is "+e.getMessage());
				
				e.printStackTrace();
				
				//error message
				session.setAttribute("message", new Message("Something went wrong!! Try again..", "danger"));
				
				return "redirect:/user/add-contact";
				
			}
		

		
		
	}
	
	//show contacts
	//per page 5 and current page 0
	@GetMapping("/view-contact/{page}")
	public String showContact(@PathVariable("page") Integer page,Model model, Principal principal)
	{
		model.addAttribute("title", "view user contacts");
		
		//fetching the current user 
		String userName = principal.getName();
		
		User currentUser = userRepository.getUserByUserName(userName);
		
		//page include in the single page
		
		Pageable pageable = PageRequest.of(page, 10);
		
		//fetching the current user by user id and return the list of contacts
		Page<Contact> Contacts = contactRepository.getContactsByUser(currentUser.getId(), pageable);
		
		model.addAttribute("contacts", Contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", Contacts.getTotalPages());
		
		return "normal/show_contacts";
	}

	//showing a single contact details
	@RequestMapping("/{cId}/view-contact")
	public String contactDetailsShow(@PathVariable("cId") int cId, Model model, Principal principal)
	{
		Optional<Contact> optionalContact = contactRepository.findById(cId);
		
		Contact contact = optionalContact.get();
		
		//current user fetching
		String userName = principal.getName();
		
		User currentUser = userRepository.getUserByUserName(userName);
		
		//check the user and our contacts
		if(currentUser.getId()==contact.getUser().getId())
		{
			model.addAttribute("contact", contact);
			model.addAttribute("title",contact.getName());
		}
		

		return "normal/showing_contact_details";
	}
	
	//delete contact
	@GetMapping("/delete/{cid}")
	public String deleteConatct(@PathVariable("cid") Integer cId, Model model, Principal principal, HttpSession session) 
	{
		Optional<Contact> optionalContact = contactRepository.findById(cId);
		
		Contact contact = optionalContact.get();
		
		//current user fetching
		String userName = principal.getName();
		
		User currentUser = userRepository.getUserByUserName(userName);
		
		try {
			//check the user and our contacts
			if(currentUser.getId()==contact.getUser().getId())
			{
				//remove selecting contact
				currentUser.getContacts().remove(contact);
				
				//update user data in the database after selecting contact is remove
				userRepository.save(currentUser);
				
				session.setAttribute("message",new Message("Contact is deleted", "success"));
				
			}
			
			return "redirect:/user/view-contact/0";
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("something went wrong", "danger"));
			return "redirect:/user/view-contact";
		}
		
		
	}

	//open update contact form
	@PostMapping("/update-contact/{cid}")
	public String openContactForm(@PathVariable("cid") Integer cId, Model model)
	{
		model.addAttribute("title", "Update Your Contact");
		
		Contact contact = contactRepository.findById(cId).get();
		
		model.addAttribute("contact", contact);
		
		return "normal/update_contact_form";
	}

	//update new contact handler
	@RequestMapping(value = "/update-contact", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute("contact") Contact contact,
			@RequestParam("profileImage") MultipartFile file,
			Model model,Principal principal,HttpSession session)
	{
		
		try {
			
			//old contact details
			Contact oldcontactDetails = contactRepository.findById(contact.getcId()).get();
			
			
			if(!file.isEmpty())
			{
				//new image uploaded
				
				//delete old image
				File deleteFile = new ClassPathResource("static/img").getFile();
				
				File file1=new File(deleteFile, oldcontactDetails.getImage());
				
				file1.delete();
				
				//update new image
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImage(file.getOriginalFilename());
				
			}else
			{
				contact.setImage(oldcontactDetails.getImage());
			}
			
			User user = userRepository.getUserByUserName(principal.getName());
			
			contact.setUser(user);
			
			contactRepository.save(contact);
			
			session.setAttribute("message", new Message("Your contact is updated..", "success"));
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong..", "danger"));
		}
		
		
		return "redirect:/user/"+contact.getcId()+"/view-contact";
	}
	
	//open your profile
	@GetMapping("/your-profile")
	public String yourProfile(Model model)
	{
		model.addAttribute("title", "Profile");
		
		return "normal/show_your_profile";
	}

	//open setting handler
	@GetMapping("/settings")
	public String settingHandler(Model model)
	{
		model.addAttribute("title","Settings");
		return "normal/settings";
	}
	
	//change password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,
			Principal principal, HttpSession session)
	
	{
		User currentUser = userRepository.getUserByUserName(principal.getName());
		
		if(bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) 
		{
			//change the password
			currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
			
			userRepository.save(currentUser);
			
			session.setAttribute("message", new Message("password has changed successfully", "success"));
			
			return "redirect:/user/settings";
		}
		else
		{
			//error....
			session.setAttribute("message", new Message("please enter correct old password", "danger"));
			return "redirect:/user/settings";
		}
			
	}
	
	//export pdf
	@GetMapping("/export-pdf")
	public void exportToPDF(HttpServletResponse response, Principal principal) throws DocumentException, IOException
	{
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	
		String currentDateTime = dateFormatter.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_"+currentDateTime+".pdf";
		
		response.setHeader(headerKey, headerValue);
		
		String userName = principal.getName();
		System.out.println("UserName is "+userName);
		
		//get the user using user name (email)
		User currentUser = userRepository.getUserByUserName(userName);
		
		 List<Contact> listofcontacts = currentUser.getContacts();
		
		System.out.println("list of contact"+listofcontacts);
		
		ContactPDFExporter exporter = new ContactPDFExporter(listofcontacts);
		
		exporter.export(response);
		
	}

}


