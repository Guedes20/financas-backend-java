package br.com.minhasfinancas.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.minhasfinancas.entity.Usuario;
import br.com.minhasfinancas.serivce.impl.UsuarioServiceImpl;

public class JWTAuthFilter extends OncePerRequestFilter {

	private JWTService jwtService;
	private UsuarioServiceImpl usuarioService;

	public JWTAuthFilter(JWTService jWTService, UsuarioServiceImpl usuarioServiceImpl) {
		jwtService = jWTService;
		usuarioService = usuarioServiceImpl;
	}

	/*
	 * eyJhbGciOiJIUzUxMiJ9.
	 * eyJzdWIiOiJmZXJuYW5vZ3VlZGVzMjBAZ21haWwuY29tIiwiZXhwIjoxNTg4MDMyODg0fQ.
	 * Wc9Agn4lxuXxKTdsHwBnumYfsQKcs3hVYCi2ZBn5he4EIwA0R-2NjOiBAEmGHZr-
	 * lxoRNfnV3qL4rbGek__HhQ
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");

		if (authorization != null && authorization.startsWith("Bearer")) {
			String token = authorization.split("")[1];
			boolean isValid = jwtService.tokenValido(token);

			if (isValid) {
				String loginUsuario = jwtService.obterUsuario(token);
			    Usuario usuario =	usuarioService.obterPorEmail(loginUsuario);
				UsernamePasswordAuthenticationToken user  = new UsernamePasswordAuthenticationToken(usuario, null, null);
                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(user);
			}
		}

		filterChain.doFilter(request, response);
		
	}

	
}
