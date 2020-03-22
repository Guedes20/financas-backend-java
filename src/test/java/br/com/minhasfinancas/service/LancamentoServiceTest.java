package br.com.minhasfinancas.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.minhasfinancas.entity.Lancamento;
import br.com.minhasfinancas.entity.Usuario;
import br.com.minhasfinancas.enuns.StatusLancamento;
import br.com.minhasfinancas.model.repository.LancamentoRepository;
import br.com.minhasfinancas.model.repository.LancamentoRepositoryTest;
import br.com.minhasfinancas.serivce.exeception.RegraNegocioException;
import br.com.minhasfinancas.serivce.impl.LancamentoServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImpl lancamentoServiceImpl;

	@MockBean
	LancamentoRepository lancamentoRepository;

	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(lancamentoServiceImpl).validar(lancamentoASalvar);

		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(lancamentoRepository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

		Lancamento lancamento = lancamentoServiceImpl.salvar(lancamentoASalvar);

		assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);

	}

/*	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(lancamentoServiceImpl).validar(lancamentoASalvar);

		Assertions.catchThrowableOfType(() -> lancamentoRepository.save(lancamentoASalvar),
				RegraNegocioException.class);

		Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoASalvar);

	}*/

	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.doNothing().when(lancamentoServiceImpl).validar(lancamentoSalvo);
		Mockito.when(lancamentoRepository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

		lancamentoServiceImpl.atualizar(lancamentoSalvo);

		Mockito.verify(lancamentoRepository, Mockito.times(1)).save(lancamentoSalvo);
	}

	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

		Assertions.catchThrowableOfType(() -> lancamentoServiceImpl.atualizar(lancamento), NullPointerException.class);

		Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamento);
	}
/*
	@Test
	public void deveDeltarUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);

		lancamentoServiceImpl.deletar(lancamento);

		Mockito.verify(lancamentoRepository).delete(lancamento);
	}*/

/*	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

		Assertions.catchThrowableOfType(() -> lancamentoServiceImpl.deletar(lancamento), NullPointerException.class);

		Mockito.verify(lancamentoServiceImpl, Mockito.never()).deletar(lancamento);
	}
*/
	/*@Test
	public void deveFiltrarLancamentos() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);

		List<Lancamento> resultado = lancamentoServiceImpl.buscar(lancamento);

		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
	}*/
/*
	@Test
	public void deveAtualizarStatusDeUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);

		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(lancamentoServiceImpl).atualizar(lancamento);

		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(lancamentoServiceImpl).atualizar(lancamento);
	}
	*/
	public void  deveObterUmLancamentoPorID() {
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.of(lancamento));
	    
		Optional<Lancamento> resultado = lancamentoServiceImpl.obterPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isTrue();
	
	}
	
	public void  deveRetonarVazioQuandoUmLancamentoNaoExiste() {
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.empty());
	    
		Optional<Lancamento> resultado = lancamentoServiceImpl.obterPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isFalse();
	
	}
	
	public void deveLancarErroAoValidarUmLancamento() {
		Lancamento lancamento = new Lancamento();
		Throwable erro = null;
		
		erro = Assertions.catchThrowable(()-> lancamentoServiceImpl.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição valida.");
		
		lancamento.setDescricao("Salario");
		
		erro =  Assertions.catchThrowable(()-> lancamentoServiceImpl.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Mês valido.");
				
		lancamento.setMes(1);
		
		erro =  Assertions.catchThrowable(()-> lancamentoServiceImpl.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Ano valido.");
		
		lancamento.setAno(2020);
		
		erro =  Assertions.catchThrowable(()-> lancamentoServiceImpl.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuario.");
		
		lancamento.setUsuario(new Usuario());
		lancamento.getUsuario().setId(1l);
		
		erro =  Assertions.catchThrowable(()-> lancamentoServiceImpl.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um valor valido.");
		
		lancamento.setValor(BigDecimal.ZERO);

		erro =  Assertions.catchThrowable(()-> lancamentoServiceImpl.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um valor valido.");
	
		lancamento.setValor(BigDecimal.TEN);
		
		erro =  Assertions.catchThrowable(()-> lancamentoServiceImpl.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Tipo de Lancamento.");
	
		
	}
	
}
