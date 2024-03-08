package ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if (entity.getFirstName().isEmpty()) {
            throw new ValidationException("Primul nume nu este valid!");
        } else {
            for (char c : entity.getFirstName().toCharArray()) {
                if (Character.isDigit(c) || Character.isWhitespace(c)) {
                    throw new ValidationException("Primul nume nu este valid!");
                }
            }
        }
        if (entity.getLastName().length() < 3) {
            throw new ValidationException("Al doilea nume nu este valid!");
        } else {
            for (char c : entity.getLastName().toCharArray()) {
                if (Character.isDigit(c) || Character.isWhitespace(c)) {
                    throw new ValidationException("Al doilea nume nu este valid!");
                }
            }
        }
        if (entity.getUsername().length() < 3) {
            throw new ValidationException("Username invalid!");
        }
    }
}
