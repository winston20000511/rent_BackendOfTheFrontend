const link = document.querySelector(".link")
const linklist = document.getElementById('link-list-1')
const header = document.getElementById('header')

//link滑鼠移入
link.addEventListener('mouseenter',()=>{
	const rect = window.getComputedStyle(header);
	linklist.style.top = rect.height;
	linklist.style.display='block';
	
})
//link滑鼠移入
link.addEventListener('mouseleave',() =>{
	setTimeout(()=>{
	    if(!linklist.matches(':hover')){
	        linklist.style.display= 'none';
	    }
	},400)
})

//list滑鼠移入
linklist.addEventListener('mouseenter', ()=>{
	linklist.style.display='block';
})

//list滑鼠移出
linklist.addEventListener('mouseleave', ()=>{
	linklist.style.display='none';
})

