package com.sherold.authentication.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sherold.authentication.models.User;
import com.sherold.authentication.services.UserService;

@Controller
public class UserController {
	// <----- Attributes ----->
	// dependency injection
	private final UserService userService;
    
	// <----- Constructors ----->
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    // <----- Methods ----->
    // GET Methods
    // route for registration view
    @RequestMapping("/registration")
    public String registerForm(@ModelAttribute("user") User user) {
        return "registrationPage.jsp";
    }
    
    // route for login view
    @RequestMapping("/login")
    public String login() {
        return "loginPage.jsp";
    }
    
    
    @RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
    	// if errors return form
    	if(!user.getPassword().equals(user.getPasswordConfirmation())) {
    		// if confirm password doesn't match, add error
    		ObjectError error = new ObjectError("confirmPassword","Passwords do not match.");
    		result.addError(error);;
    	}
    	if(result.hasErrors()) {
    		return "registrationPage.jsp";
    	} else {
    		// register user, redirect to home route
    		userService.registerUser(user);
    		session.setAttribute("useremail", user.getEmail());
    		return "redirect:/home";
    	}
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
        // authenticates user -- if true, add user to session and route to home
    	if(userService.authenticateUser(email, password)) {
    		session.setAttribute("useremail", email);
    		return "redirect:/home";
    	} else {
    		// else create error and return page
    		model.addAttribute("error", "The username / password combination is not valid! Please try again.");
    		return "loginPage.jsp";
    	}
    }
    
    @RequestMapping("/home")
    public String home(HttpSession session, Model model) {
        // get user from session, save them in the model and return the home page
    	model.addAttribute("user", userService.findByEmail((String)session.getAttribute("useremail")));
    	return "HomePage.jsp";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
