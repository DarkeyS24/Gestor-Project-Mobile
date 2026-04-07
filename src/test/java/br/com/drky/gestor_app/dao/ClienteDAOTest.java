package br.com.drky.gestor_app.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.drky.gestor_app.enums.TipoCliente;
import br.com.drky.gestor_app.model.Cliente;
import totalcross.sql.Connection;

@RunWith(MockitoJUnitRunner.class)
public class ClienteDAOTest {

    @Mock
    private Connection mockConn;

    @Mock
    private ClienteDAO mockDao;

    @Before
    public void setUp() {
        mockDao = mock(ClienteDAO.class);
    }

    // =========================================================
    // inserirCliente
    // =========================================================

    @Test
    public void inserirCliente_comEmailPreenchido_deveRetornarSucesso() {
        Cliente c = clienteFisico("João Silva", "529.982.247-25", "34999990000", "joao@email.com");
        when(mockDao.inserirCliente(c)).thenReturn("Cliente inserido com sucesso!!");

        String result = mockDao.inserirCliente(c);

        assertEquals("Cliente inserido com sucesso!!", result);
        verify(mockDao).inserirCliente(c);
    }

    @Test
    public void inserirCliente_comEmailNulo_deveRetornarSucesso() {
        Cliente c = clienteFisico("Maria Costa", "111.444.777-35", "34988880000", null);
        when(mockDao.inserirCliente(c)).thenReturn("Cliente inserido com sucesso!!");

        String result = mockDao.inserirCliente(c);

        assertEquals("Cliente inserido com sucesso!!", result);
    }

    @Test
    public void inserirCliente_juridico_deveRetornarSucesso() {
        Cliente c = clienteJuridico("Empresa LTDA", "11.222.333/0001-81", "34977770000", "contato@empresa.com");
        when(mockDao.inserirCliente(c)).thenReturn("Cliente inserido com sucesso!!");

        String result = mockDao.inserirCliente(c);

        assertEquals("Cliente inserido com sucesso!!", result);
    }

    // =========================================================
    // buscaTodosOsClientes
    // =========================================================

    @Test
    public void buscaTodosOsClientes_semRegistros_deveRetornarListaVazia() {
        when(mockDao.buscaTodosOsClientes()).thenReturn(Collections.emptyList());

        List<Cliente> clientes = mockDao.buscaTodosOsClientes();

        assertNotNull(clientes);
        assertTrue(clientes.isEmpty());
    }

    @Test
    public void buscaTodosOsClientes_comRegistros_deveRetornarTodos() {
        Cliente c1 = clienteFisico("João", "529.982.247-25", "34999990000", null);
        Cliente c2 = clienteFisico("Maria", "111.444.777-35", "34988880000", null);
        when(mockDao.buscaTodosOsClientes()).thenReturn(Arrays.asList(c1, c2));

        List<Cliente> clientes = mockDao.buscaTodosOsClientes();

        assertEquals(2, clientes.size());
    }

    @Test
    public void buscaTodosOsClientes_naoDeveRetornarExcluidos() {
        Cliente ativo = clienteFisico("Ativo", "529.982.247-25", "34999990000", null);
        ativo.setCodigo(1);
        ativo.setExcluido(false);

        Cliente excluido = clienteFisico("Excluido", "111.444.777-35", "34988880000", null);
        excluido.setCodigo(2);
        excluido.setExcluido(true);

        when(mockDao.buscaTodosOsClientes())
                .thenReturn(Arrays.asList(ativo, excluido))
                .thenReturn(Collections.singletonList(ativo));
        when(mockDao.excluirClienteLogico(2)).thenReturn("Cliente Excluido com sucesso!!");

        List<Cliente> todos = mockDao.buscaTodosOsClientes();
        mockDao.excluirClienteLogico(todos.get(1).getCodigo());

        List<Cliente> ativos = mockDao.buscaTodosOsClientes();

        assertEquals(1, ativos.size());
        assertEquals("Ativo", ativos.get(0).getNome());
    }

    // =========================================================
    // buscaClientePorId
    // =========================================================

