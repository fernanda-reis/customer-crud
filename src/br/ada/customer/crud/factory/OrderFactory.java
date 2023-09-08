package br.ada.customer.crud.factory;

import br.ada.customer.crud.integration.database.MemoryDatabase;
import br.ada.customer.crud.integration.memoryrepository.OrderEntityMerge;
import br.ada.customer.crud.integration.memoryrepository.OrderMemoryRepositoryImpl;
import br.ada.customer.crud.integration.sms.OrderSmsNotifierImpl;
import br.ada.customer.crud.integration.sms.SendSms;
import br.ada.customer.crud.usecases.IOrderNotifierUserCase;
import br.ada.customer.crud.usecases.IOrderUseCase;
import br.ada.customer.crud.usecases.impl.OrderUseCaseImpl;
import br.ada.customer.crud.usecases.repository.OrderRepository;

public class OrderFactory {

    public static IOrderUseCase createUseCase() {
        return new OrderUseCaseImpl(
                createRepository(),
                createNotifier()
        );
    }

    public static OrderRepository createRepository() {
        return new OrderMemoryRepositoryImpl(
                MemoryDatabase.getInstance(),
                new OrderEntityMerge(MemoryDatabase.getInstance())
        );
    }

    public static IOrderNotifierUserCase createNotifier() {
        return new OrderSmsNotifierImpl(new SendSms());
    }
}
