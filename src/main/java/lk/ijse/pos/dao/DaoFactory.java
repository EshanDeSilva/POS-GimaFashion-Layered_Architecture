package lk.ijse.pos.dao;

import lk.ijse.pos.dao.custom.impl.*;

public class DaoFactory {
    private static DaoFactory daoFactory=null;

    private DaoFactory() {

    }

    public static DaoFactory getDaoFactory(){
        return daoFactory!=null ? daoFactory : (daoFactory=new DaoFactory());
    }

    public enum DaoType{
        USER,SUPPLIER_INVOICE,SUPPLIER,SALES_RETURN,PAYMENT,ORDER_DETAILS,ORDER,ITEM,EMPLOYER,CATEGORY
    }

    public <T extends SuperDao>T getDaoType(DaoType type){
        switch (type){
            case USER: return (T) new UserDaoImpl();
            case SUPPLIER_INVOICE: return (T) new SupplierInvoiceDaoImpl();
            case SUPPLIER: return (T) new SupplierDaoImpl();
            case SALES_RETURN: return (T) new SalesReturnDaoImpl();
            case PAYMENT: return (T) new PaymentDaoImpl();
            case ORDER_DETAILS: return (T) new OrderDetailsDaoImpl();
            case ORDER: return (T) new OrderDaoImpl();
            case ITEM: return (T) new ItemDaoImpl();
            case EMPLOYER: return (T) new EmployerDaoImpl();
            case CATEGORY: return (T) new CategoryDaoImpl();
            default: return null;
        }
    }
}
