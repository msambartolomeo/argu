package ar.edu.itba.paw.model.exceptions;

public class CategoryNotFoundException extends Exception404 {
    @Override
    public String getMessageCode() {
        return "error.category.not.found";
    }
}
