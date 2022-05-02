package Validators;

import Domain.Show;

public class ShowValidator implements Validator<Long, Show> {
    @Override
    public void validate(Show entity) throws ValidationException {
        String erors = "";
        if (entity.getShowID()<0)
        {
            erors += "Id-ul nu poate fi negativ!\n";
        }
        for (int i=0;i<entity.getName().length();i++)
        {
            Boolean flag=Character.isDigit(entity.getName().charAt(i));
            if (flag){
                erors+="Numele nu poate contine cifre!\n";
                break;
            }
        }
        if (entity.getDescription()=="")
        {
            erors+= "Descrierea nu poate fi nula!\n";
        }
        if (entity.getType()=="")
        {
            erors+="Tipul spectacolului nu poate fi nul!\n";
        }
        if (entity.getNrofseats()<0)
        {
            erors += "Numarul de locuri nu poate fi negativ!\n";
        }
        if (erors.length()>0)
        {
            throw new ValidationException(erors);
        }
    }
}
