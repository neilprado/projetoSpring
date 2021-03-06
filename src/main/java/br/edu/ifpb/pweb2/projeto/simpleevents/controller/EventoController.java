package br.edu.ifpb.pweb2.projeto.simpleevents.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.edu.ifpb.pweb2.projeto.simpleevents.dao.*;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Autowired
    private AvaliacaoDAO avaliacaoDAO;

    @RequestMapping()
    public ModelAndView listAll(String inputSearch) {
        ModelAndView mav = new ModelAndView("eventos/events");
        List<Evento> eventos;
        if (inputSearch != null) {
            eventos = dao.findByNomeContainingIgnoreCase(inputSearch);
            if (eventos.size() == 0) {
                Especialidade esp = especialidadeDao.findByNomeIgnoreCase(inputSearch);
                List<Vaga> vagas = vagaDao.findByEspecialidade(esp);
                List<Evento> eventosVagas;
                for (Vaga vaga : vagas) {
                    eventosVagas = dao.findAllByVagas(vaga);
                    for (Evento e : eventosVagas) {
                        eventos.add(e);
                    }
                }
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
        return "redirect:/events/my-events";
    }

    @GetMapping("/{id}")
    public ModelAndView getEvent(@PathVariable("id") Long id, Authentication auth,
            @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        ModelAndView mav = new ModelAndView("/eventos/showEvent");
        Optional<Evento> evento = dao.findById(id);
        if (evento.isPresent()) {
            Evento e = evento.get();
            mav.addObject("evento", e);
            List<Candidato> selecionados = candidatoDAO.findByVaga_EventoAndAprovacao(e, Status.APROVADO);
            if (auth != null && auth.isAuthenticated()) {
                Usuario currentUser = getLoggedUser(auth);
                mav.addObject("currentUser", currentUser);
                if (evento.get().getDono().getUser_id().equals(currentUser.getUser_id())) {
                    mav.setViewName("/eventos/showEventOwner");
                    List<Candidato> candidatos = candidatoDAO.findByVaga_EventoAndAprovacao(e, Status.NAO_AVALIADO);
                    mav.addObject("candidatos", candidatos);
                    mav.addObject("selecionados", selecionados);
                }
                for (Candidato selecionado : selecionados) {
                    if (selecionado.getUsuario().getUser_id().equals(currentUser.getUser_id())) {
                        mav.addObject("avaliar", true);
                    }
                }
            }
            if (!success.equals("")) {
                mav.addObject("success", true);
            }
            if (!error.equals("")) {
                mav.addObject("error", true);
            }
            if (e.isFinalizado()) {
                Float result = 0f;
                for (Avaliacao a : e.getAvaliacao()) {
                    result += a.getNotaEvento();
                }
                result = result / e.getAvaliacao().size();
                mav.addObject("notaEvento", result);
            }
        }
        return mav;
    }

    @PostMapping("/candidate/confirm/{id}")
    public ModelAndView confimCandidate(@PathVariable("id") Long id, Authentication auth){
        ModelAndView mav  = new ModelAndView("/");
        Optional<Candidato> candidatoCheck = candidatoDAO.findById(id);
        Usuario usuario = getLoggedUser(auth);
        if (candidatoCheck.isPresent()){
            Candidato candidato = candidatoCheck.get();
            int qtVagas = candidato.getVaga().getQuantidade();
            if(candidato.getVaga().getEvento().getDono().getUser_id().equals(usuario.getUser_id()) && qtVagas != 0) {
                List<Candidato> aplicacoes = candidatoDAO
                        .findByUsuarioAndVaga_Evento(candidato.getUsuario(), candidato.getVaga().getEvento());
                for (Candidato aplicacao : aplicacoes){
                    if (!aplicacao.getId().equals(candidato.getId())
                            && aplicacao.getAprovacao() == Status.NAO_AVALIADO) {
                        aplicacao.setAprovacao(Status.NAO_APROVADO);
                        candidatoDAO.save(aplicacao);
                    }
                }
                candidato.setAprovacao(Status.APROVADO);
                candidato.getVaga().diminuirQuantidade();
                candidatoDAO.save(candidato);
                mav = new ModelAndView("redirect:/events/" + candidato.getVaga().getEvento().getId() + "#solicitacao");
            }
        } 
        return mav;
    }

    @PostMapping("/candidate/deny/{id}")
    public ModelAndView denyCandidate(@PathVariable("id") Long id, Authentication auth){
        ModelAndView mav  = new ModelAndView("/");
        Optional<Candidato> candidatoCheck = candidatoDAO.findById(id);
        Usuario usuario = getLoggedUser(auth);
        if (candidatoCheck.isPresent()){
            Candidato candidato = candidatoCheck.get();
            if(candidato.getVaga().getEvento().getDono().getUser_id().equals(usuario.getUser_id())) {
                candidato.setAprovacao(Status.NAO_APROVADO);
                candidatoDAO.save(candidato);
                mav = new ModelAndView("redirect:/events/" + candidato.getVaga().getEvento().getId() + "#solicitacao");
            }
        } 
        return mav;
    }

    @PostMapping("/{id}/{especialidade}")
    public String candidate(@PathVariable("id") Long id, @PathVariable String especialidade, Authentication auth,
            RedirectAttributes redirectAttributes) {
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

    @GetMapping("/applications")
    public ModelAndView userApplications(Authentication auth){
        ModelAndView mav = new ModelAndView("meus-eventos/userApplications");
        Usuario usuario = getLoggedUser(auth);
        List<Candidato> aplicacoes = candidatoDAO.findByUsuario(usuario);
        mav.addObject("aplicacoes", aplicacoes);
        return mav;
    }

    @PostMapping("/applications/cancel/{id}")
    public ModelAndView cancelApplication(@PathVariable("id") Long id, Authentication auth){
        ModelAndView mav = new ModelAndView("redirect:/events/applications");
        Usuario usuario = getLoggedUser(auth);
        Optional<Candidato> checkCandidate = candidatoDAO.findById(id);
        if (checkCandidate.isPresent()) {
            Candidato candidato = checkCandidate.get();
            if (candidato.getUsuario().getUser_id().equals(usuario.getUser_id())) {
                if (candidato.getAprovacao() == Status.APROVADO) {
                    candidato.getVaga().aumentarQuantidade();
                }
                candidato.setAprovacao(Status.CANCELADO);
                candidatoDAO.save(candidato);
            }
        }
        return mav;
    }

    @GetMapping("/avaliar/{id}")
    public ModelAndView avaliarEventoForm(@PathVariable("id") Long id, Authentication auth) {
        Evento evento = dao.findById(id).get();
        List<Candidato> selecionados = candidatoDAO.findByVaga_EventoAndAprovacao(evento, Status.APROVADO);
        ModelAndView mav = new ModelAndView("eventos/avaliar");
        Usuario currentUser = getLoggedUser(auth);
        Avaliacao avaliacao = new Avaliacao();
        mav.addObject("avaliacao", avaliacao);
        mav.addObject("idEvento", id);
        for (Candidato selecionado : selecionados) {
            if (selecionado.getUsuario().getUser_id().equals(currentUser.getUser_id())) {
                return mav;
            }
        }
        mav.setViewName("redirect:/events");
        return mav;
    }

    @PostMapping("/avaliar/{id}")
    public ModelAndView avaliarEvento(@PathVariable("id") Long id, Avaliacao avaliacao, Authentication auth, RedirectAttributes attr) {
        ModelAndView mav = new ModelAndView("redirect:/events");

        if (avaliacao.getNotaEvento() < 1 || avaliacao.getNotaEvento() > 10) {
            mav.setViewName("redirect:/events/avaliar/" + id);
            attr.addFlashAttribute("error", "A nota do evento deve ser entre 1 e 10.");
        }
        
        Evento evento = dao.findById(id).get();
        Usuario usuario = getLoggedUser(auth);
        avaliacao.setUsuario(usuario);
        avaliacao.setEvento(evento);
        avaliacaoDAO.save(avaliacao);
        return mav;
    }

    @GetMapping("/candidate/{id}")
    public ModelAndView candidateRating(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("eventos/candidateRating");
        Optional<Candidato> checkCandidate = candidatoDAO.findById(id);
        if (checkCandidate.isPresent()){
            Candidato candidato = checkCandidate.get();
            mav.addObject("candidato", candidato);
            if (candidato.getNota() != 0){
                mav.addObject("rated", "rated");
            }
        }
        return mav;
    }

    @PostMapping("/candidato/avaliar/{id}")
    public ModelAndView avaliarCandidato(@PathVariable("id") Long id, @RequestParam("nota") int nota) {        
        ModelAndView mav = new ModelAndView("redirect:/home");
        
        Optional<Candidato> checkCandidate = candidatoDAO.findById(id);
        if (checkCandidate.isPresent()){
            Candidato candidato = checkCandidate.get();
            mav.setViewName("redirect:/events/" + candidato.getVaga().getEvento().getId() + "#selecionados");
            candidato.setNota(nota);
            candidatoDAO.save(candidato);
        }
        return mav;        
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

    @PostMapping("/my-events/finished/{id}")
    public String finalizarEvento(@PathVariable("id") Long id, Authentication auth) {
        Usuario currentUser = getLoggedUser(auth);
        Evento evento = dao.findById(id).get();
        if (currentUser.getUser_id().equals(evento.getDono().getUser_id())) {
            dao.updateEvento(evento.getId(), true);
        }
        return "redirect:/events/my-events";
    }

    @DeleteMapping("/{id}")
    public String delete(Authentication auth, @PathVariable("id") Long id) {
        Usuario currentUser = getLoggedUser(auth);
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
                if (vaga.getEspecialidade().getId().equals(idEspecialidade)) {
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
        ArrayList<Especialidade> check = new ArrayList<>();
        ArrayList<Especialidade> all = (ArrayList<Especialidade>) especialidadeDao.findAll();
        for (Especialidade esp : all) {
            if (!especialidades.contains(esp.getId())) {
                check.add(esp);
            }
        }
        for (Especialidade esp : check) {
            for (Vaga vaga : event.getVagas()) {
                if (esp.getId().equals(vaga.getEspecialidade().getId())) {
                    vaga.setEvento(null);
                }
            }
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
