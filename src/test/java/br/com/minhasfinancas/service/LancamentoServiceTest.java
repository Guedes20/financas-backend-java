package br.com.minhasfinancas.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.minhasfinancas.entity.Lancamento;
import br.com.minhasfinancas.enuns.StatusLancamento;
import br.com.minhasfinancas.enuns.TipoLancamento;
import br.com.minhasfinancas.model.repository.LancamentoRepository;
import br.com.minhasfinancas.model.repository.LancamentoRepositoryTest;
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
    	
    }
    
    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
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
}
