package Validators;

import Domain.HasId;

import java.util.HashMap;

public interface Validator<ID,E extends HasId<ID>>{
    void validate(E entity) throws ValidationException;
}
