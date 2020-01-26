package br.com.minhasfinancas.controller;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.minhasfinancas.dto.LancamentoDTO;
import br.com.minhasfinancas.entity.Lancamento;
import br.com.minhasfinancas.entity.Usuario;
import br.com.minhasfinancas.enuns.StatusLancamento;
import br.com.minhasfinancas.enuns.TipoLancamento;
import br.com.minhasfinancas.serivce.LancamentoService;
import br.com.minhasfinancas.serivce.UsuarioService;
import br.com.minhasfinancas.serivce.exeception.RegraNegocioException;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

	private LancamentoService lancamentoService;

	private UsuarioService usuarioService;

	public LancamentoController(LancamentoService lancamentoService) {
		this.lancamentoService = lancamentoService;
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento entidade = converter(dto);
			entidade = lancamentoService.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
		return lancamentoService.obterPorId(id).map(entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}

	@DeleteMapping
	 public ResponseEntity atualizar(@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id).map(entidade ->{
             lancamentoService.deletar(entidade);
             return new ResponseEntity(HttpStatus.NO_CONTENT);
 		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento l = new Lancamento();
		l.setAno(dto.getAno());
		l.setMes(dto.getMes());
		l.setDescricao(dto.getDescricao());
		l.setValor(dto.getValor());

		Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuario não encontrado para o Id informado."));

		l.setUsuario(usuario);
		l.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		l.setStatus(StatusLancamento.valueOf(dto.getStatus()));

		return l;

	}

}
