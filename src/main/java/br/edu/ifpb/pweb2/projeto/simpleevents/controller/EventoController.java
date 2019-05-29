package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/events")
public class EventoController {

  @RequestMapping(method = RequestMethod.GET)
  public String home() {
    return "events";
  }
}