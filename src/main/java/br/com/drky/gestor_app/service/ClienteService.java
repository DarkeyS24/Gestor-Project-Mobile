package br.com.drky.gestor_app.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import br.com.drky.gestor_app.dao.ClienteDAO;
import br.com.drky.gestor_app.dto.CreateRequestClienteDTO;
import br.com.drky.gestor_app.dto.RequestVerificacaoDTO;
import br.com.drky.gestor_app.dto.UpdateRequestClienteDTO;
import br.com.drky.gestor_app.exception.ConnectionErrorException;
import br.com.drky.gestor_app.exception.NotElementsFoundException;
import br.com.drky.gestor_app.model.Cliente;
import totalcross.net.HttpStream;
import totalcross.net.URI;
import totalcross.util.Hashtable;

public class ClienteService {

	private ClienteDAO dao = new ClienteDAO();
	private Integer statusSync = 0;

	//private HttpClient client = HttpClient.newHttpClient();

	public boolean verificarCPF(String CPF) {

		CPF = CPF.replace("-", "").replace(".", "");

		if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222")
				|| CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555")
				|| CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888")
				|| CPF.equals("99999999999") || (CPF.length() != 11))
			return (false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		try {
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {

				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else
				dig10 = (char) (r + 48);

			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else
				dig11 = (char) (r + 48);

			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

	public boolean verificarCNPJ(String CNPJ) {

		CNPJ = CNPJ.replace("-", "").replace(".", "").replace("/", "");

		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") || CNPJ.equals("22222222222222")
				|| CNPJ.equals("33333333333333") || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555")
				|| CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") || CNPJ.equals("88888888888888")
				|| CNPJ.equals("99999999999999") || (CNPJ.length() != 14))
			return (false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		try {
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {

				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);

			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

	 public Integer sincronizarClientes() {
	        try {
	            excluirClientesSync();
	            updateCreateClientesSync();
	            return statusSync;
	        } catch (ConnectionErrorException | NotElementsFoundException e) {
	            return statusSync == 1 ? 1 : 2;
	        }
	    }

	 @SuppressWarnings({ "unchecked", "rawtypes" })
	private String enviarRequisicao(String url, String metodo, String json) throws ConnectionErrorException {
        HttpStream stream = null;
        try {
            HttpStream.Options options = new HttpStream.Options();
            options.httpType = metodo; 
            if (json != null && !json.isEmpty()) {
                Hashtable headers = new Hashtable(5);
                headers.put("Content-Type", "application/json");
                options.postHeaders = (Hashtable) headers;  
                options.data = json;            
            }
            stream = new HttpStream(new URI(url), options);
            byte[] buf = new byte[4096];
            int n = stream.readBytes(buf, 0, buf.length);
            String responseBody = (n > 0) ? new String(buf, 0, n) : "";
            System.out.println("URL: " + url);
            System.out.println("Status: " + stream.responseCode);
            System.out.println("Corpo: " + responseBody);
            return responseBody;
        } catch (IOException e) {
            this.statusSync = 2;
            throw new ConnectionErrorException("Erro ao tentar se-conectar ao servidor");
        } finally {
            if (stream != null) {
                try { stream.close(); } catch (IOException e) { }
            }
        }
    }
	 
    private int obterStatusCode(String url, String metodo, String json) throws ConnectionErrorException {
        HttpStream stream = null;
        try {
            HttpStream.Options options = new HttpStream.Options();
            options.httpType = metodo;
            if (json != null && !json.isEmpty()) {
            	Hashtable headers = new Hashtable(5);
                headers.put("Content-Type", "application/json");
                options.postHeaders = (Hashtable) headers;  
                options.data = json;  
            }
            stream = new HttpStream(new URI(url), options);
            byte[] buf = new byte[4096];
            stream.readBytes(buf, 0, buf.length);
            
            int statusCode = stream.responseCode;
            System.out.println("URL: " + url);
            System.out.println("Metodo: " + metodo);
            System.out.println("Status: " + statusCode);
            return statusCode;
        } catch (IOException e) {
            this.statusSync = 2;
            throw new ConnectionErrorException("Erro ao tentar se-conectar ao servidor");
        } finally {
            if (stream != null) {
                try { stream.close(); } catch (IOException e) { }
            }
        }
    }
    private void updateCreateClientesSync() {
        List<Cliente> clientes = dao.buscaTodosOsClientesNaoSincronizados();
        List<Boolean> bools = new ArrayList<>();
        if (clientes.size() <= 0) {
            this.statusSync = 1;
        }
        for (Cliente c : clientes) {
            String json = new RequestVerificacaoDTO(c.getTipo().toString(), c.getCpfCnpj()).toJson();
            String responseBody = enviarRequisicao(
                "http://localhost:8080/clientes/verificar",
                HttpStream.POST,
                json
            );
            bools.add(Boolean.valueOf(responseBody));
        }
        for (int i = 0; i < bools.size(); i++) {
            String json;
            String metodo;
            String url;
            if (bools.get(i)) {
                json = new UpdateRequestClienteDTO(
                    clientes.get(i).getTelefone(),
                    clientes.get(i).getEmail()
                ).toJson();
                metodo = HttpStream.PUT;
                url = "http://localhost:8080/clientes/" + clientes.get(i).getCodigo();
            } else {
                json = new CreateRequestClienteDTO(clientes.get(i)).toJson();
                metodo = HttpStream.POST;
                url = "http://localhost:8080/clientes";
            }
            int statusCode = obterStatusCode(url, metodo, json);
            if (statusCode == 200) {
                statusSync = 0;
                dao.atualizarStatusSincronizacaoCliente(clientes.get(i).getCodigo());
            }
        }
    }
    private void excluirClientesSync() {
        List<Cliente> clientes = dao.buscaTodosOsClientesExcluidos();
        List<Boolean> bools = new ArrayList<>();
        if (clientes.size() <= 0) {
            this.statusSync = 1;
        }
        for (Cliente c : clientes) {
            String json = new RequestVerificacaoDTO(c.getTipo().toString(), c.getCpfCnpj()).toJson();
            String responseBody = enviarRequisicao(
                "http://localhost:8080/clientes/verificar",
                HttpStream.POST,
                json
            );
            bools.add(Boolean.valueOf(responseBody));
        }
        for (int i = 0; i < bools.size(); i++) {
            if (bools.get(i)) {
                int statusCode = obterStatusCode(
                    "http://localhost:8080/clientes/" + clientes.get(i).getCodigo(),
                    HttpStream.DELETE,
                    null
                );
                if (statusCode == 200) {
                    statusSync = 0;
                    dao.excluirCliente(clientes.get(i).getCodigo());
                }
            }
        }
    }
}
	
	
	
	
//	public Integer sincronizarClientes() {
//		setHttpClient();
//		try {
//			excluirClientesSync();
//			updateCreateClientesSync();
//			return statusSync;
//		} catch (ConnectionErrorException | NotElementsFoundException e) {
//			return statusSync == 1 ? 1 : 2;
//		}
//	}
//
//	private void setHttpClient() {
//		this.client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5))
//				.followRedirects(HttpClient.Redirect.NORMAL).build();
//	}
//
//	private void updateCreateClientesSync() {
//		List<Cliente> clientes = dao.buscaTodosOsClientesNaoSincronizados();
//		List<Boolean> bools = new ArrayList<>();
//
//		if (clientes.size() <= 0) 
//			this.statusSync = 1;
//
//		for (Cliente c : clientes) {
//
//			ObjectMapper mapper = new ObjectMapper();
//			String json = "";
//			try {
//				json = mapper.writeValueAsString(new RequestVerificacaoDTO(c.getTipo().toString(), c.getCpfCnpj()));
//			} catch (JsonProcessingException e) {
//				return;
//			}
//
//			HttpRequest requestPost = HttpRequest.newBuilder()
//					.uri(URI.create("http://localhost:8080/clientes/verificar"))
//					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
//
//			HttpResponse<String> response;
//			try {
//				response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
//				bools.add(Boolean.valueOf(response.body()));
//
//				System.out.println("Status: " + response.statusCode());
//				System.out.println("Cuerpo: " + response.body());
//			} catch (IOException | InterruptedException e) {
//				this.statusSync = 2;
//				throw new ConnectionErrorException("Erro ao tentar se-conectar ao servidor");
//			}
//		}
//		;
//
//		for (int i = 0; i < bools.size(); i++) {
//
//			ObjectMapper mapper = new ObjectMapper();
//			String json = "";
//			HttpRequest request = null;
//
//			if (bools.get(i)) {
//
//				try {
//					json = mapper.writeValueAsString(
//							new UpdateRequestClienteDTO(clientes.get(i).getTelefone(), clientes.get(i).getEmail()));
//				} catch (JsonProcessingException e) {
//					return;
//				}
//
//				request = HttpRequest.newBuilder()
//						.uri(URI.create("http://localhost:8080/clientes/" + clientes.get(i).getCodigo()))
//						.header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(json))
//						.build();
//			} else {
//
//				try {
//					json = mapper.writeValueAsString(new CreateRequestClienteDTO(clientes.get(i)));
//				} catch (JsonProcessingException e) {
//					return;
//				}
//
//				request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/clientes"))
//						.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
//						.build();
//			}
//
//			HttpResponse<String> response;
//			try {
//				response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//				System.out.println("Metodo: " + response.request().method());
//				System.out.println("Status: " + response.statusCode());
//				System.out.println("Cuerpo: " + response.body() + "\n");
//
//				if (response.statusCode() == 200) {
//					statusSync = 0;
//					dao.atualizarStatusSincronizacaoCliente(clientes.get(i).getCodigo());
//				}
//			} catch (IOException | InterruptedException e) {
//				this.statusSync = 2;
//				throw new ConnectionErrorException("Erro ao tentar se-conectar ao servidor");
//			}
//		}
//	}
//
//	private void excluirClientesSync() {
//		List<Cliente> clientes = dao.buscaTodosOsClientesExcluidos();
//		List<Boolean> bools = new ArrayList<>();
//
//		if (clientes.size() <= 0) 
//			this.statusSync = 1;
//		
//
//		for (Cliente c : clientes) {
//
//			ObjectMapper mapper = new ObjectMapper();
//			String json = "";
//			try {
//				json = mapper.writeValueAsString(new RequestVerificacaoDTO(c.getTipo().toString(), c.getCpfCnpj()));
//			} catch (JsonProcessingException e) {
//				return;
//			}
//
//			HttpRequest requestPost = HttpRequest.newBuilder()
//					.uri(URI.create("http://localhost:8080/clientes/verificar"))
//					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
//
//			HttpResponse<String> response;
//			try {
//				response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
//				bools.add(Boolean.valueOf(response.body()));
//
//				System.out.println("Status: " + response.statusCode());
//				System.out.println("Cuerpo: " + response.body());
//			} catch (IOException | InterruptedException e) {
//				this.statusSync = 2;
//				throw new ConnectionErrorException("Erro ao tentar se-conectar ao servidor");
//			}
//		}
//
//		for (int i = 0; i < bools.size(); i++) {
//
//			HttpRequest request = null;
//
//			if (bools.get(i)) {
//
//				request = HttpRequest.newBuilder()
//						.uri(URI.create("http://localhost:8080/clientes/" + clientes.get(i).getCodigo())).DELETE()
//						.build();
//
//				HttpResponse<String> response;
//				try {
//					response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//					System.out.println("Metodo: " + response.request().method());
//					System.out.println("Status: " + response.statusCode());
//					System.out.println("Cuerpo: " + response.body() + "\n");
//
//					if (response.statusCode() == 200) {
//						statusSync = 0;
//						dao.excluirCliente(clientes.get(i).getCodigo());
//					}
//				} catch (IOException | InterruptedException e) {
//					this.statusSync = 2;
//					throw new ConnectionErrorException("Erro ao tentar se-conectar ao servidor");
//				}
//			}
//		}
//	}
//}