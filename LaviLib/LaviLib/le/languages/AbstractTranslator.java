package le.languages;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import le.install.AbstractInstall;

/**
 * The AbstractTranslator class is used to translate String to different languages.
 * */
public abstract class AbstractTranslator {
	private static AbstractTranslator instance;
	protected AbstractInstall install = null;
	public AbstractTranslator(AbstractInstall install) {
		instance = this;
		this.install = install;
	}
	private ActionListener onLanguageChangesListanet = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	};
	private Language language;
	
	public final String DEFAULT_LANGUAGE = "English";
	public String get(String s) {
		if (language == null) {
			return s;
		}
		return language.translate(s);
	}
	public static AbstractTranslator getTranslator() {
		return instance;
	}
	public void setLanguage(String name) {
		if (name == null || name.equals(DEFAULT_LANGUAGE)) {
			setLanguage((Language)null);
		}else {
			setLanguage(new Language(name, install.getFile("Languages\\" + name + ".lng")));
		}
	}
	public void setLanguage(Language newLanguage) {
		this.language = newLanguage;
	}
	public Object getLanguageName() {
		if (language == null) {
			return DEFAULT_LANGUAGE;
		}
		return language.name;
	}
	public String getBeforeTextBorder() {
		if (language != null && language.direction.equals("rtl")) {
			return BorderLayout.EAST;
		}
		return BorderLayout.WEST;
	}
	public String getAfterTextBorder() {
		if (language != null && language.direction.equals("rtl")) {
			return BorderLayout.WEST;
		}
		return BorderLayout.EAST;
	}
	public ComponentOrientation getComponentOrientation() {
		if (language != null && language.direction.equals("rtl")) {
			return ComponentOrientation.RIGHT_TO_LEFT;
		}
		return ComponentOrientation.LEFT_TO_RIGHT;
	}
	public AbstractInstall getInstall() {
		return install;
	}
	public void setInstall(AbstractInstall install) {
		this.install = install;
	}
	public ActionListener getOnLanguageChangesListanet() {
		return onLanguageChangesListanet;
	}
	public void setOnLanguageChangesListanet(ActionListener onLanguageChangesListanet) {
		this.onLanguageChangesListanet = onLanguageChangesListanet;
	}
}