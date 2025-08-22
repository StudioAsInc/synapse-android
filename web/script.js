document.addEventListener('DOMContentLoaded', function () {
	var year = document.getElementById('year');
	if (year) {
		year.textContent = new Date().getFullYear();
	}

	var links = document.querySelectorAll('a[href^="#"]');
	links.forEach(function (link) {
		link.addEventListener('click', function (e) {
			var targetId = link.getAttribute('href').slice(1);
			var target = document.getElementById(targetId);
			if (target) {
				e.preventDefault();
				target.scrollIntoView({ behavior: 'smooth', block: 'start' });
			}
		});
	});
});