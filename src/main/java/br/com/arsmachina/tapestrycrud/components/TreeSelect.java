// Copyright 2008 Sílex Sistemas Ltda. e ONP Informática Ltda.
package br.com.arsmachina.tapestrycrud.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.mixins.RenderDisabled;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.FieldValidatorDefaultSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.util.EnumSelectModel;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.services.LabelEncoderSource;
import br.com.arsmachina.tapestrycrud.services.TreeServiceSource;
import br.com.arsmachina.tapestrycrud.tree.SimpleTreeSelectNode;
import br.com.arsmachina.tapestrycrud.tree.SingleTypeTreeService;
import br.com.arsmachina.tapestrycrud.tree.TreeSelectNode;

/**
 * Most of this code was copied from the {@link Select} component.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@IncludeJavaScriptLibrary("classpath:/br/com/arsmachina/tapestrycrud/javascript/treeselect.js")
@IncludeStylesheet(Constants.TAPESTRY_CRUD_CSS_ASSET)
public class TreeSelect extends AbstractField {

	private static final ArrayList<TreeSelectNode> EMPTY_LIST =
		new ArrayList<TreeSelectNode>(0);

	/**
	 * Allows a specific implementation of {@link ValueEncoder} to be supplied.
	 * This is used to create client-side string values for the different
	 * options.
	 * 
	 * @see ValueEncoderSource
	 */
	@SuppressWarnings("unchecked")
	@Parameter
	private ValueEncoder encoder;

	@Parameter(defaultPrefix = BindingConstants.ASSET, value = Constants.NO_CHILDREN_NODE_IMAGE)
	private Asset noChildrenIcon;

	@Parameter(defaultPrefix = BindingConstants.ASSET, value = Constants.HAS_CHILDREN_NODE_IMAGE)
	private Asset hasChildrenIcon;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	/**
	 * Defines whether the tree will be shown up front or just shown when needed.
	 */
	@Parameter(value = "false")
	private boolean showTreeAtFirst;

