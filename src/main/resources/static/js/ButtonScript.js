const param ={}

export function init(){
    param.priceList = document.getElementById('btn-list-1')
    param.filter = document.getElementById('filter')

    if (!param.priceList || !param.filter) {
        console.error('部分元素未找到，請檢查 DOM 結構或選擇器');
    }
}

export function buttonClick(event){
    const {priceList , filter} = param;
    const btn = event.currentTarget;
    const icon = btn.getElementsByClassName("fa-solid")[0];
    if (icon.className==="fa-solid fa-angle-down"){
        icon.className="fa-solid fa-angle-up";
        const rect = btn.getBoundingClientRect()
        priceList.style.top = `${rect.bottom + window.scrollY}px`;
        priceList.style.left = `${rect.left + window.scrollX}px`; 
        priceList.style.display='block';

    }else{
        icon.className="fa-solid fa-angle-down";
        priceList.style.display='none';
    }
    

}

function toggleClass(element,classA,classB){

    if (element.className===classA){
        element.className=classB;
    }else if(element.className===classB){
        element.className=classA;
    }

}




// export function buttonEventConfig(){
//     const filterBtn = document.getElementsByClassName("filter-btn");

//     Array.from(filterBtn).forEach(btn => {
//         btn.addEventListener('click',()=>{
//             let i = btn.getElementsByClassName("fa-solid")[0];
//             if (i.className==="fa-solid fa-angle-down"){
//                 i.className="fa-solid fa-angle-up";
//             }else{
//                 i.className="fa-solid fa-angle-down";
//             }
            
//         })
//     });
// }