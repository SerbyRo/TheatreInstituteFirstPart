package Validators;

import Domain.Ticket;

public class TicketValidator implements Validator<Long, Ticket> {

    @Override
    public void validate(Ticket entity) throws ValidationException {
        String erors="";


        if (entity.getClient_name().getFirst_name()=="")
        {
            erors+="Numele clientului nu exista!\n";
        }
//        if (entity.getClient_name()=="")
//        {
//            erors+="Numele clientului nu exista!\n";
//        }
//        if (entity.getShow_id()<0)
//        {
//            erors+="Nu a fost selectat show-ul!\n";
//        }
        if (entity.getShow_id().getShowID()<0)
        {
            erors+="Nu a fost selectat show-ul!\n";
        }
        if (entity.getTicketID()<=0)
        {
            erors+="Nu au fost cumparate bilete la acest spectacol!\n";
        }
        if (entity.getNumberofseats()<0)
        {
            erors+="Nu au fost selectate locuri!\n";
        }
        if (entity.getRownumber()<0)
        {
            erors+="Numarul randului nu poate fi negativ!\n";
        }
        if (entity.getLodge()<0)
        {
            erors+="Numarul lojei nu poate fi negativ!\n";
        }
        if (entity.getPrice()<0)
        {
            erors+= "Pretul nu poate fi negativ!\n";
        }
        if (entity.getStatus()==null)
        {
            erors+="Statusul nu poate fi negativ!\n";
        }
        if (erors.length()>0)
        {
            throw new ValidationException(erors);
        }
    }
}
