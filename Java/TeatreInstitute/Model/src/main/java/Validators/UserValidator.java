package Validators;

import Domain.User;

public class UserValidator implements Validator<Long, User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String erors = "";
        if (entity.getUsername() == "")
        {
            erors+="Username-ul nu poate fi vid!\n";
        }
        if (entity.getFirst_name() == "")
        {
            erors+="Prenumele nu poate fi vid!\n";
        }
        if (entity.getPassword() == "")
        {
            erors+="Parola nu poate fi vida!\n";
        }
        if (erors.length()>0)
        {
            throw new ValidationException(erors);
        }
    }
}
