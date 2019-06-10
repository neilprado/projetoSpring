package br.edu.ifpb.pweb2.projeto.simpleevents.config;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Usuario;
import br.edu.ifpb.pweb2.projeto.simpleevents.service.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class RefererRedirectionAuthenticationSuccessHandler
        extends SavedRequestAwareAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String userEmail = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
        saveCookie("last_email", userEmail, response);
        super.onAuthenticationSuccess(request, response, authentication);
        super.setUseReferer(true);
    }

    public static void saveCookie(String cookieName, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(2592000); // 1 Mês
        response.addCookie(cookie);
    }
}

