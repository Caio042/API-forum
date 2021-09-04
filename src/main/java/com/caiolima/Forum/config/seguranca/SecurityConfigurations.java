package com.caiolima.Forum.config.seguranca;

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

@EnableWebSecurity //habilita o modulo de segurança, bloqueando os endpoints
@Configuration // anotando com configuration, o spring carrega as informações dessa classe no startup
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService;

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
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    // Acesso a recursos estáticos
    @Override
    public void configure(WebSecurity web) throws Exception {

    }
}
