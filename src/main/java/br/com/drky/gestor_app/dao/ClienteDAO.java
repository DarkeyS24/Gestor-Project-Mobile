package br.com.drky.gestor_app.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.drky.gestor_app.enums.TipoCliente;
import br.com.drky.gestor_app.model.Cliente;
import br.com.drky.gestor_app.util.DatabaseManager;
import totalcross.sql.Connection;
import totalcross.sql.PreparedStatement;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class ClienteDAO {
	
	public String inserirCliente(Cliente c) {

		Connection db = DatabaseManager.getConnection();
		PreparedStatement pstmt = null;
	    String sql = "INSERT INTO tbcliente(nome, tipo, cpfCnpj, sincronizado, excluido, telefone, email) VALUES(?,?,?,?,?,?,?)";
	    
	    try {
	    	
	        pstmt = db.prepareStatement(sql);
	        
	        pstmt.setString(1, c.getNome());
	        pstmt.setString(2, c.getTipo().toString());
	        pstmt.setString(3, c.getCpfCnpj());
	        pstmt.setInt(4, c.getSincronizado() ? 1 : 0);
	        pstmt.setInt(5, c.getExcluido() ? 1 : 0);
	        pstmt.setString(6, c.getTelefone());
	        
	        if (c.getEmail() == null) {
	            pstmt.setNull(7, totalcross.sql.Types.VARCHAR);
	        } else {
	            pstmt.setString(7, c.getEmail());
	        }
	        
	        int rows = pstmt.executeUpdate();
	        
	        if (rows > 0) {
	            return "Cliente inserido com sucesso!!";
	        } else {
	            return "Erro: Nenhum registro foi inserido.";
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Erro ao inserir: " + e.getMessage();
	    } finally {
	        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	    }
	}
	
	public List<Cliente> buscaTodosOsClientes() {

		Connection db = DatabaseManager.getConnection();
		List<Cliente> clientes = new ArrayList<>();
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	        stmt = db.createStatement();
	        rs = stmt.executeQuery("SELECT * FROM tbcliente WHERE excluido = 0");
	        
	        while (rs.next()) {
	        	Cliente c = new Cliente();
	            c.setCodigo(rs.getInt("codigo"));
	            c.setNome(rs.getString("nome"));
	            c.setTipo(TipoCliente.valueOf(rs.getString("tipo").toUpperCase()));
	            c.setCpfCnpj(rs.getString("cpfCnpj"));
	            c.setTelefone(rs.getString("telefone"));
	            c.setSincronizado(rs.getInt("sincronizado") == 1 ? true : false);
	            c.setExcluido(rs.getInt("excluido") == 1 ? true : false);
	            c.setEmail(rs.getString("email"));
	            
	            clientes.add(c);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	    }
	    return clientes;
	}
	
	public Cliente buscaClientePorId(Integer Id) {

		Connection db = DatabaseManager.getConnection();
		Cliente cliente = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    String sql = "SELECT * FROM tbcliente WHERE codigo = ?";
	    
	    try {
	    	
	    	pstmt = db.prepareStatement(sql);
	        pstmt.setInt(1, Id);
	        
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	        	cliente = new Cliente();
	        	cliente.setCodigo(rs.getInt("codigo"));
	        	cliente.setNome(rs.getString("nome"));
	        	cliente.setTipo(TipoCliente.valueOf(rs.getString("tipo").toUpperCase()));
	        	cliente.setCpfCnpj(rs.getString("cpfCnpj"));
	            cliente.setTelefone(rs.getString("telefone"));
	        	cliente.setSincronizado(rs.getInt("sincronizado") == 1 ? true : false);
	        	cliente.setExcluido(rs.getInt("excluido") == 1 ? true : false);
	        	cliente.setEmail(rs.getString("email"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	    }
	    return cliente;
	}
	
	public List<Cliente> buscaTodosOsClientesExcluidos() {

		Connection db = DatabaseManager.getConnection();
		List<Cliente> clientes = new ArrayList<>();
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	        stmt = db.createStatement();
	        rs = stmt.executeQuery("SELECT * FROM tbcliente WHERE excluido = 1");
	        
	        while (rs.next()) {
	        	Cliente c = new Cliente();
	            c.setCodigo(rs.getInt("codigo"));
	            c.setNome(rs.getString("nome"));
	            c.setTipo(TipoCliente.valueOf(rs.getString("tipo").toUpperCase()));
	            c.setCpfCnpj(rs.getString("cpfCnpj"));
	            c.setTelefone(rs.getString("telefone"));
	            c.setSincronizado(rs.getInt("sincronizado") == 1 ? true : false);
	            c.setExcluido(rs.getInt("excluido") == 1 ? true : false);
	            c.setEmail(rs.getString("email"));
	            
	            clientes.add(c);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	    }
	    return clientes;
	}
	
	public List<Cliente> buscaTodosOsClientesNaoSincronizados() {

		Connection db = DatabaseManager.getConnection();
		List<Cliente> clientes = new ArrayList<>();
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	        stmt = db.createStatement();
	        rs = stmt.executeQuery("SELECT * FROM tbcliente WHERE sincronizado = 0 AND excluido = 0");
	        
	        while (rs.next()) {
	        	Cliente c = new Cliente();
	            c.setCodigo(rs.getInt("codigo"));
	            c.setNome(rs.getString("nome"));
	            c.setTipo(TipoCliente.valueOf(rs.getString("tipo").toUpperCase()));
	            c.setCpfCnpj(rs.getString("cpfCnpj"));
	            c.setTelefone(rs.getString("telefone"));
	            c.setSincronizado(rs.getInt("sincronizado") == 1 ? true : false);
	            c.setExcluido(rs.getInt("excluido") == 1 ? true : false);
	            c.setEmail(rs.getString("email"));
	            
	            clientes.add(c);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	    }
	    return clientes;
	}

	public String atualizarStatusSincronizacaoCliente(Integer id) {
		Connection db = DatabaseManager.getConnection();
		PreparedStatement pstmt = null;
	    String sql = "UPDATE tbcliente SET sincronizado = 1 WHERE codigo = ?";
	    
	    try {
	    	
	        pstmt = db.prepareStatement(sql);
	        
	        pstmt.setInt(1, id);
	        
	        int rows = pstmt.executeUpdate();
	        
	        if (rows > 0) {
	            return "Cliente atualizado com sucesso!!";
	        } else {
	            return "Erro: Nenhum registro foi atualizado.";
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Erro ao atualizar: " + e.getMessage();
	    } finally {
	        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	    }
	}
	
	public String atualizarCliente(Cliente c) {
		Connection db = DatabaseManager.getConnection();
		PreparedStatement pstmt = null;
	    String sql = "UPDATE tbcliente SET telefone = ?, email = ?, sincronizado = 0 WHERE codigo = ?";
	    
	    try {
	    	
	        pstmt = db.prepareStatement(sql);
	        
	        pstmt.setString(1, c.getTelefone());
	        if (c.getEmail() == null) {
	            pstmt.setNull(2, totalcross.sql.Types.VARCHAR);
	        } else {
	            pstmt.setString(2, c.getEmail());
	        }
	        pstmt.setInt(3, c.getCodigo());
	        
	        int rows = pstmt.executeUpdate();
	        
	        if (rows > 0) {
	            return "Cliente atualizado com sucesso!!";
	        } else {
	            return "Erro: Nenhum registro foi atualizado.";
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Erro ao atualizar: " + e.getMessage();
	    } finally {
	        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	    }
	}
	
	public String excluirClienteLogico(Integer id) {
		Connection db = DatabaseManager.getConnection();
		PreparedStatement pstmt = null;
	    String sql = "UPDATE tbcliente SET excluido = 1, sincronizado = 0 WHERE codigo = ?";
	    
	    try {
	    	
	        pstmt = db.prepareStatement(sql);
	        pstmt.setInt(1, id);
	        
	        int rows = pstmt.executeUpdate();
	        
	        if (rows > 0) {
	            return "Cliente Excluido com sucesso!!";
	        } else {
	            return "Erro: Nenhum registro foi excluido.";
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Erro ao excluir: " + e.getMessage();
	    } finally {
	        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	    }
	}
	
	public void excluirCliente(Integer id) {
		Connection db = DatabaseManager.getConnection();
		PreparedStatement pstmt = null;
	    String sql = "DELETE FROM tbcliente WHERE codigo = ?";
	    
	    try {
	    	
	        pstmt = db.prepareStatement(sql);
	        pstmt.setInt(1, id);
	        
	        pstmt.executeUpdate();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	    }
	}
	
	public String estaCadastrado(Cliente newCliente) {
		Connection db = DatabaseManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean vf = false;
	    String sql = "SELECT * FROM tbcliente WHERE cpfCnpj = ?";
	    
	    try {
	    	
	    	pstmt = db.prepareStatement(sql);
	        pstmt.setString(1, newCliente.getCpfCnpj());
	        
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	        	newCliente.setCodigo(rs.getInt("codigo"));
	        	estaCadastradoMasExcluidoEntaoAtualiza(newCliente);
	        	vf = true;
	        }	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	    }
	    
	    if(vf)
	    	return "Cliente Excluido com sucesso!!";
	    
	    return null;
	}

	public Boolean estaCadastradoMasExcluidoEntaoAtualiza(Cliente cliente) {
		Connection db = DatabaseManager.getConnection();
		PreparedStatement pstmt = null;
	    String sql = "UPDATE tbcliente SET nome = ?, sincronizado = 0, excluido = 0, telefone = ?, email = ? WHERE codigo = ?";
	    
	    try {
	    	
	        pstmt = db.prepareStatement(sql);
	        
	        pstmt.setString(1, cliente.getNome());
	        pstmt.setString(2, cliente.getTelefone());
	        
	        if (cliente.getEmail() == null) {
	            pstmt.setNull(3, totalcross.sql.Types.VARCHAR);
	        } else {
	            pstmt.setString(3, cliente.getEmail());
	        }
	        
	        pstmt.setInt(4, cliente.getCodigo());
	        
	        int row = pstmt.executeUpdate();
	        
	        if(row > 0) {
	        	return true;
	        }else {
	        	return false;
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    	return false;
	    } finally {
	        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	    }
	}
}