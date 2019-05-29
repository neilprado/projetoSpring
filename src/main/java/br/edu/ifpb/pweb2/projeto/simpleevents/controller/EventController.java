package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.projeto.simpleevents.dao.EventoDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Evento;

@Controller
@RequestMapping("/event")
public class EventController {
	@Autowired
	private EventoDAO dao;
	
	
	@RequestMapping("/cadastrar")
	public String form() {
		return "event/form";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String save(Evento evento) {
		dao.save(evento);
		return "redirect:event";
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list(String nomeEvento) {
		ModelAndView model = new ModelAndView("event/list");
		List<Evento> eventos = dao.findAll();
		model.addObject("eventos", eventos);
		return model;
	}

}
