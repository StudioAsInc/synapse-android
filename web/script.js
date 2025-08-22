(function(){
	const yearEl = document.getElementById('year');
	if (yearEl) yearEl.textContent = new Date().getFullYear();

	const toggle = document.getElementById('themeToggle');
	const key = 'theme';
	const apply = (theme) => {
		if (theme === 'dark') document.documentElement.classList.add('dark');
		else document.documentElement.classList.remove('dark');
	};
	const saved = localStorage.getItem(key);
	if (saved) apply(saved);
	if (toggle) {
		// set initial icon
		toggle.textContent = document.documentElement.classList.contains('dark') ? '🌙' : '☀️';
		toggle.addEventListener('click', () => {
			const current = document.documentElement.classList.contains('dark') ? 'dark' : 'light';
			const next = current === 'dark' ? 'light' : 'dark';
			localStorage.setItem(key, next);
			apply(next);
			toggle.textContent = next === 'dark' ? '🌙' : '☀️';
		});
	}
})();