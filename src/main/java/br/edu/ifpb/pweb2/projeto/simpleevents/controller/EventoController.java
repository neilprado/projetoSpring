package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.projeto.simpleevents.dao.EspecialidadeDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.dao.EventoDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.dao.UsuarioDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.dao.VagaDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Especialidade;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Evento;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Usuario;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Vaga;
import br.edu.ifpb.pweb2.projeto.simpleevents.service.CustomUserDetails;

@Controller
@RequestMapping("/events")
public class EventoController {
    @Autowired
    private EventoDAO eventoDao;
    @Autowired
    private UsuarioDAO userDao;
    @Autowired
    private EspecialidadeDAO especialidadeDao;
    @Autowired
    private VagaDAO vagaDao;

    @RequestMapping()
    public ModelAndView listAll(String inputSearch) {
        ModelAndView mav = new ModelAndView("eventos/events");
        List<Evento> eventos;
        if (inputSearch != null) {
            eventos = dao.findByNomeContainingIgnoreCase(inputSearch);
            if (eventos.size() == 0) {
                Especialidade esp = especialidadeDao.findByNomeIgnoreCase(inputSearch);
                Vaga vaga = vagaDao.findByEspecialidade(esp);
                eventos = dao.findAllByVagas(vaga);
            }
        } else {
            eventos = dao.findAll();
        }
        mav.addObject("eventos", eventos);
        return mav;
    }

    @GetMapping("/form")
    public String getForm(Model model) {
        model.addAttribute("evento", new Evento());
        List<Especialidade> especialidades = especialidadeDao.findAll();
        model.addAttribute("especialidades", especialidades);
        return "eventos/addEvent";
    }

    @PostMapping
    public String save(Evento evento, Authentication auth, @RequestParam("especialidades") List<Long> especialidades,
            @RequestParam("quantidadevagas") List<Integer> quantidadevagas) {
        Optional<Especialidade> esp;
        int i = 0;
        for (Long id : especialidades) {
            esp = especialidadeDao.findById(id);
            Vaga vaga = new Vaga();
            vaga.setEspecialidade(esp.get());
            vaga.setQuantidade(quantidadevagas.get(i));
            vaga.setEvento(evento);
            evento.addVaga(vaga);
            i++;
        }
        String userEmail = ((CustomUserDetails) auth.getPrincipal()).getEmail();
        Usuario currentUser = userDao.findByEmail(userEmail);
        evento.setDono(currentUser);
        dao.save(evento);
        return "redirect:events";
    }

    @GetMapping("/{id}")
    public ModelAndView getEvent(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("eventos/showEvent");
        Optional<Evento> evento = dao.findById(id);
        mav.addObject("evento", evento.get());
        return mav;
    }

    // MY EVENTS

    @GetMapping("/my-events")
    public ModelAndView getMyEvents(Authentication auth) {
        ModelAndView mav = new ModelAndView("meus-eventos/myEvents");
        String userEmail = ((CustomUserDetails) auth.getPrincipal()).getEmail();
        Usuario currentUser = userDao.findByEmail(userEmail);
        List<Evento> eventos = dao.findAllByDono(currentUser);
        mav.addObject("eventos", eventos);
        return mav;
    }

    @DeleteMapping("/{id}")
    public String delete(Authentication auth, @PathVariable Long id) {
        String userEmail = ((CustomUserDetails) auth.getPrincipal()).getEmail();
        Usuario currentUser = userDao.findByEmail(userEmail);
        Evento evento = dao.findById(id).get();
        if (evento.getDono() == currentUser)
            dao.deleteById(id);
        return "redirect:events";
        // Mensagem de sucesso
    }

    @PutMapping("/{id}")
    public String update(@RequestBody Evento evento, @PathVariable Long id) {
        Optional<Evento> event = dao.findById(id);
        if (!event.isPresent()) {
            return "redirect:events";
            // Mensagem de erro
        }
        evento.setId(id);
        dao.save(evento);
        return "redirect:events";
        // Mensagem de sucesso
    }
}