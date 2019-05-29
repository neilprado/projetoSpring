package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/especialidades")
public class EspecialidadeController {

  @RequestMapping(method = RequestMethod.GET)
  public String home() {
    return "especialidades-form";
  }
}