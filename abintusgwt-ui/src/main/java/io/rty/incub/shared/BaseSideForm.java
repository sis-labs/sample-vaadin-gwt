package io.rty.incub.shared;

import java.util.Arrays;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.DomEventListener;

/**
 * Base definition for a sidebar form
 * 
 * @author mlefebvre
 *
 * @param <T> the type of the item the form should edit
 */
public abstract class BaseSideForm<T> extends Div {
	
	protected VerticalLayout content;
	
	protected Binder<T> binder;

	protected T currentItem;
	
    protected Button save;
    protected Button discard;
    protected Button cancel;
    protected Button delete;
    
    /**
     * Set up the view layout of the form.
     * 
     * The form is aligned on the side bar (on the right).
     */
    protected void setUpViewLayout() {
    	content = new VerticalLayout();
        content.setSizeUndefined();
        add(content);
    }
    
    /**
     * Prepare the binder to manage the binding behavior of the form.
     * 
     * @todo(mlefebvre): a common part of the setup may be shared, double check if it makes sense.
     */
    protected abstract void setUpBinder();
    
    /**
     * Create the listener invoked when the user click on the {@code save} button
     * 
     * @return the listener
     */
    protected abstract ComponentEventListener<ClickEvent<Button>> saveClickListener();
    
    /**
     * Create the listener invoked when the user click on the {@code discard} button
     * 
     * @return the listener
     */
    protected abstract ComponentEventListener<ClickEvent<Button>> discardClickListener();
    
    /**
     * Create the listener invoked when the user click on the {@code cancel} button
     * 
     * @return the listener
     */
    protected abstract ComponentEventListener<ClickEvent<Button>> cancelClickListener();
    
    /**
     * Create the listener invoked when the user click on the {@code delete} button
     * 
     * @return the listener
     */
    protected abstract ComponentEventListener<ClickEvent<Button>> deleteClickListener();
    
    /**
     * Add an event listener on the {@code keyDown} event to
     * handle the specific case of the {@code escape}
     * 
     * @param eventListener the listener to respond to the escape key pressed event
     */
    protected void onEscape(final DomEventListener eventListener) {
        getElement()
                .addEventListener("keydown", eventListener)
                .setFilter("event.key == 'Escape'");
    }

    /**
     * Create and initialize the save button.
     * This basically set up the button to fit with the layout
     * and then prepare the behavior with predefined listener.
     * 
     * If you override this method, be sure to call super before
     * doing anything in order to have a valid reference to the
     * button. If you have to override the onClickListener (which
     * sounds wired), you have to remove the default listener
     * explicitly.
     */
    private void createSaveButton() {
    	save = new Button("Save");
        save.setWidth("100%");
        save.getElement().getThemeList().add("primary");
        save.addClickListener(saveClickListener());
    }

    /**
     * Create and initialize the discard button.
     * This basically set up the button to fit with the layout
     * and then prepare the behavior with predefined listener.
     * 
     * If you override this method, be sure to call super before
     * doing anything in order to have a valid reference to the
     * button. If you have to override the onClickListener (which
     * sounds wired), you have to remove the default listener
     * explicitly.
     */
    private void createDiscardButton() {
    	discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(discardClickListener());
    }

    /**
     * Create and initialize the cancel button.
     * This basically set up the button to fit with the layout
     * and then prepare the behavior with predefined listener.
     * 
     * If you override this method, be sure to call super before
     * doing anything in order to have a valid reference to the
     * button. If you have to override the onClickListener (which
     * sounds wired), you have to remove the default listener
     * explicitly.
     */
    private void createCancelButton() {
    	cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(cancelClickListener());
    }
    
    /**
     * Create and initialize the delete button.
     * This basically set up the button to fit with the layout
     * and then prepare the behavior with predefined listener.
     * 
     * If you override this method, be sure to call super before
     * doing anything in order to have a valid reference to the
     * button. If you have to override the onClickListener (which
     * sounds wired), you have to remove the default listener
     * explicitly.
     */
    protected void createDeleteButton() {
    	delete = new Button("Delete");
        delete.setWidth("100%");
        delete.getElement().getThemeList()
                .addAll(Arrays.asList("error", "primary"));
        delete.addClickListener(deleteClickListener());
    }
    
    /**
     * Create the set of buttons to manage the behavior of the
     * form.
     * This create and initialize all available buttons with behavior
     * defined in the respective listener and then add those in the
     * layout.
     */
    protected void createButtons() {
    	createSaveButton();
    	createDiscardButton();
    	createCancelButton();
        createDeleteButton();
        
        content.add(save, discard, delete, cancel);
    }
    
    /**
     * Adjust the form to be able to edit the {@code item} from within this
     * form.
     * 
     * @param item the item to edit
     */
    protected void editItem(final T item) {
    	currentItem = item;
        binder.readBean(item);
    }
}
