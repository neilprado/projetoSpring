package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.projeto.simpleevents.dao.EspecialidadeDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Especialidade;

@Controller
@RequestMapping("/especialidades")
public class EspecialidadeController {

  @Autowired
  private EspecialidadeDAO dao;

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView home() {
    ModelAndView mav = new ModelAndView("especialidades-list");
    List<Especialidade> especialidades = dao.findAll();
    mav.addObject("especialidades", especialidades);
    return mav;
  }

  @RequestMapping("/form")
  public String form() {
    return "especialidades-form";
  }

  @RequestMapping(method = RequestMethod.POST)
  public String createEspecialidade(Especialidade especialidade) {
    dao.save(especialidade);
    return "redirect:especialidades";
  }
}