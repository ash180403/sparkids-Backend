package com.Cascade.Form;

import com.Cascade.Form.Repository.ContactRepository;
import com.Cascade.Form.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;
import java.util.Scanner;
import java.util.List;


@SpringBootApplication
public class FormApplication implements CommandLineRunner {

    @Autowired
    private ContactRepository contactRepository;

    public static void main(String[] args) {
        SpringApplication.run(FormApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nChoose an option: ");
            System.out.println("1. Create User");
            System.out.println("2. Read User by Name");
            System.out.println("3. Update User by Name");
            System.out.println("4. Delete User by Name");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1 -> createUser(scanner);
                case 2 -> readUser(scanner);
                case 3 -> updateUser(scanner);
                case 4 -> deleteUser(scanner);
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void createUser(Scanner scanner) {
        System.out.println("Enter user details:");

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Contact: ");
        String contact = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("reason: ");
        String reason = scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setContact(contact);
        user.setAddress(address);
        user.setReason(reason);

        contactRepository.save(user);
        System.out.println("User created successfully.");
    }
    
    private void readUser(Scanner scanner) {
        System.out.print("Enter User Name to read: ");
        String name = scanner.nextLine();

                List<User> users = contactRepository.findByName(name);

        if (!users.isEmpty()) {
            System.out.println("User Details:");
            for (User user : users) {
                System.out.println("Name: " + user.getName());
                System.out.println("Contact: " + user.getContact());
                System.out.println("Address: " + user.getAddress());
                System.out.println("reason: " + user.getReason());
                System.out.println("-------------------");
            }
        } else {
            System.out.println("User not found.");
        }
    }



    private void updateUser(Scanner scanner) {
        System.out.print("Enter User Name to update: ");
        String name = scanner.nextLine();
        List<User> users = contactRepository.findByName(name);

        if (!users.isEmpty()) {
            System.out.println("Multiple users found. Updating the first match.");
            User user = users.get(0); // Handle the first user for simplicity

            System.out.print("Enter new contact (current: " + user.getContact() + "): ");
            String contact = scanner.nextLine();
            if (!contact.isBlank()) user.setContact(contact);

            System.out.print("Enter new address (current: " + user.getAddress() + "): ");
            String address = scanner.nextLine();
            if (!address.isBlank()) user.setAddress(address);

            System.out.print("Enter new reason (current: " + user.getReason() + "): ");
            String reason = scanner.nextLine();
            if (!reason.isBlank()) user.setReason(reason);

            contactRepository.save(user);
            System.out.println("User updated successfully.");
        } else {
            System.out.println("No users found with the given name.");
        }
    }

    private void deleteUser(Scanner scanner) {
        System.out.print("Enter User Name to delete: ");
        String name = scanner.nextLine();
        if (contactRepository.existsByName(name)) {
            contactRepository.deleteByName(name);
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    @Configuration
public static class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") 
                .allowedOrigins("http://localhost:3000") 
                .allowedMethods("GET", "POST", "PUT", "DELETE") 
                .allowCredentials(true);
    }
}
}
