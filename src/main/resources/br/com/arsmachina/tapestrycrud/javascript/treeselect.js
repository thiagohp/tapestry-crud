var TreeSelect = {

	disableDescendentInputs: function(id) {
	
		// id is always an <input>, so we need its parent, a <li>
		var parent = $(id).parentNode;
		
		parent.select("li").each(
				
			function(li) {
				
				// a opção de não ter mãe sempre é disponível
				if (li.hasClassName("noParentOption") == false) {
					li.addClassName("disabled");
				}
				
			}
			
		);

		// desligandos os <input>s de opções desabilitadas
		parent.select("input").each(
			
			function(input) {
				if (!input.checked) {
					input.disabled = true;
				}
			}
			
		);
	
	}

};
