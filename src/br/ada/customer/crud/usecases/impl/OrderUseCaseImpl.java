package br.ada.customer.crud.usecases.impl;

import br.ada.customer.crud.exceptions.OrderException;
import br.ada.customer.crud.model.*;
import br.ada.customer.crud.usecases.IOrderNotifierUserCase;
import br.ada.customer.crud.usecases.IOrderUseCase;
import br.ada.customer.crud.usecases.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class OrderUseCaseImpl implements IOrderUseCase {
    private OrderRepository repository;
    private IOrderNotifierUserCase notifier;

    public OrderUseCaseImpl(OrderRepository repository, IOrderNotifierUserCase notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }
    /*
     * 1 - Inicia um novo pedido para o cliente
     * 2 - Pedido deve iniciar com status igual a OrderStatus.OPEN
     * 3 - Lembrar de atualizar o banco através do repository
     */
    @Override
    public Order create(Customer customer) {
        Order order = new Order();
        order.setCustomer(customer);

        Random gerador = new Random();
        order.setId(gerador.nextLong());
        order.setOrderedAt(LocalDateTime.now());

        order.setStatus(OrderStatus.OPEN);
        repository.save(order);
        System.out.println("Pedido criado: " + order.getId());
        return order;
    }

    /*
     * 1 - Pedido precisa estar com status == OrderStatus.OPEN
     * 2 - Lembrar de atualizar o banco através do repository
     */
    @Override
    public OrderItem addItem(Order order, Product product, BigDecimal price, Integer amount) {
        if(order.getStatus() == OrderStatus.OPEN) {
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setProduct(product);
            newOrderItem.setSaleValue(price);
            newOrderItem.setAmount(amount);

            List<OrderItem> orderItems = order.getItems();
            orderItems.add(newOrderItem);
            order.setItems(orderItems);

            repository.update(order);
            return newOrderItem;
        } else throw new OrderException("Pedido indisponível.", new Exception());
    }

    /*
     * 1 - Pedido precisa estar com status == OrderStatus.OPEN
     * 2 - Trocar a quantidade que foi vendida desse produto
     * 3 - Lembrar de atualizar o banco através do repository
     */
    @Override
    public OrderItem changeAmount(Order order, Product product, Integer amount) {
        if(order.getStatus() == OrderStatus.OPEN) {
            List<OrderItem> orderItems = order.getItems();
            OrderItem updatedOrderItem = new OrderItem();

            for(OrderItem orderedItem : orderItems) {
                if (orderedItem.getProduct() == product) {
                    orderedItem.setAmount(amount);
                    updatedOrderItem = orderedItem;
                }
            }
            repository.update(order);

            return updatedOrderItem;
        } else throw new OrderException("Pedido indisponível.", new Exception());
    }

    /*
     * 1 - Pedido precisa estar com status == OrderStatus.OPEN
     * 2 - Lembrar de atualizar o banco através do repository
     */
    @Override
    public void removeItem(Order order, Product product) {
        if(order.getStatus() == OrderStatus.OPEN) {
            List<OrderItem> orderItems = order.getItems();

            for(OrderItem orderedItem : orderItems) {
                if (orderedItem.getProduct() == product) {
                    orderedItem = null;
                }
            }
            repository.update(order);
        } else throw new OrderException("Pedido indisponível.", new Exception());
    }

    /*
     * 1 - Pedido precisa estar com status == OrderStatus.OPEN
     * 2 - Pedido precisa ter no mínimo um item
     * 3 - Valor deve ser maior que zero
     * 4 - Notificar o cliente que esta aguardando o pagamento
     * 5 - Pedido deve passar a ter o status igual OrderStatus.PENDING_PAYMENT
     */
    @Override
    public void placeOrder(Order order) {
        if(order.getStatus() == OrderStatus.OPEN) {

            List<OrderItem> orderItems = order.getItems();
            if(orderItems.size() > 0) {
                BigDecimal totalValue = BigDecimal.valueOf(0);

                for(OrderItem orderedItem : orderItems){
                    totalValue = totalValue.add(orderedItem.getSaleValue());
                }

                if(totalValue != BigDecimal.ZERO) {
                    notifier.registered(order);
                    order.setStatus(OrderStatus.PENDING_PAYMENT);

                }

            }

        } else throw new OrderException("Pedido indisponível.", new Exception());
    }

    /*
     * 1 - Pedido precisa estar com status == OrderStatus.PENDING_PAYMENT
     * 2 - Pedido deve passar a ter o status igual a OrderStatus.PAID
     * 3 - Notificar o cliente sobre o pagamento com sucesso
     */
    @Override
    public void pay(Order order) {
        if(order.getStatus() == OrderStatus.PENDING_PAYMENT) {
            order.setStatus(OrderStatus.PAID);
            notifier.confirmedPayment(order);
        } else throw new OrderException("Pedido indisponível.", new Exception());
    }

    /*
     * 1 - Entrega só pode acontecer depois do pedido ter sido pago (order.status == OrderStatus.PAID)
     * 2 - Pedido deve passar a ter o status igual a OrderStatus.FINISH
     * 3 - Notificar o cliente e agradecer pela compra
     */
    @Override
    public void shipping(Order order) {
        if(order.getStatus() == OrderStatus.PAID) {
            order.setStatus(OrderStatus.FINISH);
            notifier.shippingNotice(order);
        } else throw new OrderException("Pedido indisponível.", new Exception());
    }
}
