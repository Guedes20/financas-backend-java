package br.com.minhasfinancas.serivce;

import java.util.Optional;

import br.com.minhasfinancas.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);
	Usuario salvarUsuario(Usuario usuario);	
	void validarEmail(String email);
	Optional<Usuario> obterPorId(Long id);
	Usuario obterPorEmail(String email);
	
}
