package br.com.minhasfinancas.dto;

import javax.persistence.Column;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioDTO {

	private String nome;
	private String email;
	private String senha;

}
