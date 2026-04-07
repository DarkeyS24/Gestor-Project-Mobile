package br.com.drky.gestor_app.ui;

import br.com.drky.gestor_app.dao.ClienteDAO;
import br.com.drky.gestor_app.enums.TipoCliente;
import br.com.drky.gestor_app.model.Cliente;
import br.com.drky.gestor_app.util.Colors;
import br.com.drky.gestor_app.util.Fonts;
import br.com.drky.gestor_app.util.Images;
import totalcross.io.IOException;
import totalcross.ui.Button;
import totalcross.ui.ComboBox;
import totalcross.ui.Container;
import totalcross.ui.Edit;
import totalcross.ui.ImageControl;
import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;

public class Editar extends Container {

	private Integer id;
	private Cliente cliente = null;
	
	public Editar(Integer id) {
		this.id = id;
	}

	private ClienteDAO dao = new ClienteDAO();
	
	private Container back;
	private Label lbNome = new Label("Nome Completo", CENTER);
	private Edit nome = new Edit();
	private Label lbTipo = new Label("Tipo de Cliente", CENTER);
	ComboBox tipoCb = new ComboBox();

	private Label lbCpf = new Label("CPF", CENTER);
	private Edit cpf = new Edit();
	private Label lbCnpj = new Label("CNPJ", CENTER);
	private Edit cnpj = new Edit();
	
	private Label lbTelefone = new Label("Telefone", CENTER);
	private Edit telefone = new Edit();
	private Label lbEmail = new Label("E-mail", CENTER);
	private Edit email = new Edit();

	@Override
	public void initUI() {
		this.setBackColor(Colors.BACKGROUND);
		Images.loadImages();
		back = new Container();
		back.setBackColor(Colors.BACKGROUND);
		add(back, LEFT, TOP, FILL, FILL);

		ImageControl logo = new ImageControl(Images.logo_azul);
		logo.scaleToFit = true;
		logo.centerImage = true;
		logo.transparentBackground = true;
		back.add(logo, LEFT + 25, TOP - 110, PARENTSIZE - 6, PARENTSIZE - 6);

		try {
			Image returnImg = new Image("images/return.png");
			Button voltarButton = new Button("", returnImg.scaledBy(0.5, 0.5), CENTER, Button.BORDER_OUTLINED);
			voltarButton.setFont(Fonts.latoBoldPlus6);
			voltarButton.setBackColor(Colors.BACKGROUND);
			back.add(voltarButton, RIGHT - 25, TOP + 25);

			voltarButton.addPressListener(new PressListener() {

				@Override
				public void controlPressed(ControlEvent e) {
					MainWindow.getMainWindow().swap(new Inicial());
				}
			});

		} catch (IOException | ImageException e) {
			e.printStackTrace();
		}

		Container cont = new Container();
		cont.setBackColor(Colors.BLUE);
		back.add(cont, LEFT + 25, TOP + 180, FILL - 25, FILL - 180);

		Label lbl = new Label("Edição de Clientes");
		lbl.setFont(Fonts.latoBoldPlus6);
		lbl.transparentBackground = true;
		lbl.setForeColor(Colors.WHITE);
		cont.add(lbl, CENTER, TOP + 20);
		
		this.cliente = dao.buscaClientePorId(this.id);

		try {

			lbNome.setFont(Fonts.latoBoldPlus4);
			lbNome.setForeColor(Colors.WHITE);

			nome.setFont(Fonts.latoBoldPlus4);
			nome.setEnabled(false);
			nome.transparentBackground = true;
			nome.captionColor = Color.WHITE;
			nome.setForeColor(Color.WHITE);

			 cont.add(lbNome, LEFT + 75, AFTER + 50);
			    cont.add(nome, LEFT + 75, AFTER, PARENTSIZE + 80, PARENTSIZE + 6);
		} catch (Exception e) {
			MessageBox.showException(e, true);
		}

		try {

			String[] items = { "Fisico", "Juridico" };
			lbTipo.setFont(Fonts.latoBoldPlus4);
			lbTipo.setForeColor(Colors.WHITE);

			ComboBox.usePopupMenu = false;
			tipoCb = new ComboBox(items);
			tipoCb.setEnabled(false);
			tipoCb.setFont(Fonts.latoBoldPlus4);
			tipoCb.setBackForeColors(Colors.BLUE, Colors.WHITE);
			tipoCb.setBorderStyle(BORDER_ROUNDED);

			tipoCb.addPressListener(new PressListener() {
				
				@Override
				public void controlPressed(ControlEvent e) {
					int index = tipoCb.getSelectedIndex();
			        boolean isFisica = (index == 0);

			        lbCpf.setVisible(isFisica);
			        cpf.setVisible(isFisica);
			        
			        lbCnpj.setVisible(!isFisica);
			        cnpj.setVisible(!isFisica);
					repaintNow(); 
				}
			});
			
			cont.add(lbTipo, LEFT + 75, AFTER + 20);
		    cont.add(tipoCb, LEFT + 75, AFTER, PARENTSIZE + 80, PARENTSIZE + 6);
		} catch (Exception e) {
			MessageBox.showException(e, true);
		}

		try {

			lbCpf.setVisible(true);
			lbCpf.setFont(Fonts.latoBoldPlus4);
			lbCpf.setForeColor(Colors.WHITE);

			cpf = new Edit("999.999.999-99");
			cpf.setEnabled(false);
			cpf.setVisible(true);
			cpf.setFont(Fonts.latoBoldPlus4);
			cpf.transparentBackground = true;
			cpf.setKeyboard(Edit.KBD_NUMERIC);
			cpf.setValidChars("0123456789");
			cpf.setMode(Edit.NORMAL, true);
			cpf.captionColor = Color.WHITE;
			cpf.setForeColor(Color.WHITE);
			
			cont.add(lbCpf, LEFT + 75, AFTER + 20);
		    cont.add(cpf, LEFT + 75, AFTER, PARENTSIZE + 80, PARENTSIZE + 6);
		} catch (Exception e) {
			MessageBox.showException(e, true);
		}
		
		try {

			lbCnpj.setVisible(false);
			lbCnpj.setFont(Fonts.latoBoldPlus4);
			lbCnpj.setForeColor(Colors.WHITE);

			cnpj = new Edit("99.999.999/9999-99");
			cnpj.setEnabled(false);
			cnpj.setVisible(false);
			cnpj.setFont(Fonts.latoBoldPlus4);
			cnpj.transparentBackground = true;
			cnpj.setKeyboard(Edit.KBD_NUMERIC);
			cnpj.setValidChars("0123456789");
			cnpj.setMode(Edit.NORMAL, true);
			cnpj.captionColor = Color.WHITE;
			cnpj.setForeColor(Color.WHITE);

			cont.add(lbCnpj, LEFT + 75, AFTER + 20);
		    cont.add(cnpj, SAME, SAME, SAME, SAME);
		} catch (Exception e) {
			MessageBox.showException(e, true);
		}

		try {

			lbTelefone.setFont(Fonts.latoBoldPlus4);
			lbTelefone.setForeColor(Colors.WHITE);

			telefone = new Edit("(99) 99999-9999");
			telefone.setKeyboard(Edit.KBD_NUMERIC);
			telefone.setValidChars("0123456789");
			telefone.setMode(Edit.NORMAL, true);
			telefone.setFont(Fonts.latoBoldPlus4);
			telefone.transparentBackground = true;
			telefone.captionColor = Color.WHITE;
			telefone.setForeColor(Color.WHITE);

			 cont.add(lbTelefone, LEFT + 75, AFTER + 20);
			    cont.add(telefone, LEFT + 75, AFTER, PARENTSIZE + 80, PARENTSIZE + 6);
		} catch (Exception e) {
			MessageBox.showException(e, true);
		}

		try {

			lbEmail.setFont(Fonts.latoBoldPlus4);
			lbEmail.setForeColor(Colors.WHITE);

			email.setFont(Fonts.latoBoldPlus4);
			email.transparentBackground = true;
			email.captionColor = Color.WHITE;
			email.setForeColor(Color.WHITE);

			cont.add(lbEmail, LEFT + 75, AFTER + 20);
		    cont.add(email, LEFT + 75, AFTER, PARENTSIZE + 80, PARENTSIZE + 6);
		} catch (Exception e) {
			MessageBox.showException(e, true);
		}
		
		SetFields();
		
		Button inserirButton = new Button("Atualizar", Button.BORDER_OUTLINED);
		inserirButton.setFont(Fonts.latoBoldPlus6);
		inserirButton.setBackColor(Colors.BLUE_BUTTONS);
		inserirButton.setForeColor(Colors.WHITE); 
		back.add(inserirButton, LEFT + 25, BOTTOM - 35);
		
		inserirButton.addPressListener(new PressListener() {
			
			@Override
			public void controlPressed(ControlEvent e) {
				
				if(verificarCampos()) {
					showMessage(dao.atualizarCliente(cliente));
					MainWindow.getMainWindow().swap(new Inicial());
				}
			}
		});
	}
	
