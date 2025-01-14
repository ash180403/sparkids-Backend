package com.Cascade.Form.Controller;

import com.Cascade.Form.Repository.ContactRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.Cascade.Form.entity.User;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "User APIs", description = "Add, Read, Update & Delete")
public class UserController {

    @Autowired
    private ContactRepository contactRepository;


    @PostMapping("/submitForm")
    public String submitForm(@ModelAttribute User user, Model model) {

       
        Optional<User> existingContact = contactRepository.findByNameAndContactAndAddress(user.getName(), user.getContact(), user.getAddress());

        if (existingContact.isPresent()) {
        	model.addAttribute("errorMessage", "This contact already exists in the database.");
            model.addAttribute("user", user);  
            return "form";
        } else {
            
            contactRepository.save(user);  
            return "redirect:/contactform";  
        }	
    }
    @PostMapping("/email")
    public ResponseEntity<String> saveEmail(@RequestParam("Email") String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        if (contactRepository.existsByEmail(email)) {
            return ResponseEntity.status(409).body("Email already exists in the database");
        }
        User user = new User();
        
        user.setEmail(email);
        contactRepository.save(user);

        return ResponseEntity.ok("Email saved successfully");
    }
    
    @GetMapping("/admin/email")
    public ResponseEntity<List<User>> getAllEmails() {
        List<User> users = contactRepository.findAllByEmailIsNotNull();
        return ResponseEntity.ok(users);
    }
    
    @PostMapping("/api/contact")
    public ResponseEntity<String> savePhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return ResponseEntity.badRequest().body("Phone number is required");
        }

        if (contactRepository.existsByContact(phoneNumber)) {
            return ResponseEntity.status(409).body("Phone number already exists in the database");
        }

        User contact = new User();
        contact.setContact(phoneNumber);

        contactRepository.save(contact);

        return ResponseEntity.ok("Phone number saved successfully");
    }
    @GetMapping("/admin/phone")
    public ResponseEntity<List<User>> getAllPhoneNumbers() {
        List<User> users = contactRepository.findAllByContactIsNotNull();
        return ResponseEntity.ok(users);
    }

    
    @PostMapping("/contact")
    public ResponseEntity<String> saveContactForm(
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String contact,
            @RequestParam("email") String email,
            @RequestParam("reason") String reason,
            @RequestParam("message") String message) {

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        if (contactRepository.existsByEmail(email)) {
            return ResponseEntity.status(409).body("Email already exists in the database");
        }

        User contactForm = new User(fullName, contact, email, reason, message);

        contactRepository.save(contactForm);

        return ResponseEntity.ok("Contact form submitted successfully");
    }
    @GetMapping("/admin/contact")
    public ResponseEntity<String> getAllContactForms() {
        List<User> users = contactRepository.findAll();
        
        StringBuilder response = new StringBuilder();
        
        for (User user : users) {
            response.append("fullName=").append(user.getName()).append("\n");
            response.append("phone=").append(user.getContact()).append("\n");
            response.append("email=").append(user.getEmail()).append("\n");
            response.append("reason=").append(user.getReason()).append("\n");
            response.append("message=").append(user.getMessage()).append("\n");
            response.append("-----------------------------\n"); 
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(response.toString());
    }
//    @GetMapping("/contact")
//    public ResponseEntity<List<ContactForm>> getAllContactForms() {
//        List<ContactForm> contactForms = contactFormRepository.findAll();
//        return ResponseEntity.ok(contactForms);
//    }

    
    @PostMapping("/job-application")
    public ResponseEntity<String> saveJobApplication(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("currentCTC") String currentCTC,
            @RequestParam("expectedCTC") String expectedCTC,
            @RequestParam("noticePeriod") String noticePeriod,
            @RequestParam("resume") MultipartFile resume,
            @RequestParam(value = "portfolioLink", required = false) String portfolioLink) {

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        if (contactRepository.existsByEmail(email)) {
            return ResponseEntity.status(409).body("Email already exists in the database");
        }

        byte[] resumeBytes = null;
        try {
            resumeBytes = resume.getBytes();
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to process the resume file. Please try again.");
        }

        User jobApplication = new User();
        jobApplication.setName(fullName);
        jobApplication.setEmail(email);
        jobApplication.setContact(phoneNumber);
        jobApplication.setCurrentCTC(currentCTC);
        jobApplication.setExpectedCTC(expectedCTC);
        jobApplication.setNoticePeriod(noticePeriod);
        jobApplication.setResume(resumeBytes);
        jobApplication.setPortfolioLink(portfolioLink);

        contactRepository.save(jobApplication);

        return ResponseEntity.ok("Job application submitted successfully");
    }
    @GetMapping("/admin/job-application")
    public ResponseEntity<String> getAllJobApplications() {
        List<User> users = contactRepository.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.status(404).body("No job applications found.");
        }

        StringBuilder response = new StringBuilder();

        for (User user : users) {
            response.append("fullName=").append(user.getName()).append("\n");
            response.append("email=").append(user.getEmail()).append("\n");
            response.append("phoneNumber=").append(user.getContact()).append("\n");
            response.append("currentCTC=").append(user.getCurrentCTC()).append("\n");
            response.append("expectedCTC=").append(user.getExpectedCTC()).append("\n");
            response.append("noticePeriod=").append(user.getNoticePeriod()).append("\n");
            response.append("portfolioLink=").append(user.getPortfolioLink() != null ? user.getPortfolioLink() : "N/A").append("\n");
            response.append("-----------------------------\n");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(response.toString());
    }


    
    @GetMapping("admin/job-application/resume/{email}")
    public ResponseEntity<byte[]> getResumeByEmail(@PathVariable("email") String email) {
        Optional<User> userOptional = contactRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            byte[] resumeData = user.getResume();

            if (resumeData != null) {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=resume.pdf")
                        .body(resumeData);
            } else {
                return ResponseEntity.status(404).body(null); // No resume data found
            }
        } else {
            return ResponseEntity.status(404).body(null); // User with the given email not found
        }
    }


}
