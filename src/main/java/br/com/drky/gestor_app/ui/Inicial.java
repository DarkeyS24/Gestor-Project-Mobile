package br.com.drky.gestor_app.ui;

import java.util.ArrayList;
import java.util.List;

import br.com.drky.gestor_app.dao.ClienteDAO;
import br.com.drky.gestor_app.model.Cliente;
import br.com.drky.gestor_app.service.ClienteService;
import br.com.drky.gestor_app.util.Colors;
import br.com.drky.gestor_app.util.Fonts;
import br.com.drky.gestor_app.util.Images;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Edit;
import totalcross.ui.Grid;
import totalcross.ui.ImageControl;
import totalcross.ui.MainWindow;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.dialog.ProgressBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.UnitsConverter;

public class Inicial extends Container {

	public Inicial() {
	}

	private Container back;
	private Edit search = new Edit();
	private List<Cliente> clientes = new ArrayList<>();
	private Grid grid;
	private int GAP = UnitsConverter.toPixels(DP + 8);
	private ClienteDAO dao = new ClienteDAO();
	private ClienteService service = new ClienteService();

	@Override
	public void initUI() {
		String[] gridCaptions = { "ID", "Nome", "Tipo", "Documento", "Sincronizado" };
		int[] gridWidths = { -15, -50, -30, -50, -15 };
		int[] gridAligns = { CENTER, CENTER, CENTER, CENTER, CENTER };

		grid = new Grid(gridCaptions, gridWidths, gridAligns, false);
		grid.verticalLineStyle = Grid.VERT_LINE;
		Grid.useHorizontalScrollBar = true;

		grid.canClickSelectAll = false;

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

		Button syncButton = new Button("Sincronizar", Button.BORDER_OUTLINED);
		syncButton.setFont(Fonts.latoBoldPlus6);
		syncButton.setBackColor(Colors.BLUE_BUTTONS);
		syncButton.setForeColor(Colors.WHITE);
		back.add(syncButton, RIGHT - 25, TOP + 35);

		syncButton.addPressListener(new PressListener() {

			@Override
			public void controlPressed(ControlEvent e) {

				ProgressBox pb = new ProgressBox("Aguarde", "Sincronizando...", null);
				pb.contentFont = Fonts.latoBoldPlus4;
				pb.messageFont = Fonts.latoBoldPlus4;
				pb.titleFont = Fonts.latoBoldPlus4;
				pb.popupNonBlocking();
				
				new Thread() {
					
					@Override
					public void run() {
							
						Integer status = service.sincronizarClientes();
						pb.unpop();
						
						MainWindow.getMainWindow().runOnMainThread(new Runnable() {
							
							@Override
							public void run() {
								
								MessageBox mb = new MessageBox("Info", status == 0 ? "Clientes sincronizados com sucesso!!"
										: status == 1 ? "Todos os registros estão sincronizados" : "Servidor indisponivel no momento");
								mb.messageFont = Fonts.latoBoldPlus4;
								mb.titleFont = Fonts.latoBoldPlus4;
								mb.buttonsFont = Fonts.latoBoldPlus4;
								mb.setRect(CENTER, CENTER, SCREENSIZE + 75, SCREENSIZE + 30);
								mb.popup();
								if (status == 0)
									refreshGrid();

							}
						});
					}
				}.start();
			}
		});

		Container cont = new Container();
		cont.setBackColor(Colors.BLUE);
		back.add(cont, LEFT + 25, TOP + 180, FILL - 25, FILL - 180);

		Button vizualicacaoButton = new Button("Vizualizar", Button.BORDER_OUTLINED);
		vizualicacaoButton.setFont(Fonts.latoBoldPlus6);
		vizualicacaoButton.setBackColor(Colors.BLUE_BUTTONS);
		vizualicacaoButton.setForeColor(Colors.WHITE);
		back.add(vizualicacaoButton, LEFT + 25, BOTTOM - 35);

		vizualicacaoButton.addPressListener(new PressListener() {

			@Override
			public void controlPressed(ControlEvent e) {
				int idx = grid.getSelectedIndex();
				if (idx < 0 || idx >= clientes.size()) {
					showMessage("Selecione um cliente.");
					return;
				}
				Cliente c = clientes.get(idx);
				MainWindow.getMainWindow().swap(new Editar(c.getCodigo()));
			}
		});

		Button exclusaoButton = new Button("Excluir", Button.BORDER_OUTLINED);
		exclusaoButton.setFont(Fonts.latoBoldPlus6);
		exclusaoButton.setBackColor(Colors.RED);
		exclusaoButton.setForeColor(Colors.WHITE);
		back.add(exclusaoButton, RIGHT - 25, BOTTOM - 35);

		exclusaoButton.addPressListener(new PressListener() {

			@Override
			public void controlPressed(ControlEvent e) {
				int idx = grid.getSelectedIndex();
				if (idx < 0 || idx >= clientes.size()) {
					showMessage("Selecione um cliente para excluir.");
					return;
				}
				Cliente c = clientes.get(idx);
				MessageBox mb = new MessageBox("Confirmar", "Excluir " + c.getNome() + "?",
						new String[] { "Sim", "Nao" });
				mb.messageFont = Fonts.latoBoldPlus4;
				mb.titleFont = Fonts.latoBoldPlus4;
				mb.buttonsFont = Fonts.latoBoldPlus4;
				mb.setRect(CENTER, CENTER, SCREENSIZE + 75, SCREENSIZE + 30);
				mb.popup();
				if (mb.getPressedButtonIndex() == 0) {
					dao.excluirClienteLogico(c.getCodigo());
					refreshGrid();
				}
			}
		});

		try {
			search.caption = "Pesquisa por ID";
			search.setFont(Fonts.latoBoldPlus4);
			search.transparentBackground = true;
			search.setKeyboard(Edit.KBD_NUMERIC);
			search.setValidChars("0123456789");
			search.captionColor = Color.WHITE;
			search.setForeColor(Color.WHITE);

			cont.add(search, LEFT + 25, TOP + 25, PARENTSIZE + 50, PARENTSIZE + 6);
		} catch (Exception e) {
			MessageBox.showException(e, true);
		}

		try {

			Image searchImg = new Image("images/search_branco.png");
			Button searchButton = new Button("", searchImg.scaledBy(0.28, 0.28), CENTER, Button.BORDER_OUTLINED);
			searchButton.setFont(Fonts.latoBoldPlus2);
			searchButton.setBackColor(Colors.BLUE);
			cont.add(searchButton, RIGHT - 325, TOP + 25);

			searchButton.addPressListener(new PressListener() {

				@Override
				public void controlPressed(ControlEvent e) {
					if (!search.getText().isEmpty()) {
						Cliente cliente = dao.buscaClientePorId(Integer.parseInt(search.getText()));
						if (cliente == null) {
							showMessage("Cliente não encontrado");
							refreshGrid();
						} else {
							refreshGridById(cliente.getCodigo());
						}
					} else {
						showMessage("Digite o codigo do cliente");
						refreshGrid();
					}
					search.setText("");
				}
			});

			Image addImg = new Image("images/add_branco.png");
			Button addButton = new Button("", addImg.scaledBy(0.2, 0.2), CENTER, Button.BORDER_OUTLINED);
			addButton.setFont(Fonts.latoBoldPlus2);
			addButton.setBackColor(Colors.BLUE);
			cont.add(addButton, RIGHT - 50, TOP + 25);

			addButton.addPressListener(new PressListener() {

				@Override
				public void controlPressed(ControlEvent e) {
					MainWindow.getMainWindow().swap(new Cadastro());
				}
			});

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		Container list = new Container();
		list.setBackColor(Colors.WHITE);
		cont.add(list, LEFT + 25, TOP + 175, FILL - 25, FILL - 25);

		grid.setBackColor(Colors.BLACK);
		grid.setFont(Fonts.latoBoldPlus4);
		list.add(grid, LEFT + GAP, TOP + GAP, FILL - GAP, FILL - GAP);

		refreshGrid();
	}

	private void refreshGrid() {
		clientes = dao.buscaTodosOsClientes();

		if (clientes.isEmpty()) {
			grid.setItems(new String[0][0]);
			repaintNow();
			return;
		}
		String[][] items = new String[clientes.size()][5];
		for (int i = 0; i < clientes.size(); i++) {
			Cliente c = clientes.get(i);
			items[i] = new String[] { c.getCodigo().toString(), c.getNome(), c.getTipo().toString(), c.getCpfCnpj(),
					(c.getSincronizado()) ? "Sim" : "Não" };
		}
		grid.setItems(items);
		repaintNow();
	}

	private void refreshGridById(Integer Id) {
		Cliente c = dao.buscaClientePorId(Id);

		String[][] items = new String[clientes.size()][5];
		items[0] = new String[] { c.getCodigo().toString(), c.getNome(), c.getTipo().toString(), c.getCpfCnpj(),
				(c.getSincronizado()) ? "Sim" : "Não" };
		grid.setItems(items);
		repaintNow();
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