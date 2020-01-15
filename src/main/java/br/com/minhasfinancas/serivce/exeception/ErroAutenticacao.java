package br.com.minhasfinancas.serivce.exeception;

public class ErroAutenticacao extends RuntimeException {

	public ErroAutenticacao (String mensagem) {
		super(mensagem);
	}
}
