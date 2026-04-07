package br.com.drky.gestor_app.util;

import totalcross.ui.font.Font;

public class Fonts {

	public static final int FONT_DEFAULT_SIZE = 12;

	public static Font latoBoldDefaultSize;
	public static Font latoBoldMinus1;
	public static Font latoBoldMinus2;
	public static Font latoBoldMinus4;
	public static Font latoBoldPlus1;
	public static Font latoBoldPlus2;
	public static Font latoBoldPlus4;
	public static Font latoBoldPlus6;
	public static Font latoBoldPlus8;
	public static Font messageFont;

	public static Font latoRegularMinus5;
	public static Font latoRegularDefaultSize;

	static {

		// Lato Regular
		latoRegularDefaultSize = Font.getFont("fonts/Lato Regular", false, FONT_DEFAULT_SIZE);
		latoRegularMinus5 = latoRegularDefaultSize.adjustedBy(-5);

		// Lato Bold
		latoBoldDefaultSize = Font.getFont("fonts/Lato Bold", false, FONT_DEFAULT_SIZE);
		latoBoldPlus1 = latoBoldDefaultSize.adjustedBy(1);
		latoBoldPlus2 = latoBoldDefaultSize.adjustedBy(36);
		latoBoldPlus4 = latoBoldDefaultSize.adjustedBy(50);
		latoBoldPlus6 = latoBoldDefaultSize.adjustedBy(75);
		latoBoldPlus8 = latoBoldDefaultSize.adjustedBy(132);
		latoBoldMinus1 = latoBoldDefaultSize.adjustedBy(-1);
		latoBoldMinus2 = latoBoldDefaultSize.adjustedBy(-2);
		latoBoldMinus4 = latoBoldDefaultSize.adjustedBy(-4);
		messageFont = latoBoldDefaultSize.adjustedBy(500);
		
	}
}
