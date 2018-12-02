package io.rty.incub.crud;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.vaadin.pekka.CheckboxGroup;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import io.rty.incub.backend.data.Availability;
import io.rty.incub.backend.data.Category;
import io.rty.incub.backend.data.Product;
import io.rty.incub.shared.BaseSideForm;
import io.rty.incub.shared.FunctionalUtilities;

/**
 * A form for editing a single product.
 */
public class ProductForm extends BaseSideForm<Product> {

    private TextField productName;
    private TextField price;
    private TextField stockCount;
    private ComboBox<Availability> availability;
    private CheckboxGroup<Category> category;

    private SampleCrudLogic viewLogic;

    private static class PriceConverter extends StringToBigDecimalConverter {

        public PriceConverter() {
            super(BigDecimal.ZERO, "Cannot convert value to a number.");
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            // Always display currency with two decimals
            NumberFormat format = super.getFormat(locale);
            if (format instanceof DecimalFormat) {
                format.setMaximumFractionDigits(2);
                format.setMinimumFractionDigits(2);
            }
            return format;
        }
    }

    private static class StockCountConverter extends StringToIntegerConverter {

        public StockCountConverter() {
            super(0, "Could not convert value to " + Integer.class.getName()
                    + ".");
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            // Do not use a thousands separator, as HTML5 input type
            // number expects a fixed wire/DOM number format regardless
            // of how the browser presents it to the user (which could
            // depend on the browser locale).
            final DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            format.setDecimalSeparatorAlwaysShown(false);
            format.setParseIntegerOnly(true);
            format.setGroupingUsed(false);
            return format;
        }
    }
    
    private void createProductField() {
    	productName = new TextField("Product name");
        productName.setWidth("100%");
        productName.setRequired(true);
        productName.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(productName);
    }
    
    private void createPriceAndStockFields() {
    	// create the price field
    	price = new TextField("Price");
        price.setSuffixComponent(new Span("€"));
        price.getElement().getThemeList().add("align-right");
        price.setValueChangeMode(ValueChangeMode.EAGER);
        
        // create the stock field
        stockCount = new TextField("In stock");
        stockCount.getElement().getThemeList().add("align-right");
        stockCount.setValueChangeMode(ValueChangeMode.EAGER);

        // align both on the same line
        final HorizontalLayout horizontalLayout = new HorizontalLayout(price,
                stockCount);
        horizontalLayout.setWidth("100%");
        horizontalLayout.setFlexGrow(1, price, stockCount);
        content.add(horizontalLayout);
    }
    
    private void createAvailabilityField() {
    	availability = new ComboBox<>("Availability");
        availability.setWidth("100%");
        availability.setRequired(true);
        availability.setItems(Availability.values());
        availability.setAllowCustomValue(false);
        content.add(availability);
    }
    
    private void createCategoryField() {
    	category = new CheckboxGroup<>();
        category.setId("category");
        category.getContent().getStyle().set("flex-direction", "column")
                .set("margin", "0");
        final Label categoryLabel = new Label("Categories");
        categoryLabel.setClassName("vaadin-label");
        categoryLabel.setFor(category);
        content.add(categoryLabel, category);
    }
    
    protected void setUpBinder() {
    	binder = new BeanValidationBinder<>(Product.class);
        binder.forField(price).withConverter(new PriceConverter())
                .bind("price");
        binder.forField(stockCount).withConverter(new StockCountConverter())
                .bind("stockCount");
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });
    }
    
    protected ComponentEventListener<ClickEvent<Button>> saveClickListener() {
    	return event -> {
            if (currentItem != null
                    && binder.writeBeanIfValid(currentItem)) {
                viewLogic.saveProduct(currentItem);
            }
        };     	
    }
    
    protected ComponentEventListener<ClickEvent<Button>> discardClickListener() {
    	return event -> viewLogic.editProduct(currentItem);
    }
    
    protected ComponentEventListener<ClickEvent<Button>> cancelClickListener() {
    	return event -> viewLogic.cancelProduct();
    }
    
    protected ComponentEventListener<ClickEvent<Button>> deleteClickListener() {
    	return event -> {
            if (currentItem != null) {
                viewLogic.deleteProduct(currentItem);
            }
        };
    }
    
    public ProductForm(final SampleCrudLogic sampleCrudLogic) {
    	super();
        setClassName("product-form");
        setUpViewLayout();
        viewLogic = sampleCrudLogic;
        createProductField();
        createPriceAndStockFields();
        createAvailabilityField();
        createCategoryField();
        setUpBinder();
        createButtons();
        onEscape(event -> viewLogic.cancelProduct());
    }

    public void setCategories(Collection<Category> categories) {
        category.setItems(categories);
    }

    public void editProduct(Product product) {
        final Product p = FunctionalUtilities.ensureInstance(product, Product.class);
        delete.setVisible(!p.isNewProduct());
        editItem(p);
    }
}