    @Test
    public void buscaClientePorId_existente_deveRetornarCliente() {
        Cliente c = clienteFisico("Carlos", "529.982.247-25", "34911110000", "carlos@email.com");
        c.setCodigo(1);
        when(mockDao.buscaClientePorId(1)).thenReturn(c);

        Cliente found = mockDao.buscaClientePorId(1);

        assertNotNull(found);
        assertEquals("Carlos", found.getNome());
        assertEquals("carlos@email.com", found.getEmail());
    }

    @Test
    public void buscaClientePorId_inexistente_deveRetornarNull() {
        when(mockDao.buscaClientePorId(9999)).thenReturn(null);

        Cliente found = mockDao.buscaClientePorId(9999);

        assertNull(found);
    }

    // =========================================================
    // atualizarCliente
    // =========================================================

    @Test
    public void atualizarCliente_deveAlterarTelefoneEEmail() {
        Cliente c = clienteFisico("Ana", "529.982.247-25", "34999990000", "ana@email.com");
        c.setCodigo(1);

        Cliente atualizado = clienteFisico("Ana", "529.982.247-25", "34000000001", "novo@email.com");
        atualizado.setCodigo(1);

        when(mockDao.atualizarCliente(c)).thenReturn("Cliente atualizado com sucesso!!");
        when(mockDao.buscaClientePorId(1)).thenReturn(atualizado);

        c.setTelefone("34000000001");
        c.setEmail("novo@email.com");

        String result = mockDao.atualizarCliente(c);
        assertEquals("Cliente atualizado com sucesso!!", result);

        Cliente retornado = mockDao.buscaClientePorId(c.getCodigo());
        assertEquals("34000000001", retornado.getTelefone());
        assertEquals("novo@email.com", retornado.getEmail());
    }

    @Test
    public void atualizarCliente_comEmailNulo_deveDefinirEmailComoNull() {
        Cliente c = clienteFisico("Pedro", "529.982.247-25", "34999990000", "pedro@email.com");
        c.setCodigo(1);
        c.setEmail(null);

        Cliente semEmail = clienteFisico("Pedro", "529.982.247-25", "34999990000", null);
        semEmail.setCodigo(1);

        when(mockDao.atualizarCliente(c)).thenReturn("Cliente atualizado com sucesso!!");
        when(mockDao.buscaClientePorId(1)).thenReturn(semEmail);

        mockDao.atualizarCliente(c);

        Cliente atualizado = mockDao.buscaClientePorId(1);
        assertNull(atualizado.getEmail());
    }

    @Test
    public void atualizarCliente_deveMarcaComoNaoSincronizado() {
        Cliente c = clienteFisico("Lucas", "529.982.247-25", "34999990000", null);
        c.setCodigo(1);

        Cliente sincronizado = clienteFisico("Lucas", "529.982.247-25", "34999990000", null);
        sincronizado.setCodigo(1);
        sincronizado.setSincronizado(true);

        Cliente naoSincronizado = clienteFisico("Lucas", "529.982.247-25", "34111110000", null);
        naoSincronizado.setCodigo(1);
        naoSincronizado.setSincronizado(false);

        when(mockDao.buscaClientePorId(1))
                .thenReturn(sincronizado)
                .thenReturn(naoSincronizado);
        when(mockDao.atualizarCliente(c)).thenReturn("Cliente atualizado com sucesso!!");

        mockDao.atualizarStatusSincronizacaoCliente(1);

        Cliente sync = mockDao.buscaClientePorId(1);
        assertTrue(sync.getSincronizado());

        c.setTelefone("34111110000");
        mockDao.atualizarCliente(c);

        Cliente posAtualizar = mockDao.buscaClientePorId(1);
        assertFalse(posAtualizar.getSincronizado());
    }

    // =========================================================
    // excluirClienteLogico
    // =========================================================

    @Test
    public void excluirClienteLogico_deveMarcarComoExcluido() {
        Cliente c = clienteFisico("Bruno", "529.982.247-25", "34999990000", null);
        c.setCodigo(1);
        c.setExcluido(true);

        when(mockDao.excluirClienteLogico(1)).thenReturn("Cliente Excluido com sucesso!!");
        when(mockDao.buscaTodosOsClientes()).thenReturn(Collections.emptyList());
        when(mockDao.buscaTodosOsClientesExcluidos()).thenReturn(Collections.singletonList(c));

        String result = mockDao.excluirClienteLogico(1);
        assertEquals("Cliente Excluido com sucesso!!", result);

        List<Cliente> ativos = mockDao.buscaTodosOsClientes();
        assertTrue(ativos.isEmpty());

        List<Cliente> excluidos = mockDao.buscaTodosOsClientesExcluidos();
        assertEquals(1, excluidos.size());
        assertTrue(excluidos.get(0).getExcluido());
    }

