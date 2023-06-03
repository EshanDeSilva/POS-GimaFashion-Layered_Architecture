package lk.ijse.pos.bo;

import lk.ijse.pos.bo.custom.impl.SupplierInvoiceBoImpl;
import lk.ijse.pos.bo.custom.impl.UserBoImpl;

public class BoFactory {
    private static BoFactory boFactory;

    private BoFactory(){
    }

    public static BoFactory getInstance(){
        return boFactory!=null ? boFactory:(boFactory=new BoFactory());
    }

    public enum BoType{
        USER_BO,SUPPLIER_INVOICE_BO,SUPPLIER_BO,SALES_RETURN_BO,PAYMENT_BO,ORDER_DETAILS_BO,ORDER_BO,ITEM_BO,EMPLOYER_BO,CATEGORY_BO
    }

    public <T extends SuperBo>T getBoType(BoType type){
        switch (type){
            case USER_BO: return (T) new UserBoImpl();
            case SUPPLIER_INVOICE_BO: return (T) new SupplierInvoiceBoImpl();
            default: return null;
        }
    }
}
