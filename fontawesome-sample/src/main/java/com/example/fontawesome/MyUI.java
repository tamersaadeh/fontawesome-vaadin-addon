package com.example.fontawesome;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import it.saadeh.fontawesome.FontAwesome;
import it.saadeh.fontawesome.FontAwesomeCore.Style;
import it.saadeh.fontawesome.FontAwesomeItem;
import it.saadeh.fontawesome.FontAwesomeList;
import it.saadeh.fontawesome.FontAwesomeList.ListType;

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

		FontAwesomeList list = new FontAwesomeList(ListType.ORDERED);

		FontAwesomeItem item1 = new FontAwesomeItem(FontAwesome.ADDRESS_BOOK_R.add(Style.LARGE), "a regular address book");
		FontAwesomeItem item2 = new FontAwesomeItem(FontAwesome.ADDRESS_BOOK_S.add(Style.LARGE), "a solid address book");

		list.add(item1);
		list.add(item2);

		layout.addComponents(list);

		setContent(layout);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}

