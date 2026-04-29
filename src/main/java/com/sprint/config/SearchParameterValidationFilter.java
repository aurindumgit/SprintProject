package com.sprint.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SearchParameterValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Check if this is a findByFirstNameAndLastName search request
        if (httpRequest.getRequestURI().contains("/customers/search/findByFirstNameAndLastName")) {
            String firstName = httpRequest.getParameter("firstName");
            String lastName = httpRequest.getParameter("lastName");

            // Validate both parameters are present and not blank
            if (firstName == null || firstName.trim().isEmpty() || 
                lastName == null || lastName.trim().isEmpty()) {
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\":\"Both firstName and lastName parameters are required\"}");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
