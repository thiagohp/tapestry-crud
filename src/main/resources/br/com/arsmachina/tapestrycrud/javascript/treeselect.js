var TreeSelect = {

	disableDescendentInputs: function(id) {
	
		// id is always an <input>, so we need its parent, a <li>
		var parent = $(id).parentNode;
		
		parent.select("li").each(
				
			function(li) {
				
				// the no parent option is always enabled
				if (li.hasClassName("noParentOption") == false) {
					li.addClassName("disabled");
				}
				
			}
			
		);

		// turning off disabled option <input>s de opções
		parent.select("input").each(
			
			function(input) {
				if (!input.checked) {
					input.disabled = true;
				}
			}
			
		);
	
	},

	handleChange: function(radioId, clientId) {
		
		$(clientId).select("li.checked").each(
			function(label) {
				label.removeClassName("checked");
			}
		);
		
		var parentNode = $(radioId).parentNode;
		
		if (parentNode.addClassName == undefined) {
			Element.extend(parentNode);
		}
		
		parentNode.addClassName("checked");
		
	}

};
