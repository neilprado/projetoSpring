package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import br.edu.ifpb.pweb2.projeto.simpleevents.dao.RoleDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.dao.UsuarioDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Role;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
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
    public String signUp(Model model, @ModelAttribute("check_user") Usuario check_user) {
        if (check_user.getEmail() != null) {
            model.addAttribute("usuario", check_user);
            model.addAttribute("erro_email", true);
        } else {
          model.addAttribute("usuario", new Usuario());
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String addUser(@ModelAttribute(value = "usuario") Usuario user,
                          @ModelAttribute(value = "password2") String pass2,
                          RedirectAttributes redirectAttributes) {
        Usuario check_user = userRepository.findByEmail(user.getEmail());
        if (check_user != null) {
            redirectAttributes.addFlashAttribute("check_user", user);
            return "redirect:/signup";
        }
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

    @GetMapping("/home")
    public String index() {
        return "index";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:index";
    }

    @PutMapping("/{id}")
    public String update(Authentication auth, Usuario usuario, @PathVariable("id") Long id, @ModelAttribute(value="password2") String pass2) {
  	  Usuario user = userRepository.findById(id).get();
  	  user.setNome(usuario.getNome());
  	  user.setDataNascimento(usuario.getDataNascimento());
  	  user.setTelefone(usuario.getTelefone());
  	  user.setEmail(usuario.getEmail());
  	  
  	  String pwd = usuario.getPassword();
  	  String encryptPwd = passwordEncoder.encode(pwd);
  	  usuario.setPassword(encryptPwd);
  	  
  	  userRepository.save(usuario);
  	  return "redirect:index";
    }
    
    @GetMapping("/atualizar")
    public ModelAndView getMyUser(@ModelAttribute(value="usuario") Usuario user, String email) {
  	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  	  ModelAndView mav = new ModelAndView("update");
  	  user = userRepository.findByEmail(email);
  	  mav.addObject("usuario", user);
  	  return mav;
    }
}