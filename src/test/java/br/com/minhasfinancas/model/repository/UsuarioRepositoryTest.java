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
		// cenário
		Usuario usuario = Usuario.builder().nome("Fulano").email("fulano@gmail.com").build();
		repository.save(usuario);
		// ação
		boolean result = repository.existsByEmail("fulano@gmail.com");
		// verificação
		Assertions.assertThat(result).isTrue();
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		// cenario
		repository.deleteAll();
		// ação
		boolean result = repository.existsByEmail("fulano@gmail.com");
		// verificação
		Assertions.assertThat(result).isFalse();
	}

	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		// cenario
		Usuario usuario = criaUsuario();
		// ação
		Usuario usuarioSalvo = repository.save(usuario);
		// verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}

	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		// cenario
		Usuario usuario = criaUsuario();
		entityManager.persist(usuario);
		// ação
		Optional<Usuario> result = repository.findByEmail("usuario@gmail.com");
		// verificação
		Assertions.assertThat(result.isPresent()).isTrue();
	}

	@Test
	public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExisteNaBase() {
		// cenario

		// ação
		Optional<Usuario> result = repository.findByEmail("usuario@gmail.com");
		// verificação
		Assertions.assertThat(result.isPresent()).isFalse();
	}

	public static Usuario criaUsuario() {
		return Usuario.builder().nome("usuario").email("usuario@gmail.com").senha("senha").build();
	}

}
