package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

  @GetMapping("/list")
  public ModelAndView home() {
    ModelAndView mav = new ModelAndView("especialidades/list");
    List<Especialidade> especialidades = dao.findAll();
    mav.addObject("especialidades", especialidades);
    return mav;
  }

  @RequestMapping("/form")
  public String form() {
    return "especialidades/form";
  }

  @RequestMapping(method = RequestMethod.POST)
  public String createEspecialidade(Especialidade especialidade) {
    dao.save(especialidade);
    return "redirect:/especialidades/list";
  }
  
  @DeleteMapping("/{id}")
  public String delete(@PathVariable Long id) {
	  dao.deleteById(id);
	  return "redirect:/especialidades/list";
	  //Exibir mensagem de sucesso
  }
  
  @PutMapping("/{id}")
  public String update(Authentication auth, Especialidade especialidade, @PathVariable("id") Long id) {
	 Especialidade specialist = dao.findById(id).get();
	 specialist.setNome(especialidade.getNome());
	 specialist.setDescricao(especialidade.getDescricao());
	 dao.save(especialidade);
	 return "redirect:/especialidades/list";
  }
  
  @GetMapping("/especialidades/{id}")
  public ModelAndView getMyEspecialidade(@PathVariable("id") Long id) {
	  ModelAndView mav = new ModelAndView("especialidades/mySpecialty");
	  Optional<Especialidade> especialidade = dao.findById(id);
	  mav.addObject("especialidade", especialidade.get());
	  return mav;
  }
}