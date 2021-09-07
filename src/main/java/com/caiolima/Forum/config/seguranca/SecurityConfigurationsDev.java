package com.caiolima.Forum.config.seguranca;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableWebSecurity //habilita o modulo de segurança, bloqueando os endpoints
@Configuration // anotando com configuration, o spring carrega as informações dessa classe no startup
@Profile("dev")
public class SecurityConfigurationsDev extends WebSecurityConfigurerAdapter {

    // configurações de autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .and().csrf().disable();
    }
}
