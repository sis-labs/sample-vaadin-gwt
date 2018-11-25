package io.rty.incub.settings;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Version;

import io.rty.incub.MainLayout;
import io.rty.incub.authentication.AccessControl;
import io.rty.incub.authentication.AccessControlFactory;

@Route(value = "settings", layout = MainLayout.class)
@PageTitle("Settings")
public class SettingsView extends HorizontalLayout  {

	public static final String VIEW_NAME = "settings";
	
	
	private HorizontalLayout layout;
    private AccessControl accessControl;
	
	public SettingsView() {
		accessControl = AccessControlFactory.getInstance().createAccessControl();
		buildUI();
	}
	
	private void buildUI() {
		setSizeFull();
		layout = new HorizontalLayout();
		setClassName("settings-screen");
	}
}
