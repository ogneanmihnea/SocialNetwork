package ro.ubbcluj.cs.map.socialnetwork_gui.repository;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.Validator;

import java.util.List;

public class UtilizatorFileRepository extends AbstractFileRepository<Long, Utilizator> {

    public UtilizatorFileRepository(String fileName, Validator<Utilizator> validator) {
        super(fileName, validator);
    }

    @Override
    public Utilizator extractEntity(List<String> attributes) {
        //TODO: implement method
        Utilizator user = new Utilizator(attributes.get(1),attributes.get(2), attributes.get(3),attributes.get(4));
        user.setId(Long.parseLong(attributes.get(0)));

        return user;
    }

    @Override
    protected String createEntityAsString(Utilizator entity) {
        return entity.getId()+";"+entity.getFirstName()+";"+entity.getLastName();
    }
}