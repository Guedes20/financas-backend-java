package br.com.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.minhasfinancas.entity.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findByEmail(String email);
	Optional<Usuario> findByEmailAndNome(String email, String nome);	
	boolean existsByEmail(String email);
	

}
