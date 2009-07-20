Event.observe(window, "load", function() {
	setTimeout(function() {
		var array = $$('div.t-crud-message');
		if (array.size() == 1) {
			array.first().fade();
		}
	}, 5000);
});