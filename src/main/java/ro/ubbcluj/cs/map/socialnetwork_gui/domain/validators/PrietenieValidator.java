package ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Prietenie;

import java.util.Objects;

public class PrietenieValidator implements Validator<Prietenie> {
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(Objects.equals(entity.getUser1().getId(), entity.getUser2().getId()))
            throw new ValidationException("You cannot add yourself as a friend");
    }
}