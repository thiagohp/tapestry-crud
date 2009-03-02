var TreeGrid = {

	/*
	toggleChildren: function(tr) {
	
		tr.descendants().each(
			function(e) {
				e.toggle();
			}
		);
		
	}
	*/

	initialize: function(grid, hash) {
		
		var grid = $(grid);
		var trElements = grid.select("tr").each(
			function (tr, index) {
				var firstTd = tr.firstDescendant();
				firstTd.addClassName("level" + hash.get(index - 1));
			}
		);

	}
		
};
