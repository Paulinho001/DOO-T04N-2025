
import java.util.*;


class Endereco {
    String estado, cidade, bairro, numero, complemento;

    public Endereco(String estado, String cidade, String bairro, String numero, String complemento) {
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.numero = numero;
        this.complemento = complemento;
    }

    public void apresentarLogradouro() {
        System.out.println(numero + ", " + complemento + " - " + bairro + ", " + cidade + " - " + estado);
    }
}


abstract class Pessoa {
    String nome;
    int idade;
    Endereco endereco;

    public Pessoa(String nome, int idade, Endereco endereco) {
        this.nome = nome;
        this.idade = idade;
        this.endereco = endereco;
    }

    public abstract void apresentarse();
}


abstract class Funcionario extends Pessoa {
    Loja loja;
    double salarioBase;
    double[] salarioRecebido;

    public Funcionario(String nome, int idade, Loja loja, Endereco endereco, double salarioBase, double[] salarioRecebido) {
        super(nome, idade, endereco);
        this.loja = loja;
        this.salarioBase = salarioBase;
        this.salarioRecebido = salarioRecebido;
    }

    public double calcularMedia() {
        return Arrays.stream(salarioRecebido).average().orElse(0);
    }

    public abstract double calcularBonus();
}

class Vendedor extends Funcionario {
    public Vendedor(String nome, int idade, Loja loja, Endereco endereco, double salarioBase, double[] salarioRecebido) {
        super(nome, idade, loja, endereco, salarioBase, salarioRecebido);
    }

    public void apresentarse() {
        System.out.println("Nome: " + nome);
        System.out.println("Idade: " + idade);
        System.out.println("Loja: " + loja.nomeFantasia);
    }

    public double calcularBonus() {
        return salarioBase * 0.2;
    }
}


class Gerente extends Funcionario {
    public Gerente(String nome, int idade, Loja loja, Endereco endereco, double salarioBase, double[] salarioRecebido) {
        super(nome, idade, loja, endereco, salarioBase, salarioRecebido);
    }

    public void apresentarse() {
        System.out.println("Nome: " + nome);
        System.out.println("Idade: " + idade);
        System.out.println("Loja: " + loja.nomeFantasia);
    }

    public double calcularBonus() {
        return salarioBase * 0.35;
    }
}


class Cliente extends Pessoa {
    public Cliente(String nome, int idade, Endereco endereco) {
        super(nome, idade, endereco);
    }

    public void apresentarse() {
        System.out.println("Nome: " + nome);
        System.out.println("Idade: " + idade);
    }
}


class Loja {
    String nomeFantasia, razaoSocial, cnpj;
    Endereco endereco;
    ArrayList<Vendedor> vendedores = new ArrayList<>();
    ArrayList<Cliente> clientes = new ArrayList<>();

    public Loja(String nomeFantasia, String razaoSocial, String cnpj, Endereco endereco) {
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.endereco = endereco;
    }

    public void adicionarVendedor(Vendedor vendedor) {
        vendedores.add(vendedor);
    }

    public void adicionarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void contarClientes() {
        System.out.println("Total de clientes: " + clientes.size());
    }

    public void contarVendedores() {
        System.out.println("Total de vendedores: " + vendedores.size());
    }

    public void apresentarse() {
        System.out.println("Nome Fantasia: " + nomeFantasia);
        System.out.println("CNPJ: " + cnpj);
        endereco.apresentarLogradouro();
    }
}


class Item {
    int id;
    String nome, tipo;
    double valor;

    public Item(int id, String nome, String tipo, double valor) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.valor = valor;
    }

    public void gerarDescricao() {
        System.out.printf("Item #%d - %s (%s): R$ %.2f\n", id, nome, tipo, valor);
    }
}


class Pedido {
    int id;
    Date dataCriacao, dataPagamento, dataVencimentoReserva;
    Cliente cliente;
    Vendedor vendedor;
    Loja loja;
    ArrayList<Item> itens;

    public Pedido(int id, Date dataCriacao, Date dataPagamento, Date dataVencimentoReserva, Cliente cliente, Vendedor vendedor, Loja loja, ArrayList<Item> itens) {
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.dataPagamento = dataPagamento;
        this.dataVencimentoReserva = dataVencimentoReserva;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.loja = loja;
        this.itens = itens;
    }

    public double calcularValorTotal() {
        return itens.stream().mapToDouble(item -> item.valor).sum();
    }

    public void gerarDescricaoVenda() {
        System.out.println("Pedido criado em: " + dataCriacao);
        System.out.printf("Valor total: R$ %.2f\n", calcularValorTotal());
    }
}


class ProcessaPedido {
    public Pedido processar(int id, Cliente cliente, Vendedor vendedor, Loja loja, ArrayList<Item> itens, Date dataVencimentoReserva) {
        Date agora = new Date();
        if (!confirmarPagamento(dataVencimentoReserva, agora)) {
            System.out.println("Reserva vencida. Pagamento não confirmado.");
            return null;
        }
        return new Pedido(id, agora, agora, dataVencimentoReserva, cliente, vendedor, loja, itens);
    }

    private boolean confirmarPagamento(Date dataVencimento, Date dataAtual) {
        return dataAtual.compareTo(dataVencimento) <= 0;
    }
}


public class Aula7 {
    public static void main(String[] args) {
        Endereco enderecoLoja = new Endereco("PR", "Cidade Verde", "Centro", "123", "Rua das Flores");
        Loja loja = new Loja("My Plant", "My Plant LTDA", "12.345.678/0001-99", enderecoLoja);

        Endereco enderecoVendedor = new Endereco("PR", "Cidade Verde", "Centro", "123", "Rua das Flores");
        Vendedor vendedor = new Vendedor("Carlos", 28, loja, enderecoVendedor, 2000, new double[]{2000.00, 2100.50, 1900.75});

        Endereco enderecoCliente = new Endereco("PR", "Cidade Verde", "Jardim", "456", "Av. das Rosas");
        Cliente cliente = new Cliente("João", 40, enderecoCliente);

        loja.adicionarVendedor(vendedor);
        loja.adicionarCliente(cliente);

        ArrayList<Item> itens = new ArrayList<>();
        itens.add(new Item(1, "Vaso Decorativo", "Decoração", 59.90));
        itens.add(new Item(2, "Samambaia", "Planta", 35.00));

        ProcessaPedido processador = new ProcessaPedido();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2); // vencimento em 2 dias

        Pedido pedido = processador.processar(101, cliente, vendedor, loja, itens, cal.getTime());
        if (pedido != null) pedido.gerarDescricaoVenda();
    }
}

