package com.example.web.common.errorhandling;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

class ErrorViewResolverImplTest {

    ErrorViewResolverImpl sut = new ErrorViewResolverImpl();

    @Test
    void testResolveErrorViewRedirectLogin() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.isNew()).thenReturn(true);

        HttpStatus status = HttpStatus.FORBIDDEN;
        Map<String, Object> model = Map.of();

        ModelAndView modelAndView = sut.resolveErrorView(request, status, model);

        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    void testResolveErrorViewReturnNullWhenSessionIsNotNew() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.isNew()).thenReturn(false);

        HttpStatus status = HttpStatus.FORBIDDEN;
        Map<String, Object> model = Map.of();

        ModelAndView modelAndView = sut.resolveErrorView(request, status, model);

        assertNull(modelAndView);
    }

    @Test
    void testResolveErrorViewReturnNullWhenSessionIsNull() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(null);

        HttpStatus status = HttpStatus.FORBIDDEN;
        Map<String, Object> model = Map.of();

        ModelAndView modelAndView = sut.resolveErrorView(request, status, model);

        assertNull(modelAndView);
    }

    @Test
    void testResolveErrorViewReturnNullWhenStatusCodeIsNot403() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.isNew()).thenReturn(true);

        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> model = Map.of();

        ModelAndView modelAndView = sut.resolveErrorView(request, status, model);

        assertNull(modelAndView);
    }
}
