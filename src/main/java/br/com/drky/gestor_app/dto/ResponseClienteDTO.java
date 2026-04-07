package br.com.drky.gestor_app.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.drky.gestor_app.model.Cliente;

public class ResponseClienteDTO {

	private String codigo;
	private String nome;
	private String tipo;
	private String cpfCnpj;
	private String telefone;
	private String email;
	private String sincronizado;
	private String excluido;

	public ResponseClienteDTO() {
	}
	
	public ResponseClienteDTO(Cliente cliente) {

		this.codigo = cliente.getCodigo().toString();
		this.nome = cliente.getNome();
		this.tipo = cliente.getTipo().toString();
		this.cpfCnpj = cliente.getCpfCnpj();
		this.telefone = cliente.getTelefone();
		this.email = cliente.getEmail();
		this.sincronizado = cliente.getSincronizado().toString();
		this.excluido = cliente.getExcluido().toString();
	}

	public static List<ResponseClienteDTO> toDtoList(List<Cliente> clientes) {
		List<ResponseClienteDTO> list = new ArrayList<>();

		for (Cliente c : clientes) {
			ResponseClienteDTO dto = new ResponseClienteDTO(c);
			list.add(dto);
		}

		return list;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}

	public String getExcluido() {
		return excluido;
	}

	public void setExcluido(String excluido) {
		this.excluido = excluido;
	}
}