    // =========================================================
    // atualizarStatusSincronizacaoCliente
    // =========================================================

    @Test
    public void atualizarStatusSincronizacao_deveMarcarComoSincronizado() {
        Cliente c = clienteFisico("Fernanda", "529.982.247-25", "34999990000", null);
        c.setCodigo(1);
        c.setSincronizado(false);

        Cliente sincronizado = clienteFisico("Fernanda", "529.982.247-25", "34999990000", null);
        sincronizado.setCodigo(1);
        sincronizado.setSincronizado(true);

        when(mockDao.buscaTodosOsClientes()).thenReturn(Collections.singletonList(c));
        when(mockDao.buscaClientePorId(1))
                .thenReturn(c)
                .thenReturn(sincronizado);

        List<Cliente> lista = mockDao.buscaTodosOsClientes();
        Cliente original = mockDao.buscaClientePorId(lista.get(0).getCodigo());
        assertFalse(original.getSincronizado());

        mockDao.atualizarStatusSincronizacaoCliente(1);

        Cliente atualizado = mockDao.buscaClientePorId(1);
        assertTrue(atualizado.getSincronizado());
    }

    // =========================================================
    // buscaTodosOsClientesNaoSincronizados
    // =========================================================

    @Test
    public void buscaNaoSincronizados_deveRetornarApenasNaoSincronizados() {
        Cliente a = clienteFisico("A", "529.982.247-25", "34911110000", null);
        a.setCodigo(1);
        a.setSincronizado(true);

        Cliente b = clienteFisico("B", "111.444.777-35", "34922220000", null);
        b.setCodigo(2);
        b.setSincronizado(false);

        when(mockDao.buscaTodosOsClientes()).thenReturn(Arrays.asList(a, b));
        when(mockDao.buscaTodosOsClientesNaoSincronizados()).thenReturn(Collections.singletonList(b));

        List<Cliente> todos = mockDao.buscaTodosOsClientes();
        mockDao.atualizarStatusSincronizacaoCliente(todos.get(0).getCodigo());

        List<Cliente> naoSync = mockDao.buscaTodosOsClientesNaoSincronizados();
        assertEquals(1, naoSync.size());
        assertEquals("B", naoSync.get(0).getNome());
    }

    // =========================================================
    // excluirCliente (exclusão física)
    // =========================================================

    @Test
    public void excluirCliente_deveRemoverRegistroFisicamente() {
    	Cliente c = clienteFisico("Teste", "529.982.247-25", "34999990000", null);
        c.setCodigo(1);
        
        when(mockDao.buscaClientePorId(1))
                .thenReturn(c)
                .thenReturn(null);
        
        Cliente antes = mockDao.buscaClientePorId(1);
        assertNotNull(antes);
        
        mockDao.excluirCliente(1);
        
        Cliente encontrado = mockDao.buscaClientePorId(1);
        
        assertNull(encontrado);
    }

    // =========================================================
    // Helpers
    // =========================================================

    private Cliente clienteFisico(String nome, String cpf, String telefone, String email) {
        Cliente c = new Cliente();
        c.setNome(nome);
        c.setTipo(TipoCliente.FISICO);
        c.setCpfCnpj(cpf);
        c.setTelefone(telefone);
        c.setEmail(email);
        c.setSincronizado(false);
        c.setExcluido(false);
        return c;
    }

    private Cliente clienteJuridico(String nome, String cnpj, String telefone, String email) {
        Cliente c = new Cliente();
        c.setNome(nome);
        c.setTipo(TipoCliente.JURIDICO);
        c.setCpfCnpj(cnpj);
        c.setTelefone(telefone);
        c.setEmail(email);
        c.setSincronizado(false);
        c.setExcluido(false);
        return c;
    }
}