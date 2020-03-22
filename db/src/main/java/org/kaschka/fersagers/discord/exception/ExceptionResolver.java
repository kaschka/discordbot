package org.kaschka.fersagers.discord.exception;

import javax.servlet.http.HttpServletRequest;

import org.kaschka.fersagers.discord.logging.ErrorLogCreator;
import org.kaschka.fersagers.discord.logging.ErrorResultDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResultDTO> resolveNotFoundException(HttpServletRequest request, Exception ex) {
        return ErrorLogCreator.create(HttpStatus.NOT_FOUND, ex.getMessage())
                .log().createResponseEntity();
    }

    @ExceptionHandler(GuildAlreadyExistsException.class)
    public ResponseEntity<ErrorResultDTO> resolveGuildAlreadyExistsException(HttpServletRequest request, Exception ex) {
        return ErrorLogCreator.create(HttpStatus.CONFLICT, "guild.already.exists" ,ex.getMessage())
                .log().createResponseEntity();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResultDTO> resolveInvalidArgumentException(HttpServletRequest request, Exception ex) {
        return ErrorLogCreator.create(HttpStatus.BAD_REQUEST, "arguments.not.allowed" ,ex.getMessage())
                .log().createResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResultDTO> resolveException(HttpServletRequest request, Exception ex) {
        return ErrorLogCreator.create(HttpStatus.INTERNAL_SERVER_ERROR, "internal.server.error" , ex.getMessage())
                .log().createResponseEntity();
    }
}