package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import modelo.Aluno;

import java.util.List;

public class AlunoDao {
    private final EntityManager em;
    public AlunoDao(EntityManager em) {
        this.em = em;
    }
    public void cadastrar(Aluno aluno) {
        this.em.persist(aluno);
    }

    public Aluno buscarAlunoPorNome(String nome){
        try{
            String jpql = "SELECT a FROM Aluno a WHERE a.nome = :nome";
            return em.createQuery(jpql, Aluno.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    public void excluir(Aluno aluno){
        if(em.contains(aluno)){
            em.remove(aluno);
        }else{
            em.remove(em.merge(aluno));
        }
    }

    public void atualizar(Aluno aluno){
        em.merge(aluno);
    }

    public List<Aluno> listarTodos(){
        String jpql = "SELECT a FROM Aluno a";
        return em.createQuery(jpql, Aluno.class).getResultList();
    }
}
