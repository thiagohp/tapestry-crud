var TreeSelect = {

	disableDescendentInputs: function(id) {
	
		// o id é sempre de um <input>, então temos que pegar o pai dele, um <li>
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
				input.disabled = true;
			}
			
		);
	
	}

};
