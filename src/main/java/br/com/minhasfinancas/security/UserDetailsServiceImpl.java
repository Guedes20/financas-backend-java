package br.com.minhasfinancas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.minhasfinancas.entity.Usuario;
import br.com.minhasfinancas.model.repository.LancamentoRepository;
import br.com.minhasfinancas.serivce.UsuarioService;
import br.com.minhasfinancas.serivce.exeception.ErroAutenticacao;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.obterPorEmail(login);

		if (usuario == null) {
			throw new ErroAutenticacao("Usuario não encontrado para o e-mail informado.");
		}

		return User
				 .builder()
				 .username(usuario.getEmail())
				 .password(usuario.getSenha())
				 .authorities(new SimpleGrantedAuthority("USER"))
				 .build();

	}

}
