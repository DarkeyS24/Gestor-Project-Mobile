package br.com.drky.gestor_app.dto;

public class RequestVerificacaoDTO {
	
	private String tipo;
	private String documento;
	
	public RequestVerificacaoDTO() {
	}
	
	public RequestVerificacaoDTO(String tipo, String documento) {
		this.tipo = tipo;
		this.documento = documento;
	}
	
	public String toJson() {
		return "{\"tipo\":\"" + tipo + "\",\"documento\":\"" + documento + "\"}";
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
}