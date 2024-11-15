export function listEventConfig(){

    const link = document.querySelector('.link');
    const list = document.getElementById('list-left-1');
    const header = document.getElementById('header');
    
    link.addEventListener('mouseenter', ()=>{
        const rect = window.getComputedStyle(header);
        list.style.top = rect.height;
        list.style.display='block';
    })
    link.addEventListener('mouseleave', ()=>{
        setTimeout(()=>{
            if(!list.matches(':hover')){
                list.style.display= 'none';
            }
        },400)
    })
    
    list.addEventListener('mouseenter',()=>{
        list.style.display='block';
    })
    
    list.addEventListener('mouseleave',()=>{
        list.style.display='none';
    })

}
