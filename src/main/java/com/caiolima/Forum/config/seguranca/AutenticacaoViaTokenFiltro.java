package com.caiolima.Forum.config.seguranca;

import com.caiolima.Forum.model.Usuario;
import com.caiolima.Forum.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



// lógica de validar o token
public class AutenticacaoViaTokenFiltro extends OncePerRequestFilter {  // filtro chamado uma vez por requisição

    private UsuarioRepository usuarioRepository;

    private TokenServ tokenServ;

    public AutenticacaoViaTokenFiltro(TokenServ tokenServ, UsuarioRepository usuarioRepository) {
        this.tokenServ = tokenServ;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarTokenBearer(request);

        if (tokenServ.isValido(token)) {
            autenticarCliente(token);
        }

        filterChain.doFilter(request, response);
    }

    private void autenticarCliente(String token) {

        Long usuarioId = tokenServ.recuperarIdUsuario(token);

        Usuario usuario = usuarioRepository.findById(usuarioId).get();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(usuario, usuario.getPassword(), usuario.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Retira o tipo do token e o retorna apenas com o valor caso seja do tipo Bearer
     * @param request requisição http
     * @return token sem o tipo caso seja Bearer ou null caso esteja vazio, null ou não seja Bearer
     */
    private String recuperarTokenBearer(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && !token.isEmpty() && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }
}
