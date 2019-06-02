package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import br.edu.ifpb.pweb2.projeto.simpleevents.dao.EventoDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.dao.UsuarioDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Evento;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Usuario;
import br.edu.ifpb.pweb2.projeto.simpleevents.service.CustomUserDetails;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/events")
public class EventoController {
    @Autowired
    private EventoDAO dao;
    @Autowired
    private UsuarioDAO userDao;

  @RequestMapping()
  public ModelAndView listAll(Authentication auth, Principal principal) {
    System.out.println("OLHA AQUI PORRANS!");
    System.out.println(((CustomUserDetails) auth.getPrincipal()).getEmail());
    ModelAndView mav = new ModelAndView("events");
    List<Evento> eventos = dao.findAll();
    mav.addObject("eventos", eventos);
    return mav;
  }

  @GetMapping("/form")
  public String getForm(Model model) {
      model.addAttribute("evento", new Evento());
      return "addEvent";
  }
  
  @PostMapping
  public String save(Evento evento, Principal principal, Authentication auth) {
      String userEmail = ((CustomUserDetails) auth.getPrincipal()).getEmail();
      Usuario currentUser = userDao.findByEmail(userEmail);
      evento.setDono(currentUser);
	  dao.save(evento);
	  return "redirect:events";
  }
  
  @DeleteMapping("/{id}")
  public String delete(@PathVariable Long id) {
	  dao.deleteById(id);
	  return "redirect:events";
	  //Mensagem de sucesso
  }
  
  @PutMapping("/{id}")
  public String update(@RequestBody Evento evento, @PathVariable Long id) {
	  Optional<Evento> event = dao.findById(id);
	  if(!event.isPresent()) {
		  return "redirect:events";
		  //Mensagem de erro
	  }
	  evento.setId(id);
	  dao.save(evento);
	  return "redirect:events";
	  //Mensagem de sucesso
  }
}