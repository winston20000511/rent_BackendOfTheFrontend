const link = document.querySelector('.link');
const list = document.getElementById('list-left-1');

link.addEventListener('mouseenter', ()=>{
    list.style.display='block';
})
link.addEventListener('mouseleave', ()=>{
    setTimeout(()=>{
        if(!list.matches(':hover')){
            list.style.display= 'none';
        }
    },200)
})

list.addEventListener('mouseenter',()=>{
    list.style.display='block';
})

list.addEventListener('mouseleave',()=>{
    list.style.display='none';
})