//Nome: Thiago de Almeida Maciel

package testes;

import dao.AlunoDao;
import jakarta.persistence.EntityManager;
import modelo.Aluno;
import util.JPAUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

public class CrudProduto {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EntityManager em = JPAUtil.getEntityManager();
        AlunoDao dao = new AlunoDao(em);
        boolean flag = true;

        while (flag) {
            System.out.println("** CADASTRO DE ALUNOS **");
            System.out.println("1. Cadastrar Aluno");
            System.out.println("2. Excluir Aluno");
            System.out.println("3. Alterar Aluno");
            System.out.println("4. Buscar aluno pelo nome");
            System.out.println("5. Listar Alunos (com status aprovação)");
            System.out.println("6. FIM");
            System.out.print("Escolha uma opção: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    cadastrarAluno(scanner, dao, em);
                    break;
                case 2:
                    excluirAluno(scanner, dao, em);
                    break;
                case 3:
                    alterarAluno(scanner, dao, em);
                    break;
                case 4:
                    buscarAluno(scanner, dao);
                    break;
                case 5:
                    listarAlunos(dao);
                    break;
                case 6:
                    flag = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
        em.close();
    }

    private static void listarAlunos(AlunoDao dao) {
        List<Aluno> alunos = dao.listarTodos();

        if(alunos.isEmpty()){
            System.out.println("Nenhum aluno cadastrado.");
        }else{
            System.out.println("Exibindo todos os alunos: ");
            for (Aluno aluno : alunos){
                BigDecimal media = calcularMedia(aluno);
                String status = determinarStatus(media);

                System.out.println(STR."Nome: \{aluno.getNome()}");
                System.out.println(STR."Email: \{aluno.getEmail()}");
                System.out.println(STR."RA: \{aluno.getRa()}");
                System.out.println(STR."Notas: \{aluno.getNota1()} - \{aluno.getNota2()} - \{aluno.getNota3()}");
                System.out.println(STR."Média: \{media}");
                System.out.println(STR."Situação: \{status}");
                System.out.println("----------------------------");
            }
        }
    }

    private static BigDecimal calcularMedia(Aluno aluno) {
        BigDecimal soma = aluno.getNota1().add(aluno.getNota2()).add(aluno.getNota3());
        MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
        return soma.divide(new BigDecimal(3), mc);
    }

    private static String determinarStatus(BigDecimal media) {
        BigDecimal aprovado = new BigDecimal("6.0");
        if (media.compareTo(aprovado) >= 0) {
            return "Aprovado";
        }else{
            BigDecimal recuperacao = new BigDecimal("4.0");
            if (media.compareTo(recuperacao) >= 0) {
                return "Recuperação";
            }else{
                return "Reprovado";
            }
        }
    }

    private static void buscarAluno(Scanner scanner, AlunoDao dao) {
        System.out.println("CONSULTAR ALUNO: ");
        System.out.print("Digite o nome: ");
        String nome = scanner.nextLine();

        Aluno aluno = dao.buscarAlunoPorNome(nome);

        if (aluno != null) {
            System.out.println("Dados do aluno:");
            System.out.println(STR."Nome: \{aluno.getNome()}");
            System.out.println(STR."Email: \{aluno.getEmail()}");
            System.out.println(STR."RA: \{aluno.getRa()}");
            System.out.println(STR."Notas: \{aluno.getNota1()} - \{aluno.getNota2()} - \{aluno.getNota3()}");
        }else{
            System.out.println("Aluno não encontrado!");
        }
    }

    private static void alterarAluno(Scanner scanner, AlunoDao dao, EntityManager em) {
        System.out.println("ALTERAR ALUNO: ");
        System.out.print("Digite o nome: ");
        String nome = scanner.nextLine();

        Aluno aluno = dao.buscarAlunoPorNome(nome);

        if (aluno != null) {
            System.out.println("Dados atuais do aluno:");
            System.out.println(STR."Nome: \{aluno.getNome()}");
            System.out.println(STR."Email: \{aluno.getEmail()}");
            System.out.println(STR."RA: \{aluno.getRa()}");
            System.out.println(STR."Notas: \{aluno.getNota1()} - \{aluno.getNota2()} - \{aluno.getNota3()}\n");
            System.out.println("NOVOS DADOS: ");
            System.out.print("Digite o nome: ");
            aluno.setNome(scanner.nextLine());
            System.out.print("Digite o RA: ");
            aluno.setRa(scanner.nextLine());
            System.out.print("Digite o email: ");
            aluno.setEmail(scanner.nextLine());
            System.out.print("Digite a nota 1: ");
            aluno.setNota1(scanner.nextBigDecimal());
            System.out.print("Digite a nota 2: ");
            aluno.setNota2(scanner.nextBigDecimal());
            System.out.print("Digite a nota 3: ");
            aluno.setNota3(scanner.nextBigDecimal());
            scanner.nextLine();

            em.getTransaction().begin();
            dao.atualizar(aluno);
            em.getTransaction().commit();

            System.out.println("Aluno alterado com sucesso!");
        }else{
            System.out.println("Aluno não encontrado!");
        }
    }

    private static void excluirAluno(Scanner scanner, AlunoDao dao, EntityManager em) {
        System.out.println("EXCLUIR ALUNO: ");
        System.out.print("Digite o nome: ");
        String nome = scanner.nextLine();

        Aluno aluno = dao.buscarAlunoPorNome(nome);

        if(aluno != null){
            em.getTransaction().begin();
            dao.excluir(aluno);
            em.getTransaction().commit();
            System.out.println("Aluno excluído com sucesso!");
        }else{
            System.out.println("Aluno não encontrado.");
        }
    }

    private static void cadastrarAluno(Scanner scanner, AlunoDao dao, EntityManager em) {
        System.out.println("CADASTRO DE ALUNO: ");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("RA: ");
        String ra = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Nota 1: ");
        BigDecimal nota1 = scanner.nextBigDecimal();
        System.out.print("Nota 2: ");
        BigDecimal nota2 = scanner.nextBigDecimal();
        System.out.print("Nota 3: ");
        BigDecimal nota3 = scanner.nextBigDecimal();
        scanner.nextLine();

        Aluno aluno = new Aluno(nome, ra, email, nota1, nota2, nota3);

        em.getTransaction().begin();
        dao.cadastrar(aluno);
        em.getTransaction().commit();

        System.out.println("Aluno cadastrado com sucesso!");
    }
}
