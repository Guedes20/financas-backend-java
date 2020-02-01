package br.com.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.minhasfinancas.entity.Usuario;
import br.com.minhasfinancas.model.repository.UsuarioRepository;
import br.com.minhasfinancas.serivce.UsuarioService;
import br.com.minhasfinancas.serivce.exeception.ErroAutenticacao;
import br.com.minhasfinancas.serivce.exeception.RegraNegocioException;
import br.com.minhasfinancas.serivce.impl.UsuarioServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;

	@MockBean
	UsuarioRepository repository;

	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		/* Cenario */
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		/* Ação */
		service.salvarUsuario(usuario);
		/* Verificação */
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}

	@Test
	public void deveSalvarUmUsuario() {
		/* Cenario */
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().nome("nome").email("email@email.com").senha("senha").id(1l).build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		/* Ação */
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		/* Verificação */
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		Assertions.assertThat(usuarioSalvo.getNome()).isNotNull();
		Assertions.assertThat(usuarioSalvo.getSenha()).isNotNull();
		Assertions.assertThat(usuarioSalvo.getEmail()).isNotNull();

	}

	@Test(expected = Test.None.class)
	public void deveAutenticarUsuarioComSucesso() {
		/* Cenario */
		String email = "email@email.com";
		String senha = "senha";
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		/* Ação */
		Usuario resultado = service.autenticar(email, senha);
		/* Verificação */
		Assertions.assertThat(resultado).isNotNull();
	}

	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		/* Cenario */
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		/* Ação */
		service.validarEmail("email@email.com");
	}

	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		/* Cenario */
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		/* Ação */
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));
		/* Verificação */
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class)
				.hasMessage("Usuario não encontrado para o email informado.");
	}

	@Test
	public void deveLancarErroQuandoSenhaEstiverIncorreta() {
		/* Cenário */
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		/* Ação */
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senhaDif"));
		/* Verificação */
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida!!");
	}

	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExisteEmailCadastrado() {
		/* Cenario */
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		/* Ação */
		service.validarEmail("email@email.com");
	}
}
