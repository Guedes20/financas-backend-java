package br.com.minhasfinancas.model.repository;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.minhasfinancas.entity.Usuario;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		/* Cenario */
		Usuario usuario = Usuario.builder().nome("Fulano").email("fulano@gmail.com").build();
		repository.save(usuario);
		/* Ação */
		boolean result = repository.existsByEmail("fulano@gmail.com");
		/* Verificação */
		Assertions.assertThat(result).isTrue();
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		/* Cenario */
		repository.deleteAll();
		/* Ação */
		boolean result = repository.existsByEmail("fulano@gmail.com");
		/* Verificação */
		Assertions.assertThat(result).isFalse();
	}

	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		/* Cenario */
		Usuario usuario = criaUsuario();
		/* Ação */
		Usuario usuarioSalvo = repository.save(usuario);
		/* Verificação */
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}

	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		/* Cenario */
		Usuario usuario = criaUsuario();
		entityManager.persist(usuario);
		/* Ação */
		Optional<Usuario> result = repository.findByEmail("usuario@gmail.com");
		/* Verificação */
		Assertions.assertThat(result.isPresent()).isTrue();
	}

	@Test
	public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExisteNaBase() {
		/* Ação */
		Optional<Usuario> result = repository.findByEmail("usuario@gmail.com");
		/* Verificação */
		Assertions.assertThat(result.isPresent()).isFalse();
	}

	public static Usuario criaUsuario() {
		return Usuario.builder().nome("usuario").email("usuario@gmail.com").senha("senha").build();
	}

}
