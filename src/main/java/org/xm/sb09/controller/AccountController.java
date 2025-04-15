package org.xm.sb09.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.xm.sb09.model.dto.AccountCreationRequest;
import org.xm.sb09.services.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@AllArgsConstructor
@Controller
public class AccountController {
    private final AccountService accountService;
    
    @PostMapping("/register")
    public String doRegistration(@ModelAttribute AccountCreationRequest req) {
        accountService.createAccount(req);   
        return "redirect:login";
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register-new-account";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/logout-page")
    public String logoutPage(HttpServletRequest req, HttpServletResponse res) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(req, res, auth);
        return "logout-page";
    }
    
}
