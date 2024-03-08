package ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}