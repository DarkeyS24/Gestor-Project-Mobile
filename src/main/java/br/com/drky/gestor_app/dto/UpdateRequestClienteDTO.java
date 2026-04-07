package br.com.drky.gestor_app.dto;

public class UpdateRequestClienteDTO {

	private String telefone; 
	private String email;

	public UpdateRequestClienteDTO() {
	}
	
	public UpdateRequestClienteDTO(String telefone, String email) {
		this.telefone = telefone;
		this.email = email;
	}
	
	public String toJson() {
		return "{\"telefone\":\"" + telefone + "\",\"email\":\"" + email + "\"}";
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
}