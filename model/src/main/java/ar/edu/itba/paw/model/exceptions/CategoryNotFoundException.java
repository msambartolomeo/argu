package ar.edu.itba.paw.model.exceptions;

public class CategoryNotFoundException extends Exception404 {
    public CategoryNotFoundException() {
        super("Category not found.");
    }
    @Override
    public String getMessageCode() {
        return "error.category.not.found";
    }
}
