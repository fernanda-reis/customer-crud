package br.ada.customer.crud.view;

import br.ada.customer.crud.model.Customer;
import br.ada.customer.crud.model.Order;
import br.ada.customer.crud.model.Product;
import br.ada.customer.crud.usecases.ICustomerUseCase;
import br.ada.customer.crud.usecases.IOrderUseCase;
import br.ada.customer.crud.usecases.IProductUseCase;

import java.util.Scanner;

public class OrderView {
    private Scanner scanner = new Scanner(System.in);
    private static Order order;
    private IOrderUseCase useCase;
    private ICustomerUseCase customerUseCase;
    private IProductUseCase productUseCase;

    public OrderView(IOrderUseCase useCase, ICustomerUseCase customerUseCase, IProductUseCase productUseCase){
        this.useCase = useCase;
        this.customerUseCase = customerUseCase;
        this.productUseCase = productUseCase;
    }

    public void showMenu() {
        System.out.println("Escolha uma opção abaixo:");
        System.out.println("1 - Cadastrar");
        System.out.println("2 - Adicionar item");
        System.out.println("3 - Mudar quantidade");
        System.out.println("4 - Remover item");
        System.out.println("5 - Finalizar Pedido");
        System.out.println("6 - Pagar");
        System.out.println("5 - Enviar Pedido");
        System.out.println("0 - Voltar");
        String option = scanner.nextLine();
        switch (option) {
            case "1":
                create();
                break;
            case "2":
                addItem();
                break;
            case "3":
                changeAmount();
                break;
            case "4":
                removeItem();
                break;
            case "5":
                placeOrder();
                break;
            case "6":
                pay();
                break;
            case "7":
                shipping();
                break;
            case "0":
                break;
            default:
                System.out.println("Opção invalida");
                showMenu();
                break;
        }
    }

    public void create() {
        Customer customer = inputDataCustomer();
        order = useCase.create(customer);
    }

    public Customer inputDataCustomer() {
        System.out.println("Informe o nome do cliente:");
        String name = scanner.nextLine();
        return customerUseCase.findByDocument(name);
    }

    public Product inputDataProduct() {
        System.out.println("Informe o código de barra do produto:");
        String barcode = scanner.nextLine();
        return productUseCase.findByBarcode(barcode);
    }

    public void addItem() {
        Product product = inputDataProduct();

        System.out.println("Informe a quantidade desejada:");
        int amount = scanner.nextInt();

        useCase.addItem(order, product, product.getValue(), amount);
    }

    public void changeAmount(){
        Product product = inputDataProduct();

        System.out.println("Informe a quantidade desejada:");
        int amount = scanner.nextInt();

        useCase.changeAmount(order, product, amount);
    }

    public void removeItem(){
        Product product = inputDataProduct();
        useCase.removeItem(order, product);
    }

    public void placeOrder(){
        useCase.placeOrder(order);
    }

    public void pay(){
        useCase.pay(order);
    }

    public void shipping(){
        useCase.shipping(order);
    }

}
