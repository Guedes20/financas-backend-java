package br.com.minhasfinancas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.minhasfinancas.dto.AtualizaStatusDTO;
import br.com.minhasfinancas.dto.LancamentoDTO;
import br.com.minhasfinancas.entity.Lancamento;
import br.com.minhasfinancas.entity.Usuario;
import br.com.minhasfinancas.enuns.StatusLancamento;
import br.com.minhasfinancas.enuns.TipoLancamento;
import br.com.minhasfinancas.serivce.LancamentoService;
import br.com.minhasfinancas.serivce.UsuarioService;
import br.com.minhasfinancas.serivce.exeception.RegraNegocioException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

	private final LancamentoService lancamentoService;

	private final UsuarioService usuarioService;

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

	@PutMapping("{id}/atualizar-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {
		return lancamentoService.obterPorId(id).map(entity -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());

			if (statusSelecionado == null) {
				return ResponseEntity.badRequest()
						.body("Não foi possivel atulizar o status do lancamento, envie um status valido.");
			}

			try {
				entity.setStatus(statusSelecionado);
				lancamentoService.atualizar(entity);
				return ResponseEntity.ok(entity);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}

	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id).map(entidade -> {
			lancamentoService.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}

	@GetMapping
	public ResponseEntity pesquisar(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano, @RequestParam("usuario") Long idUsuario) {

		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);

		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);

		if (!usuario.isPresent()) {
			return ResponseEntity.badRequest()
					.body("Não foi possivel realizar a consulta.Usuário não encontrado para o id informado.");
		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}

		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
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

		if (dto.getStatus() != null) {
			l.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}

		if (dto.getTipo() != null) {
			l.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}

		return l;

	}

}
