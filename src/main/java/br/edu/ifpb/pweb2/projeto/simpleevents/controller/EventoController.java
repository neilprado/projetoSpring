package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import br.edu.ifpb.pweb2.projeto.simpleevents.dao.EventoDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Evento;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/events")
public class EventoController {
	private EventoDAO dao;

  @RequestMapping()
  public String home() {
    return "events";
  }

  @GetMapping("/form")
  public String getForm(Model model) {
      model.addAttribute("evento", new Evento());
      return "addEvent";
  }
  
  @RequestMapping(method = RequestMethod.POST)
  public String save(Evento evento) {
	  /* 
	   * Precisa saber como pegar o usuário corrente
	   * evento.setDono(Usuario corrente da sessão);
	   */
	  dao.save(evento);
	  return "redirect:events";
  }
  
  @RequestMapping(method = RequestMethod.DELETE)
  public String delete(Long id) {
	  dao.deleteById(id);
	  return "redirect:events";
  }
}