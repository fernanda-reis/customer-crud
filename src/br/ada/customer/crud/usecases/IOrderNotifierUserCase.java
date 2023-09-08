package br.ada.customer.crud.usecases;

import br.ada.customer.crud.model.Order;

public interface IOrderNotifierUserCase extends INotifierUserCase<Order> {
    void confirmedPayment(Order order);
    void shippingNotice(Order order);

}
