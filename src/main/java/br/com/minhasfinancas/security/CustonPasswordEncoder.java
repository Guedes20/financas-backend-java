package br.com.minhasfinancas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustonPasswordEncoder implements PasswordEncoder {

	private static final  String SUFIXO = "S3nh4";
	
	@Override
	public String encode(CharSequence senhaDigitada) {
		return senhaDigitada+SUFIXO;
	}

	@Override
	public boolean matches(CharSequence senhaDigitada, String senhaCodificada) {
		senhaCodificada = senhaCodificada.substring(0, senhaCodificada.indexOf(SUFIXO));
		return senhaDigitada.equals(senhaCodificada);
	}
	

}
