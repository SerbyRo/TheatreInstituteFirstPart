package Validators;

import Domain.Client;

public class ClientValidator implements Validator<Long, Client> {
    @Override
    public void validate(Client entity) throws ValidationException {
        String erors="";
        if (entity.getGender()=="")
        {
            erors+="Sexul nu poate fi vid!\n";
        }
        for (int i=0;i<entity.getFirst_name().length();i++)
        {
            Boolean flag=Character.isDigit(entity.getFirst_name().charAt(i));
            if (flag){
                erors+="Numele nu poate contine cifre!\n";
                break;
            }
        }
        if (entity.getUsername()=="")
        {
            erors+="Numele nu poate fi vid!\n";
        }
        if (entity.getPassword()=="")
        {
            erors+="Numele nu poate fi vid!\n";
        }
        if (entity.getAge()< 0 || entity.getAge()> 150)
        {
            erors+= "Varsta incorecta!\n";
        }
        if (erors.length()>0)
        {
            throw new ValidationException(erors);
        }
    }
}
