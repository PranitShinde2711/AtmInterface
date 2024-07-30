package com.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AtmInterface {
    public static void main(String[] args) {
        SpringApplication.run(AtmInterface.class, args);
    }
}



@Controller
class AtmController {
    private static double balance = 1000;
    private Map<String, String> userDatabase = new HashMap<>();

    public AtmController() {
        userDatabase.put("user1", "12345");
        userDatabase.put("user2", "54321");
        userDatabase.put("user3", "23415");
        userDatabase.put("user4", "54123");
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String loginId, @RequestParam String password, Model model) {
        if (authenticateUser(loginId, password)) {
            return "atm";
        } else {
            model.addAttribute("error", "Invalid login credentials");
            return "index";
        }
    }

    @PostMapping("/atm")
    public String atm(@RequestParam String action, @RequestParam(required = false) Double amount, Model model) {
        switch (action) {
            case "checkBalance":
                model.addAttribute("message", "Your balance is: $" + balance);
                break;
            case "deposit":
                if (amount != null && amount > 0) {
                    balance += amount;
                    model.addAttribute("message", "$" + amount + " deposited successfully. New balance is: $" + balance);
                } else {
                    model.addAttribute("message", "Invalid deposit amount.");
                }
                break;
            case "withdraw":
                if (amount != null && amount > 0 && amount <= balance) {
                    balance -= amount;
                    model.addAttribute("message", "$" + amount + " withdrawn successfully. New balance is: $" + balance);
                } else {
                    model.addAttribute("message", "Invalid withdrawal amount or insufficient balance.");
                }
                break;
            case "exit":
                return "redirect:/";
            default:
                model.addAttribute("message", "Invalid choice. Please select a valid option.");
        }
        return "atm";
    }

    private boolean authenticateUser(String loginId, String password) {
        return userDatabase.containsKey(loginId) && userDatabase.get(loginId).equals(password);
    }
}


