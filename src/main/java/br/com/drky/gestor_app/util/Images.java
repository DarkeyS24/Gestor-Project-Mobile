package br.com.drky.gestor_app.util;

import totalcross.io.IOException;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;

public class Images {
	private Images() {
	}

	public static Image logo_azul, logo_branco, background, searchIcon, addIcon, returnIcon;

	public static void loadImages() {
		try {
			logo_branco = new Image("images/logo_branco.png");
			background = new Image("images/background.png");
			logo_azul = new Image("images/logo_azul.png");
			searchIcon = new Image("images/search.png");
			addIcon = new Image("images/add.png");
			returnIcon = new Image("images/return.png");
		} catch (ImageException | IOException e) {
			e.printStackTrace();
		}
	}
}
