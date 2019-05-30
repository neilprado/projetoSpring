package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Evento;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/events")
public class EventoController {

  @RequestMapping(method = RequestMethod.GET)
  public String home() {
    return "events";
  }

  @GetMapping("/form")
  public String getForm( Model model) {
      model.addAttribute("evento", new Evento());
      return "addEvent";
  }
}