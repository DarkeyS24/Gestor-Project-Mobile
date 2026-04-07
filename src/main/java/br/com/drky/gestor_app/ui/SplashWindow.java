package br.com.drky.gestor_app.ui;

import br.com.drky.gestor_app.util.Images;
import totalcross.io.IOException;
import totalcross.ui.ImageControl;
import totalcross.ui.Window;
import totalcross.ui.anim.ControlAnimation;
import totalcross.ui.anim.FadeAnimation;
import totalcross.ui.image.ImageException;

public class SplashWindow extends Window {

	public SplashWindow() throws IOException, ImageException {

	}

	protected void onPopup() {
		Images.loadImages();
		ImageControl logo, back;

		back = new ImageControl(Images.background);
		back.scaleToFit = true;
		back.centerImage = true;
		back.hwScale = true;
		back.strechImage = true;
		add(back, LEFT, TOP, FILL, FILL);

		logo = new ImageControl(Images.logo_azul);
		logo.scaleToFit = true;
		logo.centerImage = true;
		logo.transparentBackground = true;
		add(logo, CENTER, CENTER, PARENTSIZE + 75, PARENTSIZE + 75);

		FadeAnimation.create(logo, true, null, 3000)
				.then(FadeAnimation.create(logo, false, this::onAnimationFinished, 3000)).start();
	}

	public void onAnimationFinished(ControlAnimation anim) {
		this.unpop();
	}

}