	private void SetFields() {
		nome.setText(this.cliente.getNome());
		telefone.setText(this.cliente.getTelefone());
		email.setText(this.cliente.getEmail() == null ? "" : this.cliente.getEmail());
		
		if(this.cliente.getTipo() == TipoCliente.FISICO) {
			tipoCb.setSelectedIndex(0);
			cpf.setText(this.cliente.getCpfCnpj());
		}else {
			tipoCb.setSelectedIndex(1);
			cnpj.setText(this.cliente.getCpfCnpj());
		}
	}
	
	private boolean verificarCampos() {
		String msg = ""; 
		
		if(telefone.getTextWithoutMask().isEmpty() || telefone.getTextWithoutMask().length() != 11 || tryParseTelefone())
			msg += "Telefone invalido\n";
		
		if(!msg.isEmpty()) {
			showMessage(msg);
			return false;
		}else {
			this.cliente.setTelefone(telefone.getText());
			this.cliente.setEmail(email.getText().isEmpty()?null:email.getText());
			return true;
		}
	}
	
	@SuppressWarnings("unused")
	private boolean tryParseTelefone() {
		try {
			@SuppressWarnings("unused")
			Integer number = Integer.parseInt(telefone.getTextWithoutMask());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void showMessage(String msg) {
		MessageBox mb = new MessageBox("Info", msg, new String[] { "OK" });
		mb.messageFont = Fonts.latoBoldPlus4;
		mb.titleFont = Fonts.latoBoldPlus4;
		mb.buttonsFont = Fonts.latoBoldPlus4;
        mb.setRect(CENTER, CENTER, SCREENSIZE + 75, SCREENSIZE + 30);
		mb.popup();
	}
}