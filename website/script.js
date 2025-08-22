// Mobile nav toggle
const hamburger=document.getElementById('hamburger');
const navLinks=document.getElementById('nav-links');
if(hamburger){
  hamburger.addEventListener('click',()=>{navLinks.classList.toggle('open');});
}
// Update copyright
const yearEl=document.getElementById('year');
if(yearEl){yearEl.textContent=new Date().getFullYear();}