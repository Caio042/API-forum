package com.caiolima.Forum.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErroValidacaoHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler (MethodArgumentNotValidException.class) // este metodo resolve os erros dessa exceção que está como argumento
    public List<ErroDeFormDto> handler(MethodArgumentNotValidException exception){

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        List<ErroDeFormDto> errosDto = fieldErrors
                .stream()
                .map(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            return new ErroDeFormDto(e.getField(), mensagem);
        }).collect(Collectors.toList());

        return errosDto;
    }
}