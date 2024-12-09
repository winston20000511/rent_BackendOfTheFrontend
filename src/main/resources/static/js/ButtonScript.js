const btnPrice = document.getElementById('filter-btn-1')
const btnHomeType = document.getElementById('filter-btn-2')
const btnListPrice = document.getElementById('btn-list-1')
const btnListHomeType=document.getElementById('btn-list-2')
const filter = document.getElementById('filter')


btnPrice.addEventListener('click',()=>{
	// testFetch()
	showList(btnPrice,btnListPrice);
})

btnHomeType.addEventListener('click',()=>{
	showList(btnHomeType,btnListHomeType);
})


document.addEventListener('click',(event)=>{

	const elementToCheck = [btnPrice , btnListPrice , btnHomeType , btnListHomeType]

	if (!elementToCheck.some((ele)=> ele.contains(event.target))){
		closeList(event.target);
	}
	

})

function showList(btn,list){
	closeList(btn);
	const icon = btn.getElementsByClassName("fa-solid")[0];
	
	if (icon.className=== "fa-solid fa-angle-down"){
		icon.className="fa-solid fa-angle-up";
		const rect = btn.getBoundingClientRect()
		list.style.top = `${rect.bottom + window.scrollY}px`;
		list.style.left = `${rect.left + window.scrollX}px`; 
		list.style.display='block';
	}else{
		icon.className="fa-solid fa-angle-down";
	}
}
function closeList(btn){
	const fBtn = document.getElementsByClassName('filter-btn')
	const lBtn = document.getElementsByClassName('btn-list')
	for (let i =0 ; i < fBtn.length; i++)
	{
		if (!fBtn[i].contains(btn))
		fBtn[i].firstElementChild.className="fa-solid fa-angle-down";
		lBtn[i].style.display='none';
	}
}


// const apiUrltest = 'http://localhost:8080/api/test';
// async function testFetch(){
	
// 	fetch(apiUrltest).then(response =>{
// 		if (!response.ok){
// 			throw new Error('Network response');	
// 		}
// 		return response.json();
// 	}).then(data =>{
// 		console.log('Data received:' , data);
// 	}).catch(error =>{
// 		console.error('Three has been a proble with your fetch operation',error)
// 	})
// }