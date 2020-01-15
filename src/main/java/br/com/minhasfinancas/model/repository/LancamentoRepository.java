package br.com.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.minhasfinancas.entity.Lancamento;

public interface LancamentoRepository  extends JpaRepository<Lancamento, Long>{

}
