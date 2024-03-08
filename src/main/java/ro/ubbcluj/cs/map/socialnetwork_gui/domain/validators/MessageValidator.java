package ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Message;

public class MessageValidator implements Validator<Message>{

    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity.getMessage().isEmpty()){
            throw new ValidationException("Your message is empty!");
        }
        if(entity.getFrom() == null){
            throw new ValidationException("Your message is not coming from anyone!");
        }
        if(entity.getTo() == null){
            throw new ValidationException("Your message is not going to anyone!");
        }
        if(entity.getTo().contains(entity.getFrom())){
            throw new ValidationException("You cannot give yourself a message!");
        }
    }
}
