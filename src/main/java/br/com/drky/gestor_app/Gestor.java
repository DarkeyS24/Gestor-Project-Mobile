package br.com.drky.gestor_app;

import br.com.drky.gestor_app.ui.Inicial;
import br.com.drky.gestor_app.ui.SplashWindow;
import totalcross.io.IOException;
import totalcross.sys.Settings;
import totalcross.ui.MainWindow;
import totalcross.ui.image.ImageException;

public class Gestor extends MainWindow {
	public Gestor() {
		setUIStyle(Settings.MATERIAL_UI);
	}

	static {
		Settings.applicationId = "NUBK";
		Settings.appVersion = "1.0.1";
		Settings.iosCFBundleIdentifier = "com.totalcross.sample.nubank";
	}

	public void initUI() {
		SplashWindow sp;
		Inicial inicial = new Inicial();
		try {
			sp = new SplashWindow();
			sp.popupNonBlocking();
			swap(inicial);

		} catch (IOException | ImageException e) {
			e.printStackTrace();
		}
	}
}
