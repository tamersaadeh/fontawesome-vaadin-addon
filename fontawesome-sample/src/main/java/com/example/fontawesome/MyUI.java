package com.example.fontawesome;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import it.saadeh.fontawesome.FontAwesome;

//@StyleSheet("../../VAADIN/addons/fontawesome/css/fa-svg-with-js.css")
@JavaScript("../../VAADIN/addons/fontawesome/js/fontawesome-all.js")
@Theme("valo")
public class MyUI extends UI {

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		com.vaadin.ui.JavaScript js = new com.vaadin.ui.JavaScript();
		js.execute("FontAwesomeConfig = { autoAddCss: false }");

		final VerticalLayout layout = new VerticalLayout();

		final TextField name = new TextField();
		name.setCaption("Type your name here:");
		layout.addComponent(name);

		ClickListener e = x -> {
			layout.addComponent(new Label("Thanks " + name.getValue()
					+ ", it works!"));
		};

		Button faButton = new Button("FA File");
		faButton.addClickListener(e);

		Label fa = new FontAwesomeLabel(FontAwesome.FILE_S);
		faButton.setIcon(FontAwesome.FILE_S);
		layout.addComponents(faButton, fa);

		setContent(layout);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}

