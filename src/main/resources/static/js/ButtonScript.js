const btnPrice = document.getElementById('filter-btn-1')
const btnListPrice = document.getElementById('btn-list-1')
const filter = document.getElementById('filter')


btnPrice.addEventListener('click',()=>{
	const icon = btnPrice.getElementsByClassName("fa-solid")[0];
	if (icon.className==="fa-solid fa-angle-down"){
	    icon.className="fa-solid fa-angle-up";
	    const rect = btnPrice.getBoundingClientRect()
	    btnListPrice.style.top = `${rect.bottom + window.scrollY}px`;
	    btnListPrice.style.left = `${rect.left + window.scrollX}px`; 
	    btnListPrice.style.display='block';

	}else{
	    icon.className="fa-solid fa-angle-down";
	    btnListPrice.style.display='none';
	}
})

