package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.edu.ifpb.pweb2.projeto.simpleevents.dao.*;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.projeto.simpleevents.service.CustomUserDetails;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/events")
public class EventoController {
    @Autowired
    private EventoDAO dao;
    @Autowired
    private UsuarioDAO userDao;
    @Autowired
    private EspecialidadeDAO especialidadeDao;
    @Autowired
    private VagaDAO vagaDao;
    @Autowired
    private CandidatoDAO candidatoDAO;

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
    public ModelAndView getEvent(@PathVariable("id") Long id, Authentication auth,
                                 @ModelAttribute("success") String success,
                                 @ModelAttribute("error") String error) {
        ModelAndView mav = new ModelAndView("/eventos/showEvent");
        Optional<Evento> evento = dao.findById(id);
        if (evento.isPresent()) {
            Evento e = evento.get();
            mav.addObject("evento", e);
            if (auth != null && auth.isAuthenticated()) {
                Usuario currentUser = getLoggedUser(auth);
                mav.addObject("currentUser", currentUser);;
                if (evento.get().getDono().getUser_id().equals(currentUser.getUser_id())) {
                    mav.setViewName("/eventos/showEventOwner");
                    List<Candidato> candidatos = candidatoDAO.findByVaga_Evento(e);
                    mav.addObject("candidatos", candidatos);
                }
            }
            if (!success.equals("")) {
                mav.addObject("success", true);
            }
            if (!error.equals("")) {
                mav.addObject("error", true);
            }
        }
        return mav;
    }

    @PostMapping("/{id}/{especialidade}")
    public String candidate(@PathVariable("id") Long id, @PathVariable String especialidade,
                            Authentication auth, RedirectAttributes redirectAttributes) {
        Evento evento = dao.getOne(id);
        Especialidade espec = especialidadeDao.findByNomeIgnoreCase(especialidade);
        Vaga vaga = vagaDao.findByEventoAndEspecialidade(evento, espec);
        Candidato currentCandidate = candidatoDAO.findByUsuarioAndVaga(getLoggedUser(auth), vaga);
        if (vaga.getQuantidade() == 0 || currentCandidate != null) {
            redirectAttributes.addFlashAttribute("error", "error");
            return "redirect:/events/" + id;
        }

        Candidato candidato = new Candidato();
        candidato.setAprovacao(Status.NAO_AVALIADO);
        candidato.setUsuario(getLoggedUser(auth));
        candidato.setVaga(vaga);

        vaga.getCandidatos().add(candidato);
        candidatoDAO.save(candidato);

        redirectAttributes.addFlashAttribute("success", "success");
        return "redirect:/events/" + id;
    }

    // MY EVENTS

    @GetMapping("/my-events")
    public ModelAndView getMyEvents(Authentication auth) {
        ModelAndView mav = new ModelAndView("meus-eventos/myEvents");
        Usuario currentUser = getLoggedUser(auth);
        List<Evento> eventos = dao.findAllByDono(currentUser);
        mav.addObject("eventos", eventos);
        return mav;
    }

    public Usuario getLoggedUser(Authentication auth) {
        String userEmail = ((CustomUserDetails) auth.getPrincipal()).getEmail();
        return userDao.findByEmail(userEmail);
    }

    @DeleteMapping("/{id}")
    public String delete(Authentication auth, @PathVariable("id") Long id) {
        String userEmail = ((CustomUserDetails) auth.getPrincipal()).getEmail();
        Usuario currentUser = userDao.findByEmail(userEmail);
        Evento evento = dao.findById(id).get();
        if (evento.getDono() == currentUser)
            dao.deleteById(id);
        return "redirect:/events/my-events";
        // Mensagem de sucesso
    }

    @PutMapping("/{id}")
    public String update(Authentication auth, Evento evento, @PathVariable("id") Long id,
                         @RequestParam("especialidades") List<Long> especialidades,
                         @RequestParam("quantidadevagas") List<Integer> quantidadevagas) {

        Evento event = dao.findById(id).get();
        event.setNome(evento.getNome());
        event.setLocal(evento.getLocal());
        event.setData(evento.getData());
        event.setDescricao(evento.getDescricao());

        int i = 0;
        for (Long idEspecialidade : especialidades) {
            Boolean create = true;
            for (Vaga vaga : event.getVagas()) {
                if (vaga.getEspecialidade().getId() == idEspecialidade) {
                    vaga.setQuantidade(quantidadevagas.get(i));
                    create = false;
                }
            }

            if (create) {
                Especialidade esp = especialidadeDao.findById(idEspecialidade).get();
                Vaga novaVaga = new Vaga();
                novaVaga.setEspecialidade(esp);
                novaVaga.setQuantidade(quantidadevagas.get(i));
                novaVaga.setEvento(event);
                event.addVaga(novaVaga);
            }
            i++;
            create = true;
        }

        dao.save(event);
        return "redirect:/events/my-events";
        // Mensagem de sucesso
    }

    @GetMapping("/my-events/{id}")
    public ModelAndView getMyEvent(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("meus-eventos/showMyEvent");
        Optional<Evento> evento = dao.findById(id);
        mav.addObject("evento", evento.get());
        List<Especialidade> especialidades = especialidadeDao.findAll();
        mav.addObject("especialidades", especialidades);
        return mav;
    }
}