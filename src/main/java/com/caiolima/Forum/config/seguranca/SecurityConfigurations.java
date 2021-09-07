package com.caiolima.Forum.config.seguranca;

import com.caiolima.Forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity //habilita o modulo de segurança, bloqueando os endpoints
@Configuration // anotando com configuration, o spring carrega as informações dessa classe no startup
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private TokenServ tokenServ;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Bean // fazer com que o AuthenticationManager seja injetavel
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // configrações de autenticação
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService)
        .passwordEncoder(new BCryptPasswordEncoder());
    }

    // configurações de autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET , "/topicos")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/topicos/*")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/auth")
                .permitAll()
                .antMatchers("/swagger-ui/*").permitAll()
                .antMatchers(HttpMethod.DELETE).hasRole("MODERADOR")
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoViaTokenFiltro(tokenServ, usuarioRepository), UsernamePasswordAuthenticationFilter.class); // rodar o do token antes do filtro do spring que ja roda pro padrao
    }

    // Acesso a recursos estáticos
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**",
                "/configurations/**", "/swagger-resources/**");
    }
}