//	/**
//	 * Defines whether descendent options will be disabled or not. This is useful when
//	 * dealing with parent/child self relationships.
//	 */
//	@Parameter(value = "false")
//	private boolean disableDescendentOptions;

	/**
	 * The list of root nodes to be used as options.
	 */
	@Parameter(required = true, allowNull = false)
	private List<TreeSelectNode> rootNodes;

	@Inject
	private Request request;

	@Inject
	private ComponentResources resources;

	@Environmental
	private ValidationTracker tracker;

	@Inject
	private TreeServiceSource treeServiceSource;

	/**
	 * Performs input validation on the value supplied by the user in the form
	 * submission.
	 */
	@Parameter(defaultPrefix = BindingConstants.VALIDATE)
	private FieldValidator<Object> validate;

	/**
	 * The value to read or update.
	 */
	@Parameter(required = true, principal = true, autoconnect = true)
	private Object value;

	/**
	 * "No parent object" option label.
	 */
	@Parameter(value = "message:treeselect.noparent.option", defaultPrefix = BindingConstants.MESSAGE)
	private String noParentOptionLabel;

	/**
	 * Message used in the link that shows the tree select.
	 */
	@Parameter(value = "message:treeselect.show", defaultPrefix = BindingConstants.MESSAGE)
	private String showTreeMessage;

	/**
	 * Message used in the link that hides the tree select.
	 */
	@Parameter(value = "message:treeselect.hide", defaultPrefix = BindingConstants.MESSAGE)
	private String hideTreeMessage;

	@Inject
	private FieldValidationSupport fieldValidationSupport;

	@SuppressWarnings("unused")
	@Mixin
	private RenderDisabled renderDisabled;

	@Inject
	private RenderSupport renderSupport;

	@Inject
	private LabelEncoderSource labelEncoderSource;

	private String selectedClientValue;

	private String clientId;

	@SuppressWarnings("unused")
	private boolean isSelected(String clientValue) {
		return TapestryInternalUtils.isEqual(clientValue, selectedClientValue);
	}

	@Override
	protected void processSubmission(String elementName) {

		String submittedValue = request.getParameter(elementName);

		// When the null option is selected, the "on" value is submitted.
		if (submittedValue != null && submittedValue.equals("on")) {
			submittedValue = null;
		}

		tracker.recordInput(this, submittedValue);

		Object selectedValue =
			InternalUtils.isBlank(submittedValue) ? null
					: encoder.toValue(submittedValue);

		try {
			fieldValidationSupport.validate(selectedValue, resources, validate);
			value = selectedValue;
		}
		catch (ValidationException ex) {
			tracker.recordError(this, ex.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	void beginRender(MarkupWriter writer) {

		writer.element("div", "class", "t-crud-tree-select");

		clientId = renderSupport.allocateClientId(resources);
		final String hideId = clientId + "-hide";
		final String textId = clientId + "-text";
		final String showId = clientId + "-show";

		if (showTreeAtFirst == false) {

			writer.element("p", "id", textId);

			if (value == null) {
				writer.write(noParentOptionLabel);
			} else {

				final Class boundType = resources.getBoundType("value");

				final SingleTypeTreeService<Object> treeService =
					treeServiceSource.get(boundType);

				List<Object> stack = new ArrayList<Object>();
				Object current = value;

				while (current != null) {
					stack.add(current);
					current = treeService.getParent(current);
				}

				Collections.reverse(stack);

				final LabelEncoder labelEncoder =
					labelEncoderSource.get(boundType);

				final int size = stack.size();
				for (int i = 0; i < size; i++) {

					Object object = stack.get(i);
					writer.write(labelEncoder.toLabel(object));

					if (i < size - 1) {
						writer.write("/");
					}

				}

			}

			writer.write(" ");

			writer.element("a", "id", showId, "href", "#");
			writer.write(showTreeMessage);
			writer.end();

			writer.end(); // p

			renderSupport.addScript(
					"Event.observe('%s', 'click', function() { "
							+ "$('%s').hide(); $('%s').hide(); $('%s').show(); $('%s').show(); });",
					showId, showId, textId, clientId, hideId);

		}

		writer.element("ul", "class", "t-crud-tree-select", "id", clientId);

		if (showTreeAtFirst == false) {
			writer.attributes("style", "display: none;");
		}

		renderNoParentOption(writer);

		for (TreeSelectNode node : rootNodes) {
			render(node, writer, 1);
		}

		writer.end(); // outer ul tag

		if (showTreeAtFirst == false) {

			writer.element("p", "id", hideId, "style", "display: none");
			writer.element("a", "href", "#");
			writer.write(hideTreeMessage);
			writer.end(); // a
			writer.end(); // p

			renderSupport.addScript(
					"Event.observe('%s', 'click', function() { $('%s').hide(); $('%s').hide(); $('%s').show(); $('%s').show(); });",
					hideId, hideId, clientId, textId, showId);

		}
		
		writer.end(); // div

	}

	private void renderNoParentOption(MarkupWriter writer) {

		Map<String, String> attributes = new HashMap<String, String>(1);
		attributes.put("class", "noParentOption");

		SimpleTreeSelectNode node =
			new SimpleTreeSelectNode(null, EMPTY_LIST, noParentOptionLabel,
					false, attributes);

		render(node, writer, 1);

	}

	@SuppressWarnings("unchecked")
	private void render(TreeSelectNode node, MarkupWriter writer, int level) {

		String selectedClientValue = encoder.toClient(value);

		// Use the value passed up in the form submission, if available.
		// Failing that, see if there is a current value (via the value
		// parameter), and
		// convert that to a client value for later comparison.

		String thisClientValue = encoder.toClient(node.getValue());
		boolean checked = isEqual(selectedClientValue, thisClientValue);

		String clientId = getClientId();
		String radioId = clientId + "-" + thisClientValue;

		writer.element("li", "class", "level" + level);

		writeAttributes(node.getAttributes(), writer);

		if (checked) {
			
			writer.getElement().addClassName("checked");
			
//			if (disableDescendentOptions) {
//				renderSupport.addScript("TreeSelect.disableDescendentInputs('%s')", radioId);
//			}

		}

		renderLabel(node, writer, radioId);
		renderRadioButton(writer, thisClientValue, checked, radioId);

		writer.end(); // input

		List<TreeSelectNode> children = node.getChildren();

		if (children.isEmpty() == false) {

			writer.element("ul");

			for (TreeSelectNode child : children) {
				render(child, writer, level + 1);
			}

			writer.end(); // ul

		}

		writer.end(); // li

	}

	/**
	 * @param selectedClientValue
	 * @param thisClientValue
	 * @return
	 */
	private boolean isEqual(String selectedClientValue, String thisClientValue) {

		if (selectedClientValue == thisClientValue) {
			return true;
		} else if (selectedClientValue == null && thisClientValue != null
				|| selectedClientValue != null && thisClientValue == null) {

			return false;

		}

		return thisClientValue.equals(selectedClientValue);

	}

	/**
	 * @param writer
	 * @param thisClientValue
	 * @param checked
	 * @param radioId
	 */
	private void renderRadioButton(MarkupWriter writer, String thisClientValue,
			boolean checked, String radioId) {

		writer.element("input", "type", "radio", "name", getControlName(),
				"id", radioId, "value", thisClientValue);

		if (checked) {
			writer.attributes("checked", "checked");
		}

		renderSupport.addScript(
				"Event.observe('%s', 'click', function() { TreeSelect.handleChange('%s', '%s'); } );",  
				radioId, radioId, clientId);

	}

	/**
	 * @param node
	 * @param writer
	 * @param radioId
	 */
	private void renderLabel(TreeSelectNode node, MarkupWriter writer,
			String radioId) {

		final boolean noChildren = node.getChildren().isEmpty();

		writer.element("label", "for", radioId);

		Asset asset = noChildren ? noChildrenIcon : hasChildrenIcon;
		writer.element("img", "src", asset);
		writer.end();

		writer.write(node.getLabel());

		writer.end(); // label

	}

	@SuppressWarnings("unchecked")
	ValueEncoder defaultEncoder() {
		return defaultProvider.defaultValueEncoder("value", resources);
	}

	@SuppressWarnings("unchecked")
	SelectModel defaultModel() {
		Class valueType = resources.getBoundType("value");

		if (valueType == null)
			return null;

		if (Enum.class.isAssignableFrom(valueType))
			return new EnumSelectModel(valueType,
					resources.getContainerMessages());

		return null;
	}

	/**
	 * Computes a default value for the "validate" parameter using
	 * {@link FieldValidatorDefaultSource}.
	 */
	Binding defaultValidate() {
		return defaultProvider.defaultValidatorBinding("value", resources);
	}

	@Override
	public boolean isRequired() {
		return validate.isRequired();
	}

	private void writeAttributes(Map<String, String> attributes,
			MarkupWriter writer) {
		if (attributes == null)
			return;

		for (Map.Entry<String, String> e : attributes.entrySet()) {
			writer.attributes(e.getKey(), e.getValue());
		}

	}

}
