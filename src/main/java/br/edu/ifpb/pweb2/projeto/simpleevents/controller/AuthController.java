package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import br.edu.ifpb.pweb2.projeto.simpleevents.dao.RoleDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.dao.UsuarioDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Role;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Controller
public class AuthController {

  @Autowired
  private UsuarioDAO userRepository;

  @Autowired
  private RoleDAO roleRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/login")
  public String login(HttpServletRequest request) {
    return "login";
  }

  @GetMapping("/signup")
  public String signUp(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "signup";
  }

  @PostMapping("/signup")
  public String addUser(@ModelAttribute(value = "usuario") Usuario user, @ModelAttribute(value = "password2") String pass2) {
    String pwd = user.getPassword();
    String encryptPwd = passwordEncoder.encode(pwd);
    user.setPassword(encryptPwd);
    Role role = roleRepository.findByRole("USER");
    if (role == null) {
      role = new Role();
      role.setRole("USER");
      roleRepository.save(role);
    }
    Set<Role> roles = new HashSet<>();
    roles.add(role);
    user.setRoles(roles);
    userRepository.save(user);
    return "login";
  }

  @GetMapping
  public String index() {return "index";}
  
  @DeleteMapping("/user/{id}")
  public String delete(@PathVariable Long id) {
	 userRepository.deleteById(id);
	 return "redirect:index";
  }
  
  


}