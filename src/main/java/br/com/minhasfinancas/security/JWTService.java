package br.com.minhasfinancas.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import br.com.minhasfinancas.Application;
import br.com.minhasfinancas.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTService {
    
	@Value("${security.jwt.expiracao}")
	private String expiracao;
	
	@Value("${security.jwt.chave-assinatura}")
	private String chaveAssinatura;
	
	public String gerarToken(Usuario usuario) {
		long expiracaoLong = Long.valueOf(expiracao);
		LocalDateTime datahoraExpiracao = LocalDateTime.now().plusMinutes(expiracaoLong);
		Date exp = Date.from(datahoraExpiracao.atZone(ZoneId.systemDefault()).toInstant());
		
	    return 	Jwts
		   .builder()
		   .setSubject(usuario.getEmail())
		   .setExpiration(exp)
		   .signWith(SignatureAlgorithm.HS512, chaveAssinatura)
		   .compact();
	}
	
	public String obterUsuario(String token)throws ExpiredJwtException {
		return (String) obterClaims(token).getSubject();
	}
	
	public boolean tokenValido(String token) {
		try {
			Claims claims = obterClaims(token);
			Date dataHoraExpiracao = claims.getExpiration();

			LocalDateTime expiracao = dataHoraExpiracao.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			return !LocalDateTime.now().isAfter(expiracao);
		} catch (Exception e) {
			return false;
		}
	}
	
	public Claims obterClaims(String token)throws ExpiredJwtException {
		return Jwts
				.parser()
				.setSigningKey(chaveAssinatura)
				.parseClaimsJws(token)
				.getBody();	  
	}
		
	/**
	 * Classe de teste
	 * @param args
	 */
	public static void main(String[] args) {
		ConfigurableApplicationContext contexto  =SpringApplication.run(Application.class);
		JWTService service = contexto.getBean(JWTService.class);
		Usuario usuario = Usuario.builder().email("fernanoguedes20@gmail.com").build();
 		String token = service.gerarToken(usuario);
		System.out.println(token);
		boolean isTokenValido = service.tokenValido(token);
		System.out.println("O token esta valido? "+ isTokenValido);
		System.out.println(service.obterUsuario(token));	
	}
}
