package br.com.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.minhasfinancas.entity.Lancamento;
import br.com.minhasfinancas.enuns.StatusLancamento;
import br.com.minhasfinancas.enuns.TipoLancamento;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository lancamentoRepository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamento = Lancamento.builder()
				                          .ano(2020)
				                          .mes(2)
				                          .descricao("Lancamento de teste.")
				                          .valor(BigDecimal.valueOf(10))
				                          .tipo(TipoLancamento.RECEITA)
				                          .status(StatusLancamento.PENDENTE)
				                          .dataCadastro(LocalDate.now())
				                          .build();

		lancamento = lancamentoRepository.save(lancamento);

		assertThat(lancamento.getId()).isNotNull();

	}

	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criaEPersitirLancamento();

		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		lancamentoRepository.delete(lancamento);
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());

		assertThat(lancamentoInexistente).isNull();

	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criaEPersitirLancamento();
        
		lancamento.setAno(2019);
		lancamento.setDescricao("Lancamento Atualizado");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		lancamentoRepository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoAtualizado.getAno()).isEqualTo(2019);
		assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Lancamento Atualizado");
		assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
		
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criaEPersitirLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = lancamentoRepository.findById(lancamento.getId());
		
		assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}

	private Lancamento criaEPersitirLancamento() {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		return lancamento;
	}

	private Lancamento criarLancamento() {
		return Lancamento.builder()
				         .ano(2020)
				         .mes(2)
				         .descricao("Lancamento de teste.")
				         .valor(BigDecimal.valueOf(10))
				         .tipo(TipoLancamento.RECEITA)
				         .status(StatusLancamento.PENDENTE)
				         .dataCadastro(LocalDate.now())
				         .build();
	}

}
