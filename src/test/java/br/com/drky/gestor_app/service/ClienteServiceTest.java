package br.com.drky.gestor_app.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClienteServiceTest {

	private ClienteService service;

    @Before
    public void setUp() {
        service = new ClienteService();
    }

    // =========================================================
    // verificarCPF — casos válidos
    // =========================================================

    @Test
    public void cpfValido_semMascara_deveRetornarTrue() {
        assertTrue(service.verificarCPF("52998224725"));
    }

    @Test
    public void cpfValido_comMascara_deveRetornarTrue() {
        assertTrue(service.verificarCPF("529.982.247-25"));
    }

    @Test
    public void cpfValido_outroExemplo_deveRetornarTrue() {
        assertTrue(service.verificarCPF("111.444.777-35"));
    }

    // =========================================================
    // verificarCPF — casos inválidos
    // =========================================================

    @Test
    public void cpfInvalido_digitosVerificadoresErrados_deveRetornarFalse() {
        assertFalse(service.verificarCPF("123.456.789-00"));
    }

    @Test
    public void cpfInvalido_sequenciaDeZeros_deveRetornarFalse() {
        assertFalse(service.verificarCPF("000.000.000-00"));
    }

    @Test
    public void cpfInvalido_sequenciaDeUns_deveRetornarFalse() {
        assertFalse(service.verificarCPF("111.111.111-11"));
    }

    @Test
    public void cpfInvalido_sequenciaDeDois_deveRetornarFalse() {
        assertFalse(service.verificarCPF("222.222.222-22"));
    }

    @Test
    public void cpfInvalido_sequenciaDeTres_deveRetornarFalse() {
        assertFalse(service.verificarCPF("333.333.333-33"));
    }

    @Test
    public void cpfInvalido_sequenciaDeNoves_deveRetornarFalse() {
        assertFalse(service.verificarCPF("999.999.999-99"));
    }

    @Test
    public void cpfInvalido_menosDigitos_deveRetornarFalse() {
        assertFalse(service.verificarCPF("1234567"));
    }

    @Test
    public void cpfInvalido_maisDigitos_deveRetornarFalse() {
        assertFalse(service.verificarCPF("123456789012"));
    }

    @Test
    public void cpfInvalido_vazio_deveRetornarFalse() {
        assertFalse(service.verificarCPF(""));
    }

    @Test
    public void cpfInvalido_apenasLetras_deveRetornarFalse() {
        assertFalse(service.verificarCPF("abcdefghijk"));
    }

    // =========================================================
    // verificarCNPJ — casos válidos
    // =========================================================

    @Test
    public void cnpjValido_semMascara_deveRetornarTrue() {
        assertTrue(service.verificarCNPJ("11222333000181"));
    }

    @Test
    public void cnpjValido_comMascara_deveRetornarTrue() {
        assertTrue(service.verificarCNPJ("11.222.333/0001-81"));
    }

    @Test
    public void cnpjValido_outroExemplo_deveRetornarTrue() {
        assertTrue(service.verificarCNPJ("45.723.174/0001-10"));
    }

    // =========================================================
    // verificarCNPJ — casos inválidos
    // =========================================================

    @Test
    public void cnpjInvalido_digitosVerificadoresErrados_deveRetornarFalse() {
        assertFalse(service.verificarCNPJ("11.222.333/0001-00"));
    }

    @Test
    public void cnpjInvalido_sequenciaDeZeros_deveRetornarFalse() {
        assertFalse(service.verificarCNPJ("00.000.000/0000-00"));
    }

    @Test
    public void cnpjInvalido_sequenciaDeUns_deveRetornarFalse() {
        assertFalse(service.verificarCNPJ("11.111.111/1111-11"));
    }

    @Test
    public void cnpjInvalido_sequenciaDeNoves_deveRetornarFalse() {
        assertFalse(service.verificarCNPJ("99.999.999/9999-99"));
    }

    @Test
    public void cnpjInvalido_menosDigitos_deveRetornarFalse() {
        assertFalse(service.verificarCNPJ("1122233300018"));
    }

    @Test
    public void cnpjInvalido_maisDigitos_deveRetornarFalse() {
        assertFalse(service.verificarCNPJ("112223330001810"));
    }

    @Test
    public void cnpjInvalido_vazio_deveRetornarFalse() {
        assertFalse(service.verificarCNPJ(""));
    }

    @Test
    public void cnpjInvalido_apenasLetras_deveRetornarFalse() {
        assertFalse(service.verificarCNPJ("abcdefghijklmn"));
    }

    // =========================================================
    // verificarCPF — robustez com formatações diferentes
    // =========================================================

    @Test
    public void cpfValido_comEspacos_naoDeveSerAceitoComoDiferenteDaVersaoSemEspaco() {
        // CPF sem mascara válido
        boolean semMascara = service.verificarCPF("52998224725");
        // Com mascara também deve ser válido (o método remove pontos e hífen)
        boolean comMascara = service.verificarCPF("529.982.247-25");
        assertEquals(semMascara, comMascara);
    }

    @Test
    public void cnpjValido_comDiferentesFormatacoes_deveRetornarMesmoResultado() {
        boolean semMascara = service.verificarCNPJ("11222333000181");
        boolean comMascara = service.verificarCNPJ("11.222.333/0001-81");
        assertEquals(semMascara, comMascara);
    }
}