package storemanagementtool.store.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import storemanagementtool.store.exception.custom.ProductOutOfStockException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleProductNotFoundException(NoSuchElementException ex) {
        logger.error("Error: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ProductOutOfStockException.class)
    public ResponseEntity<String> handleProductOutOfStockException(ProductOutOfStockException ex) {
        logger.error("Error: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }
}
