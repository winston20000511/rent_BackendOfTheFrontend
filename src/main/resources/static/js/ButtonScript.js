const filterBtn = document.getElementsByClassName("filter-btn");

Array.from(filterBtn).forEach(btn => {
    btn.addEventListener('click',()=>{
        let i = btn.getElementsByClassName("fa-solid")[0];
        if (i.className==="fa-solid fa-angle-down"){
            i.className="fa-solid fa-angle-up";
        }else{
            i.className="fa-solid fa-angle-down";
			testFetch();
        }
        
    })
});