package br.ada.customer.crud.integration.sms;

import br.ada.customer.crud.model.Order;
import br.ada.customer.crud.usecases.INotifierUserCase;
import br.ada.customer.crud.usecases.IOrderNotifierUserCase;

public class OrderSmsNotifierImpl implements INotifierUserCase<Order>, IOrderNotifierUserCase {
    private SendSms sendSms;

    public OrderSmsNotifierImpl(SendSms sendSms) {
        this.sendSms = sendSms;
    }
    @Override
    public void registered(Order order) {
        sendSms.send("1111111111111", order.getCustomer().getTelephone(), "Novo pedido realizado. Aguardando o pagamento.");

    }

    @Override
    public void updated(Order order) {
        sendSms.send("1111111111111", order.getCustomer().getTelephone(), "O pedido foi alterado com sucesso.");

    }

    @Override
    public void deleted(Order order) {
        sendSms.send("1111111111111", order.getCustomer().getTelephone(), "O pedido foi cancelado com sucesso.");

    }

    @Override
    public void confirmedPayment(Order order) {
        sendSms.send("1111111111111", order.getCustomer().getTelephone(), "O pagamento foi realizado com sucesso e o pedido foi confirmado.");

    }

    @Override
    public void shippingNotice(Order order) {
        sendSms.send("1111111111111", order.getCustomer().getTelephone(), "Seu pedido já foi enviado! Agradecemos a confiança.");
    }
}
