package br.luiztoni.demo.thymeleaf.infra.controllers;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.luiztoni.demo.thymeleaf.domain.product.ProductRepository;
import br.luiztoni.demo.thymeleaf.domain.user.User;
import br.luiztoni.demo.thymeleaf.domain.user.UserRepository;
import br.luiztoni.demo.thymeleaf.domain.user.UserService;

import br.luiztoni.demo.thymeleaf.domain.util.CpfValidator;

@Controller
@RequestMapping
public class AuthController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository repository;
    
    private static final List<String> COURSES = List.of("Computer Science", "Systems Information"); 
 
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
 
    @GetMapping({"/", "/index"})
    public String main() {
        return "redirect:login";
    }
   
    @GetMapping("/login")
    public String login() {
        if (isAuthenticated()) {
            return "redirect:home";
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("courses", COURSES);
        model.addAttribute("user", user);
        return "register";
    }


    @PostMapping("/register")
    public String register(@Validated @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        int age = Period.between(user.getBirth(), LocalDate.now()).getYears();
        user.setAge(age);
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "There is already an account registered with the same email");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Password not match");
        }
        if (result.hasErrors()) {
            return null;
        }
        userService.save(user);

        return "redirect:/register?success";
    }

    @GetMapping("/validate/cpf/{cpf}")
    public ResponseEntity<String> validateCpf(@PathVariable String cpf) {
        System.out.println("CPF para validar: "+cpf);
        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        cpf = cpf.trim();
        if (CpfValidator.isCpf(cpf)) {
            System.out.println("Valid CPF!");
            return new ResponseEntity<String>("ok", HttpStatus.OK);
        }
        System.out.println("Invalid CPF!");
        return new ResponseEntity<String>("invalid", HttpStatus.OK);
    }
}
