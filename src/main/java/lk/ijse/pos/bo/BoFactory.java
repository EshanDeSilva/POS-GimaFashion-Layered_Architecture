package lk.ijse.pos.bo;

import lk.ijse.pos.bo.custom.impl.*;

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
            case SUPPLIER_BO: return (T) new SupplierBoImpl();
            case SALES_RETURN_BO: return (T) new SalesReturnBoImpl();
            case PAYMENT_BO: return (T) new PaymentBoImpl();
            default: return null;
        }
    }
}
