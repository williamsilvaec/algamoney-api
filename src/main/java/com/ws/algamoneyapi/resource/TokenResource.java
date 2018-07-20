package com.ws.algamoneyapi.resource;

import com.ws.algamoneyapi.config.property.AlgamoneyApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/tokens")
public class TokenResource {

    private final AlgamoneyApiProperty algamoneyApiProperty;

    @Autowired
    public TokenResource(AlgamoneyApiProperty algamoneyApiProperty) {
        this.algamoneyApiProperty = algamoneyApiProperty;
    }

    @DeleteMapping("/revoke")
    public void revoke(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps());
        cookie.setPath(request.getContextPath() + "/oauth/token");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
